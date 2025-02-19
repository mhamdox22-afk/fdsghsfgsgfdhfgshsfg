<!--
 * @Autor: Aka
 * @Date: 2023-06-30 17:28:36
 * @Description: 
 * @LastEditTime: 2023-08-22 10:43:44
-->
<!-- 合约规则 -->
<template>
  <div class="trading-rules-container">
    <HeaderBar :currentName="currentName" class="header-animate" />
    <div class="content-wrapper">
      <div class="itemDetailObj content-animate" v-html="currentHtml"></div>
      <Nodata v-if="!currentHtml"></Nodata>
    </div>
  </div>
</template>
<script setup>
import { useMainStore } from '@/store/index'
const mainStore = useMainStore()
import { computed, onMounted, ref } from 'vue'
import { rulesList } from '@/api/common/index'
import { useRoute } from 'vue-router'
import { _t18 } from '@/utils/public'
onMounted(()=>{
  mainStore.setTradeFlag(mainStore.tradeFlag + mainStore.isOption)
})
const $route = useRoute()
const currentName2 = computed(() => {
  switch (Number($route.query.type)) {
    case 0:
      // 秒合约规则
      return _t18(`Second_Contract_Rules`)
    case 1:
      // 币币交易规则
      return _t18(`Currency_Trading_Rules`)
    case 2:
      // U本位合约规则
      return _t18(`Ustandard_contract_rules`)
    default:
      // 秒合约规则
      return _t18(`Second_Contract_Rules`)
  }
})
const currentName = ref(currentName2.value)

const currentHtml = ref(null)
const getList = async (type) => {
  try {
    const res = await rulesList(type)
    if (res.code === 200) {
      currentHtml.value = res.data[0].content
      currentName.value=res.data[0].title||currentName2.value
    }
  } catch (error) {}
}
onMounted(() => {
  // 0 秒合约 1 币币 2 U本位
  switch (Number($route.query.type)) {
    case 0:
      getList('PERIOD_EXPLAIN')
      break
    case 1:
      getList('COIN_EXPLAIN')
      break
    case 2:
      getList('U_STANDARD_EXPLAIN')
      break
    default:
      break
  }
})
</script>
<style lang="scss" scoped>
.trading-rules-container {
  min-height: 100vh;
  background: linear-gradient(to bottom, #1a1a1a, #2d2d2d);
  position: relative;
  overflow: hidden;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 100px;
    background: linear-gradient(to bottom, rgba(0,0,0,0.3), transparent);
    pointer-events: none;
  }
}

.header-animate {
  animation: slideDown 0.5s ease-out;
}

.content-wrapper {
  padding: 15px;
  animation: fadeIn 0.6s ease-out;
}

.itemDetailObj {
  font-size: 14px;
  padding: 20px;
  color: #ffffff;
  line-height: 1.6;
  word-wrap: break-word;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 12px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2),
              0 1px 2px rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(10px);
  transition: transform 0.3s ease, box-shadow 0.3s ease;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 6px 20px rgba(0, 0, 0, 0.25),
                0 1px 2px rgba(255, 255, 255, 0.1);
  }

  img {
    width: 100%;
    border-radius: 8px;
    margin: 10px 0;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
    transition: transform 0.3s ease;

    &:hover {
      transform: scale(1.02);
    }
  }

  // 添加自定义滚动条样式
  :deep(*) {
    color: #ffffff !important;
    
    h1, h2, h3, h4, h5, h6 {
      color: #fff;
      margin: 15px 0;
      font-weight: 600;
    }

    p {
      margin: 10px 0;
      line-height: 1.8;
    }

    a {
      color: #3498db !important;
      text-decoration: none;
      transition: color 0.3s ease;

      &:hover {
        color: #2980b9 !important;
      }
    }
  }
}

@keyframes slideDown {
  from {
    transform: translateY(-20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
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

// 自定义滚动条
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-track {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 3px;
}

::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.2);
  border-radius: 3px;
  
  &:hover {
    background: rgba(255, 255, 255, 0.3);
  }
}
</style>
