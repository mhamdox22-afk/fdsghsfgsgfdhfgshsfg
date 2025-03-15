<template>
  <div>
    <div class="content">
      <!-- 盘口信息 -->
      <div class="content_left">
        <Handicap :rows="8" :coinInfo="coinInfo" @setTradePrice="setTradePrice"></Handicap>
      </div>
      <!-- 下单 -->
      <div class="content_right">
        <!-- 市价/限价委托  杠杆倍数 -->
        <div class="rightFirst">
          <div class="left firstItem">
            <van-popover v-model:show="showPopover" :show-arrow="false" placement="bottom">
              <div class="rightScondList">
                <!-- 市价委托 -->
                <div
                  class="rightScondListItem"
                  :class="transactionLabel ? 'hightBlue' : ''"
                  @click="onSelect(1)"
                >
                  {{ _t18(`bb_market_order`) }}
                </div>
                <!-- 限价委托 -->
                <div
                  class="rightScondListItem"
                  :class="!transactionLabel ? 'hightBlue' : ''"
                  @click="onSelect(0)"
                >
                  {{ _t18(`bb_Limit_order`) }}
                </div>
              </div>
              <template #reference>
                <div class="entrustSelect left">
                  <div>
                    {{ transactionLabel ? _t18(`bb_market_order`) : _t18(`bb_Limit_order`) }}
                  </div>
                  <svg-load
                    :name="showPopover ? 'jiantou10x5-s' : 'jiantou10x5-x'"
                    class="img"
                  ></svg-load>
                </div>
              </template>
            </van-popover>
          </div>
          <div class="right firstItem">
            <van-popover v-model:show="showPopoverNum" :show-arrow="false" placement="bottom">
              <div class="rightScondListNum" v-if="numList.length > 0">
                <div
                  v-for="item in numList"
                  :key="item.id"
                  class="rightScondListItem"
                  :class="item.name === transactionNum ? 'hightBlue' : ''"
                  @click="selectNum(item)"
                >
                  {{ item.name }}
                </div>
              </div>
              <template #reference>
                <div class="entrustSelect right">
                  <div class="fw-num">{{ transactionNum }}</div>
                  <svg-load
                    :name="showPopoverNum ? 'jiantou10x5-s' : 'jiantou10x5-x'"
                    class="img"
                  ></svg-load>
                </div>
              </template>
            </van-popover>
          </div>
        </div>
        <!-- 市价占位 -->
        <div class="marketPrice" v-if="transactionLabel">{{ _t18(`market_price`) }}</div>
        <!-- 市价委托：数量(手lots/张lots2) -->
        <div class="inputField" v-if="transactionLabel">
          <input
            type="number"
            v-model.trim="delegateTotal"
            class="amount"
            :placeholder="`${_t18(`metastaking_bb_count`)}(${_t18(`lots`)})`"
          />
        </div>
        <!-- 限价委托：价格，数量 -->
        <div class="inputField" v-if="!transactionLabel">
          <!-- 价格 -->
          <input
            type="number"
            class="amount"
            v-model.trim="delegatePrice"
            :placeholder="_t18(`price`)"
          />
        </div>
        <div class="inputField" v-if="!transactionLabel">
          <!-- 数量(手lots/张lots2) -->
          <input
            type="number"
            v-model.trim="delegateTotal"
            class="amount"
            :placeholder="`${_t18(`metastaking_bb_count`)}(${_t18(`lots`)})`"
          />
        </div>
        <!-- slider滑动条 -->
        <div class="sliderContainer">
          <!-- 百分比显示 -->
          <div class="percentage">{{ sliderValue }}%</div>
          
          <!-- 使用Vant滑块，但自定义按钮样式 -->
          <van-slider 
            v-model="sliderValue" 
            active-color="#00cc7a" 
            inactive-color="#333333"
          >
            <template #button>
              <div class="sliderDiamond"></div>
            </template>
          </van-slider>
        </div>
        <!-- 可用 -->
        <div class="balanceInfo">
          <div>{{ _t18(`account_available`) }}</div>
          <div class="number">{{ availableBalance }} USDT</div>
        </div>
        <!-- 买入(做多)按钮 -->
        <div class="actionButtonWrapper">
          <div class="buyButton" @click="buyOrSell(0)">
            {{ _t18(`purchase`) }} ({{ _t18(`going_long`) }})
          </div>
        </div>
        <!-- 交易信息区域 - 买入 -->
        <div class="tradeInfoSection">
          <!-- 可开空 -->
          <div class="tradeInfoItem">
            <div class="label">{{ _t18(`openable`, ['aams']) }}</div>
            <div class="value">
              {{ bearableValue ? _toFixed(bearableValue, 4) : 0 }}
              {{
                coinInfo.customizeFlag === 2
                  ? matchText(coinInfo.showSymbol, '/USDT')
                  : coinInfo.coin?.toUpperCase()
              }}
            </div>
          </div>
          <!-- 保证金 -->
          <div class="tradeInfoItem">
            <div class="label">{{ _t18(`cash_deposit`) }}</div>
            <div class="value">{{ _toFixed(marginValue, 4) }} USDT</div>
          </div>
          <!-- 合约面值 -->
          <div class="tradeInfoItem">
            <div class="label">{{ _t18(`contract_face_value`, ['aams']) }}</div>
            <div class="value">
              {{ coinInfo.shareNumber }}
              {{
                coinInfo.customizeFlag === 2
                  ? matchText(coinInfo.showSymbol, '/USDT')
                  : coinInfo.coin?.toUpperCase()
              }}
              /{{ _t18(`lots`) }}
            </div>
          </div>
        </div>
        <!-- 卖出(开空)按钮 -->
        <div class="actionButtonWrapper">
          <div class="sellButton" @click="buyOrSell(1)">
            {{ _t18(`bb_sell1`) }} ({{ _t18(`open_short`) }})
          </div>
        </div>
        <!-- 交易信息区域 - 卖出 -->
        <div class="tradeInfoSection">
          <!-- 可开空 -->
          <div class="tradeInfoItem">
            <div class="label">{{ _t18(`openable`, ['aams']) }}</div>
            <div class="value">
              {{ bearableValue ? _toFixed(bearableValue, 4) : 0 }}
              {{
                coinInfo.customizeFlag === 2
                  ? matchText(coinInfo.showSymbol, '/USDT')
                  : coinInfo.coin?.toUpperCase()
              }}
            </div>
          </div>
          <!-- 保证金 -->
          <div class="tradeInfoItem">
            <div class="label">{{ _t18(`cash_deposit`) }}</div>
            <div class="value">{{ _toFixed(marginValue, 4) }} USDT</div>
          </div>
          <!-- 合约面值 -->
          <div class="tradeInfoItem">
            <div class="label">{{ _t18(`contract_face_value`, ['aams']) }}</div>
            <div class="value">
              {{ coinInfo.shareNumber }}
              {{
                coinInfo.customizeFlag === 2
                  ? matchText(coinInfo.showSymbol, '/USDT')
                  : coinInfo.coin?.toUpperCase()
              }}
              /{{ _t18(`lots`) }}
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="divider"></div>
    <!-- 订单信息 -->
    <OrderListBox
      ref="orderListBoxRef"
      :coinInfo="coinInfo"
      :availableBalance="availableBalance"
    ></OrderListBox>
  </div>
