<template>
  <div class="statistics-section">
    <div class="who-we-are">
      <div class="section-title">{{ $t('home_introduction.title') }}</div>
      <p>{{ $t('home_introduction.description') }}</p>
    </div>

    <div class="charity-stats">
      <div class="section-title">{{ $t('home_introduction.charity.title') }}</div>
      <div class="amount-display">
        <div class="usdt-amount">
          <span>{{ formatNumber(5231.48) }}w USDT</span>
          <span class="dollar-value">${{ formatNumber(52314791.00) }}</span>
        </div>
      </div>

      <div class="stats-circle">
        <div class="stat-item">
          <div class="value">1,230</div>
          <div class="label">{{ $t('home_introduction.stats.representatives') }}</div>
        </div>
        <div class="stat-item">
          <div class="value">1,077</div>
          <div class="label">{{ $t('home_introduction.stats.market') }}</div>
        </div>
        <div class="stat-item">
          <div class="value">185000w</div>
          <div class="label">{{ $t('home_introduction.stats.usdtVolume') }}</div>
        </div>
      </div>
    </div>

    <div class="advantages">
      <div class="section-title">{{ $t('home_introduction.advantages.title') }}</div>
      <div class="advantage-items">
        <div class="advantage-item" v-for="(item, index) in 3" :key="index">
          <div class="icon" :class="`security-level-${index + 1}`"></div>
          <div class="text">{{ $t('home_introduction.advantages.securityStable') }}</div>
        </div>
      </div>
    </div>

    <div class="contribution">
      <div class="section-title">{{ $t('home_introduction.contribution.title') }}</div>
      <p>{{ $t('home_introduction.contribution.description') }}</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'StatisticsSection',
  methods: {
    formatNumber(num) {
      return num.toLocaleString()
    }
  }
}
</script>

<style scoped>
.statistics-section {
  padding: 12px;
  min-height: 100vh;
}

.section-title {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 12px;
  background: linear-gradient(90deg, #00ffff 0%, #9932cc 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  animation: fadeIn 0.5s ease-in;
}

.who-we-are, .charity-stats, .advantages, .contribution {
  margin-bottom: 20px;
  padding: 15px;
  border-radius: 16px;
  box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.36);
  transform: translateY(0);
  transition: all 0.3s ease;
}

.who-we-are:hover, .charity-stats:hover, .advantages:hover, .contribution:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 40px 0 rgba(0, 0, 0, 0.45);
}

.amount-display {
  font-size: 24px;
  margin: 15px 0;
  text-align: center;
}

.stats-circle {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
  margin: 15px 0;
}

.stat-item {
  padding: 12px 8px;
  border-radius: 12px;
  text-align: center;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  transition: transform 0.3s ease;
}

.stat-item:hover {
  transform: scale(1.05);
}

.advantage-items {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
}

.advantage-item {
  padding: 12px;
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
}

.advantage-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.3);
}

.security-level-1 {
  background: linear-gradient(45deg, #ff4444, #ff6b6b);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.security-level-2 {
  background: linear-gradient(45deg, #00ffff, #44ffff);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.security-level-3 {
  background: linear-gradient(45deg, #9932cc, #ba55d3);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.value {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 4px;
  background: linear-gradient(90deg, #00ffff, #9932cc);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.label {
  font-size: 12px;
  background: linear-gradient(90deg, #ffffff, #b0b0b0);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.usdt-amount {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  animation: pulse 2s infinite;
}

.usdt-amount span {
  background: linear-gradient(90deg, #00ffff 0%, #9932cc 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.dollar-value {
  font-size: 18px;
  background: linear-gradient(90deg, #b0b0b0, #ffffff);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

p {
  background: linear-gradient(90deg, #ffffff, #b0b0b0);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

@keyframes fadeIn {
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
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
  100% {
    transform: scale(1);
  }
}

/* 响应式调整 */
@media (max-width: 480px) {
  .advantage-items {
    grid-template-columns: 1fr;
  }
  
  .stats-circle {
    grid-template-columns: 1fr;
  }
  
  .section-title {
    font-size: 18px;
  }
  
  .amount-display {
    font-size: 20px;
  }
}
</style>
