<template>
  <div class="financial-banner">
    <div class="banner-content">
      <div class="title">
        <div>{{ _t18('financial_page.quality_financial') }}  {{ _t18('financial_page.high_returns') }}</div>
        <div></div>
      </div>
      <div class="subtitle">
        {{ _t18('financial_page.safe_investment') }}
      </div>
    </div>
  </div>
</template>

<script setup>
</script>

<style lang="scss" scoped>
.financial-banner {
  width: 96%;
  margin: 0 auto;
  height: 160px;
  background: url('@/assets/icons/Facilitated_banner-142711ca.png') no-repeat;
  background-size: cover;
  padding: 24px;
  box-sizing: border-box;

  .banner-content {
    .title {
      font-size: 24px;
      font-weight: 600;
      color: #fff;
      line-height: 1.4;
    }

    .subtitle {
      font-size: 14px;
      color: rgba(255, 255, 255, 0.8);
      margin-top: 8px;
    }
  }
}
</style> 