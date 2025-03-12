<template>
     <HeaderBar
      :currentName="_t18('menu_simulated_trade')"
    ></HeaderBar>
  <div class="simulated-trade-container">
    <div class="header">
      <h1>{{ t('menu_simulated_trade') }}</h1>
      <p class="description">{{ t('menu_simulated_trade_desc') }}</p>
    </div>
    
    <div class="balance-info">
      <div class="balance-card">
        <div class="label">{{ t('simulated_balance') }}</div>
        <div class="amount">{{ formatCurrency(balance) }} USDT</div>
        <button class="reset-btn" @click="resetBalance">{{ t('reset_balance') }}</button>
      </div>
    </div>
    
    <div class="trading-interface">
      <div class="market-section">
        <h2>{{ t('market') }}</h2>
        <div class="market-selector">
          <input type="text" v-model="searchTerm" :placeholder="t('search_pairs')" class="search-input" />
          <div class="market-tabs">
            <div 
              v-for="tab in marketTabs" 
              :key="tab.id" 
              :class="['market-tab', { active: activeMarketTab === tab.id }]"
              @click="activeMarketTab = tab.id"
            >
              {{ tab.name }}
            </div>
          </div>
        </div>
        <div class="market-table">
          <table v-if="filteredMarketData.length > 0">
            <thead>
              <tr>
                <th>{{ t('pair') }}</th>
                <th>{{ t('price') }}</th>
                <th>{{ t('24h_change') }}</th>
                <th>{{ t('24h_volume') }}</th>
              </tr>
            </thead>
            <tbody>
              <tr 
                v-for="item in filteredMarketData" 
                :key="item.symbol"
                @click="selectTradingPair(item)"
                :class="{ 'selected-pair': selectedPair === item.symbol }"
              >
                <td>{{ item.symbol }}</td>
                <td>{{ item.price }}</td>
                <td :class="item.change >= 0 ? 'positive' : 'negative'">
                  {{ item.change >= 0 ? '+' : '' }}{{ item.change }}%
                </td>
                <td>{{ item.volume }}</td>
              </tr>
            </tbody>
          </table>
          <div v-else class="placeholder">{{ t('no_market_data') }}</div>
        </div>
      </div>
      
      <div class="order-section">
        <h2>{{ t('place_order') }}</h2>
        <div class="price-overview" v-if="selectedPairData">
          <div class="current-price">{{ selectedPairData.price }} <span class="currency">USDT</span></div>
          <div :class="['price-change', selectedPairData.change >= 0 ? 'positive' : 'negative']">
            {{ selectedPairData.change >= 0 ? '+' : '' }}{{ selectedPairData.change }}%
          </div>
        </div>
        <div class="order-form">
          <div class="form-group">
            <label>{{ t('trading_pair') }}</label>
            <div class="custom-select">
              <div class="custom-select-selected" @click="togglePairDropdown">
                {{ selectedPair }}
                <span class="dropdown-icon"></span>
              </div>
              <div class="custom-select-dropdown" v-if="showPairDropdown">
                <div 
                  v-for="item in marketData" 
                  :key="item.symbol" 
                  :class="['custom-select-option', { selected: selectedPair === item.symbol }]"
                  @click="selectPair(item)"
                >
                  {{ item.symbol }}
                </div>
              </div>
            </div>
          </div>
          
          <div class="form-group">
            <label>{{ t('order_type') }}</label>
            <div class="tab-group">
              <div 
                :class="['tab', { active: orderType === 'buy' }]"
                @click="orderType = 'buy'"
              >
                {{ t('buy') }}
              </div>
              <div 
                :class="['tab', { active: orderType === 'sell' }]"
                @click="orderType = 'sell'"
              >
                {{ t('sell') }}
              </div>
            </div>
          </div>
          
          <div class="form-group">
            <label>{{ t('price') }}</label>
            <input type="text" v-model="price" placeholder="0.00" />
            <div class="input-suffix">USDT</div>
          </div>
          
          <div class="form-group">
            <label>{{ t('amount') }}</label>
            <input type="text" v-model="amount" placeholder="0.00" />
            <div class="input-suffix">{{ selectedPair ? selectedPair.split('/')[0] : 'BTC' }}</div>
            
            <div class="percentage-selector">
              <div 
                v-for="percent in [25, 50, 75, 100]" 
                :key="percent"
                class="percent-btn"
                @click="setAmountPercent(percent)"
              >
                {{ percent }}%
              </div>
            </div>
          </div>
          
          <div class="form-group">
            <label>{{ t('total') }}</label>
            <div class="total-value">{{ calculateTotal() }} USDT</div>
          </div>
          
          <button 
            :class="['submit-btn', orderType]"
            @click="placeOrder"
            :disabled="!isValidOrder"
          >
            {{ t(orderType) }} {{ selectedPair ? selectedPair.split('/')[0] : 'BTC' }}
          </button>
        </div>
      </div>
    </div>
    
    <div class="order-history">
      <h2>{{ t('order_history') }}</h2>
      <div class="history-tabs">
        <div 
          v-for="tab in historyTabs" 
          :key="tab.id"
          :class="['history-tab', { active: activeHistoryTab === tab.id }]"
          @click="activeHistoryTab = tab.id"
        >
          {{ t(tab.name) }}
        </div>
      </div>
      <div class="history-table">
        <table v-if="filteredOrderHistory.length > 0">
          <thead>
            <tr>
              <th>{{ t('date') }}</th>
              <th>{{ t('pair') }}</th>
              <th>{{ t('type') }}</th>
              <th>{{ t('price') }}</th>
              <th>{{ t('amount') }}</th>
              <th>{{ t('total') }}</th>
              <th>{{ t('status') }}</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(order, index) in filteredOrderHistory" :key="index">
              <td>{{ formatDate(order.date) }}</td>
              <td>{{ order.pair }}</td>
              <td :class="order.type">{{ t(order.type) }}</td>
              <td>{{ order.price }}</td>
              <td>{{ order.amount }}</td>
              <td>{{ order.total }}</td>
              <td :class="'status-' + order.status">{{ t('order_status_' + order.status) }}</td>
            </tr>
          </tbody>
        </table>
        <div v-else class="placeholder">{{ t('no_orders_yet') }}</div>
      </div>
    </div>
    
    <!-- 确认弹窗 -->
    <div class="dialog-overlay" v-if="confirmDialogVisible" @click="cancelAction">
      <div class="dialog-container" @click.stop>
        <div class="dialog-header">
          <h3>{{ t('confirmation') }}</h3>
          <button class="close-btn" @click="cancelAction">&times;</button>
        </div>
        <div class="dialog-content">
          <p>{{ confirmDialogMessage }}</p>
        </div>
        <div class="dialog-actions">
          <button class="cancel-btn" @click="cancelAction">{{ t('cancel') }}</button>
          <button class="confirm-btn" @click="confirmAction">{{ t('confirm') }}</button>
        </div>
      </div>
    </div>
    
    <!-- 消息提示 -->
    <div class="message-container" v-if="messageDialogVisible" :class="messageDialogType">
      <div class="message-icon">
        <span v-if="messageDialogType === 'success'">✓</span>
        <span v-else-if="messageDialogType === 'error'">✗</span>
        <span v-else-if="messageDialogType === 'warning'">!</span>
        <span v-else>i</span>
      </div>
      <div class="message-content">{{ messageDialogContent }}</div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useI18n } from 'vue-i18n'

