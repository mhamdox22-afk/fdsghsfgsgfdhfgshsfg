<!-- 初级认证 -->
<script setup>
import HeaderBar from '@/components/HeaderBar/index.vue'
import Success from './components/success.vue'
import { uploadKYC } from '@/api/user'
import { reactive } from 'vue'
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
const formData = reactive({
  userName: '',
  number: ''
})
const cuttentRight = { iconRight: [{ iconName: 'kefu', clickTo: 'event_serviceChange' }] }
// 是否初级认证
const primaryAuth = ref(userInfo.value.detail?.auditStatusPrimary)
// 审核状态 0未申请 1通过 3审核中 2拒绝
// 提交认证信息
const submit = () => {
  if (formData.userName == '') {
    // showToast('请填写姓名')
    _toast('advanced_please_name')
    return
  }
  if (formData.number == '') {
    // showToast('请填写身份证号')
    _toast('advanced_please_cardName')
    return
  }
  // realName 姓名 ,idCard 证件号码 ,flag 1 初级 2高级
  let params = `realName=${formData.userName}&idCard=${formData.number}&flag=1`
  uploadKYC(params).then((res) => {
    if (res.code == '200') {
      // showToast('提交成功，请等待...')
      _toast('advanced_please_wait')
      primaryAuth.value = '3'
      userStore.getUserInfo()
    } else {
      showToast(res.msg)
      primaryAuth.value = '2'
    }
  })
}
const reSubmit = () => {
  primaryAuth.value = '0'
}
</script>
<template>
  <!-- 导航条 -->
  <HeaderBar :currentName="_t18('sidebar_primary')" :cuttentRight="cuttentRight"></HeaderBar>
  <div class="content" v-if="primaryAuth == '0' || primaryAuth == null">
    <div class="tip advanced_txt">{{ _t18('advanced_txt', ['aams']) }}</div>
    <div v-if="['coinsexpto'].includes(_getConfig('_APP_ENV'))" class="tip_info">
      {{ _t18('basic_certification_info') }}
    </div>
    <div class="form">
      <div class="formInput">
        <div class="label">{{ _t18('advanced_name') }}：</div>
        <input :placeholder="_t18('login_please')" v-model="formData.userName" class="form-input" />
      </div>
      <div class="formInput">
        <div class="label">{{ _t18('advanced_license_number') }}：</div>
        <input :placeholder="_t18('login_please')" v-model="formData.number" class="form-input" />
      </div>
    </div>
    <div class="btnBox" @click="submit">
      <ButtonBar :btnValue="_t18('advanced_submit')" />
    </div>
  </div>
  <div class="success" v-if="primaryAuth == '3'">
    <Success :text="_t18('under_review')" :imgUrl="'defi/zhong.png'" :color="'#333'"></Success>
  </div>
  <div class="success" v-if="primaryAuth == '2'">
    <Success :text="_t18('Audit_failure')" :imgUrl="'defi/failed.png'" :color="'#333'">
      <template #btn>
        <div class="btnBox" @click="reSubmit">
          <ButtonBar :btnValue="_t18('Resubmit')" />
        </div>
      </template>
    </Success>
  </div>
  <div v-if="primaryAuth == '1'">
    <div class="fromContent">
      <div>{{ _t18('advanced_name') }}：{{ userInfo.detail?.realName }}</div>
      <div>{{ _t18('advanced_license_number') }}：{{ userInfo.detail?.idCard }}</div>
    </div>
    <Success
      :text="_t18('advanced_success')"
      :imgUrl="'defi/success.png'"
      :color="'#333'"
    ></Success>
  </div>
</template>
<style lang="scss" scoped>
.content {
  padding: 30px 15px 0 15px;
  color: #fff;
  animation: fadeIn 0.5s ease-in-out;

  .tip {
    font-size: 12px;
    color: rgba(255, 255, 255, 0.6);
    transform: translateY(0);
    transition: all 0.3s ease;
    
    &:hover {
      transform: translateY(-2px);
    }
  }

  .tip_info {
    font-size: 12px;
    text-align: left;
    padding: 10px;
    background: rgba(255, 255, 255, 0.05);
    border-radius: 8px;
    backdrop-filter: blur(10px);
    margin: 15px 0;
  }

  .advanced_txt {
    margin-bottom: 20px;
    text-align: left;
  }

  .form {
    padding-top: 5px;
    .formInput {
      padding: 0 15px;
      display: flex;
      justify-content: flex-start;
      background: rgba(255, 255, 255, 0.05);
      border-radius: 12px;
      border: 1px solid rgba(255, 255, 255, 0.1);
      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
      align-items: center;
      margin: 20px 0;
      transition: all 0.3s ease;
      
      &:focus-within {
        transform: translateY(-2px);
        box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
        border-color: rgba(255, 255, 255, 0.2);
      }

      .label {
        width: 70px;
        font-size: 14px;
        color: rgba(255, 255, 255, 0.8);
        text-align: left;
      }

      input {
        width: 100%;
        height: 46px;
        font-size: 14px;
        color: #fff;
        background-color: transparent;
        
        &::placeholder {
          color: rgba(255, 255, 255, 0.3);
          font-size: 14px;
        }
      }
    }
  }

  .btnBox {
    width: 100%;
    margin-top: 50px;
    transform: translateY(0);
    transition: all 0.3s ease;
    
    &:active {
      transform: scale(0.98);
    }
  }
}

.fromContent {
  color: #fff;
  padding: 30px 15px;
  animation: slideUp 0.5s ease-out;
  
  div {
    margin-bottom: 20px;
    padding: 16px 13px;
    background: rgba(255, 255, 255, 0.05);
    border-radius: 12px;
    color: #fff;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
    backdrop-filter: blur(10px);
    transition: all 0.3s ease;
    
    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
    }
  }
}

.success {
  color: #fff;
  margin-top: 100px;
  animation: bounceIn 0.6s ease-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes slideUp {
  from {
    transform: translateY(20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

@keyframes bounceIn {
  0% {
    transform: scale(0.3);
    opacity: 0;
  }
  50% {
    transform: scale(1.05);
    opacity: 0.8;
  }
  70% {
    transform: scale(0.9);
    opacity: 0.9;
  }
  100% {
    transform: scale(1);
    opacity: 1;
  }
}
</style>
