<template>
  <div class="rules animate-fade-in">
    <!-- 产品规则 -->
    <div class="title fw-bold">{{ _t18('product_rules') }}</div>
    <!-- 暂无介绍 -->
    <div class="item">{{ proRules ? proRules : _t18(`No_introductionyet`) }}</div>
  </div>
</template>
<script setup>
import { _t18 } from '@/utils/public'
const props = defineProps({
  proRules: {
    type: String,
    default: ''
  }
})
</script>
<style lang="scss" scoped>
.rules {
  padding: 25px 20px;
  background: linear-gradient(145deg, #1a1a1a, #2d2d2d);
  border: none;
  border-radius: 15px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.3),
              inset 0 2px 4px rgba(255, 255, 255, 0.1);
  margin: 15px;
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;

  &:before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 1px;
    background: linear-gradient(90deg, 
      rgba(255, 255, 255, 0),
      rgba(255, 255, 255, 0.1),
      rgba(255, 255, 255, 0));
  }

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 12px 25px rgba(0, 0, 0, 0.4);
  }

  .title {
    color: #ffffff;
    font-size: 18px;
    margin-bottom: 20px;
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
    position: relative;
    
    &:after {
      content: '';
      position: absolute;
      bottom: -8px;
      left: 0;
      width: 40px;
      height: 3px;
      background: linear-gradient(90deg, #4a90e2, #79b8ff);
      border-radius: 3px;
      transition: width 0.3s ease;
    }

    &:hover:after {
      width: 60px;
    }
  }

  .item {
    font-size: 14px;
    color: #e0e0e0;
    margin-bottom: 15px;
    line-height: 1.6;
    letter-spacing: 0.5px;
    opacity: 0.9;
  }

  .tail {
    color: #ffffff;
    font-size: 13px;
    opacity: 0.8;
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

.animate-fade-in {
  animation: fadeIn 0.5s ease-out forwards;
}
</style>
