<template>
  <div class="userAmount">
    <div>
      <!-- 账户余额 -->
      <p>{{ _t18('asset_account', ['ebc']) }}(USDT)</p>
      <svg-load :name="showicon" class="yanjin" @click="handleYanjin"></svg-load>
      <svg-load name="refresh" class="shuaxin" @click="handleShuaxin"></svg-load>
    </div>
    <p class="userInfo fw-num">{{ _numberWithCommas(amount) }}</p>
  </div>
  <div class="currencyAbout">
    <div
      v-for="(item, index) in currencyAbout.filter((items) => {
        return items.isShow
      })"
      :key="index"
      @click="toRecharge(item.url)"
    >
      <svg-load :name="item.icon" class="currencyIcon"></svg-load>
      <p>{{ item.title }}</p>
    </div>
  </div>
</template>

<script setup>
import { dispatchCustomEvent } from '@/utils'
import { _t18, _toView } from '@/utils/public'
import { DIFF_RECHARGE_COSTORM } from '@/config/index'
const props = defineProps({
  data: {
    type: Object
  },
  amount: {
    type: String
  },
  coin: {
    type: String,
    default: 'USDT'
  },
  type: {
    type: String
  },
  showicon: {
    type: String
  }
})

const emit = defineEmits(['handleYanjin', 'handleShuaxin'])
const handleYanjin = () => {
  emit('handleYanjin')
}
const handleShuaxin = () => {
  emit('handleShuaxin')
}

const currencyAbout = computed(() => {
  let list = []
  // {
  //         icon: 'asset_ganggan',
  //         title: _t18('trade_tab5', ['paxpay']),
  //         url: '/tradeU',
  //         isShow: true
  //       }
  if (props.type == '平台资产') {
    if (['paxpay', 'aams'].includes(__config._APP_ENV)) {
      list = [
        // {
        //   icon: 'chongbi',
        //   title: _t18('asset_recharge', ['aams']),
        //   url: '/recharge',
        //   isShow: true
        // }, //充币 //杠杆
        { icon: 'tibi', title: _t18('asset_withdraw', ['aams']), url: '/withdraw', isShow: true }, //提币 ,
        { icon: 'dui', title: _t18('asset_swap'), url: '/swap', isShow: true }, //闪兑
        // {icon: 'asset_licai',title: _t18('financial', ['paxpay']),url: '/financial',isShow: true}, //理财
        // { icon: 'daikuai', title: _t18('asset_loan'), url: '/loan', isShow: false }, //助力贷
        { icon: 'huazhuan', title: _t18('asset_transfer'), url: '/transfer', isShow: true } //划转
      ]
    } else if (['coinmarketcap'].includes(__config._APP_ENV)) {
      list = [
        // {
        //   icon: 'chongbi',
        //   title: _t18('asset_recharge', ['coinmarketcap']),
        //   url: '/recharge',
        //   isShow: true
        // },
        {
          icon: 'tibi',
          title: _t18('asset_withdraw', ['coinmarketcap']),
          url: '/withdraw',
          isShow: true
        },
        { icon: 'dui', title: _t18('asset_swap'), url: '/swap', isShow: true },
        // { icon: 'huazhuan', title: _t18('asset_transfer'), url: '/loan', isShow: true }
      ]
    } else {
      list = [
        // {
        //   icon: 'chongbi',
        //   title: _t18('asset_recharge', ['latcoin']),
        //   url: '/recharge',
        //   isShow: true
        // }, //充币
        {
          icon: 'tibi',
          title: _t18('asset_withdraw', ['latcoin']),
          url: '/withdraw',
          isShow: true
        }, //提币
        { icon: 'dui', title: _t18('asset_swap'), url: '/swap', isShow: true }, //闪兑
        // { icon: 'daikuai', title: _t18('asset_loan'), url: '/loan', isShow: false }, //助力贷
        { icon: 'huazhuan', title: _t18('asset_transfer'), url: '/transfer', isShow: true } //划转
      ]
    }
  } else {
    if (['paxpay'].includes(__config._APP_ENV)) {
      list = [
        { icon: 'huazhuan', title: _t18('asset_transfer'), url: '/transfer', isShow: true }
        // { icon: 'chongbi', title: '充币', url: '/recharge', isShow: true },
        // { icon: 'tibi', title: '提币', url: '/withdraw', isShow: true },
        // { icon: 'dui', title: '闪兑', url: '/swap', isShow: true },
        // { icon: 'daikuai', title: _t18('asset_loan'), url: '/loan', isShow: true },
      ]
    } else {
      list = [
        { icon: 'huazhuan', title: _t18('asset_transfer'), url: '/transfer', isShow: true },
        // { icon: 'chongbi', title: '充币', url: '/recharge', isShow: true },
        // { icon: 'tibi', title: '提币', url: '/withdraw', isShow: true },
        // { icon: 'dui', title: '闪兑', url: '/swap', isShow: true },
        // { icon: 'daikuai', title: _t18('asset_loan'), url: '/loan', isShow: true }
      ]
    }
  }

  return list
})
/**
 * 点击快捷充币
 */
