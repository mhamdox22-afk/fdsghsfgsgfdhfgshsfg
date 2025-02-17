<template>
  <!-- 服务条款 -->
  <div class="bind-card animate-fade-in">
    <!-- 导航条 -->
    <HeaderBar
      :currentName="_t18(`sidebar_termsService`)"
      :cuttentRight="cuttentRight"
      :border_bottom="true"
      class="header-shadow"
    ></HeaderBar>
    <!--内容-->
    <div 
      class="content-wrapper animate-slide-up"
      :class="{ 'has-content': currentHtml }"
    >
      <div class="itemDetailObj" v-html="currentHtml"></div>
      <Nodata v-if="!currentHtml"></Nodata>
    </div>
  </div>
</template>
<script setup>
import { _t18 } from '@/utils/public'
const cuttentRight = { iconRight: [{ iconName: 'kefu', clickTo: 'event_serviceChange' }] }
import { rulesList } from '@/api/common/index'
const currentHtml = ref(null)
onMounted(async () => {
  try {
    const res = await rulesList('TERMS_CLAUSE')
    if (res.code === 200) {
      currentHtml.value = res.data[0].content
    }
  } catch (error) {}
})
</script>
<style scoped>
.bind-card {
  min-height: 100vh;
  background: #1a1a1a;
  color: #ffffff;
}

.header-shadow {
  background: #242424;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.15);
}

.content-wrapper {
  padding: 16px;
  background: #242424;
  border-radius: 12px;
  margin: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
}

.content-wrapper:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 25px rgba(0, 0, 0, 0.25);
}

.content-wrapper.has-content {
  animation: glow 2s infinite alternate;
}

.itemDetailObj {
  font-size: 14px;
  line-height: 1.6;
  color: #e0e0e0;
}

.itemDetailObj img {
  width: 100%;
  border-radius: 8px;
  margin: 12px 0;
  transition: transform 0.3s ease;
}

.itemDetailObj img:hover {
  transform: scale(1.02);
}

/* 动画关键帧 */
@keyframes glow {
  from {
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
  }
  to {
    box-shadow: 0 4px 20px rgba(64, 64, 64, 0.4);
  }
}

.animate-fade-in {
  animation: fadeIn 0.5s ease-out;
}

.animate-slide-up {
  animation: slideUp 0.5s ease-out;
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
</style>
