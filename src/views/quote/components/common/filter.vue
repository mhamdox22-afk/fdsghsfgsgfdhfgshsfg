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
  background: #1a1a1a;
  transition: all 0.3s ease;

  .main {
    background-color: #232323;
    border-radius: 20px 20px 0px 0;
    padding: 20px 15px 10px;
    font-size: 12px;
    color: #a8a8a8;
    display: flex;
    justify-content: space-between;
    box-shadow: 0 -8px 20px rgba(0, 0, 0, 0.2);
    backdrop-filter: blur(10px);
    transform: translateY(0);
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);

    .mainItem {
      display: flex;
      align-items: center;
      padding: 8px 12px;
      border-radius: 12px;
      cursor: pointer;
      transition: all 0.2s ease;

      &:hover {
        background: rgba(255, 255, 255, 0.05);
        transform: translateY(-2px);
      }

      .arrows {
        display: flex;
        flex-direction: column;
        margin-left: 5px;
        opacity: 0.8;
        transition: all 0.2s ease;

        .itemImg {
          width: 6px;
          height: 8px;
          filter: brightness(2);
          transition: transform 0.2s ease;

          &:hover {
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
    border-radius: 20px 20px 0px 0;
    padding: 10px 15px 10px;
    font-size: 12px;
    color: #a8a8a8;
    box-shadow: 0 -4px 20px rgba(0, 0, 0, 0.15);
    animation: slideUp 0.3s ease-out;

    .main_header {
      display: flex;
      justify-content: space-between;
      font-size: 14px;
      color: #ffffff;
      padding: 10px 15px 0;
      letter-spacing: 0.5px;

      .right {
        svg-load {
          opacity: 0.8;
          transition: all 0.2s ease;
          
          &:hover {
            opacity: 1;
            transform: scale(1.1);
          }
        }
      }
    }

    .main {
      display: flex;
      justify-content: space-between;
      margin-top: 10px;
      border-top: 1px solid rgba(255, 255, 255, 0.05);
      padding-top: 15px;
    }
  }
}

@keyframes slideUp {
  from {
    transform: translateY(20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

// 添加暗色主题适配
:root[theme='dark'] {
  .mainBgc {
    background: #121212;
    
    .main, .main_collect {
      background-color: #1e1e1e;
    }
  }
}

// 添加点击波纹效果
.mainItem {
  position: relative;
  overflow: hidden;

  &::after {
    content: '';
    position: absolute;
    top: 50%;
    left: 50%;
    width: 100%;
    height: 100%;
    background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0) 70%);
    transform: translate(-50%, -50%) scale(0);
    opacity: 0;
    transition: all 0.3s ease;
  }

  &:active::after {
    transform: translate(-50%, -50%) scale(2);
    opacity: 1;
  }
}
</style>
