<template>
  <div class="crypto-composition">
    <div class="background-glow"></div>
    
    <h2 class="title animate__animated opacity-0" ref="titleRef">
      {{ $t('home.cryptoComposition.title') }}
    </h2>
    
    <div class="composition-container">
      <div class="floating-coins">
        <!-- 左侧浮动币种 -->
        <div class="coin-group left">
          <div class="coin animate__animated opacity-0" 
               v-for="(coin, index) in leftCoins" 
               :key="index"
               :ref="el => { if (el) leftCoinRefs[index] = el }"
               :style="{ 
                 animation: `float-${coin.name} ${coin.duration}s ease-in-out infinite`,
                 animationDelay: `${coin.delay}s`,
                 top: `${coin.top}%`,
                 left: `${coin.left}%`,
                 animationFillMode: 'both'
               }">
            <div class="coin-glow"></div>
            <img :src="coin.icon" :alt="coin.name">
          </div>
        </div>
        
        <!-- 右侧浮动币种 -->
        <div class="coin-group right">
          <div class="coin animate__animated opacity-0" 
               v-for="(coin, index) in rightCoins" 
               :key="index"
               :ref="el => { if (el) rightCoinRefs[index] = el }"
               :style="{ 
                 animation: `float-${coin.name} ${coin.duration}s ease-in-out infinite`,
                 animationDelay: `${coin.delay}s`,
                 top: `${coin.top}%`,
                 right: `${coin.right}%`,
                 animationFillMode: 'both'
               }">
            <div class="coin-glow"></div>
            <img :src="coin.icon" :alt="coin.name">
          </div>
        </div>
      </div>
      
      <div class="action-button">
        <button class="cta-button animate__animated opacity-0" 
                ref="ctaButtonRef"
                @click="goToTrading">
          <span class="button-text">{{ $t('home.cryptoComposition.cta') }}</span>
          <div class="button-glow"></div>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useI18n } from 'vue-i18n'
import { ref, onMounted } from 'vue'
import 'animate.css'
import { useRouter } from 'vue-router'

const { t } = useI18n()

const router = useRouter()

const leftCoins = [
  {
    name: 'BTC',
    icon: 'https://cryptologos.cc/logos/bitcoin-btc-logo.png',
    duration: 4,
    delay: 0,
    top: 15,
    left: 10
  },
  {
    name: 'ETH',
    icon: 'https://cryptologos.cc/logos/ethereum-eth-logo.png',
    duration: 5,
    delay: 0.5,
    top: 35,
    left: 25
  },
  {
    name: 'DOT',
    icon: 'https://cryptologos.cc/logos/polkadot-new-dot-logo.png',
    duration: 4.5,
    delay: 1,
    top: 55,
    left: 15
  },
  {
    name: 'BNB',
    icon: 'https://cryptologos.cc/logos/bnb-bnb-logo.png',
    duration: 4.8,
    delay: 0.7,
    top: 75,
    left: 20
  },
  {
    name: 'SOL',
    icon: 'https://cryptologos.cc/logos/solana-sol-logo.png',
    duration: 5.2,
    delay: 1.3,
    top: 25,
    left: 35
  },
  {
    name: 'AVAX',
    icon: 'https://cryptologos.cc/logos/avalanche-avax-logo.png',
    duration: 4.6,
    delay: 0.9,
    top: 65,
    left: 30
  }
]