</template>

<script setup>
import { _mul, _div, _toFixed } from '@/utils/decimal'
import { DIFF_ISFREEZE } from '@/config/index'
import { useFreeze } from '@/hook/useFreeze'
const { _isFreeze } = useFreeze()
import Handicap from '../../common/handicap.vue'
import OrderListBox from './OrderList.vue'
import { _t18 } from '@/utils/public'
import { showToast, showLoadingToast, closeToast } from 'vant'
import { useToast } from '@/hook/useToast'
const { _toast } = useToast()
import { useMainStore } from '@/store/index'
const mainStore = useMainStore()
import { matchText, formatCurrentcurrency } from '@/utils/filters'
import {
  submitUcontract,
  contractHistoryList,
  contractLossList,
  orderList
} from '@/api/trade/index'
import { useTradeStore } from '@/store/trade'
const tradeStore = useTradeStore()

import { useUserStore } from '@/store/user/index'
const userStore = useUserStore()
const { asset } = storeToRefs(userStore)
import { socketDict } from '@/config/dict'
import PubSub from 'pubsub-js'
const orderListBoxRef = ref(null)
const props = defineProps({
  coinInfo: {
    type: Object,
    default: () => {}
  }
})
const coinPriceInfo = computed(() => {
  return tradeStore.allCoinPriceInfo[props.coinInfo.coin] || {}
})
const delegateTotal = ref('') // 数量（市价/限价）
const delegatePrice = ref('') // 价格（限价）
const sliderValue = ref(0) //滑动
//可用余额
const availableBalance = computed(() => {
  var cur = asset.value.filter((item) => {
    return item.type === 3
  })

  if (cur.length > 0) {
    return cur[0].availableAmount
  } else {
    return ''
  }
})

