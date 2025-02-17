<!-- 行情——>自选 -->
<template>
  <div class="quote-optional">
    <Filter
      :isOptional="isOptional && collectList?.length > 0"
      :list="collectList"
      @toSort="toSort"
    />
    <div v-if="collectList?.length > 0" class="collect-list">
      <CollectItem
        v-for="(item, index) in filterKeyCoin(collectList, searchName, 2)"
        :key="index"
        :collectItem="item"
        @click="linkTo(item)"
        class="collect-item"
      >
      </CollectItem>
    </div>
    <div v-else class="empty-state">
      <Nodata></Nodata>
      <div class="addOptional">
        <div class="addOptionalBtn" @click="$router.push('/addOptional')">
          +{{ _t18(`quote_addOwn`) }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { filterKeyCoin } from '@/utils/filters'
import Filter from './common/filter.vue'
import CollectItem from './common/collectItem.vue'
import { getCollect } from '@/api/trade'
import { onMounted } from 'vue'
import { useUserStore } from '@/store/user'
const props = defineProps({
  searchName: {
    type: String
  }
})
const isOptional = ref(true)
// 自选列表
const collectList = ref()
const getCollectList = async () => {
  const res = await getCollect()
  if (res.code == 200) {
    collectList.value = res.data
  }
}
const userStore = useUserStore()
onMounted(() => {
  if (userStore.isSign) {
    getCollectList()
  }
})
const toSort = (v) => {
  collectList.value = v
}

import { useRouter } from 'vue-router'
const router = useRouter()
const linkTo=(item)=>{
  router.push(`/trade?symbol=${item.coin}`)
}
</script>

<style lang="scss" scoped>
.quote-optional {
  min-height: 100vh;
  background: #121212;
  color: #ffffff;
  
  .collect-list {
    padding: 10px;
    
    .collect-item {
      margin-bottom: 12px;
      transform: translateY(0);
      transition: all 0.3s ease;
      
      &:hover {
        transform: translateY(-2px);
      }
    }
  }

  .empty-state {
    animation: fadeIn 0.5s ease;
  }

  .addOptional {
    padding: 0 15px 81px;

    .addOptionalBtn {
      display: flex;
      align-items: center;
      justify-content: center;
      height: 50px;
      background: #1a1a1a;
      border-radius: 8px;
      font-size: 16px;
      font-weight: normal;
      color: #ffffff;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
      transition: all 0.3s ease;
      
      &:active {
        transform: scale(0.98);
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
      }
    }
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
</style>
