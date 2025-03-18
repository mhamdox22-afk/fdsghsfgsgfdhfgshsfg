<!-- 交易页面：秒合约，币币交易，U本位 -->
<template>
  <div class="trade-container">
    <!-- tabs -->
    <van-sticky>
      <div class="headerList">
        <div class="left">
          <svg-load name="jiantou-z" class="leftImg" @click="_back()"></svg-load>
        </div>
        <div class="headerChoose">
          <van-tabs
            swipeable
            shrink
            :swipe-threshold="2"
            v-model:active="currentIndex"
            :line-width="30"
            title-active-color="linear-gradient(187.2204122539037deg, #18C8FF 0%, #933FFE 100%);"
            title-inactive-color="#8b8b8b"
          >
            <van-tab v-for="(item, index) in headerList" :key="index" :title="item.title">
            </van-tab>
          </van-tabs>
        </div>
      </div>
    </van-sticky>
    <!-- tab -->
    <Suspense>
      <component
        :is="currentComponent"
        :headerList="headerList"
        :key="currentIndex"
      ></component>
      <template #fallback>
        <div class="loading-container">
          <van-loading type="spinner" color="#18c8ff" />
        </div>
      </template>
    </Suspense>
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
import { computed, onMounted, ref, watch, defineAsyncComponent } from 'vue'

// Lazy load components
const SecondContract = defineAsyncComponent(() => 
  import('./components/tradeSecondContract.vue')
)
const BBTrading = defineAsyncComponent(() => 
  import('./components/tradeBBTrading.vue')
)
const Ustandard = defineAsyncComponent(() => 
  import('./components/tradeUstandard.vue')
)

// tabs
const headerList = computed(() => {
  return mainStore.getTradeHeaderList.filter(item => item.componentName != 'Optional')
})

const currentIndex = ref(
  mainStore.tradeFlag - mainStore.isOption < 0 ? 0 : mainStore.tradeFlag - mainStore.isOption
)

// Optimize component switching with memoization
const currentComponent = computed(() => {
  const componentName = headerList.value[currentIndex.value]?.componentName
  if (componentName === 'SecondContract') return SecondContract
  if (componentName === 'BBTrading') return BBTrading
  if (componentName === 'Ustandard') return Ustandard
  return null
})

// Debounced tab change handler
let debounceTimer = null
const handleTabChange = (newIndex) => {
  if (debounceTimer) clearTimeout(debounceTimer)
  
  debounceTimer = setTimeout(() => {
    mainStore.setTradeFlag(newIndex)
    // Only call API if needed
    if (!tradeStore.hasLoadedCoinList) {
      tradeStore.getCoinList()
    }
  }, 100)
}

watch(currentIndex, handleTabChange, { immediate: true })

onMounted(() => {
  // Prefetch common data
  if (!tradeStore.hasLoadedCoinList) {
    tradeStore.getCoinList()
  }
})
</script>

<style lang="scss" scoped>
.trade-container {
  background: #121212;
  min-height: 100vh;
}

.loading-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200px;
}

.headerList {
  height: 50px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  display: flex;
  align-items: center;
  background: #121212;
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
    flex: 1;

    :deep(.van-tabs__wrap) {
      height: 49px;
    }

    :deep(.van-tabs__nav) {
      background: transparent;
    }

    :deep(.van-tab) {
      flex: none;
      font-size: 16px;
      margin-right: 20px;
      padding: 0;
      color: #8b8b8b;
      background: transparent;
      position: relative;
      transition: all 0.3s ease;
    }

    :deep(.van-tab--active) {
      font-weight: 500;
    }

    :deep(.van-tab--active .van-tab__text) {
      background: linear-gradient(180deg, #18c8ff 0%, #933ffe 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
      color: transparent !important;
    }

    :deep(.van-tabs__line) {
      background: linear-gradient(180deg, #18c8ff 0%, #933ffe 100%);
      bottom: 12px;
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