const marginValue = ref('0') //保证金
/**
 * 市价/限价委托
 */
const showPopover = ref(false) // 委托类型
const transactionLabel = ref(1) // 市价  1  限价 0
const onSelect = (item) => {
  transactionLabel.value = item
  showPopover.value = false
  delegateTotal.value = ''
  delegatePrice.value = ''
  sliderValue.value = 0
}

// 限价委托：设置交易价格
const setTradePrice = (val) => {
  delegatePrice.value = !transactionLabel.value ? val : ''
}
const contractObj = ref({}) // 币种对象
/**
 * 杠杆倍数
 */
const showPopoverNum = ref(false) //弹窗
const transactionNum = ref(null) //倍数
const numList = ref([]) //杠杆下拉
// 切换杠杆倍数
const selectNum = (item) => {
  transactionNum.value = item.name
  showPopoverNum.value = false
}
// 切换币种：重置价格、数量，杠杆变化
watch(
  () => props.coinInfo.coin,
  (n) => {
      delegateTotal.value = ''
      setTradePrice()
      if (tradeStore.contractCoinList.length) {
        numList.value = []
        transactionNum.value = ''
        let newcoin = {}
        tradeStore.contractCoinList.forEach((element) => {
          if (element.coin === n) {
            contractObj.value = element
            newcoin = element
            return
          }
        })
        if (newcoin?.leverage) {
          newcoin.leverage.split(',').forEach((ele) => {
            numList.value.push({
              name: ele + 'X',
              id: ele
            })
          })
        }
        transactionNum.value = numList.value[0].name
      }
  }
)
// 监听滑块，修改数量（手）
watch(
  () => sliderValue.value,
  (n) => {
    console.log(n)
    let priceNew
    if (transactionLabel.value) {
      // 市价委托
      priceNew = coinPriceInfo.value.close
    } else {
      // 限价委托
      priceNew = delegatePrice.value
    }
    if (!priceNew) {
      sliderValue.value = 0
      return
    }
    let numberSlider
    if (['das'].includes(__config._APP_ENV)) {
      // 可用余额/合约面值
      // availableBalance/shareNumber
      numberSlider = _div(availableBalance.value, props.coinInfo.shareNumber)
    } else {
      // 可用余额*杠杆倍数/可用余额市价或输入价格/合约面值
      // availableBalance*transactionNum/priceNew/shareNumber
      numberSlider = _div(
        _div(
          _mul(
            availableBalance.value,
            transactionNum.value.substr(0, transactionNum.value.length - 1)
          ),
          priceNew
        ),
        props.coinInfo.shareNumber
      )
    }
    // 向下取整
    delegateTotal.value = Math.floor(_mul(numberSlider, _div(n, 100)))
  }
)
//监听数量，计算保证金
watch(
  () => delegateTotal.value,
  (n) => {
    if (n) {
      const regex = /^[1-9]\d*$/
      if (!regex.test(n)) {
        // 数量只能输入整数
        return _toast('quantitycan_onlyenter_integers')
      } else {
        var typeId = ''
        numList.value.forEach((element) => {
          if (element.name === transactionNum.value) {
            return (typeId = element.id)
          }
        })

        if (['das'].includes(__config._APP_ENV)) {
          // das平台：保证金=手*合约面值
          // delegateTotal.value*contractObj.value.shareNumber
          marginValue.value = _mul(delegateTotal.value, contractObj.value.shareNumber)
        } else {
          //保证金=价格*数量*合约面值/杠杆倍数
          // coinPriceInfo.value.close*n*contractObj.value.shareNumber/typeId
          marginValue.value = _toFixed(
            _div(_mul(_mul(coinPriceInfo.value.close, n), contractObj.value.shareNumber), typeId),
            6
          )
        }
      }
    } else {
      marginValue.value = '0'
    }
  }
)
// 监听价格
watch(
  () => delegatePrice.value,
  (n) => {
    if (n) {
      if (!/^[1-9]\d*(\.\d{1,6})?$|^0(\.\d{1,6})?$/.test(n) || Number(n) === 0) {
        // 请输入最多保留6位小数的大于0的价格
        return _toast('Please_entera_price_greater')
      }
    } else {
      sliderValue.value = 0
      delegateTotal.value = ''
    }
  }
)
// 监听杠杆倍数
watch(
  () => transactionNum.value,
  (n) => {
    const CurN = n.substr(0, n.length - 1)
    let priceNew
    if (transactionLabel.value) {
      priceNew = coinPriceInfo.value.close
    } else {
      priceNew = delegatePrice.value
    }
    if (!priceNew) {
      sliderValue.value = 0
      return
    }
    let numberSlider = _div(
      _div(_mul(availableBalance.value, CurN), priceNew),
      props.coinInfo.shareNumber
    )
    // // 向下取整
    if (!['das'].includes(__config._APP_ENV)) {
      delegateTotal.value = Math.floor(_mul(numberSlider, _div(sliderValue.value, 100)))
    }
  }
)
// 可开空=可用/价格*杠杆倍数
// das可开空=合约面值*手/价格*杠杆
const bearableValue = computed(() => {
  let price = transactionLabel.value ? coinPriceInfo.value.close : delegatePrice.value
  if (price) {
    var typeId = ''
    numList.value.forEach((element) => {
      if (element.name === transactionNum.value) {
        return (typeId = element.id)
      }
    })
    let result
    if (['das'].includes(__config._APP_ENV)) {
      result = _mul(_div(_mul(delegateTotal.value, contractObj.value.shareNumber), price), typeId)
    } else {
      result = _mul(_div(availableBalance.value, price), typeId)
    }
    return result
  } else {
    return 0
  }
})
// 初始化杠杆倍数
const init = () => {
  if (tradeStore.contractCoinList.length) {
    numList.value = []
    transactionNum.value = ''
    tradeStore.contractCoinList.forEach((element) => {
      if (element.coin === props.coinInfo.coin) {
        contractObj.value = element
        if (element.leverage) {
          element.leverage.split(',').forEach((element) => {
            numList.value.push({
              name: element + 'X',
              id: element
            })
          })
        }
        transactionNum.value = numList.value[0].name
        return
      }
    })
  }
}

