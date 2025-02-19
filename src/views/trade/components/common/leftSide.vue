<template>
  <div class="leftSide">
    <div class="leftSideHeader fw-bold">{{ _t18('quote') }}</div>
    <div class="search">
      <svg-load name="sousuo-h" class="searchImg"></svg-load>
      <div class="contain">
        <input
          type="text"
          v-model.trim="searchName"
          class="inputSearch"
          :placeholder="_t18('search_currency')"
          @input="searchBtn"
        />
      </div>
    </div>
    <Filter :list="currentCoinList" @toSort="toSort"></Filter>
    <div class="leftList">
      <div v-if="currentList.length">
        <CurrencyItem
          v-for="(item, index) in filterKeyCoin(currentList, searchName)"
          :key="index"
          :currencyItem="item"
          :showLeftImg="['fx'].includes(_getConfig('_APP_ENV')) ? true : false"
          @click="linkTo(item)"
        ></CurrencyItem>
      </div>
      <Nodata v-else></Nodata>
    </div>
  </div>
</template>

<script setup>
import Filter from './filter.vue'
import { filterKeyCoin } from '@/utils/filters'
import CurrencyItem from '@/components/CurrencyList/currencyItem.vue'
import { _t18 } from '@/utils/public'
import { useMainStore } from '@/store/index'
const mainStore = useMainStore()
import { useTradeStore } from '@/store/trade'

const tradeStore = useTradeStore()
import { computed } from 'vue'
import { dispatchCustomEvent } from '@/utils'

import { useRouter, useRoute } from 'vue-router'
const $router = useRouter()
const $route = useRoute()
const props = defineProps({
  headerList: {
    type: Array
  }
})
/**
 * 搜索
 */
const searchName = ref('')
/**
 * 币种列表
 */
const currentList = ref([])
const currentCoinList = computed(() => {
  let tempList = []
  if ($route.query.type) {
    if ($route.query.type == 1) {
      tempList = tradeStore.spotCoinList
    } else if ($route.query.type == 2) {
      tempList = tradeStore.contractCoinList
    }
  } else {
    if ($route.path == '/tradeU') {
      tempList = tradeStore.contractCoinList
    } else if ($route.path == '/tradeBB') {
      tempList = tradeStore.spotCoinList
    } else {
      let temp = props.headerList[mainStore.tradeFlag]
      if (temp.currentTab == 'secondContract') {
        tempList = tradeStore.secondContractCoinList
      } else if (temp.currentTab == 'spot') {
        tempList = tradeStore.spotCoinList
      } else if (temp.currentTab == 'contract') {
        tempList = tradeStore.contractCoinList
      }
    }
  }

  currentList.value = tempList
  return tempList
})
/** 点击每一项 */
const emit = defineEmits(['linkTo', 'close'])
const linkTo = (item) => {
  let type = ''
  if ($route.query.type == 1) {
    type = 'spot'
  } else if ($route.query.type == 2) {
    type = 'contract'
  }
  if (!$route.query.type) {
    $router.replace(`${$route.path}?symbol=${item.coin}`)
  } else {
    $router.replace(`${$route.path}?symbol=${item.coin}&type=${$route.query.type}`)
  }
  dispatchCustomEvent('event_tradeSymbolChange', {
    type: type,
    symbol: item.symbol,
    coin: item.coin,
    coinInfo: item
  })

  emit('close')
  emit('linkTo', item.coin)
}
const toSort = (v) => {
  currentList.value = v
}
</script>

<style lang="scss" scoped>
.leftSide {
  background: #1a1a1a;
  min-height: 100vh;
  height: 100%;
  width: 100%;
  position: fixed;
  left: 0;
  top: 0;
  overflow-y: auto;
  transition: all 0.3s ease;
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.5);

  .leftSideHeader {
    margin: 0 15px;
    padding-top: 5px;
    font-size: 24px;
    font-weight: bold;
    color: #ffffff;
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
    animation: fadeInDown 0.5s ease;
  }

  .search {
    margin: 30px 15px 15px;
    height: 46px;
    background: #2a2a2a;
    border-radius: 23px;
    display: flex;
    padding: 0 20px;
    align-items: center;
    justify-content: space-between;
    box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.2);
    transition: all 0.3s ease;
    animation: fadeInUp 0.5s ease;

    &:focus-within {
      transform: translateY(-1px);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
      background: #333333;
    }

    .searchImg {
      width: 20px;
      height: 20px;
      margin-right: 10px;
      opacity: 0.8;
      transition: all 0.3s ease;
    }

    .contain {
      flex: 1;
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-size: 14px;
      color: #ffffff;

      .inputSearch {
        flex: 1;
        background: transparent;
        color: #ffffff;
        transition: all 0.3s ease;
        
        &:focus {
          outline: none;
        }
      }

      input::-webkit-input-placeholder {
        color: rgba(255, 255, 255, 0.5);
      }
      input::-moz-input-placeholder {
        color: rgba(255, 255, 255, 0.5);
      }
      input::-ms-input-placeholder {
        color: rgba(255, 255, 255, 0.5);
      }
    }
  }

  .leftList {
    padding: 0 10px;
    animation: fadeIn 0.6s ease;

    :deep(.currency-item) {
      margin: 8px 5px;
      padding: 12px;
      border-radius: 12px;
      background: #2a2a2a;
      transition: all 0.3s ease;
      box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);

      &:hover, &:active {
        transform: translateY(-2px);
        background: #333333;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
      }
    }
  }
}

@keyframes fadeInDown {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
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

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

// 添加深色模式的滚动条样式
::-webkit-scrollbar {
  width: 6px;
}

::-webkit-scrollbar-track {
  background: #1a1a1a;
}

::-webkit-scrollbar-thumb {
  background: #444;
  border-radius: 3px;
  
  &:hover {
    background: #555;
  }
}
</style>
