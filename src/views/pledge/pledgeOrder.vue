<template>
  <HeaderBar
    :currentName="_t18('pledge_order_record')"
    :cuttentRight="cuttentRight"
    :border_bottom="true"
  ></HeaderBar>
  <Tab :tabList="tabList" :active="curIndex" @change="changeIndex">
    <template #tabContent>
      <!-- 下拉刷新 -->
      <van-pull-refresh
        v-model="refreshing"
        @refresh="onRefresh"
        :loading-text="_t18(`loading`)"
        :loosing-text="_t18(`release_refresh`)"
      >
        <!-- 加载中动画 -->
        <van-loading v-if="showLoading" />
        <!-- 数据列表 -->
        <div v-else>
          <van-list
            v-if="tabContentList.length > 0"
            v-model:loading="loading"
            :finished="finished"
            :finished-text="_t18(`no_more_data`)"
            :loading-text="_t18(`loading`)"
            @load="onLoad"
          >
            <van-cell v-for="(item, index) in tabContentList" :key="index" class="card-cell">
              <OrderCard :data="item" @getList="getList" class="order-card"></OrderCard>
            </van-cell>
          </van-list>
          <!-- 数据为空 -->
          <Nodata v-else />
        </div>
      </van-pull-refresh> </template
  ></Tab>
</template>
<script setup>
import HeaderBar from '@/components/HeaderBar/index.vue'
import { getPledgeOrderList } from '@/api/pledge/index'
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { _t18 } from '@/utils/public'
import Tab from '@/components/Tab/index.vue'
import OrderCard from './components/orderCard.vue'
import { showToast } from 'vant'
const refreshing = ref(false) //下拉刷新的加载展示
const showLoading = ref(true) //加载动画
const loading = ref(false) //分页加载
const finished = ref(false) //数据是否加载完成
const status = ref()
const getList = () => {
  tabContentList.value = []
  pageNum.value = 1
  getOrderList(status.value)
}
//下拉加载事件（初始化数据）
const onRefresh = () => {
  refreshing.value = false
  showLoading.value = true
  finished.value = false
  pageNum.value = 1
  loading.value = true
  tabContentList.value = []
  getOrderList(status.value)
}
const route = useRoute()
const curIndex = ref(0)
const cuttentRight = reactive({
  iconRight: [{ iconName: 'kefu', clickTo: 'event_serviceChange' }]
})
const tabList = computed(() => {
  let list = []
  list = [
    _t18('all', ['aams']),
    _t18('pledge_in_progress'),
    _t18('pledge_completed'),
    _t18('pledge_redeem_ok')
  ]
  return list
})
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const tabContentList = ref([])
const getOrderList = async (status) => {
  let params = `pageNum=${pageNum.value}&pageSize=${pageSize.value}`
  // 排除全部时不传参数
  if (curIndex.value != '0') {
    // curIndex.value - 1
    params = `status=${status}&pageNum=${pageNum.value}&pageSize=${pageSize.value}`
  }
  const res = await getPledgeOrderList(params)
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
    showToast(res.msg)
  }
}
const onLoad = () => {
  loading.value = true
  getOrderList(status.value)
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
  (newValue) => {
    status.value = newValue - 1
    getOrderList(status.value)
  },
  {
    immediate: true
  }
)
</script>

<style lang="scss" scoped>
.content {
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  padding: 20px 15px;
  transition: all 0.3s ease;
}
.van-loading {
  text-align: center;
  padding: 30px;
}
.card-cell {
  margin: 12px 16px;
  border-radius: 12px;
  overflow: hidden;
  transform: translateY(0);
  transition: all 0.3s ease;
  
  &:hover {
    transform: translateY(-2px);
  }
}
.order-card {
  animation: fadeIn 0.5s ease-out;
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
:deep(.van-cell) {
  background: #1a1a1a !important;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  margin-bottom: 12px;
  border-radius: 12px;
  
  &:active {
    background: #252525 !important;
  }
}
:deep(.van-tabs) {
  background: #121212 !important;
}
.van-tabs {
  :deep(.van-tab) {
    color: #ffffff !important;
    background: #121212 !important;
  }

  :deep(.van-tab--active) {
    color: #4a9eff !important;
    font-weight: 600;
  }

  :deep(.van-tab__text) {
    position: relative;
    
    &::after {
      content: '';
      position: absolute;
      bottom: -4px;
      left: 50%;
      width: 0;
      height: 2px;
      background: #4a9eff;
      transition: all 0.3s ease;
      transform: translateX(-50%);
    }
  }

  :deep(.van-tab--active .van-tab__text::after) {
    width: 80%;
  }
}
:deep(.van-tabs__line) {
  background: #4a9eff;
  height: 3px;
  border-radius: 2px;
}
:deep(.van-cell::after) {
  border: none !important;
}
:deep(.van-pull-refresh) {
  background: #121212;
}
:deep(.van-list__loading, .van-list__finished-text) {
  color: rgba(255, 255, 255, 0.6);
  padding: 16px 0;
}
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}
::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.2);
  border-radius: 3px;
  
  &:hover {
    background: rgba(255, 255, 255, 0.3);
  }
}
::-webkit-scrollbar-track {
  background: #121212;
}
</style>
