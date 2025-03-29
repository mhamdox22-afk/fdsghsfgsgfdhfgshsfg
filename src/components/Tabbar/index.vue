<!-- 底部 -->
<script setup>
import { useMainStore } from '@/store/index.js'
import { _toView } from '../../utils/public'
import { useRouter } from 'vue-router'
import { computed } from 'vue'
const router = useRouter()

const mainStore = useMainStore()
let currentTab = ref({})

const getTabbarList = computed(() => {
  return mainStore.getTabbarList.filter((elem) => elem.isOpen)
})

watch(
  router.currentRoute,
  (newVal) => {
    // currentTab.value = tabbarList.find((elem) => elem.pathName == newVal.name)
    currentTab.value = getTabbarList.value.find((elem) => elem.key == newVal.name.toLowerCase())
  },
  { immediate: true }
)
</script>

<template>
  <div>
    <!-- Placeholder div to prevent content from being hidden behind the fixed tabbar -->
    <div class="tab-bar-placeholder" v-if="currentTab.linkUrl !== '/trade'"></div>
    
    <div class="tab-bar-box" v-if="currentTab.linkUrl !== '/trade'">
      <div class="tab-bar">
        <template v-for="item in getTabbarList" :key="item.key">
          <div class="item" @click="_toView(item.linkUrl)">
            <div class="itemTop">
              <image-load
                :filePath="item.checkedImgUrl"
                v-show="currentTab.key == item.key"
                class="tabbarImg"
              />
              <image-load
                :filePath="item.imgUrl"
                v-show="currentTab.key != item.key"
                v-cloak
                class="tabbarImg"
              />
            </div>
            <div :class="{ name: true, active: currentTab.key == item.key }" class="text-ellipsis2">
              {{ _t18(item.key, ['paxpay', 'bitmake', 'latcoin']) }}
            </div>
          </div>
        </template>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.tab-bar-placeholder {
  height: 80px; /* Height should match the tabbar height + padding */
  width: 100%;
}

.tab-bar-box {
  padding-bottom: constant(safe-area-inset-bottom); /* iOS 11.0 */
  padding-bottom: 10px;
  position: fixed;
  z-index: 9;
  background-color: #000;
  bottom: 0;
  left: 0;
  right: 0;
  margin: auto;
  width: var(--ex-max-width);
  border-top: 1px solid #646363;
  .tab-bar {
    background-color: #000;
    display: flex;
    align-items: center;
    justify-content: center;

    .item {
      min-height: 70px;
      width: 100%;
      height: 100%;
      display: flex;
      flex-direction: column;
      align-items: center;
      // justify-content: center;
      .icon {
        font-size: 20px;
      }
      .name {
        display: flex;
        align-items: flex-start;
        font-size: 12px;
        text-align: center;
        color: #fff;
        &.active {
          color: #fff;
          font-weight: bold;
        }
      }
      .itemTop {
        padding: 15px 0 5px;
      }
    }
  }
}
.tabbarImg {
  width: 20px;
  height: 20px;
}
</style>
