<template>
  <div class="header-container">
    <!-- Left side with menu icon -->
    <div class="left-section">
      <svg-load
        name="cebian17x14"
        class="menu-icon"
        @click="emits('showSidePopup')"
      ></svg-load>
    </div>
    
    <!-- Center with trading pair and percentage -->
    <div class="center-section">
      <div class="trading-pair">{{ coinInfo.showSymbol }}</div>
      <div class="percentage-change" :class="{ 'negative': isPriceDown }">
        {{ priceChangePercent }}
      </div>
    </div>
    
    <!-- Right section with icons -->
    <div class="right-section">
      <svg-load
        name="k-xian"
        class="action-icon"
        @click="$router.push(`/detail?symbol=${coinInfo.coin}&type=2`)"
      ></svg-load>
      <svg-load
        name="guize"
        class="action-icon"
        @click="_toView('/tradingRules?type=2')"
      ></svg-load>
    </div>
  </div>
</template>

<script setup>
import { useTradeStore } from '@/store/trade'
import { countFormat, priceFormat } from '@/utils/decimal'
import { _t18 } from '@/utils/public'
import { useMainStore } from '@/store/index.js'
import { computed } from 'vue'

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

// Calculate price change percentage
const priceChangePercent = computed(() => {
  const changePercent = coinPriceInfo.value?.changePercent || 0
  return `${changePercent >= 0 ? '+' : ''}${changePercent.toFixed(2)}%`
})

// Check if price is down
const isPriceDown = computed(() => {
  return (coinPriceInfo.value?.changePercent || 0) < 0
})

const emits = defineEmits(['showSidePopup'])
</script>

<style lang="scss" scoped>
.header-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #131316;
  padding: 0 16px;
  height: 50px;
  width: 100%;
  color: #ffffff;
  
  .left-section {
    .menu-icon {
      width: 24px;
      height: 24px;
      filter: brightness(1.8);
      cursor: pointer;
    }
  }
  
  .center-section {
    display: flex;
    align-items: center;
    position: absolute;
    left: 40%;
    transform: translateX(-50%);
    
    .trading-pair {
      font-size: 20px;
      font-weight: 600;
      color: #ffffff;
      margin-right: 12px;
    }
    
    .percentage-change {
      font-size: 20px;
      color: #4CAF50; /* Bright green for positive */
      
      &.negative {
        color: #FF5252; /* Bright red for negative */
      }
    }
  }
  
  .right-section {
    display: flex;
    gap: 16px;
    
    .action-icon {
      width: 24px;
      height: 24px;
      cursor: pointer;
    }
  }
}
</style>
