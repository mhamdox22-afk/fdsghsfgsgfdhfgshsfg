<!-- 涨跌按钮 -->
<template>
  <div>
    <!-- 看跌 看跌 -->
    <div class="upsAndDowns">
      <div class="ups item" @click="showBtn(1)">
        {{ _t18(`exchange_bullish`, ['bitmake', 'bityc', 'aams', 'math']) }}
      </div>
      <div class="downs item" @click="showBtn(0)">
        {{ _t18(`exchange_bearish`, ['bitmake', 'bityc', 'aams', 'math']) }}
      </div>
    </div>
  </div>
  <!-- 涨跌 -->
  <PublicPopup :show="showOverlay" :direction="`bottom`" @handelClose="handelClose">
    <template #titleCustomize>
      <div class="titleLeft">
        <!-- customizeFlag   1否 2是 -->
        <div class="fw-bold">
          {{
            coinInfo.customizeFlag === 2
              ? matchText(coinInfo.showSymbol, '/USDT')
              : coinInfo.coin.toUpperCase()
          }}
        </div>
        <!-- titleFlag 买涨  !titleFlag 买跌 -->
        <div class="titleName" :class="titleFlag ? '' : 'titleColor'">
          {{ titleFlag ? _t18(`option_buy_up`, ['bityc']) : _t18(`option_buy_short`, ['bityc']) }}
        </div>
      </div>
    </template>
    <template #contentCustomize>
      <div class="popupContain">
        <div class="item">
          <!-- 选择周期 -->
          <div class="firstHeader fw-bold">{{ _t18(`select_cycle`, ['ebc']) }}</div>
          <div class="firstList">
            <div
              style="display: inline-block"
              class="firstItem fw-num"
              :class="cycleIndex === index ? 'hightColor' : ''"
              v-for="(item, index) in cycleList"
              :key="index"
              @click="chooseCycle(index, item)"
            >
              <div class="firstItemTop">{{ item.period }}s</div>
              <div>{{ _mul(item.odds, 100) }}%</div>
            </div>
          </div>
        </div>
        <div class="item">
          <!-- 买入量 -->
          <div class="firstHeader fw-bold">{{ _t18(`buying_volume`, ['ebc']) }}</div>
          <!-- 最少 -->
          <input
            v-model="quantity"
            type="number"
            :placeholder="`${_t18(`least`, ['ebc', 'aams'])} ${
              ['aams'].includes(_getConfig('_APP_ENV'))
                ? 100
                : cycleObj && cycleObj.minAmount
                ? cycleObj.minAmount
                : 0
            } ${['aams'].includes(_getConfig('_APP_ENV')) ? 'USD' : 'USDT'}`"
            label-width="0"
            class="inputQuantity"
          />
          <div class="quantityList">
            <div
              v-show="item.show"
              class="item fw-num"
              :class="
                selectAll
                  ? item.name === all
                    ? 'hightColor'
                    : ''
                  : Number(quantity) === Number(item.name)
                  ? 'hightColor'
                  : ''
              "
              v-for="(item, index) in quantityList"
              :key="index"
              @click="chooseNums(item.name)"
            >
              {{ item.name }}
            </div>
          </div>
          <div class="balance">
            <!-- 可用余额： -->
            <div>{{ _t18(`exchange_balance`, ['ebc']) }}：</div>
            <div class="fw-num">{{ availableBalance }} USDT</div>
          </div>
          <!-- 确定 -->
          <div class="okButton">
            <div class="button" @click="determine">
              {{ _t18(`btnConfirm`, ['bitmake', 'ebc']) }}
            </div>
          </div>
        </div>
      </div>
    </template>
  </PublicPopup>
  <!-- 倒计时 -->
  <OverlayPulic :show="showCountdown" :z-index="999">
    <template #contentCustomize>
      <div class="countdown">
        <div class="fw-bold closeBtn">
          <div>{{ matchText(coinInfo.showSymbol, '/USDT') }}</div>
          <svg-load class="closeSvg" name="guanbi" @click="turnOffCountdown"></svg-load>
        </div>
        <div class="circle">
          <van-circle
            v-model:current-rate="currentRate"
            :rate="(1 - countDown / Number(cycleObj.period)) * 100"
            :speed="100"
            :stroke-width="`100`"
            :size="`150px`"
            :layer-color="`#D9D9D9`"
            :color="`#138A5D`"
            :text="countdownNum(countDown)"
          />
        </div>
        <div class="countList margin20">
          <!-- 现价 -->
          <div class="item">
            <div>{{ _t18(`quick_label_2`) }}</div>
            <div class="itemRight fw-num">
              {{ coinPriceInfo.close }}
            </div>
          </div>
          <!-- 周期 -->
          <div class="item">
            <div>{{ _t18(`quick_label_3`) }}</div>
            <div class="itemRight fw-num">{{ cycleObj.period }}s</div>
          </div>
          <!-- 方向 -->
          <div class="item">
            <div>{{ _t18(`bb_direction`) }}</div>
            <!-- titleFlag ? '买涨' : '买跌' -->
            <div class="itemRight fw-num" :class="titleFlag ? 'hightUp' : 'hightDown'">
              {{
                titleFlag ? _t18(`option_buy_up`, ['bityc']) : _t18(`option_buy_short`, ['bityc'])
              }}
            </div>
          </div>
          <!-- 数量 -->
          <div class="item">
            <div>{{ _t18(`metastaking_bb_count`) }}</div>
            <div class="itemRight fw-num">{{ quantity }} USDT</div>
          </div>
          <!-- 价格 -->
          <div class="item">
            <div>{{ _t18(`price`) }}</div>
            <div class="itemRight fw-num">{{ buyPrice }} USDT</div>
          </div>
          <!-- 预计盈利 -->
          <div class="item" v-if="!['bitmake'].includes(_getConfig('_APP_ENV'))">
            <div>{{ _t18(`quick_label_7`) }}</div>
            <div
              class="itemRight fw-num"
              :class="formatExpectedProfitColor(titleFlag, buyPrice, coinPriceInfo.close)"
            >
              {{
                formatExpectedProfit(
                  titleFlag,
                  buyPrice,
                  coinPriceInfo.close,
                  quantity,
                  cycleObj.odds,
                  cycleObj.flag
                )
              }}
              USDT
            </div>
          </div>
        </div>
        <!-- 最终价格以系统结算为准 -->
        <div class="countFooter">{{ _t18(`option_profit_loss_3`) }}</div>
      </div>
    </template>
  </OverlayPulic>
  <!-- 倒计时完成 -->
  <OverlayPulic :show="showOver" :z-index="999">
    <div class="countdown"></div>
    <template #contentCustomize>
      <div class="countdown">
        <div class="fw-bold closeBtn">
          <div>{{ matchText(coinInfo.showSymbol, '/USDT') }}</div>
          <svg-load class="closeSvg" name="guanbi" @click="countdownClose"></svg-load>
        </div>
        <div class="countdownHeader">
          <div
            :class="profitAndlossColor(orderObj.betAmount, orderObj.rewardAmount)"
            class="ff-num"
          >
            {{ profitAndloss(orderObj.betAmount, orderObj.rewardAmount) }}
          </div>
          <div class="countdownHeaderName">USDT</div>
        </div>
        <!-- 到期结算完成 -->
        <div class="countdownOver">{{ _t18(`quick_label_1`) }}</div>
        <div class="countList">
          <!-- 现价 -->
          <div class="item">
            <div>{{ _t18(`quick_label_2`) }}</div>
            <div class="itemRight fw-num">
              {{ orderObj.closePrice }}
            </div>
          </div>
          <!-- 周期 -->
          <div class="item">
            <div>{{ _t18(`quick_label_3`) }}</div>
            <div class="itemRight fw-num">{{ cycleObj.period }}s</div>
          </div>
          <!-- 方向 -->
          <div class="item">
            <div>{{ _t18(`bb_direction`) }}</div>
            <!-- titleFlag ? '买涨' : '买跌' -->
            <div class="itemRight fw-num" :class="titleFlag ? 'hightUp' : 'hightDown'">
              {{
                titleFlag ? _t18(`option_buy_up`, ['bityc']) : _t18(`option_buy_short`, ['bityc'])
              }}
            </div>
          </div>
          <!-- 数量 -->
          <div class="item">
            <div>{{ _t18(`metastaking_bb_count`) }}</div>
            <div class="itemRight fw-num">{{ quantity }} USDT</div>
          </div>
          <!-- 价格 -->
          <div class="item">
            <div>{{ _t18(`price`) }}</div>
            <div class="itemRight fw-num">{{ buyPrice }} USDT</div>
          </div>
        </div>
        <!-- 最终价格以系统结算为准 -->
        <div class="countFooter">{{ _t18(`option_profit_loss_3`) }}</div>
      </div>
    </template>
  </OverlayPulic>
