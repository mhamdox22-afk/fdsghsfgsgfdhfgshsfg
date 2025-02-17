<!-- 质押挖矿 -->
<script setup>
import { _t18 } from '@/utils/public'
import { onMounted } from 'vue'
import HeaderBar from '@/components/HeaderBar/index.vue'
import { rulesList } from '@/api/common/index'
const cuttentRight = { iconRight: [{ iconName: 'kefu', clickTo: 'event_serviceChange' }] }
const currentHtml = ref(null)
onMounted(async () => {
  try {
    const res = await rulesList('LOANS_RULE')
    if (res.code === 200) {
      currentHtml.value = res.data[0].content
    }
  } catch (error) {}
})
</script>
<template>
  <div class="loan-rule-container">
    <HeaderBar :currentName="_t18('loan_rule')" :cuttentRight="cuttentRight"></HeaderBar>
    <div class="content-wrapper">
      <div class="itemDetailObj" v-html="currentHtml"></div>
      <Nodata v-if="!currentHtml"></Nodata>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.loan-rule-container {
  min-height: 100vh;
  background: linear-gradient(145deg, #1a1a1a, #2d2d2d);
  
  .content-wrapper {
    padding: 16px;
    animation: fadeIn 0.5s ease-in-out;
  }
}

.itemDetailObj {
  font-size: 14px;
  padding: 20px;
  color: #ffffff;
  line-height: 1.6;
  word-wrap: break-word;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 12px;
  box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.2);
  backdrop-filter: blur(10px);
  transition: all 0.3s ease;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 12px 40px 0 rgba(0, 0, 0, 0.3);
  }

  img {
    width: 100%;
    border-radius: 8px;
    margin: 10px 0;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    transition: transform 0.3s ease;
    
    &:hover {
      transform: scale(1.02);
    }
  }

  // 添加自定义滚动条样式
  :deep(*) {
    color: #ffffff !important;
    
    &::-webkit-scrollbar {
      width: 6px;
    }
    
    &::-webkit-scrollbar-track {
      background: rgba(255, 255, 255, 0.1);
      border-radius: 3px;
    }
    
    &::-webkit-scrollbar-thumb {
      background: rgba(255, 255, 255, 0.2);
      border-radius: 3px;
      
      &:hover {
        background: rgba(255, 255, 255, 0.3);
      }
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

// 添加深色主题下的链接样式
:deep(a) {
  color: #7eb6ff !important;
  text-decoration: none;
  transition: color 0.3s ease;
  
  &:hover {
    color: #a8cfff !important;
  }
}

// 确保 Nodata 组件在深色主题下可见
:deep(.nodata) {
  background: rgba(255, 255, 255, 0.05);
  border-radius: 12px;
  padding: 20px;
  margin-top: 20px;
  box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.2);
}
</style>
