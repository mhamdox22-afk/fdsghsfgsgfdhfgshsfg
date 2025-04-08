<script setup>
import { getKlineHistory } from '@/api/common/kline.js'
import Datafees from '@/config/kline/datafees.js'
import { getConfig, getIntervalList, getStudyList } from '@/config/kline/index.js'
import { _coinWebSocket } from '@/plugin/socket/index'
import { _t18, _klineTimeFormat } from '@/utils/public'
import { socketDict } from '@/config/dict'
import PubSub from 'pubsub-js'
import { _add, _div, _mul, priceFormat } from '@/utils/decimal'
import _ from 'lodash'
import { computed, watch } from 'vue'
import { useMainStore } from '@/store'
import { useTradeStore } from '@/store/trade'
import { debounce, throttle } from 'lodash'
import {useRouter} from "vue-router";

const router = useRouter()

const props = defineProps({
  /**
   * 类型
   * secondContract：秒合约
   * detail：详情
   */
  type: {
    type: String,
    default: 'detail'
  },

  /**
   * 商品信息
   */
  coinInfo: {
    type: Object,
    default: () => {}
  }
})
/**
 * 分辨率(k线图右侧刻度)
 */
const priceScale = computed(() => {
  let tempPricescale = 10000
  if (props.coinInfo?.amount > 100) {
    tempPricescale = 100
  } else if (props.coinInfo?.amount < 0.1) {
    tempPricescale = 1000000
  } else if (props.coinInfo?.amount < 100) {
    tempPricescale = 10000
  }
  return tempPricescale
})
/**
 * 临时交易(打开页面币种初始信息)
 */
const tempTrade = {
  time: '',
  amount: '',
  open: '',
  high: '',
  low: '',
  close: '',
  volume: '',
  lastClose: '',
  intervention: false
}
/**
 * 分辨率差
 */
const intervalDiff = ref('')
/**
 * 总分辨率列表
 */
const intervalList = getIntervalList()
/**
 * 指标列表
 */
const studyList = getStudyList()

const headIntervalList = reactive([])
const menuIntervalList = intervalList.filter((elem) => elem?.isMore)

/**
 * K线使用 分辨率列表
 */
let supportedResolutions = []
/**
 * 数据sdk实例
 */
let datafeeds = null
// 指标
let studies = reactive([])

/**
 * 当前symbol 切换时过渡
 */
let currentCoinInfo = reactive({})
/**
 * 当前interval 切换时过渡
 */
let currentInterval = reactive({})

/**
 * KLine
 */
const klineId = computed(() => `kline_${props.type}`)

let widget = null
const mainStore = useMainStore()
const tradeStore = useTradeStore()

/**
 * 获取分辨率
 */
const getSupportedResolutions = (tempCoinInfo = currentCoinInfo) => {
  let tempList = intervalList.map((elem) => elem.interval)
  if (tempCoinInfo?.market == 'mt5') {
    tempList = intervalList.filter((elem) => elem.market == 'mt5').map((elem) => elem.interval)
  }
  return tempList
}

/**
 * 获取分辨率显示菜单
 */
const getHeadIntervalList = (tempCoinInfo = currentCoinInfo) => {
  let tempList = intervalList.filter((elem) => !elem?.isMore)
  if (tempCoinInfo?.market == 'mt5' || tempCoinInfo?.coinType === 3) {
    tempList = intervalList.filter((elem) => elem.market == 'mt5')
  }
  return tempList
}

/**
 * 交易对监听 - Optimize the debounce function
 */
const eventTradeSymbolChange = debounce((e) => {
  // Early return if no valid data
  if (!e?.detail?.symbol || !e?.detail?.coinInfo) return;
  
  // 监听币种切换
  let symbol = e.detail.symbol;
  let tempCoinInfo = e.detail.coinInfo;
  
  // 分辨率改变
  let tempHeadIntervalList = getHeadIntervalList(tempCoinInfo);

  // Only update if the interval list has changed
  const newIntervalString = tempHeadIntervalList.map(elem => elem.value).join();
  const currentIntervalString = headIntervalList.map(elem => elem.value).join();
  
  if (newIntervalString !== currentIntervalString) {
    supportedResolutions = getSupportedResolutions();
    headIntervalList.splice(0, headIntervalList.length, ...tempHeadIntervalList);
    Object.assign(currentInterval, headIntervalList[0]);
  }
  
  showMenu.value = false;
  
  // 设置币种
  setSymbol(symbol, currentInterval.interval, () => {
    Object.assign(currentCoinInfo, props.coinInfo);
  });
}, 300);

