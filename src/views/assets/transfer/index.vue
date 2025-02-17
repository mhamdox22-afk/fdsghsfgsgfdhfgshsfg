<!-- 划转 -->
<template>
  <HeaderBar :currentName="_t18('transfer')"></HeaderBar>

  <div class="content_box">
    <p class="title fw-bold">{{ _t18('transfer') }}</p>
    <div class="cardOne">
      <div class="left">
        <!-- 从   到 -->
        <div class="left_top">
          <span>{{ _t18('transfer_from') }}</span>
          <van-dropdown-menu active-color="#17ac74">
            <van-dropdown-item
              v-model="params.transferOutAccount"
              :options="transferOutAccountList"
            />
          </van-dropdown-menu>
        </div>
        <div class="left_bottom">
          <span>{{ _t18('transfer_to', ['aams']) }}</span>
          <van-dropdown-menu active-color="#17ac74">
            <van-dropdown-item
              v-model="params.transferInAccount"
              :options="transferInAccountList"
            />
          </van-dropdown-menu>
        </div>
      </div>
      <div class="right" @click="jiaohuanbtn2">
        <svg-load name="huazhuanbtn" class="huazhuanbtn"></svg-load>
      </div>
    </div>
    <div class="cardTwo">
      <!-- 划转金额 -->
      <div class="top">{{ _t18('transfer_amount') }}</div>
      <div class="bottom">
        <input
          type="number"
          :placeholder="`${_t18('transfer_less', ['aams'])}1${params.coin?.toUpperCase()}`"
          v-model="params.amount"
        />
        <span @click="amountAll">
          {{ _t18('swap_all') }}
        </span>
        <i>|</i>
        <p>{{ params.coin?.toUpperCase() }}</p>
      </div>
    </div>
    <!-- 可用余额 -->
    <div class="keyong">
      {{ _t18('transfer_available') }}：<span class="fw-num"
        >{{ availableAmount }} {{ params.coin?.toUpperCase() }}</span
      >
    </div>
    <div class="btnBox" @click="submit">
      <!-- 确认划转 -->
      <ButtonBar :btnValue="_t18('transfer_require')" :btnColor="'#138A5D'" />
    </div>
  </div>
</template>

<script setup>
import { DIFF_ISFREEZE } from '@/config/index'
import { useFreeze } from '@/hook/useFreeze'

const { _isFreeze } = useFreeze()
import ButtonBar from '@/components/common/ButtonBar/index.vue'
import { getTransferList, getUserBalance } from '@/api/account'
import { onMounted, ref, computed } from 'vue'
import { showToast } from 'vant'
import { _t18 } from '@/utils/public'
import { useMainStore } from '@/store'
import { useUserStore } from '@/store/user'

const mainStore = useMainStore()
const userStore = useUserStore()
import { useToast } from '@/hook/useToast'

const { _toast } = useToast()

//数据
const params = ref({
  coin: 'usdt',
  transferOutAccount: '',
  transferInAccount: '',
  amount: ''
})

//获取余额
const availableList = ref([])
const getBalance = async () => {
  let res = await getUserBalance()
  if (res.code == '200') {
    availableList.value = res.data
  }
}
// 计算余额   -->1 平台资产 2理财资产 3合约账户
const availableAmount = computed(() => {
  let amountTemp = ''
  availableList.value.forEach((item, index) => {
    if (item?.type == params.value.transferOutAccount) {
      amountTemp = item.availableAmount
    }
  })

  return amountTemp
})

// 选择器数据
const tempList = computed(() => {
  const list = mainStore.getAssetsTabList.filter((item) => {
    console.log(item)
    // 对应参数key值 -->1 平台资产 2理财资产 3合约账户
    const key = { 平台资产: 1, 理财资产: 2, 合约资产: 3 }
    if (!item.isOpen) return false
    item.name == '平台资产' && (item.keyStr = _t18(item.keyStr, ['latcoin']))
    item.name == '理财资产' && (item.keyStr = _t18(item.keyStr, ['paxpay', 'latcoin']))
    item.name == '合约资产' && (item.keyStr = _t18(item.keyStr, ['paxpay', 'latcoin']))

    item['text'] = item.keyStr
    item['value'] = key[item.name]
    return true
  })
  return list.sort((a, b) => a.sort - b.sort)
  // return list || []
})
//默认值
params.value.transferOutAccount = tempList.value[0]?.value || ''
params.value.transferInAccount = tempList.value[1]?.value || ''
const transferOutAccountList = computed(() =>
  tempList.value.filter((item) => item.value != params.value.transferInAccount)
)
const transferInAccountList = computed(() =>
  tempList.value.filter((item) => item.value != params.value.transferOutAccount)
)

