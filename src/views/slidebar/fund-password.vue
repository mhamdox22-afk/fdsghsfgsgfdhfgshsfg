<script setup>
import Success from './components/success.vue'
import HeaderBar from '@/components/HeaderBar/index.vue'
import ButtonBar from '@/components/common/ButtonBar/index.vue'
import SetForm from './components/fund-password/SetForm.vue'
import ChangeForm from './components/fund-password/ChangeForm.vue'
import { useUserStore } from '@/store/user/index'
import { storeToRefs } from 'pinia'
import { showToast } from 'vant'
import { _t18 } from '@/utils/public'
import { useToast } from '@/hook/useToast'
const { _toast } = useToast()
const userStore = useUserStore()
userStore.getUserInfo()
// 用户信息
const { userInfo } = storeToRefs(userStore)
const cuttentRight = { iconRight: [{ iconName: 'kefu', clickTo: 'event_serviceChange' }] }
// 用户是否设置过资金密码(userInfo.detail?.userTardPwd)

// const notPwd = ref(true)
const notPwd = ref(userInfo.value.detail?.userTardPwd)
const success = ref(true)
const changePwd = () => {
  notPwd.value = true
  success.value = false
  userStore.getUserInfo()
}
const setPwd = (v) => {
  success.value = v
  notPwd.value = v
  userStore.getUserInfo()
}
const toCustorm = () => {
  // showToast('请联系客服')
  _toast('custorm_service')
}
</script>
<template>
  <div class="fund-password-container">
    <!-- 导航条 -->
    <HeaderBar
      :currentName="notPwd ? _t18('Change_security_password') : _t18('sidebar_tardPwd',['bitmake'])"
      :cuttentRight="cuttentRight"
      :border_bottom="true"
      class="header-animate"
    ></HeaderBar>

    <!-- 没有设置资金密码 -->
    <div class="form-container">
      <SetForm v-if="!notPwd && success" @setPwd="setPwd" class="form-animate"></SetForm>
      <ChangeForm v-if="notPwd && !success" @setPwd="setPwd" class="form-animate"></ChangeForm>
      <Success
        :text="_t18('Fund_password_has_set')"
        :imgUrl="'defi/fund.png'"
        v-if="notPwd && success"
        class="success card-animate"
      >
        <template #btn>
          <div class="btnBox" @click="changePwd">
            <ButtonBar :btnValue="_t18('Change_security_password')" class="button-animate"/>
          </div>
          <div class="forgot hover-animate" @click="toCustorm">
            {{_t18('forgot_security_code')}}?
          </div>
        </template>
      </Success>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.fund-password-container {
  min-height: 100vh;
  background: linear-gradient(145deg, #1a1a1a, #2d2d2d);
  color: #ffffff;
}

.form-container {
  padding: 20px;
}

.success {
  padding: 20px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 16px;
  backdrop-filter: blur(10px);
  box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.2);
  transition: transform 0.3s ease;

  &:hover {
    transform: translateY(-2px);
  }

  :deep(img) {
    margin: 100px 0 0 0;
    filter: drop-shadow(0 4px 8px rgba(0, 0, 0, 0.2));
    animation: float 3s ease-in-out infinite;
  }

  :deep(.text) {
    font-size: 16px;
    margin: 30px 0 40px 0;
    color: #ffffff;
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }

  .btnBox {
    width: 100%;
    font-size: 14px;

    :deep(.van-button) {
      background: linear-gradient(90deg, #3366ff, #5c85ff);
      border: none;
      box-shadow: 0 4px 15px rgba(51, 102, 255, 0.2);
    }
  }

  .forgot {
    margin-top: 25px;
    text-align: center;
    font-size: 14px;
    color: #a0a0a0;
    transition: color 0.3s ease;

    &:hover {
      color: #ffffff;
    }
  }
}

// 动画类
.header-animate {
  animation: slideDown 0.5s ease-out;
}

.form-animate {
  animation: fadeIn 0.5s ease-out;
}

.card-animate {
  animation: scaleIn 0.5s ease-out;
}

.button-animate {
  transition: transform 0.2s ease;
  
  &:active {
    transform: scale(0.98);
  }
}

.hover-animate {
  transition: all 0.3s ease;
  
  &:hover {
    transform: translateY(-1px);
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  }
}

// 关键帧动画
@keyframes float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

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

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
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
</style>
