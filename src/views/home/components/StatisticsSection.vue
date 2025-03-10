<template>
  <div class="statistics-section">
    <div class="background-grid"></div>
    <h2 class="title animate" ref="titleRef">{{ $t('home.statistics.title') }}</h2>
    <div class="stats-container">
      <div class="stats-wrapper">
        <div class="stats-content">
          <div class="stat-item animate" ref="stat1">
            <div class="number-wrapper">
              <div class="number">1800</div>
              <div class="plus">{{ $t('home.statistics.plus') }}</div>
            </div>
            <div class="label">{{ $t('home.statistics.customers') }}</div>
            <div class="stat-circle"></div>
          </div>
          <div class="stat-item animate" ref="stat2">
            <div class="number-wrapper">
              <div class="number">190</div>
              <div class="plus">{{ $t('home.statistics.plus') }}</div>
            </div>
            <div class="label">{{ $t('home.statistics.countries') }}</div>
            <div class="stat-circle"></div>
          </div>
          <div class="stat-item animate" ref="stat3">
            <div class="number-wrapper">
              <div class="number">$207B</div>
              <div class="plus">{{ $t('home.statistics.plus') }}</div>
            </div>
            <div class="label">{{ $t('home.statistics.volume') }}</div>
            <div class="stat-circle"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useI18n } from 'vue-i18n'
import { ref, onMounted } from 'vue'

const { t } = useI18n()

const titleRef = ref(null)
const stat1 = ref(null)
const stat2 = ref(null)
const stat3 = ref(null)

onMounted(() => {
  const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        entry.target.classList.add('animate-active')
        
        if (entry.target.classList.contains('stat-item')) {
          const numberEl = entry.target.querySelector('.number')
          const finalValue = numberEl.textContent.replace(/[^0-9]/g, '')
          
          if (!isNaN(parseInt(finalValue))) {
            animateNumber(numberEl, 0, parseInt(finalValue), 2000)
          }
        }
        
        observer.unobserve(entry.target)
      }
    })
  }, {
    threshold: 0.2
  })

  ;[titleRef.value, stat1.value, stat2.value, stat3.value].forEach(el => {
    observer.observe(el)
  })
})

function animateNumber(element, start, end, duration) {
  let current = start
  const increment = (end - start) / (duration / 16)
  const timer = setInterval(() => {
    current += increment
    if (current >= end) {
      current = end
      clearInterval(timer)
    }
    if (element.textContent.includes('B')) {
      element.textContent = `$${Math.floor(current)}B`
    } else {
      element.textContent = Math.floor(current)
    }
  }, 16)
}
</script>

<style lang="scss" scoped>
.statistics-section {
  padding: 100px 24px;
  background: #0a0a0a;
  color: #fff;
  text-align: center;
  position: relative;
  overflow: hidden;
}

.background-grid {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: 
    linear-gradient(rgba(255,255,255,0.05) 1px, transparent 1px),
    linear-gradient(90deg, rgba(255,255,255,0.05) 1px, transparent 1px);
  background-size: 30px 30px;
  transform: perspective(500px) rotateX(30deg);
  transform-origin: center top;
  animation: grid-move 20s linear infinite;
}

@keyframes grid-move {
  from { background-position: 0 0; }
  to { background-position: 0 30px; }
}

.title {
  font-size: 32px;
  font-weight: 800;
  max-width: 700px;
  margin: 0 auto 80px;
  background: linear-gradient(to right, #fff 0%, #7000FF 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  position: relative;
  
  &::after {
    content: '';
    position: absolute;
    bottom: -20px;
    left: 50%;
    transform: translateX(-50%);
    width: 100px;
    height: 3px;
    background: linear-gradient(90deg, #00C4FF 0%, #7000FF 100%);
    border-radius: 2px;
  }
}

.stats-wrapper {
  position: relative;
  padding: 3px;
  border-radius: 30px;
  background: linear-gradient(90deg, #00C4FF 0%, #0074FF 51.04%, #7000FF 100%);
  box-shadow: 0 0 30px rgba(0, 196, 255, 0.2);
}

.stats-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 60px;
}

.stats-content {
  background: rgba(10, 10, 10, 0.95);
  border-radius: 28px;
  padding: 60px 20px;
  display: flex;
  justify-content: space-around;
  align-items: center;
  backdrop-filter: blur(10px);
  gap: 40px;
}

.stat-item {
  text-align: center;
  padding: 20px;
  position: relative;
  flex: 0 1 280px;
  
  .number-wrapper {
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 15px;
  }

  .number {
    font-size: 42px;
    font-weight: 800;
    background: linear-gradient(90deg, #00C4FF 0%, #7000FF 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
  }

  .plus {
    font-size: 20px;
    margin-left: 5px;
    background: linear-gradient(90deg, #00C4FF 0%, #7000FF 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
  }

  .label {
    font-size: 14px;
    color: #fff;
    font-weight: 500;
    text-transform: uppercase;
    letter-spacing: 1px;
  }

  .stat-circle {
    position: absolute;
    width: 150%;
    height: 150%;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    background: radial-gradient(circle, rgba(0,196,255,0.1) 0%, rgba(112,0,255,0) 70%);
    border-radius: 50%;
    z-index: -1;
    opacity: 0;
    transition: opacity 0.5s ease;
  }

  &:hover .stat-circle {
    opacity: 1;
  }
}

// 动画相关样式改进
.animate {
  opacity: 0;
  transform: translateY(50px);
  transition: all 1s cubic-bezier(0.4, 0, 0.2, 1);
  
  &.animate-active {
    opacity: 1;
    transform: translateY(0);
  }
}

.stat-item {
  &:nth-child(1) {
    transition-delay: 0.3s;
  }
  
  &:nth-child(2) {
    transition-delay: 0.6s;
  }
  
  &:nth-child(3) {
    transition-delay: 0.9s;
  }
}

@media (max-width: 768px) {
  .statistics-section {
    padding: 40px 16px;
  }

  .title {
    font-size: 24px;
    padding: 0 16px;
    margin-bottom: 40px;
    
    &::after {
      width: 60px;
      bottom: -15px;
    }
  }

  .stats-container {
    padding: 0;
  }

  .stats-wrapper {
    margin: 0 16px;
    padding: 2px;
  }

  .stats-content {
    padding: 24px 16px;
    flex-direction: column;
    gap: 32px;
  }

  .stat-item {
    padding: 16px;
    flex: none;
    width: 100%;
    
    .number-wrapper {
      margin-bottom: 10px;
    }
    
    .number {
      font-size: 36px;
    }
    
    .plus {
      font-size: 16px;
      margin-left: 4px;
    }
    
    .label {
      font-size: 12px;
      letter-spacing: 0.5px;
    }

    .stat-circle {
      width: 120%;
      height: 120%;
    }
  }

  .background-grid {
    background-size: 20px 20px;
  }

  @keyframes grid-move {
    from { background-position: 0 0; }
    to { background-position: 0 20px; }
  }
}

@media (max-width: 375px) {
  .statistics-section {
    padding: 32px 12px;
  }

  .title {
    font-size: 20px;
  }

  .stat-item {
    .number {
      font-size: 32px;
    }
    
    .plus {
      font-size: 14px;
    }
    
    .label {
      font-size: 11px;
    }
  }
}

@media (prefers-reduced-motion: reduce) {
  .animate {
    transition: none;
    opacity: 1;
    transform: none;
  }
  
  .background-grid {
    animation: none;
  }
}
</style> 