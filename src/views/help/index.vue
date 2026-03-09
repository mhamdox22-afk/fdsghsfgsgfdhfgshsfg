<template>
  <!-- 帮助中心 -->
  <HeaderBar :currentName="_t18(`help_center`)" />
  <div class="help-container" v-if="list.length > 0">
    <div 
      class="itemList" 
      v-for="(item, index) in list" 
      :key="index"
      :style="{ animationDelay: `${index * 0.1}s` }"
    >
      <div class="title">
        <div class="title_top"></div>
        <div>{{ item.title }}</div>
      </div>
      <div v-if="item.infoList.length > 0" class="items-container">
       <div
          class="item"
          v-for="(items, indexs) in item.infoList"
          :key="indexs"
          @click="$router.push(`/helpDetail/${items.id}`)"
          :style="{ animationDelay: `${(index * item.infoList.length + indexs) * 0.1}s` }"
        >
          {{ items.question }}
        </div>
      </div>
    </div>
  </div>
  <Nodata v-if="list.length === 0 && isShow"></Nodata>
</template>

<script setup>
import { _t18 } from '@/utils/public'
import { getHelpcenter } from '@/api/help/index'
import { onMounted, ref } from 'vue'
const list = ref([])
const isShow = ref(false)
onMounted(async () => {
  try {
    const res = await getHelpcenter()
    if (res.code === 200) {
      list.value = res.data
      list.value.length === 0 ? (isShow.value = true) : (isShow.value = false)
    }
  } catch (error) {}
})
</script>

<style lang="scss" scoped>
.help-container {
  min-height: 100vh;
  background: #1a1a1a;
  padding: 10px 0;
}

.itemList {
  margin: 15px;
  padding: 20px;
  background: #2a2a2a;
  border-radius: 12px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  animation: slideIn 0.5s ease forwards;
  opacity: 0;
  transform: translateY(20px);
  
  .title {
    display: flex;
    align-items: center;
    font-size: 18px;
    color: #ffffff;
    font-weight: 600;
    margin-bottom: 15px;
    
    .title_top {
      width: 4px;
      height: 20px;
      margin-right: 12px;
      background: linear-gradient(45deg, #7c4dff, #448aff);
      border-radius: 2px;
      animation: pulse 2s infinite;
    }
  }

  .items-container {
    display: grid;
    gap: 12px;
  }
  
  .item {
    padding: 15px;
    font-size: 14px;
    font-weight: 400;
    color: #e0e0e0;
    background: #363636;
    border-radius: 8px;
    text-decoration: none;
    transition: all 0.3s ease;
    animation: fadeIn 0.5s ease forwards;
    opacity: 0;
    
    &:active {
      transform: scale(0.98);
    }
    
    &:hover {
      background: #404040;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    }
  }
}

@keyframes slideIn {
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeIn {
  to {
    opacity: 1;
  }
}

@keyframes pulse {
  0% {
    transform: scale(1);
    opacity: 1;
  }
  50% {
    transform: scale(1.1);
    opacity: 0.8;
  }
  100% {
    transform: scale(1);
    opacity: 1;
  }
}
</style>
