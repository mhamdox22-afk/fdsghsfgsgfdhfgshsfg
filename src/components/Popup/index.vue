<!-- 弹窗 -->
<script setup>
let props = defineProps({
  show: {
    type: Boolean,
    default: false
  },
  close: {
    type: Boolean,
    default: true
  },
  title: {
    type: String
  },
  content: {
    type: Object
  },
  direction: {
    type: String,
    default: 'center'
  }
})

let emit = defineEmits(['handelClose'])
const handelClose = () => {
  emit('handelClose', false)
}
</script>

<template>
  <div>
    <van-popup
      v-model:show="props.show"
      round
      :transition-appear="true"
      @click-overlay="handelClose"
      :position="direction"
    >
      <div class="popup">
        <!-- 标题 -->
        <p>{{ props.title }}</p>
        <!-- 关闭按钮 -->
        <svg-load class="closeSvg" name="guanbi" v-if="props.close" @click="handelClose"></svg-load>
        <!-- 内容 仅适用于规则/说明-->
        <div v-if="props.content">
          <div v-for="(item, index) in content" :key="index" class="popupContent">
            <div v-if="item.content" class="content1" v-html="item.content"></div>
          </div>
        </div>
        <slot name="content2"></slot>
      </div>
    </van-popup>
  </div>
</template>

<style lang="scss" scoped>
.popup {
  display: flex;
  width: 100%;
  flex-direction: column;
  position: relative;
  background: #1a1a1a; // 深色背景
  color: #ffffff; // 白色文字
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3); // 添加阴影
  animation: slideUp 0.3s ease-out; // 添加入场动画

  > * {
    font-size: 14px;
    color: #ffffff; // 确保所有直接子元素文字为白色
  }

  & > p {
    font-size: 18px;
    position: absolute;
    top: 20px;
    left: 50%;
    transform: translateX(-50%);
    color: #ffffff;
    font-weight: 600;
    letter-spacing: 0.5px; // 增加字间距提高可读性
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
    transition: all 0.3s ease;
  }

  .closeSvg {
    font-size: 24px;
    position: absolute;
    top: 15px;
    right: 20px;
    color: #ffffff;
    opacity: 0.8;
    transition: all 0.3s ease;
    cursor: pointer;

    &:hover {
      opacity: 1;
      transform: rotate(90deg);
    }
  }

  & > div {
    padding-top: 50px;
  }

  .popupContent {
    padding: 15px 20px;
    background: #2a2a2a; // 稍微浅一点的背景色
    margin: 10px;
    border-radius: 12px;
    transition: all 0.3s ease;
    
    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
    }

    .title {
      font-size: 16px;
      margin-bottom: 15px;
      font-weight: bold;
      color: #ffffff;
      letter-spacing: 0.5px;
    }

    .content1 {
      font-size: 12px;
      padding: 10px 15px;
      color: rgba(255, 255, 255, 0.95); // 提高对比度
      line-height: 24px;
      word-wrap: break-word;

      & > div {
        p {
          display: flex;
          align-items: center;
          font-size: 14px;
          color: #ffffff;
          letter-spacing: 0.3px;
          
          .square {
            font-size: 6px;
            margin-right: 5px;
            color: #4a9eff; // 突出显示的蓝色
          }
        }

        div {
          color: rgba(255, 255, 255, 0.85); // 提高对比度
          line-height: 1.5;
          margin: 12px 0;
          padding-left: 15px;
          border-left: 2px solid rgba(74, 158, 255, 0.3); // 左边框装饰
        }
      }
    }

    .content2 {
      .note {
        margin-bottom: 5px;
        font-size: 12px;
        line-height: 1.5;
        color: rgba(255, 255, 255, 0.8); // 提高对比度
      }
    }
  }
}

// 弹窗动画
@keyframes slideUp {
  from {
    transform: translateY(100px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

// 深色主题下的弹窗样式
:deep(.van-popup--bottom) {
  max-width: var(--ex-max-width);
  max-height: 80%;
  margin: auto;
  left: 0;
  right: 0;
  background: #1a1a1a;
  box-shadow: 0 -8px 32px rgba(0, 0, 0, 0.4);
  border-top-left-radius: 20px;
  border-top-right-radius: 20px;
}

:deep(.van-popup) {
  max-width: var(--ex-max-width);
  background: #1a1a1a;
  border-radius: 20px;
  overflow: hidden;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  
  // 强制所有文本为白色
  * {
    color: #ffffff;
  }
  
  .popup {
    color: #ffffff;
    
    p, div, span {
      color: #ffffff;
    }
    
    .content1 {
      color: rgba(255, 255, 255, 0.95) !important;
      
      * {
        color: #ffffff;
      }
    }
  }
}

// 确保弹窗内所有文本都是白色
:deep(.popup) {
  color: #ffffff !important;
  
  p, div, span {
    color: #ffffff !important;
  }
  
  .content1 {
    color: rgba(255, 255, 255, 0.95) !important;
  }
  
  .content2 {
    color: rgba(255, 255, 255, 0.95) !important;
  }
}
</style>
