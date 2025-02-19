<!-- 交易页面：秒合约，币币交易，U本位 -->
<template>
  <div class="trade-container">
    <!-- tabs -->
    <van-sticky>
      <div class="headerList">
        <div class="left">
          <svg-load name="jiantou-z" class="leftImg pulse-animation" @click="_back()"></svg-load>
        </div>
        <div class="headerChoose">
          <van-tabs
            swipeable
            shrink
            :swipe-threshold="2"
            v-model:active="currentIndex"
            :line-width="0"
            title-active-color="#00ff9d"
            title-inactive-color="#8b8b8b"
          >
            <van-tab v-for="(item, index) in headerList" :key="index" :title="item.title">
            </van-tab>
          </van-tabs>
        </div>
      </div>
    </van-sticky>
    <!-- tab -->
    <component
      :is="currentComponent"
      :headerList="headerList"
    ></component>
  </div>
</template>

<script setup>
import { _t18 } from '@/utils/public'
import { useMainStore } from '@/store/index'
const mainStore = useMainStore()
import { useTradeStore } from '@/store/trade'
const tradeStore = useTradeStore()
import { useRoute } from 'vue-router'
const $route = useRoute()
import { computed, onMounted } from 'vue'
import SecondContract from './components/tradeSecondContract.vue'
import BBTrading from './components/tradeBBTrading.vue'
import Ustandard from './components/tradeUstandard.vue'
// tabs
const headerList = computed(() => {
  let tempList = mainStore.getTradeHeaderList.filter((item, index) => {
    return item.componentName != 'Optional'
  })
  return tempList
})
const currentIndex = ref(
  mainStore.tradeFlag - mainStore.isOption < 0 ? 0 : mainStore.tradeFlag - mainStore.isOption
)
// 切换组件
const currentComponent = computed(() => {
  let temp = ''
  if (headerList.value[currentIndex.value].componentName == 'SecondContract') {
    temp = SecondContract
  }else if (headerList.value[currentIndex.value].componentName == 'BBTrading') {
    temp = BBTrading
  }else if (headerList.value[currentIndex.value].componentName == 'Ustandard') {
    temp = Ustandard
  }
  return temp
})
watch(
  currentIndex,
  (n) => {
    mainStore.setTradeFlag(n)
    tradeStore.getCoinList()
    /* if (headerList.value[n].componentName == 'SecondContract') {
      let temp={}
      if ($route.query.symbol) {
        temp = tradeStore.secondContractCoinList.filter((item, index) => {
          return item.coin === $route.query.symbol
        })[0]
        if (!temp) {
          temp = tradeStore.secondContractCoinList[0]
        }
      } else {
        temp = tradeStore.secondContractCoinList[0]
      }
      coinInfo2.value=temp
    } else if (headerList.value[currentIndex.value].componentName == 'BBTrading') {
      let temp={}
      if ($route.query.symbol) {
        temp = tradeStore.spotCoinList.filter((item, index) => {
          return item.coin === $route.query.symbol
        })[0]
        if (!temp) {
          temp = tradeStore.spotCoinList[0]
        }
      } else {
        temp = tradeStore.spotCoinList[0]
      }
      coinInfo2.value=temp
    } else if (headerList.value[currentIndex.value].componentName == 'Ustandard') {
      let temp={}
      if ($route.query.symbol) {
        temp = tradeStore.contractCoinList.filter((item, index) => {
          return item.coin === $route.query.symbol
        })[0]
        if (!temp) {
          temp = tradeStore.contractCoinList[0]
        }
      } else {
        temp = tradeStore.contractCoinList[0]
      }
      coinInfo2.value=temp
    } */
  },
  { immediate: true }
)
onMounted(()=>{
  tradeStore.getCoinList()
})
</script>

<style lang="scss" scoped>
.trade-container {
  background: #121212;
  min-height: 100vh;
}

.headerList {
  height: 70px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  display: flex;
  align-items: center;
  background: #1a1a1a;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(10px);
  transition: all 0.3s ease;
  padding-left: 15px;

  .left {
    display: flex;
    font-size: 16px;
    font-weight: normal;
    color: #ffffff;

    .leftImg {
      width: 18px;
      height: 14px;
      margin-right: 15px;
      filter: brightness(0) invert(1);
      transition: transform 0.3s ease;

      &:active {
        transform: scale(0.95);
      }
    }
  }

  .headerChoose {
    display: flex;
    padding: 0 15px 0 0;
    background: transparent;

    :deep(.van-tabs__wrap) {
      height: 69px;
    }

    :deep(.van-tabs__nav) {
      background: transparent;
    }

    :deep(.van-tab) {
      flex: none;
      font-size: 16px;
      margin-right: 30px;
      padding: 0;
      color: #ffffff !important;
      background: transparent;
      position: relative;
      transition: all 0.3s ease;
    }

    :deep(.van-tab::after) {
      content: '';
      position: absolute;
      bottom: 0;
      left: 50%;
      width: 0;
      height: 2px;
      background: #00ff9d;
      transition: all 0.3s ease;
      transform: translateX(-50%);
      opacity: 0;
    }

    :deep(.van-tab--active) {
      transform: scale(1.05);
      font-weight: 500;
      color: #00ff9d !important;
      text-shadow: 0 0 10px rgba(0, 255, 157, 0.3);
    }

    :deep(.van-tab--active::after) {
      width: 100%;
      opacity: 1;
    }
  }
}

// 添加动画
@keyframes pulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
  100% {
    transform: scale(1);
  }
}

.pulse-animation {
  animation: pulse 2s infinite;
}

// 添加过渡动画
.component-fade-enter-active,
.component-fade-leave-active {
  transition: opacity 0.3s ease;
}

.component-fade-enter-from,
.component-fade-leave-to {
  opacity: 0;
}
</style>
