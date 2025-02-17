<script setup>
import { _t18 } from '@/utils/public'
let props = defineProps({
  cardData: {
    type: Object
  }
})
const dataValue = ref(props.cardData)
</script>
<template>
  <div class="box">
    <div class="top">
      <div class="top_left">
        <svg-load name="usdt" class="usdt"></svg-load>
        <div class="title fw-bold">{{ dataValue.title }}</div>
      </div>
      <div class="top_right">{{ _t18('pledge_Buy', ['aams']) }}</div>
      <!-- <div class="top_right">
        可购：<span class="ff-num">{{ `${dataValue.kegou}/${dataValue.sum}` }}</span>
      </div> -->
    </div>
    <div class="content">
      <div class="left">
        <div class="content1">
          <!-- 限额 -->
          <p>{{ _t18('pledge_quota') }}</p>
          <span class="fw-num">{{ dataValue.limitMin }}~{{ dataValue.limitMax }}</span>
        </div>
        <div class="content2">
          <!-- 天数 -->
          <p>{{ _t18('pledge_number_days') }}</p>
          <span class="fw-num">{{ dataValue.days }}</span>
        </div>
        <div class="content3">
          <!-- 可购 -->
          <p>{{ _t18('pledge_available_purchase', ['aams']) }}</p>
          <span class="fw-num" v-if="['dev'].includes(_getConfig('_APP_ENV'))">{{ `${dataValue.buyPurchase}/${dataValue.timeLimit}` }}</span>
          <!-- buyPurchase -->
          <span class="fw-num" v-else>{{ `${dataValue.timeLimit}` }}</span>
        </div>
        <div class="content3">
          <!-- 收益率 -->
          <p>{{ _t18('pledge_rate_return') }}</p>
          <span class="fw-num rate">{{ dataValue.minOdds }}%~{{ dataValue.maxOdds }}%</span>
        </div>
      </div>
      <div class="right">
        <image-load filePath="zhiyacard.png" name="zhiyacard" class="zhiyacard"></image-load>
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.box {
  margin-top: 15px;
  cursor: pointer;
  background: linear-gradient(145deg, #1a1a1a, #2d2d2d);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  font-size: 14px;
  color: #9ca3af;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
  overflow: hidden;
  position: relative;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(
      90deg,
      transparent,
      rgba(255, 255, 255, 0.05),
      transparent
    );
    transition: 0.5s;
  }

  &:active {
    transform: scale(0.98);
  }

  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 15px 30px rgba(0, 0, 0, 0.3);

    &::before {
      left: 100%;
    }
  }

  .top {
    display: flex;
    align-items: center;
    justify-content: space-between;
    border-bottom: 1px solid rgba(255, 255, 255, 0.1);
    padding: 20px 15px;
    background: rgba(255, 255, 255, 0.03);

    .top_left {
      display: flex;
      align-items: center;
      
      .title {
        font-weight: bold;
        color: #ffffff;
        font-size: 16px;
        text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
      }

      .usdt {
        font-size: 24px;
        margin-right: 12px;
        filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.3));
      }
    }

    .top_right {
      min-width: 104px;
      padding: 8px 15px;
      background: rgba(255, 255, 255, 0.1);
      color: #ffffff;
      text-align: center;
      border-radius: 20px;
      backdrop-filter: blur(5px);
      transition: all 0.3s ease;

      &:hover {
        background: rgba(255, 255, 255, 0.15);
      }
    }
  }

  .content {
    display: flex;
    padding: 20px 15px;
    justify-content: space-between;
    background: rgba(0, 0, 0, 0.2);

    .left {
      display: flex;
      flex-direction: column;
      gap: 15px;

      & > div {
        flex: 1;
        display: flex;
        align-items: center;
        color: #9ca3af;
        font-size: 13px;

        p {
          min-width: 36px;
        }

        span {
          margin-left: 20px;
          color: #ffffff;
          font-size: 18px;
          font-weight: 500;
          text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
        }

        span.rate {
          color: #10b981;
          background: linear-gradient(90deg, #10b981, #059669);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
        }
      }
    }

    .right {
      .zhiyacard {
        font-size: 120px;
        filter: drop-shadow(0 4px 6px rgba(0, 0, 0, 0.3));
        transition: transform 0.3s ease;

        &:hover {
          transform: rotate(5deg);
        }
      }
    }
  }
}

// 添加暗色主题适配
@media (prefers-color-scheme: dark) {
  .box {
    background: linear-gradient(145deg, #1a1a1a, #2d2d2d);
  }
}
</style>
