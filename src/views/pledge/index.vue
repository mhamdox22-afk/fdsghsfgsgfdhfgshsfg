<!-- 质押挖矿 -->
<script setup>
import { _t18 } from '@/utils/public'
import { getPledgeProductList, getPledgeShowInfo } from '@/api/pledge/index'
import { priceFormat } from '@/utils/decimal.js'
import { useRouter } from 'vue-router'
import Popup from '@/components/Popup/index.vue'
import Card from './components/card.vue'
import { onMounted, watch, onUnmounted } from 'vue'
import { rulesList } from '@/api/common/index'
const router = useRouter()

const showRule = ref(false)
const txt = _t18('defi_speak')
const popupContent = ref({})
const direction = ref('bottom')
let showPopup = () => {
  showRule.value = true
}
let closePopup = () => {
  showRule.value = false
}
const cuttentRight = reactive({
  iconRight: [
    { iconName: 'guize', clickTo: '' },
    { iconName: 'jilu', clickTo: '/pledge/pledgeOrder' }
  ]
})
const toView = (data) => {
  router.push({
    path: '/pledge/pledgeDetail',
    query: { data: encodeURI(JSON.stringify(data)) }
  })
}
const showInfo = ref({})
const projectList = ref([])
const getProjectList = async () => {
  const res = await getPledgeProductList()
  if (res.code == '200') {
    projectList.value = res.rows
  }
}
const getShowInfo = async () => {
  const res = await getPledgeShowInfo()
  if (res.code == '200') {
    showInfo.value = res.data
  }
}

/**弹窗内容 */
const getPopupContent = async () => {
  const res = await rulesList('PLEDGE_EXPLAIN')
  if ((res.code = '200')) {
    popupContent.value = res.data
  }
}

onMounted(() => {
  getProjectList()
  getShowInfo()
  getPopupContent()
})
// const projectList = computed(() => {
//   let list = []
//   list = [
//     {
//       id: 1,
//       title: '项目一',
//       kegou: '5',
//       sum: '50',
//       xiane: '200-500',
//       rate: '0%-10%',
//       period: '15'
//     },
//     {
//       id: 2,
//       title: '项目二',
//       kegou: '10',
//       sum: '50',
//       xiane: '200-500',
//       rate: '0%-10%',
//       period: '20'
//     },
//     {
//       id: 3,
//       title: '项目三',
//       kegou: '15',
//       sum: '50',
//       xiane: '200-500',
//       rate: '0%-10%',
//       period: '25'
//     }
//   ]
//   return list
// })
const manual = computed(() => {
  let list = []
  list = [
    {
      title: '什么是质押挖矿？',
      content: [
        {
          childContent:
            '质押挖矿是本公司打造的一款帮助用户快捷进行链上质押获取奖励的工具产品。通过将数字资产质押在区块链网络，并获得基于POS（Proof of Stake，即权益证明）机制产生的奖励。在这个机制中，用户将数字资产委托给节点，节点在区块链上行使出块、打包交易等权利并获得奖励。用户根据锁仓数量按比例分享节点获得的奖励。'
        }
      ],
      notes: [
        ' 注：(非出借)每6小时一个阶段每天发送4次收益详情，您可以通过收益详情查看详细收益情况。 ',
        ' 注：(本产品属福利项引流产品目前名额有限，后续将对用户增设数字资产评估，只对达标用户开启该产品，所以先到先得)。 '
      ]
    },
    {
      title: '产品优势',
      content: [
        {
          childTitle: '稳健型',
          childContent: '质押挖矿可以获得第三方相对稳定的奖励，产品期限多样。'
        }
      ]
    },
    {
      title: '投向生息资产说明',
      content: [
        {
          childContent:
            '质押挖矿是将您的数字资产质押在区块链上，以支持该区块链网络的运营，并获得相应的奖励。'
        }
      ]
    },
    {
      title: '收益说明',
      content: [
        {
          childContent: 'T日申购成功后，T+1日00:00开始起息，02:00利息结算。'
        }
      ]
    },
    {
      title: '交易说明',
      content: [
        {
          childTitle: '买入规则',
          childContent: '支持随时申购。'
        }
      ]
    },
    {
      title: '风险提示',
      content: [
        {
          childContent: '提前赎回定期产品，系统将扣除该笔订单已经获得的部分收益。'
        }
      ]
    }
  ]
  return list
})

// 监听弹窗状态，控制body滚动
watch(() => showRule.value, (newVal) => {
  if (newVal) {
    // 当弹窗显示时，禁止背景滚动
    document.body.style.overflow = 'hidden'
    document.body.style.position = 'fixed'
    document.body.style.width = '100%'
  } else {
    // 当弹窗关闭时，恢复滚动
    document.body.style.overflow = ''
    document.body.style.position = ''
    document.body.style.width = ''
  }
})

