<script setup>
import { DIFF_ISFREEZE } from '@/config/index'
import { useFreeze } from '@/hook/useFreeze'
const { _isFreeze } = useFreeze()
import { _t18 } from '@/utils/public'
import { pledgeSubmit } from '@/api/pledge/index'
import { priceFormat } from '@/utils/decimal.js'
import { useUserStore } from '@/store/user/index'
import { storeToRefs } from 'pinia'
import { useRoute, useRouter } from 'vue-router'
import { showToast } from 'vant'
import { useToast } from '@/hook/useToast'
const { _toast } = useToast()

const route = useRoute()
const router = useRouter()
const data = reactive(JSON.parse(decodeURI(route.query.data)))
/**
 * 账户信息
 */
const userStore = useUserStore()
const { asset } = storeToRefs(userStore)
const amount = computed(() => {
  // let data = 999
  let data = asset.value.filter((item, index) => {
    return item.type == '1' && item.symbol == 'usdt'
  })[0].availableAmount
  return data
})
const maxAmount = ref('')
let maxNum = () => {
  maxAmount.value = amount.value
}
const submitForm = () => {
  let params = { planId: data.id, amount: maxAmount.value }
  if (maxAmount.value == '' || maxAmount.value > data.limitMax || maxAmount.value < data.limitMin) {
    // showToast('购买金额有误')
    _toast('pledge_buy_error')
    return
  }
  pledgeSubmit(params).then((res) => {
    if (res.code == '200') {
      // showToast(res.msg)购买成功
      _toast('Purchase_successful')
      setTimeout(() => {
        router.push('/pledge/pledgeOrder')
      }, 500)
    } else {
      showToast(res.msg)
    }
  })
}
const submit = () => {
  if (DIFF_ISFREEZE.includes(__config._APP_ENV)) {
    if (_isFreeze(DIFF_ISFREEZE)) {
      submitForm()
    }
  } else {
    submitForm()
  }
}
</script>
<template>
  <HeaderBar :currentName="_t18('host.detail')"></HeaderBar>
  <!-- 申购金额 -->
  <div class="content1">
    <div class="jine">
      <!-- 申购金额存入资金 -->
      <div>
        <div>{{ _t18('pledge_Subscription_amount') }} <span>(USDT)</span></div>
        <p @click="$router.push('/recharge')">{{ _t18('pledge_deposit_funds') }}</p>
      </div>
      <div class="ipt">
        <input
          v-model="maxAmount"
          type="number"
          :placeholder="`${_t18('pledge_purchasing_price', ['aams'])}`"
          class="ff-num"
        />
        <p @click="maxNum()">{{ _t18('pledge_maximum') }}</p>
      </div>
    </div>
    <div class="shouyi">
      <div>
        <p>{{ _t18('pledge_quota') }}</p>
        <p class="ff-num">{{ data.limitMin }}~{{ data.limitMax }}</p>
      </div>
      <!-- 日收益率 -->
      <div>
        <p>{{ _t18('host_dailyrateof_return') }}</p>
        <p class="ff-num">{{ data.minOdds }}%~{{ data.maxOdds }}%</p>
      </div>
      <!-- 周期(天) -->
      <div>
        <p>{{ _t18('pledge_cycle') }}({{ _t18('pledge_day') }})</p>
        <p class="ff-num">{{ data.days }}</p>
      </div>
    </div>
  </div>
  <!-- 信息 -->
  <div class="content2">
    <div>
      <!-- 可用余额 -->
      <div>
        <div class="left">{{ _t18('pledge_Available_Balance') }}</div>
        <div class="right ff-num">{{ priceFormat(amount) }} USDT</div>
      </div>
      <!-- 可购次数 -->
      <div>
        <div class="left">{{ _t18('pledge_Available_times') }}</div>
        <div class="right ff-num" v-if="['dev'].includes(_getConfig('_APP_ENV'))">{{ `${data.buyPurchase}/${data.timeLimit}` }}</div>
        <div class="right ff-num" v-else>{{ `${data.timeLimit}` }}</div>
      </div>
      <!-- <div>
        <div class="left">总年化率</div>
        <div class="right ff-num">0.00%-0.00%</div>
      </div> -->
    </div>
    <!-- <div class="btn" @click="$router.push('/pledge/pledgeOrder')">确定</div> -->
    <div class="btn" @click="submit">{{ _t18('btnConfirm', ['bitmake']) }}</div>
  </div>
