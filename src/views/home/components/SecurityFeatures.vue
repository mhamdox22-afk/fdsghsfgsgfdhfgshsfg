<template>
  <div class="security-features" ref="containerRef">
    <div class="features-grid">
      <!-- 储备金说明 -->
      <div class="feature-card animate__animated" :class="{ 'animate__fadeInUp': isVisible }" style="animation-delay: 0.2s">
        <div class="icon-wrapper">
          <div class="icon-circle">
            <div class="icon bitcoin-icon">
              <img src="@/assets/icons/bank-safe.png" alt="储备金">
            </div>
          </div>
        </div>
        <h3 class="feature-title">{{ $t('home.securityFeatures.mainFeatures.reserve.title') }}</h3>
        <p class="feature-desc">{{ $t('home.securityFeatures.mainFeatures.reserve.desc') }}</p>
      </div>

      <!-- 安全的冷储存 -->
      <div class="feature-card animate__animated" :class="{ 'animate__fadeInUp': isVisible }" style="animation-delay: 0.4s">
        <div class="icon-wrapper">
          <div class="icon-circle">
            <div class="icon storage-icon">
              <img src="@/assets/icons/database.png" alt="冷储存">
            </div>
          </div>
        </div>
        <h3 class="feature-title">{{ $t('home.securityFeatures.mainFeatures.storage.title') }}</h3>
        <p class="feature-desc">{{ $t('home.securityFeatures.mainFeatures.storage.desc') }}</p>
      </div>

      <!-- 用户保护基金 -->
      <div class="feature-card animate__animated" :class="{ 'animate__fadeInUp': isVisible }" style="animation-delay: 0.6s">
        <div class="icon-wrapper">
          <div class="icon-circle">
            <div class="icon shield-icon">
              <img src="@/assets/icons/security-checked.png" alt="保护基金">
            </div>
          </div>
        </div>
        <h3 class="feature-title">{{ $t('home.securityFeatures.mainFeatures.protection.title') }}</h3>
        <p class="feature-desc">{{ $t('home.securityFeatures.mainFeatures.protection.desc') }}</p>
      </div>
    </div>

    <div class="additional-features">
      <div class="feature-item animate__animated" :class="{ 'animate__fadeInLeft': isVisible }" style="animation-delay: 0.8s">
        <div class="feature-icon">
          <img src="@/assets/icons/fingerprint.png" alt="安全技术">
        </div>
        <div class="feature-content">
          <h4>{{ $t('home.securityFeatures.additionalFeatures.technology.title') }}</h4>
          <p>{{ $t('home.securityFeatures.additionalFeatures.technology.desc') }}</p>
        </div>
      </div>

      <div class="feature-item animate__animated" :class="{ 'animate__fadeInRight': isVisible }" style="animation-delay: 0.8s">
        <div class="feature-icon">
          <img src="@/assets/icons/customer-support.png" alt="用户理念">
        </div>
        <div class="feature-content">
          <h4>{{ $t('home.securityFeatures.additionalFeatures.philosophy.title') }}</h4>
          <p>{{ $t('home.securityFeatures.additionalFeatures.philosophy.desc') }}</p>
        </div>
      </div>

      <div class="feature-item animate__animated" :class="{ 'animate__fadeInLeft': isVisible }" style="animation-delay: 1s">
        <div class="feature-icon">
          <img src="@/assets/icons/folder.jpg" alt="全球运营">
        </div>
        <div class="feature-content">
          <h4>{{ $t('home.securityFeatures.additionalFeatures.operation.title') }}</h4>
          <p>{{ $t('home.securityFeatures.additionalFeatures.operation.desc') }}</p>
        </div>
      </div>

      <div class="feature-item animate__animated" :class="{ 'animate__fadeInRight': isVisible }" style="animation-delay: 1s">
        <div class="feature-icon">
          <img src="@/assets/icons/gift.png" alt="奖励活动">
        </div>
        <div class="feature-content">
          <h4>{{ $t('home.securityFeatures.additionalFeatures.rewards.title') }}</h4>
          <p>{{ $t('home.securityFeatures.additionalFeatures.rewards.desc') }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useI18n } from 'vue-i18n'
