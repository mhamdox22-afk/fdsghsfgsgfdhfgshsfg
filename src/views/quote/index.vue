<!-- 行情页面：自选，秒合约，币币交易，U本位 -->
<template>
  <div>
    <HeaderBar
    :currentName="_t18('quote')"
    :border_bottom="true"
  ></HeaderBar>
    <!-- 搜索 -->
    <div class="search">
      <div class="searchBtn">
        <svg-load name="lujing3047" class="searchLeft"></svg-load>
        <input
          type="text"
          v-model.trim="searchName"
          :placeholder="_t18(`search_currency`)"
          class="searchInput"
        />
      </div>
    </div>
    <!-- tabs -->

    <div class="headerChoose">
      <van-tabs
        swipeable
        shrink
        :swipe-threshold="2"
        v-model:active="currentIndex"
        line-width="20"
        line-height="2"
        color="#17ac74"
        title-active-color="#17AC74"
        title-inactive-color="#5d626d"
        background="var(--ex-div-bgColor8)"
      >
        <van-tab v-for="(item, index) in headerList" :key="index" :title="item.title">
          <component :is="currentComponent" :searchName="searchName"></component>
        </van-tab>
      </van-tabs>
    </div>
    <!-- tab -->
  </div>
</template>

<script setup>
import { _t18 } from '@/utils/public'
import { useMainStore } from '@/store/index'
const mainStore = useMainStore()
import { useTradeStore } from '@/store/trade'

const tradeStore = useTradeStore()
import { computed } from 'vue'
import Optional from './components/quoteOptional.vue'
import SecondContract from './components/quoteSecondContract.vue'
import BBTrading from './components/quoteBBTrading.vue'
import Ustandard from './components/quoteUstandard.vue'
// 搜索
const searchName = ref('')
const searchBtn = () => {}
// tabs
const headerList = computed(() => {
  let tempList = mainStore.getTradeHeaderList
  return tempList
})
const currentIndex = ref(mainStore.tradeFlag + mainStore.isOption)
// 切换组件
const currentComponent = computed(() => {
  let temp = ''
  if (headerList.value[currentIndex.value].componentName == 'Optional') {
    temp = Optional
  } else if (headerList.value[currentIndex.value].componentName == 'SecondContract') {
    temp = SecondContract
  } else if (headerList.value[currentIndex.value].componentName == 'BBTrading') {
    temp = BBTrading
  } else if (headerList.value[currentIndex.value].componentName == 'Ustandard') {
    temp = Ustandard
  }
  return temp
})
watch(
  currentIndex,
  (n) => {
    mainStore.setTradeFlag(n)
  },
  { immediate: true }
)
</script>

<style lang="scss" scoped>
// 搜索
.search {
  padding: 20px 15px 10px;
  animation: fadeInDown 0.5s ease-out;

  .searchBtn {
    height: 34px;
    background: rgba(255, 255, 255, 0.05);
    border-radius: 20px;
    padding: 0 20px;
    display: flex;
    align-items: center;
    justify-content: center;
    backdrop-filter: blur(10px);
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-1px);
      box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
    }

    .searchLeft {
      width: 12px;
      height: 12px;
      margin-right: 10px;
      margin-top: -1.5px;
      opacity: 0.8;
      transition: all 0.3s ease;
    }

    .searchInput {
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 13px;
      background: transparent;
      flex: 1;
      color: rgba(255, 255, 255, 0.9);
      transition: all 0.3s ease;

      &:focus {
        color: #fff;
      }
    }

    input::-webkit-input-placeholder {
      color: rgba(255, 255, 255, 0.5);
    }
  }
}

.headerChoose {
  animation: fadeInUp 0.5s ease-out;
  
  :deep(.van-tabs__nav) {
    background: rgba(18, 18, 18, 0.95) !important;
    backdrop-filter: blur(10px);
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  }

  :deep(.van-tab) {
    flex: none;
    font-size: 14px;
    margin-right: 30px;
    padding: 0;
    color: rgba(255, 255, 255, 0.7) !important;
    background: transparent !important;
    transition: all 0.3s ease;
    position: relative;
    
    &::after {
      content: '';
      position: absolute;
      bottom: 0;
      left: 50%;
      width: 0;
      height: 2px;
      background: #17ac74;
      transition: all 0.3s ease;
      transform: translateX(-50%);
    }
  }

  :deep(.van-tab--active) {
    font-weight: 500;
    color: #fff !important;
    text-shadow: 0 0 20px rgba(23, 172, 116, 0.3);

    &::after {
      width: 20px;
    }
  }
}

// 添加动画关键帧
@keyframes fadeInDown {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeInUp {
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