const toRecharge = (url) => {
  if (DIFF_RECHARGE_COSTORM.includes(__config._APP_ENV) && url == '/recharge') {
    dispatchCustomEvent('event_serviceChange')
  } else {
    _toView(url)
  }
}
</script>

<style lang="scss" scoped>
* {
  font-size: 14px;
  color: #ffffff;
}

.userAmount {
  background: linear-gradient(145deg, #1a1a1a, #2d2d2d);
  border-radius: 16px;
  padding: 30px 20px;
  margin: 15px;
  box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.36);
  backdrop-filter: blur(4px);
  transition: all 0.3s ease;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 10px 40px 0 rgba(0, 0, 0, 0.45);
  }

  div {
    display: flex;
    align-items: center;
    margin-bottom: 25px;
    
    .yanjin {
      font-size: 16px;
      margin-left: 15px;
      margin-right: 15px;
      cursor: pointer;
      transition: transform 0.2s ease;
      
      &:hover {
        transform: scale(1.1);
      }
    }
    
    .shuaxin {
      font-size: 14px;
      cursor: pointer;
      transition: transform 0.3s ease;
      
      &:hover {
        transform: rotate(180deg);
      }
    }
    
    p {
      font-size: 14px;
      color: rgba(255, 255, 255, 0.7);
    }
  }
  
  & > p {
    font-size: 34px;
    font-weight: 600;
    background: linear-gradient(90deg, #fff, #a5a5a5);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    animation: fadeIn 0.5s ease;
  }
}

.currencyAbout {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(80px, 1fr));
  gap: 15px;
  padding: 20px 15px;
  
  & > div {
    background: rgba(255, 255, 255, 0.05);
    border-radius: 12px;
    padding: 15px 10px;
    transition: all 0.3s ease;
    cursor: pointer;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    
    &:hover {
      background: rgba(255, 255, 255, 0.1);
      transform: translateY(-3px);
      box-shadow: 0 6px 20px rgba(0, 0, 0, 0.2);
    }
    
    .currencyIcon {
      font-size: 32px;
      margin-bottom: 10px;
      transition: transform 0.3s ease;
    }
    
    &:hover .currencyIcon {
      transform: scale(1.1);
    }
    
    p {
      font-size: 13px;
      color: rgba(255, 255, 255, 0.9);
      margin-top: 8px;
      text-align: center;
    }
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

// 添加深色主题相关的CSS变量覆盖
:root {
  --ex-default-font-color: #ffffff;
  --ex-passive-font-color: rgba(255, 255, 255, 0.7);
  --ex-border-color: rgba(255, 255, 255, 0.1);
}
</style>