onMounted(async () => {
  // Initialize once with a delay to allow the DOM to render
  Object.assign(currentCoinInfo, props.coinInfo);
  supportedResolutions = getSupportedResolutions();
  headIntervalList.splice(0, headIntervalList.length, ...getHeadIntervalList());
  Object.assign(currentInterval, headIntervalList[0]);
  
  // Add event listener
  document.addEventListener('event_tradeSymbolChange', eventTradeSymbolChange);
  
  // Delayed initialization for better initial rendering
  setTimeout(() => {
    initWidget();
  }, 100);
});

onBeforeUnmount(() => {
  // Clean up all resources
  document.removeEventListener('event_tradeSymbolChange', eventTradeSymbolChange);
  unsubscribeTrades(true);
  if (widget) {
    widget.remove();
    widget = null;
  }
});

/**
 * 设置币种、周期
 */
const setSymbol = async (symbol, interval, callBack) => {
  if (!widget) return;
  
  if (currentCoinInfo.symbol !== symbol || interval !== currentInterval.interval) {
    return new Promise((resolve) => {
      widget.setSymbol(symbol, interval, () => {
        Object.assign(
          currentInterval,
          intervalList.find((elem) => elem.interval === interval)
        );
        callBack?.();
        resolve();
      });
    });
  }
};

// 数据实例
const dataFeedInstance = {}
/**
 * 获取配置
 */
dataFeedInstance.getConfig = async (callback) => {
  setTimeout(() => {
    callback({
      // 分辨率
      supported_resolutions: supportedResolutions,
      supports_marks: false,
      supports_timescale_marks: true,
      supports_time: true
    })
  }, 0)
}
/**
 * 获取服务器时间
 */
dataFeedInstance.getServerTime = async (callback) => {
  let time = +new Date()
  callback && callback(time)
}
/**
 * 获取历史KLine数据 - 优化数据处理
 */
dataFeedInstance.getBars = async ({ symbolInfo: coinInfo, resolution, from, firstDataRequest }) => {
  let tempInterval = intervalList.find((elem) => elem.interval == resolution);
  
  // Return early if no valid interval or from time
  if (!tempInterval || !(from === '' || from > 0)) {
    return [];
  }
  
  try {
    let params = {
      symbol: coinInfo.coinUpperCase,
      interval: tempInterval.key,
      limit: firstDataRequest ? 500 : 1000 // Reduce initial load size
    };
    
    if (from) {
      params.end = from;
    }
    
    // Fetch data with timeout to prevent hanging
    const fetchPromise = getKlineHistory({
      ...params,
      interval: tempInterval.key,
      symbol: coinInfo.coinUpperCase,
      market: coinInfo.market
    });
    
    const timeoutPromise = new Promise((_, reject) => 
      setTimeout(() => reject(new Error('Request timeout')), 10000)
    );
    
    const { data } = await Promise.race([fetchPromise, timeoutPromise]);
    
    // Process data only if valid
    if (!data) return [];
    
    // Update store
    tradeStore.setKlineTicker(data.ticker);
    
    // Process history data more efficiently
    let barList = data.historyKline || [];
    
    // Use map but avoid creating new objects repeatedly in the loop
    barList = barList.map((elem) => ({
      open: parseFloat(elem.o),
      high: parseFloat(elem.h),
      low: parseFloat(elem.l),
      close: parseFloat(priceFormat(elem.c)),
      amount: parseFloat(priceFormat(elem.c)),
      volume: parseFloat(elem.v),
      time: elem.T
    })).sort((a, b) => a.time - b.time);
    
    // Only initialize tempTrade on first request
    if (firstDataRequest && barList.length > 0) {
      const tempObj = barList[barList.length - 1];
      const prevObj = barList.length > 1 ? barList[barList.length - 2] : null;
      
      // Batch update tempTrade to reduce reactivity overhead
      Object.assign(tempTrade, {
        amount: tempObj.amount,
        open: tempObj.open,
        close: tempObj.close,
        high: tempObj.high,
        low: tempObj.low,
        volume: tempObj.volume,
        time: tempObj.time,
        lastClose: tempObj.close,
        intervention: false
      });
      
      // Calculate interval difference only if we have previous data
      if (prevObj) {
        intervalDiff.value = Math.abs(tempObj.time - prevObj.time);
      }
      
      // Update and subscribe
      updateDataKline(tempTrade);
      await subscribeTrades({
        coin: coinInfo.coin,
        symbol: coinInfo.symbol,
        interval: tempInterval.key,
        firstDataRequest: firstDataRequest
      });
    }
    
    return barList;
  } catch (error) {
    console.error('Error fetching kline data:', error);
    unsubscribeTrades(true);
    return [];
  }
};