//提交
const submit = () => {
  if (DIFF_ISFREEZE.includes(__config._APP_ENV)) {
    if (_isFreeze(DIFF_ISFREEZE)) {
      submitForm()
    }
  } else {
    submitForm()
  }
}
const submitForm = () => {
  if (params.value.amount == '' || params.value.amount <= 0) {
    // showToast('请输入划转金额')
    return _toast('transfer_please_amount')
  }

  getTransferList(params.value).then((res) => {
    if (res.code == '200') {
      // showToast('操作成功，请稍后...')
      _toast('transfer_success')
      getBalance()
    } else {
      showToast(res.msg || '')
    }
  })
}

const amountAll = () => {
  params.value.amount = availableAmount.value
}

//交换
const jiaohuanbtn2 = () => {
  [params.value.transferOutAccount, params.value.transferInAccount] = [
    params.value.transferInAccount,
    params.value.transferOutAccount
  ]
}

onMounted(() => {
  getBalance()
})
</script>

<style lang="scss" scoped>
* {
  font-size: 14px;
  color: #ffffff;
}

.content_box {
  padding: 0 15px;
  min-height: 100vh;
  background: linear-gradient(145deg, #1a1a1a, #2d2d2d);

  .title {
    font-size: 30px;
    margin: 30px 0;
    background: linear-gradient(45deg, #17ac74, #138A5D);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    animation: fadeIn 0.8s ease-in;
  }

  .cardOne,
  .cardTwo {
    padding: 20px;
    background: rgba(255, 255, 255, 0.05);
    margin-top: 20px;
    border-radius: 15px;
    backdrop-filter: blur(10px);
    box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
    border: 1px solid rgba(255, 255, 255, 0.1);
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 10px 40px 0 rgba(31, 38, 135, 0.47);
    }
  }

  .cardOne {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .left {
      margin-right: 20px;
      flex: 1;

      & > div {
        display: flex;
        color: #ffffff;
        padding: 10px 0;

        span {
          display: inline-block;
          width: 40px;
          opacity: 0.8;
        }
      }

      .left_top {
        border-bottom: 1px dashed rgba(255, 255, 255, 0.1);
        display: flex;
        align-items: center;
      }

      .left_bottom {
        display: flex;
        align-items: center;
      }
    }

    .huazhuanbtn {
      font-size: 30px;
      cursor: pointer;
      transition: transform 0.3s ease;
      
      &:hover {
        transform: rotate(180deg);
      }
    }
  }

  .cardTwo {
    .top {
      margin-bottom: 15px;
      color: rgba(255, 255, 255, 0.8);
    }

    .bottom {
      display: flex;
      justify-content: space-between;
      align-items: center;
      border-bottom: 1px solid rgba(255, 255, 255, 0.1);
      padding-bottom: 10px;

      i {
        padding: 0 10px;
        color: rgba(255, 255, 255, 0.3);
      }

      span {
        color: #17ac74;
        cursor: pointer;
        transition: color 0.3s ease;
        
        &:hover {
          color: #138A5D;
        }
      }

      input {
        flex: 1;
        margin-right: 20px;
        padding: 8px 0;
        background-color: transparent;
        color: #ffffff;
        border: none;

        &::placeholder {
          color: rgba(255, 255, 255, 0.3);
        }
      }
    }
  }

  .keyong {
    margin-top: 15px;
    color: rgba(255, 255, 255, 0.6);
    font-size: 12px;
    animation: slideIn 0.5s ease-out;
  }

  .btnBox {
    margin-top: 50px;
    animation: fadeInUp 0.8s ease-out;
  }
}

.van-dropdown-menu {
  flex: 1;
  display: flex;

  :deep(.van-popup--top) {
    max-width: var(--ex-max-width);
    left: 50%;
    translate: -50%;
  }
}

:deep(.van-cell) {
  background: #2d2d2d !important;
  color: #ffffff;
}

:deep(.van-dropdown-menu__bar) {
  background-color: transparent;
  box-shadow: none;
}

:deep(.van-dropdown-menu__title) {
  color: #ffffff;
  font-size: 14px;
  padding-left: 20px;
}

:deep(.van-dropdown-menu__title:after) {
  border-color: transparent transparent rgba(255, 255, 255, 0.3) rgba(255, 255, 255, 0.3);
}

// Animations
@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes slideIn {
  from {
    transform: translateX(-20px);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

@keyframes fadeInUp {
  from {
    transform: translateY(20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}
</style>
