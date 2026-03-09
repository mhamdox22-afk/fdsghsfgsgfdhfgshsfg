<template>
  <div class="currencyItem">
    <div class="left">
      <image-load :filePath="currencyItem.logo" class="leftImg" v-if="showLeftImg" />
      <div>
        <div class="topText ff-num">
          <div class="textTop fw-num">
            {{ currencyItem.showSymbol }}
          </div>
        </div>
      </div>
    </div>
    <div class="right">
      <div class="rightLeft">
        <div class="fw-num">
          {{ priceFormat(tradeStore.allCoinPriceInfo[currencyItem.coin]?.close) }}
        </div>
      </div>
      <div class="rightBox">
        <div
          :class="[
            _isRFD(
              tradeStore.allCoinPriceInfo[currencyItem.coin]?.openPrice,
              tradeStore.allCoinPriceInfo[currencyItem.coin]?.close
            ),
            'rfd-sign rfd-bg rightRight fw-num'
          ]"
        >
          <span>{{ tradeStore.allCoinPriceInfo[currencyItem.coin]?.priceChangePercent }}%</span>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { useTradeStore } from '@/store/trade/index'
import { priceFormat } from '@/utils/decimal.js'
const tradeStore = useTradeStore()
const props = defineProps({
  currencyItem: {
    type: Object,
    default: {}
  },
  showLeftImg: {
    type: Boolean,
    default: true
  }
})
</script>
<style lang="scss" scoped>
.currencyItem {
  padding: 12px 10px;
  display: flex;
  justify-content: space-between;
  background: linear-gradient(145deg, #100f0f, #252a2e);
  margin: 8px 10px;
  border-radius: 8px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
  
  &:active {
    transform: scale(0.98);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  }

  .left {
    display: flex;
    align-items: center;
    flex: 0 0 auto;
    min-width: 100px;
    
    .leftImg {
      width: 28px;
      height: 28px;
      margin-right: 8px;
      border-radius: 50%;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
      transition: transform 0.3s ease;
      
      &:hover {
        transform: rotate(15deg);
      }
    }

    .topText {
      display: flex;
      align-items: flex-end;
      font-size: 13px;
      color: rgba(255, 255, 255, 0.6);

      .textTop {
        color: rgba(255, 255, 255, 0.9);
        font-size: 15px;
        font-weight: 500;
        letter-spacing: 0.5px;
      }

      .tip {
        margin-left: 8px;
        padding: 4px 8px;
        height: auto;
        background: rgba(255, 255, 255, 0.1);
        backdrop-filter: blur(5px);
        border-radius: 6px;
        font-size: 12px;
        color: rgba(255, 255, 255, 0.7);
      }
    }
  }

  .right {
    display: flex;
    align-items: center;
    gap: 8px;
    flex: 1;
    justify-content: flex-end;

    .rightLeft {
      color: rgba(255, 255, 255, 0.9);
      text-align: right;
      font-size: 15px;
      font-weight: 500;
      width: 120px;
      min-width: 120px;

      .numRight {
        font-size: 13px;
        margin-top: 4px;
        color: rgba(255, 255, 255, 0.6);
      }
    }

    .rightBox {
      height: 100%;
      display: flex;
      align-items: center;

      .rightRight {
        width: 80px;
        min-width: 80px;
        padding: 4px 8px;
        border-radius: 8px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 13px;
        font-weight: 500;
        transition: all 0.3s ease;
        
        &.rfd-up {
          background: rgba(0, 255, 0, 0.15);
          color: #00ff00;
        }
        
        &.rfd-down {
          background: rgba(255, 0, 0, 0.15);
          color: #ff4d4d;
        }

        &:active {
          transform: scale(0.95);
        }
      }
    }
  }
}

// 添加列表项进入动画
@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.currencyItem {
  animation: slideIn 0.3s ease-out;
}
</style>
