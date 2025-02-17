<template>
  <div class="columnFlex">
    <!-- 购买基金 -->
    <HeaderBar :currentName="_t18('buy_funds')" :backwardNum="backwardNum" />
    <div v-if="!isSuccess">
      <DetailHeader :headerObj="headerObj"></DetailHeader>
      <div class="introduction">
        <!-- 买入额度 -->
        <div class="title fw-bold">{{ _t18('buy_limit') }}</div>
        <!-- 请输入 -->
        <input v-model="limit" type="text" :placeholder="_t18('exchange.input')" class="quota" />
      </div>
      <ProductDetail :proDetail="proDetail"></ProductDetail>
      <div class="buyNow">
        <!-- 支付 -->
        <div class="buyBtn" @click="payNow">{{ _t18('to pay') }}</div>
        <div class="protocol">
          <svg-load v-if="!isCheck" name="gou" class="protocolImg" @click="toSwitch"></svg-load>
          <svg-load v-if="isCheck" name="gouH" class="protocolImg" @click="toSwitch"></svg-load>
          <!-- 我已阅读并同意 -->
          <div>{{ _t18('read_and_confirm') }}</div>
          <!-- 理财协议 -->
          <div class="hightName" @click="$router.push('/financialAgreement')">
            {{ _t18('financial agreement') }}
          </div>
        </div>
      </div>
    </div>
    <div v-if="isSuccess" class="success">
      <svg-load name="icon8" class="successImg"></svg-load>
      <!-- 支付成功 -->
      <div>{{ _t18('payment_successful') }}</div>
    </div>
  </div>
</template>
<script setup>
import { DIFF_ISFREEZE } from '@/config/index'
import { useFreeze } from '@/hook/useFreeze'
const { _isFreeze } = useFreeze()
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { showToast } from 'vant'
import { useToast } from '@/hook/useToast'
const { _toast, _showName } = useToast()
import HeaderBar from '@/components/HeaderBar/index.vue'
import DetailHeader from './components/detailHeader.vue' //头
import ProductDetail from './components/productDetail.vue' // 详情
import { financialDetail, financialSubmit } from '@/api/financial/index'
import { _t18 } from '@/utils/public'
const Route = useRoute()
// 起投金额 限投金额 锁仓天数
const headerObj = ref({
  leftName: _t18(`starting-amount`),
  centerName: _t18(`Investment_Limit`),
  rightName: _t18(`Lock-up_days`)
})
const limit = ref('') // 额度
const dayNum = ref('') // 额度
const coninName = ref('')
const proDetail = ref({}) // 产品详情
const isCheck = ref(false) // 选择服务协议
const isSuccess = ref(false) // 判断成功
const backwardNum = ref(-1) //
const minNum = ref(0) // 最小输入
const maxNum = ref(0) // 最大输入
const payNowForm = () => {
  // 请先同意理财协议
  if (!isCheck.value) return _toast('please_agree_financial')
  if (Number(limit.value) < minNum.value)
    // 最少可购买
    return showToast(_t18(`Can_buy_down_to`) + ` ${minNum.value} ${coninName.value}`)
  if (Number(limit.value) > maxNum.value)
    // 最多可购买
    return showToast(_showName(`Can_buy_up_to`) + ` ${maxNum.value} ${coninName.value}`)
  let regNum = /^\d+(\.\d{1,4})?$/
  // 最多输入小数点后4位
  if (!regNum.test(limit.value)) return _toast('entet4_point')
  submit()
}
// 支付
const payNow = () => {
  if (DIFF_ISFREEZE.includes(__config._APP_ENV)) {
    if (_isFreeze(DIFF_ISFREEZE)) {
      payNowForm()
    }
  } else {
    payNowForm()
  }
}
/**立即购买 */
const submit = async () => {
  const res = await financialSubmit(Route.params.id, limit.value, dayNum.value)
  if (res.code === 200) {
    isSuccess.value = true
    backwardNum.value = -2
  } else {
    showToast(res.msg)
  }
}
// 勾选协议
const toSwitch = () => {
  isCheck.value = !isCheck.value
}
/** 获取详情 */
const getDetail = async () => {
  try {
    const res = await financialDetail(Route.params.id)
    if (res.code === 200) {
      const { title, icon, avgRate, days, limitMin, limitMax, coin } = res.data
      minNum.value = limitMin
      maxNum.value = limitMax
      coninName.value = coin.toUpperCase() // 弹窗使用
      dayNum.value = days
      headerObj.value = {
        title,
        icon,
        leftNum: limitMin + ' ' + coin.toUpperCase(),
        centerNum: limitMax + ' ' + coin.toUpperCase(),
        rightNum: days,
        ...headerObj.value
      }
      /**产品详情 */
      const { process, totalInvestAmount, remainAmount, timeLimit } = res.data
      proDetail.value = {
        title,
        process,
        totalInvestAmount,
        remainAmount,
        timeLimit,
        avgRate,
        coin: coin.toUpperCase()
      }
    }
  } catch (error) {}
}
onMounted(() => {
  getDetail()
})
</script>
<style lang="scss" scoped>
.columnFlex {
  background: #1a1a1a;
  min-height: 100vh;
  color: #fff;
}