</template>
<script setup>
import { ref, watchEffect, computed } from 'vue'
import { matchText } from '@/utils/filters'
import PublicPopup from '@/components/Popup/public.vue'
import OverlayPulic from '@/components/Popup/overlayPulic.vue'
import { getPeriodList, createSecondContractOrder } from '@/api/trade/index'
import { useUserStore } from '@/store/user/index'
import { showToast } from 'vant'
import { useToast } from '@/hook/useToast'
const { _toast, _showName } = useToast()
import { useTradeStore } from '@/store/trade/index'
import {
  countdownNum,
  formatExpectedProfit,
  formatExpectedProfitColor,
  profitAndloss,
  profitAndlossColor
} from '@/utils/filters'
import { _toFixed, _mul } from '@/utils/decimal'
import { _t18 } from '@/utils/public'
const tradeStore = useTradeStore()
const props = defineProps({
  coinInfo: {
    type: Object,
    default: () => {}
  }
})
const userStore = useUserStore()
// 用户余额信息
const { asset } = storeToRefs(userStore)
const showOverlay = ref(false)
const showCountdown = ref(false)
document.addEventListener('visibilitychange', function () {
  let pageVisibility = document.visibilityState
  if (pageVisibility == 'hidden') {
    // 关闭
    // turnOffCountdown()
  }
  if (pageVisibility == 'visible') {
  }
})
const quantity = ref('')
const availableBalance = ref('') //  可用余额
const selectAll = ref(false)
const titleFlag = ref(1)
const cycleIndex = ref(0)
const cycleObj = ref({})
// 倒计时
const currentRate = ref(0)
const cycleList = ref([]) // 周期列表
// 全部
const all = ref(_t18(`all`, ['ebc']))
const quantityList = ref([
  {
    name: '10',
    show: true
  },
  {
    name: '20',
    show: true
  },
  {
    name: '50',
    show: true
  },
  {
    name: '100',
    show: true
  },
  {
    name: '500',
    show: true
  },
  {
    name: '1000',
    show: true
  },
  {
    name: '2000',
    show: true
  },
  // 全部
  {
    name: all.value
  }
])
onMounted(() => {
  if (['gmdoin'].includes(__config._APP_ENV)) {
    quantityList.value = [
      {
        name: '100',
        show: true
      },
      {
        name: '5000',
        show: true
      },
      {
        name: '20000',
        show: true
      },
      {
        name: '50000',
        show: true
      },
      {
        name: '100000',
        show: true
      },
      {
        name: '200000',
        show: true
      },
      {
        show: false
      },
      // 全部
      {
        name: all.value,
        show: true
      }
    ]
  } else if (['gmtoin'].includes(__config._APP_ENV)) {
    quantityList.value = [
      {
        name: '100',
        show: true
      },
      {
        name: '5000',
        show: true
      },
      {
        name: '20000',
        show: true
      },
      {
        name: '50000',
        show: true
      },
      {
        name: '100000',
        show: true
      },
      {
        name: '200000',
        show: true
      },
      {
        name: '500000',
        show: true
      },
      // 全部
      {
        name: all.value,
        show: true
      }
    ]
  } else if (['bitfly'].includes(__config._APP_ENV)) {
    quantityList.value = [
      {
        name: '200',
        show: true
      },
      {
        name: '500',
        show: true
      },
      {
        name: '1000',
        show: true
      },
      {
        name: '5000',
        show: true
      },
      {
        name: '10000',
        show: true
      },
      {
        name: '50000',
        show: true
      },
      {
        name: '100000',
        show: true
      },
      // 全部
      {
        name: all.value,
        show: true
      }
    ]
  } else if (['rxce'].includes(__config._APP_ENV)) {
    quantityList.value = [
      {
        name: '100',
        show: true
      },
      {
        name: '300',
        show: true
      },
      {
        name: '500',
        show: true
      },
      {
        name: '1000',
        show: true
      },
      {
        name: '2000',
        show: true
      },
      {
        name: '3000',
        show: true
      },
      {
        name: '5000',
        show: true
      },
      {
        name: '10000',
        show: true
      },
      // 全部
      {
        name: all.value,
        show: true
      }
    ]
  } else if (['trustwallet', 'gatedefi'].includes(__config._APP_ENV)) {
    quantityList.value = [
      {
        name: '500',
        show: true
      },
      {
        name: '1000',
        show: true
      },
      {
        name: '1500',
        show: true
      },
      {
        name: '2000',
        show: true
      },
      {
        name: '3000',
        show: true
      },
      {
        name: '4000',
        show: true
      },
      {
        name: '5000',
        show: true
      },
      // 全部
      {
        name: all.value,
        show: true
      }
    ]
  } else if (['hfm2'].includes(__config._APP_ENV)) {
    quantityList.value = [
      {
        name: '100',
        show: true
      },
      {
        name: '500',
        show: true
      },
      {
        name: '1000',
        show: true
      },
      {
        name: '2000',
        show: true
      },
      {
        name: '8000',
        show: true
      },
      {
        name: '10000',
        show: true
      },
      {
        name: '15000',
        show: true
      },
      // 全部
      {
        name: all.value,
        show: true
      }
    ]
  }
})
/**
 * 币种价格详情
 */
