<!-- 登录 -->
<template>
  <div class="sign-in-container">
    <Header :type="0"></Header>
    <div class="main-image">
      <img src="https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcSIVtrWgC9ftS0jl2WrkfbQ5Wwou5JehBX78L1lZ25vCP08V_eM" alt="Login" />
    </div>
    <div class="login-card">
      <div class="login-glow"></div>
      <div class="loginMethod">
        <van-tabs
          v-model:active="active"
          shrink
          color="transparent"
          title-active-color="#17AC74"
          title-inactive-color="#ffffff"
        >
          <van-tab v-for="(item, index) in tabList" :key="index" :title="item.title">
            <component :is="currentComponent"></component>
          </van-tab>
        </van-tabs>
      </div>
    </div>
  </div>
</template>

<script setup>
import { _t18 } from '@/utils/public'
import Header from './components/signHeader.vue'
import AccountLogin from './components/sign-in/AccountLogin.vue'
import EmailLogin from './components/sign-in/EmailLogin.vue'
import MobileLogin from './components/sign-in/MobileLogin.vue'
import { useMainStore } from '@/store/index.js'
/**
 * 登录注册方式
 * [{ id: 1, title: '账号登录', type: 1 },{ id: 2, title: '邮箱登录', type: 2 },{ id: 3, title: '手机登录', type: 3 }]
 */
// 登录列表
const mainStore = useMainStore()
const tabList = computed(() => {
  let list = []
  if (mainStore.getLoginMethodList.ordinaryIsOpen || ['aams'].includes(__config._APP_ENV)) {
    let obj = {}
    obj['id'] = 1
    obj['title'] = _t18('login_account', ['bitmake', 'aams'])
    obj['type'] = '1'
    obj['componentName'] = AccountLogin
    list.push(obj)
  }
  if (mainStore.getLoginMethodList.emailIsOpen && !['aams'].includes(__config._APP_ENV)) {
    let obj = {}
    obj['id'] = 2
    obj['title'] = _t18('login_email')
    obj['type'] = '2'
    obj['componentName'] = EmailLogin
    list.push(obj)
  }
  if (mainStore.getLoginMethodList.phoneIsOpen) {
    let obj = {}
    obj['id'] = 3
    obj['title'] = _t18('login_mobile')
    obj['type'] = '3'
    obj['componentName'] = MobileLogin
    list.push(obj)
  }
  if (['coinsexpto'].includes(__config._APP_ENV)) {
    list = list.reverse()
  }
  return list
})
// 切换组件
const currentComponent = computed(() => {
  return tabList.value[active.value].componentName
})
// 初始组件
const active = ref(0)
</script>
<style lang="scss" scoped>
* {
  color: #ffffff;
  font-size: 14px;
}

.sign-in-container {
  min-height: 100vh;
  width: 100%;
  background: linear-gradient(135deg, #121212, #1e1e1e, #121212);
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    pointer-events: none;
    z-index: 1;
  }
  
  &::after {
    content: '';
    position: absolute;
    width: 200%;
    height: 200%;
    top: -50%;
    left: -50%;
    pointer-events: none;
    opacity: 0.3;
    animation: grain 8s steps(10) infinite;
    z-index: 0;
  }
}

@keyframes grain {
  0%, 100% { transform: translate(0, 0); }
  10% { transform: translate(-5%, -10%); }
  20% { transform: translate(-15%, 5%); }
  30% { transform: translate(7%, -25%); }
  40% { transform: translate(-5%, 25%); }
  50% { transform: translate(-15%, 10%); }
  60% { transform: translate(15%, 0%); }
  70% { transform: translate(0%, 15%); }
  80% { transform: translate(3%, 35%); }
  90% { transform: translate(-10%, 10%); }
}

.login-card {
  position: relative;
  width: 90%;
  max-width: 500px;
  overflow: hidden;
  animation: fadeIn 0.6s ease-out;
  z-index: 2;
  
  &:hover .login-glow {
    opacity: 1;
  }
  
 
}

.login-glow {
  position: absolute;
  width: 100%;
  height: 5px;
  top: 0;
  left: 0;
  opacity: 0.8;
  transition: opacity 0.3s ease;
}

