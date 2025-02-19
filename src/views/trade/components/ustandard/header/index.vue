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
        <!-- U本位规则，收藏 -->
        <div class="first">
          <svg-load
          name="k-xian"
          class="senLeftImg"
          @click="$router.push(`/detail?symbol=${coinInfo.coin}&type=2`)"
        ></svg-load>
          <svg-load
            name="guize"
            class="senLeftImg"
            @click="_toView('/tradingRules?type=2')"
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
  background: linear-gradient(180deg, #1a1a1a 0%, #252525 100%);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  border-radius: 0 0 20px 20px;
  transition: all 0.3s ease;

  .first {
    display: flex;
    justify-content: space-between;
    animation: slideDown 0.5s ease;

    .firLeft {
      display: flex;
      align-items: center;
      font-size: 16px;
      color: #ffffff;
      padding: 8px 15px;
      background: rgba(255, 255, 255, 0.05);
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
          transform: rotate(-5deg);
        }
      }

      .fw-bold {
        background: linear-gradient(90deg, #fff 0%, #e0e0e0 100%);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
      }
    }

    .senLeftImg {
      margin-left: 15px;
      display: block;
      width: 24px;
      height: 24px;
      padding: 5px;
      background: rgba(255, 255, 255, 0.05);
      border-radius: 50%;
      transition: all 0.3s ease;
      filter: brightness(2);

      &:active {
        transform: scale(0.95);
        background: rgba(255, 255, 255, 0.1);
      }
    }
  }

  .second {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 25px 0 15px;
    animation: slideUp 0.5s ease 0.2s both;

    .secondLeft {
      font-size: 36px;
      font-weight: bold;
      color: #ffffff;
      text-shadow: 0 2px 10px rgba(255, 255, 255, 0.1);
      
      .secondLeftB {
        font-size: 14px;
        margin-top: 5px;
        color: rgba(255, 255, 255, 0.7);
      }
    }

    .secondRight {
      .secondItem {
        display: flex;
        align-items: center;
        padding: 8px 0;
        font-size: 14px;
        justify-content: space-between;
        
        .itemL {
          color: rgba(255, 255, 255, 0.5);
          margin-right: 12px;
        }

        .itemR {
          color: #ffffff;
        }
      }
    }
  }

  .third {
    margin-top: 20px;
    animation: slideUp 0.5s ease 0.3s both;

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
          position: relative;
          
          &:after {
            content: '';
            position: absolute;
            bottom: -5px;
            left: 0;
            width: 0;
            height: 2px;
            background: #fff;
            transition: width 0.3s ease;
          }

          &:hover:after {
            width: 100%;
          }
        }
      }

      .thirdRight {
        display: flex;
        align-items: center;
        font-size: 14px;
        color: rgba(255, 255, 255, 0.7);
        
        .thirdRightImg {
          width: 10px;
          height: 6px;
          margin-left: 5px;
          transition: transform 0.3s ease;
        }

        &:hover .thirdRightImg {
          transform: translateY(2px);
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
      border-radius: 6px;
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

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
