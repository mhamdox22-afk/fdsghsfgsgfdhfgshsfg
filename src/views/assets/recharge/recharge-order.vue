<template>
  <!-- 充值记录 -->
  <HeaderBar :currentName="_t18('recharge_order')"></HeaderBar>
  <Tab :tabList="tabList" :active="curIndex" @change="changeIndex">
    <template #tabContent>
      <!-- 下拉刷新 -->
      <van-pull-refresh
        v-model="refreshing"
        @refresh="onRefresh"
        :loading-text="_t18('loading')"
        :loosing-text="_t18('release_refresh')"
      >
        <!-- 加载中动画 -->
        <van-loading v-if="showLoading" />
        <!-- 数据列表 -->
        <div v-else>
          <!-- 没有更多数据了 no_more_data 加载中  loading-->
          <van-list
            v-if="tabContentList.length > 0"
            v-model:loading="loading"
            :finished="finished"
            :finished-text="_t18(`no_more_data`)"
            :loading-text="_t18(`loading`)"
            @load="onLoad"
          >
            <van-cell v-for="(item, index) in tabContentList" :key="index">
              <OrderList :data="item"></OrderList>
            </van-cell>
          </van-list>
          <!-- 数据为空 -->
          <Nodata v-else />
        </div>
      </van-pull-refresh>
    </template>
  </Tab>
</template>

<script setup>
import { getRechargeList } from '@/api/account'
import Tab from '@/components/Tab/index.vue'
import OrderList from '../components/orderList.vue'
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
  // list = ['全部', '审核', '成功', '失败']
  list = [
    _t18('recharge_tab_all', ['aams']),
    _t18('recharge_tab_wait'),
    _t18('recharge_tab_success'),
    _t18('recharge_tab_error')
  ]
  return list
})
/**
 * 充值记录查询
 * pageNum页码
 * pageSize每页条数
 * total总条数
 */
const pageNum = ref(1)
const pageSize = ref(2)
const total = ref(0)
const tabContentList = ref([]) //数据列表
const getList = () => {
  let params = `pageNum=${pageNum.value}&pageSize=${pageSize.value}`
  // 排除全部时不传参数
  if (curIndex.value != '0') {
    params = `status=${curIndex.value - 1}&pageNum=${pageNum.value}&pageSize=${pageSize.value}`
  }
  getRechargeList(params).then((res) => {
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
      // 数据加载完成
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
  // getList()
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
</script>

<style lang="scss" scoped>
.content {
  background: #1a1a1a !important;
  border-bottom: 5px solid rgba(255, 255, 255, 0.1);
  padding: 20px 15px;
  transition: all 0.3s ease;
}
.van-loading {
  text-align: center;
  padding: 30px;
}
.van-list {
  min-height: calc(100vh - 60px - 44px);
}
:deep(.van-cell) {
  background: #242424 !important;
  margin: 10px 15px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transform: translateY(0);
  transition: all 0.3s ease;
  
  &:active {
    transform: translateY(2px);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  }
  
  &::after {
    display: none !important; // 移除原有边框
  }
}
:deep(.van-pull-refresh) {
  background: #1a1a1a;
}
:deep(.van-tabs) {
  background: #242424 !important;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}
:deep(.van-tabs__nav) {
  background: #242424 !important;
  padding: 0 15px;
}
:deep(.van-tab) {
  color: rgba(255, 255, 255, 0.5) !important;
  background: #242424 !important;
  position: relative;
  transition: all 0.3s ease;
  font-weight: 500;
  padding: 0 8px;
  margin: 0 8px;
  
  &::after {
    content: '';
    position: absolute;
    bottom: 2px;
    left: 0;
    width: 100%;
    height: 3px;
    background: linear-gradient(90deg, #007aff, #00c6ff);
    border-radius: 4px;
    opacity: 0;
    transform: scaleX(0.6);
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  }
}
:deep(.van-tab--active) {
  color: #fff !important;
  font-weight: 600;
  
  &::after {
    opacity: 1;
    transform: scaleX(1);
  }
}
// 移除原有的 tab 线条
:deep(.van-tabs__line) {
  display: none;
}
// 添加列表项动画
:deep(.van-cell-group) {
  .van-cell {
    animation: slideIn 0.3s ease-out forwards;
    opacity: 0;
    
    @for $i from 1 through 10 {
      &:nth-child(#{$i}) {
        animation-delay: $i * 0.1s;
      }
    }
  }
}
@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
// 添加加载动画样式
:deep(.van-loading) {
  .van-loading__spinner {
    color: #007aff;
  }
}
// 自定义滚动条
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}
::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.2);
  border-radius: 3px;
}
::-webkit-scrollbar-track {
  background: #1a1a1a;
}
// 下拉刷新样式优化
:deep(.van-pull-refresh__head) {
  color: rgba(255, 255, 255, 0.8);
}
// 列表底部加载更多样式
:deep(.van-list__finished-text),
:deep(.van-list__loading-text) {
  color: rgba(255, 255, 255, 0.5);
  font-size: 14px;
  padding: 16px 0;
}
</style>
