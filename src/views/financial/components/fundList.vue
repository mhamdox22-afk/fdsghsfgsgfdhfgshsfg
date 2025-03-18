<template>
  <div class="fund-list-container">
    <div class="fund-list" v-if="listArray.length > 0 && !isShow">
      <TransitionGroup 
        name="fund-item"
        tag="div"
        appear
      >
        <ItemFund 
          v-for="(item, index) in listArray" 
          :key="item.id" 
          :itemObj="item"
          :style="{ animationDelay: `${index * 0.1}s` }"
        ></ItemFund>
      </TransitionGroup>
    </div>
    <Nodata v-if="listArray.length === 0 && isShow"></Nodata>
  </div>
</template>
<script setup>
import ItemFund from './itemFund.vue'
import { onMounted, ref } from 'vue'
import { getFinancial } from '@/api/financial/index'
const listArray = ref([])
const isShow = ref(false)
const getInit = async () => {
  try {
    let data = {
      classify: ''
    }
    const res = await getFinancial(data)
    if (res.code === 200) {
      listArray.value = res.rows
      listArray.value.length === 0 ? (isShow.value = true) : (isShow.value = false)
    }
  } catch (error) {
    console.log(45)
  }
}
onMounted(() => {
  getInit()
})
</script>
<style lang="scss" scoped>
.fund-list-container {
  background: #131316;
  min-height: 100vh;
}

.fund-list {
  padding: 20px 15px 66px;
}

// 列表项动画
.fund-item-enter-active,
.fund-item-leave-active {
  transition: all 0.5s ease;
}

.fund-item-enter-from {
  opacity: 0;
  transform: translateY(30px);
}

.fund-item-leave-to {
  opacity: 0;
  transform: translateY(-30px);
}

// 列表项出现动画
:deep(.fund-item) {
  animation: slideIn 0.5s ease forwards;
  opacity: 0;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
