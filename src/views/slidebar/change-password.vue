<script setup>
import HeaderBar from '@/components/HeaderBar/index.vue'
import SetForm from './components/change-password/SetForm.vue'
import EmailForm from './components/change-password/EmailForm.vue'
import AccountForm from './components/change-password/AccountForm.vue'
import {useUserStore} from '@/store/user'
import {storeToRefs} from 'pinia'
import {_t18} from '@/utils/public'
import {useToast} from '@/hook/useToast'

const {_toast} = useToast()
const userStore = useUserStore()
const {userInfo} = storeToRefs(userStore)

// 进入路由已请求  ---> 用户数据
// userStore.getUserInfo()
// ??
const notPwd = ref(false)
//修改登录密码的方式(true普通，false邮箱)
const updateLoginPwdMethod = ref(true)
//是否已有登录密码
const loginPassword = ref(userInfo.value.user?.loginPassword || '')
//导航数据
const cuttentRight = {iconRight: [{iconName: 'kefu', clickTo: 'event_serviceChange'}]}

const changeMethod = () => {

  // 是否绑定邮箱
  if (!userInfo.value.user?.email) {
    return _toast('please_bind_email')
  }

  updateLoginPwdMethod.value = !updateLoginPwdMethod.value

}
</script>

<template>
  <div class="change-password-container">
    <!-- 导航条 -->
    <HeaderBar
        :currentName="notPwd ? _t18('sidebar_loginPwd',['bitmake']) : _t18('password_set')"
        :cuttentRight="cuttentRight"
        :border_bottom="true"
        class="header-animate"
    ></HeaderBar>
    
    <div class="form-container">
      <!-- 添加登录密码 -->
      <transition name="fade">
        <SetForm v-if="!loginPassword && updateLoginPwdMethod" class="form-card"></SetForm>
      </transition>
      <!-- 修改登录密码 -->
      <transition name="fade">
        <AccountForm v-if="loginPassword && updateLoginPwdMethod" class="form-card"></AccountForm>
      </transition>
      <transition name="fade">
        <EmailForm v-if="loginPassword &&  !updateLoginPwdMethod" class="form-card"></EmailForm>
      </transition>
    </div>

    <transition name="slide-up">
      <div class="box" v-if="loginPassword">
        <div class="set-button" @click="changeMethod" v-if="!updateLoginPwdMethod">
          {{ _t18('password_update_pwd') }}
        </div>
        <div class="set-button" @click="changeMethod" v-else>
          {{ _t18('password_update_email') }}
        </div>
      </div>
    </transition>
  </div>
</template>

<style lang="scss" scoped>
.change-password-container {
  min-height: 100vh;
  background: #1a1a1a;
  color: #ffffff;
}

.header-animate {
  animation: slideDown 0.3s ease-out;
}

.form-container {
  padding: 20px;
}

.form-card {
  background: #2a2a2a;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
  transform-origin: top;
  animation: scaleIn 0.3s ease-out;
}

.box {
  padding: 0 20px;
  
  .set-button {
    background: #333333;
    margin-top: 20px;
    padding: 15px;
    border-radius: 8px;
    text-align: left;
    font-size: 14px;
    color: #ffffff;
    transition: all 0.3s ease;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    
    &:active {
      transform: scale(0.98);
      background: #404040;
    }
  }
}

// 动画定义
@keyframes slideDown {
  from {
    transform: translateY(-20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

@keyframes scaleIn {
  from {
    transform: scale(0.95);
    opacity: 0;
  }
  to {
    transform: scale(1);
    opacity: 1;
  }
}

// 过渡动画
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.slide-up-enter-active,
.slide-up-leave-active {
  transition: all 0.3s ease;
}

.slide-up-enter-from {
  transform: translateY(20px);
  opacity: 0;
}

.slide-up-leave-to {
  transform: translateY(-20px);
  opacity: 0;
}
</style>
