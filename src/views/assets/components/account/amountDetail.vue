<template>
  <div class="assetDetails">
    <div v-for="(item, index) in assetDetails" :key="index">
      <div class="title" v-if="item.icon == 'usdt'">
        <!-- 资产明细 -->
        <div class="left fw-bold">{{ _t18('asset_detail', ['ebc']) }}</div>
      </div>
      <div class="details">
        <div class="top">
          <svg-load v-if="item.title == 'USDT'" :name="item.icon" class="currencyIcon"></svg-load>
          <image-load :filePath="item.loge" v-else />
          <p class="fw-num">{{ item.title }}</p>
        </div>
        <div class="bottom">
          <div>
            <!-- 可用 -->
            <p class="til">{{ _t18('asset_available') }}</p>
            <p class="num fw-num">
              {{ amountFormat(item.keyong) }}
            </p>
          </div>
          <div>
            <!-- 占用 冻结-->
            <p class="til" v-if="['rxce'].includes(_getConfig('_APP_ENV'))">
              {{ _t18('asset_cold') }}
            </p>
            <p class="til" v-else>{{ _t18('asset_occupation', ['bitmake', 'ebc']) }}</p>
            <p class="num fw-num">
              {{ amountFormat(item.zhanyong, 4) }}
            </p>
          </div>
          <div>
            <!-- 折合 -->
            <p class="til">{{ _t18('asset_equivalent', ['bitmake', 'ebc']) }}(USDT)</p>
            <p class="num fw-num">
              {{ amountFormat(item.zhehe) }}
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { _hide, _t18, _numberWithCommas } from '@/utils/public.js'
import { priceFormat } from '@/utils/decimal.js'
const props = defineProps({
  assetDetails: {
    type: Object
  },
  showNum: {
    type: Boolean
  }
})

/**
 * 金额格式化
 * @param {*} amount
 * @param {*} hide
 * @param {*} decimal
 */
const amountFormat = (amount, decimal = 0) => {
  let tempVal = priceFormat(amount, decimal)
  if (props.showNum) {
    tempVal = _numberWithCommas(tempVal)
  } else {
    tempVal = _hide(tempVal)
  }
  return tempVal
}
</script>

<style lang="scss" scoped>
.assetDetails {
  padding-bottom: 100px;
  background: #1a1a1a;
  
  & > div {
    padding: 20px 15px;
    margin: 15px;
    border-top: none;
    background: #2a2a2a;
    border-radius: 16px;
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
    transition: all 0.3s ease;
    
    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 12px 20px rgba(0, 0, 0, 0.3);
    }

    .title {
      display: flex;
      justify-content: space-between;
      margin-bottom: 20px;
      
      .left {
        color: #ffffff;
        font-size: 18px;
        font-weight: 600;
        text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      }
    }

    .details {
      .top {
        display: flex;
        align-items: center;
        padding: 10px;
        background: rgba(255, 255, 255, 0.05);
        border-radius: 12px;
        margin-bottom: 15px;
        
        img {
          border-radius: 100%;
          width: 32px;
          height: 32px;
          margin-right: 10px;
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
        }
        
        .currencyIcon {
          font-size: 32px;
          margin-right: 10px;
          filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.2));
        }
        
        p {
          font-size: 16px;
          color: #ffffff;
          font-weight: 500;
        }
      }

      .bottom {
        display: flex;
        background: rgba(255, 255, 255, 0.02);
        border-radius: 12px;
        padding: 15px;
        
        & > div {
          flex: 1;
          text-align: center;
          position: relative;
          
          &:not(:last-child)::after {
            content: '';
            position: absolute;
            right: 0;
            top: 20%;
            height: 60%;
            width: 1px;
            background: rgba(255, 255, 255, 0.1);
          }

          .til {
            padding: 10px 0;
            color: rgba(255, 255, 255, 0.6);
            font-size: 14px;
            transition: color 0.3s ease;
          }

          .num {
            color: #ffffff;
            font-size: 16px;
            font-weight: 600;
            text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            
            @keyframes numberFadeIn {
              from {
                opacity: 0;
                transform: translateY(10px);
              }
              to {
                opacity: 1;
                transform: translateY(0);
              }
            }
            
            animation: numberFadeIn 0.5s ease-out;
          }

          &:first-child {
            text-align: start;
          }
          
          &:last-child {
            text-align: end;
          }

          &:hover {
            .til {
              color: rgba(255, 255, 255, 0.8);
            }
          }
        }
      }
    }
  }
}

// 添加深色主题适配
:root[theme='dark'] {
  .assetDetails {
    background: #121212;
    
    & > div {
      background: #1e1e1e;
    }
  }
}

// 添加响应式设计
@media screen and (max-width: 375px) {
  .assetDetails {
    & > div {
      margin: 10px;
      padding: 15px 12px;
      
      .details {
        .bottom {
          flex-direction: column;
          
          & > div {
            padding: 10px 0;
            text-align: left !important;
            
            &::after {
              display: none;
            }
          }
        }
      }
    }
  }
}
</style>
