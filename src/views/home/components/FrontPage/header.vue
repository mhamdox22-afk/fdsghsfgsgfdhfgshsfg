<!-- 首页 -->
<template>
  <div>
    <van-popup v-model:show="show" position="left" class="sidebar" @close="closeSideBar">
      <SideBar @closeSideBar="closeSideBar"></SideBar>
    </van-popup>
    <div class="carousel">
      <van-swipe :autoplay="3000" lazy-render :loop="true" :show-indicators="false">
        <van-swipe-item v-for="(item, index) in carouselList" :key="index">
          <img :src="item.imgUrl" alt="" class="carouselItem" @click="linkto(item)" />
        </van-swipe-item>
      </van-swipe>
      <div class="top">
        <div style="display: flex;align-items: center;justify-content: center;">
          <Logo></Logo>
          <span class="gradient-text">ANTBITT</span>
        </div>
        <div style="display: flex;align-items: center;justify-content: center;">
          <img src="@/assets/icons/icon-1.png" class="rightImg" alt="Profile" style="border-radius: 50%;"
            @click="dispatchCustomEvent('event_serviceChange')" />
          <MobileHeader></MobileHeader>
          <img src="@/assets/icons/icon-2.png" class="rightImg" style="width: 30px;height: 30px;" @click="openSideBar"
            alt="User Profile" />
        </div>
      </div>
    </div>

    <!-- 小k线图 -->
    <div class="currentList" style="display: flex;width: 100%;overflow-x: auto;padding: 0 10px;">
      <!-- BTC Section -->
      <div style="background: #1D1E27;
border-radius: 15px;
display: flex;
flex-direction: row;
width: 100%;
margin-right: 10px;
">
        <div class="item centerItem" v-if="dataList[0]" @click="linkTo(dataList[0])">
          <div class="itemTop fw-num">{{ dataList[0].showSymbol }}</div>
          <div :class="[
            _isRFD(
              tradeStore.allCoinPriceInfo[dataList[0].coin]?.open,
              tradeStore.allCoinPriceInfo[dataList[0].coin]?.close
            ),
            'itemFooter fw-num'
          ]">
            {{ tradeStore.allCoinPriceInfo[dataList[0].coin]?.close }}
          </div>
          <div :class="[
            _isRFD(
              tradeStore.allCoinPriceInfo[dataList[0].coin]?.openPrice,
              tradeStore.allCoinPriceInfo[dataList[0].coin]?.close
            ),
            'rfd-sign itemMain fw-num'
          ]">
            {{ tradeStore.allCoinPriceInfo[dataList[0].coin]?.priceChangePercent }}%
          </div>
        </div>
        <Candlestick :coinInfo="coinInfo" type="secondContract"></Candlestick>
      </div>
      <!-- ETH Section -->
      <div style="background: #1D1E27;
border-radius: 15px;
display: flex;
flex-direction: row;
width: 100%;
margin-right: 10px;

">
        <div class="item centerItem" v-if="dataList[1]" @click="linkTo(dataList[1])">
          <div class="itemTop fw-num">{{ dataList[1].showSymbol }}</div>
          <div :class="[
            _isRFD(
              tradeStore.allCoinPriceInfo[dataList[1].coin]?.open,
              tradeStore.allCoinPriceInfo[dataList[1].coin]?.close
            ),
            'itemFooter fw-num'
          ]">
            {{ tradeStore.allCoinPriceInfo[dataList[1].coin]?.close }}
          </div>
          <div :class="[
            _isRFD(
              tradeStore.allCoinPriceInfo[dataList[1].coin]?.openPrice,
              tradeStore.allCoinPriceInfo[dataList[1].coin]?.close
            ),
            'rfd-sign itemMain fw-num'
          ]">
            {{ tradeStore.allCoinPriceInfo[dataList[1].coin]?.priceChangePercent }}%
          </div>
        </div>
        <Candlestick :coinInfo="coinInfo2" type="secondContract2"></Candlestick>
      </div>
      <!-- XAU Section -->
      <div style="background: #1D1E27;
border-radius: 15px;
display: flex;
flex-direction: row;
width: 100%;
">
        <div class="item centerItem" v-if="dataList[2]" @click="linkTo(dataList[2])">
          <div class="itemTop fw-num">{{ dataList[2].showSymbol }}</div>
          <div :class="[
            _isRFD(
              tradeStore.allCoinPriceInfo[dataList[2].coin]?.open,
              tradeStore.allCoinPriceInfo[dataList[2].coin]?.close
            ),
            'itemFooter fw-num'
          ]">
            {{ tradeStore.allCoinPriceInfo[dataList[2].coin]?.close }}
          </div>
          <div :class="[
            _isRFD(
              tradeStore.allCoinPriceInfo[dataList[2].coin]?.openPrice,
              tradeStore.allCoinPriceInfo[dataList[2].coin]?.close
            ),
            'rfd-sign itemMain fw-num'
          ]">
            {{ tradeStore.allCoinPriceInfo[dataList[2].coin]?.priceChangePercent }}%
          </div>
        </div>
        <Candlestick :coinInfo="coinInfo3" type="secondContract3"></Candlestick>
      </div>
    </div>


  </div>
