<template>
  <div class="email-register">
    <div class="form-container">
      <div class="formData">
        <p class="label">{{ _t18('login_emailCode') }}</p>
        <div class="input-wrapper">
          <input type="text" :placeholder="_t18('login_emailCode')" v-model="formData2.email" />
        </div>
      </div>

      <div v-if="['aams'].includes(_getConfig('_APP_ENV'))" class="formData">
        <p class="label">{{ _t18('login_mobileCode') }}</p>
        <div class="input-wrapper">
          <input type="text" :placeholder="_t18('login_mobileCode')" v-model="formData2.mobile" />
          <div class="area-code" @click="shouAreaCode">
            <i>+</i>
            <p>{{ formData2.areaCode }}</p>
            <svg-load name="jiantou10x5-x" class="arrow-icon"></svg-load>
          </div>
        </div>
      </div>

      <div class="formData">
        <p class="label">{{ _t18('login_pwd') }}</p>
        <div class="input-wrapper">
          <input
            :type="showk ? 'text' : 'password'"
            :placeholder="_t18('login_please')"
            v-model="formData2.password"
          />
          <svg-load class="eye-icon" :name="showk ? 'yanjin-k' : 'yanjin-g'" @click="showk = !showk"></svg-load>
        </div>
      </div>

      <div class="formData">
        <p class="label">{{ _t18('register_pwd_require') }}</p>
        <div class="input-wrapper">
          <input
            :type="requireShowk ? 'text' : 'password'"
            :placeholder="_t18('login_please')"
            v-model="formData2.password2"
            @input="inputPass"
          /><svg-load
            :name="requireShowk ? 'yanjin-k' : 'yanjin-g'"
            @click="requireShowk = !requireShowk"
          ></svg-load>
        </div>
      </div>

      <p class="requirePass" v-if="requirePass">*{{ _t18('register_pwd_diff') }}</p>

      <div class="formData">
        <p class="label">
          {{ _t18('register_invitation') }}
          <i v-if="REGISTER_REQUIRED_ACTIVECODE.includes(_getConfig('_APP_ENV'))"
            >({{ _t18('required') }})</i
          >
          <i v-else>({{ _t18('optional') }})</i>
        </p>
        <div class="input-wrapper">
          <input type="text" :placeholder="_t18('login_please')" v-model="formData2.invitCode" />
        </div>
      </div>

      <div class="formData">
        <p class="label">{{ _t18('login_code') }}</p>
        <div class="input-wrapper">
          <input type="text" :placeholder="_t18('login_please')" v-model="formData2.code" />
          <p class="send-code" v-if="!flag" @click="send()">{{ _t18('login_send') }}</p>
          <p class="countdown" v-else><van-count-down :time="time" format="ss" @finish="finish" /></p>
        </div>
      </div>
    </div>

    <Footer :type="1" :formDataToRegister="formData2" @refersh="refreshCode"></Footer>
    <AreaCode :show="show" @handelClick="close" @handelSelect="select"></AreaCode>
  </div>
</template>

<script setup>
import Footer from './../signFooter.vue'
import { _t18 } from '@/utils/public'
import { emailCode } from '@/api/user'
import { showToast } from 'vant'
import { useToast } from '@/hook/useToast'
import AreaCode from './../areaCode.vue'
import { REGISTER_REQUIRED_ACTIVECODE } from '@/config'
const { _toast } = useToast()
import { useRoute } from 'vue-router'
const route = useRoute()
/**
 * 表单数据
 */
const formData2 = ref({
  type: 2, //自定义注册类型：1普通2邮箱3手机
  email: '',
  password: '', //密码
  password2: '', //二次密码
  invitCode: route.query.invite_code, //邀请码
  code: '', //验证码
  mobile: '', // 手机号
  areaCode: '93' //区号
})
const requirePass = ref(false)
const inputPass = () => {
  requirePass.value = formData2.value.password2 !== formData2.value.password ? true : false
}
// 眼睛
const showk = ref(false)
const requireShowk = ref(false)
/**
 * 倒计时
 */

// 倒计时
const time = ref(0)
const flag = ref(false)
const send = () => {
  // 邮箱发送验证码
  if (formData2.value.email == '') {
    _toast(`login_please_emailCode`)

    return
  }
  emailCode('REGISTER', formData2.value.email).then((res) => {
    if (res.code == '200') {
      flag.value = true
      time.value = 60 * 1000
    } else {
      showToast(res.msg)
    }
  })
}
// 倒计时结束
const finish = () => {
  flag.value = false
}
const refreshCode = () => {}

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
  formData2.value.areaCode = val
}
import './../style.scss'
</script>

<style lang="scss" scoped>
.email-register {
  min-height: 100vh;
  background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 100%);
  padding: 20px;

  .form-container {
    max-width: 480px;
    margin: 0 auto;
    padding: 20px;
  }

  .formData {
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

    .label {
      color: #ffffff;
      font-size: 14px;
      margin-bottom: 8px;
      font-weight: 500;
      opacity: 0.9;
    }

    .input-wrapper {
      position: relative;
      background: rgba(255, 255, 255, 0.05);
      border-radius: 12px;
      backdrop-filter: blur(10px);
      transition: all 0.3s ease;
      
      &:hover {
        background: rgba(255, 255, 255, 0.08);
        transform: translateY(-1px);
      }

      input {
        width: 100%;
        padding: 15px;
        background: transparent;
        border: none;
        color: #ffffff;
        font-size: 16px;
        
        &::placeholder {
          color: rgba(255, 255, 255, 0.5);
        }

        &:focus {
          outline: none;
        }
      }

      .eye-icon, .arrow-icon {
        position: absolute;
        right: 15px;
        top: 50%;
        transform: translateY(-50%);
        width: 20px;
        height: 20px;
        cursor: pointer;
        fill: #ffffff;
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

      i, p {
        margin: 0 3px;
      }
    }

    .send-code {
      position: absolute;
      right: 15px;
      top: 50%;
      transform: translateY(-50%);
      color: #4a9eff;
      cursor: pointer;
      padding: 5px 10px;
      border-radius: 6px;
      transition: all 0.3s ease;

      &:hover {
        background: rgba(74, 158, 255, 0.1);
      }
    }

    .countdown {
      position: absolute;
      right: 15px;
      top: 50%;
      transform: translateY(-50%);
      color: #ffffff;
      opacity: 0.7;
    }
  }

  .requirePass {
    color: #ff4d4f;
    font-size: 12px;
    margin-top: -15px;
    margin-bottom: 20px;
    animation: fadeIn 0.3s ease;
  }

  @keyframes fadeIn {
    from {
      opacity: 0;
    }
    to {
      opacity: 1;
    }
  }
}
</style>