const coinPriceInfo = computed(() => tradeStore.allCoinPriceInfo[props.coinInfo.coin])
//  如果是全部赋值
const chooseNums = (e) => {
  if (e === all.value) {
    quantity.value = availableBalance.value
    selectAll.value = true
  } else {
    quantity.value = e
    selectAll.value = false
  }
}
// 选择周期
const chooseCycle = (index, item) => {
  cycleIndex.value = index
  cycleObj.value = item
}
const countDown = ref(0) // 初始倒计时
const overFlag = ref(false)
import { secondContractOrderselectOrderList } from '@/api/trade/index'
// import { watch } from 'fs'
const isCloseCountdown = ref(false) // 根据关闭按钮展示是否开始倒计时
// 订单数据
const orderObj = ref(null)
watchEffect(() => {
  if (isCloseCountdown.value) {
    if (countDown.value > 0) {
      setTimeout(() => {
        countDown.value--
      }, 1000)
    } else {
      showCountdown.value = false
      userStore.getUserInfo()
    }
    if (countDown.value === 1) {
      // 调用订单接口用于展示倒计时完成
      let data = {
        status: 1,
        id: orderId.value
      }
      try {
        setTimeout(()=>{
          secondContractOrderselectOrderList(data).then((res) => {
          if (res.code === 200) {
            orderObj.value = res.data[0]
          } else {
            showToast(res.msg)
          }
        })
        },500)
      } catch (error) {}
    }
    if (overFlag.value && countDown.value <= 0) {
      showOver.value = true
    }
  }
})
// 确定
const buyPrice = ref(0)
// 订单id用于查询倒计时订单
const orderId = ref('')
const determine = async () => {
  if (Number(quantity.value) < cycleObj.value.minAmount) {
    // 最少输入
    return showToast(`${_showName(`minimum_input`)} ${cycleObj.value.minAmount} USDT`)
  }
  if (Number(quantity.value) > cycleObj.value.maxAmount) {
    // 最多输入
    return showToast(`${_showName(`Maximum_input`)} ${cycleObj.value.maxAmount} USDT`)
  }
  if (Number(quantity.value) > availableBalance.value) {
    // 输入量超出可用余额
    return _toast('Input_amount_exceeds')
  }
  let data = {
    betContent: titleFlag.value,
    betAmount: quantity.value,
    openPrice: coinPriceInfo.close,
    openTime: new Date().getTime(),
    symbol: props.coinInfo.symbol,
    coinSymbol: props.coinInfo.coin,
    baseSymbol: 'usdt',
    periodId: cycleObj.value.id
  }
  try {

    const res = await createSecondContractOrder(data)
    console.log(res)
    if (res.code === 200) {
      orderId.value = res.data.id
      handelClose()
      setTimeout(() => {
        showCountdown.value = true
        buyPrice.value = res.data.openPrice
        countDown.value = Number(res.data.type)
        overFlag.value = true
        isCloseCountdown.value = true
      }, 1000)
    } else {
      showToast(res.msg)
    }
  } catch (error) {}
}
// 关闭倒计时
const turnOffCountdown = () => {
  showCountdown.value = false
  isCloseCountdown.value = false
  emit('eventBusBrother')
}
/** 倒计时完成 */
const showOver = ref(false)
const countdownClose = () => {
  showOver.value = false
  emit('eventBusBrother')
}
/**看涨 0涨 1跌 */
const showBtn = (e) => {
  availableBalance.value =
    asset.value.filter((item) => {
      return item.symbol === 'usdt'
    })[0].availableAmount + ''
  quantity.value = ''
  titleFlag.value = e
  showOverlay.value = true
  cycleIndex.value = 0
  currentRate.value = 0
  selectAll.value = false
  getCycleList()
}
/** 获取周期 */
const getCycleList = async () => {
  try {
    const res = await getPeriodList({ secondId: props.coinInfo.id })
    if (res.code === 200) {
      cycleList.value = res.data
      cycleObj.value = cycleList.value[0]
    }
  } catch (error) {}
}
const emit = defineEmits(['eventBusBrother'])
const handelClose = () => {
  showOverlay.value = false
}
</script>
<style lang="scss" scoped>
// 涨跌颜色
.hightUp {
  color: var(--ex-active-font-color) !important;
}
.hightDown {
  color: var(--ex-font-color10) !important;
}
.hightColor {
  background-color: var(--ex-div-bgColor1) !important;
  color: var(--ex-font-color) !important;
}
.firstList::-webkit-scrollbar {
  display: none;
}
//输入量
.inputQuantity {
  display: flex;
  align-items: center;
  height: 50px;
  background: var(--ex-default-background-color) !important;
  border-radius: 3px 3px 3px 3px;
  opacity: 1;
  border: 1px solid var(--ex-border-color2);
  font-size: 14px;
  color: var(--ex-default-font-color);
  width: 100%;
  padding: 0 10px;
  :deep(.van-cell) {
    padding: 0 !important;
  }
  :deep(.van-field__control::-webkit-input-placeholder) {
    color: var(--ex-font-color1);
  }
}
input::-webkit-input-placeholder {
  color: var(--ex-font-color1);
}
input::-moz-input-placeholder {
  color: var(--ex-font-color1);
}
input::-ms-input-placeholder {
  color: var(--ex-font-color1);
}

