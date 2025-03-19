<!-- 充值申请 -->
<template>
  <HeaderBar :currentName="currentName" :cuttentRight="cuttentRight"></HeaderBar>
  <!-- 二维码 -->
  <div class="erweima">
    <QRCode :address="address"></QRCode>
  </div>
  <!-- 申请信息 -->
  <div class="applyMes">
    <div class="address">
      <!-- 充值地址 -->
      <p class="top">{{ _t18('recharge_address', ['bitmake']) }}({{ route.query.type }})</p>
      <div class="bottom">
        <Copy :data="address" :fontSize="'16px'">
          <template #copyMsg>
            <span class="fw-num">{{ address }}</span>
          </template>
        </Copy>
      </div>
    </div>
  </div>
  <!-- 确认充值按钮 -->
  <div class="btn" @click="submit" style="display: none;">
    <p>{{ _t18('recharge_require', ['bitmake']) }}</p>
  </div>
  <!-- 充值说明列表 -->
  <div class="tip-list">
    <div class="tip" v-for="(item, index) in tipList" :key="index">
      {{ index + 1 }}.{{ item.content }}
    </div>
  </div>
</template>

<script setup>
import { uploadImg } from '@/api/common/index.js'
import { rechargeSubmit, getUserRechageNewApi } from '@/api/account.js'
import { _toView, _hideAddress, _t18, _getConfig } from '@/utils/public'
import { priceFormat } from '@/utils/decimal'
import QRCode from '@/components/common/QRCode/index.vue'
import Copy from '@/components/common/Copy/index.vue'
import { showToast } from 'vant'
import { debounce } from 'lodash'
import { useToast } from '@/hook/useToast'
import { useCopy } from '@/hook/useCopy'
import { useRouter, useRoute } from 'vue-router'
import { useMainStore } from '@/store'
import { onMounted, reactive } from 'vue'

const { _toast } = useToast()
const { _copy } = useCopy()
const route = useRoute()
const router = useRouter()
// 充值
const currentName = `${_t18('recharge', ['latcoin'])} ${route.query.type}`
const cuttentRight = { iconRight: [{ iconName: 'jilu', clickTo: '/recharge-order' }] }

// const address = ref('')
/**
 * 充值说明
 */
const tipList = reactive([
  { content: _t18('recharge_tip1') },
  { content: _t18('recharge_tip2') },
  { content: _t18('recharge_tip3') },
  { content: _t18('recharge_tip4') }
])
const tipList2 = reactive([{ content: _t18('recharge_tip5') }])
const num = ref('')
const fileList = ref([])
const afterRead = (file) => {
  file.status = 'uploading'
  // 此时可以自行将文件上传至服务器
  let formData = new FormData()
  formData.append('file', file.file)
  uploadImg(formData).then((res) => {
    res = res.data
    if (res.code == '200') {
      file.res = res.data.url
      file.status = 'success'
    } else {
      showToast(res.msg)
      fileList.value = []
    }
  })
}
const submit = debounce(() => {
  const params = {
    amount: 0,
    type: route.query.type,
    coin: route.query.coin,
    address: address.value
  }

  rechargeSubmit(params).then((res) => {
    if (res.code == '200') {
      _toast('recharge_success') // 充值成功
      setTimeout(() => {
        _toView('/recharge-order')
      }, 500)
    } else {
      showToast(res.msg)
    }
  })
}, 500)

const mainStore = useMainStore()
/**
 * 充值地址
 */
const address = computed(() => {

  let rechargeObj = mainStore.getRechargeList.find((elem) => elem.coinName == route.query.type)
  return rechargeObj.coinAddress
})

async function getRechageList(){
  const {data} = await getUserRechageNewApi(route.query.coin,route.query.type)
  address.value = data[route.query.type][route.query.type]
}

onMounted(()=>{
  getRechageList()
})
</script>

<style lang="scss" scoped>
* {
  font-size: 14px;
  color: #ffffff;
}

// 整体背景色
:deep(.page-container) {
  background: linear-gradient(145deg, #1a1a1a, #2d2d2d);
  min-height: 100vh;
}

.erweima {
  padding: 50px 0;
  // 添加缓动动画
  animation: fadeInDown 0.8s ease-out;
  
  :deep(.qr-wrapper) {
    background: rgba(255, 255, 255, 0.05);
    border-radius: 16px;
    padding: 20px;
    box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.36);
    backdrop-filter: blur(4px);
    margin: 0 20px;
    transition: transform 0.3s ease;
    
    &:hover {
      transform: translateY(-5px);
    }
  }
}

.applyMes {
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  padding: 30px 15px;
  animation: fadeInUp 0.8s ease-out;
  
  & > div {
    margin-bottom: 25px;
    background: rgba(255, 255, 255, 0.05);
    border-radius: 16px;
    padding: 20px;
    box-shadow: 0 8px 22px 0 rgba(0, 0, 0, 0.3);
    transition: all 0.3s ease;
    
    &:hover {
      transform: translateY(-3px);
      box-shadow: 0 12px 28px 0 rgba(0, 0, 0, 0.4);
    }

    p {
      color: rgba(255, 255, 255, 0.7);
      margin-bottom: 12px;
      font-weight: 500;
    }
  }

  .address {
    .bottom {
      word-break: break-all;
      background: rgba(0, 0, 0, 0.2);
      padding: 12px;
      border-radius: 8px;
    }
  }
}

.btn {
  padding: 0 15px 55px;
  animation: fadeInUp 1s ease-out;
  
  p {
    text-align: center;
    padding: 16px 0;
    color: #ffffff;
    font-size: 16px;
    background: linear-gradient(135deg, #4a4a4a 0%, #2d2d2d 100%);
    border-radius: 12px;
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
    transition: all 0.3s ease;
    
    &:active {
      transform: scale(0.98);
    }
  }
}

.tip-list {
  padding: 0 20px;
  animation: fadeInUp 1.2s ease-out;
  
  .tip {
    margin-bottom: 15px;
    padding: 15px;
    background: rgba(255, 255, 255, 0.05);
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    transition: all 0.3s ease;
    
    &:hover {
      transform: translateX(5px);
    }
  }
}

// 动画关键帧
@keyframes fadeInDown {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

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
</style>
