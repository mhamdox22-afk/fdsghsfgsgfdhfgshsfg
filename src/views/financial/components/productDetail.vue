<template>
  <div class="detail">
    <!-- 产品详情 -->
    <div class="title fw-bold">{{ _t18(`product_details`) }}</div>
    <!-- 项目名称 -->
    <div class="item">
      <div>{{ _t18(`product-details`) }}</div>
      <div class="itemName">{{ proDetail.title }}</div>
    </div>
    <!-- 项目进度 -->
    <div class="item">
      <div>{{ _t18(`project_progress`) }}</div>
      <div class="itemName ff-num">{{ proDetail.process }}%</div>
    </div>
    <!-- 项目总额 -->
    <div class="item">
      <div>{{ _t18(`total_project`) }}</div>
      <div class="itemName ff-num">{{ proDetail.totalInvestAmount }}&nbsp;{{ proDetail.coin }}</div>
    </div>
    <!-- 剩余金额 -->
    <div class="item">
      <div>{{ _t18(`balance`) }}</div>
      <div class="itemName ff-num">{{ proDetail.remainAmount }}&nbsp;{{ proDetail.coin }}</div>
    </div>
    <!-- 限投次数 -->
    <div class="item">
      <div>{{ _t18(`limit_number`) }}</div>
      <div class="itemName fw-num">
        <!-- 不存在则不限购 -->
        {{ Number(proDetail.timeLimit) ? Number(proDetail.timeLimit) : _t18(`unlimited_purchase`) }}
      </div>
    </div>
    <!-- 平均日收益 -->
    <div class="item">
      <div>{{ _t18(`average_daily_income`) }}</div>
      <div class="itemName fw-num hightColor">{{ proDetail.avgRate }}%</div>
    </div>
  </div>
</template>
<script setup>
import { _t18 } from '@/utils/public'
const props = defineProps({
  proDetail: {
    type: Object,
    default: {}
  }
})
</script>
<style lang="scss" scoped>
.detail {
  padding: 25px 20px;
  background: linear-gradient(145deg, #1a1a1a, #2d2d2d);
  border-radius: 16px;
  box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.36);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  animation: fadeIn 0.5s ease-out;

  .title {
    color: #ffffff;
    font-size: 18px;
    margin-bottom: 5px;
    position: relative;
    padding-left: 12px;
    
    &::before {
      content: '';
      position: absolute;
      left: 0;
      top: 50%;
      transform: translateY(-50%);
      width: 4px;
      height: 18px;
      background: linear-gradient(to bottom, #7367f0, #ce9ffc);
      border-radius: 2px;
    }
  }

  .item {
    margin-top: 22px;
    padding: 12px 15px;
    color: rgba(255, 255, 255, 0.6);
    font-size: 13px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: rgba(255, 255, 255, 0.05);
    border-radius: 12px;
    transition: all 0.3s ease;
    
    &:hover {
      transform: translateX(5px);
      background: rgba(255, 255, 255, 0.08);
      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
    }

    .itemName {
      color: #ffffff;
      font-weight: 500;
      
      &.ff-num {
        font-size: 15px;
        background: linear-gradient(45deg, #7367f0, #ce9ffc);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
      }
    }

    .hightColor {
      color: #7367f0;
      font-size: 16px;
      font-weight: bold;
    }
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

// 添加深色主题相关的CSS变量覆盖
:root {
  --ex-default-font-color: #ffffff;
  --ex-passive-font-color: rgba(255, 255, 255, 0.6);
  --ex-font-color3: #7367f0;
}
</style>
