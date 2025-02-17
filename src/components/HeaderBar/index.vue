<template>
  <div class="placeholder" v-if="topHeight"></div>
  <van-sticky>
    <header
      :class="[border_bottom ? 'border_bottom' : '', 'header-animate']"
      :style="{ 'background-color': bgColor }"
    >
      <div class="left">
        <svg-load
          v-if="backTo"
          name="jiantou-z"
          class="leftImg"
          @click="$router.push(backTo)"
        ></svg-load>
        <svg-load
          v-else-if="backwardNum != -1"
          name="jiantou-z"
          class="leftImg"
          @click="$router.go(backwardNum)"
        ></svg-load>
        <svg-load v-else name="jiantou-z" class="leftImg" @click="_back()"></svg-load>
        <div :class="isCenter ? 'centerStyle' : ''">{{ currentName }}</div>
        <slot name="titleName"></slot>
      </div>
      <div class="right">
        <div class="rightBtn" v-if="JSON.stringify(cuttentRight) !== '{}'">
          <div v-if="cuttentRight.name" @click="linkTo">
            {{ cuttentRight.name }}
          </div>
          <image-load
            v-if="cuttentRight.icon"
            :filePath="cuttentRight.icon"
            class="rightBtnImg"
            @click="linkTo"
          />

          <div
            v-show="cuttentRight.iconRight"
            v-for="(item, index) in cuttentRight.iconRight"
            :key="index"
          >
            <svg-load
              :name="item.iconName"
              class="rightIcon"
              @click="handelClick(item.clickTo)"
            ></svg-load>
          </div>
        </div>
        <slot name="footer"></slot>
      </div>
    </header>
  </van-sticky>
</template>
<script setup>
import { dispatchCustomEvent } from '@/utils'
import { useRouter } from 'vue-router'
const router = useRouter()
const props = defineProps({
  backTo: {
    type: String
  },
  //子组件接收父组件传递过来的值
  backwardNum: {
    type: Number,
    default: -1
  },
  currentName: {
    type: String,
    default: ''
  },
  cuttentRight: {
    type: Object,
    default: {}
  },
  border_bottom: {
    type: Boolean,
    default: true
  },
  topHeight: {
    type: Boolean,
    default: true
  },
  bgColor: {
    type: String,
    default: '#1a1a1a'
  },
  isCenter: {
    type: Boolean,
    default: false
  }
})
const emit = defineEmits(['linkTo', 'showPopup', 'del'])
const linkTo = () => {
  emit('linkTo')
}
const showPopup = () => {
  emit('showPopup')
}
let handelClick = (item) => {
  if (item == 'event_serviceChange') {
    dispatchCustomEvent('event_serviceChange')
  } else if (item == 'del') {
    emit('del')
  } else if (item) {
    router.push(item)
  } else {
    showPopup()
  }
}
</script>
<style lang="scss" scoped>
.placeholder {
  height: 61px;
}

@keyframes headerSlideDown {
  from {
    transform: translateY(-100%);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

header.border_bottom {
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

header {
  position: fixed;
  top: 0;
  width: 100%;
  max-width: var(--ex-max-width);
  display: flex;
  height: 60px;
  padding: 0 15px;
  align-items: center;
  background-color: rgba(26, 26, 26, 0.95) !important;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(10px);
  z-index: 99;
  animation: headerSlideDown 0.3s ease-out;

  .left {
    display: flex;
    align-items: center;
    font-size: 16px;
    font-weight: normal;
    color: #ffffff;
    
    div {
      font-size: 16px;
      transition: all 0.3s ease;
      
      &:hover {
        text-shadow: 0 0 10px rgba(255, 255, 255, 0.5);
      }
    }
    
    .leftImg {
      width: 16px;
      height: 12px;
      margin-right: 15px;
      filter: invert(1);
      transition: transform 0.3s ease;
      
      &:hover {
        transform: translateX(-3px);
      }
    }
  }

  .right {
    flex: 1;
    
    .rightBtn {
      display: flex;
      justify-content: flex-end;
      align-items: center;
      color: rgba(255, 255, 255, 0.9);
      font-size: 14px;
      
      div {
        transition: all 0.3s ease;
        
        &:hover {
          color: #ffffff;
          text-shadow: 0 0 10px rgba(255, 255, 255, 0.5);
        }
      }
      
      .rightBtnImg {
        width: 24px;
        height: 24px;
        transition: transform 0.3s ease;
        
        &:hover {
          transform: scale(1.1);
        }
      }
      
      .rightIcon {
        margin-left: 20px;
        font-size: 24px;
        color: rgba(255, 255, 255, 0.9);
        transition: all 0.3s ease;
        cursor: pointer;
        
        &:hover {
          color: #ffffff;
          transform: scale(1.1);
          filter: drop-shadow(0 0 5px rgba(255, 255, 255, 0.5));
        }
      }
    }
  }
}

.centerStyle {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  color: #ffffff;
  font-weight: 500;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
  
  &:hover {
    text-shadow: 0 0 10px rgba(255, 255, 255, 0.5);
  }
}

@media (prefers-color-scheme: dark) {
  header {
    background-color: rgba(26, 26, 26, 0.95) !important;
  }
}
</style>