const { t } = useI18n()

// Balance management
const balance = ref(100000)
const resetBalance = () => {
  // 替换默认的confirm弹窗，使用自定义弹窗
  showConfirmDialog(
    t('confirm_reset_balance'), 
    () => {
      balance.value = 100000
      orderHistory.value = []
      showSuccessMessage(t('balance_reset_success'))
    }
  )
}

// 弹窗状态管理
const confirmDialogVisible = ref(false)
const confirmDialogMessage = ref('')
const confirmDialogCallback = ref(null)
const messageDialogVisible = ref(false)
const messageDialogContent = ref('')
const messageDialogType = ref('success') // success, error, warning, info

const showConfirmDialog = (message, callback) => {
  confirmDialogMessage.value = message
  confirmDialogCallback.value = callback
  confirmDialogVisible.value = true
}

const confirmAction = () => {
  if (confirmDialogCallback.value) {
    confirmDialogCallback.value()
  }
  confirmDialogVisible.value = false
}

const cancelAction = () => {
  confirmDialogVisible.value = false
}

const showSuccessMessage = (message) => {
  messageDialogContent.value = message
  messageDialogType.value = 'success'
  messageDialogVisible.value = true
  setTimeout(() => {
    messageDialogVisible.value = false
  }, 3000)
}