</template>
<script setup>
import Candlestick from '@/views/trade/components/common/simapleCandlestick.vue'

import { useTradeStore } from '@/store/trade/index'
import { useMainStore } from '@/store/index.js'
import { useRouter } from 'vue-router'
import { onMounted, onUnmounted } from 'vue'
import { publiceNotice } from '@/api/common/index'
import { computed } from 'vue'
import SideBar from '@/views/home/sidebar/index.vue'
import MobileHeader from './MobileHeader.vue'
import { dispatchCustomEvent } from '@/utils'
import mainImg1 from '@/assets/icons/main-img-1.jpg'
import mainImg2 from '@/assets/icons/main-img-2.jpg'

const show = ref(false)

const coinInfo = ref({
  "id": 42,
  "symbol": "btcusdt",
  "status": 1,
  "showFlag": 1,
  "coin": "btc",
  "baseCoin": "usdt",
  "market": "binance",
  "sort": 0,
  "searchValue": null,
  "logo": "https://tg-mahalebi.oss-cn-hongkong.aliyuncs.com/waihui/bitcoin.png",
  "showSymbol": "BTC/USDT",
  "type": 2,
  "coinType": 2,
  "amount": 83308.77,
  "open": 82351.29,
  "isCollect": 1,
  "baseCoinUpperCase": "USDT",
  "coinUpperCase": "BTC",
  "symbolUpperCase": "BTCUSDT"
})

const coinInfo2 = ref({
  "id": 43,
  "symbol": "ethusdt",
  "status": 1,
  "showFlag": 1,
  "coin": "eth",
  "baseCoin": "usdt",
  "market": "binance",
  "sort": 1,
  "searchValue": null,
  "logo": "https://tg-mahalebi.oss-cn-hongkong.aliyuncs.com/waihui/ethereum.png",
  "showSymbol": "ETH/USDT",
  "type": 2,
  "coinType": 2,
  "amount": 1926.7,
  "open": 1880.54,
  "isCollect": 1,
  "baseCoinUpperCase": "USDT",
  "coinUpperCase": "ETH",
  "symbolUpperCase": "ETHUSDT"
})


const coinInfo3 = ref({
  "id": 65,
  "symbol": "XAU",
  "status": 1,
  "showFlag": 1,
  "coin": "xau",
  "baseCoin": "XAU",
  "market": "metal",
  "sort": 31,
  "searchValue": null,
  "logo": "https://tg-mahalebi.oss-cn-hongkong.aliyuncs.com/waihui/XAU.png",
  "showSymbol": "XAU/USDT",
  "type": 2,
  "coinType": 3,
  "amount": 2986,
  "open": 2986,
  "isCollect": 2,
  "baseCoinUpperCase": "XAU",
  "coinUpperCase": "XAU",
  "symbolUpperCase": "XAU"
})

const coinInfoArray = [coinInfo, coinInfo2, coinInfo3]

const openSideBar = () => {
  show.value = true
}
const closeSideBar = () => {
  show.value = false
}
const tradeStore = useTradeStore()
const mainStroe = useMainStore()
const $router = useRouter()

const dataList = computed(() => {
  // let tempFilterKey = Object.keys(tradeStore.allCoinPriceInfo)
  //   .filter((key) => tradeStore.allCoinPriceInfo[key]?.priceChangePercent > 0)
  //   .slice(0, 3)
  let tempData = []
  let tempFilterKey = mainStroe.getHomeCoinList.map((item) => {
    if (item.isOpen == 'true') {
      return item.coin
    }
  })
  // let tempData = tradeStore.secondContractCoinList.filter((elem) =>
  //   tempFilterKey.includes(elem.coin)
  // )
  tempFilterKey.forEach((elem) => {
    tradeStore.secondContractCoinList.forEach((elem2) => {
      if (elem2.coin == elem) {
        tempData.push(elem2)
      }
    })
  })
  return tempData
})

const linkTo = (item) => {
  mainStroe.setTradeStatus(Number(0))
  $router.push(`/trade?symbol=${item.coin}`)
}
const carouselList = ref([])
// 轮播图跳转
const linkto = (detail) => {
  if (detail.noticeContent && detail.noticeContent !== '<p><br></p>') {
    $router.push(`/broadcastDetails?id=${detail.noticeId}`)
  } else {
    $router.push(`${detail.detailUrl}`)
  }
}

// Variable to store the interval ID
const priceUpdateInterval = ref(null)