const rightCoins = [
  {
    name: 'XRP',
    icon: 'https://cryptologos.cc/logos/xrp-xrp-logo.png',
    duration: 4.2,
    delay: 0.3,
    top: 20,
    right: 20
  },
  {
    name: 'ADA',
    icon: 'https://cryptologos.cc/logos/cardano-ada-logo.png',
    duration: 4.8,
    delay: 0.8,
    top: 40,
    right: 15
  },
  {
    name: 'DOGE',
    icon: 'https://cryptologos.cc/logos/dogecoin-doge-logo.png',
    duration: 4.4,
    delay: 1.2,
    top: 60,
    right: 25
  },
  {
    name: 'MATIC',
    icon: 'https://cryptologos.cc/logos/polygon-matic-logo.png',
    duration: 5.1,
    delay: 0.6,
    top: 30,
    right: 35
  },
  {
    name: 'LINK',
    icon: 'https://cryptologos.cc/logos/chainlink-link-logo.png',
    duration: 4.7,
    delay: 1.5,
    top: 70,
    right: 18
  },
  {
    name: 'UNI',
    icon: 'https://cryptologos.cc/logos/uniswap-uni-logo.png',
    duration: 4.9,
    delay: 1.1,
    top: 50,
    right: 30
  }
]

const goToTrading = () => {
  router.push('/trade')
}

// 创建 refs
const titleRef = ref(null)
const ctaButtonRef = ref(null)
const leftCoinRefs = ref([])
const rightCoinRefs = ref([])

// 动画序列函数
const animateElement = (element, animation, delay = 0) => {
  setTimeout(() => {
    element.classList.remove('opacity-0')
    element.classList.add(animation)
  }, delay)
}

// 设置观察器
onMounted(() => {
  const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        // 标题动画
        animateElement(titleRef.value, 'animate__fadeInDown')
        
        // 左侧币种动画
        leftCoinRefs.value.forEach((coin, index) => {
          animateElement(coin, 'animate__fadeInLeft', 200 * (index + 1))
        })
        
        // 右侧币种动画
        rightCoinRefs.value.forEach((coin, index) => {
          animateElement(coin, 'animate__fadeInRight', 200 * (index + 1))
        })
        
        // CTA按钮动画
        animateElement(ctaButtonRef.value, 'animate__bounceIn', 1000)
        
        // 动画完成后取消观察
        observer.disconnect()
      }
    })
  }, {
    threshold: 0.2
  })

  // 开始观察
  observer.observe(titleRef.value)
})
</script>

<style lang="scss" scoped>
.crypto-composition {
  padding: 120px 120px;
  background: #0a0a0a;
  color: #fff;
  overflow: hidden;
  position: relative;
  min-height: 600px;
}

.background-glow {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 60%;
  height: 60%;
  background: radial-gradient(circle at center, 
    rgba(0, 116, 255, 0.15),
    rgba(0, 196, 255, 0.1),
    transparent 70%);
  filter: blur(80px);
  pointer-events: none;
}

.title {
  text-align: center;
  font-size: 36px;
  font-weight: 800;
  margin-bottom: 80px;
  max-width: 1000px;
  margin-left: auto;
  margin-right: auto;
  background: linear-gradient(to right, #fff 0%, #7aa7ff 50%, #fff 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  letter-spacing: 1px;
  text-shadow: 0 0 30px rgba(255, 255, 255, 0.1);
}

.composition-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 100px;
  position: relative;
  height: 450px;
}

.floating-coins {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
}

.coin-group {
  position: absolute;
  width: 50%;
  height: 100%;
  
  &.left {
    left: 0;
  }
  
  &.right {
    right: 0;
  }
}

.coin {
  position: absolute;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(5px);
  padding: 8px;
  box-shadow: 0 4px 30px rgba(0, 116, 255, 0.3);
  will-change: transform;
  
  &.animate__animated {
    animation-duration: inherit;
    animation-delay: inherit;
  }
  
  .coin-glow {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 100%;
    height: 100%;
    background: radial-gradient(circle at center,
      rgba(255, 255, 255, 0.8),
      transparent 70%);
    opacity: 0;
    transition: opacity 0.3s ease;
  }
  
  img {
    width: 100%;
    height: 100%;
    object-fit: contain;
    position: relative;
    z-index: 1;
  }
  
  &:hover {
    transform: scale(1.3) translateY(-10px);
    box-shadow: 0 8px 40px rgba(0, 116, 255, 0.5);
    animation-play-state: paused;
    
    .coin-glow {
      opacity: 0.2;
    }
  }
}

