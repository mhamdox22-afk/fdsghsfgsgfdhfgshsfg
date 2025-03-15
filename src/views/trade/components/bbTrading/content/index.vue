<!-- 盘口信息 -->
<template>
  <div>
    <div class="content">
      <!-- 盘口信息 -->
      <div class="content_left">
        <Handicap :coinInfo="coinInfo" @setTradePrice="setTradePrice"></Handicap>
      </div>
      <!-- 下单 -->
      <div class="content_right">
        <!-- 买入/卖出切换 -->
        <div class="rightFirst">
          <div
            v-for="(item, index) in tradeTypeList"
            :key="item.label"
            :class="{
              hightColor: form.type == item.value && index == 0,
              hightColorRed: form.type == item.value && index == 1,
              name: true
            }"
            @click="checkedTradeType(item)"
          >
            {{ item.label }}
          </div>
        </div>
        <!-- 市价/限价 rxce卖出隐藏限价 -->
        <div
          class="rightScond"
          v-if="
            !['rxce'].includes(_getConfig('_APP_ENV')) ||
            (['rxce'].includes(_getConfig('_APP_ENV')) && form.type == 0)
          "
        >
          <van-popover v-model:show="showPopover" :show-arrow="false">
            <div class="rightScondList">
              <div
                class="rightScondListItem"
                v-for="item in delegateTypeList"
                :key="item.label"
                :class="{
                  hightBlue: form.delegateType == item.value
                }"
                @click="checkedDelegateType(item)"
              >
                {{ item.label }}
              </div>
            </div>
            <template #reference>
              <div class="entrustSelect">
                <div>{{ currentDelegateType.label }}</div>
                <svg-load  :name="showPopover?'jiantou10x5-s':'jiantou10x5-x'" class="img"></svg-load>
              </div>
            </template>
          </van-popover>
        </div>
        <!-- 占位：'市价' -->
        <div class="rightThird" v-if="form.delegateType == 1">
          {{ currentDelegateType.placeholder }}
        </div>
        <div class="rightFifth" v-if="form.delegateType == 0">
          <!-- 限价>买入，限价>卖出：价格 -->
          <input
            v-model="form.price"
            @input="priceChange"
            type="number"
            class="amount"
            :placeholder="`${_t18(`price`)} (${props.coinInfo.baseCoinUpperCase})`"
          />
        </div>
        <!-- 限价>买入，限价>卖出，市价>卖出：数量 -->
        <div
          class="rightFifth"
          v-if="form.delegateType == 0 || (form.type == 1 && form.delegateType == 1)"
        >
          <input
            v-model="form.count"
            @input="countChange"
            type="number"
            class="amount"
            :placeholder="`${_t18(`metastaking_bb_count`)} (${
              props.coinInfo.customizeFlag === 2
                ? matchText(props.coinInfo.showSymbol, '/USDT')
                : props.coinInfo.coin.toUpperCase()
            })`"
          />
        </div>
        <!-- 滑块部分 -->
        <div class="rightFourth">
          <div class="slider-t">
            <div class="item" v-for="(item, index) in 4" :key="index">
              <div
                class="hightItem"
                :class="{ hightColorRed: form.type == 1 }"
                :style="{
                  width: `${
                    form.slider >= (index + 1) * 25
                      ? '100%'
                      : form.slider < (index + 1) * 25 && form.slider >= index * 25
                      ? (form.slider / 25 - index) * 100 + '%'
                      : '0%'
                  }`
                }"
              ></div>
            </div>
          </div>
          <div class="slider-name ff-num">
            <div
              class="item"
              :class="form.slider >= (index + 1) * 25 ? 'itemHight' : ''"
              v-for="(item, index) in 4"
              :key="index"
              @click="onChange2(index)"
            >
              {{ 25 * (index + 1) }}
            </div>
          </div>
          <div class="rightLine"></div>
          <van-slider
            v-model="form.slider"
            @change="sliderChange"
            active-color="var(--ex-div-bgColor1)"
            inactive-color="var(--ex-div-bgColor)"
            button-size="16"
          ></van-slider>
          <div class="rightLine"></div>
        </div>
        <!-- 市价>买入，限价>买入，限价>卖出：成交金额 -->
        <div class="rightFifth" v-if="!(form.type == 1 && form.delegateType == '1')">
          <input
            v-model="form.turnover"
            @input="turnoverChange"
            type="number"
            class="amount"
            :placeholder="`${_t18(
              ['rxce'].includes(_getConfig('_APP_ENV')) ? `pledge_purchasing_price` : `bb_turnover`
            )} (${props.coinInfo.baseCoinUpperCase})`"
          />
        </div>
        <!-- 数量 (rxce特殊需求：增加展示usdt价格折合成当前币种)-->
        <div
          class="rightSix"
          v-if="['rxce'].includes(_getConfig('_APP_ENV')) && form.type == 0"
        >
          <div>{{ _t18(`buy_bb_count`) }}</div>
          <div class="number fw-num">
            {{ priceFormat(_div(form.turnover, coinPriceInfo.close)) }}
            {{
              props.coinInfo.customizeFlag === 2
                ? matchText(props.coinInfo.showSymbol, '/USDT')
                : props.coinInfo.coin?.toUpperCase()
            }}
          </div>
        </div>
        <!-- 可用 -->
        <div class="rightSix">
          <div>{{ _t18(`account_available`) }}</div>
          <div class="number fw-num" v-if="form.type == 0">{{ availableBalance }} USDT</div>
          <div class="number fw-num" v-else>
            {{ availableBalance }}
            {{
              props.coinInfo.customizeFlag === 2
                ? matchText(props.coinInfo.showSymbol, '/USDT')
                : props.coinInfo.coin?.toUpperCase()
            }}
          </div>
        </div>
        <!-- 占位 -->
        <div class="rightSeat" v-if="form.delegateType == 1&&!['rxce'].includes(_getConfig('_APP_ENV'))"></div>
        <!-- 买入/ 卖出按钮：自定义币种 (coinInfo.customizeFlag == 2),非自定义币种 -->
        <div class="rightSeven" @click="submit" v-if="props.coinInfo.customizeFlag == 2">
          <div class="maybutton" :class="{ hightColorRed: form.type == 1 }">
            <!-- 买入 -->
            <template v-if="form.type == 0">
              {{ _t18(`purchase`) }} {{ props.coinInfo.showSymbol.replace('/USDT', '') }}
            </template>
            <!-- 卖出 -->
            <template v-else>
              {{ _t18(`bb_sell1`) }} {{ props.coinInfo.showSymbol.replace('/USDT', '') }}</template
            >
          </div>
        </div>
        <div class="rightSeven" @click="submit" v-else>
          <div class="maybutton" :class="{ hightColorRed: form.type == 1 }">
            <!-- 买入 -->
            <template v-if="form.type == 0">
              {{ _t18(`purchase`) }} {{ props.coinInfo.coinUpperCase }}
            </template>
            <!-- 卖出 -->
            <template v-else> {{ _t18(`bb_sell1`) }} {{ props.coinInfo.coinUpperCase }}</template>
          </div>
        </div>
      </div>
    </div>
    <div class="line"></div>
    <!-- 订单信息 -->
    <OrderListBox ref="orderListBoxRef" :coinInfo="coinInfo"></OrderListBox>
  </div>
</template>

<script setup>
import { submitOrderCurrencyApi } from '@/api/trade'
import Handicap from '../../common/handicap.vue'
import OrderListBox from './OrderListBox.vue'
import { _t18 } from '@/utils/public'
import { _div, _mul, _toFixed, priceFormat } from '@/utils/decimal'
import { showToast } from 'vant'
import { useToast } from '@/hook/useToast'
const { _toast, _showName } = useToast()
import { useTradeStore } from '@/store/trade'
const tradeStore = useTradeStore()

import { socketDict } from '@/config/dict'
import PubSub from 'pubsub-js'

const props = defineProps({
  coinInfo: {
    type: Object
  }
})
/**
 * 账户余额
 */
import { useUserStore } from '@/store/user/index'
const userStore = useUserStore()
const { asset } = storeToRefs(userStore)
const availableBalance = computed(() => {
  let tempValue = 0
  if (asset.value.length) {
    if (form.type == '0') {
      tempValue = asset.value.filter((item) => item.symbol === 'usdt')[0]?.availableAmount || 0
    } else {
      tempValue =
        asset.value.filter((item) => item.symbol === props.coinInfo.coin)[0]?.availableAmount || 0
    }
  }
  return tempValue
})

// 币种开盘价等

const coinPriceInfo = computed(() => {
  return tradeStore.allCoinPriceInfo[props.coinInfo.coin] || {}
})

// 切换币种，重置表单
watch(
  () => {
    props.coinInfo
  },
  (n) => {
    restForm()
  },
  {
    deep: true
  }
)
/**
 * 表单
 * type：买入0，卖出1
 * delegateType：限价0，市价1
 */
const form = reactive({
  type: '0',
  delegateType: null
})
// 重置表单
const restForm = () => {
  let tempForm = {
    ...form,
    price: '', // 价格
    count: '', // 数量
    turnover: '', // 成交金额
    slider: 0 // 滑块
  }
  Object.assign(form, tempForm) //复制tempForm到form
}

/**
 * 买入 卖出
 */
const tradeTypeList = [
  { label: _t18(`purchase`), value: '0' },
  { label: _t18(`bb_sell1`), value: '1' }
]
// 切换买入/卖出
const checkedTradeType = (item) => {
  if (item.value !== form.type) {
    form.type = item.value
    // rxce卖出只有市价
    if (['rxce'].includes(__config._APP_ENV)) {
      if (item.value == '1') {
        checkedDelegateType(delegateTypeList[0])
      }
    }
  }
  restForm()
}

/**
 * 市价 限价
 */
const delegateTypeList = [
  { label: _t18(`bb_market_order`), placeholder: _t18(`market_price`), value: '1' },
  { label: _t18(`bb_Limit_order`), placeholder: _t18(`limit_price`), value: '0' }
]
// 当前选中交易价格类型：限价/市价
const currentDelegateType = ref({})
// 限价/市价选择控件
const showPopover = ref(false)
// 切换限价/市价
const checkedDelegateType = (item) => {
  if (item.value !== form.delegateType) {
    form.delegateType = item.value
    currentDelegateType.value = item
  }
  restForm()
  showPopover.value = false
}
// 默认选中
checkedDelegateType(delegateTypeList[0])

/**
 * 订单模块Ref
 */
const orderListBoxRef = ref(null)

/**
 * 提交表单
 */
const submit = async () => {
  let msg = false
  if (form.delegateType == 1 && form.type == 1) {
    // 市价&&卖出
    if (!form.count) {
      msg = _showName('please_enter_quantity') // 请输入数量
    } else if (form.count > availableBalance.value) {
      msg = _showName('Insufficient_funds_available') // 可用金额不足
    }
  } else if (form.delegateType == 0 && form.type == 1) {
    // 限价&&卖出
    if (!form.price) {
      msg = _showName('enter_the_amount') // 请输入金额
    } else if (!form.count) {
      msg = _showName('please_enter_quantity') // 请输入数量
    } else if (form.count > availableBalance.value) {
      msg = _showName('Insufficient_funds_available') // 可用金额不足
    }
  } else {
    // (市价&&买入)||(限价&&买入)
    if (!form.turnover) {
      msg = _showName('enter_the_amount') // 请输入金额
    } else if (form.turnover <= 0) {
      msg = _showName('amount_incorrect') //金额不正确
    } else if (form.turnover > availableBalance.value) {
      msg = _showName('Insufficient_funds_available') // 可用金额不足
    }
  }
  if (msg) {
    showToast(msg)
    return
  }
  let params = {
    symbol: props.coinInfo.coin, //交易币种(e.g btc)
    coin: props.coinInfo.baseCoin, //结算币种(usdt)
    delegateTotal: form.count, //委托总量
    delegatePrice: form.price, //委托价格
    delegateValue: form.turnover, //委托价值
    delegateType: form.delegateType, //委托类型（0限价1市价）
    type: form.type //（0买入1卖出）
  }
  const res = await submitOrderCurrencyApi(params)
  // 购买成功
  if (res.code == 200) {
    userStore.getUserInfo()
    showToast(res.msg)
    restForm()
    // 刷新
    orderListBoxRef.value.refresh()
  } else {
    showToast(res.msg)
  }
}

/**
 * 滑块监听
 */
const sliderChange = (val) => {
  val = val / 100
  let tempPrice = 0
  if (form.delegateType == 0) {
    // 限价
    tempPrice = form.price
  } else if (form.delegateType == 1) {
    // 市价
    tempPrice = coinPriceInfo.value.close
  }
  if (Number(tempPrice)) {
    if (form.delegateType == 1 && form.type == 1) {
      // 市价&&卖出
      form.count = _mul(availableBalance.value, val)
    } else if (form.delegateType == 0 && form.type == 1) {
      // 限价&&卖出
      form.count = _mul(availableBalance.value, val)
      form.turnover = _toFixed(_mul(form.count, tempPrice))
    } else {
      // (市价&&买入)||(限价&&买入)
      form.count = _mul(_div(availableBalance.value, tempPrice), val)
      form.turnover = _toFixed(_mul(form.count, tempPrice))
    }
  }
}

/**
 * 成交金额监听
 */
const turnoverChange = () => {
  let val = form.turnover
  if (Number(val) > Number(availableBalance.value)) {
    // 输入金额大于可用余额
    form.turnover = availableBalance.value
    val = availableBalance.value
  }
  let tempPrice = 0
  if (form.delegateType == 0) {
    // 限价
    tempPrice = form.price
  } else if (form.delegateType == 1) {
    // 市价
    tempPrice = coinPriceInfo.value.close
  }
  // 根据成交额 价格 计算数量
  if (Number(tempPrice)) {
    form.count = _div(val, tempPrice)
    form.slider = parseInt(_mul(_div(val, availableBalance.value), 100))
  }
}

/**
 * 数量监听
 */
const countChange = () => {
  let val = form.count
  if (form.delegateType == 1 && form.type == 1) {
    // 市价&&卖出
    form.slider = parseInt(_mul(_div(val, availableBalance.value), 100))
  } else if (form.delegateType == 0) {
    // (限价&&卖出)||(限价&&买入)
    if (Number(form.price) && Number(val)) {
      // 根据数量、价格form.price计算成交额
      form.turnover = _toFixed(_mul(form.price, val))
    }
  } else if (form.delegateType == 1) {
    // 市价&&买入
    if (Number(val) && Number(coinPriceInfo.value.close)) {
      // 根据数量、价格计算成交额
      form.turnover = _toFixed(_mul(coinPriceInfo.value.close, val))
    }
  }
}

/**
 * 价格监听
 */
const priceChange = () => {
  let val = form.price
  if (form.delegateType == 0) {
    // 限价
    if (Number(form.count) && Number(val)) {
      // 根据数量、价格计算成交额
      form.turnover = _toFixed(_mul(form.count, val))
    }
  }
  // 市价忽略
}

/**
 * 设置交易价格
 */
const setTradePrice = (val) => {
  if (form.delegateType == 0) {
    // 限价触发
    form.price = priceFormat(val)
    priceChange(form.price)
  }
}
/**
 * 订单结算，更新订单和用户信息
 */
const settlementKey = ref('')

const coinOverNotification = () => {
  /**
   * 结算改变通知
   */
  settlementKey.value = PubSub.subscribe(socketDict.SETTLEMENT, (key, data) => {
    const { type } = JSON.parse(data.date.message)
    if (type == 1) {
      orderListBoxRef.value.refresh()
      userStore.getUserInfo()
    }
  })
}
coinOverNotification()

onUnmounted(() => {
  settlementKey && PubSub.unsubscribe(settlementKey)
})
</script>

<style lang="scss" scoped>
.hightColor {
  color: #ffffff !important;
  background-color: #28c285 !important; // 买入按钮绿色
}
.hightColorRed {
  color: #ffffff !important;
  background-color: #484852 !important; // 卖出按钮灰色
}
.rightScondList {
  width: 180px;
  background-color: #1a1a1a;
  padding: 5px 0;
  font-size: 12px;
  color: #8c8c8c;
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0,0,0,0.3);
  
  .rightScondListItem {
    padding: 10px 0;
    text-align: center;
    transition: all 0.3s ease;
    
    &:hover {
      background: #2a2a2a;
      color: #fff;
    }
    
    &.hightBlue {
      color: #28c285;
    }
  }
}

.content {
  display: flex;
  padding: 15px;
  background: #1a1a1a; // 深色背景
  
  &_left {
    flex: 1;
    margin-right: 15px;
  }
  
  &_right {
    flex: 1;
    transition: all 0.3s ease;
    
    .rightFirst {
      height: 40px; // 增加高度匹配图片
      display: flex;
      border-radius: 8px;
      overflow: hidden;
      box-shadow: 0 2px 8px rgba(0,0,0,0.2);
      
      .name {
        display: flex;
        align-items: center;
        justify-content: center;
        flex: 1;
        font-size: 14px;
        font-weight: 500;
        color: #fff;
        background: #484852; // 默认灰色背景
        transition: all 0.3s ease;
        
        &:active {
          opacity: 0.9;
        }
      }
    }

    .rightScond {
      margin-top: 12px;
      
      .entrustSelect {
        font-size: 13px;
        width: 100%;
        height: 38px;
        background: #2a2a2a;
        border-radius: 6px;
        box-shadow: 0 2px 6px rgba(0,0,0,0.2);
        display: flex;
        align-items: center;
        justify-content: center;
        position: relative;
        color: #fff;
        transition: all 0.3s ease;
        padding: 0 6px;

        &:active {
          opacity: 0.9;
        }

        .img {
          width: 10px;
          height: 5px;
          position: absolute;
          right: 12px;
          top: 50%;
          transform: translateY(-50%);
          transition: all 0.3s ease;
        }
      }
    }

    .rightThird {
      margin-top: 10px;
      width: 100%;
      height: 38px;
      background: #2a2a2a;
      border-radius: 6px;
      font-size: 13px;
      color: #8c8c8c;
      display: flex;
      justify-content: center;
      align-items: center;
    }

    .rightFourth {
      margin: 20px 0;
      
      .slider-t {
        display: flex;
        justify-content: space-between;
        gap: 3px;
        margin-bottom: 5px;
        
        .item {
          flex: 1;
          height: 4px;
          background: #3a3a3a;
          border-radius: 2px;
          overflow: hidden;
          
          .hightItem {
            height: 100%;
            background: #28c285; // 绿色滑块
            border-radius: 2px;
            transition: width 0.3s ease;
            
            &.hightColorRed {
              background: #484852; // 灰色滑块
            }
          }
        }
      }

      .slider-name {
        display: flex;
        justify-content: space-between;
        margin: 8px 0;
        
        .item {
          font-size: 13px;
          color: #8c8c8c;
          transition: color 0.3s ease;
          cursor: pointer;
          
          &.itemHight {
            color: #fff;
          }
        }
      }
      
      .van-slider {
        margin: 15px 0;
      }
    }

    .rightFifth {
      margin-top: 12px;
      
      .amount {
        width: 100%;
        height: 42px;
        background: #2a2a2a;
        border-radius: 6px;
        box-shadow: 0 2px 6px rgba(0,0,0,0.2);
        font-size: 14px;
        color: #fff;
        text-align: center;
        transition: all 0.3s ease;
        border: none;
        outline: none;
        padding: 0 15px;

        &:focus {
          box-shadow: 0 4px 12px rgba(0,0,0,0.3);
        }
      }

      input::placeholder {
        color: #666;
      }
    }

    .rightSix {
      flex: 1;
      width: 100%;
      margin-top: 15px;
      margin-bottom: 5px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-size: 13px;
      color: #8c8c8c;
      
      .number {
        color: #fff;
        font-weight: 500;
      }
    }

    .rightSeat {
      height: 43px;
    }

    .rightSeven {
      
      .maybutton {
        height: 48px;
        background: #28c285; // 绿色按钮
        border-radius: 8px;
        font-size: 16px;
        color: #fff;
        font-weight: 500;
        display: flex;
        justify-content: center;
        align-items: center;
        box-shadow: 0 4px 12px rgba(40,194,133,0.2);
        transition: all 0.3s ease;

        &:active {
          transform: scale(0.98);
          box-shadow: 0 2px 8px rgba(40,194,133,0.1);
        }

        &.hightColorRed {
          background: #484852; // 灰色按钮
          box-shadow: 0 4px 12px rgba(72,72,82,0.2);
          
          &:active {
            box-shadow: 0 2px 8px rgba(72,72,82,0.1);
          }
        }
      }
    }
  }
}

// 默认全局变量覆盖
:root {
  --ex-default-background-color: #1a1a1a;
  --ex-div-bgColor: #2a2a2a;
  --ex-div-bgColor1: #28c285;
  --ex-div-bgColor7: #484852;
  --ex-default-font-color: #fff;
  --ex-passive-font-color: #8c8c8c;
  --ex-font-color: #fff;
}

// 添加滑入动画
.content > div {
  animation: slideIn 0.3s ease;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(5px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.line {
  height: 8px;
  background: #242424;
}

.rightLine {
  height: 8px;
}

// 自定义滑块样式
.van-slider__bar {
  background: #28c285 !important;
}

.van-slider__button {
  width: 18px !important;
  height: 18px !important;
  background: #fff !important;
  border: 2px solid #28c285 !important;
}
</style>
