<template>
  <div class="trading-header">
    <!-- Left section with hamburger menu and trading pair -->
    <div class="trading-symbol">
      <svg-load
        name="cebian17x14"
        class="menu-icon"
        @click="emits('showSidePopup')"
      ></svg-load>
      <div class="symbol-text">{{ coinInfo.showSymbol }}</div>
      <div
        :class="[
          _isRFD(coinPriceInfo.openPrice, coinPriceInfo.close, 'buy', 'rise'),
          'percentage-change'
        ]"
      >
        {{ coinPriceInfo?.priceChangePercent }}%
      </div>
    </div>
    
    <!-- Right section with chart and info icons -->
    <div class="trading-actions">
      <svg-load
        name="k-xian"
        class="action-icon"
        @click="$router.push(`/detail?symbol=${coinInfo.coin}&type=1`)"
      ></svg-load>
      <svg-load
        name="guize"
        class="action-icon"
        @click="_toView('/tradingRules?type=1')"
      ></svg-load>
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

const tradeStore = useTradeStore()
const coinPriceInfo = computed(() => {
  return tradeStore.allCoinPriceInfo[props.coinInfo.coin] || {}
})
const emits = defineEmits(['showSidePopup'])
</script>

<style lang="scss" scoped>
.trading-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  background-color: #131316;
  color: #ffffff;
  height: 60px;
  width: 100%;

  .trading-symbol {
    display: flex;
    align-items: center;
    
    .menu-icon {
      width: 17px;
      height: 14px;
      margin-right: 15px;
      cursor: pointer;
    }
    
    .symbol-text {
      font-size: 20px;
      font-weight: bold;
      margin-right: 10px;
    }
    
    .percentage-change {
      font-size: 18px;
      
      &.rise {
        color: #00C087;
      }
      
      &.buy {
        color: #FF5757;
      }
    }
  }
  
  .trading-actions {
    display: flex;
    align-items: center;
    
    .action-icon {
      width: 24px;
      height: 24px;
      margin-left: 20px;
      cursor: pointer;
    }
  }
}

// Removed the complex layered style and animations for a cleaner look
</style>