// 更新浮动动画为持续运动
@keyframes float-BTC {
  0%, 100% { transform: translate(0, 0) rotate(0deg); }
  25% { transform: translate(15px, -25px) rotate(8deg); }
  50% { transform: translate(0, -40px) rotate(0deg); }
  75% { transform: translate(-15px, -25px) rotate(-8deg); }
}

@keyframes float-ETH {
  0%, 100% { transform: translate(0, 0) rotate(0deg); }
  33% { transform: translate(-20px, -30px) rotate(-10deg); }
  66% { transform: translate(20px, -45px) rotate(10deg); }
}

@keyframes float-DOT {
  0% { transform: translate(0, 0) rotate(0deg); }
  20% { transform: translate(10px, -20px) rotate(5deg); }
  40% { transform: translate(20px, -35px) rotate(10deg); }
  60% { transform: translate(10px, -50px) rotate(5deg); }
  80% { transform: translate(-10px, -35px) rotate(-5deg); }
  100% { transform: translate(0, 0) rotate(0deg); }
}

@keyframes float-BNB {
  0% { transform: translate(0, 0) rotate(0deg); }
  25% { transform: translate(-15px, -28px) rotate(-10deg); }
  50% { transform: translate(0, -42px) rotate(0deg); }
  75% { transform: translate(15px, -28px) rotate(10deg); }
  100% { transform: translate(0, 0) rotate(0deg); }
}

@keyframes float-SOL {
  0% { transform: translate(0, 0) rotate(0deg); }
  30% { transform: translate(20px, -22px) rotate(8deg); }
  60% { transform: translate(-20px, -38px) rotate(-8deg); }
  100% { transform: translate(0, 0) rotate(0deg); }
}

@keyframes float-AVAX {
  0% { transform: translate(0, 0) rotate(0deg); }
  25% { transform: translate(-10px, -26px) rotate(-7deg); }
  50% { transform: translate(0, -40px) rotate(0deg); }
  75% { transform: translate(10px, -26px) rotate(7deg); }
  100% { transform: translate(0, 0) rotate(0deg); }
}

@keyframes float-XRP {
  0% { transform: translate(0, 0) rotate(0deg); }
  33% { transform: translate(15px, -24px) rotate(9deg); }
  66% { transform: translate(-15px, -36px) rotate(-9deg); }
  100% { transform: translate(0, 0) rotate(0deg); }
}

@keyframes float-ADA {
  0% { transform: translate(0, 0) rotate(0deg); }
  20% { transform: translate(-20px, -27px) rotate(-6deg); }
  40% { transform: translate(0, -45px) rotate(0deg); }
  60% { transform: translate(20px, -27px) rotate(6deg); }
  80% { transform: translate(0, -10px) rotate(0deg); }
  100% { transform: translate(0, 0) rotate(0deg); }
}

@keyframes float-DOGE {
  0% { transform: translate(0, 0) rotate(0deg); }
  25% { transform: translate(10px, -23px) rotate(7deg); }
  50% { transform: translate(0, -35px) rotate(0deg); }
  75% { transform: translate(-10px, -23px) rotate(-7deg); }
  100% { transform: translate(0, 0) rotate(0deg); }
}

@keyframes float-MATIC {
  0% { transform: translate(0, 0) rotate(0deg); }
  20% { transform: translate(-15px, -29px) rotate(-9deg); }
  40% { transform: translate(15px, -40px) rotate(9deg); }
  60% { transform: translate(0, -50px) rotate(0deg); }
  80% { transform: translate(-15px, -29px) rotate(-9deg); }
  100% { transform: translate(0, 0) rotate(0deg); }
}

@keyframes float-LINK {
  0% { transform: translate(0, 0) rotate(0deg); }
  33% { transform: translate(20px, -21px) rotate(5deg); }
  66% { transform: translate(-20px, -32px) rotate(-5deg); }
  100% { transform: translate(0, 0) rotate(0deg); }
}

