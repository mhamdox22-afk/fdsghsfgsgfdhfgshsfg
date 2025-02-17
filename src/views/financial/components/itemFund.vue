<template>
  <div class="item">
    <div class="top">
      <div class="fw-bold">{{ itemObj.title }}</div>
      <!-- 普通和增值没有 -->
      <div class="grade fw-num" v-if="itemObj.classify !== '0' && itemObj.classify !== '2'">
        VIP{{ itemObj.level }}
      </div>
    </div>
    <div class="dataList">
      <div class="left">
        <div class="leftTop">
          <div class="tip_left">
            <div v-if="!itemObj.icon" class="leftTopImg"></div>
            <image-load :filePath="itemObj.icon" v-if="itemObj.icon" class="itemImg" />
            <div class="fw-num">{{ itemObj.avgRate }} %</div>
          </div>
          <img src="@/assets/financial/shangshen.png" class="rightImg" />
        </div>
        <div class="leftBottom">
          <!-- 起投金额 -->
          <div class="centerNum">
            <div>
              <svg-load name="lingxing" class="starImg"></svg-load>
              <div>{{ _t18(`starting-amount`) }}：</div>
            </div>
            <div class="money fw-num">
              {{ itemObj.limitMin }} {{ itemObj.coin ? itemObj.coin.toUpperCase() : '' }}
            </div>
          </div>
          <!-- 限投金额： -->
          <div class="centerNum">
            <div>
              <svg-load name="lingxing" class="starImg"></svg-load>
              <div>{{ _t18(`Investment_Limit`) }}：</div>
            </div>
            <div class="money fw-num">
              {{ itemObj.limitMax }} {{ itemObj.coin ? itemObj.coin.toUpperCase() : '' }}
            </div>
          </div>
          <!-- 锁仓天数： -->
          <div class="centerNum">
            <div>
              <svg-load name="lingxing" class="starImg"></svg-load>
              <div>{{ _t18(`Lock-up_days`) }}：</div>
            </div>
            <div class="money fw-num">{{ itemObj.days }}</div>
          </div>
        </div>
      </div>
      <!-- <div class="right">
        <img src="@/assets/financial/shangshen.png" alt="" class="rightImg" />
      </div> -->
    </div>
    <div class="line">
      <div class="hight" :style="{ width: `${itemObj.process}%` }"></div>
    </div>
    <div class="footer">
      <!-- 项目进度：   -->
      <div class="footerL">
        <div>{{ _t18(`project_progress`) }}：</div>
        <div class="num fw-num">{{ itemObj.process }}%</div>
      </div>
      <!-- 立即参投  -->
      <div class="footerR" @click="$router.push(`/financialDetails/${itemObj.id}`)">
        {{ _t18(`vote_now`) }}
      </div>
    </div>
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
  background: #2a2a2a;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 15px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
  color: #ffffff;
  
  &:active {
    transform: scale(0.98);
  }

  // 悬浮效果
  &:hover {
    box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
    transform: translateY(-2px);
  }

  // 添加霓虹灯效果的边框
  position: relative;
  &::after {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    border-radius: 12px;
    border: 1px solid rgba(255, 255, 255, 0.1);
    pointer-events: none;
  }
}

// 文字样式
.title {
  color: #ffffff;
  font-weight: 500;
}

.desc {
  color: rgba(255, 255, 255, 0.7);
}

.value {
  color: #00ff9d; // 使用霓虹绿色突出显示数值
  font-weight: bold;
}

// 添加渐变背景
.gradient-bg {
  background: linear-gradient(135deg, #2a2a2a 0%, #1a1a1a 100%);
}

.item {
  background: linear-gradient(145deg, #1a1a1a, #2a2a2a);
  border-radius: 15px;
  margin-bottom: 20px;
  padding: 20px;
  box-sizing: border-box;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.3);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  
  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 12px 25px rgba(0, 0, 0, 0.4);
  }

  // 光效动画
  &::after {
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

  &:hover::after {
    left: 100%;
  }

  .top {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;

    .fw-bold {
      color: #ffffff;
      font-size: 16px;
      font-weight: 600;
    }

    .grade {
      padding: 4px 12px;
      background: linear-gradient(90deg, #2c7a7b, #234e52);
      border-radius: 15px;
      font-size: 12px;
      color: #ffffff;
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }

  .dataList {
    .left {
      .leftTop {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;

        .tip_left {
          display: flex;
          align-items: center;
          gap: 15px;

          .leftTopImg, .itemImg {
            width: 50px;
            height: 50px;
            border-radius: 10px;
            background: rgba(255, 255, 255, 0.1);
            transition: transform 0.3s ease;
            display: flex;
            align-items: center;
            justify-content: center;
            border: 1px solid rgba(255, 255, 255, 0.1);

            &:hover {
              transform: scale(1.05);
            }
          }

          .fw-num {
            color: #48bb78;
            font-size: 36px;
            font-weight: 600;
          }
        }

        .rightImg {
          height: 50px;
          filter: brightness(0.9);
        }
      }

      .leftBottom {
        background: rgba(255, 255, 255, 0.05);
        border-radius: 12px;
        padding: 15px;
        margin: 15px 0;
        border: 1px solid rgba(255, 255, 255, 0.1);

        .centerNum {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 12px;
          
          & > div {
            display: flex;
            align-items: center;
            color: rgba(255, 255, 255, 0.7);
            font-size: 12px;

            .starImg {
              width: 7px;
              height: 7px;
              margin-right: 5px;
              filter: brightness(0.9);
            }
          }

          .money {
            color: #ffffff;
            font-weight: 500;
          }
        }
      }
    }
  }

  .line {
    height: 6px;
    background: rgba(255, 255, 255, 0.1);
    border-radius: 3px;
    margin: 15px 0;
    overflow: hidden;
    
    .hight {
      height: 100%;
      background: linear-gradient(90deg, #2c7a7b, #48bb78);
      border-radius: 3px;
      transition: width 0.3s ease-in-out;
    }
  }

  .footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 15px;

    .footerL {
      display: flex;
      align-items: center;
      color: rgba(255, 255, 255, 0.7);
      font-size: 14px;

      .num {
        color: #ffffff;
        margin-left: 5px;
        font-weight: 500;
      }
    }

    .footerR {
      background: linear-gradient(90deg, #2c7a7b, #48bb78);
      padding: 8px 20px;
      border-radius: 20px;
      color: #ffffff;
      font-size: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      transition: all 0.3s ease;
      box-shadow: 0 4px 12px rgba(44, 122, 123, 0.3);

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 15px rgba(44, 122, 123, 0.4);
      }

      &:active {
        transform: translateY(0);
      }
    }
  }
}
</style>
