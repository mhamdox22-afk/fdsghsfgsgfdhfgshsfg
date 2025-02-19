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
    <SecondContractContent :coinInfo="coinInfo" ref="childComp" class="contract-content"></SecondContractContent>
    <!-- 看涨看跌 -->
    <UpsAndDowns :coinInfo="coinInfo" @eventBusBrother="eventBusBrother" class="ups-and-downs"></UpsAndDowns>
    <!-- 左侧切换币种 -->
    <PublicPopup
      v-model:show="sidePopup"
      @handelClose="sidePopup = false"
      :direction="`left`"
      :height="`100%`"
      :width="`80%`"
      :showHeader="false"
      :empty="false"
      class="side-popup"
    >
      <template #emptyContentCustomize>
        <LeftSide @close="sidePopup = false" :headerList="headerList" class="left-side"></LeftSide>
      </template>
    </PublicPopup>
  </div>
</template>

<script setup>
import SecondContractHeader from './secondContract/header/index.vue' // 秒合约header部分
import SecondContractContent from './secondContract/content/index.vue' // 秒合约内容部分
import UpsAndDowns from './secondContract/content/upsAndDowns.vue' //涨跌
import PublicPopup from '@/components/Popup/public.vue'
import LeftSide from './common/leftSide.vue'
import { showToast } from 'vant'
import { _t18 } from '@/utils/public'
import { setCollect, removeCollect } from '@/api/trade'
import { useTradeStore } from '@/store/trade'
const tradeStore = useTradeStore()
import { useUserStore } from '@/store/user'
const userStore = useUserStore()
import { useRoute } from 'vue-router'
import { onMounted } from 'vue'
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
// 监听路由展示对应币种信息
watch(
  () => $route.query.symbol,
  (val) => {
    coinInfo.value = tradeStore.secondContractCoinList.filter((item, index) => {
      return item.coin === val
    })[0]
  },
  {
    deep: true
  }
)
// 初始化展示币种信息
const init = () => {
  if ($route.query.symbol) {
    coinInfo.value = tradeStore.secondContractCoinList.filter((item, index) => {
      return item.coin === $route.query.symbol
    })[0]
    if (!coinInfo.value) {
      coinInfo.value = tradeStore.secondContractCoinList[0]
    }
  } else {
    coinInfo.value = tradeStore.secondContractCoinList[0]
  }
}
onMounted(() => {
  init()
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
  userStore.getUserInfo()
  childComp.value.updateList()
}
</script>

<style lang="scss" scoped>
.trade-second-contract {
  background: #1a1a1a;
  min-height: 100vh;
  color: #fff;

  .contract-header {
    background: #242424;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
    position: sticky;
    top: 0;
    z-index: 10;
    transition: all 0.3s ease;

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
    animation: slideUp 0.5s ease;
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
    animation: slideUp 0.6s ease;
    z-index: 9;
  }

  .side-popup {
    .left-side {
      background: #242424;
      height: 100%;
      animation: slideRight 0.3s ease;
    }
  }
}

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

// 添加深色主题的全局变量
:root {
  --primary-bg: #1a1a1a;
  --secondary-bg: #242424;
  --text-primary: #ffffff;
  --text-secondary: #b3b3b3;
  --accent-color: #2196f3;
  --border-color: #333333;
}

// 添加滚动条样式
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
