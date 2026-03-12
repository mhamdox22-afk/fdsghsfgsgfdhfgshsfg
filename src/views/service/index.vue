<template>
  <div class="service">
    <div class="custom-header">
      <div class="header-left" @click="goBack">
        <svg-load name="jiantou-z" class="back-icon"></svg-load>
        <span class="back-text">返回交易所</span>
      </div>
    </div>
    <iframe v-if="link" :src="link" frameborder="0" @load="onIframeLoad" @error="onIframeError"></iframe>
  </div>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const link = ref('')

const url = route.query.url || ''
link.value = decodeURIComponent(url)

const goBack = () => {
  router.go(-1)
}

const onIframeLoad = () => {
  console.log('Iframe loaded successfully')
}

const onIframeError = () => {
  console.error('Iframe failed to load')
}
</script>

<style lang="scss" scoped>
.service {
  width: 100%;
  height: 100vh;
  overflow: hidden;
}

.custom-header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  width: 100%;
  max-width: var(--ex-max-width);
  height: 60px;
  padding: 0 15px;
  display: flex;
  align-items: center;
  background-color: rgba(26, 26, 26, 0.95);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(10px);
  z-index: 99;
  margin: 0 auto;
}

.header-left {
  display: flex;
  align-items: center;
  cursor: pointer;
  transition: all 0.3s ease;
  
  &:hover {
    opacity: 0.8;
  }
}

.back-icon {
  width: 16px;
  height: 12px;
  filter: invert(1);
  margin-right: 8px;
}

.back-text {
  font-size: 16px;
  color: #ffffff;
  font-weight: 500;
}

iframe {
  width: 100%;
  height: calc(100vh - 60px);
  margin-top: 60px;
  border: none;
  display: block;
}
</style>
