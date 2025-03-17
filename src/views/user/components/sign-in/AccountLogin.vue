<template>
  <div>
    <!-- 账号 密码 验证码 -->
    <div class="formData">
      <p>{{ _t18('account', ['bitmake', 'aams']) }}</p>
      <div>
        <input
          type="text"
          :placeholder="_t18('login_name', ['bitmake'])"
          v-model="formData1.username"
        />
      </div>
    </div>
    <div class="formData">
      <p>{{ _t18('login_pwd') }}</p>
      <div>
        <input
          :type="showk ? 'text' : 'password'"
          :placeholder="_t18('login_please')"
          v-model="formData1.password"
        /><svg-load :name="showk ? 'yanjin-k' : 'yanjin-g'" @click="showk = !showk"></svg-load>
      </div>
    </div>
    <div class="formData" v-if="mainStore.getISCode">
      <p>{{ _t18('login_code') }}</p>
      <div>
        <input type="text" :placeholder="_t18('login_please')" v-model="formData1.code" /><span
          @click="refreshCode"
        >
          <img :src="codeUrl" />
        </span>
      </div>
    </div>

    
    <!-- Keeping the Footer component for functionality but it may need to be hidden or styled differently -->
    <Footer ref="footerRef" :type="0" :formDataToLogin="formData1" @refersh="refreshCode" class="hidden-footer"></Footer>
  </div>
</template>

<script setup>
import Footer from './../signFooter.vue'
import { _t18 } from '@/utils/public'
import { useMainStore } from '@/store/index.js'
const mainStore = useMainStore()
const formData1 = ref({
  type: 1, //自定义注册类型：1普通2邮箱3手机
  username: '', //用户名/邮箱/手机号
  password: '', //密码
  code: '' //验证码
})
// ***************刷新验证码**************
// 时间戳
const timestamp = ref(+new Date())
// 图形验证码地址
const codeUrl = computed(
  () => `${mainStore.verificationCodeUrl}?codeType=LOGIN&timestamp=${timestamp.value}`
)

// 刷新验证码
const refreshCode = () => {
  timestamp.value = +new Date()
}
// 眼睛
const showk = ref(false)

// 登录方法，可能需要调用Footer组件中的方法
const footerRef = ref(null)
const login = () => {
  // 触发 Footer 组件中的登录方法
  document.querySelector('.hidden-footer button').click()
}

import './../style.scss'
</script>

<style lang="scss" scoped>
.login-options {
  display: flex;
  justify-content: space-between;
  margin: 15px 0;
}

.register-link {
  color: #999;
  font-size: 14px;
  cursor: pointer;
  
  span {
    color: #fff;
  }
}

.forgot-pwd {
  color: #999;
  font-size: 14px;
  cursor: pointer;
}

.login-button {
  height: 48px;
  border-radius: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(to right, #6a36c9, #38b8d2);
  color: white;
  font-size: 16px;
  cursor: pointer;
  margin-top: 10px;
}

.hidden-footer {
  display: none; /* Hide the original footer if it's being replaced */
}

/* Existing form styling may need some adjustments */
.formData {
  margin-bottom: 16px;
  
  p {
    color: #999;
    margin-bottom: 8px;
  }
  
  div {
    position: relative;
    
    input {
      width: 100%;
      height: 48px;
      background: #18181b;
      border: none;
      border-radius: 8px;
      padding: 0 15px;
      color: #fff;
      
      &::placeholder {
        color: #666;
      }
    }
    
    svg-load {
      position: absolute;
      right: 15px;
      top: 50%;
      transform: translateY(-50%);
      cursor: pointer;
    }
  }
}
</style>
