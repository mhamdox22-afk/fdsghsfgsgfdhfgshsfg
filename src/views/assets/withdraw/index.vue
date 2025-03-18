<!-- 提现 -->
<template>
  <HeaderBar :currentName="_t18('withdraw', ['latcoin'])" class="header-bar"></HeaderBar>
  <div class="withdraw-container">
    <List :data="coinList" class="withdraw-list"></List>
  </div>
</template>
<script setup>
import { _t18 } from '@/utils/public'
import List from './withdraw-list.vue'
import { filterCoin2 } from '@/utils/public'
import { useMainStore } from '@/store/index.js'
const mainStore = useMainStore()
import { storeToRefs } from 'pinia'
import { useUserStore } from '@/store/user/index'
import { onMounted } from 'vue'
const userStore = useUserStore()
// 用户余额信息
const { asset } = storeToRefs(userStore)
// 提现方式列表
const coinList = computed(() => {
  let list = []
  mainStore.getWithdrawList.forEach((item, index) => {
    // status:0关闭，1开启
    if (item.status == '1') {
      let obj = {}
      obj['type'] = 1 //提现
      obj['ratio'] = item.ratio || '' //手续费
      obj['fee'] = item.fee //固定手续费
      // type:0数据货币，1银行卡
      if (item.type == 0) {
        obj['icon'] = item.rechargeType
        obj['title'] = item.rechargeName
      } else {
        obj['icon'] = 'card'
        // 银行卡提现
        obj['title'] = _t18('withdraw_card')
        obj['card'] = filterCoin2(item.rechargeType).toLocaleUpperCase()
      }

      list.push(obj)
    }
  })
  return list
})
// const coinList = computed(() => {
//   let list = []
//   list = [
//     { icon: 'usdt', type: '1', title: 'USDT - TRC' },
//     { icon: 'usdt', type: '1', title: 'USDT - ERC' },
//     { icon: 'btc', type: '1', title: 'BTC' },
//     { icon: 'eth', type: '1', title: 'ETH - ERC' },
//     { icon: 'usdc', type: '1', title: 'USDC' },
//     { icon: 'card', type: '2', title: '银行卡提现' }
//   ]
//   return list
// })
</script>

<style lang="scss" scoped>
.withdraw-container {
  min-height: 100vh;
  position: relative;
  top: -20px;
  padding: 16px;

  
  .withdraw-list {
    :deep(.list-item) {
      background: rgba(255, 255, 255, 0.05);
      border-radius: 16px;
      padding: 20px;
      margin-bottom: 16px;
      box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
      backdrop-filter: blur(10px);
      transition: all 0.3s ease;
      border: 1px solid rgba(255, 255, 255, 0.1);
      
      &:active {
        transform: scale(0.98);
      }
      
      &:hover {
        background: rgba(255, 255, 255, 0.08);
        box-shadow: 0 12px 20px rgba(0, 0, 0, 0.25);
      }

      .icon {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        background: rgba(255, 255, 255, 0.1);
        padding: 8px;
        margin-right: 16px;
        transition: all 0.3s ease;
      }

      .title {
        color: #ffffff;
        font-size: 16px;
        font-weight: 500;
        letter-spacing: 0.5px;
      }

      .subtitle {
        color: rgba(255, 255, 255, 0.6);
        font-size: 14px;
        margin-top: 4px;
      }
    }
  }
}

.header-bar {
  :deep(.nav-bar) {
    background: #1a1a1a;
    color: #ffffff;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
    
    .title {
      font-weight: 500;
      letter-spacing: 0.5px;
    }
    
    .back-icon {
      color: #ffffff;
    }
  }
}

// 添加页面进入动画
.withdraw-container {
  animation: fadeIn 0.3s ease-out;
}

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

// 添加列表项进入动画
.list-item {
  animation: slideIn 0.4s ease-out forwards;
  opacity: 0;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateX(-20px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

// 为不同延迟的列表项添加动画
@for $i from 1 through 10 {
  .list-item:nth-child(#{$i}) {
    animation-delay: #{$i * 0.1}s;
  }
}
</style>