@keyframes float-UNI {
  0% { transform: translate(0, 0) rotate(0deg); }
  25% { transform: translate(-10px, -25px) rotate(-8deg); }
  50% { transform: translate(10px, -38px) rotate(8deg); }
  75% { transform: translate(-10px, -25px) rotate(-8deg); }
  100% { transform: translate(0, 0) rotate(0deg); }
}

.action-button {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  z-index: 1;
}

.cta-button {
  background: linear-gradient(135deg, #00C4FF, #0074FF);
  border: none;
  border-radius: 30px;
  color: #fff;
  font-size: 16px;
  padding: 16px 40px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  
  .button-text {
    position: relative;
    z-index: 1;
    font-weight: 600;
    letter-spacing: 0.5px;
  }
  
  .button-glow {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: linear-gradient(135deg, 
      rgba(255, 255, 255, 0.4),
      rgba(255, 255, 255, 0.1));
    transform: translateX(-100%);
    transition: transform 0.4s ease;
  }

  &:hover {
    transform: translateY(-3px) scale(1.05);
    box-shadow: 0 10px 30px rgba(0, 196, 255, 0.4);
    
    .button-glow {
      transform: translateX(100%);
    }
  }
}

@media (max-width: 768px) {
  .crypto-composition {
    padding: 60px 20px;
    min-height: 500px;
  }

  .title {
    font-size: 24px;
    margin-bottom: 40px;
    padding: 0 15px;
  }

  .composition-container {
    height: 400px;
    padding: 0 20px;
  }

  .coin {
    width: 32px;
    height: 32px;
    padding: 6px;

    &:hover {
      transform: scale(1.15) translateY(-5px);
    }
  }

  // 调整左侧币种位置
  .coin-group.left .coin {
    &:nth-child(1) { left: 5%; }
    &:nth-child(2) { left: 20%; }
    &:nth-child(3) { left: 8%; }
    &:nth-child(4) { left: 15%; }
    &:nth-child(5) { left: 25%; }
    &:nth-child(6) { left: 18%; }
  }

  // 调整右侧币种位置
  .coin-group.right .coin {
    &:nth-child(1) { right: 5%; }
    &:nth-child(2) { right: 20%; }
    &:nth-child(3) { right: 8%; }
    &:nth-child(4) { right: 15%; }
    &:nth-child(5) { right: 25%; }
    &:nth-child(6) { right: 18%; }
  }

  .cta-button {
    font-size: 14px;
    padding: 12px 28px;
    
    &:hover {
      transform: translateY(-2px) scale(1.02);
    }
  }

  .background-glow {
    width: 90%;
    height: 90%;
    filter: blur(60px);
  }
}

// 添加更小屏幕的适配
@media (max-width: 480px) {
  .crypto-composition {
    padding: 40px 15px;
    min-height: 400px;
  }

  .title {
    font-size: 20px;
    margin-bottom: 30px;
  }

  .composition-container {
    height: 300px;
    padding: 0 10px;
  }

  .coin {
    width: 28px;
    height: 28px;
    padding: 4px;
  }

  // 进一步调整币种位置，使布局更紧凑
  .coin-group.left .coin {
    &:nth-child(1) { left: 2%; }
    &:nth-child(2) { left: 15%; }
    &:nth-child(3) { left: 5%; }
    &:nth-child(4) { left: 12%; }
    &:nth-child(5) { left: 20%; }
    &:nth-child(6) { left: 8%; }
  }

  .coin-group.right .coin {
    &:nth-child(1) { right: 2%; }
    &:nth-child(2) { right: 15%; }
    &:nth-child(3) { right: 5%; }
    &:nth-child(4) { right: 12%; }
    &:nth-child(5) { right: 20%; }
    &:nth-child(6) { right: 8%; }
  }

  .cta-button {
    font-size: 13px;
    padding: 10px 24px;
  }
}

.opacity-0 {
  opacity: 0;
}

.coin {
  &:hover {
    transform: scale(1.3) translateY(-10px);
    box-shadow: 0 8px 40px rgba(0, 116, 255, 0.5);
    animation-play-state: paused;
  }
}
</style> 