/**
 * 加载商品配置
 */
dataFeedInstance.resolveSymbol = async () => {
  return {
    // 商品名称
    name: props.coinInfo.symbolUpperCase,
    // 自定义携带参数
    coin: props.coinInfo.coin,
    coinUpperCase: props.coinInfo.coin,
    symbol: props.coinInfo.symbol,
    symbolUpperCase: props.coinInfo.symbolUpperCase,
    market: props.coinInfo.market,
    // resolution: '1m',
    fractional: false,
    //设置周期
    session: '24x7',
    // 显示商品是否具有历史数据
    has_intraday: true,
    //设置是否支持周月线
    has_weekly_and_monthly: true,
    // 布尔值显示商品是否具有以日为单位的历史数据
    // has_daily: true,
    // 交易所名称
    exchange: __config._APP_EXCHANGE_NAME,
    //设置是否支持周月线
    description: props.coinInfo.symbolUpperCase,
    //设置价格精度  100表示保留两位小数   1000三位   10000四位
    pricescale: priceScale.value,
    // 最小波动
    minmov: 1 / priceScale.value,
    // 分辨率数组
    supported_resolutions: supportedResolutions
  }
}

/**
 * 初始化图表
 */
const initWidget = () => {
  if (widget) {
    widget.remove();
    widget = null;
  }

  datafeeds = new Datafees(dataFeedInstance);
  
  // 主题
  let theme = 'dark' // 强制使用深色主题
  
  // Use shallowRef to avoid deep reactivity overhead
  const widgetOptions = {
    symbol: props.coinInfo.symbolUpperCase,
    theme: 'dark',
    debug: false,
    autosize: true,
    interval: currentInterval.interval,
    container_id: klineId.value,
    datafeed: datafeeds,
    library_path: '/charting_library/',
    locale: 'en',
    timezone: mainStore.timezone,
    
    // Reduce features to improve performance
    disabled_features: [
      "header_symbol_search",
      "header_settings",
      "header_compare",
      "header_undo_redo",
      "timeframes_toolbar",
      "left_toolbar", 
      "control_bar",
      "popup_hints",
      "go_to_date",
      "volume_force_overlay",
      "chart_property_page_trading",
      "property_pages",
      "high_density_bars", // Disable high density bars
      "animated_zoom", // Disable animations
      "countdown" // Disable countdown
    ],
    
    // Enable necessary features
    enabled_features: [
      "hide_left_toolbar_by_default",
      "use_localstorage_for_settings",
      "create_volume_indicator_by_default",
      "show_price_scale_on_right",
      "show_ohlc_values",
      "legend_widget",
      "uppercase_instrument_names",
      "no_legend_on_touch_devices",
      "simple_legend_context_menu"
    ],
    
    // Chart appearance
    overrides: {
      // Background & Grid
      "paneProperties.background": "#131316",
      "paneProperties.vertGridProperties.color": "rgba(255, 255, 255, 0.05)",
      "paneProperties.horzGridProperties.color": "rgba(255, 255, 255, 0.05)",
      
      // Price Scale
      "scalesProperties.textColor": "#ffffff",
      "scalesProperties.backgroundColor": "#131316",
      "scalesProperties.lineColor": "rgba(255, 255, 255, 0.1)",
      
      // OHLC Display Format
      "mainSeriesProperties.statusViewStyle.symbolTextSource": "ticker",
      "mainSeriesProperties.statusViewStyle.showExchange": false,
      "mainSeriesProperties.statusViewStyle.showSeriesTitle": false,
      "mainSeriesProperties.statusViewStyle.priceSource": "close",
      
      // Candle Colors
      "mainSeriesProperties.candleStyle.upColor": "#26a69a",
      "mainSeriesProperties.candleStyle.downColor": "#ef5350",
      "mainSeriesProperties.candleStyle.drawWick": true,
      "mainSeriesProperties.candleStyle.drawBorder": true,
      "mainSeriesProperties.candleStyle.borderUpColor": "#26a69a",
      "mainSeriesProperties.candleStyle.borderDownColor": "#ef5350",
      "mainSeriesProperties.candleStyle.wickUpColor": "#26a69a",
      "mainSeriesProperties.candleStyle.wickDownColor": "#ef5350",
      
      // Volume display
      "volumePaneSize": "medium",
      "volume.volume.color.0": "rgba(239, 83, 80, 0.5)",
      "volume.volume.color.1": "rgba(38, 166, 154, 0.5)",
      
      // Legend configuration to match screenshot
      "paneProperties.legendProperties.showLegend": true,
      "paneProperties.legendProperties.showSeriesOHLC": true,
      "paneProperties.legendProperties.showStudyValues": true,
      "paneProperties.legendProperties.showStudyTitles": true,
      "paneProperties.legendProperties.showStudyArguments": true,
      "paneProperties.legendProperties.showSeriesTitle": false,
      "paneProperties.legendProperties.showBarChange": false,
      
      // Moving Average style
      "MovingAverage.linewidth": 1,
      "MovingAverage.plottype": 0, // Line
    },
    
    // Formatting configuration
    customFormatters: {
      dateFormatter: {
        format(date) {
          return _klineTimeFormat(date.getTime(), 'DD/MM/YYYY', true)
        }
      },
      timeFormatter: {
        format(date) {
          return _klineTimeFormat(date.getTime(), 'HH:mm:ss', true)
        }
      }
    },
    
    loading_screen: {
      backgroundColor: "#131316",
      foregroundColor: "#2962FF"
    },
    
    preset: "mobile"
  };
  
  return new Promise((resolve) => {
    widget = new TradingView.widget(widgetOptions);
    
    widget.onChartReady(() => {
      addMultipleMovingAverages();
      resolve();
    });
  });
}

