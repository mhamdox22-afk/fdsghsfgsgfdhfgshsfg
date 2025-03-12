<template>
  <HeaderBar
      :currentName="_t18('menu_fund_security')"
    ></HeaderBar>
  <div class="app-root funds-security-con">
    <div class="security-banner">
      <div class="banner-content">
        <div class="banner-main">
          <div class="main-info">
            <h1 class="main-title">{{ $t('fundSecurity.title') }}</h1>
            <p class="desc">
              {{ $t('fundSecurity.description1') }}
              <br><br>
              {{ $t('fundSecurity.description2') }}
            </p>
          </div>
          <img 
            class="main-img" 
            src="https://www.okx.com/cdn/assets/imgs/234/09D1465E7EB95BD4.png" 
            alt="{{ $t('fundSecurity.altText') }}"
          >
        </div>
      </div>
    </div>
    
    <div class="security-operational">
      <div class="balance_okui balance_okui-tabs wallet-type-switch">
        <div class="balance_okui-tabs-pane-list balance_okui-tabs-pane-list-xxxl balance_okui-tabs-pane-list-grey balance_okui-tabs-pane-list-underline balance_okui-tabs-pane-list-underline-no-line">
          <div class="balance_okui-tabs-pane-list-wrapper">
            <div class="balance_okui-tabs-pane-list-container" role="tablist">
              <div class="balance_okui-tabs-pane-list-flex-shrink">
                <div 
                  class="balance_okui-tabs-pane balance_okui-tabs-pane-xxxl balance_okui-tabs-pane-grey balance_okui-tabs-pane-underline" 
                  :class="{ 'balance_okui-tabs-pane-underline-active': activeTab === 0 }"
                  data-pane-id="0" 
                  id=":R2:-0" 
                  role="tab" 
                  :aria-selected="activeTab === 0" 
                  tabindex="-1"
                  @click="activeTab = 0"
                >
                  {{ $t('fundSecurity.coldWallet') }}
                </div>
                <div 
                  class="balance_okui-tabs-pane balance_okui-tabs-pane-xxxl balance_okui-tabs-pane-grey balance_okui-tabs-pane-underline" 
                  :class="{ 'balance_okui-tabs-pane-underline-active': activeTab === 1 }"
                  data-pane-id="1" 
                  id=":R2:-1" 
                  role="tab" 
                  :aria-selected="activeTab === 1" 
                  tabindex="-1"
                  @click="activeTab = 1"
                >
                  {{ $t('fundSecurity.hotWallet') }}
                </div>
                <div 
                  class="balance_okui-tabs-pane balance_okui-tabs-pane-xxxl balance_okui-tabs-pane-grey balance_okui-tabs-pane-underline" 
                  :class="{ 'balance_okui-tabs-pane-underline-active': activeTab === 2 }"
                  data-pane-id="2" 
                  id=":R2:-2" 
                  role="tab" 
                  :aria-selected="activeTab === 2" 
                  tabindex="0"
                  @click="activeTab = 2"
                >
                  {{ $t('fundSecurity.antbittFund') }}
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <div class="balance_okui-tabs-panel-list">
          <div 
            class="balance_okui-tabs-panel" 
            :class="{ 'balance_okui-tabs-panel-show': activeTab === 0 }"
            role="tabpanel" 
            aria-labelledby=":R2:-0" 
            tabindex="-1" 
            :aria-hidden="activeTab !== 0"
          >
            <!-- 冷钱包内容 -->
            <div class="cold-wallet-content">
              <h2 class="section-title">{{ $t('fundSecurity.coldWalletTitle') }}</h2>
              
              <p class="section-subtitle">
                {{ $t('fundSecurity.coldWalletSubtitle') }}
              </p>
              
              <p class="section-desc">
                {{ $t('fundSecurity.coldWalletDescription') }}
              </p>
              
              <h3 class="subsection-title">{{ $t('fundSecurity.coldWalletKeyPoints') }}</h3>
              
              <ul class="feature-list">
                <li class="feature-item">
                  <span class="check-icon">✓</span>
                  {{ $t('fundSecurity.coldWalletFeature1') }}
                </li>
                <li class="feature-item">
                  <span class="check-icon">✓</span>
                  {{ $t('fundSecurity.coldWalletFeature2') }}
                </li>
                <li class="feature-item">
                  <span class="check-icon">✓</span>
                  {{ $t('fundSecurity.coldWalletFeature3') }}
                </li>
              </ul>
              
              <h3 class="subsection-title">{{ $t('fundSecurity.coldWalletResults') }}</h3>
              
              <ul class="feature-list">
                <li class="feature-item">
                  <span class="check-icon">✓</span>
                  {{ $t('fundSecurity.coldWalletResult1') }}
                </li>
                <li class="feature-item">
                  <span class="check-icon">✓</span>
                  {{ $t('fundSecurity.coldWalletResult2') }}
                </li>
                <li class="feature-item">
                  <span class="check-icon">✓</span>
                  {{ $t('fundSecurity.coldWalletResult3') }}
                </li>
              </ul>
            </div>
          </div>
          <div 
            class="balance_okui-tabs-panel" 
            :class="{ 'balance_okui-tabs-panel-show': activeTab === 1 }"
            role="tabpanel" 
            aria-labelledby=":R2:-1" 
            tabindex="-1" 
            :aria-hidden="activeTab !== 1"
          >
            <!-- 热钱包内容 -->
            <div class="hot-wallet-content">
              <h2 class="section-title">{{ $t('fundSecurity.hotWalletTitle') }}</h2>
              
              <p class="section-desc">
                {{ $t('fundSecurity.hotWalletDescription') }}
              </p>
              
              <h3 class="subsection-title">{{ $t('fundSecurity.hotWalletKeyPoints') }}</h3>
              
              <ul class="feature-list">
                <li class="feature-item">
                  <span class="check-icon">✓</span>
                  {{ $t('fundSecurity.hotWalletFeature1') }}
                </li>
                <li class="feature-item">
                  <span class="check-icon">✓</span>
                  {{ $t('fundSecurity.hotWalletFeature2') }}
                </li>
              </ul>
              
              <h3 class="subsection-title">{{ $t('fundSecurity.hotWalletResults') }}</h3>
              
              <ul class="feature-list">
                <li class="feature-item">
                  <span class="check-icon">✓</span>
                  {{ $t('fundSecurity.hotWalletResult1') }}
                </li>
                <li class="feature-item">
                  <span class="check-icon">✓</span>
                  {{ $t('fundSecurity.hotWalletResult2') }}
                </li>
                <li class="feature-item">
                  <span class="check-icon">✓</span>
                  {{ $t('fundSecurity.hotWalletResult3') }}
                </li>
              </ul>
            </div>
          </div>
          <div 
            class="balance_okui-tabs-panel" 
            :class="{ 'balance_okui-tabs-panel-show': activeTab === 2 }"
            role="tabpanel" 
            aria-labelledby=":R2:-2" 
            tabindex="0" 
            :aria-hidden="activeTab !== 2"
          >
            <!-- ANTBITT万全基金内容 -->
            <div class="risk-shield-con concept-con">
              <p class="concept-title">{{ $t('fundSecurity.antbittFundTitle') }}</p>
              <div class="concept-desc">
                <p class="desc-item">
                  {{ $t('fundSecurity.antbittFundDescription') }}
                </p>
              </div>
              
              <p class="concept-title">{{ $t('fundSecurity.fundNeedTitle') }}</p>
              <div class="concept-desc">
                <p class="desc-item">
                  {{ $t('fundSecurity.fundNeedDescription') }}
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'FundSecurity',
  data() {
    return {
      activeTab: 1 // 默认选中热钱包
    }
  }
}
</script>

