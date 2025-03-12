<template>
  <div class="register-container">
    <div class="form-wrapper">
      <div class="formData">
        <p class="label">{{ _t18('account', ['bitmake']) }}</p>
        <div class="input-group">
          <input type="text" :placeholder="_t18('login_please')" v-model="formData1.username" />
        </div>
      </div>
      
      <div class="formData">
        <p class="label">{{ _t18('login_pwd') }}</p>
        <div class="input-group">
          <input
            :type="showk ? 'text' : 'password'"
            :placeholder="_t18('login_please')"
            v-model="formData1.password"
          />
          <svg-load class="eye-icon" :name="showk ? 'yanjin-k' : 'yanjin-g'" @click="showk = !showk"></svg-load>
        </div>
      </div>

      <div class="formData">
        <p class="label">{{ _t18('register_pwd_require') }}</p>
        <div class="input-group">
          <input
            :type="requireShowk ? 'text' : 'password'"
            :placeholder="_t18('login_please')"
            v-model="formData1.password2"
            @input="inputPass"
          />
          <svg-load 
            class="eye-icon"
            :name="requireShowk ? 'yanjin-k' : 'yanjin-g'"
            @click="requireShowk = !requireShowk"
          ></svg-load>
        </div>
      </div>
      <p class="requirePass error-text" v-if="requirePass">*{{ _t18('register_pwd_diff') }}</p>

      <div class="formData">
        <p class="label">
          {{ _t18('register_invitation') }}
          <i v-if="REGISTER_REQUIRED_ACTIVECODE.includes(_getConfig('_APP_ENV'))"
            >({{ _t18('required') }})</i
          >
          <i v-else
            >({{
              _t18(['tokencan'].includes(_getConfig('_APP_ENV')) ? 'register_optional' : 'optional')
            }})</i
          >
        </p>
        <div class="input-group">
          <input type="text" :placeholder="_t18('login_please')" v-model="formData1.invitCode" />
        </div>
      </div>

      <div class="formData" v-if="mainStore.getISCode">
        <p class="label">{{ _t18('login_code') }}</p>
        <div class="input-group">
          <input type="text" :placeholder="_t18('login_please')" v-model="formData1.code" />
          <span class="captcha-wrapper" @click="refreshCode">
            <img :src="codeUrl" />
          </span>
        </div>
      </div>

      <Footer :type="1" :formDataToRegister="formData1" @refersh="refreshCode"></Footer>
    </div>
  </div>
</template>

<script setup>
import Footer from './../signFooter.vue'
import { _t18 } from '@/utils/public'
import { REGISTER_REQUIRED_ACTIVECODE } from '@/config'
import { useMainStore } from '@/store/index.js'
import { useRoute } from 'vue-router'
const route = useRoute()
const mainStore = useMainStore()
/**
 * 表单数据
 */
const formData1 = ref({
  type: 1, //自定义注册类型：1普通2邮箱3手机
  username: '', //用户名/邮箱/手机号
  password: '', //密码
  password2: '', //二次密码
  invitCode: route.query.invite_code, //邀请码
  code: '' //验证码
})
/**
 * 图形验证码
 */
// 时间戳
const timestamp = ref(+new Date())
// 图形验证码地址
const codeUrl = computed(
  () => `${mainStore.verificationCodeUrl}?codeType=REGISTER&timestamp=${timestamp.value}`
)
// 刷新验证码
const refreshCode = () => {
  timestamp.value = +new Date()
}
const requirePass = ref(false)
const inputPass = () => {
  requirePass.value = formData1.value.password2 !== formData1.value.password ? true : false
}
// 眼睛
const showk = ref(false)
const requireShowk = ref(false)
import './../style.scss'
</script>

<style lang="scss" scoped>
.register-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 100%);
  padding: 20px;
  
  .form-wrapper {
    max-width: 480px;
    margin: 0 auto;
    padding: 30px 20px;
    border-radius: 16px;
    background: rgba(255, 255, 255, 0.05);
    backdrop-filter: blur(10px);
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
    animation: fadeIn 0.5s ease-out;
  }

  .formData {
    margin-bottom: 24px;
    
    .label {
      color: #ffffff;
      font-size: 16px;
      margin-bottom: 8px;
      font-weight: 500;
      
      i {
        color: rgba(255, 255, 255, 0.6);
        font-size: 14px;
        font-style: normal;
      }
    }

    .input-group {
      position: relative;
      
      input {
        width: 100%;
        height: 48px;
        background: rgba(255, 255, 255, 0.1);
        border: 1px solid rgba(255, 255, 255, 0.1);
        border-radius: 8px;
        padding: 0 16px;
        color: #ffffff;
        font-size: 16px;
        transition: all 0.3s ease;
        
        &::placeholder {
          color: rgba(255, 255, 255, 0.4);
        }
        
        &:focus {
          border-color: #4a90e2;
          box-shadow: 0 0 0 2px rgba(74, 144, 226, 0.2);
          background: rgba(255, 255, 255, 0.15);
        }
      }

      .eye-icon {
        position: absolute;
        right: 16px;
        top: 50%;
        transform: translateY(-50%);
        cursor: pointer;
        opacity: 0.7;
        transition: opacity 0.2s;
        
        &:hover {
          opacity: 1;
        }
      }
    }
  }

  .error-text {
    color: #ff4d4f;
    font-size: 14px;
    margin-top: -16px;
    margin-bottom: 24px;
    animation: shake 0.5s ease-in-out;
  }

  .captcha-wrapper {
    position: absolute;
    right: 8px;
    top: 50%;
    transform: translateY(-50%);
    height: 32px;
    border-radius: 4px;
    overflow: hidden;
    cursor: pointer;
    transition: transform 0.2s;
    
    &:hover {
      transform: translateY(-50%) scale(1.05);
    }
    
    img {
      height: 100%;
      display: block;
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

@keyframes shake {
  0%, 100% { transform: translateX(0); }
  25% { transform: translateX(-5px); }
  75% { transform: translateX(5px); }
}
</style>