/**
 * 创建指标
 */
const createStudy = () => {
  // let id = widget.chart().createStudy('volume', false, false, [5], null)
  // studies.push(id)
  console.log(studyList[0])
  let id = setStudy(studyList[0].name)
  studies.push(id)
}
/**
 * 订阅客户端列表
 */
const subscribeClientList = []
/**
 * 取消订阅
 * @param {*} firstDataRequest
 */
const unsubscribeTrades = (firstDataRequest = false) => {
  if (!currentCoinInfo.coin) return; // Early return if no coin
  
  _coinWebSocket.send({
    op: socketDict.unsubscribe,
    type: socketDict.KLINE,
    symbol: currentCoinInfo.coin,
    interval: currentInterval.key
  })
  
  if (firstDataRequest) {
    // Clear all subscriptions at once
    while(subscribeClientList.length > 0) {
      const subKey = subscribeClientList.pop();
      subKey && PubSub.unsubscribe(subKey);
    }
    
    _coinWebSocket.send({
      op: socketDict.unsubscribe,
      type: socketDict.TRADE,
      symbol: currentCoinInfo.coin
    })
  }
}

/**
 * 订阅实时成交 - Optimized to reduce overhead
 */
const subscribeTrades = async (params) => {
  // 先取消订阅
  unsubscribeTrades(params.firstDataRequest)

  // Only subscribe if we have a valid symbol
  if (!params.coin) return;
  
  // Send subscription requests
  _coinWebSocket.send({
    op: socketDict.subscribe,
    type: socketDict.KLINE,
    symbol: params.coin,
    interval: params.interval
  })

  if (params.firstDataRequest) {
    _coinWebSocket.send({
      op: socketDict.subscribe,
      type: socketDict.TRADE,
      symbol: params.coin
    })
  }
  
  // Only handle KLINE updates for better performance
  let candlestickKey = PubSub.subscribe(socketDict.KLINE, (key, data) => {
    // Only process data for the current symbol
    if (data.symbol !== params.coin) return;
    
    let tempData = data.data.tick;
    
    // Update intervention status if needed
    if (tempTrade.intervention !== tempData?.intervention) {
      tempData.open = tempTrade.lastClose;
      tempTrade.intervention = tempData?.intervention;
    }
    
    // Calculate time and update only if needed
    const tempTime = parseInt(tempData.id / intervalDiff.value) * intervalDiff.value;
    if (tempTrade.time < tempTime) {
      tempTrade.time = tempTime;
      tempTrade.open = tempData.open;
    }
    
    // Update trade data
    tempTrade.high = tempData.high;
    tempTrade.low = tempData.low;
    tempTrade.close = Number(priceFormat(tempData.close));
    tempTrade.volume = tempData.vol;
    
    // Use non-throttled update for better responsiveness
    updateDataKline(tempTrade);
  })
  
  subscribeClientList.push(candlestickKey);
}
/**
 * 更新数据
 */
