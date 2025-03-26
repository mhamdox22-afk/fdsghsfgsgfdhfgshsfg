<!-- 交易——>秒合约 -->
<template>
  <div class="trade-second-contract">
    <!-- 头部 -->
    <SecondContractHeader
      :coinInfo="coinInfo"
      @showSidePopup="showSidePopup"
      class="contract-header"
    ></SecondContractHeader>
    <!-- k线，委托数据 -->
    <KeepAlive>
      <SecondContractContent :coinInfo="coinInfo" ref="childComp" class="contract-content"></SecondContractContent>
    </KeepAlive>
    <!-- 看涨看跌 -->
    <!-- 左侧切换币种 -->
    <Suspense>
      <PublicPopup
        v-model:show="sidePopup"
        @handelClose="sidePopup = false"
        :direction="`left`"
        :height="`100%`"
        :width="`300px`"
        :showHeader="false"
        :empty="false"
        class="side-popup"
      >
        <template #emptyContentCustomize>
          <Footer @closePopup="sidePopup = false" />
          <!-- <LeftSide v-if="sidePopup" @close="sidePopup = false" :headerList="headerList" class="left-side"></LeftSide> -->
        </template>
      </PublicPopup>
    </Suspense>
  </div>

  <UpsAndDowns :coinInfo="coinInfo" @eventBusBrother="eventBusBrother" class="ups-and-downs"></UpsAndDowns>

</template>

<script setup>
import { ref, watch, onMounted, computed, defineAsyncComponent, nextTick } from 'vue'
import { useRoute } from 'vue-router'
import { showToast } from 'vant'
import { _t18 } from '@/utils/public'
import { setCollect, removeCollect } from '@/api/trade'
import { useTradeStore } from '@/store/trade'
import { useUserStore } from '@/store/user'
import Footer from '../../home/components/FrontPage/footer-trade.vue'

// 懒加载组件
const SecondContractHeader = defineAsyncComponent(() => 
  import('./secondContract/header/index.vue')
)
const SecondContractContent = defineAsyncComponent(() => 
  import('./secondContract/content/index.vue')
)
const UpsAndDowns = defineAsyncComponent(() => 
  import('./secondContract/content/upsAndDowns.vue')
)
const PublicPopup = defineAsyncComponent(() => 
  import('@/components/Popup/public.vue')
)
const LeftSide = defineAsyncComponent(() => 
  import('./common/leftSide.vue')
)

const tradeStore = useTradeStore()
const userStore = useUserStore()
const $route = useRoute()

const props = defineProps({
  headerList: {
    type: Array
  }
})

/**
 * 币种信息
 */
// 当前币种信息
const coinInfo = ref({})

// 使用computed属性减少不必要的重新计算
const currentCoinInfo = computed(() => {
  const symbol = $route.query.symbol
  if (!symbol) return tradeStore.secondContractCoinList[0]
  
  const coin = tradeStore.secondContractCoinList.find(item => item.coin === symbol)
  return coin || tradeStore.secondContractCoinList[0]
})

// 优化监听路由，减少不必要的深度监听
watch(
  () => $route.query.symbol,
  () => {
    // 使用nextTick优化DOM更新时机
    nextTick(() => {
      coinInfo.value = currentCoinInfo.value
    })
  }
)

// 初始化展示币种信息
const init = () => {
  coinInfo.value = currentCoinInfo.value
}

onMounted(() => {
  // 使用requestAnimationFrame优化初始渲染
  window.requestAnimationFrame(() => {
    init()
  })
})

/**
 * 侧边栏
 */
const sidePopup = ref(false)
const showSidePopup = () => {
  sidePopup.value = true
}

/** 子调用兄弟更新委托列表 */
const childComp = ref(null)
const eventBusBrother = () => {
  // 添加防抖，避免频繁更新
  if (eventBusBrother.timer) clearTimeout(eventBusBrother.timer)
  eventBusBrother.timer = setTimeout(() => {
    userStore.getUserInfo()
    childComp.value?.updateList()
  }, 100)
}
</script>

<style lang="scss" scoped>
.trade-second-contract {
  background: #1a1a1a;
  min-height: 100vh;
  color: #fff;
  /* 使用CSS contain优化渲染性能 */
  contain: content;
}

.contract-header {
  background: #242424;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  position: sticky;
  top: 0;
  z-index: 10;
  transition: all 0.3s ease;
  /* 告知浏览器该元素可能有变化，提前优化 */
  will-change: transform;

  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
  }
}

.contract-content {
  margin: 12px;
  background: #242424;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  overflow: hidden;
  /* 使用更高效的transform动画代替 */
  animation: none;
  transform: translateY(0);
  opacity: 1;
  transition: transform 0.5s ease, opacity 0.5s ease;
  will-change: transform, opacity;
}

.ups-and-downs {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #242424;
  padding: 16px;
  box-shadow: 0 -4px 16px rgba(0, 0, 0, 0.2);
  border-radius: 20px 20px 0 0;
  /* 优化动画 */
  will-change: transform;
  z-index: 9;
}

.side-popup {
  overflow-x: hidden;
  .left-side {
    height: 100%;
    /* 优化动画 */
    will-change: transform;
  }
}

/* 减少CSS动画定义，使用更高效的transform */
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

@keyframes slideRight {
  from {
    transform: translateX(-20px);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

// 减少全局变量使用以提高渲染效率，只保留必要的
:root {
  --primary-bg: #1a1a1a;
  --secondary-bg: #242424;
}

// 优化滚动条样式，减少重绘
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-track {
  background: var(--secondary-bg);
}

::-webkit-scrollbar-thumb {
  background: #444;
  border-radius: 3px;
}

::-webkit-scrollbar-thumb:hover {
  background: #555;
}
</style>
