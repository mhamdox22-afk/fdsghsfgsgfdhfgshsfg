<template>
  <div class="loan-record-container">
    <!-- 导航条 -->
    <HeaderBar
      :currentName="_t18('loan_order', ['aams'])"
      :cuttentRight="cuttentRight"
      :border_bottom="true"
      class="header-animation"
    ></HeaderBar>
    <!--内容-->
    <!-- <div class="content">
      <van-tabs :active="active" @click-tab="handleClick">
        <van-tab v-for="item in headerList" :title="item.name">
          <LoanItem v-for="data in dataList" :dataList="data" :key="data"></LoanItem
        ></van-tab>
      </van-tabs>
    </div> -->
    <Tab :tabList="tabList" :active="curIndex" @change="changeIndex" class="custom-tab">
      <template #tabContent>
        <!-- release_refresh释放即可刷新 -->
        <!-- 下拉刷新 -->
        <van-pull-refresh
          v-model="refreshing"
          @refresh="onRefresh"
          :loading-text="_t18(`loading`)"
          :loosing-text="_t18(`release_refresh`)"
          class="refresh-container"
        >
          <!-- 加载中动画 -->
          <van-loading v-if="showLoading" class="loading-animation"/>
          <!-- 数据列表 -->
          <div v-else class="content-container">
            <!-- 没有更多数据了 no_more_data 加载中  loading-->
            <van-list
              v-if="tabContentList.length > 0"
              v-model:loading="loading"
              :finished="finished"
              :finished-text="_t18(`no_more_data`)"
              :loading-text="_t18(`loading`)"
              @load="onLoad"
              class="list-container"
            >
              <van-cell v-for="(item, index) in tabContentList" 
                       :key="index" 
                       class="cell-item"
              >
                <LoanItem :dataList="item"></LoanItem>
              </van-cell>
            </van-list>
            <!-- 数据为空 -->
            <Nodata v-else class="no-data-animation"/>
          </div>
        </van-pull-refresh>
      </template>
    </Tab>
  </div>
</template>

<script setup>
import HeaderBar from '@/components/HeaderBar/index.vue'
import LoanItem from './components/LoanItem.vue'
import { getLoanOrderList } from '@/api/loan.js'
import Tab from '@/components/Tab/index.vue'
import { dict } from '@/api/common/index'
import { _t18 } from '@/utils/public'
const refreshing = ref(false) //下拉刷新的加载展示
const showLoading = ref(true) //加载动画
const loading = ref(false) //分页加载
const finished = ref(false) //数据是否加载完成
//下拉加载事件（初始化数据）
const onRefresh = () => {
  refreshing.value = false
  showLoading.value = true
  finished.value = false
  pageNum.value = 1
  loading.value = true
  tabContentList.value = []
  getList()
}

const curIndex = ref(0)
const tabList = computed(() => {
  let list = []
  // list = ['全部', '待审核', '审核成功', '审核失败', '已结清', '已逾期']
  list = [
    _t18('loan_tab_all', ['aams']),
    _t18('loan_tab_wait'),
    _t18('loan_tab_success'),
    _t18('loan_tab_err'),
    _t18('loan_tab_complete'),
    _t18('loan_tab_overdue')
  ]
  return list
})
/**
 * 充值记录查询
 * pageNum页码
 * pageSize每页条数
 * total总条数
 */
onMounted(() => {
  getDict()
})
const dictList = ref()
const getDict = async () => {
  const res = await dict('t_repay_type')
  if (res.code == '200') {
    // repayTypeLabel.value=res.data[0].dictLabel
    dictList.value = res.data
  }
}
const pageNum = ref(1)
const pageSize = ref(3)
const total = ref(0)
const tabContentList = ref([]) //数据列表
const getList = () => {
  let params = `pageNum=${pageNum.value}&pageSize=${pageSize.value}`
  // 排除全部时不传参数
  if (curIndex.value != '0') {
    params = `status=${curIndex.value - 1}&pageNum=${pageNum.value}&pageSize=${pageSize.value}`
  }
  getLoanOrderList(params).then((res) => {
    if (res.code == '200') {
      setTimeout(() => {
        // 页面加载动画，下拉刷新动画
        if (showLoading.value) {
          showLoading.value = false
        }
        if (refreshing.value) {
          refreshing.value = false
        }
      }, 200)
      //分页加载动画
      loading.value = false
      tabContentList.value = tabContentList.value.concat(res.rows)
      total.value = res.total
      if (tabContentList.value.length >= total.value) {
        finished.value = true
      }
      pageNum.value++
    } else {
      finished.value = true
      loading.value = false
      showLoading.value = false
      refreshing.value = false
    }
  })
}
const onLoad = () => {
  loading.value = true
  getList()
}
const changeIndex = (v) => {
  curIndex.value = v
  showLoading.value = true
  tabContentList.value = []
  finished.value = false
  pageNum.value = 1
  loading.value = true
}
watch(
  curIndex,
  () => {
    getList()
  },
  {
    immediate: true
  }
)
watch(tabContentList, (newValue) => {
  newValue.forEach((item, index) => {
    dictList.value?.forEach((itm, idx) => {
      if (item.tloadProduct.repayType == itm.dictValue) {
        item['repayTypeLabel'] = itm.dictLabel
      }
    })
  })
})
</script>
<style lang="scss" scoped>
.loan-record-container {
  min-height: 100vh;
  background: #1a1a1a;
  color: #ffffff;

  .header-animation {
    animation: slideDown 0.3s ease-out;
  }

  .custom-tab {
    margin-top: 10px;

    :deep(.tabs-nav) {
      min-width: 100%;
      overflow-x: auto;
      margin-bottom: 10px;
    }
    :deep(.tab-item) {
      background: #2a2a2a;
      border-radius: 8px;
      margin: 0 5px;
      padding: 8px 15px;
      transition: all 0.3s ease;
      white-space: nowrap;
      &.active {
        background: #3a3a3a;
        transform: scale(1.05);
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.3);
      }
    }
  }

  .refresh-container {
    background: #1a1a1a;
  }

  .content-container {
    padding: 10px;
  }

  .list-container {
    .cell-item {
      margin-bottom: 15px;
      border-radius: 12px;
      background: #2a2a2a !important;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
      transform: translateY(0);
      transition: all 0.3s ease;
      animation: fadeIn 0.5s ease-out;

      &:hover {
        transform: translateY(-3px);
        box-shadow: 0 6px 15px rgba(0, 0, 0, 0.3);
      }
    }
  }

  .loading-animation {
    padding: 30px;
    text-align: center;
    color: #ffffff;
  }

  .no-data-animation {
    animation: fadeIn 0.5s ease-out;
  }

  :deep {
    .van-pull-refresh {
      background: #1a1a1a;
    }

    .van-tabs {
      background: #1a1a1a !important;
    }

    .van-tabs__nav {
      background: #1a1a1a !important;
    }

    .van-tab {
      color: #ffffff !important;
      background: #2a2a2a !important;
    }

    .van-tab--active {
      color: #4a9eff !important;
    }

    .van-cell {
      background: #2a2a2a !important;
      color: #ffffff;
      
      &::after {
        border-bottom: 1px solid #3a3a3a !important;
      }
    }

    .van-list__loading,
    .van-list__finished-text {
      color: #888888;
    }

    .van-loading__spinner {
      color: #4a9eff;
    }
  }
}

// Animation keyframes
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
</style>