.quantityList {
  padding: 10px 0 5px;
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: flex-start;
  .item {
    width: 25%;
    display: flex;
    align-items: center;
    justify-content: center;
    width: 75px;
    height: 36px;
    background: var(--ex-div-bgColor17);
    color: var(--ex-font-color6);
    border-radius: 3px 3px 3px 3px;
    font-size: 14px;
    margin: 0 5px 15px;
  }
}
.upsAndDowns {
  width: var(--ex-max-width);
  position: fixed;
  display: flex;
  align-items: center;
  justify-content: space-between;
  bottom: 0;
  height: 104px;
  background-color: #1a1a1a; // 深色背景
  z-index: 9;
  padding: 0 15px;
  box-shadow: 0 -4px 12px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;

  .item {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 165px;
    height: 50px;
    font-size: 16px;
    color: #ffffff;
    border-radius: 8px;
    transition: all 0.3s ease;
    
    &:active {
      transform: scale(0.95);
    }
  }

  .ups {
    background: linear-gradient(135deg, #2ebd85 0%, #1c8f64 100%);
    box-shadow: 0 4px 15px rgba(46, 189, 133, 0.2);
  }

  .downs {
    background: linear-gradient(135deg, #f6465d 0%, #cf304a 100%);
    box-shadow: 0 4px 15px rgba(246, 70, 93, 0.2);
  }
}
.titleLeft {
  font-size: 16px;
  color: #E0E0E0;
  display: flex;
  align-items: center;
  
  .titleName {
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 5px;
    background-color: var(--ex-div-bgColor1);
    border-radius: 2px;
    font-size: 12px;
    color: #FFFFFF;
    margin-left: 10px;
  }
  
  .titleColor {
    background: var(--ex-div-bgColor7);
  }
}
.popupContain {
  background: #121212;
  padding: 20px;
  height: calc(66vh - 60px); // 减去标题栏高度
  overflow-y: auto;
  
  // 自定义滚动条样式
  &::-webkit-scrollbar {
    width: 4px;
  }
  
  &::-webkit-scrollbar-track {
    background: transparent;
  }
  
  &::-webkit-scrollbar-thumb {
    background: #333;
    border-radius: 2px;
  }
  
  .item {
    margin-bottom: 24px;

    .firstHeader {
      color: #E0E0E0;
      font-size: 16px;
      margin-bottom: 15px;
      font-weight: 600;
    }

    .firstList {
      display: flex;
      gap: 12px;
      overflow-x: auto;
      padding: 4px;
      margin: 0 -4px;
      
      .firstItem {
        min-width: 80px;
        padding: 12px 16px;
        background: #1E1E1E;
        border-radius: 12px;
        color: #E0E0E0;
        text-align: center;
        cursor: pointer;
        transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
        
        &:hover {
          transform: translateY(-2px);
          background: #252525;
        }

        .firstItemTop {
          font-size: 15px;
          margin-bottom: 4px;
        }

        &.hightColor {
          background: linear-gradient(135deg, #2ebd85 0%, #1c8f64 100%);
          box-shadow: 0 4px 15px rgba(46, 189, 133, 0.2);
        }
      }
    }
  }

  .inputQuantity {
    background: #1E1E1E !important;
    border: 1px solid #2C2C2C;
    border-radius: 12px;
    color: #E0E0E0;
    
    &:focus {
      border-color: #2ebd85;
      box-shadow: 0 0 0 2px rgba(46, 189, 133, 0.15);
    }

    &::placeholder {
      color: #666666;
    }
  }

  .quantityList {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(75px, 1fr));
    gap: 12px;
    padding: 4px;
    margin: 0 -4px 16px;

    .item {
      height: 40px;
      background: #1E1E1E;
      color: #E0E0E0;
      border-radius: 8px;
      font-size: 14px;
      cursor: pointer;
      transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);

      &:hover {
        transform: translateY(-2px);
        background: #252525;
      }

      &.hightColor {
        background: linear-gradient(135deg, #2ebd85 0%, #1c8f64 100%);
        box-shadow: 0 4px 15px rgba(46, 189, 133, 0.2);
      }
    }
  }

  .balance {
    display: flex;
    align-items: center;
    gap: 8px;
    color: #666666;
    font-size: 14px;
    margin-bottom: 24px;
  }

  .okButton {
    .button {
      height: 48px;
      background: linear-gradient(135deg, #2ebd85 0%, #1c8f64 100%);
      border-radius: 12px;
      color: #ffffff;
      font-size: 16px;
      font-weight: 600;
      display: flex;
      align-items: center;
      justify-content: center;
      cursor: pointer;
      transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
      box-shadow: 0 4px 15px rgba(46, 189, 133, 0.2);

      &:active {
        transform: scale(0.98);
      }

      &:hover {
        box-shadow: 0 4px 15px rgba(46, 189, 133, 0.3);
      }
    }
  }
}

// Add animation classes
.popup-enter-active,
.popup-leave-active {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.popup-enter-from,
.popup-leave-to {
  transform: translateY(100%);
  opacity: 0;
}

.popup-enter-to,
.popup-leave-from {
  transform: translateY(0);
  opacity: 1;
}

.countdown {
  background: #121212;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.3);

  .circle {
    position: relative;
    &::after {
      content: '';
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      width: 160px;
      height: 160px;
      background: radial-gradient(circle, rgba(46, 189, 133, 0.1) 0%, transparent 70%);
      border-radius: 50%;
      z-index: -1;
    }
  }

  .countList {
    .item {
      border-bottom: 1px solid #1E1E1E;
      padding: 15px 0;
      transition: all 0.3s ease;

      &:hover {
        background: rgba(255, 255, 255, 0.03);
      }

      .itemRight {
        color: #E0E0E0;
        
        &.hightUp {
          color: #2ebd85;
        }
        
        &.hightDown {
          color: #f6465d;
        }
      }
    }
  }
}

// 更新根变量
:root {
  --ex-default-background-color: #121212;
  --ex-default-font-color: #E0E0E0;
  --ex-passive-font-color: #666666;
  --ex-border-color2: #1E1E1E;
  --ex-div-bgColor1: #2ebd85;
  --ex-div-bgColor7: #f6465d;
  --ex-div-bgColor17: #1E1E1E;
}

// 修改 PublicPopup 相关样式
:deep(.van-popup) {
  background: #121212 !important;
  height: 66vh !important; // 设置为视窗高度的三分之二
  max-height: 66vh !important;
  border-radius: 16px 16px 0 0;
}

:deep(.van-overlay) {
  background-color: rgba(0, 0, 0, 0.7) !important;
}
</style>
