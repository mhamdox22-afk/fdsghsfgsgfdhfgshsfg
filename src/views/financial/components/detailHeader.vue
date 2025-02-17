<template>
  <div class="detail-header-wrapper">
    <div class="header animate-in">
      <div v-if="!headerObj.icon" class="headerImg pulse"></div>
      <image-load :filePath="headerObj.icon" v-if="headerObj.icon" class="itemImg pulse" />
      <div class="fw-bold title-animate">{{ headerObj.title }}</div>
    </div>
    <div class="headerList animate-up">
      <div class="item">
        <div
          class="itemNum fw-num"
          :style="headerObj.leftColor ? `color: ${headerObj.leftColor}` : ''"
        >
          {{ headerObj.leftNum }}
        </div>
        <div class="itemName">{{ headerObj.leftName }}</div>
      </div>
      <div class="item">
        <div class="itemNum fw-num">{{ headerObj.centerNum }}</div>
        <div class="itemName">{{ headerObj.centerName }}</div>
      </div>
      <div class="item">
        <div class="itemNum fw-num">{{ headerObj.rightNum }}</div>
        <div class="itemName">{{ headerObj.rightName }}</div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { defineProps } from 'vue'
const props = defineProps({
  headerObj: {
    type: Object,
    default: {}
  }
})
</script>
<style lang="scss" scoped>
.detail-header-wrapper {
  background: linear-gradient(145deg, #1a1a1a, #2d2d2d);
  border-radius: 15px;
  margin: 15px;
  box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.36);
  backdrop-filter: blur(4px);
  -webkit-backdrop-filter: blur(4px);
  border: 1px solid rgba(255, 255, 255, 0.05);
  overflow: hidden;
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 12px 40px 0 rgba(0, 0, 0, 0.45);
  }
}

.header {
  padding: 25px 20px;
  font-size: 16px;
  color: #ffffff;
  display: flex;
  align-items: center;
  position: relative;
  
  .headerImg {
    width: 50px;
    height: 50px;
    background: rgba(255, 255, 255, 0.1);
    border-radius: 12px;
    margin-right: 15px;
    transition: all 0.3s ease;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  }

  .itemImg {
    width: 50px;
    height: 50px;
    object-fit: cover;
    border-radius: 12px;
    margin-right: 15px;
    transition: all 0.3s ease;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  }

  .fw-bold {
    background: linear-gradient(90deg, #ffffff, #e0e0e0);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    font-size: 18px;
    font-weight: 600;
  }
}

.headerList {
  padding: 0 20px 25px;
  display: flex;
  gap: 20px;

  .item {
    flex: 1;
    transition: all 0.3s ease;
    padding: 15px;
    border-radius: 12px;
    background: rgba(255, 255, 255, 0.05);
    
    &:hover {
      background: rgba(255, 255, 255, 0.08);
      transform: translateY(-2px);
    }

    .itemNum {
      font-size: 18px;
      color: #ffffff;
      font-weight: 600;
      margin-bottom: 5px;
    }

    .itemName {
      margin-top: 10px;
      font-size: 12px;
      color: rgba(255, 255, 255, 0.6);
    }
  }

  .item:nth-child(2) {
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
  }

  .item:nth-child(3) {
    text-align: right;
    display: flex;
    flex-direction: column;
    align-items: flex-end;
  }
}

// 动画效果
.animate-in {
  animation: fadeIn 0.5s ease-out;
}

.animate-up {
  animation: slideUp 0.5s ease-out;
}

.pulse {
  animation: pulse 2s infinite;
}

.title-animate {
  animation: shimmer 2s infinite;
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

@keyframes pulse {
  0% {
    box-shadow: 0 0 0 0 rgba(255, 255, 255, 0.1);
  }
  70% {
    box-shadow: 0 0 0 10px rgba(255, 255, 255, 0);
  }
  100% {
    box-shadow: 0 0 0 0 rgba(255, 255, 255, 0);
  }
}

@keyframes shimmer {
  0% {
    opacity: 1;
  }
  50% {
    opacity: 0.8;
  }
  100% {
    opacity: 1;
  }
}
</style>
