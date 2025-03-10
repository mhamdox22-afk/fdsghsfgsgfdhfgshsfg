<template>
  <div class="purchase-steps">
    <div class="background-elements">
      <div class="gradient-circle circle-1"></div>
      <div class="gradient-circle circle-2"></div>
      <div class="dots-bg"></div>
    </div>
    
    <h2 class="title animate-title" ref="titleRef">
      {{ $t('home.purchaseSteps.title') }}
      <div class="title-underline"></div>
    </h2>

    <div class="steps-container">
      <div class="step animate-step" ref="steps">
        <div class="icon-wrapper">
          <div class="icon">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" class="plus-icon">
              <path d="M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z" fill="currentColor"/>
            </svg>
          </div>
          <div class="ripple-circles">
            <div class="circle"></div>
            <div class="circle"></div>
            <div class="circle"></div>
          </div>
        </div>
        <div class="step-text">{{ $t('home.purchaseSteps.steps.createAccount') }}</div>
      </div>

      <div class="connector">
        <svg viewBox="0 0 100 50" class="curve">
          <path d="M0,25 C30,25 70,25 100,25" class="path"></path>
        </svg>
      </div>

      <div class="step animate-step" ref="steps">
        <div class="icon-wrapper">
          <div class="icon wallet-icon">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
              <path d="M21 18v1c0 1.1-.9 2-2 2H5c-1.11 0-2-.9-2-2V5c0-1.1.89-2 2-2h14c1.1 0 2 .9 2 2v1h-9c-1.11 0-2 .9-2 2v8c0 1.1.89 2 2 2h9zm-9-2h10V8H12v8zm4-2.5c-.83 0-1.5-.67-1.5-1.5s.67-1.5 1.5-1.5 1.5.67 1.5 1.5-.67 1.5-1.5 1.5z" fill="currentColor"/>
            </svg>
          </div>
          <div class="ripple-circles">
            <div class="circle"></div>
            <div class="circle"></div>
            <div class="circle"></div>
          </div>
        </div>
        <div class="step-text">{{ $t('home.purchaseSteps.steps.connect') }}</div>
      </div>

      <div class="connector">
        <svg viewBox="0 0 100 50" class="curve">
          <path d="M0,25 C30,25 70,25 100,25" class="path"></path>
        </svg>
      </div>

      <div class="step animate-step" ref="steps">
        <div class="icon-wrapper">
          <div class="icon crypto-icon">
            <div class="coins">
              <img src="https://cryptologos.cc/logos/bitcoin-btc-logo.png" alt="Bitcoin" />
              <img src="https://cryptologos.cc/logos/ethereum-eth-logo.png" alt="Ethereum" />
              <img src="https://cryptologos.cc/logos/tether-usdt-logo.png" alt="USDT" />
            </div>
          </div>
          <div class="ripple-circles">
            <div class="circle"></div>
            <div class="circle"></div>
            <div class="circle"></div>
          </div>
        </div>
        <div class="step-text">{{ $t('home.purchaseSteps.steps.trade') }}</div>
      </div>

      <div class="connector">
        <svg viewBox="0 0 100 50" class="curve">
          <path d="M0,25 C30,25 70,25 100,25" class="path"></path>
        </svg>
      </div>

      <div class="step animate-step" ref="steps">
        <div class="icon-wrapper">
          <div class="icon shield-icon">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
              <path d="M12 1L3 5v6c0 5.55 3.84 10.74 9 12 5.16-1.26 9-6.45 9-12V5l-9-4zm0 10.99h7c-.53 4.12-3.28 7.79-7 8.94V12H5V6.3l7-3.11v8.8z" fill="currentColor"/>
            </svg>
          </div>
          <div class="ripple-circles">
            <div class="circle"></div>
            <div class="circle"></div>
            <div class="circle"></div>
          </div>
        </div>
        <div class="step-text">{{ $t('home.purchaseSteps.steps.security') }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useI18n } from 'vue-i18n'
import { ref, onMounted } from 'vue'

const { t } = useI18n()
const titleRef = ref(null)
const steps = ref([])

onMounted(() => {
  const observer = new IntersectionObserver((entries) => {
    entries.forEach((entry, index) => {
      if (entry.isIntersecting) {
        entry.target.classList.add('animate-visible')
      }
    })
  }, {
    threshold: 0.2
  })

  // Observe title
  if (titleRef.value) {
    observer.observe(titleRef.value)
  }

  // Observe all steps
  document.querySelectorAll('.animate-step').forEach((step, index) => {
    observer.observe(step)
  })
})
</script>

