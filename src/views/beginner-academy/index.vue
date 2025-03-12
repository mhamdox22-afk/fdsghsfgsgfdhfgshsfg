<template>
   <HeaderBar
      :currentName="_t18('menu_beginner_academy')"
    ></HeaderBar>
  <div class="index_topBannerContainer">
    <div class="index_imgContainer">
      <picture class="academy academy-picture index_picture">
        <source media="(max-width: 767px)" srcset="https://www.okx.com/cdn/assets/imgs/242/ED33801F54E6BD17.webp">
        <source media="(min-width: 768px) and (max-width: 1023px)" srcset="https://www.okx.com/cdn/assets/imgs/242/4A3BFFBA98BDE5D5.webp">
        <source media="(min-width: 1024px) and (max-width: 1269px)" srcset="https://www.okx.com/cdn/assets/imgs/242/06065C71DAF4040B.webp">
        <source media="(min-width: 1270px) and (max-width: 1859px)" srcset="https://www.okx.com/cdn/assets/imgs/242/0E745AE71F1C0FE8.webp">
        <img  class="academy-picture-cover index_img" src="https://www.okx.com/cdn/assets/imgs/242/0E745AE71F1C0FE8.webp" alt="在新手学院中学习数字货币交易">
      </picture>
    </div>
    <div class="index_overlay"></div>
    <div class="index_textContainer">
      <h1 class="index_title">{{ t('index_title') }}</h1>
      <div class="index_subtitle">{{ t('index_subtitle') }}</div>
    </div>
  </div>

  <div class="beginner-academy-container" style="margin-top: 30px;">
    <h1 class="page-title">{{ t('menu_beginner_academy') }}</h1>
    
    <div class="academy-categories">
      <div class="category-tab" 
           v-for="(category, index) in categories" 
           :key="index"
           :class="{ active: activeCategory === index }"
           @click="activeCategory = index">
        {{ t(category.name) }}
      </div>
    </div>
    
    <div class="academy-content">
      <div class="content-sidebar">
        <div class="sidebar-item" 
             v-for="(article, index) in categories[activeCategory].articles" 
             :key="index"
             :class="{ active: activeArticle === index }"
             @click="activeArticle = index">
          {{ t(article.title) }}
        </div>
      </div>
      
      <div class="content-main">
        <h2>{{ t(categories[activeCategory].articles[activeArticle].title) }}</h2>
        <div class="article-meta">
          <span class="article-date">{{ categories[activeCategory].articles[activeArticle].date }}</span>
          <span class="article-views">{{ categories[activeCategory].articles[activeArticle].views }} {{ t('views') }}</span>
        </div>
        
        <div class="article-content" v-html="t(categories[activeCategory].articles[activeArticle].content)"></div>
        
        <div class="article-navigation">
          <button class="nav-button" 
                  :disabled="activeArticle === 0"
                  @click="activeArticle > 0 ? activeArticle-- : null">
            {{ t('previous') }}
          </button>
          <button class="nav-button" 
                  :disabled="activeArticle === categories[activeCategory].articles.length - 1"
                  @click="activeArticle < categories[activeCategory].articles.length - 1 ? activeArticle++ : null">
            {{ t('next') }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'

const { t } = useI18n()
const activeCategory = ref(0)
const activeArticle = ref(0)

const categories = [
  {
    name: 'beginner_basics',
    articles: [
      {
        title: 'beginner_article_1_title',
        content: 'beginner_article_1_content',
        date: '2024-05-15',
        views: 1245
      },
      {
        title: 'beginner_article_2_title',
        content: 'beginner_article_2_content',
        date: '2024-05-20',
        views: 987
      },
      {
        title: 'beginner_article_3_title',
        content: 'beginner_article_3_content',
        date: '2025-05-25',
        views: 756
      }
    ]
  },
  {
    name: 'trading_guides',
    articles: [
      {
        title: 'trading_guide_1_title',
        content: 'trading_guide_1_content',
        date: '2023-06-01',
        views: 2345
      },
      {
        title: 'trading_guide_2_title',
        content: 'trading_guide_2_content',
        date: '2023-06-10',
        views: 1876
      }
    ]
  },
  {
    name: 'crypto_knowledge',
    articles: [
      {
        title: 'crypto_article_1_title',
        content: 'crypto_article_1_content',
        date: '2023-07-05',
        views: 3421
      },
      {
        title: 'crypto_article_2_title',
        content: 'crypto_article_2_content',
        date: '2023-07-15',
        views: 2198
      }
    ]
  }
]
</script>

<style scoped>
.beginner-academy-container {
  padding: 24px;
  max-width: 1200px;
  margin: 0 auto;
  color: #fff;
}

.page-title {
  font-size: 28px;
  margin-bottom: 32px;
  text-align: center;
}

.academy-categories {
  display: flex;
  gap: 16px;
  margin-bottom: 32px;
  flex-wrap: wrap;
}

.category-tab {
  padding: 12px 24px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.3s;
}

.category-tab:hover {
  background: rgba(255, 255, 255, 0.1);
}

.category-tab.active {
  background: linear-gradient(to right, #30CFD0, #c43ad6);
  color: #fff;
}

.academy-content {
  display: flex;
  gap: 24px;
}

.content-sidebar {
  width: 250px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 12px;
  padding: 16px;
}

.sidebar-item {
  padding: 12px;
  border-radius: 8px;
  cursor: pointer;
  margin-bottom: 8px;
  transition: background 0.3s;
}

.sidebar-item:hover {
  background: rgba(255, 255, 255, 0.1);
}

.sidebar-item.active {
  background: rgba(255, 255, 255, 0.1);
  color: #30CFD0;
}

.content-main {
  flex: 1;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 12px;
  padding: 24px;
}

.content-main h2 {
  font-size: 24px;
  margin-bottom: 16px;
}

.article-meta {
  display: flex;
  gap: 16px;
  color: #7A7A97;
  margin-bottom: 24px;
  font-size: 14px;
}

.article-content {
  line-height: 1.8;
  color: #e0e0e0;
  margin-bottom: 32px;
}

.article-navigation {
  display: flex;
  justify-content: space-between;
}

.nav-button {
  padding: 10px 20px;
  background: rgba(255, 255, 255, 0.1);
  border: none;
  border-radius: 8px;
  color: #fff;
  cursor: pointer;
  transition: background 0.3s;
}

.nav-button:hover:not(:disabled) {
  background: rgba(255, 255, 255, 0.2);
}

.nav-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

@media (max-width: 768px) {
  .academy-content {
    flex-direction: column;
  }
  
  .content-sidebar {
    width: 100%;
  }
}

.index_topBannerContainer {
  position: relative;
  overflow: hidden;
  top: 30px;
}

.index_imgContainer {
  width: 100%;
}

.academy-picture-cover {
  width: 100%;
  height: auto;

}

.index_overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
}

.index_textContainer {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: #fff;
  text-align: center;
}

.index_title {
  font-size: 36px;
  margin-bottom: 16px;
}

.index_subtitle {
  font-size: 18px;
}
</style> 