.loginMethod {
  padding: 0;

  :deep(.van-tab__text) {
    font-size: 15px;
    font-weight: 500;
    margin-top: 4px;
    transition: transform 0.2s ease, color 0.2s ease;
  }
  
  :deep(.van-tabs__nav) {
    background: transparent !important;
    border-bottom: 1px solid rgba(255, 255, 255, 0.1);
    margin-bottom: 25px;
  }
  
  :deep(.van-tabs__line) {
    display: none;
  }
  
  :deep(.van-tab--active) {
    font-weight: 600;
    
    .van-tab__text {
      transform: translateY(-2px);
    }
  }
  
  :deep(.van-tab) {
    color: rgba(255, 255, 255, 0.7) !important;
    
    &:hover {
      color: rgba(255, 255, 255, 0.9) !important;
    }
  }
  
  :deep(.van-tab--active) {
    color: #17AC74 !important;
    position: relative;
    
    &::after {
      content: '';
      position: absolute;
      bottom: 0;
      left: 0;
      width: 100%;
      height: 3px;
      background: linear-gradient(90deg, #17AC74, #25d997);
      border-radius: 3px 3px 0 0;
      animation: tabIndicator 0.3s ease-out;
    }
  }
  
  :deep(.van-tabs__nav) {
    padding: 0;
  }
  
  :deep(.van-tab--shrink) {
    padding: 0 20px 15px 0;
  }
  
  :deep(.van-count-down) {
    font-size: 14px;
    color: #ffffff;
    padding: 0;
  }
  
  /* 表单元素样式覆盖 */
  :deep(input), :deep(button), :deep(.van-field) {
    background: rgba(255, 255, 255, 0.05) !important;
    border: 1px solid rgba(255, 255, 255, 0.1) !important;
    color: white !important;
    border-radius: 8px !important;
    transition: all 0.3s ease !important;
    backdrop-filter: blur(5px);
    
    &:focus, &:hover {
      background: rgba(255, 255, 255, 0.1) !important;
      box-shadow: 0 0 0 2px rgba(23, 172, 116, 0.3) !important;
      border-color: rgba(23, 172, 116, 0.5) !important;
    }
  }
  
  :deep(.van-field__label) {
    color: rgba(255, 255, 255, 0.8) !important;
  }
  
  :deep(.van-field__control) {
    color: white !important;
  }
  
  :deep(button), :deep(.van-button) {
    background: linear-gradient(90deg, #17AC74, #25d997) !important;
    border: none !important;
    color: white !important;
    font-weight: 600 !important;
    transform: translateY(0) !important;
    transition: all 0.3s ease !important;
    box-shadow: 0 4px 15px rgba(23, 172, 116, 0.4) !important;
    position: relative;
    overflow: hidden;
    
    &::before {
      content: '';
      position: absolute;
      top: -50%;
      left: -50%;
      width: 200%;
      height: 200%;
      background: rgba(255, 255, 255, 0.1);
      transform: rotate(45deg);
      z-index: 1;
      transition: all 0.6s ease;
      opacity: 0;
    }
    
    &:hover {
      transform: translateY(-2px) !important;
      box-shadow: 0 6px 20px rgba(23, 172, 116, 0.5) !important;
      
      &::before {
        left: 100%;
        opacity: 1;
      }
    }
    
    &:active {
      transform: translateY(1px) !important;
      box-shadow: 0 2px 10px rgba(23, 172, 116, 0.3) !important;
    }
  }
  
  /* 增加输入框动画效果 */
  :deep(.van-field__control) {
    position: relative;
    transition: all 0.3s ease;
    
    &:focus {
      transform: scale(1.02);
    }
  }
  
  /* 表单其他元素优化 */
  :deep(.van-checkbox__icon--checked) {
  
  }
  
  :deep(.van-checkbox__label) {
    color: rgba(255, 255, 255, 0.8) !important;
  }
  
  :deep(a), :deep(.link) {
    transition: all 0.3s ease;
    position: relative;
    
    &::after {
      content: '';
      position: absolute;
      bottom: -2px;
      left: 0;
      width: 0;
      height: 1px;
      background: #17AC74;
      transition: width 0.3s ease;
    }
    
    &:hover {
      color: #25d997 !important;
      text-shadow: 0 0 8px rgba(23, 172, 116, 0.5);
      
      &::after {
        width: 100%;
      }
    }
  }
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

@keyframes tabIndicator {
  from {
    width: 0;
    left: 50%;
  }
  to {
    width: 100%;
    left: 0;
  }
}

/* 主图片样式 */
.main-image {
  width: 100%;
  height: 50vh;
  margin: 10px auto;
  text-align: center;
  position: relative;
  z-index: 2;
  display: flex;
  align-items: center;
  justify-content: center;
  
  img {
      height: 100%;
      width: auto;
      object-fit: contain;
      animation: float 6s ease-in-out infinite;
      transition: all 0.5s ease;
      
      &:hover {
        filter: drop-shadow(0 15px 30px rgba(23, 172, 116, 0.5)) brightness(1.1);
        transform: translateY(-5px) scale(1.02);
      }
  }
  
  &::after {
    content: '';
    position: absolute;
    bottom: -10px;
    left: 15%;
    width: 70%;
    height: 10px;
    background: radial-gradient(ellipse at center, rgba(23, 172, 116, 0.3), transparent 70%);
    border-radius: 50%;
    filter: blur(5px);
    animation: shadowPulse 6s ease-in-out infinite;
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

@keyframes shadowPulse {
  0%, 100% {
    opacity: 0.5;
    transform: scaleX(1);
  }
  50% {
    opacity: 0.3;
    transform: scaleX(0.85);
  }
}

/* 响应式调整 */
@media (max-width: 480px) {
  .login-card {
    padding: 0 20px;
  }
  
  .main-image {
    height: 35vh;
    margin: 15px auto;
  }
  
  :deep(.van-tab__text) {
    font-size: 16px;
  }
}
</style>
