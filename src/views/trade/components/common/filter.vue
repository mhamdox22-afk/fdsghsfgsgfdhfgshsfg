<!-- 序列 -->
<template>
  <div>
    <div class="mainBgc">
      <div class="main_collect">
        <div class="main_header" v-if="isOptional">
          <!-- 现货 -->
          <div class="left">{{ _t18(`quote_spot`) }}</div>
          <div class="right" @click="$router.push('/editOptional')">
            <svg-load name="bianji"></svg-load>
          </div>
        </div>
        <div class="main">
          <div class="mainItem" @click="toDealSort">
            <div>{{ _t18(`home_currencyName`, ['latcoin']) }}</div>
            <div class="arrows">
              <svg-load v-if="arrowList.firstIcon === 0" name="moren" class="itemImg"></svg-load>
              <svg-load v-if="arrowList.firstIcon === 1" name="gao" class="itemImg"></svg-load>
              <svg-load v-if="arrowList.firstIcon === 2" name="di" class="itemImg"></svg-load>
            </div>
          </div>
          <div class="mainItem" @click="toUpSort">
            <div>{{ _t18(`home_newPrice`, ['latcoin']) }}</div>
            <div class="arrows">
              <svg-load v-if="arrowList.secondIcon === 0" name="moren" class="itemImg"></svg-load>
              <svg-load v-if="arrowList.secondIcon === 1" name="gao" class="itemImg"></svg-load>
              <svg-load v-if="arrowList.secondIcon === 2" name="di" class="itemImg"></svg-load>
            </div>
          </div>
          <div class="mainItem" @click="toRafSort">
            <div>{{ _t18(`home_upDown`, ['latcoin']) }}</div>
            <div class="arrows">
              <svg-load v-if="arrowList.thirdIcon === 0" name="moren" class="itemImg"></svg-load>
              <svg-load v-if="arrowList.thirdIcon === 1" name="gao" class="itemImg"></svg-load>
              <svg-load v-if="arrowList.thirdIcon === 2" name="di" class="itemImg"></svg-load>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {
  letterSmallToLarge,
  letterLargeToSmall,
  LatestpriceLargeToSmall,
  LatestpriceSmallToLarge
} from '@/utils/filters'
import { useTradeStore } from '@/store/trade'
const tradeStore = useTradeStore()
const props = defineProps({
  isOptional: {
    type: Boolean,
    default: false
  },
  list: {
    type: Array,
    default: []
  }
})
const listResult = []
const emits = defineEmits(['toSort'])
// 筛选
let arrowList = reactive({
  firstIcon: 0,
  secondIcon: 0,
  thirdIcon: 0
})

// 切换名称
const toDealSort = () => {
  arrowList.secondIcon = 0
  arrowList.thirdIcon = 0
  if (arrowList.firstIcon === 0) {
    arrowList.firstIcon = 1
    listResult.value = letterSmallToLarge(props.list, 'coin')
  } else if (arrowList.firstIcon === 1) {
    arrowList.firstIcon = 2
    listResult.value = letterLargeToSmall(props.list, 'coin')
  } else if (arrowList.firstIcon === 2) {
    arrowList.firstIcon = 0
    listResult.value = props.list
  }
  emits('toSort', listResult.value)
}
// 切换最新价
const toUpSort = () => {
  arrowList.firstIcon = 0
  arrowList.thirdIcon = 0
  if (arrowList.secondIcon === 0) {
    arrowList.secondIcon = 1
    listResult.value = LatestpriceLargeToSmall(props.list,tradeStore.allCoinPriceInfo, 'close')
  } else if (arrowList.secondIcon === 1) {
    arrowList.secondIcon = 2
    listResult.value = LatestpriceSmallToLarge(props.list,tradeStore.allCoinPriceInfo, 'close')
  } else if (arrowList.secondIcon === 2) {
    arrowList.secondIcon = 0
    listResult.value = props.list
  }
  emits('toSort', listResult.value)
}
// 切换涨跌
const toRafSort = () => {
  arrowList.firstIcon = 0
  arrowList.secondIcon = 0
  if (arrowList.thirdIcon === 0) {
    arrowList.thirdIcon = 1
    listResult.value = LatestpriceLargeToSmall(props.list,tradeStore.allCoinPriceInfo, 'change',1)
  } else if (arrowList.thirdIcon === 1) {
    arrowList.thirdIcon = 2
    listResult.value = LatestpriceSmallToLarge(props.list,tradeStore.allCoinPriceInfo, 'change',1)
  } else if (arrowList.thirdIcon === 2) {
    arrowList.thirdIcon = 0
    listResult.value = props.list
  }
  emits('toSort', listResult.value)
}
</script>

<style lang="scss" scoped>
.mainBgc {
  background: #1a1a1a; // 深色背景
  min-height: 100px;
  transition: all 0.3s ease;

  .main {
    background-color: #232323;
    border-radius: 20px 20px 0px 0;
    padding: 20px 15px 10px;
    font-size: 12px;
    color: #8c8c8c;
    display: flex;
    justify-content: space-between;
    box-shadow: 0 -8px 20px rgba(0, 0, 0, 0.2);
    backdrop-filter: blur(10px);
    transform: translateY(0);
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 -10px 25px rgba(0, 0, 0, 0.25);
    }

    .mainItem {
      display: flex;
      align-items: center;
      padding: 8px 12px;
      border-radius: 12px;
      cursor: pointer;
      transition: all 0.2s ease;
      
      &:hover {
        background: rgba(255, 255, 255, 0.05);
      }

      .arrows {
        display: flex;
        flex-direction: column;
        margin-left: 5px;

        .itemImg {
          width: 6px;
          height: 8px;
          opacity: 0.7;
          transition: all 0.2s ease;

          &:hover {
            opacity: 1;
            transform: scale(1.1);
          }
        }
      }
    }

    .mainItem:nth-child(2) {
      flex: 1;
      text-align: right;
      justify-content: right;
    }

    .mainItem:nth-child(3) {
      margin-left: 20px;
      max-width: 80px;
      min-width: 80px;
      text-align: right;
      justify-content: flex-end;
    }
  }

  .main_collect {
    background-color: #232323;
    padding: 15px;
    font-size: 12px;
    color: #8c8c8c;
    border-radius: 20px 20px 0 0;
    box-shadow: 0 -4px 15px rgba(0, 0, 0, 0.15);

    .main_header {
      display: flex;
      justify-content: space-between;
      font-size: 14px;
      color: #ffffff;
      padding: 10px 15px 15px;
      border-bottom: 1px solid rgba(255, 255, 255, 0.05);
      margin-bottom: 10px;

      .right {
        cursor: pointer;
        opacity: 0.8;
        transition: all 0.2s ease;

        &:hover {
          opacity: 1;
          transform: scale(1.05);
        }
      }
    }

    .main {
      display: flex;
      justify-content: space-between;
      padding: 10px 0;

      .mainItem {
        display: flex;
        align-items: center;
        padding: 8px 12px;
        border-radius: 12px;
        transition: all 0.2s ease;
        
        &:active {
          background: rgba(255, 255, 255, 0.08);
          transform: scale(0.98);
        }

        .arrows {
          display: flex;
          flex-direction: column;
          margin-left: 5px;

          .itemImg {
            width: 6px;
            height: 8px;
            opacity: 0.7;
            transition: transform 0.2s ease;

            &:hover {
              opacity: 1;
            }
          }
        }
      }
    }
  }
}

// 添加动画类
@keyframes slideIn {
  from {
    transform: translateY(20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

.mainBgc {
  animation: slideIn 0.3s ease-out;
}
</style>
