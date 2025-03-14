<template>
  <div :class="DIFF_HOME_BANNER.includes(_getConfig('_APP_ENV')) ? 'main mainEbc' : 'main'">
    <div
      class="item"
      v-for="item in menuList"
      :key="item.img"
      @click="routeLink(item.linkUrl, item.flag)"
    >
      <img :src="item.imgUrl" :alt="item.name" class="itemImg" />
      <div class="itemName text-ellipsis2">
        {{ _t18(`${item.key}`, ['robinhood2']) }}
      </div>
    </div>
  </div>
  <div class="notice">
    <div class="itemNo">
      <svg-load name="mengbanzu12" class="noticeImg"></svg-load>
      <van-notice-bar class="currentNotice" :text="currentNotice" />
    </div>
  </div>
  <div class="linkList">
    <div class="linkLeft">
      <!-- 快捷充币 -->
      <div class="item" @click="toRecharge">
        <svg-load name="mengbanzu13" class="leftImg"></svg-load>
        <div class="right">
          <div class="top">{{ _t18(`home_recharge`, ['latcoin', 'aams', 'robinhood2']) }}</div>
        </div>
      </div>
      <!-- 快速提币 -->
      <div class="item" @click="$router.push('/withdraw')">
        <svg-load name="zu29" class="leftImg"></svg-load>
        <div class="right">
          <div class="top">{{ _t18(`quick_withdrawal`, ['latcoin', 'aams', 'robinhood2']) }}</div>
        </div>
      </div>
    </div>
    <!-- 在线客服 -->
    <div class="linkRight" @click="dispatchCustomEvent('event_serviceChange')">
      <div class="itemImg">💬</div>
      <span>{{ _t18(`home_service`) }}</span>
    </div>
    <InfoPopup :show="showInfoPopup" :data="infoData" @closeBtn="closeBtn"></InfoPopup>
  </div>
    <!-- 直播弹窗 -->
    <van-popup v-model:show="showNotice" round closeable>
      <div class="showNoticeContent">
        <p class="showNoticeContent_title fw-bold">
          {{ showNoticeContent?.noticeTitle || '' }}
        </p>
        <div class="showNoticeContent_content">
          <img :src="showNoticeContent?.imgUrl" v-if="showNoticeContent?.imgUrl" />
        </div>
      </div>
    </van-popup>
</template>
<script setup>
import { DIFF_RECHARGE_COSTORM, DIFF_HOME_BANNER } from '@/config/index'
import { publiceNotice } from '@/api/common/index'
import { onMounted, computed, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useMainStore } from '@/store/index.js'
import { useUserStore } from '@/store/user/index'
import { getInfo } from '@/api/info.js'
import { noticeList } from '@/api/common/index'
import { _t18, _toView } from '@/utils/public'
import InfoPopup from '@/views/home/components/InfoPopup.vue'
import { dispatchCustomEvent } from '@/utils'

const mainStroe = useMainStore()
const userStore = useUserStore()
const tokenStatus = ref(userStore.isSign)
const $router = useRouter()
// DeFi挖矿 质押挖矿 助力货 闪兑 下载中心 推广中心 秒合约 理财 申购 直播 福利活动
const menuList = computed(() => {
  let tempData = mainStroe.getJinGangList.filter((item) => {
    return item.isOpen == true
  })
  console.log("tempData",tempData)
  
  return [
    {
        "name": "质押挖矿",
        "key": "defi_host_lockup",
        "imgUrl": "/src/assets/icons/3fab02d630fb439ea67978312608d79d.png",
        "linkUrl": "/pledge",
        "sort": 2,
        "isOpen": true
    },
    {
        "name": "助理贷",
        "key": "fast_help_loan",
        "imgUrl": "/src/assets/icons/0781c670efd044a3a7bba32daf14b674.png",
        "linkUrl": "/loan",
        "sort": 3,
        "isOpen": true
    },
    {
        "name": "下载中心",
        "key": "download_center",
        "imgUrl": "/src/assets/icons/df71446a79964bee8eb2873054aba775.jpg",
        "linkUrl": "https://www.antbit.cc/",
        "sort": 5,
        "isOpen": true
    },
    {
        "name": "推广中心",
        "key": "promotion_center",
        "imgUrl": "/src/assets/icons/d8df6263d88c44fe9347d54b0b229e27.jpg",
        "linkUrl": "/plug",
        "sort": 6,
        "isOpen": true
    },
    {
        "name": "期权",
        "key": "trade_tab6",
        "imgUrl": "/src/assets/icons/c6c0e72d6425491d9d61e7033934542f.jpg",
        "linkUrl": "/trade",
        "sort": 7,
        "isOpen": true
    },
    {
        "name": "理财",
        "key": "financial",
        "imgUrl": "/src/assets/icons/b3a27a6643da429782828d89019e18a9.jpg",
        "linkUrl": "/financial",
        "sort": 8,
        "isOpen": true
    },
    {
        "name": "U本位",
        "key": "trade_tab5",
        "imgUrl": "/src/assets/icons/5e03fe26952c4220b66ee9b7e6f88adb.jpg",
        "linkUrl": "/tradeU",
        "sort": 9,
        "isOpen": true
    },
    {
        "name": "币币交易",
        "key": "trade_tab3",
        "imgUrl": "/src/assets/icons/aacb46ce3bb2462983e4bef9db25fb87.png",
        "linkUrl": "/trade?flag=BB&show=onlyB",
        "sort": 9,
        "isOpen": true
    }
]
})
const currentNotice = ref('')
const routeLink = (link) => {
  if (link === 'live' && showNoticeContent.value) {
    // showNotice.value=true
    getOpenPopupContent()
  } else if (link === '/trade') {
    mainStroe.setTradeStatus(Number(0))
    $router.push(link)
  } else if (link.includes('http')) {
    location.href = link
  } else {
    $router.push(link)
  }
}
const showInfoPopup = ref(false)
const closeBtn = () => {
  showInfoPopup.value = false
}
const infoData = ref([])
/**
 * 用户信息改变监听回调事件
 * @param {*} e
 */
