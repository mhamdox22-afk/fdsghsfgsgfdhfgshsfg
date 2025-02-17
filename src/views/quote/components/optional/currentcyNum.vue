<template>
  <div class="numList fw-num">
      <div class="price-container">
        <div class="numTop">
          {{ priceFormat(coinPriceInfo?.close) }}
        </div>
        <div
          :class="[
            _isRFD(coinPriceInfo?.openPrice, coinPriceInfo?.close, 'buy', 'rise'),
            'rfd-sign numBot fw-num'
          ]"
        >
          {{ coinPriceInfo?.priceChangePercent }}%
        </div>
      </div>
  </div>
</template>
<script setup>
import { priceFormat } from '@/utils/decimal.js'
import { useTradeStore } from '@/store/trade'
const tradeStore = useTradeStore()
const props = defineProps({
  data: {
    type: Object,
    default: () => {}
  }
})
const coinPriceInfo = computed(() => {
  return tradeStore.allCoinPriceInfo[props.data.coin] || {}
})
</script>
<style lang="scss" scoped>
.numList {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 12px;
  background: linear-gradient(145deg, #1a1a1a, #2d2d2d);
  border-radius: 16px;
  box-shadow: 5px 5px 15px rgba(0, 0, 0, 0.3),
              -5px -5px 15px rgba(255, 255, 255, 0.02);
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 8px 8px 20px rgba(0, 0, 0, 0.4),
                -8px -8px 20px rgba(255, 255, 255, 0.03);
  }

  .price-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 8px;
    border-radius: 12px;
    background: rgba(255, 255, 255, 0.03);
    backdrop-filter: blur(5px);
    animation: fadeIn 0.5s ease-out;
  }

  .numTop {
    margin-bottom: 4px;
    font-size: 18px;
    font-weight: 600;
    color: #ffffff;
    text-shadow: 0 0 10px rgba(255, 255, 255, 0.2);
    transition: all 0.3s ease;
  }

  .numBot {
    margin-top: 4px;
    font-size: 14px;
    font-weight: 500;
    transition: all 0.3s ease;

    &.rise {
      color: #00ff88;
      text-shadow: 0 0 10px rgba(0, 255, 136, 0.3);
    }

    &.fall {
      color: #ff4d4d;
      text-shadow: 0 0 10px rgba(255, 77, 77, 0.3);
    }
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

// 添加数字变化动画
.fw-num {
  transition: all 0.3s ease;
  &:hover {
    transform: scale(1.02);
  }
}
</style>