</template>
<style lang="scss" scoped>
* {
  font-size: 14px;
  color: #ffffff;
}

.content1 {
  background: linear-gradient(145deg, #1a1a1a, #2d2d2d);
  border-radius: 16px;
  margin: 15px;
  padding: 25px 20px 30px;
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.2);
  backdrop-filter: blur(4px);
  border: 1px solid rgba(255, 255, 255, 0.08);
  transform: translateY(0);
  transition: all 0.3s ease;

  &:active {
    transform: translateY(2px);
  }

  .jine {
    & > div {
      display: flex;
      justify-content: space-between;
      align-items: center;
      
      span {
        color: #8b8b8b;
      }
      
      p {
        color: #00c4ff;
        font-weight: 500;
        transition: color 0.3s ease;
        
        &:active {
          color: #0099ff;
        }
      }
    }

    .ipt {
      background: rgba(255, 255, 255, 0.05);
      border: 1px solid rgba(255, 255, 255, 0.1);
      border-radius: 8px;
      padding: 15px;
      margin: 15px 0;
      display: flex;
      align-items: center;
      transition: all 0.3s ease;

      &:focus-within {
        border-color: #00c4ff;
        box-shadow: 0 0 15px rgba(0, 196, 255, 0.15);
      }

      input {
        background: transparent;
        color: #ffffff;
        border: none;
        width: 100%;
        
        &::placeholder {
          color: #666666;
        }
      }

      p {
        color: #00c4ff;
        margin-left: 10px;
        padding: 5px 10px;
        border-radius: 4px;
        background: rgba(0, 196, 255, 0.1);
        transition: all 0.3s ease;

        &:active {
          background: rgba(0, 196, 255, 0.2);
        }
      }
    }
  }

  .shouyi {
    padding: 25px 0 0;
    display: flex;
    justify-content: space-between;
    
    & > div {
      flex: 1;
      display: flex;
      flex-direction: column;
      align-items: center;
      
      & > p:first-child {
        margin-bottom: 12px;
        color: #8b8b8b;
      }
      
      & > p:last-child {
        font-weight: 600;
        color: #00c4ff;
        font-size: 16px;
      }
    }
  }
}

.content2 {
  background: linear-gradient(145deg, #1a1a1a, #2d2d2d);
  border-radius: 16px;
  margin: 15px;
  padding: 25px 20px;
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.2);
  
  & > div {
    & > div {
      display: flex;
      justify-content: space-between;
      margin-bottom: 25px;
      padding: 10px;
      border-radius: 8px;
      background: rgba(255, 255, 255, 0.02);
      transition: all 0.3s ease;

      &:hover {
        background: rgba(255, 255, 255, 0.05);
      }
      
      .left {
        color: #8b8b8b;
      }
      
      .right {
        font-weight: 500;
        color: #ffffff;
      }
    }
  }

  .btn {
    background: linear-gradient(45deg, #00c4ff, #0099ff);
    color: #ffffff;
    text-align: center;
    padding: 16px 0;
    font-size: 16px;
    border-radius: 8px;
    font-weight: 600;
    box-shadow: 0 4px 15px rgba(0, 196, 255, 0.2);
    transition: all 0.3s ease;

    &:active {
      transform: translateY(2px);
      box-shadow: 0 2px 8px rgba(0, 196, 255, 0.2);
      background: linear-gradient(45deg, #0099ff, #00c4ff);
    }
  }
}

// 添加页面进入动画
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

.content1, .content2 {
  animation: fadeInUp 0.5s ease-out;
}
</style>
