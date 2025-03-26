<template>
  <!-- 理财协议 -->
  <HeaderBar :currentName="_t18(`financial agreement`)"></HeaderBar>
  <div class="financial-container">
    <div class="itemDetailObj animate-fade-in" v-html="currentHtml" v-if="currentHtml"></div>
    <Nodata v-if="!currentHtml"></Nodata>
  </div>
</template>
<script setup>
import { rulesList } from '@/api/common/index'
import { _t18 } from '@/utils/public'
const currentHtml = ref(null)
onMounted(async () => {
  try {
    const res = await rulesList('FINANCIAL_AGREEMENT')
    if (res.code === 200) {
      currentHtml.value = res.data[0].content
    }
  } catch (error) {}
})
</script>
<style scoped>
.financial-container {
  min-height: calc(100vh - 60px);
  display: flex;
  justify-content: center;
}

.itemDetailObj {
  font-size: 14px;
  padding: 25px 30px;
  color: #f2f2f2;
  line-height: 1.6;
  word-wrap: break-word;
  background-color: #252525;
  border-radius: 8px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3), 
              0 1px 3px rgba(0, 0, 0, 0.4),
              0 0 20px rgba(92, 92, 92, 0.1) inset;
  max-width: 1000px;
  width: 100%;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.itemDetailObj:hover {
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.4), 
              0 3px 10px rgba(0, 0, 0, 0.5),
              0 0 30px rgba(110, 110, 110, 0.15) inset;
  transform: translateY(-3px);
}

.itemDetailObj img {
  width: 100%;
  border-radius: 6px;
  margin: 10px 0;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  transition: transform 0.3s ease;
}

.itemDetailObj img:hover {
  transform: scale(1.02);
}

.itemDetailObj h1, 
.itemDetailObj h2, 
.itemDetailObj h3, 
.itemDetailObj h4, 
.itemDetailObj h5 {
  color: #ffffff;
  margin-top: 24px;
  margin-bottom: 16px;
  font-weight: 600;
}

.itemDetailObj p {
  margin-bottom: 16px;
}

.itemDetailObj a {
  color: #6ca0ff;
  text-decoration: none;
  position: relative;
  transition: all 0.2s ease;
}

.itemDetailObj a:hover {
  color: #90b8ff;
}

.itemDetailObj a:after {
  content: '';
  position: absolute;
  width: 0;
  height: 1px;
  bottom: -2px;
  left: 0;
  background-color: #90b8ff;
  transition: width 0.3s ease;
}

.itemDetailObj a:hover:after {
  width: 100%;
}

.animate-fade-in {
  animation: fadeIn 0.8s ease-out forwards;
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

/* 确保深色模式下内容中的文字也是白色的 */
:deep(.itemDetailObj p),
:deep(.itemDetailObj li),
:deep(.itemDetailObj span),
:deep(.itemDetailObj div) {
  color: #e6e6e6 !important;
}

:deep(.itemDetailObj table) {
  border-collapse: collapse;
  width: 100%;
  margin: 15px 0;
  border-radius: 6px;
  overflow: hidden;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

:deep(.itemDetailObj th),
:deep(.itemDetailObj td) {
  padding: 12px 15px;
  border: 1px solid #333;
}

:deep(.itemDetailObj th) {
  background-color: #333;
  color: white;
}

:deep(.itemDetailObj tr:nth-child(even)) {
  background-color: #2a2a2a;
}

:deep(.itemDetailObj tr:hover) {
  background-color: #303030;
}
</style>