const updateDataKline = (newData) => {
  if (newData?.close) {
    // console.log('更新数据', newData)
    datafeeds.updateData(newData)
    PubSub.publish(socketDict.DETAIL, {
      data: {
        ...newData,
        vol: newData.volume
      },
      origin: 'kline',
      symbol: currentCoinInfo.coin,
      type: socketDict.DETAIL
    })
  }
}
/**
 * 更新数据 限流
 */
const updateDataKlineThrottle = throttle(function (newData) {
  updateDataKline(newData)
}, 300)
/**
 * 显示更多分辨率
 */
const showMenu = ref(false)
/**
 * 选中分辨率
 * @param {*}
 */
const checkedInterval = (item, type = '') => {
  if (item.interval != currentInterval.interval) {
    setSymbol(props.coinInfo.symbol, item.interval)
  }
  showMenu.value = !!type
}

// 指标选择
const currentStudy = ref('')
/**
 * 设置指标
 */
const setStudy = (name) => {
  if (currentStudy.value == name) {
    return
  }
  const activeChart = widget.activeChart()
  // 关闭旧指标
  if (currentStudy.value) {
    let oldStudy = studyList.filter((elem) => elem.name == currentStudy.value)[0]
    oldStudy.list.forEach((elem) => {
      activeChart.removeEntity(elem)
    })
  }
  // 设置新指标
  currentStudy.value = name
  let newStudy = studyList.filter((elem) => elem.name == currentStudy.value)[0]
  let tempList = []
  newStudy.cycleList.forEach(async (cycle, index) => {
    let result = ''
    if (name == 'MACD') {
      result = await activeChart.createStudy(currentStudy.value, false, false, [], {})
    } else if (name == 'Bollinger Bands') {
      result = await activeChart.createStudy(currentStudy.value, false, false, [], {})
    } else {
      result = await activeChart.createStudy(currentStudy.value, false, false, [], {
        'plot.color': newStudy.colorList[index],
        'plot.linewidth': 2
      })
    }
    tempList.push(result)
  })
  newStudy.list = tempList
}

/**
 * 添加多个移动平均线 - 优化性能
 */
const addMultipleMovingAverages = () => {
  // Skip if chart isn't ready
  if (!widget || !widget.activeChart) return;
  
  const activeChart = widget.activeChart();
  
  // Batch removal of studies for better performance
  const studies = activeChart.getAllStudies();
  if (studies && studies.length > 0) {
    studies.forEach(study => {
      activeChart.removeEntity(study.id);
    });
  }
  
  // Add studies one by one with a slight delay to prevent UI blocking
  setTimeout(() => {
    // 黄色MA
    activeChart.createStudy('Moving Average', false, false, [9, "close", 0], {
      'plot.color': '#F0CB35',
      'plot.linewidth': 1,
    });
    
    // Add next study with a small delay
    setTimeout(() => {
      // 青色MA
      activeChart.createStudy('Moving Average', false, false, [9, "close", 0], {
        'plot.color': '#2DA1EC', 
        'plot.linewidth': 1,
      });
      
      // Add final study with a small delay
      setTimeout(() => {
        // 紫色MA
        activeChart.createStudy('Moving Average', false, false, [9, "close", 0], {
          'plot.color': '#8E59F0',
          'plot.linewidth': 1,
        });
        
        // Apply overrides in a single batch
        widget.applyOverrides({
          "symbolWatermarkProperties.color": "rgba(0, 0, 0, 0)",
          "paneProperties.legendProperties.showStudyArguments": true,
          "paneProperties.legendProperties.showStudyValues": true,
          "paneProperties.legendProperties.showStudyTitles": true
        });
      }, 50);
    }, 50);
  }, 100);
}

