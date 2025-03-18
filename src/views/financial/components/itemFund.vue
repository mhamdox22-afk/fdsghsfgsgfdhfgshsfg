<template>
  <div class="fund-item">
    <div class="header">
      <div class="title">{{ itemObj.title }}</div>
      <img src="@/assets/icons/goods_card_graph-b3adc3cb.png" class="coin-stack" />
    </div>
    
    <div class="rate-section">
      <div class="coin-icon">
        <div v-if="!itemObj.icon" class="default-icon"></div>
        <image-load :filePath="itemObj.icon" v-if="itemObj.icon" class="icon-img" />
      </div>
      <div class="rate-value fw-num">{{ itemObj.avgRate }} %</div>
    </div>

    <div class="info-list">
      <div class="info-item">
        <div class="label">
          <svg-load name="lingxing" class="star-icon"></svg-load>
          {{ _t18(`starting-amount`) }}
        </div>
        <div class="value fw-num">
          {{ itemObj.limitMin }} {{ itemObj.coin ? itemObj.coin.toUpperCase() : '' }}
        </div>
      </div>
      
      <div class="info-item">
        <div class="label">
          <svg-load name="lingxing" class="star-icon"></svg-load>
          {{ _t18(`Investment_Limit`) }}
        </div>
        <div class="value fw-num">
          {{ itemObj.limitMax }} {{ itemObj.coin ? itemObj.coin.toUpperCase() : '' }}
        </div>
      </div>

      <div class="info-item">
        <div class="label">
          <svg-load name="lingxing" class="star-icon"></svg-load>
          {{ _t18(`Lock-up_days`) }}
        </div>
        <div class="value fw-num">{{ itemObj.days }}</div>
      </div>
    </div>

    <div class="progress-bar">
      <div class="progress" :style="{ width: `${itemObj.process}%` }"></div>
    </div>

    <div class="progress-text">
      {{ _t18(`project_progress`) }}：
      <span class="progress-value fw-num">{{ itemObj.process }}%</span>
    </div>

    <button class="invest-btn" @click="$router.push(`/financialDetails/${itemObj.id}`)">
      {{ _t18(`vote_now`) }}
    </button>
  </div>
</template>

<script setup>
import { _t18 } from '@/utils/public'
const props = defineProps({
  itemObj: {
    type: Object,
    default: {}
  }
})
</script>

<style lang="scss" scoped>
.fund-item {
  background: #1A1B1E;
  border-radius: 12px;
  padding: 15px;
  width: 96%;
  margin: 0 auto;
  margin-bottom: 20px;
  
  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12px;

    .title {
      font-size: 18px;
      color: #fff;
      font-weight: 500;
    }

    .coin-stack {
      width: 50px;
      height: 50px;
    }
  }

  .rate-section {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 12px;

    .coin-icon {
      .default-icon,
      .icon-img {
        width: 24px;
        height: 24px;
        border-radius: 6px;
        background: rgba(255,255,255,0.1);
      }
    }

    .rate-value {
      font-size: 28px;
      color: #fff;
      font-weight: 500;
    }
  }

  .info-list {
    background: rgba(255,255,255,0.03);
    border-radius: 10px;
    padding: 12px;
    margin-bottom: 12px;

    .info-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 8px;
      
      &:last-child {
        margin-bottom: 0;
      }

      .label {
        display: flex;
        align-items: center;
        color: rgba(255,255,255,0.6);
        font-size: 13px;

        .star-icon {
          width: 5px;
          height: 5px;
          margin-right: 6px;
          opacity: 0.6;
        }
      }

      .value {
        color: #fff;
        font-size: 13px;
        font-weight: 500;
      }
    }
  }

  .progress-bar {
    height: 4px;
    background: rgba(255,255,255,0.05);
    border-radius: 2px;
    margin: 12px 0 8px;
    overflow: hidden;

    .progress {
      height: 100%;
      background: #2EAD71;
      border-radius: 2px;
      transition: width 0.3s ease;
    }
  }

  .progress-text {
    color: rgba(255,255,255,0.6);
    font-size: 13px;
    margin-bottom: 12px;

    .progress-value {
      color: #fff;
      margin-left: 4px;
    }
  }

  .invest-btn {
    width: 100%;
    background: linear-gradient(90deg, #7F3AFF, #1D9BF0);
    color: #fff;
    border: none;
    padding: 10px;
    border-radius: 20px;
    font-size: 14px;
    font-weight: 500;
    cursor: pointer;
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(127,58,255,0.3);
    }

    &:active {
      transform: translateY(0);
    }
  }
}
</style>
