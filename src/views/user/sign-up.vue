<!-- 注册 -->
<template>
  <HeaderBar
    :currentName="_t18('login_toRegister')"
    :border_bottom="true"
  ></HeaderBar>
  <div class="register-container">
    <div class="tabs">
      <div 
        v-for="(tab, index) in registerTabs" 
        :key="tab.id" 
        class="tab-item"
        :class="{ active: active === index }"
        @click="active = index"
      >
        {{ tab.title }}
      </div>
    </div>
    <div class="tab-content">
      <component :is="currentComponent" />
    </div>
  </div>
</template>

<script setup>
import HeaderBar from '@/components/HeaderBar/index.vue'

import { ref, computed } from 'vue'
import { _t18 } from '@/utils/public'
import { useMainStore } from '@/store'
import Header from './components/signHeader.vue'
import AccountRegister from './components/sign-up/AccountRegister.vue'
import EmailRegister from './components/sign-up/EmailRegister.vue'
import MobileRegister from './components/sign-up/MobileRegister.vue'

const mainStore = useMainStore()
const active = ref(0)

// 注册方式列表
const registerTabs = computed(() => {
  const tabs = [
    mainStore.getLoginMethodList.ordinaryIsOpen && {
      id: 1,
      title: _t18('register_account', ['bitmake']),
      componentName: AccountRegister
    },
    mainStore.getLoginMethodList.emailIsOpen && {
      id: 2,
      title: _t18('register_email'),
      componentName: EmailRegister
    },
    mainStore.getLoginMethodList.phoneIsOpen && {
      id: 3,
      title: _t18('register_mobile'),
      componentName: MobileRegister
    }
  ].filter(Boolean)

  return ['coinsexpto'].includes(__config._APP_ENV) ? tabs.reverse() : tabs
})

// 当前激活的组件
const currentComponent = computed(() => registerTabs.value[active.value].componentName)
</script>

<style lang="scss" scoped>
.register-container {
  padding: 30px 15px 0;
}

.tabs {
  display: flex;
  gap: 20px;
  position: relative;
  padding: 0 4px;
  
  &::after {
    content: '';
    position: absolute;
    left: 0;
    bottom: -1px;
    width: 100%;
    height: 1px;
    background: linear-gradient(
      90deg,
      rgba(var(--ex-line-color-rgb), 0.05) 0%,
      rgba(var(--ex-line-color-rgb), 0.15) 50%,
      rgba(var(--ex-line-color-rgb), 0.05) 100%
    );
  }
}

.tab-item {
  position: relative;
  padding: 0 4px 12px;
  font-size: 16px;
  color: var(--ex-home-list-ftcolor);
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  white-space: nowrap;

  &.active {
    color: var(--ex-theme-color);
    font-weight: 500;

    &::after {
      content: '';
      position: absolute;
      bottom: -1px;
      left: 0;
      width: 100%;
      height: 2px;
      background: linear-gradient(
        90deg,
        var(--ex-theme-color) 0%,
        rgba(var(--ex-theme-color-rgb), 0.8) 50%,
        var(--ex-theme-color) 100%
      );
      border-radius: 4px;
      box-shadow: 0 1px 8px rgba(var(--ex-theme-color-rgb), 0.2);
    }
  }

  &:hover {
    color: var(--ex-theme-color);
    transform: translateY(-1px);

    &:not(.active)::after {
      content: '';
      position: absolute;
      bottom: -1px;
      left: 0;
      width: 100%;
      height: 2px;
      background: linear-gradient(
        90deg,
        transparent,
        rgba(var(--ex-theme-color-rgb), 0.3),
        transparent
      );
      border-radius: 4px;
    }
  }
}

.tab-content {
  padding-top: 24px;
  animation: fadeIn 0.3s ease-in-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
