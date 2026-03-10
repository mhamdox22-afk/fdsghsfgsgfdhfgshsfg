<!-- 平台Logo -->
<script setup name="img-load">
import { computed } from 'vue'
import { _toView } from '@/utils/public.js'
import { useMainStore } from '@/store/index.js'
import logoImage from '@/../public/resource/images/logo6.png'
const mainStroe = useMainStore()

/**
 * Logo
 */
const path = computed(() => {
  // 使用导入的图片
  return logoImage
})
</script>
<template>
  <img :src="path" loading="lazy" class="logo" @click="_toView('/home')" />
</template>
<style lang="scss" scoped>
.logo {
  height: 33px;
  position: relative;
  left: -1px;
}
</style>