.introduction {
  padding: 25px 20px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  color: #999;
  font-size: 12px;
  animation: fadeIn 0.5s ease-in-out;

  .title {
    font-size: 18px;
    color: #fff;
    margin-bottom: 15px;
    font-weight: 600;
    transform: translateY(0);
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
    }
  }

  .quota {
    width: 100%;
    height: 55px;
    background: rgba(255, 255, 255, 0.05);
    border-radius: 8px;
    border: 1px solid rgba(255, 255, 255, 0.1);
    padding: 0 15px;
    font-size: 16px;
    color: #fff;
    transition: all 0.3s ease;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);

    &:focus {
      border-color: #3d7eff;
      box-shadow: 0 6px 12px rgba(61, 126, 255, 0.2);
      transform: translateY(-2px);
    }
  }

  input::-webkit-input-placeholder {
    color: rgba(255, 255, 255, 0.3);
  }
}

.buyNow {
  padding: 40px 20px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  animation: slideUp 0.5s ease-out;

  .buyBtn {
    height: 55px;
    background: linear-gradient(45deg, #3d7eff, #6d5dfc);
    border-radius: 8px;
    font-size: 17px;
    font-weight: 600;
    color: #fff;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.3s ease;
    box-shadow: 0 4px 15px rgba(61, 126, 255, 0.3);

    &:active {
      transform: scale(0.98);
      box-shadow: 0 2px 8px rgba(61, 126, 255, 0.2);
    }
  }

  .protocol {
    display: flex;
    align-items: center;
    justify-content: center;
    margin: auto;
    padding: 35px 0 0;
    font-size: 14px;
    text-align: center;
    color: rgba(255, 255, 255, 0.7);

    .protocolImg {
      width: 20px;
      height: 20px;
      margin-right: 8px;
      transition: transform 0.2s ease;

      &:active {
        transform: scale(1.1);
      }
    }

    .hightName {
      color: #3d7eff;
      text-decoration: none;
      position: relative;
      
      &::after {
        content: '';
        position: absolute;
        bottom: -2px;
        left: 0;
        width: 100%;
        height: 1px;
        background: #3d7eff;
        transform: scaleX(0);
        transition: transform 0.3s ease;
      }

      &:hover::after {
        transform: scaleX(1);
      }
    }
  }
}

.success {
  flex: 1;
  margin: 150px auto;
  font-size: 16px;
  color: rgba(255, 255, 255, 0.8);
  text-align: center;
  animation: bounceIn 0.6s cubic-bezier(0.68, -0.55, 0.265, 1.55);

  .successImg {
    width: 120px;
    height: 118px;
    margin-bottom: 35px;
    filter: drop-shadow(0 4px 10px rgba(61, 126, 255, 0.3));
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes bounceIn {
  0% {
    opacity: 0;
    transform: scale(0.3);
  }
  50% {
    opacity: 0.9;
    transform: scale(1.1);
  }
  80% {
    opacity: 1;
    transform: scale(0.89);
  }
  100% {
    opacity: 1;
    transform: scale(1);
  }
}
</style>