<style lang="scss" scoped>
.purchase-steps {
  padding: 0 120px;
  background: linear-gradient(180deg, #0a0a0a 0%, #111111 100%);
  color: #fff;
  overflow: hidden;
  position: relative;
  min-height: 450px;
}

.background-elements {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  z-index: 1;
  
  .gradient-circle {
    position: absolute;
    border-radius: 50%;
    filter: blur(80px);
  }
  
  .circle-1 {
    width: 400px;
    height: 400px;
    background: radial-gradient(circle, rgba(0,196,255,0.15) 0%, rgba(0,116,255,0.05) 70%);
    top: -100px;
    left: -100px;
  }
  
  .circle-2 {
    width: 500px;
    height: 500px;
    background: radial-gradient(circle, rgba(112,0,255,0.1) 0%, rgba(0,255,126,0.05) 70%);
    bottom: -200px;
    right: -100px;
  }
  
  .dots-bg {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-image: radial-gradient(circle, #ffffff 1px, transparent 1px);
    background-size: 20px 20px;
    opacity: 0.1;
  }
}

.title {
  position: relative;
  z-index: 2;
  text-align: center;
  font-size: 36px;
  font-weight: 800;
  margin-bottom: 80px;
  background: linear-gradient(to right, #fff 0%, #00C4FF 50%, #7000FF 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  max-width: 800px;
  margin-left: auto;
  margin-right: auto;
  
  .title-underline {
    width: 100px;
    height: 4px;
    background: linear-gradient(to right, #00C4FF, #7000FF);
    margin: 20px auto 0;
    border-radius: 2px;
  }
}

.steps-container {
  position: relative;
  z-index: 2;
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 60px 80px;
  gap: 60px;
}

.step {
  text-align: center;
  position: relative;
  flex: 1;
  max-width: 150px;
  
  &:hover {
    .icon {
      transform: translateY(-5px);
      box-shadow: 0 10px 20px rgba(0,196,255,0.2);
    }
    
    .ripple-circles .circle {
      animation-duration: 2s;
    }
  }
}

.icon-wrapper {
  position: relative;
  width: 80px;
  height: 80px;
  margin: 0 auto 16px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.icon {
  width: 80px;
  height: 80px;
  background: rgba(26, 26, 26, 0.8);
  backdrop-filter: blur(10px);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  z-index: 2;
  color: #00C4FF;
  border: 2px solid rgba(42, 42, 42, 0.5);
  transition: all 0.3s ease;
  
  svg {
    width: 40px;
    height: 40px;
  }
}

.wallet-icon {
  color: #0074FF;
}

.crypto-icon {
  color: #7000FF;
  .coins {
    display: flex;
    gap: 8px;
    align-items: center;
    justify-content: center;
    
    img {
      width: 24px;
      height: 24px;
      object-fit: contain;
    }
  }
}

.shield-icon {
  color: #00ff7e;
}

.ripple-circles {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 80px;
  height: 80px;
  
  .circle {
    position: absolute;
    top: 50%;
    left: 50%;
    width: 100%;
    height: 100%;
    border: 2px solid currentColor;
    border-radius: 50%;
    animation: ripple 3s infinite;
    opacity: 0;
    
    &:nth-child(1) {
      animation-delay: 0s;
    }
    
    &:nth-child(2) {
      animation-delay: 1s;
    }
    
    &:nth-child(3) {
      animation-delay: 2s;
    }
  }
}

@keyframes ripple {
  0% {
    transform: translate(-50%, -50%) scale(1);
    opacity: 0.5;
  }
  100% {
    transform: translate(-50%, -50%) scale(2);
    opacity: 0;
  }
}

.step-text {
  font-size: 12px;
  color: #fff;
  margin-top: 14px;
  max-width: 120px;
  margin-left: auto;
  margin-right: auto;
}

.connector {
  flex: 0.5;
  height: 70px;
  display: flex;
  align-items: center;
  
  .curve {
    width: 100%;
    height: 50px;
    
    .path {
      fill: none;
      stroke: #2a2a2a;
      stroke-width: 2;
      stroke-dasharray: 100;
      animation: dash 3s linear infinite;
    }
  }
}

@keyframes dash {
  to {
    stroke-dashoffset: -200;
  }
}

@media (max-width: 968px) {
  .steps-container {
    flex-direction: column;
    gap: 40px;
    padding: 40px;
  }

  .connector {
    display: none;
  }

  .step {
    max-width: 100%;
  }

  .title {
    font-size: 28px;
    padding: 0 20px;
  }
  
  .purchase-steps {
    padding: 80px 40px;
  }
  
  .background-elements {
    .gradient-circle {
      opacity: 0.5;
    }
  }
}

.animate-title {
  opacity: 0;
  transform: translateY(30px);
  
  &.animate-visible {
    animation: fadeInDown 0.8s ease forwards;
  }
}

.animate-step {
  opacity: 0;
  transform: translateY(30px);
  
  &.animate-visible {
    animation: fadeInUp 0.8s ease forwards;
  }
  
  &:nth-child(1) { animation-delay: 0.2s; }
  &:nth-child(3) { animation-delay: 0.4s; }
  &:nth-child(5) { animation-delay: 0.6s; }
  &:nth-child(7) { animation-delay: 0.8s; }
}

@keyframes fadeInDown {
  from {
    opacity: 0;
    transform: translateY(-30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@media (max-width: 968px) {
  .animate-step {
    &.animate-visible {
      animation-delay: 0.2s !important;
    }
  }
}
</style> 