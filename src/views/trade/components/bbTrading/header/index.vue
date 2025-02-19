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
          <div
          :class="[
            _isRFD(coinPriceInfo.openPrice, coinPriceInfo.close, 'buy', 'rise'),
            ' rfd-sign firNum fw-num'
          ]"
        >
          {{ coinPriceInfo?.priceChangePercent }}%
        </div>
        </div>
        <!-- 币币交易规则，收藏 -->
        <div class="first">
          <svg-load
          name="k-xian"
          class="senLeftImg"
          @click="$router.push(`/detail?symbol=${coinInfo.coin}&type=1`)"
        ></svg-load>
          <svg-load
            name="guize"
            class="senLeftImg"
            @click="_toView('/tradingRules?type=1')"
          ></svg-load>
          <!-- <svg-load
            v-if="mainStore.hasOption"
            @click="setCollectByCoin"
            :name="coinCollect ? 'tianjia24x24-x' : 'tianjia24x24-w'"
            class="senLeftImg"
          ></svg-load> -->
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
const mainStore = useMainStore()

const props = defineProps({
  coinInfo: {
    type: Object,
    default: () => {}
  }
})
/**
 * 黄金白银取值
 */

const tradeStore = useTradeStore()
const coinPriceInfo = computed(() => {
  return tradeStore.allCoinPriceInfo[props.coinInfo.coin] || {}
})
const emits = defineEmits(['showSidePopup'])
</script>
<style lang="scss" scoped>
.hightItem {
  color: var(--ex-active-font-color) !important;
}
.top {
  padding: 20px 15px 0;
  z-index: 9;
  background: linear-gradient(180deg, #1a1a1a 0%, #232323 100%);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  border-radius: 0 0 20px 20px;
  position: relative;
  transition: all 0.3s ease;

  &::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    height: 1px;
    background: linear-gradient(90deg, 
      rgba(255, 255, 255, 0) 0%,
      rgba(255, 255, 255, 0.1) 50%,
      rgba(255, 255, 255, 0) 100%
    );
  }

  .first {
    display: flex;
    justify-content: space-between;
    padding: 10px 0;

    .firLeft {
      display: flex;
      align-items: center;
      font-size: 16px;
      color: #ffffff;
      background: rgba(255, 255, 255, 0.05);
      padding: 8px 15px;
      border-radius: 12px;
      backdrop-filter: blur(5px);
      transition: all 0.3s ease;

      &:active {
        transform: scale(0.98);
      }

      .firLeftImg {
        width: 17px;
        height: 14px;
        margin-right: 10px;
        filter: brightness(2);
        transition: transform 0.3s ease;

        &:hover {
          transform: rotate(5deg);
        }
      }

      .firNum {
        font-size: 14px;
        margin-left: 10px;
        padding: 2px 8px;
        border-radius: 8px;
        background: rgba(255, 255, 255, 0.05);
      }
    }

    .senLeftImg {
      margin-left: 15px;
      display: block;
      width: 24px;
      height: 24px;
      padding: 5px;
      border-radius: 50%;
      background: rgba(255, 255, 255, 0.05);
      transition: all 0.3s ease;
      
      &:active {
        transform: scale(0.9);
        background: rgba(255, 255, 255, 0.1);
      }
    }
  }

  .second {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px 0 10px;

    .secondLeft {
      font-size: 36px;
      font-weight: bold;
      color: #ffffff;
      text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
      
      > * {
        transition: 0.3s;
      }

      .secondLeftB {
        font-size: 14px;
        margin-top: 5px;
        opacity: 0.8;
      }
    }

    .secondRight {
      .secondItem {
        display: flex;
        align-items: center;
        padding: 8px 12px;
        font-size: 14px;
        justify-content: space-between;
        background: rgba(255, 255, 255, 0.05);
        border-radius: 10px;
        margin-bottom: 8px;
        backdrop-filter: blur(5px);

        .itemL {
          color: rgba(255, 255, 255, 0.6);
          margin-right: 10px;
        }

        .itemR {
          color: #ffffff;
        }
      }
    }
  }

  .third {
    margin-top: 20px;
    
    .list {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 10px 0;

      .thirdLeft {
        display: flex;
        font-size: 14px;
        color: #ffffff;

        .item {
          margin-right: 30px;
          padding: 5px 10px;
          border-radius: 8px;
          transition: all 0.3s ease;
          
          &:hover {
            background: rgba(255, 255, 255, 0.05);
          }
        }
      }

      .thirdRight {
        display: flex;
        align-items: center;
        font-size: 14px;
        color: rgba(255, 255, 255, 0.8);
        padding: 5px 10px;
        border-radius: 8px;
        background: rgba(255, 255, 255, 0.05);

        .thirdRightImg {
          width: 10px;
          height: 6px;
          margin-left: 5px;
          transition: transform 0.3s ease;
        }

        &:active {
          background: rgba(255, 255, 255, 0.1);
        }
      }
    }
  }
}

.selectTimes {
  position: fixed;
  height: 100vh;
  width: var(--ex-max-width);
  background: rgba(0, 0, 0, 0.8);
  backdrop-filter: blur(5px);
  z-index: 10;

  .times {
    background: linear-gradient(180deg, #1a1a1a 0%, #232323 100%);
    position: absolute;
    width: 100%;
    height: 84px;
    display: flex;
    align-items: center;
    border-radius: 0 0 20px 20px;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.3);

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
      transition: all 0.3s ease;

      &:active {
        transform: scale(0.95);
        background: rgba(255, 255, 255, 0.1);
      }
    }
  }
}

// 添加涟漪动画效果
@keyframes ripple {
  0% {
    transform: scale(1);
    opacity: 0.4;
  }
  100% {
    transform: scale(2);
    opacity: 0;
  }
}

// 添加上升动画
@keyframes slideUp {
  from {
    transform: translateY(20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

// 为主要元素添加动画
.top {
  animation: slideUp 0.3s ease-out;
}
</style>
