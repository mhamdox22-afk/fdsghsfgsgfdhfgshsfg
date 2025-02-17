<template>
  <div class="collectItem">
    <div class="left">
      <img v-if="collectItem.icon" :src="collectItem.icon" class="leftImg" alt="" />
      <!-- <p class="fw-bold">{{ item.coin?.toUpperCase() }}</p> -->
      <div>
        <div class="topText ff-num">
          <div class="textTop fw-num">
            {{ collectItem.coin?.toUpperCase() }}
          </div>
        </div>
      </div>
    </div>
    <!-- <div class="right">
          <p>{{ tradeStore.allCoinPriceInfo[`${item.coin}`]?.close }}</p>
          <p>{{ tradeStore.allCoinPriceInfo[`${item.coin}`]?.priceChangePercent }}</p>
        </div> -->
    <div class="right">
      <div class="rightLeft">
        <div class="fw-num">
          {{ priceFormat(tradeStore.allCoinPriceInfo[collectItem.coin]?.close) }}
        </div>
      </div>
      <div class="rightBox">
        <div
          :class="[
            _isRFD(
              tradeStore.allCoinPriceInfo[collectItem.coin]?.openPrice,
              tradeStore.allCoinPriceInfo[collectItem.coin]?.close
            ),
            'rfd-sign rfd-bg rightRight fw-num'
          ]"
        >
          <span>{{ tradeStore.allCoinPriceInfo[collectItem.coin]?.priceChangePercent }}%</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { priceFormat } from '@/utils/decimal.js'
import { useTradeStore } from '@/store/trade'
const tradeStore = useTradeStore()
const props = defineProps({
  collectItem: {
    type: Object
  }
})
</script>

<style lang="scss" scoped>
.collectItem {
  background: #1a1a1a;
  margin: 8px 12px;
  padding: 15px;
  border-radius: 12px;
  display: flex;
  justify-content: space-between;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  position: relative;
  overflow: hidden;
  
  &:active {
    transform: scale(0.98);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
  }

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: linear-gradient(45deg, rgba(255,255,255,0.03), transparent);
    z-index: 1;
  }

  .left {
    display: flex;
    align-items: center;
    position: relative;
    z-index: 2;

    .leftImg {
      width: 32px;
      height: 32px;
      margin-right: 12px;
      border-radius: 50%;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
      transition: transform 0.3s ease;

      &:hover {
        transform: scale(1.1);
      }
    }

    .topText {
      display: flex;
      align-items: flex-end;
      font-size: 13px;
      color: rgba(255, 255, 255, 0.6);

      .textTop {
        color: #ffffff;
        font-size: 16px;
        font-weight: 500;
        letter-spacing: 0.5px;
      }

      .tip {
        margin-left: 8px;
        padding: 4px 8px;
        height: auto;
        background: rgba(255, 255, 255, 0.1);
        backdrop-filter: blur(4px);
        border-radius: 4px;
        font-size: 12px;
        color: #fff;
        transition: all 0.3s ease;

        &:hover {
          background: rgba(255, 255, 255, 0.15);
        }
      }
    }
  }

  .right {
    display: flex;
    align-items: center;
    position: relative;
    z-index: 2;

    .rightLeft {
      text-align: right;
      font-size: 16px;
      color: #ffffff;
      margin-right: 15px;

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
        min-width: 90px;
        padding: 8px 12px;
        border-radius: 6px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 14px;
        font-weight: 500;
        transition: all 0.3s ease;
        
        &.rfd-up {
          background: rgba(14, 203, 129, 0.15);
          color: #0ECB81;
        }
        
        &.rfd-down {
          background: rgba(246, 70, 93, 0.15);
          color: #F6465D;
        }
      }
    }
  }
}
</style>
