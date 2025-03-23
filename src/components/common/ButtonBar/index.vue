<!-- 表单提交按钮 -->
<template>
  <div  style="    background: linear-gradient(225deg, rgb(24, 200, 255) 0%, rgb(147, 63, 254) 100%);">
    {{ btnValue }}
  </div>
</template>

<script setup>
const props = defineProps({
  btnValue: {
    type: String,
    default: '确定'
  },
  btnReverse: {
    type: Boolean,
    default: true
  },
  btnColor: {
    type: String,
    default: '#17ac74'
  }
})
</script>

<style lang="scss" scoped>
div {
  text-align: center;
  border-radius: 3px;
  font-size: 16px;
  padding: 8px 0;
}

.btn1 {
  border: 1px solid var(--ex-btn-boder-color) !important;
  background: var(--ex-btn-background-color) !important;
  color: var(--ex-btn-font-color) !important;
}

.btn2 {
  border: 1px solid var(--ex-btn-boder-color2) !important;
  background: var(--ex-btn-background-color2) !important;
  color: var(--ex-btn-font-color2) !important;
}
</style>
