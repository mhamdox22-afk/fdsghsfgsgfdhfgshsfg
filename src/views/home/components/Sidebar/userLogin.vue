<template>
  <div class="userMess" v-if="userId">
    <!-- vip等级 -->
    <div class="messLeft" v-if="showVip" @click="$router.push('/vip-rule')">
      <image-load filePath="defi/组 249.png" class="userMessImg"></image-load>
      <p class="fw-num">v{{ vipClass }}</p>
    </div>
    <svg-load name="userImg" class="userMessImg" v-else></svg-load>
    <div class="messRight">
      <Copy :data="userId">
        <template #copyMsg>
          <div class="rightTop fw-bold">UID：{{userId}}</div>
        </template>
      </Copy>
      <!-- 信用分 -->
      <div class="rightBot">
        {{ _t18('credit_score') }}：{{ userStore.userInfo.detail?.credits }}
      </div>
    </div>
  </div>
  <!-- 钱包地址 -->
  <div class="copyName" v-if="address">
    <Copy :data="address">
      <template #copyMsg>
        <p>{{ _hideAddress(address) }}</p>
      </template>
    </Copy>
  </div>
</template>
<script setup>
import { _t18, _hideAddress, _hideLoginName } from '@/utils/public'
import Copy from '@/components/common/Copy/index.vue'
import { useUserStore } from '@/store/user/index'
import { useMainStore } from '@/store/index'
import { computed } from 'vue'
// const vipClass = ref(0) //vip等级
const userStore = useUserStore()
const mainStore = useMainStore()
const userId = computed(() => userStore.userInfo.user?.userId)
const loginName = computed(() => userStore.userInfo.user?.loginName)
const address = computed(() => userStore.userInfo.user?.address)
const userAccount = computed(() => {
  let temp = userStore.userInfo.asset?.filter((item) => {
    return item.symbol == 'usdt' && item.type == 1
  })
  return temp[0]?.availableAmount || 0
})
const showVip = ref(mainStore.getVIPClass?.isOpen)
const vipClass = computed(() => {
  let temp = 0
  let start = ''
  let end = ''
  for (let key in mainStore.getVIPClass) {
    if (userAccount.value > mainStore.getVIPClass[key]) {
      start = key
    }
    if (userAccount.value <= mainStore.getVIPClass[key]) {
      end = key
      break
    }
  }
  if (start.replace('Start', '') === end.replace('End', '')) {
    temp = start.replace('Start', '').replace('vip', '')
  }
  return temp
})
</script>
<style lang="scss" scoped>
.userMess {
  padding: 20px 15px;
  display: flex;
  align-items: center;
  border-radius: 15px;
  margin: 10px 15px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.3);
  transition: all 0.3s ease;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 12px 25px rgba(0, 0, 0, 0.4);
  }

  .userMessImg {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    border: 2px solid rgba(255, 255, 255, 0.1);
    transition: transform 0.3s ease;
    
    &:hover {
      transform: scale(1.05);
    }
  }

  .messRight {
    margin-left: 15px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    padding: 5px 0;

    .rightTop {
      font-size: 16px;
      color: #ffffff;
      margin-bottom: 5px;
      font-weight: 500;
      max-width: 200px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      
      &::before,
      &::after {
        color: rgba(255, 255, 255, 0.6);
      }
    }

    .rightBot {
      font-size: 14px;
      color: rgba(255, 255, 255, 0.7);
    }
  }
}

.copyName {
  margin: 10px 15px;
  font-size: 14px;
  border-radius: 10px;
  padding: 12px 15px;
  color: rgba(255, 255, 255, 0.9) !important;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
  
  &:hover {
    transform: translateX(2px);
    box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
  }

  p {
    margin: 0;
  }
}

.messLeft {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  position: relative;
  
  p {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    font-size: 18px;
    color: #ffffff;
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
  }
}

// 添加暗色主题变量覆盖
:root {
  --ex-default-font-color: #ffffff;
  --ex-passive-font-color: rgba(255, 255, 255, 0.7);
  --ex-home-sidebar-address-bgcolor: #2a2a2a;
  --ex-home-sidebar-address-ftcolor: rgba(255, 255, 255, 0.9);
}
</style>