const showErrorMessage = (message) => {
  messageDialogContent.value = message
  messageDialogType.value = 'error'
  messageDialogVisible.value = true
  setTimeout(() => {
    messageDialogVisible.value = false
  }, 3000)
}

// Market data
const marketData = ref([
  { symbol: 'BTC/USDT', price: '68452.30', change: 2.5, volume: '1,234,567' },
  { symbol: 'ETH/USDT', price: '3521.45', change: 1.8, volume: '987,654' },
  { symbol: 'BNB/USDT', price: '574.32', change: -0.7, volume: '456,789' },
  { symbol: 'SOL/USDT', price: '142.87', change: 5.2, volume: '345,678' },
  { symbol: 'XRP/USDT', price: '0.5423', change: -1.3, volume: '234,567' },
  { symbol: 'ADA/USDT', price: '0.4521', change: 0.9, volume: '123,456' },
  { symbol: 'DOGE/USDT', price: '0.1234', change: 3.7, volume: '789,012' },
  { symbol: 'DOT/USDT', price: '6.78', change: -2.1, volume: '98,765' },
  { symbol: 'AVAX/USDT', price: '35.67', change: 4.3, volume: '87,654' },
  { symbol: 'MATIC/USDT', price: '0.7654', change: 1.2, volume: '76,543' },
])

// Market filtering and selection
const searchTerm = ref('')
const activeMarketTab = ref('all')
const selectedPair = ref('BTC/USDT')

const marketTabs = [
  { id: 'all', name: 'All' },
  { id: 'gainers', name: 'Gainers' },
  { id: 'losers', name: 'Losers' },
]

const filteredMarketData = computed(() => {
  let filtered = marketData.value

  // Filter by search term
  if (searchTerm.value) {
    const term = searchTerm.value.toLowerCase()
    filtered = filtered.filter(item => 
      item.symbol.toLowerCase().includes(term)
    )
  }

  // Filter by tab
  if (activeMarketTab.value === 'gainers') {
    filtered = filtered.filter(item => item.change > 0)
  } else if (activeMarketTab.value === 'losers') {
    filtered = filtered.filter(item => item.change < 0)
  }

  return filtered
})

const selectedPairData = computed(() => {
  return marketData.value.find(item => item.symbol === selectedPair.value)
})

const selectTradingPair = (item) => {
  selectedPair.value = item.symbol
  price.value = item.price
}

// Order form
const orderType = ref('buy')
const price = ref('')
const amount = ref('')

watch(selectedPair, (newValue) => {
  const pair = marketData.value.find(item => item.symbol === newValue)
  if (pair) {
    price.value = pair.price
  }
})

const calculateTotal = () => {
  if (!price.value || !amount.value) return '0.00'
  return (parseFloat(price.value) * parseFloat(amount.value || 0)).toFixed(2)
}

const setAmountPercent = (percent) => {
  if (orderType.value === 'buy') {
    const maxAmount = balance.value / parseFloat(price.value || 1)
    amount.value = ((maxAmount * percent) / 100).toFixed(8)
  } else {
    // For sell orders, would need to track owned assets
    // This is simplified for the example
    amount.value = '0.00'
  }
}