onMounted(async () => {
  try {
    const res = await publiceNotice('ACTIVITY_NOTICE', 'HOME_ACTIVITY ')
    if (res.code === 200) {
      console.log("res.data", res.data);
      const defaultImg = [
        {
          "createBy": null,
          "createTime": "2023-08-08 11:09:56",
          "updateBy": null,
          "updateTime": "2024-08-21 02:18:41",
          "remark": null,
          "noticeId": 58,
          "noticeTitle": "test",
          "noticeType": "活动公告",
          "modelType": "首页轮播活动",
          "noticeContent": "<p><br></p>",
          "commentsNum": 0,
          "cover": null,
          "viewNum": 0,
          "expireTime": null,
          "imgUrl": mainImg1,
          "chainedUrl": "1",
          "detailUrl": "",
          "languageId": "zh",
          "status": "0",
          "sort": null,
          "source": null,
          "key": null,
          "modelKey": null
        },
        {
          "createBy": null,
          "createTime": "2024-08-26 03:14:31",
          "updateBy": null,
          "updateTime": null,
          "remark": null,
          "noticeId": 127,
          "noticeTitle": "中简",
          "noticeType": "活动公告",
          "modelType": "首页轮播活动",
          "noticeContent": null,
          "commentsNum": 0,
          "cover": null,
          "viewNum": 0,
          "expireTime": null,
          "imgUrl": mainImg2,
          "chainedUrl": null,
          "detailUrl": null,
          "languageId": "zh",
          "status": "0",
          "sort": null,
          "source": null,
          "key": null,
          "modelKey": null
        }
      ]

      carouselList.value = defaultImg.filter((item) => {
        return item.status != '1'
      })
    }
  } catch (error) { }

  // Initialize price data
  await tradeStore.getCoinList()

  // Set up interval to continuously update price data (every 5 seconds)
  priceUpdateInterval.value = setInterval(async () => {
    await tradeStore.getCoinList()
  }, 1000)
})

// Cleanup interval when component is unmounted
onUnmounted(() => {
  if (priceUpdateInterval.value) {
    clearInterval(priceUpdateInterval.value)
  }
})
</script>
<style lang="scss" scoped>
.carousel {
  position: relative;
  overflow: hidden;

  .carouselItem {
    height: 200px;
    width: 100%;
    margin-top: 58px;
    object-fit: cover;
    transition: transform 0.3s ease;

    &:hover {
      transform: scale(1.05);
    }
  }

  .top {
    height: 52px;
    background: rgba(28, 28, 35, 0.95);
    backdrop-filter: blur(10px);
    border-radius: 8px;
    padding: 15px 20px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    position: absolute;
    top: 5px;
    left: 50%;
    transform: translateX(-50%);
    width: 100%;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
    transition: all 0.3s ease;

    .rightImg {
      width: 24px;
      height: 24px;
      cursor: pointer;
      transition: transform 0.3s ease;

      &:hover {
        transform: scale(1.1);
      }
    }
  }
}

.currentList {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  width: calc(100% - 30px);
  box-sizing: border-box;
  top: 268px;
  // background: linear-gradient(145deg, #1c1c23, #2a2a35);
  // box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
  border-radius: 15px;
  display: flex;
  padding: 25px 0;
  transition: transform 0.3s ease, box-shadow 0.3s ease;

  &:hover {
    transform: translateX(-50%) translateY(-2px);
    box-shadow: 0 12px 40px rgba(0, 0, 0, 0.4);
  }

  .item {
    flex: 1;
    display: flex;
    flex-direction: column;
    padding: 10px;
    transition: transform 0.2s ease;

    &:hover {
      transform: translateY(-3px);
    }

    .itemTop {
      font-size: 14px;
      color: #ffffff;
      font-weight: 500;
      text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
    }

    .itemMain {
      margin-top: 8px;
      display: flex;
      align-items: center;
      font-size: 16px;
      font-weight: 600;

      &.rise {
        color: #00f7b5;
      }

      &.fall {
        color: #ff4976;
      }
    }

    .itemFooter {
      display: flex;
      font-size: 18px;
      margin-top: 8px;
      color: #ffffff;
      font-weight: 500;
    }
  }

  .centerItem {
    justify-content: center;
    align-items: center;
    position: relative;

    // Remove or comment out the ::after pseudo-element that creates the vertical line
    // &::after {
    //   content: '';
    //   position: absolute;
    //   right: 0;
    //   top: 15%;
    //   height: 70%;
    //   width: 1px;
    //   background: rgba(255, 255, 255, 0.1);
    // }

    &:last-child::after {
      display: none;
    }
  }
}

// 添加暗色主题变量覆盖
:root {
  --ex-home-box-background-color: #1c1c23;
  --ex-home-box-background-color3: #2a2a35;
  --ex-default-font-color: #ffffff;
}

:deep(.sidebar) {
  margin-top: -1px;
  max-width: var(--ex-max-width);
  width: 100%;
  left: auto;
  height: 100%;
  background: #1c1c23;
  box-shadow: -5px 0 20px rgba(0, 0, 0, 0.3);
}

.gradient-text {
  background-image: linear-gradient(to right, #00f7d8, #4db9ff, #aa5fff, #ff56c1);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  font-weight: bold;
  font-size: 10px;
  letter-spacing: 1px;
}
</style>
