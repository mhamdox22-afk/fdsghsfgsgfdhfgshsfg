<!-- 首页 -->
<script setup>
import Header from './components/FrontPage/header.vue'
import HeaderDiff from './components/FrontPage/headerDiff.vue'
import Menu from './components/FrontPage/menu.vue'
import Footer from './components/FrontPage/footer.vue'

import StatisticsSection from './components/StatisticsSection.vue'
import PurchaseSteps from './components/PurchaseSteps.vue'
import SecurityFeatures from './components/SecurityFeatures.vue'
import CryptoComposition from './components/CryptoComposition.vue'

import { DIFF_HOME_BANNER } from '@/config/index'
import { useMainStore } from '@/store/index.js'

const mainStore = useMainStore()
onMounted(() => {
  mainStore.getSettingConfig()
})
</script>

<template>
  <HeaderDiff v-if="DIFF_HOME_BANNER.includes(_getConfig('_APP_ENV'))"></HeaderDiff>
  <Header v-else class="header-placeholder"></Header>
  <Menu></Menu>
  <Footer></Footer>

  <!-- <适合所有人的加密货币 /> -->
    <StatisticsSection />
    <!-- 只需几分钟 -->
    <!-- PurchaseSteps -->


    <!-- 未做动画 -->
    <!-- <SecurityFeatures /> -->
    <!-- <CryptoComposition /> -->

</template>

<style scoped>
.header-placeholder {
  min-height: 258px; /* 您可以根据实际需要调整这个高度值 */
  width: 100%;
}
</style>