const event_userInfoChange = (e) => {
  getInfoList()
}

const getInfoList = async () => {
  if (tokenStatus.value) {
    let res = await getInfo()
    if (res.code == '200' && res.rows.length > 0) {
      console.log(res.rows)
      infoData.value = res.rows
      showInfoPopup.value = true
    }
  }

  // getInfo().then((res) => {
  //   if (res.code == 200) {
  //     infoData.value = res.rows
  //     showInfoPopup.value = true
  //   }
  // })
}
getInfoList()
/**直播弹窗内容 */
const showNotice = ref(false)
const showNoticeContent = ref({})
const getOpenPopupContent = async () => {
  const res = await publiceNotice('POP_UPS_NOTICE', 'DEFI_POP_UPS_NOTICE')
  if ((res.code = '200')) {
    if (res.data.length) {
      showNotice.value = true
      showNoticeContent.value = res.data[0]
    }
  }
}
onMounted(async () => {
  try {
    const res = await noticeList('ROLL_NOTICE')
    if (res.code === 200) {
      if (res.data.length > 0 && res.data[0].status == '0')
        currentNotice.value = res.data[0].noticeContent
    }
  } catch (error) {}
  document.addEventListener('event_userInfoChange', event_userInfoChange)
  // getInfoList()
})

onUnmounted(() => {
  document.removeEventListener('event_userInfoChange', event_userInfoChange)
})
/**
 * 点击快捷充币
 */
const toRecharge = () => {
  if (DIFF_RECHARGE_COSTORM.includes(__config._APP_ENV)) {
    if (tokenStatus.value) {
      dispatchCustomEvent('event_serviceChange')
    } else {
      _toView('/sign-in')
    }
  } else {
    _toView('/recharge')
  }
}
</script>
<style lang="scss" scoped>
.main {
  padding: 160px 0px 0;
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  background: linear-gradient(180deg, #121212 0%, #1a1a1a 100%);

  .item {
    width: 25%;
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-bottom: 20px;
    transition: all 0.3s ease;
    animation: fadeInUp 0.5s ease;
    animation-fill-mode: both;

    @for $i from 1 through 8 {
      &:nth-child(#{$i}) {
        animation-delay: #{$i * 0.1}s;
      }
    }
    
    &:active {
      transform: scale(0.95);
    }

    .itemImg {
      width: 24px;
      height: 24px;
      padding: 12px;
      transition: all 0.3s ease;
      animation: pulse 2s infinite;
      
      &:hover {
        animation: bounce 1s;
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
      }
    }

    .itemName {
      margin-top: 8px;
      font-size: 12px;
      color: rgba(255, 255, 255, 0.95);
      text-align: center;
      width: 60px;
      word-break: break-word;
    }
  }
}

.mainEbc {
  padding-top: 0;
}

.notice {
  padding: 0 15px;
  margin: 15px 0;

  .itemNo {
    display: flex;
    height: 40px;
    align-items: center;
    border-radius: 20px;
    backdrop-filter: blur(10px);
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
    }

    .noticeImg {
      margin-left: 15px;
      width: 20px;
      height: 20px;
    }

    .currentNotice {
      flex: 1;
      border-radius: 20px;
      font-size: 14px;
      color: rgba(255, 255, 255, 0.9);
      background: transparent;
    }
  }
}

