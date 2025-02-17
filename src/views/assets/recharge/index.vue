<!-- 快捷充币 -->
<template>
  <div class="recharge-container">
    <HeaderBar :currentName="_t18('recharge_fast', ['aams', 'robinhood2'])" class="header-animate"></HeaderBar>
    <div class="list-wrapper">
      <List :data="coinList"></List>
    </div>
  </div>
</template>

<script setup>
import { dispatchCustomEvent } from '@/utils'
import { _t18 } from '@/utils/public'
import List from './recharge-list.vue'
import { filterCoin2 } from '@/utils/public'
import { useMainStore } from '@/store/index.js'
const mainStore = useMainStore()

/**
 * 充值方式列表
 * [{ icon: 'usdt', type: '0', title: 'USDT - ERC' ,address:'111111'},{ icon: 'btc', type: '0', title: 'BTC' ,address:'222222'},]
 */

const coinList = computed(() => {
  return mainStore.getRechargeList.map((item, index) => {
    console.log(item.coinName)
    return {
      icon: filterCoin2(item.coin),
      type: 0, //充值
      title: item.coinName,
      address: item.address
    }
  })
})
</script>

<style lang="scss" scoped>
// 添加深色主题变量
:root {
  --bg-primary: #1a1a1a;
  --bg-secondary: #2d2d2d;
  --text-primary: #ffffff;
  --text-secondary: #b3b3b3;
  --accent-color: #3d5afe;
}

.recharge-container {
  min-height: 100vh;
  background: linear-gradient(145deg, #1a1a1a, #2d2d2d);
  color: var(--text-primary);
  padding: 16px;

  // 添加页面进入动画
  animation: fadeIn 0.3s ease-in-out;
  
  @keyframes fadeIn {
    from {
      opacity: 0;
      transform: translateY(20px);
    }
    to {
      opacity: 1;
      transform: translateY(0);
    }
  }
}

.header-animate {
  margin-bottom: 20px;
  background: rgba(45, 45, 45, 0.8);
  backdrop-filter: blur(10px);
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 10px 40px rgba(61, 90, 254, 0.2);
  }
}

.list-wrapper {
  // 列表容器样式
  background: rgba(45, 45, 45, 0.5);
  border-radius: 16px;
  padding: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(8px);

  // 添加hover效果
  &:hover {
    transform: scale(1.01);
    transition: transform 0.3s ease;
  }

  // 添加列表项动画
  :deep(.list-item) {
    background: rgba(26, 26, 26, 0.8);
    border-radius: 12px;
    margin-bottom: 12px;
    padding: 16px;
    transition: all 0.3s ease;
    
    &:hover {
      transform: translateX(5px);
      box-shadow: 0 4px 20px rgba(61, 90, 254, 0.15);
    }

    // 列表项文字样式
    .title {
      color: var(--text-primary);
      font-weight: 500;
    }

    .address {
      color: var(--text-secondary);
      font-size: 0.9em;
    }
  }
}

// 原来的 .custorm 样式（如果需要的话）可以保留并修改为深色主题
.custorm {
  background: rgba(45, 45, 45, 0.5);
  border-radius: 12px;
  padding: 15px;
  margin-bottom: 30px;
  backdrop-filter: blur(8px);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
  transition: all 0.3s ease;

  &:hover {
    transform: translateX(5px);
    box-shadow: 0 8px 32px rgba(61, 90, 254, 0.2);
  }

  .left,
  .right {
    display: flex;
    align-items: center;
    
    .service-img {
      font-size: 20px;
      width: 20px;
      height: 20px;
      margin-right: 15px;
    }
    
    .text {
      font-size: 14px;
      color: var(--text-primary);
    }
    
    .jiantou {
      font-size: 10px;
      color: var(--text-secondary);
    }
  }
}
</style>
