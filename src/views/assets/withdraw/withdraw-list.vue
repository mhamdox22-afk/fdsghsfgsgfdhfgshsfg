<template>
  <div class="content">
    <!-- 币种列表 -->
    <p class="title">{{ _t18('coinList') }}</p>
    <div class="list">
      <div 
        class="listCoin" 
        v-for="(item, index) in data" 
        :key="index" 
        @click="toClick(item)"
      >
        <div class="left">
          <div class="icon-wrapper">
            <svg-load :name="item.icon" class="coin"></svg-load>
          </div>
          <span>{{ item.title }}</span>
        </div>
        <div class="right">
          <svg-load name="jiantou" class="jiantou"></svg-load>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { _t18 } from '@/utils/public'
import { useToast } from '@/hook/useToast'
const { _toast } = useToast()
const router = useRouter()
const props = defineProps({
  data: {
    type: Array,
    default: []
  }
})
const flag = ref(true)
const toClick = (item) => {
  if (item.type == 1) {
    // 货币提现
    if (['paxpay'].includes(__config._APP_ENV)) {
      flag.value = false
      needPrimary(item)
    } else {
      flag.value = true
    }
    if (flag.value) {
      // fee手续费，ratio手续费率
      let params=``
      if(item.fee&&item.fee>0){
        params=`type=${item.card || item.title}&icon=${item.icon}&ratio=${item.ratio}&fee=${item.fee}`
      }else{
        params=`type=${item.card || item.title}&icon=${item.icon}&ratio=${item.ratio}`
      }
      setTimeout(() => {
        router.push(
          `/withdraw-apply?${params}`
        )
      }, 200)
    }
  } else if (item.type == 2) {
    // 银行卡提现
    if (['paxpay'].includes(__config._APP_ENV)) {
      needPrimary(item)
    } else {
      flag.value = true
    }
    if (flag.value) {
      router.push(`/withdraw-apply?type=银行卡&icon=${item.icon}`)
    }
  }
}
// 特殊平台，提现需要初级认证（初级）
import { useUserStore } from '@/store/user/index'
import { storeToRefs } from 'pinia'
const userStore = useUserStore()
userStore.getUserInfo()
const { userInfo } = storeToRefs(userStore)
const primaryAuth = ref(userInfo.value.detail?.auditStatusPrimary)
const needPrimary = (item) => {
  // console.log(primaryAuth.value)
  if (primaryAuth.value !== '1') {
    _toast('please_primary')
    setTimeout(() => {
      router.push('/certification-primary')
    }, 800)
  } else {
    flag.value = true
  }
}
</script>

<style lang="scss" scoped>
* {
  font-size: 14px;
  color: #ffffff;
}

.content {
  padding: 30px 15px 0;
  min-height: 100vh;
  background: linear-gradient(145deg, #1a1a1a, #2d2d2d);

  .title {
    font-size: 24px;
    margin-bottom: 30px;
    text-align: left;
    font-weight: 600;
    background: linear-gradient(90deg, #fff, #a0a0a0);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    animation: fadeIn 0.8s ease-in-out;
  }

  .list {
    .listCoin {
      margin-bottom: 20px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      background: rgba(255, 255, 255, 0.05);
      padding: 20px;
      border-radius: 16px;
      backdrop-filter: blur(10px);
      box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
      transition: all 0.3s ease;
      animation: slideUp 0.5s ease-out;
      animation-fill-mode: both;

      @for $i from 1 through 10 {
        &:nth-child(#{$i}) {
          animation-delay: $i * 0.1s;
        }
      }

      &:hover {
        transform: translateY(-5px);
        background: rgba(255, 255, 255, 0.08);
        box-shadow: 0 12px 20px rgba(0, 0, 0, 0.3);
      }

      .left {
        display: flex;
        align-items: center;
        
        .icon-wrapper {
          background: rgba(255, 255, 255, 0.1);
          padding: 12px;
          border-radius: 12px;
          margin-right: 15px;
          display: flex;
          align-items: center;
          justify-content: center;
          
          .coin {
            font-size: 24px;
            color: #fff;
          }
        }

        span {
          font-size: 16px;
          font-weight: 500;
          letter-spacing: 0.5px;
        }
      }

      .right {
        .jiantou {
          font-size: 12px;
          color: #ffffff;
          opacity: 0.7;
          transition: transform 0.3s ease;
        }

        &:hover .jiantou {
          transform: translateX(5px);
        }
      }
    }
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>

    transform: translateY(0);
