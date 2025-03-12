<template>
  <div v-if="props.show" class="area-code-modal">
    <div class="overlay" @click="handelClick"></div>
    <div class="area-code-sheet">
      <div class="top">
        <div class="title">{{ _t18('areacode') }}</div>
        <div class="right">
          <svg-load name="guanbi" class="guanbi" @click="handelClick"></svg-load>
        </div>
      </div>

      <div class="search-box">
        <div class="search-input">
          <input
            type="text"
            v-model="keywords"
            :placeholder="_t18('enter_search_keywords')"
            @input="onUpdate"
          />
        </div>
      </div>

      <div class="content" v-if="areaList.length">
        <div v-for="(item, index) in areaList" :key="index">
          <div v-if="item.list.length" class="item">
            <div class="title">{{ item.letter }}</div>
            <div
              v-for="(items, indexs) in item.list"
              :key="indexs"
              class="country-item"
              @click="selectCode(items.phoneCode)"
            >
              <p class="left">{{ current == 'zh' ? items.chineseName : items.englishName }}</p>
              <p class="right">+{{ items.phoneCode }}</p>
            </div>
          </div>
        </div>
      </div>
      <Nodata v-else></Nodata>
    </div>
  </div>
</template>

<script setup>
import { _t18 } from '@/utils/public'
import { getAreaCode } from '@/api/user'
import { storageDict } from '@/config/dict'
import { ref } from 'vue'

const current = ref(localStorage.getItem(storageDict.LANGUAGE))
const keywords = ref('')

// 区号列表
let originAreaList = []
const areaList = ref([])
// 获取区号
const codeList = () => {
  getAreaCode().then((res) => {
    if (res.code == '200') {
      let arr = res.data.map((item) => {
        item.list = item.data
        return item
      })
      areaList.value = arr
      originAreaList = JSON.parse(JSON.stringify(arr))
    }
  })
}
codeList()
const props = defineProps({
  show: {
    type: Boolean
  }
})
const emit = defineEmits(['handelClick', 'handelSelect'])
const handelClick = () => {
  emit('handelClick')
}
const selectCode = (item) => {
  handelClick()
  emit('handelSelect', item)
}
const onUpdate = (val) => {
  areaList.value = originAreaList
    .filter((item) => {
      item.list = item.data.filter((item1) => {
        return (
          item1.englishName.toLowerCase().indexOf(val.toLowerCase()) > -1 ||
          item1.chineseName.toLowerCase().indexOf(val.toLowerCase()) > -1 ||
          item1.phoneCode.toLowerCase().indexOf(val.toLowerCase()) > -1 ||
          item1.countryCode.toLowerCase().indexOf(val.toLowerCase()) > -1
        )
      })
      return item
    })
    .filter((item) => item.list.length)
}
</script>

<style lang="scss" scoped>
.area-code-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 2000;
  
  .overlay {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
    backdrop-filter: blur(4px);
    animation: fadeIn 0.3s ease;
  }
}

.area-code-sheet {
  position: fixed;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 100%;
  max-width: var(--ex-max-width);
  max-height: 70vh;
  background: #1a1a1a;
  border-radius: 16px 16px 0 0;
  overflow: hidden;
  animation: slideUp 0.3s ease;
  
  .top {
    padding: 20px 0;
    text-align: center;
    position: relative;
    background: #232323;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);

    .title {
      color: #ffffff;
      font-weight: 600;
      letter-spacing: 1px;
      font-size: 18px;
    }

    .right {
      position: absolute;
      top: 20px;
      right: 20px;
      
      .guanbi {
        font-size: 20px;
        color: #ffffff;
        transition: transform 0.3s ease;
        cursor: pointer;
        
        &:hover {
          transform: rotate(90deg);
        }
      }
    }
  }

  .search-box {
    padding: 15px;
    background: #232323;

    .search-input {
      position: relative;
      background: #2c2c2c;
      border-radius: 8px;
      padding: 8px 12px;
      display: flex;
      align-items: center;
      box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.1);

      .search-icon {
        width: 20px;
        height: 20px;
        color: #666666;
        margin-right: 8px;
      }

      input {
        flex: 1;
        border: none;
        background: transparent;
        color: #ffffff;
        font-size: 16px;
        outline: none;
        padding: 4px 0;

        &::placeholder {
          color: #666666;
        }
      }
    }
  }

  .content {
    overflow-y: auto;
    max-height: calc(70vh - 120px);

    .item {
      border-top: 1px solid #333333;
      padding: 15px 0;

      .title {
        font-size: 18px;
        font-weight: 600;
        padding: 0 20px;
        color: #888888;
      }

      .country-item {
        display: flex;
        justify-content: space-between;
        padding: 12px 20px;
        transition: all 0.2s ease;
        cursor: pointer;

        &:hover {
          background: #2c2c2c;
          transform: translateX(5px);
        }

        .left {
          font-size: 14px;
          color: #ffffff;
        }

        .right {
          font-size: 14px;
          color: #00a8ff;
          font-weight: 500;
        }
      }
    }
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

@keyframes slideUp {
  from {
    transform: translate(-50%, 100%);
  }
  to {
    transform: translate(-50%, 0);
  }
}

// 添加滚动条样式
.content {
  &::-webkit-scrollbar {
    width: 6px;
  }

  &::-webkit-scrollbar-track {
    background: #1a1a1a;
  }

  &::-webkit-scrollbar-thumb {
    background: #333333;
    border-radius: 3px;
    
    &:hover {
      background: #444444;
    }
  }
}
</style>