/**
 * 买入/卖出按钮
 */
const buyOrSellForm = (type) => {
  // 杠杆倍数
  var typeId = ''
  numList.value.forEach((element) => {
    if (element.name === transactionNum.value) {
      return (typeId = element.id)
    }
  })
  let data = {
    symbol: props.coinInfo.coin, //币种
    leverage: typeId, //杠杆倍数
    delegatePrice: transactionLabel.value ? coinPriceInfo.value.close : delegatePrice.value, //价格
    delegateTotal: delegateTotal.value, //数量
    type: type, //0买入 1卖出
    delegateType: transactionLabel.value //委托类型0市价 1限价
  }
  if (
    !/^[1-9]\d*(\.\d{1,6})?$|^0(\.\d{1,6})?$/.test(data.delegatePrice) ||
    Number(data.delegatePrice) === 0
  ) {
    // 请输入最多保留6位小数的大于0的价格
    return _toast('Please_entera_price_greater')
  }
  if (!/^[1-9]\d*$/.test(data.delegateTotal)) {
    // 数量只能输入整数
    return _toast('quantitycan_onlyenter_integers')
  }
  try {
    showLoadingToast({
      forbidClick: true,
      duration: 0
    })
    submitUcontract(data).then((res) => {
      if (res.code === 200) {
        // !type ? '买入' : '卖出'}成功`
        _toast('quote_addSuccess')
        // 刷新订单
        orderListBoxRef.value.handelRefresh()
        userStore.getUserInfo()
      } else {
        closeToast()
        showToast(res.msg)
      }
    })
  } catch (error) {
    closeToast()
  }
}
const buyOrSell = (type) => {
  if (DIFF_ISFREEZE.includes(__config._APP_ENV)) {
    if (_isFreeze(DIFF_ISFREEZE)) {
      buyOrSellForm(type)
    }
  } else {
    buyOrSellForm(type)
  }
}

onMounted(() => {
  // init()
})
// 当前持仓 当前委托 止盈止损 历史委托
const trusetList = ref([
  {
    // name: ['coinsexpto'].includes(__config._APP_ENV)?_t18(`current_position2`):_t18(`current_position`,['latcoin']),
    name: _t18(`current_position`, ['latcoin']),
    num: 0,
    show: true
  },
  {
    name: _t18(`exchange_curentrust`, ['bitmake', 'ebc', 'latcoin']),
    num: 0,
    show: true
  },
  {
    name: _t18(`profit_loss`, ['paxpay', 'latcoin', 'aams']),
    num: 0,
    show: true
  },
  {
    // name: _t18(`exchange_hisentrust`, ['bitmake', 'ebc','latcoin']),
    name: ['coinsexpto'].includes(__config._APP_ENV)
      ? _t18(`exchange_hisentrust2`)
      : _t18(`exchange_hisentrust`, ['bitmake', 'ebc', 'latcoin']),
    num: 0,
    show: true
  }
])

/**
 * 订单模块
 */