<style lang="scss" scoped>
.app-root.funds-security-con {
  width: 100%;
  background-color: #0c0c0c;
  color: #fff;
  padding: 0 5vw;
  font-size: 12px;
}

.security-banner {
  padding: 50px 0;
  
  .banner-content {
    max-width: 1200px;
    margin: 0 auto;
    
    .banner-main {
      display: flex;
      align-items: center;
      justify-content: space-between;
      
      .main-info {
        max-width: 50%;
        
        .main-title {
          font-size: 32px;
          font-weight: bold;
          margin-bottom: 16px;
        }
        
        .desc {
          font-size: 12px;
          line-height: 1.4;
          color: rgba(255, 255, 255, 0.8);
        }
      }
      
      .main-img {
        max-width: 45%;
      }
    }
  }
}

.security-operational {
  max-width: 1200px;
  margin: 0 auto;
  padding: 30px 0;
  
  .wallet-type-switch {
    margin-bottom: 30px;
    
    .balance_okui-tabs-pane {
      padding: 10px 16px;
      cursor: pointer;
      font-size: 14px;
      position: relative;
      
      &.balance_okui-tabs-pane-underline-active {
        color: #00b897;
        
        &::after {
          content: '';
          position: absolute;
          bottom: 0;
          left: 0;
          width: 100%;
          height: 2px;
          background-color: #00b897;
        }
      }
    }
    
    .balance_okui-tabs-pane-list-container {
      display: flex;
      border-bottom: 1px solid rgba(255, 255, 255, 0.1);
    }
    
    .balance_okui-tabs-pane-list-flex-shrink {
      display: flex;
    }
    
    .balance_okui-tabs-panel {
      display: none;
      
      &.balance_okui-tabs-panel-show {
        display: block;
      }
    }
  }
}

.risk-shield-con {
  max-width: 1200px;
  margin: 0 auto;
  padding: 30px 0 50px;
  
  .concept-title {
    font-size: 18px;
    font-weight: bold;
    margin-bottom: 12px;
    margin-top: 30px;
    
    &:first-child {
      margin-top: 0;
    }
  }
  
  .concept-desc {
    margin-bottom: 30px;
    
    .desc-item {
      font-size: 12px;
      line-height: 1.5;
      color: rgba(255, 255, 255, 0.8);
    }
  }
}

.cold-wallet-content, .hot-wallet-content {
  padding: 15px 0;
  
  .section-title {
    font-size: 22px;
    font-weight: bold;
    margin-bottom: 16px;
  }
  
  .section-subtitle {
    font-size: 14px;
    font-weight: 500;
    margin-bottom: 12px;
    color: rgba(255, 255, 255, 0.9);
  }
  
  .section-desc {
    font-size: 12px;
    line-height: 1.5;
    margin-bottom: 30px;
    color: rgba(255, 255, 255, 0.8);
  }
  
  .subsection-title {
    font-size: 16px;
    font-weight: bold;
    margin-bottom: 16px;
    margin-top: 30px;
  }
  
  .feature-list {
    list-style: none;
    padding: 0;
    margin: 0 0 30px 0;
    
    .feature-item {
      display: flex;
      align-items: flex-start;
      margin-bottom: 12px;
      font-size: 12px;
      line-height: 1.5;
      color: rgba(255, 255, 255, 0.8);
      
      .check-icon {
        color: #00b897;
        margin-right: 8px;
        font-weight: bold;
      }
    }
  }
}
</style>
