<!-- 我的投资 -->
<template>
  <div class="columnFlex">
    <HeaderBar :currentName="_t18(`my_invest`)" />
    <!-- 添加投资数据卡片容器 -->
    <div class="invest-container">
      <div class="invest">
        <!-- 总投入价值卡片 -->
        <div class="item-card">
          <div class="item">
            <div>{{ _t18(`sum_bet_value`) }}</div>
            <div class="itemRight fw-num">{{ headerObj.sumAmount }} USDT</div>
          </div>
        </div>
        <!-- 当日赚取奖励卡片 -->
        <div class="item-card">
          <div class="item">
            <div>{{ _t18(`earn_rewards`) }}</div>
            <div class="itemRight fw-num">{{ headerObj.commission }} USDT</div>
          </div>
        </div>
        <!-- 累计收益卡片 -->
        <div class="item-card">
          <div class="item">
            <div>{{ _t18(`Cumulative_income`) }}</div>
            <div class="itemRight highlight fw-num">{{ headerObj.sumEarn }} USDT</div>
          </div>
        </div>
        <!-- 持仓数量卡片 -->
        <div class="item-card">
          <div class="item">
            <div>{{ _t18(`number_of_positions`) }}</div>
            <div class="itemRight fw-num">{{ headerObj.position }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 交易记录部分 -->
    <div class="record-section">
      <div class="record">{{ _t18(`transaction_record`) }}</div>
      <div class="recordList">
        <!-- 没有更多数据了 no_more_data 加载中  loading-->
        <van-list
          v-if="list.length > 0 && !isShow"
          v-model:loading="loading"
          :finished="finished"
          :finished-text="_t18(`no_more_data`)"
          :loading-text="_t18(`loading`)"
          @load="onLoad"
        >
          <RecordItem v-for="item in list" :key="item" :itemObj="item"></RecordItem>
        </van-list>
        <Nodata v-if="list.length === 0 && isShow"></Nodata>
      </div>
    </div>
  </div>
</template>
<script setup>
import HeaderBar from '@/components/HeaderBar/index.vue'
import RecordItem from './components/recordItem.vue'
import { ref, onMounted } from 'vue'
import { investmentList, personalIncome } from '@/api/financial/index'
import { _t18 } from '@/utils/public'
import { _toFixed } from '@/utils/decimal'
const headerObj = ref({})
const list = ref([])
const pageNum = ref(1)
const pageSize = ref(5)
const isShow = ref(false)
const loading = ref(false)
const finished = ref(false)
const total = ref(0)
const onLoad = async () => {
  try {
    const data = await getInvestment()
    list.value = [...list.value, ...data]
    list.value.length === 0 ? (isShow.value = true) : (isShow.value = false)
    if (list.value.length === total.value) {
      finished.value = true
    }
    pageNum.value++
  } catch (error) {
  } finally {
    loading.value = false
  }
}
const allList = async () => {
  try {
    const res = await personalIncome()
    if (res.code === 200) {
      headerObj.value = res.data
    }
  } catch (error) {}
}
const getInvestment = async () => {
  try {
    const res = await investmentList(pageNum.value, pageSize.value)
    if (res.code === 200) {
      total.value = res.total
      return res.rows
    } else {
      finished.value = true
    }
  } catch (error) {}
}
onMounted(() => {
  allList()
  onLoad()
})
</script>
<style lang="scss" scoped>
.columnFlex {
  height: 100vh;
  background: linear-gradient(to bottom, #1a1a1a, #2d2d2d);
  color: #ffffff;
}

.invest-container {
  padding: 15px;
  animation: fadeIn 0.5s ease-in-out;
}

.invest {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
  
  .item-card {
    background: linear-gradient(145deg, #2a2a2a, #333333);
    border-radius: 15px;
    padding: 20px;
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
    transition: transform 0.3s ease, box-shadow 0.3s ease;
    
    &:hover {
      transform: translateY(-5px);
      box-shadow: 0 12px 20px rgba(0, 0, 0, 0.3);
    }

    .item {
      display: flex;
      flex-direction: column;
      gap: 10px;
      
      div:first-child {
        color: #a0a0a0;
        font-size: 14px;
      }

      .itemRight {
        color: #ffffff;
        font-size: 18px;
        font-weight: 600;
      }

      .highlight {
        background: linear-gradient(90deg, #4a90e2, #63e2ff);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        font-weight: bold;
      }
    }
  }
}

.record-section {
  background: rgba(255, 255, 255, 0.05);
  border-radius: 20px 20px 0 0;
  margin-top: 20px;
  flex: 1;
  
  .record {
    font-size: 18px;
    color: #ffffff;
    padding: 20px;
    border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  }
}

.recordList {
  flex: 1;
  overflow: auto;
  padding: 0 15px;
}

:deep(.van-list__finished-text) {
  height: 79px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  color: rgba(255, 255, 255, 0.6);
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

// 添加滚动条样式
.recordList::-webkit-scrollbar {
  width: 6px;
}

.recordList::-webkit-scrollbar-track {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 3px;
}

.recordList::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.2);
  border-radius: 3px;
  
  &:hover {
    background: rgba(255, 255, 255, 0.3);
  }
}
</style>