.linkList {
  padding: 0 15px;
  display: flex;
  justify-content: space-between;
  align-items: stretch;
  gap: 10px;
  margin: 0 auto 6px;
  width: calc(100% - 30px);
  animation: fadeInUp 0.8s ease;

  .linkLeft {
    flex: 1;
    display: flex;
    gap: 10px;
    min-width: 0;

    .item {
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: flex-start;
      border-radius: 12px;
      padding: 8px;
      height: 40px;
      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
      transition: all 0.3s ease;
      min-width: 0;

      &:hover {
        transform: translateY(-2px);
        background: #252525;
        box-shadow: 0 6px 20px rgba(0, 0, 0, 0.25);
      }

      .leftImg {
        width: 20px;
        height: 20px;
        margin-right: 6px;
        flex-shrink: 0;
      }

      .right {
        min-width: 0;
        
        .top {
          font-size: 12px;
          color: rgba(255, 255, 255, 0.95);
          font-weight: 500;
          white-space: normal;
          word-wrap: break-word;
          overflow: hidden;
          text-overflow: ellipsis;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
        }
      }
    }
  }

  .linkRight {
    width: 50px;
    min-width: 50px;
    height: 40px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    gap: 2px;
    padding: 4px;
    border-radius: 12px;
    transition: all 0.3s ease;
    flex-shrink: 0;
    
    &:hover {
      transform: translateY(-2px);
      background: #252525;
      box-shadow: 0 6px 20px rgba(0, 0, 0, 0.25);
    }

    .itemImg {
      width: 18px;
      height: 18px;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 16px;
    }

    span {
      font-size: 11px;
      color: rgba(255, 255, 255, 0.95);
      text-align: center;
      white-space: normal;
      word-wrap: break-word;
      width: 100%;
      transform: scale(0.9);
    }
  }
}

.showNoticeContent {
  min-width: 300px;
  text-align: center;
  background: #1a1a1a;
  border-radius: 16px;
  
  .showNoticeContent_title {
    font-size: 18px;
    padding: 20px 0;
    color: rgba(255, 255, 255, 0.9);
    border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  }
  
  .showNoticeContent_content {
    padding: 20px 15px 30px;
    max-height: 400px;
  }
}

// 添加深色主题相关变量覆盖
:root {
  --ex-default-font-color: rgba(255, 255, 255, 0.95);
  --ex-home-notice-bgcolor: rgba(255, 255, 255, 0.07);
  --ex-home-notice-color: rgba(255, 255, 255, 0.95);
  --ex-home-box-background-color: rgba(255, 255, 255, 0.09);
  --ex-home-box-background-color2: rgba(255, 255, 255, 0.07);
  --ex-home-box-border-color: rgba(255, 255, 255, 0.12);
  --ex-home-font-color: rgba(255, 255, 255, 0.95);
}

// 添加动画关键帧
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translate3d(0, 20px, 0);
  }
  to {
    opacity: 1;
    transform: translate3d(0, 0, 0);
  }
}

@keyframes fadeInLeft {
  from {
    opacity: 0;
    transform: translate3d(-20px, 0, 0);
  }
  to {
    opacity: 1;
    transform: translate3d(0, 0, 0);
  }
}

@keyframes fadeInRight {
  from {
    opacity: 0;
    transform: translate3d(20px, 0, 0);
  }
  to {
    opacity: 1;
    transform: translate3d(0, 0, 0);
  }
}

@keyframes pulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
  100% {
    transform: scale(1);
  }
}

@keyframes bounce {
  0%, 20%, 50%, 80%, 100% {
    transform: translateY(0);
  }
  40% {
    transform: translateY(-10px);
  }
  60% {
    transform: translateY(-5px);
  }
}

@keyframes headShake {
  0% {
    transform: translateX(0);
  }
  6.5% {
    transform: translateX(-6px) rotateY(-9deg);
  }
  18.5% {
    transform: translateX(5px) rotateY(7deg);
  }
  31.5% {
    transform: translateX(-3px) rotateY(-5deg);
  }
  43.5% {
    transform: translateX(2px) rotateY(3deg);
  }
  50% {
    transform: translateX(0);
  }
}
</style>
