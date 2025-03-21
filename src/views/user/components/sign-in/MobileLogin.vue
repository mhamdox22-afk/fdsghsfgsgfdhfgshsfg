<template>
  <div>
    <!-- 手机号 验证码 发送 -->
    <div class="formData">
      <p>{{ _t18('login_mobileCode') }}</p>
      <div class="input-container">
        <div class="area-code-selector" @click="showAreaCode">
          <p>+{{ formData3.areaCode }}</p>
          <svg-load name="jiantou10x5-x" class="jiantou" ></svg-load>
        </div>
        <input type="text" :placeholder="_t18('login_mobileCode')" v-model="formData3.mobile" />
      </div>
    </div>
    <div class="formData">
      <p>{{ _t18('login_code') }}</p>
      <div class="input-wrapper">
        <input type="text" :placeholder="_t18('login_please')" v-model="formData3.code" />
        <div class="send-button-wrapper">
          <p v-if="!flag" class="send-button" @click="send()">{{ _t18('login_send') }}</p>
          <p v-else class="countdown"><van-count-down :time="time" format="ss" @finish="finish" /></p>
        </div>
      </div>
    </div>
    <p class="forgotPwd" @click="$router.push('/forgot-password')">
      {{ _t18('forget_pwd', ['bitmake']) }}
    </p>
    <Footer :type="0" :formDataToLogin="formData3" @refersh="refreshCode"></Footer>
    <AreaCode :show="show" @handelClick="close" @handelSelect="select"></AreaCode>
  </div>
</template>

<script setup>
import Footer from './../signFooter.vue'
import { mobileCode } from '@/api/user'
import AreaCode from './../areaCode.vue'
import { useToast } from '@/hook/useToast'
const { _toast } = useToast()
import { _t18 } from '@/utils/public'
/**
 * 表单数据
 */
const formData3 = ref({
  type: 3, //自定义注册类型：1普通2邮箱3手机
  mobile: '',
  password: '', //密码
  code: '', //验证码
  areaCode: '93' //区号
})
if (['bitbyex'].includes(__config._APP_ENV)) {
  formData3.value.areaCode = '1'
}

// 发送||倒计时
const show = ref(false)
// 选择区号面板
const showAreaCode = () => {
  show.value = true
}
// 关闭区号面板
const close = () => {
  show.value = false
}
// 选择区号
const select = (v) => {
  formData3.value.areaCode = v
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
  mobileCode('LOGIN', formData3.value.areaCode + formData3.value.mobile).then((res) => {
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
.input-container {
  display: flex;
  align-items: center;
  gap: 8px;

  input {
    flex: 1;
    height: 40px;
    border: 1px solid rgba(255, 255, 255, 0.1);
    border-radius: 4px;
    background: transparent;
    color: #fff;
    padding: 0 12px;
  }
}

.area-code-selector {
  display: flex;
  align-items: center;
  gap: 4px;
  height: 40px;
  padding: 0 12px;
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 4px;
  cursor: pointer;
  
  p {
    color: #fff;
    margin: 0;
  }

  .jiantou {
    width: 10px;
    height: 5px;
    fill: #fff;
  }
}

.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;

  input {
    width: 100%;
    padding-right: 80px;
  }

  .send-button-wrapper {
    position: absolute;
    right: 10px;
    
    .send-button {
      background: linear-gradient(to right, #4481eb, #04befe);
      -webkit-background-clip: text;
      background-clip: text;
      color: transparent;
      cursor: pointer;
      font-size: 14px;
    }

    .countdown {
      color: #666;
      font-size: 14px;
    }
  }
}
</style>
