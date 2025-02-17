<template>
  <!-- 详情 -->
  <HeaderBar :currentName="_t18(`host.detail`)" />
  <DetailHeader :headerObj="headerObj"></DetailHeader>
  <div class="introduction animate-fade-in">
    <!-- 基金介绍 -->
    <div class="title fw-bold">{{ _t18(`Fund_introduction`) }}</div>
    <!-- 暂无介绍 -->
    <div class="content">{{ fundIntroduction ? fundIntroduction : _t18(`No_introductionyet`) }}</div>
  </div>
  <ProductDetail class="animate-slide-up" :proDetail="proDetail"></ProductDetail>
  <ProductRules class="animate-slide-up" :proRules="proRules"></ProductRules>
  <div class="buyNow animate-fade-in">
    <!-- 立即购买 -->
    <div class="buyBtn" @click="buyNow">{{ _t18(`buy_it_now`) }}</div>
  </div>
</template>
<script setup>
import { DIFF_ISFREEZE_OTHER } from '@/config/index'
import { useFreeze } from '@/hook/useFreeze'
const { _isFreeze } = useFreeze()
import { useRouter, useRoute } from 'vue-router'
import { computed, onMounted, ref } from 'vue'
import HeaderBar from '@/components/HeaderBar/index.vue'
import DetailHeader from './components/detailHeader.vue' //头
import ProductDetail from './components/productDetail.vue' // 详情
import ProductRules from './components/productRules.vue' // 规则
import { financialDetail } from '@/api/financial/index'
import { _t18 } from '@/utils/public'
const $router = useRouter()
const Route = useRoute()
// 平均收益 项目周期（天） 起投金额
const headerObj = ref({
  leftName: _t18('average_income'),
  leftColor: '#DF163D',
  centerName: `${_t18('project_cycle')}(${_t18('ldgpt_host_day')})`,
  rightName: _t18(`starting-amount`)
})
const fundIntroduction = ref('') // 基金介绍
const proDetail = ref({}) // 产品详情
const proRules = ref('') // 产品规则
const buyNow = () => {
  if (DIFF_ISFREEZE_OTHER.includes(__config._APP_ENV)) {
    if (_isFreeze(DIFF_ISFREEZE_OTHER)) {
      $router.push(`/buyFunds/${Route.params.id}`)
    }
  } else {
    $router.push(`/buyFunds/${Route.params.id}`)
  }
}
/** 获取详情 */
const getDetail = async () => {
  try {
    const res = await financialDetail(Route.params.id)
    if (res.code === 200) {
      const { title, icon, avgRate, days, limitMin, coin, prodectIntroduction, problem } = res.data
      /**产品介绍 */
      fundIntroduction.value = prodectIntroduction
      headerObj.value = {
        title,
        icon,
        leftNum: avgRate + '%',
        centerNum: days,
        rightNum: limitMin + ' ' + coin.toUpperCase(),
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
      /**产品规则 */
      proRules.value = problem
    }
  } catch (error) {}
}
onMounted(() => {
  getDetail()
})
</script>
<style lang="scss" scoped>
// 添加动画关键帧
@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
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

// 动画类
.animate-fade-in {
  animation: fadeIn 0.8s ease-out;
}

.animate-slide-up {
  animation: slideUp 0.8s ease-out;
}

.introduction {
  padding: 25px 20px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  background: linear-gradient(145deg, #1a1a1a, #2a2a2a);
  color: #a0a0a0;
  font-size: 14px;
  border-radius: 12px;
  margin: 15px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 12px 20px rgba(0, 0, 0, 0.3);
  }

  .title {
    font-size: 18px;
    color: #ffffff;
    margin-bottom: 15px;
    font-weight: 600;
    letter-spacing: 0.5px;
  }

  .content {
    line-height: 1.6;
  }
}

.buyNow {
  padding: 30px 20px 50px;

  .buyBtn {
    height: 56px;
    background: linear-gradient(135deg, #3a3a3a, #202020);
    border-radius: 28px;
    font-size: 17px;
    font-weight: 500;
    color: #ffffff;
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2),
                inset 0 1px 1px rgba(255, 255, 255, 0.1);
    transition: all 0.3s ease;
    letter-spacing: 1px;
    
    &:active {
      transform: scale(0.98);
      box-shadow: 0 3px 6px rgba(0, 0, 0, 0.15);
    }

    &:hover {
      background: linear-gradient(135deg, #404040, #252525);
      box-shadow: 0 8px 15px rgba(0, 0, 0, 0.25),
                  inset 0 1px 1px rgba(255, 255, 255, 0.1);
    }
  }
}

// 添加深色主题变量覆盖
:root {
  --ex-default-font-color: #ffffff;
  --ex-passive-font-color: #a0a0a0;
  --ex-border-color: rgba(255, 255, 255, 0.1);
  --ex-div-bgColor1: linear-gradient(135deg, #3a3a3a, #202020);
  --ex-font-color: #ffffff;
}
</style>
