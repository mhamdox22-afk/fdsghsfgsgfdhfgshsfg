<template>
  <div class="mobile-register">
    <div class="form-container">
      <!-- 手机号 -->
      <div class="form-group">
        <p class="label">{{ _t18('login_mobileCode') }}</p>
        <div class="input-wrapper">
          <input type="text" :placeholder="_t18('login_mobileCode')" v-model="formData3.mobile" />
          <div class="area-code" @click="shouAreaCode">
            <i>+</i>
            <p>{{ formData3.areaCode }}</p>
            <svg-load name="jiantou10x5-x" class="arrow-icon"></svg-load>
          </div>
        </div>
      </div>

      <!-- 密码 -->
      <div class="form-group">
        <p class="label">{{ _t18('login_pwd') }}</p>
        <div class="input-wrapper">
          <input
            :type="showk ? 'text' : 'password'"
            :placeholder="_t18('login_please')"
            v-model="formData3.password"
          />
          <svg-load 
            :name="showk ? 'yanjin-k' : 'yanjin-g'" 
            @click="showk = !showk"
            class="eye-icon"
          ></svg-load>
        </div>
      </div>

      <!-- 确认密码 -->
      <div class="form-group">
        <p class="label">{{ _t18('register_pwd_require') }}</p>
        <div class="input-wrapper">
          <input
            :type="requireShowk ? 'text' : 'password'"
            :placeholder="_t18('login_please')"
            v-model="formData3.password2"
            @input="inputPass"
          />
          <svg-load
            :name="requireShowk ? 'yanjin-k' : 'yanjin-g'"
            @click="requireShowk = !requireShowk"
            class="eye-icon"
          ></svg-load>
        </div>
      </div>
      <p class="error-text" v-if="requirePass">*{{ _t18('register_pwd_diff') }}</p>

      <!-- 邀请码 -->
      <div class="form-group">
        <p class="label">
          {{ _t18('register_invitation') }}
          <i v-if="REGISTER_REQUIRED_ACTIVECODE_MOBILE.includes(_getConfig('_APP_ENV'))"
            >({{ _t18('required') }})</i
          >
          <i v-else>({{ _t18('optional') }})</i>
        </p>
        <div class="input-wrapper">
          <input type="text" :placeholder="_t18('login_please')" v-model="formData3.invitCode" />
        </div>
      </div>

      <!-- 验证码 -->
      <div class="form-group">
        <p class="label">{{ _t18('login_code') }}</p>
        <div class="input-wrapper">
          <input type="text" :placeholder="_t18('login_please')" v-model="formData3.code" />
          <div class="verify-btn" v-if="!flag" @click="send()">{{ _t18('login_send') }}</div>
          <div class="verify-btn counting" v-else>
            <van-count-down :time="time" format="ss" @finish="finish" />
          </div>
        </div>
      </div>
    </div>

    <Footer :type="1" :formDataToRegister="formData3" @refersh="refreshCode"></Footer>
    <AreaCode :show="show" @handelClick="close" @handelSelect="select"></AreaCode>
  </div>
</template>

<script setup>
import Footer from './../signFooter.vue'
import { REGISTER_REQUIRED_ACTIVECODE_MOBILE } from '@/config'
import { _t18 } from '@/utils/public'
import { mobileCode } from '@/api/user'
import AreaCode from './../areaCode.vue'
import { useToast } from '@/hook/useToast'
const { _toast } = useToast()
import { useRoute } from 'vue-router'
const route = useRoute()
/**
 * 表单数据
 */
const formData3 = ref({
  type: 3, //自定义注册类型：1普通2邮箱3手机
  mobile: '',
  password: '', //密码
  password2: '', //二次密码
  invitCode: route.query.invite_code, //邀请码
  code: '', //验证码
  areaCode: '93' //区号
})
if (['bitbyex'].includes(__config._APP_ENV)) {
  formData3.value.areaCode = '1'
}
const requirePass = ref(false)
const inputPass = () => {
  requirePass.value = formData3.value.password2 !== formData3.value.password ? true : false
}
// 眼睛
const showk = ref(false)
const requireShowk = ref(false)
// 发送||倒计时
const show = ref(false)
// 选择区号面板
const shouAreaCode = () => {
  show.value = true
}
// 关闭区号面板
const close = () => {
  show.value = false
}
// 选择区号
const select = (val) => {
  formData3.value.areaCode = val
}
/**
 * 倒计时
 */