const isValidOrder = computed(() => {
  const priceVal = parseFloat(price.value)
  const amountVal = parseFloat(amount.value)
  const totalVal = priceVal * amountVal
  
  if (!priceVal || !amountVal || priceVal <= 0 || amountVal <= 0) {
    return false
  }
  
  if (orderType.value === 'buy' && totalVal > balance.value) {
    return false
  }
  
  return true
})

// Order history
const orderHistory = ref([])
const activeHistoryTab = ref('all')

const historyTabs = [
  { id: 'all', name: 'all_orders' },
  { id: 'open', name: 'open_orders' },
  { id: 'completed', name: 'completed_orders' },
]

const filteredOrderHistory = computed(() => {
  if (activeHistoryTab.value === 'all') {
    return orderHistory.value
  }
  return orderHistory.value.filter(order => order.status === activeHistoryTab.value)
})

const placeOrder = () => {
  if (!isValidOrder.value) return
  
  const total = parseFloat(price.value) * parseFloat(amount.value)
  
  // Update balance
  if (orderType.value === 'buy') {
    balance.value -= total
    showSuccessMessage(t('buy_order_success'))
  } else {
    balance.value += total
    showSuccessMessage(t('sell_order_success'))
  }
  
  // Add to order history
  orderHistory.value.unshift({
    date: new Date(),
    pair: selectedPair.value,
    type: orderType.value,
    price: price.value,
    amount: amount.value,
    total: total.toFixed(2),
    status: 'completed' // In a real app, could be 'open', 'completed', 'canceled'
  })
  
  // Reset form
  amount.value = ''
}

// Utility functions
const formatCurrency = (value) => {
  return new Intl.NumberFormat('en-US').format(value)
}

const formatDate = (date) => {
  return new Date(date).toLocaleString()
}

// 添加下拉菜单状态管理
const showPairDropdown = ref(false)

const togglePairDropdown = () => {
  showPairDropdown.value = !showPairDropdown.value
}

const selectPair = (item) => {
  selectedPair.value = item.symbol
  price.value = item.price
  showPairDropdown.value = false
}

onMounted(() => {
  // Initialize with the first pair's price
  if (marketData.value.length > 0) {
    price.value = marketData.value[0].price
  }
  
  // In a real app, would fetch market data here
  console.log('Simulated trading component mounted')
  
  document.addEventListener('click', (e) => {
    const customSelect = document.querySelector('.custom-select')
    if (customSelect && !customSelect.contains(e.target)) {
      showPairDropdown.value = false
    }
  })
})
</script>

<style scoped>
.simulated-trade-container {
  padding: 24px;
  max-width: 1200px;
  margin: 0 auto;
  color: #fff;
}

.header {
  margin-bottom: 24px;
}

.header h1 {
  font-size: 24px;
  margin-bottom: 8px;
}

.description {
  color: #7A7A97;
  font-size: 14px;
}

.balance-info {
  margin-bottom: 24px;
}

.balance-card {
  background: rgba(255, 255, 255, 0.05);
  border-radius: 8px;
  padding: 16px;
  display: flex;
  align-items: center;
}

.label {
  color: #7A7A97;
  margin-right: 12px;
}

.amount {
  font-size: 18px;
  font-weight: bold;
  margin-right: auto;
}

.reset-btn {
  background: rgba(255, 255, 255, 0.1);
  border: none;
  color: #fff;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
}

.reset-btn:hover {
  background: rgba(255, 255, 255, 0.15);
}

.trading-interface {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
  margin-bottom: 24px;
}

.market-section, .order-section {
  background: rgba(255, 255, 255, 0.05);
  border-radius: 8px;
  padding: 16px;
}

h2 {
  font-size: 18px;
  margin-bottom: 16px;
}

.placeholder {
  color: #7A7A97;
  text-align: center;
  padding: 40px 0;
}

.form-group {
  margin-bottom: 16px;
  position: relative;
}

label {
  display: block;
  margin-bottom: 8px;
  color: #7A7A97;
}

