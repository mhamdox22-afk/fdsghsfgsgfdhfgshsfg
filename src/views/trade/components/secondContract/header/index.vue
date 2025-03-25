<template>
  <div>
    <div class="top">
      <div class="first">
        <!-- 打开弹窗 -->
        <div class="firLeft">
          <svg-load
            name="cebian17x14"
            class="firLeftImg"
            @click="emits('showSidePopup')"
          ></svg-load>
          <div class="fw-bold">{{ coinInfo.showSymbol }}</div>
        </div>
        <!-- 秒合约规则，收藏 -->
        <div class="first">
          <svg-load
            name="guize"
            class="senLeftImg"
            @click="_toView('/tradingRules?type=0')"
          ></svg-load>
          <!-- <svg-load
            v-if="mainStore.hasOption"
            @click="setCollectByCoin"
            :name="coinCollect ? 'tianjia24x24-x' : 'tianjia24x24-w'"
            class="senLeftImg"
          ></svg-load> -->
        </div>
      </div>
      <div class="second">
        <!-- 当前币种价格 -->
        <div class="secondLeft">
          <div :class="[_isRFD(coinPriceInfo.open, coinPriceInfo.close, 'buy', 'rise'), ' fw-num']">
            {{ coinPriceInfo.close || '0.00' }}
          </div>
          <div
            :class="[
              _isRFD(coinPriceInfo.openPrice, coinPriceInfo.close, 'buy', 'rise'),
              ' rfd-sign secondLeftB fw-num'
            ]"
          >
            {{ coinPriceInfo?.priceChangePercent }}%
          </div>
        </div>
        <div class="secondRight">
          <!-- 高 -->
          <div class="secondItem">
            <div class="itemL">{{ _t18(`k_hight`, ['ebc']) }}</div>
            <div class="itemR fw-num" v-if="coinPriceInfo?.high24">
              {{ priceFormat(coinPriceInfo.high24) }}
            </div>
            <div class="itemR fw-num" v-else>
              {{ priceFormat(tradeStore.klineTicker.highPrice || 0) }}
            </div>
          </div>
          <!-- 低 -->
          <div class="secondItem">
            <div class="itemL">{{ _t18(`k_low`, ['ebc']) }}</div>
            <div class="itemR fw-num" v-if="coinPriceInfo?.low24">
              {{ priceFormat(coinPriceInfo.low24) }}
            </div>
            <div class="itemR fw-num" v-else>
              {{ priceFormat(tradeStore.klineTicker.lowPrice || 0) }}
            </div>
          </div>
          <!-- 量 -->
          <div class="secondItem">
            <div class="itemL">{{ _t18(`k_quantity`) }}</div>
            <div class="itemR fw-num" v-if="coinPriceInfo?.volume24">
              {{ countFormat(coinPriceInfo.volume24*1000 ) }}
            </div>
            <div class="itemR fw-num" v-else>
              {{ countFormat(tradeStore.klineTicker.volume || 0) }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { useTradeStore } from '@/store/trade'
import { countFormat, priceFormat } from '@/utils/decimal'
import { _t18 } from '@/utils/public'
import { useMainStore } from '@/store/index.js'
import { onMounted } from 'vue'
const mainStore = useMainStore()

const props = defineProps({
  coinInfo: {
    type: Object,
    default: () => {}
  }
})
const coinCollect = ref()
onMounted(() => {
  coinCollect.value = props.coinInfo.isCollect === 2 ? false : true
})
watch(
  () => props.coinInfo.coin,
  () => {
    coinCollect.value = props.coinInfo.isCollect === 2 ? false : true
  },
  { deep: true }
)
/**
 * 黄金白银取值
 */

const tradeStore = useTradeStore()
const coinPriceInfo = computed(() => {
  return tradeStore.allCoinPriceInfo[props.coinInfo.coin] || {}
})
const emits = defineEmits(['showSidePopup'])
// 点击收藏按钮
const setCollectByCoin = () => {}
</script>
<style lang="scss" scoped>
.hightItem {
  color: var(--ex-active-font-color) !important;
}
.top {
  padding: 20px 15px 0;
  z-index: 9;
  background: #131316;
  position: relative;
  transition: all 0.3s ease;
  overflow: hidden;

  // 添加炫酷背景效果
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 1px;
    background: linear-gradient(90deg, 
      transparent,
      rgba(255, 255, 255, 0.2),
      transparent
    );
  }

  // 添加光晕效果

  .first {
    display: flex;
    justify-content: space-between;
    
    .firLeft {
      display: flex;
      align-items: center;
      font-size: 16px;
      color: #ffffff;
      
      .firLeftImg {
        width: 17px;
        height: 14px;
        margin-right: 10px;
      }
    }

    .senLeftImg {
      margin-left: 10px;
      display: block;
      width: 24px;
      height: 24px;
    }
  }

  .second {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    padding: 15px 0 10px;

    .secondLeft {
      font-size: 42px;
      font-weight: bold;
      
      &.fall {
        color: #FF4D4F !important;
      }
      
      .secondLeftB {
        font-size: 14px;
        margin-top: 5px;
        
        &.fall {
          color: #FF4D4F !important;
        }
      }
    }

    .secondRight {
      .secondItem {
        display: flex;
        justify-content: space-between;
        margin-bottom: 5px;
        
        .itemL {
          color: rgba(255, 255, 255, 0.6);
          margin-right: 15px;
        }

        .itemR {
          color: #ffffff;
          text-align: right;
        }
      }
    }
  }
}

.rise {
  color: #00b897 !important;
}

.fall {
  color: #FF4D4F !important;
}

// 添加新的动画
@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

@keyframes numberChange {
  0% {
    transform: translateY(-10px);
    opacity: 0;
  }
  100% {
    transform: translateY(0);
    opacity: 1;
  }
}

@keyframes pulse {
  0% {
    transform: scale(1);
    opacity: 0.5;
  }
  50% {
    transform: scale(1.05);
    opacity: 1;
  }
  100% {
    transform: scale(1);
    opacity: 0.5;
  }
}

// 添加动画类
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.selectTimes {
  position: fixed;
  height: 100vh;
  width: var(--ex-max-width);
  background: rgba(0, 0, 0, 0.8);
  backdrop-filter: blur(5px);
  z-index: 10;
  animation: fadeIn 0.3s ease;

  .times {
    background: linear-gradient(180deg, #1a1a1a 0%, #252525 100%);
    position: absolute;
    width: 100%;
    height: 84px;
    display: flex;
    align-items: center;
    border-radius: 0 0 20px 20px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);

    .item {
      display: flex;
      align-items: center;
      justify-content: center;
      margin: 0 15px;
      width: 37px;
      height: 23px;
      background: rgba(255, 255, 255, 0.05);
      border-radius: 8px;
      font-size: 12px;
      color: #ffffff;
      transition: all 0.2s ease;
      position: relative;
      overflow: hidden;
      
      &::before {
        content: '';
        position: absolute;
        top: -50%;
        left: -50%;
        width: 200%;
        height: 200%;
        background: linear-gradient(
          45deg,
          transparent,
          rgba(255, 255, 255, 0.1),
          transparent
        );
        transition: transform 0.3s ease;
        transform: translateX(-100%);
      }

      &:hover::before {
        transform: translateX(100%);
      }

      &:active {
        transform: scale(0.95);
      }
    }
  }
}
</style>
