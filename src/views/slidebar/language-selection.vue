<template>
  <div class="bind-card">
    <!-- 导航条 -->
    <HeaderBar
      :currentName="_t18('sidebar_language')"
      :cuttentRight="cuttentRight"
      :border_bottom="true"
    ></HeaderBar>
    <!--内容-->
    <div class="content">
      <!-- 选择语言 -->
      <!-- <div>{{$t('sidebar_language_title')}}</div> -->
      <div class="title">{{ _t18('sidebar_language') }}</div>
      <van-radio-group v-model="checked" class="language-list">
        <van-radio
          :name="index"
          shape="square"
          v-for="(item, index) in languageList"
          :key="index"
          label-position="left"
          :checked-color="'#17AC74'"
          @click="setLanguage(item)"
          class="language-item"
        >
          <div class="language-icon">
            <img :src="getLanguageIcon(item.remark)" :alt="item.remark" class="nation-icon">
          </div>
          <span class="language-name">{{ item.remark }}</span>
          <template #icon="props">
            <svg-load :name="props.checked ? `gou-yuyan` : `gou-yuyanno`" class="check-icon"></svg-load>
          </template>
        </van-radio>
      </van-radio-group>
    </div>
  </div>
</template>
<script setup>
import { storageDict } from '@/config/dict'
import { useMainStore } from '@/store/index.js'
import { computed } from 'vue'
const mainStore = useMainStore()
const current = ref(localStorage.getItem(storageDict.LANGUAGE))
/**
 * 设置语言
 */
const setLanguage = (item) => {
  mainStore.setLanguage(item.dictValue)
  current.value = item.dictValue
}

// const checked = ref(5)
const checked = computed(() => {
  let temp = 0
  mainStore.languageList.forEach((item, index) => {
    if (item.dictValue == current.value) {
      temp = index
    }
  })
  return temp
})
// const languageList = reactive([
//   { i18n: 'zh', index: '1', name: '中文', icon: 'zhongguo' },
//   { i18n: 'ko', index: '2', name: '한국인', icon: 'hanguo' },
//   { i18n: 'ja', index: '3', name: '日本語', icon: 'riben' },
//   { i18n: 'en', index: '4', name: 'English', icon: 'meiguo' },
//   { i18n: 'vi', index: '5', name: 'Tiếng Việt', icon: 'yuenan' },
//   { i18n: 'th', index: '6', name: 'ไทย', icon: 'taiguo' }
// ])
const languageList = mainStore.languageList
const cuttentRight = { iconRight: [{ iconName: 'kefu', clickTo: 'event_serviceChange' }] }

// 修改为图片路径映射
const languageIconMap = {
  'English': '/src/assets/language-img/en.png',
  '日本語': '/src/assets/language-img/jp.png',
  '한국인': '/src/assets/language-img/kr.png',
  '中文（简体）': '/src/assets/language-img/cn.png',
  'Português': '/src/assets/language-img/pu.jpg',
  'Tiếng Việt': '/src/assets/language-img/tv.png',
  'ไทย': '/src/assets/language-img/dubi.png',
  'español': '/src/assets/language-img/es.jpg',
  'Français': '/src/assets/language-img/fr.png',
  'Русский язык': '/src/assets/language-img/ru.jpeg',
  'Deutsch': '/src/assets/language-img/de.jpg',
  'Italian': '/src/assets/language-img/it.png',
  '中文（繁體）': '/src/assets/language-img/hk.png'
}

// 获取语言对应的图标
const getLanguageIcon = (languageName) => {
  console.log(languageName)
  return languageIconMap[languageName] || '/src/assets/language-img/default.png'
}
</script>
<style lang="scss" scoped>
.bind-card {
  min-height: 100vh;
  background: linear-gradient(145deg, #1a1a1a, #2d2d2d);
}

.content {
  padding: 30px 20px;
  
  .title {
    font-size: 24px;
    font-weight: 600;
    color: #ffffff;
    margin-bottom: 30px;
    text-transform: uppercase;
    letter-spacing: 1px;
  }

  .language-list {
    display: grid;
    gap: 15px;
  }

  .language-item {
    background: rgba(255, 255, 255, 0.05);
    border-radius: 12px;
    padding: 20px;
    transition: all 0.3s ease;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
    
    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
      background: rgba(255, 255, 255, 0.08);
    }

    .language-icon {
      display: flex;
      align-items: center;
      margin-right: 15px;

      .nation-icon {
        width: 24px;
        height: 24px;
        object-fit: contain;
        border-radius: 50%; // 如果想要圆形图标
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      }
    }

    .language-name {
      font-size: 16px;
      color: #ffffff;
      font-weight: 500;
    }

    .check-icon {
      color: #17AC74;
      filter: drop-shadow(0 2px 4px rgba(23, 172, 116, 0.3));
    }
  }

  :deep(.van-radio) {
    display: flex;
    align-items: center;
    width: 100%;
    margin: 0;
    
    .van-radio__label {
      flex: 1;
      color: #ffffff;
      display: flex;
      align-items: center;
    }
  }
}

// 添加动画
.language-item {
  animation: fadeInUp 0.5s ease forwards;
  opacity: 0;
  @for $i from 1 through 10 {
    &:nth-child(#{$i}) {
      animation-delay: $i * 0.1s;
    }
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
</style>

