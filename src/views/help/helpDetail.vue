<template>
  <div class="help-detail-container">
    <HeaderBar :currentName="_t18(`help_center`)" />
    <div class="content animate-fade-in">
      <div class="card">
        <div class="title">{{ questionDetail.question }}</div>
        <div class="time">{{ questionDetail.createTime }}</div>

        <div class="divider"></div>

        <div class="itemDetailObj" v-html="currentHtml"></div>
        <Nodata v-if="!currentHtml"></Nodata>
      </div>
    </div>
  </div>
</template>
<script setup>
import { _t18 } from '@/utils/public'
import { getHelpcenterDetail } from '@/api/help/index'
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
const Route = useRoute()
const currentHtml = ref('')
const questionDetail = ref({})
onMounted(async () => {
  if (Route.params.id) {
    try {
      const res = await getHelpcenterDetail(Route.params.id)
      if (res.code === 200) {
        questionDetail.value = res.data
        currentHtml.value = res.data.content
      }
    } catch (error) {}
  }
})
</script>
<style lang="scss" scoped>
.help-detail-container {
  background-color: #121212;
  min-height: 100vh;
  color: #f5f5f5;
  
  @keyframes fadeIn {
    from { opacity: 0; transform: translateY(20px); }
    to { opacity: 1; transform: translateY(0); }
  }
  
  .animate-fade-in {
    animation: fadeIn 0.6s ease-out forwards;
  }
}

.content {
  max-width: 900px;
  margin: 0 auto;
}

.card {
  background-color: #1e1e1e;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.5), 0 1px 3px rgba(0, 0, 0, 0.3);
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  
  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 15px 35px rgba(0, 0, 0, 0.6), 0 3px 10px rgba(0, 0, 0, 0.4);
  }
}

.title {
  font-size: 24px;
  font-weight: bold;
  color: #ffffff;
  padding: 25px 25px 10px;
  letter-spacing: 0.5px;
  text-shadow: 0px 2px 4px rgba(0, 0, 0, 0.5);
}

.time {
  padding: 0 25px 15px;
  font-size: 14px;
  color: #9e9e9e;
  font-style: italic;
}

.divider {
  height: 1px;
  background: linear-gradient(to right, transparent, rgba(255, 255, 255, 0.2), transparent);
  margin: 0 15px 15px;
}

:deep(.content) {
  .itemDetailObj {
    font-size: 15px;
    padding: 5px 25px 30px;
    color: #e0e0e0;
    line-height: 1.6;
    
    &::v-deep(a) {
      color: #64b5f6;
      text-decoration: none;
      position: relative;
      
      &:after {
        content: '';
        position: absolute;
        width: 100%;
        height: 1px;
        bottom: -2px;
        left: 0;
        background-color: #64b5f6;
        transform: scaleX(0);
        transform-origin: bottom right;
        transition: transform 0.3s ease-out;
      }
      
      &:hover:after {
        transform: scaleX(1);
        transform-origin: bottom left;
      }
    }
  }
  
  strong {
    font-weight: bold;
    color: #ffffff;
  }
  
  img {
    margin: 20px 0;
    width: 100%;
    object-fit: contain;
    border-radius: 8px;
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.4);
    transition: transform 0.3s ease;
    
    &:hover {
      transform: scale(1.02);
    }
  }
}
</style>