// 修改 watch 部分
watch(
  () => props.coinInfo,
  async (newCoinInfo, oldCoinInfo) => {
    // 确保新的 coinInfo 有效
    if (!newCoinInfo?.symbol) return;
    
    // 如果是相同的交易对，不需要刷新
    if (oldCoinInfo?.symbol === newCoinInfo.symbol) return;

    try {
      // 1. 先取消所有订阅
      unsubscribeTrades(true);

      // 2. 重置交易数据
      Object.assign(tempTrade, {
        time: '',
        amount: '',
        open: '',
        high: '',
        low: '',
        close: '',
        volume: '',
        lastClose: '',
        intervention: false
      });

      // 3. 更新当前币种信息
      Object.assign(currentCoinInfo, newCoinInfo);

      // 4. 更新分辨率相关配置
      supportedResolutions = getSupportedResolutions(newCoinInfo);
      const tempHeadIntervalList = getHeadIntervalList(newCoinInfo);
      headIntervalList.splice(0, headIntervalList.length, ...tempHeadIntervalList);
      Object.assign(currentInterval, headIntervalList[0]);

      // 5. 隐藏菜单
      showMenu.value = false;

      // 6. 等待图表准备就绪
      if (!widget) {
        // 如果 widget 不存在，重新初始化
        await initWidget();
      } else {
        // 7. 重新设置商品和周期
        await new Promise((resolve) => {
          widget.setSymbol(newCoinInfo.symbol, currentInterval.interval, () => {
            // 8. 重新添加移动平均线
            widget.onChartReady(() => {
              addMultipleMovingAverages();
              resolve();
            });
          });
        });
      }

    } catch (error) {
      console.error('Error refreshing chart:', error);
    }
  },
  { 
    deep: true,
    immediate: true // 改为 true，确保初始化时也执行
  }
);
</script>
<template>
  <div>
    <div class="time-interval-selector">
      <div 
        v-for="(item, index) in headIntervalList" 
        :key="item.interval"
        v-memo="[item.interval === currentInterval.interval]"
        :class="{'active': item.interval === currentInterval.interval, 'interval-item': true}"
        @click="checkedInterval(item)"
      >
        {{ item.value }}
      </div>
    </div>
  </div>
  <div :id="klineId" v-once class="candlestick"></div>
  <div class="indicator-options">
    <!-- <div class="indicator-option">交易</div> -->
    <div 
      v-for="(item, index) in studyList" 
      :key="item.name"
      v-memo="[item.name === currentStudy]"
      :class="{'active': item.name === currentStudy, 'indicator-option': true}"
      @click="setStudy(item.name)"
    >
      {{ item.label }}
    </div>
  </div>
</template>

<style lang="scss" scoped>
.candlestick {
  height: 348px;
  background-color: #131316;
  width: 100%;
  position: relative;
}

.time-interval-selector {
  display: flex;
  background-color: #131316;
  padding: 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
  
  .interval-item {
    flex: 1;
    text-align: center;
    padding: 10px 0;
    color: #8c8c8c;
    font-size: 14px;
    cursor: pointer;
    position: relative;
    
    &.active {
      color: #0DBB7C;
      
      &:after {
        content: '';
        position: absolute;
        bottom: -1px;
        left: 25%;
        width: 50%;
        height: 2px;
        background-color: #0DBB7C;
      }
    }
  }
}

.indicator-options {
  display: flex;
  background-color: #131316;
  padding: 10px 0;
  border-top: 1px solid rgba(255, 255, 255, 0.05);
  align-items: center; /* Vertically center items */
  justify-content: space-around; /* Distribute items evenly */
  
  .indicator-option {
    display: flex;
    align-items: center; /* Vertically center text */
    justify-content: center; /* Horizontally center text */
    padding: 8px 0;
    color: #8c8c8c;
    font-size: 14px;
    cursor: pointer;
    flex: 1; /* Each item takes equal width */
    text-align: center;
    white-space: nowrap; /* Prevent text wrapping */
    
    &.active {
      background-color: rgba(0, 0, 0, 0.35); /* Darker background for active item */
      color: #fff;
      border-radius: 4px;
    }
  }
}
</style>
