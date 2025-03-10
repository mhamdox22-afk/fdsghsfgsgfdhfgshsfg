<template>
  <div class="box">
    <div class="menu">
      <image-load
        filePath="https://tg-mahalebi.oss-cn-hongkong.aliyuncs.com/mahalebi/5cc16eb1f8b0439a8ff46ef720ed9147.png?2.0.1741576377112"
        class="lang img"
        @click="$router.push('/language-selection')"
      />
      
      <div>
        <p  style="font-size: 16px !important;"  v-if="props.type == 0">{{ _t18('login_title') }} {{ _getConfig('_APP_META_TITLE') }}</p>
        <p style="font-size: 16px !important;"  v-if="props.type == 1">{{ _t18('register_title') }} {{ _getConfig('_APP_META_TITLE') }}</p>
      </div>
      
      <svg-load name="guanbi" class="guanbi" @click="$router.push('/')"></svg-load>

    </div>
  </div>
</template>

<script setup>
import { _t18 } from '@/utils/public'
import { useMainStore } from '@/store/index'
import { computed } from 'vue'
const mainStore = useMainStore()
/**
 * 当前语言实例
 */
const currentLanguage = computed(() =>
  mainStore.languageList.find((elem) => elem.dictValue == mainStore.language)
)
const props = defineProps({
  type: {
    type: Number
  }
})
</script>

<style lang="scss" scoped>
* {
  font-size: 14px;
  color: #fff;
  transition: all 0.3s ease;
}
.box {
  padding: 5px 10px;
  width: 100%;
  background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 100%);
  border-radius: 0 0 25px 25px;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.4);
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: -50%;
    left: -50%;
    width: 200%;
    height: 200%;
    background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0) 70%);
    opacity: 0;
    transform: scale(0.5);
    animation: pulse 8s infinite;
    pointer-events: none;
  }
  
  .menu {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 10px;
    
    .guanbi {
      font-size: 26px;
      color: #fff;
      opacity: 0.8;
      transform: scale(1);
      transition: all 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275);
      
      &:active {
        transform: scale(0.9);
        opacity: 1;
      }
    }
    
    .lang {
      width: 26px;
      height: 26px;
      border-radius: 50%;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
      transform: scale(1);
      transition: all 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275);
      
      &:active {
        transform: scale(0.9);
      }
    }
  }
  
  .top {
    padding-top: 30px;
    animation: fadeIn 0.8s ease-out;
    
    .left {
      position: relative;
      display: inline-block;
      
      &::after {
        content: '';
        position: absolute;
        bottom: -8px;
        left: 0;
        width: 40px;
        height: 3px;
        background: linear-gradient(90deg, #3498db, #5352ed);
        border-radius: 3px;
      }
    }
  }
  
  .bottom {
    margin-top: 15px;
    animation: slideUp 0.6s ease-out 0.3s both;
  }
  
  p {
    font-size: 28px;
    font-weight: 600;
    line-height: 1.4;
    margin: 0;
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
    background: linear-gradient(90deg, #ffffff, #cccccc);
    -webkit-background-clip: text;
    background-clip: text;
    -webkit-text-fill-color: transparent;
    letter-spacing: 0.5px;
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
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

@keyframes pulse {
  0% {
    opacity: 0;
    transform: scale(0.5);
  }
  50% {
    opacity: 0.1;
    transform: scale(1);
  }
  100% {
    opacity: 0;
    transform: scale(0.5);
  }
}
</style>