input, select {
  width: 100%;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  color: #fff;
  padding: 8px 12px;
  border-radius: 4px;
}

.input-suffix {
  position: absolute;
  right: 12px;
  top: 36px;
  color: #7A7A97;
}

.tab-group {
  display: flex;
}

.tab {
  flex: 1;
  text-align: center;
  padding: 8px;
  background: rgba(255, 255, 255, 0.05);
  cursor: pointer;
  transition: all 0.3s ease;
}

.tab.active {
  background: rgba(0, 128, 0, 0.2);
  color: #00ff00;
}

.tab.active:last-child {
  background: rgba(128, 0, 0, 0.2);
  color: #ff6666;
}

.total-value {
  padding: 8px 0;
  font-weight: bold;
}

.submit-btn {
  width: 100%;
  padding: 12px;
  border: none;
  border-radius: 4px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
}

.submit-btn.buy {
  background: linear-gradient(to right, #00b897, #00d2a0);
  color: #fff;
}

.submit-btn.sell {
  background: linear-gradient(to right, #d45858, #ff6666);
  color: #fff;
}

.submit-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.order-history {
  background: rgba(255, 255, 255, 0.05);
  border-radius: 8px;
  padding: 16px;
}

/* New styles */
.market-selector {
  margin-bottom: 16px;
}

.search-input {
  margin-bottom: 12px;
}

.market-tabs, .history-tabs {
  display: flex;
  margin-bottom: 16px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.market-tab, .history-tab {
  padding: 8px 16px;
  cursor: pointer;
  color: #7A7A97;
}

.market-tab.active, .history-tab.active {
  color: #fff;
  border-bottom: 2px solid #00d2a0;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  padding: 12px 8px;
  text-align: left;
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}

th {
  color: #7A7A97;
  font-weight: normal;
}

tbody tr {
  cursor: pointer;
  transition: background 0.3s ease;
}

tbody tr:hover {
  background: rgba(255, 255, 255, 0.05);
}

.selected-pair {
  background: rgba(0, 210, 160, 0.1);
}

.positive {
  color: #00d2a0;
}

.negative {
  color: #ff6666;
}

.buy {
  color: #00d2a0;
}

.sell {
  color: #ff6666;
}

.status-completed {
  color: #00d2a0;
}

.status-open {
  color: #f0b90b;
}

.status-canceled {
  color: #7A7A97;
}

.percentage-selector {
  display: flex;
  margin-top: 8px;
  gap: 8px;
}

.percent-btn {
  flex: 1;
  text-align: center;
  padding: 4px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
}

.percent-btn:hover {
  background: rgba(255, 255, 255, 0.1);
}

.price-overview {
  background: rgba(255, 255, 255, 0.03);
  padding: 12px;
  border-radius: 4px;
  margin-bottom: 16px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.current-price {
  font-size: 20px;
  font-weight: bold;
}

.currency {
  font-size: 14px;
  color: #7A7A97;
}

.price-change {
  font-weight: bold;
}

/* 弹窗样式 */
.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.dialog-container {
  background-color: #1a1a2e;
  border-radius: 8px;
  width: 400px;
  max-width: 90%;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.5);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.dialog-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.dialog-header h3 {
  margin: 0;
  color: #fff;
  font-size: 18px;
}

.close-btn {
  background: none;
  border: none;
  color: #7A7A97;
  font-size: 24px;
  cursor: pointer;
}

.close-btn:hover {
  color: #fff;
}

.dialog-content {
  padding: 20px 16px;
  color: #e0e0e0;
}

.dialog-actions {
  display: flex;
  justify-content: flex-end;
  padding: 16px;
  gap: 12px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.cancel-btn, .confirm-btn {
  padding: 8px 16px;
  border-radius: 4px;
  font-weight: 500;
  cursor: pointer;
  border: none;
}

.cancel-btn {
  background-color: rgba(255, 255, 255, 0.1);
  color: #e0e0e0;
}

.cancel-btn:hover {
  background-color: rgba(255, 255, 255, 0.15);
}

.confirm-btn {
  background: linear-gradient(to right, #00b897, #00d2a0);
  color: #fff;
}

.confirm-btn:hover {
  opacity: 0.9;
}

/* 消息提示样式 */
.message-container {
  position: fixed;
  top: 20px;
  right: 20px;
  display: flex;
  align-items: center;
  padding: 12px 16px;
  border-radius: 6px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  z-index: 1001;
  animation: slideIn 0.3s ease-out;
  background-color: #1a1a2e;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

@keyframes slideIn {
  from {
    transform: translateX(100%);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

.message-container.success {
  border-left: 4px solid #00d2a0;
}

.message-container.error {
  border-left: 4px solid #ff6666;
}

.message-container.warning {
  border-left: 4px solid #f0b90b;
}

.message-container.info {
  border-left: 4px solid #3498db;
}

.message-icon {
  margin-right: 12px;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
}

.success .message-icon {
  background-color: rgba(0, 210, 160, 0.2);
  color: #00d2a0;
}

.error .message-icon {
  background-color: rgba(255, 102, 102, 0.2);
  color: #ff6666;
}

.warning .message-icon {
  background-color: rgba(240, 185, 11, 0.2);
  color: #f0b90b;
}

.info .message-icon {
  background-color: rgba(52, 152, 219, 0.2);
  color: #3498db;
}

.message-content {
  color: #e0e0e0;
}

/* 修改下拉菜单样式 */
select {
  width: 100%;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  color: #fff;
  padding: 8px 12px;
  border-radius: 4px;
  appearance: none; /* 移除默认样式 */
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='12' viewBox='0 0 12 12'%3E%3Cpath fill='%237A7A97' d='M6 9L1 4h10z'/%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: right 12px center;
  cursor: pointer;
}

/* 添加自定义下拉菜单样式 */
.custom-select {
  position: relative;
  width: 100%;
}

.custom-select-selected {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  color: #fff;
  padding: 8px 12px;
  border-radius: 4px;
  cursor: pointer;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.custom-select-selected:after {
  content: '';
  width: 0;
  height: 0;
  border-left: 5px solid transparent;
  border-right: 5px solid transparent;
  border-top: 5px solid #7A7A97;
}

.custom-select-dropdown {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background: #1a1a2e;
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 4px;
  margin-top: 4px;
  max-height: 300px;
  overflow-y: auto;
  z-index: 10;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.custom-select-option {
  padding: 8px 12px;
  cursor: pointer;
  transition: background 0.2s;
}

.custom-select-option:hover {
  background: rgba(255, 255, 255, 0.1);
}

.custom-select-option.selected {
  background: rgba(0, 210, 160, 0.1);
  color: #00d2a0;
}

/* 修改滚动条样式 */
.custom-select-dropdown::-webkit-scrollbar {
  width: 6px;
}

.custom-select-dropdown::-webkit-scrollbar-track {
  background: rgba(255, 255, 255, 0.05);
  border-radius: 3px;
}

.custom-select-dropdown::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.2);
  border-radius: 3px;
}

.custom-select-dropdown::-webkit-scrollbar-thumb:hover {
  background: rgba(255, 255, 255, 0.3);
}

/* 修改原生下拉菜单在不同浏览器中的样式 */
select option {
  background-color: #1a1a2e;
  color: #fff;
}

/* Firefox specific styles */
@-moz-document url-prefix() {
  select {
    background-color: #1a1a2e;
    color: #fff;
  }
}

/* 确保下拉菜单在所有浏览器中都有深色背景 */
select:focus {
  outline: none;
  border-color: #00d2a0;
}

/* 修改下拉菜单打开时的样式 */
.select-wrapper {
  position: relative;
  width: 100%;
}

.select-wrapper::after {
  content: '';
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  width: 0;
  height: 0;
  border-left: 5px solid transparent;
  border-right: 5px solid transparent;
  border-top: 5px solid #7A7A97;
  pointer-events: none;
}
</style> 