// 倒计时
const time = ref(0)
const flag = ref(false)
const send = () => {
  // 手机发送验证码
  if (formData3.value.mobile == '') {
    _toast(`login_please_mobileCode`)
    return
  }
  mobileCode('REGISTER', formData3.value.areaCode + formData3.value.mobile).then((res) => {
    if (res.code == '200') {
      flag.value = true
      time.value = 60 * 1000
    }
  })
}
// 倒计时结束
const finish = () => {
  flag.value = false
}
const refreshCode = () => {}
import './../style.scss'
</script>

<style lang="scss" scoped>
.mobile-register {
  min-height: 100vh;
  background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 100%);
  padding: 20px;
  
  .form-container {
    max-width: 500px;
    margin: 0 auto;
    padding: 20px;
  }

  .form-group {
    margin-bottom: 24px;
    animation: fadeInUp 0.5s ease-out;

    @keyframes fadeInUp {
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

  .label {
    color: #ffffff;
    font-size: 14px;
    margin-bottom: 8px;
    font-weight: 500;
    
    i {
      color: #8e8e8e;
      font-size: 12px;
    }
  }

  .input-wrapper {
    position: relative;
    background: rgba(255, 255, 255, 0.05);
    border-radius: 12px;
    backdrop-filter: blur(10px);
    transition: all 0.3s ease;
    
    &:focus-within {
      background: rgba(255, 255, 255, 0.1);
      box-shadow: 0 0 20px rgba(0, 0, 0, 0.2);
      transform: translateY(-2px);
    }

    input {
      width: 100%;
      background: transparent;
      border: none;
      color: #ffffff;
      padding: 15px;
      font-size: 16px;
      
      &::placeholder {
        color: rgba(255, 255, 255, 0.3);
      }
    }

    .eye-icon, .arrow-icon {
      position: absolute;
      right: 15px;
      top: 50%;
      transform: translateY(-50%);
      cursor: pointer;
      color: #ffffff;
      opacity: 0.7;
      transition: opacity 0.3s ease;
      
      &:hover {
        opacity: 1;
      }
    }
  }

  .area-code {
    position: absolute;
    right: 15px;
    top: 50%;
    transform: translateY(-50%);
    display: flex;
    align-items: center;
    color: #ffffff;
    cursor: pointer;
    padding: 5px 10px;
    border-radius: 6px;
    background: rgba(255, 255, 255, 0.1);
    transition: all 0.3s ease;
    
    &:hover {
      background: rgba(255, 255, 255, 0.15);
    }

    i {
      margin-right: 5px;
    }

    .arrow-icon {
      margin-left: 5px;
      position: static;
      transform: none;
    }
  }

  .verify-btn {
    position: absolute;
    right: 10px;
    top: 50%;
    transform: translateY(-50%);
    background: linear-gradient(135deg, #4a90e2 0%, #357abd 100%);
    color: white;
    padding: 8px 15px;
    border-radius: 6px;
    font-size: 14px;
    cursor: pointer;
    transition: all 0.3s ease;
    
    &:hover {
      transform: translateY(-50%) scale(1.05);
      box-shadow: 0 0 15px rgba(74, 144, 226, 0.3);
    }
    
    &.counting {
      background: rgba(255, 255, 255, 0.1);
    }
  }

  .error-text {
    color: #ff4d4f;
    font-size: 12px;
    margin-top: -15px;
    margin-bottom: 20px;
    animation: shake 0.5s ease-in-out;
    
    @keyframes shake {
      0%, 100% { transform: translateX(0); }
      25% { transform: translateX(-5px); }
      75% { transform: translateX(5px); }
    }
  }
}
</style>
