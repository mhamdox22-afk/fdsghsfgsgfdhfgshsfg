<template>
  <!-- 使用条款 -->
  <HeaderBar :currentName="_t18(`register_howToUse`)" class="header-animate"></HeaderBar>
  <div class="terms-container">
    <div class="itemDetailObj animate-content" v-html="currentHtml"></div>
    <Nodata v-if="!currentHtml"></Nodata>
  </div>
</template>
<script setup>
import { rulesList } from '@/api/common/index'
import { _t18 } from '@/utils/public'
const currentHtml = ref(null)
onMounted(async () => {
  try {
    const res = await rulesList('REGISTRY_CLAUSE')
    if (res.code === 200) {
      currentHtml.value = res.data[0].content
    }
  } catch (error) {}
})
</script>
<style>
.terms-container {
  min-height: calc(100vh - 60px);
 
}

.itemDetailObj {
  font-size: 14px;
  padding: 20px;
  color: #ffffff;
  line-height: 1.8;
  word-wrap: break-word;
  border-radius: 12px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  margin: 0 15px;
  transition: all 0.3s ease;
}

.itemDetailObj:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
}

.itemDetailObj img {
  width: 100%;
  border-radius: 8px;
  margin: 10px 0;
}

/* 添加入场动画 */
.animate-content {
  animation: slideUp 0.5s ease-out;
}

.header-animate {
  animation: fadeIn 0.3s ease-out;
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

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

/* 适配深色模式的链接样式 */
.itemDetailObj a {
  color: #60a5fa;
  text-decoration: none;
  transition: color 0.2s ease;
}

.itemDetailObj a:hover {
  color: #93c5fd;
}

/* 添加滚动条样式 */
.itemDetailObj::-webkit-scrollbar {
  width: 6px;
}

.itemDetailObj::-webkit-scrollbar-track {
  background: #1a1a1a;
}

.itemDetailObj::-webkit-scrollbar-thumb {
  background: #4a4a4a;
  border-radius: 3px;
}

.itemDetailObj::-webkit-scrollbar-thumb:hover {
  background: #606060;
}
</style>