import { ref, onMounted } from 'vue'
import 'animate.css'

const { t } = useI18n()
const containerRef = ref(null)
const isVisible = ref(false)

onMounted(() => {
  const observer = new IntersectionObserver(
    (entries) => {
      if (entries[0].isIntersecting) {
        isVisible.value = true
        observer.disconnect()
      }
    },
    {
      threshold: 0.2
    }
  )

  if (containerRef.value) {
    observer.observe(containerRef.value)
  }
})
</script>

<style lang="scss" scoped>
.security-features {
  padding: 40px 20px;
  background: #0a0a0a;
  color: #fff;
}

.features-grid {
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 20px;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 40px;
  margin-bottom: 40px;

  @media (max-width: 768px) {
    grid-template-columns: 1fr;
    gap: 20px;
    padding: 0;
  }
}

.feature-card {
  text-align: center;
  padding: 30px 25px;
  background: #1a1a1a;
  border-radius: 16px;
  transition: transform 0.3s ease;
  opacity: 0;

  &.animate__fadeInUp {
    opacity: 1;
  }

  &:hover {
    transform: translateY(-10px);

    .icon-circle {
      &::after {
        transform: scale(1.1);
        opacity: 0.8;
      }
    }
  }

  @media (max-width: 768px) {
    padding: 20px 15px;
  }
}

.icon-wrapper {
  margin-bottom: 24px;
}

.icon-circle {
  width: 100px;
  height: 100px;
  margin: 0 auto;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;

  &::after {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    border-radius: 50%;
    background: linear-gradient(135deg, #00C4FF, #0074FF, #7000FF);
    opacity: 0.5;
    transition: all 0.3s ease;
    z-index: 1;
  }

  .icon {
    width: 70px;
    height: 70px;
    position: relative;
    z-index: 2;

    img {
      width: 100%;
      height: 100%;
      object-fit: contain;
    }
  }

  @media (max-width: 768px) {
    width: 80px;
    height: 80px;

    .icon {
      width: 50px;
      height: 50px;
    }
  }
}

.feature-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 12px;

  @media (max-width: 768px) {
    font-size: 14px;
  }
}

.feature-desc {
  color: #888;
  line-height: 1.5;
  font-size: 12px;
  max-width: 250px;
  margin: 0 auto;

  @media (max-width: 768px) {
    font-size: 11px;
    max-width: 100%;
  }
}

.additional-features {
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 20px;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 40px;

  @media (max-width: 768px) {
    grid-template-columns: 1fr;
    gap: 20px;
    padding: 0;
  }
}

.feature-item {
  display: flex;
  align-items: flex-start;
  gap: 16px;
  padding: 24px 30px;
  background: #1a1a1a;
  border-radius: 14px;
  transition: transform 0.3s ease;
  opacity: 0;

  &.animate__fadeInLeft,
  &.animate__fadeInRight {
    opacity: 1;
  }

  &:hover {
    transform: translateY(-5px);
  }

  @media (max-width: 768px) {
    padding: 16px 20px;
  }
}

.feature-icon {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #00C4FF, #0074FF);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;

  img {
    width: 100%;
    height: 100%;
    object-fit: contain;
  }

  @media (max-width: 768px) {
    width: 32px;
    height: 32px;
  }
}

.feature-content {
  h4 {
    font-size: 14px;
    font-weight: 500;
    margin-bottom: 8px;

    @media (max-width: 768px) {
      font-size: 13px;
    }
  }

  p {
    color: #888;
    line-height: 1.5;
    font-size: 11px;
    max-width: 300px;

    @media (max-width: 768px) {
      font-size: 10px;
      max-width: 100%;
    }
  }
}

// 图标字体
@font-face {
  font-family: 'security-icons';
  // src: url('@/assets/fonts/security-icons.woff2') format('woff2');
}

[class^="icon-"] {
  font-family: 'security-icons' !important;
  font-style: normal;
  font-weight: normal;
  font-variant: normal;
  text-transform: none;
  line-height: 1;
}

.icon-fingerprint:before { content: "\e900"; }
.icon-support:before { content: "\e901"; }
.icon-globe:before { content: "\e902"; }
.icon-gift:before { content: "\e903"; }
</style> 