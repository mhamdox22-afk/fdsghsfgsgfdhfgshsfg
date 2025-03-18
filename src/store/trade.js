import { defineStore } from 'pinia'

export const useTradeStore = defineStore('trade', {
  state: () => ({
    // Preserve your existing state
    secondContractCoinList: [],
    spotCoinList: [],
    contractCoinList: [],
    // Add our new flag
    hasLoadedCoinList: false,
  }),
  
  actions: {
    async getCoinList() {
      try {
        // Your existing API call implementation
        // For example:
        // const response = await fetch('/api/coins')
        // const data = await response.json()
        // this.secondContractCoinList = data.secondContract || []
        // this.spotCoinList = data.spot || []
        // this.contractCoinList = data.contract || []
        
        // After successful loading:
        this.hasLoadedCoinList = true
      } catch (error) {
        console.error('Failed to load coin list:', error)
      }
    },
    
    // Preserve your other actions
  },
  
  getters: {
    // Preserve your existing getters
  }
}) 