/** 更新user */
const updateUser = () => {
  userStore.getUserInfo()
}
/**
 * 订单结算，更新订单和用户信息
 */
const coinOverKey = ref('')
const settlementKey = ref('')

const coinOverNotification = () => {
  /**
   * rxce平台周期改变通知
   */
  coinOverKey.value = PubSub.subscribe(socketDict.POSITION, (key, data) => {
    const { position } = JSON.parse(data.date.message)
    if (position) {
      orderListBoxRef.value.init()
      updateUser()
    }
  })
  /**
   * 结算改变通知
   */
  settlementKey.value = PubSub.subscribe(socketDict.SETTLEMENT, (key, data) => {
    const { type } = JSON.parse(data.date.message)
    if (type == 2) {
      orderListBoxRef.value.init()
      updateUser()
    }
  })
}
coinOverNotification()

onUnmounted(() => {
  settlementKey && PubSub.unsubscribe(settlementKey)
  coinOverKey && PubSub.unsubscribe(coinOverKey)
})
</script>

<style lang="scss" scoped>
.hightBlue {
  color: #00cc7a !important;
}

.content {
  display: flex;
  padding: 15px;
  background: #121212;
}

.content_right {
  flex: 1;
  background: #121212;

  .rightFirst {
    display: flex;
    justify-content: space-between;
    gap: 10px;
    margin-bottom: 12px;

    .firstItem {
      background: #1e1e1e;
      border-radius: 8px;
      flex: 1;
    }

    .entrustSelect {
      color: #ffffff;
      padding: 0 12px;
      height: 36px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      
      .img {
        width: 10px;
        height: 5px;
        margin-left: 5px;
      }
    }
  }
}

// 市场价格显示
.marketPrice {
  width: 100%;
  height: 42px;
  background: #1e1e1e;
  border-radius: 4px;
  font-size: 14px;
  color: #7c7c7c;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 12px;
}

// 输入字段
.inputField {
  margin-bottom: 12px;
  
  .amount {
    width: 100%;
    height: 42px;
    background: #1e1e1e;
    border-radius: 4px;
    border: none;
    color: #ffffff;
    text-align: center;
    font-size: 14px;
    
    &:focus {
      outline: none;
    }
    
    &::placeholder {
      color: #555555;
    }
  }
}

// 滑块容器
.sliderContainer {
  position: relative;
  margin: 25px 0 15px;
  height: 30px;
  
  // 百分比显示
  .percentage {
    position: absolute;
    width: 100%;
    text-align: center;
    top: -20px;
    font-size: 14px;
    color: #00cc7a;
    font-weight: 500;
  }
  
  // 滑块样式覆盖
  :deep(.van-slider) {
    margin-top: 15px;
    height: 2px;
    background-color: #333333;
    
    .van-slider__button-wrapper {
      height: 16px;
      width: 16px;
    }
    
    .van-slider__bar {
      background: #00cc7a;
      height: 2px;
    }
  }
  
  // 菱形滑块按钮
  .sliderDiamond {
    width: 16px;
    height: 16px;
    background: #00cc7a;
    transform: rotate(45deg);
  }
}

// 可用余额信息
.balanceInfo {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 13px;
  color: #7c7c7c;
  margin-bottom: 12px;
  
  .number {
    color: #ffffff;
    font-weight: 500;
  }
}

// 按钮样式
.actionButtonWrapper {
  margin-bottom: 12px;
  
  .buyButton, .sellButton {
    height: 36px;
    border-radius: 23px;
    font-size: 15px;
    font-weight: 500;
    color: #ffffff;
    display: flex;
    justify-content: center;
    align-items: center;
  }
  
  .buyButton {
    background: #00cc7a;
  }
  
  .sellButton {
    background: #ff5757;
  }
}

// 交易信息区域
.tradeInfoSection {
  margin-bottom: 15px;
  
  .tradeInfoItem {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 8px;
    
    .label {
      font-size: 13px;
      color: #7c7c7c;
    }
    
    .value {
      font-size: 13px;
      color: #ffffff;
      font-weight: 500;
      text-align: right;
    }
  }
}

// 下拉菜单
.rightScondList, .rightScondListNum {
  background-color: #1e1e1e;
  padding: 6px 0;
  font-size: 13px;
  color: #7c7c7c;
  border-radius: 4px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  
  .rightScondListItem {
    padding: 8px 12px;
    text-align: center;
    
    &:hover {
      background: rgba(255, 255, 255, 0.05);
    }
  }
}

// 分割线
.divider {
  height: 8px;
  background: #1e1e1e;
}
</style>