// 组件卸载时确保恢复滚动状态
onUnmounted(() => {
  document.body.style.overflow = ''
  document.body.style.position = ''
  document.body.style.width = ''
})
</script>
<template>
  <div class="pledge-container">
    <!-- 质押挖矿规则弹窗 - 使用 teleport 传送到 body -->
    <teleport to="body">
      <!-- 添加遮罩层 -->
      <div v-if="showRule" class="modal-backdrop" @click="closePopup"></div>
      
      <Popup
        :show="showRule"
        :direction="direction"
        @handelClose="closePopup"
        :title="txt"
        :content="popupContent"
        :z-index="9999"
      >
      </Popup>
    </teleport>
    <HeaderBar
      :currentName="_t18('defi_host_lockup')"
      :cuttentRight="cuttentRight"
      @showPopup="showPopup"
    ></HeaderBar>
    <!-- 收益信息（资金、收益） -->
    <div class="userAccount">
      <div class="title">
        <image-load filePath="usdt.png" name="usdt" class="usdt"></image-load>
        <p class="fw-num">USDT</p>
      </div>
      <div class="fund">
        <image-load filePath="zhiyaimg.png" name="zhiyaimg" class="zhiyaimg"></image-load>
        <div class="top">
          <div class="left">
            <p class="fw-num">{{ priceFormat(showInfo.amount) || 0 }}</p>
            <!-- 正在托管 -->
            <span>{{ _t18('pledge_hosting') }}</span>
          </div>
          <div class="right">
            <p class="fw-num">{{ showInfo.orderNum || 0 }}</p>
            <!-- 委托订单 -->
            <span>{{ _t18('pledge_commissioned_order') }}</span>
          </div>
        </div>
        <div class="bottom">
          <div class="left">
            <p class="fw-num">{{ priceFormat(showInfo.todayProfit) || 0 }}</p>
            <!-- 今日收益 -->
            <span>{{ _t18('pledge_Today_Earnings') }}</span>
          </div>
          <div class="right">
            <p class="fw-num">{{ priceFormat(showInfo.profitMoney) || 0 }}</p>
            <!-- 累计收益 -->
            <span>{{ _t18('Cumulative_income') }}</span>
          </div>
        </div>
      </div>
    </div>
    <!-- 项目信息 -->
    <div class="project">
      <div class="projectList" v-if="projectList?.length > 0">
        <Card
          v-for="(item, index) in projectList"
          @click="toView(item)"
          :key="index"
          :cardData="item"
        ></Card>
      </div>
      <Nodata v-else></Nodata>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.pledge-container {
  min-height: 100vh;
  background: linear-gradient(145deg, #121212, #1e1e1e);
  color: #ffffff;
  background-image: url('@/assets/icons/zhiyabg.png');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  background-attachment: fixed;
  position: relative;
  &::before {
    content: '';
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(145deg, rgba(18, 18, 18, 0.9), rgba(30, 30, 30, 0.9));
    z-index: 0;
  }
  > * {
    position: relative;
    z-index: 1;
  }
}
.banner {
  padding: 10px 25px 0;
  img {
    width: 100%;
    height: auto;
    border-radius: 12px;
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
    transition: transform 0.3s ease;
    &:hover {
      transform: scale(1.02);
    }
  }
}
.userAccount {
  font-size: 14px;
  padding: 0 15px;
  color: #ffffff;
  .title {
    padding: 20px 0;
    display: flex;
    align-items: center;
    .usdt {
      font-size: 34px;
      filter: drop-shadow(0 4px 6px rgba(0, 0, 0, 0.3));
    }
    p {
      margin-left: 10px;
      font-size: 24px;
      background: linear-gradient(135deg, #fff 0%, #a7a7a7 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
    }
  }
  .fund {
    border: 1px solid rgba(255, 255, 255, 0.1);
    border-radius: 16px;
    position: relative;
    background: linear-gradient(145deg, #1a1a1a, #2a2a2a);
    box-shadow: 0 10px 20px rgba(0, 0, 0, 0.3);
    backdrop-filter: blur(10px);
    transition: transform 0.3s ease;

    &:hover {
      transform: translateY(-5px);
    }

    .zhiyaimg {
      position: absolute;
      font-size: 100px;
      transform: translate(-50%, -50%);
      left: 50%;
      top: 50%;
      opacity: 0.1;
      animation: rotate 20s linear infinite;
    }

    .top,
    .bottom {
      display: flex;
      justify-content: space-between;
      .left,
      .right {
        width: 100%;
        height: 100%;
        padding: 25px 15px;
        transition: background-color 0.3s ease;

        &:hover {
          background-color: rgba(255, 255, 255, 0.05);
        }

        p {
          font-size: 18px;
          margin-bottom: 10px;
          color: #ffffff;
          text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
        }
        span {
          color: rgba(255, 255, 255, 0.6);
        }
      }
      .right {
        border-left: 1px solid rgba(255, 255, 255, 0.1);
        text-align: right;
      }
    }
    .bottom {
      border-top: 1px solid rgba(255, 255, 255, 0.1);
      p {
        color: #00ff9d;
      }
    }
  }
}
.project {
  padding: 0 15px 50px;
  .projectList {
    display: grid;
    gap: 15px;
    animation: fadeInUp 0.5s ease;
  }
}

@keyframes rotate {
  from {
    transform: translate(-50%, -50%) rotate(0deg);
  }
  to {
    transform: translate(-50%, -50%) rotate(360deg);
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

// 添加数字跳动动画
.fw-num {
  transition: transform 0.3s ease;
  &:hover {
    transform: scale(1.1);
    color: #00ff9d;
  }
}
</style>

<style lang="scss">
/* 为弹窗组件添加最高层级和遮罩层 */
.van-popup, .popup-container {
  z-index: 9999 !important; /* 使用足够高的值确保在最上层 */
}

/* 添加全局遮罩层样式 */
.modal-backdrop {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background-color: rgba(0, 0, 0, 0.7);
  z-index: 9990 !important; /* 遮罩层z-index略低于弹窗 */
  backdrop-filter: blur(3px); /* 添加模糊效果 */
}

/* 确保弹窗内容不受backdrop-filter影响 */
.popup-content {
  backdrop-filter: none !important;
}
</style>
