<template>
  <div class="mobile-header">
    <div class="menu-icon" @click="toggleDrawer">
      <div class="menu-icon-wrapper" :class="{ active: drawerVisible }">
        <span class="line"></span>
        <span class="line"></span>
        <span class="line"></span>
      </div>
    </div>

    <Teleport to="body" v-if="drawerVisible">
      <div 
        class="drawer-mask animate__animated animate__fadeIn animate__faster"
        @click="closeDrawer"
      ></div>

      <div 
        class="drawer-wrapper animate__animated"
        :class="{
          'drawer-visible': drawerVisible,
          'animate__slideInLeft animate__faster': drawerVisible,
          'animate__slideOutLeft animate__faster': !drawerVisible
        }"
      >
        <div class="drawer-content">
          <div class="drawer-header animate__animated animate__fadeInDown animate__faster">
            <img src="https://tg-mahalebi.oss-cn-hongkong.aliyuncs.com/mahalebi/efa1d0500cbc45878caec29476da6cd3.png" class="logo animate__animated animate__pulse animate__slow animate__infinite" alt="logo" />
            <div class="close-button" @click="closeDrawer">
              <div class="close-icon"></div>
            </div>
          </div>

          <div class="menu-list">
            <template v-for="(item, index) in menuItems" :key="item.key">
              <!-- 普通菜单项 -->
              <div 
                v-if="!item.subMenu" 
                class="menu-item animate__animated animate__fadeInLeft"
                :style="{ 'animation-delay': `${index * 0.05}s` }"
                @click="handleMenuClick(item)"
              >
                <div class="menu-icon-box">
                  <span v-if="item.icon" class="emoji-icon">{{ item.icon }}</span>
                  <div v-else class="icon-placeholder"></div>
                </div>
                <span class="label">{{ $t(item.label) }}</span>
              </div>

              <!-- 带子菜单的项目 -->
              <div v-else class="submenu-wrapper animate__animated animate__fadeInLeft"
                   :style="{ 'animation-delay': `${index * 0.05}s` }">
                <div 
                  class="submenu-title"
                  @click="toggleSubmenu(item.key)"
                >
                  <div class="menu-icon-box">
                    <span v-if="item.icon" class="emoji-icon">{{ item.icon }}</span>
                    <div v-else class="icon-placeholder"></div>
                  </div>
                  <span class="label">{{ $t(item.label) }}</span>
                  <i 
                    class="iconfont icon-arrow-down"
                    :class="{ 'is-active': activeSubmenu === item.key }"
                  ></i>
                </div>
                <div 
                  class="submenu-content"
                  :class="{ 'is-expanded': activeSubmenu === item.key }"
                >
                  <div 
                    v-for="subItem in item.subMenu" 
                    :key="subItem.key"
                    class="sub-menu-item"
                    @click="handleMenuClick(subItem)"
                  >
                    <div class="menu-icon-box">
                      <span v-if="subItem.icon" class="emoji-icon">{{ subItem.icon }}</span>
                      <div v-else class="icon-placeholder"></div>
                    </div>
                    <span class="label">{{ $t(subItem.label) }}</span>
                  </div>
                </div>
              </div>
            </template>
          </div>
        </div>
      </div>
    </Teleport>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const drawerVisible = ref(false)
const activeSubmenu = ref('')

const toggleDrawer = () => {
  drawerVisible.value = !drawerVisible.value
  if (!drawerVisible.value) {
    activeSubmenu.value = ''
  }
}

const closeDrawer = () => {
  drawerVisible.value = false
  activeSubmenu.value = ''
}

const toggleSubmenu = (key) => {
  activeSubmenu.value = activeSubmenu.value === key ? '' : key
}

const menuItems = [
  {
    key: 'home',
    label: 'menu_home',
    path: '/',
    icon: '🏠'
  },
  {
    key: 'trade',
    label: 'menu_trade',
    path: '/trade',
    icon: '💱'
  },
  {
    key: 'quote',
    label: 'menu_quote',
    path: '/quote',
    icon: '📊'
  },
  {
    key: 'loan',
    label: 'menu_loan',
    path: '/loan',
    icon: '💰'
  },
  {
    key: 'financial',
    label: 'menu_financial',
    path: '/financial',
    icon: '📈'
  },
  {
    key: 'pledge',
    label: 'menu_pledge',
    path: '/pledge',
    icon: '🔒'
  },
  {
    key: 'finance',
    label: 'menu_finance',
    icon: '💎',
    subMenu: [
      {
        key: 'recharge',
        label: 'home_recharge',
        path: '/recharge',
        icon: '💳'
      },
      {
        key: 'withdraw',
        label: 'quick_withdrawal',
        path: '/withdraw',
        icon: '💸'
      },
      {
        key: 'plug',
        label: 'menu_promotion',
        path: '/plug',
        icon: '🎁'
      },
    ]
  },
  {
    icon: '👤',
    label: 'menu_user_center',
    description: 'menu_user_center_desc',
    path: '/assets',
  }
]

const handleMenuClick = (item) => {
  if (item.onClick) {
    item.onClick()
  } else if (item.path) {
    router.push(item.path)
  }
  drawerVisible.value = false
}

const handleQuickBuy = () => {
  // 实现快捷买币的逻辑
  console.log('Quick buy clicked')
}
</script>

<style lang="scss" scoped>
@import 'animate.css';

.mobile-header {
  padding: 12px 16px;
  
  .menu-icon {
    cursor: pointer;
    width: 24px;
    height: 24px;
    display: flex;
    align-items: center;
    
    .menu-icon-wrapper {
      width: 24px;
      height: 18px;
      position: relative;
      cursor: pointer;
      
      .line {
        position: absolute;
        width: 100%;
        height: 2px;
        background: linear-gradient(90deg, #00E0FF, #9F56FF);
        transition: all 0.3s ease;
        border-radius: 2px;
        
        &:nth-child(1) {
          top: 0;
        }
        
        &:nth-child(2) {
          top: 50%;
          transform: translateY(-50%);
        }
        
        &:nth-child(3) {
          bottom: 0;
        }
      }
      
      &.active {
        .line {
          &:nth-child(1) {
            transform: translateY(8px) rotate(45deg);
          }
          
          &:nth-child(2) {
            opacity: 0;
          }
          
          &:nth-child(3) {
            transform: translateY(-8px) rotate(-45deg);
          }
        }
      }
    }
  }
}

:global(.drawer-mask) {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.6);
  z-index: 9998;
  backdrop-filter: blur(4px);
  animation-duration: 0.12s !important;
}

:global(.drawer-wrapper) {
  position: fixed;
  top: 0;
  left: 0;
  width: 80vw;
  max-width: 375px;
  height: 100vh;
  background: linear-gradient(145deg, #1C1C1E, #2C2C2E);
  transform: translateX(-100%);
  transition: all 0.12s cubic-bezier(0.4, 0, 0.2, 1);
  z-index: 9999;
  box-shadow: 0 0 25px rgba(0, 0, 0, 0.3);
  animation-duration: 0.12s;
  border-right: 1px solid rgba(255, 255, 255, 0.1);

  @media (min-width: 768px) {
    left: 50%;
    margin-left: -187.5px; /* Half of max-width to center it */
    transform: translateX(-50%) translateY(0) translateX(-100%);
  }

  &.drawer-visible {
    transform: translateX(0);
    
    @media (min-width: 768px) {
      transform: translateX(-50%) translateY(0);
    }
    
    .menu-item, .submenu-wrapper {
      animation-duration: 0.3s !important;
    }
  }

  &.animate__slideOutLeft {
    animation-duration: 0.12s;
    
    @media (min-width: 768px) {
      transform: translateX(-50%) translateY(0) translateX(-100%);
    }
  }
}

.drawer-content {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: transparent;

  .drawer-header {
    position: relative;
    padding: 20px;
    background: rgba(255, 255, 255, 0.07);
    border-bottom: 1px solid rgba(255, 255, 255, 0.15);
    backdrop-filter: blur(15px);
    margin-bottom: 8px;
    animation-duration: 0.4s !important;
    
    .close-button {
      position: absolute;
      top: 20px;
      right: 20px;
      width: 32px;
      height: 32px;
      border-radius: 50%;
      background: rgba(255, 255, 255, 0.1);
      display: flex;
      align-items: center;
      justify-content: center;
      cursor: pointer;
      transition: all 0.2s;
      backdrop-filter: blur(4px);
      border: 1px solid rgba(255, 255, 255, 0.15);
      
      &:hover {
        background: rgba(255, 255, 255, 0.2);
        transform: rotate(90deg);
        
        .close-icon {
          &::before,
          &::after {
            background-color: #fff;
          }
        }
      }
      
      &:active {
        transform: rotate(90deg) scale(0.95);
      }
      
      .close-icon {
        position: relative;
        width: 16px;
        height: 16px;
        
        &::before,
        &::after {
          content: '';
          position: absolute;
          width: 100%;
          height: 2px;
          background-color: rgba(255, 255, 255, 0.9);
          border-radius: 2px;
          transition: all 0.2s;
          top: 50%;
          left: 0;
        }
        
        &::before {
          transform: translateY(-50%) rotate(45deg);
        }
        
        &::after {
          transform: translateY(-50%) rotate(-45deg);
        }
      }
    }

    .logo {
      height: 32px;
      width: auto;
      filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.3));
      animation-duration: 2s !important;
      &:hover {
        animation-name: animate__rubberBand !important;
      }
    }
  }

  .menu-list {
    flex: 1;
    padding: 12px 20px;
    overflow-y: auto;
    -webkit-overflow-scrolling: touch;
    perspective: 1000px;

    &::-webkit-scrollbar {
      width: 4px;
    }

    &::-webkit-scrollbar-track {
      background: transparent;
    }

    &::-webkit-scrollbar-thumb {
      background: linear-gradient(180deg, #00E0FF, #9F56FF);
      border-radius: 4px;
    }

    .menu-item, .submenu-title {
      padding: 12px;
      border: none;
      display: flex;
      align-items: center;
      gap: 12px;
      cursor: pointer;
      transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
      color: #fff;
      position: relative;
      overflow: hidden;
      
      &::before {
        content: '';
        position: absolute;
        inset: 0;
        background: linear-gradient(
          90deg,
          transparent,
          rgba(255, 255, 255, 0.1),
          transparent
        );
        transform: translateX(-100%);
        transition: transform 0.6s ease;
      }
      
      &:hover {
        transform: translateY(-2px) scale(1.02);
        background: linear-gradient(
          135deg,
          rgba(255, 255, 255, 0.12) 0%,
          rgba(255, 255, 255, 0.06) 100%
        );
        box-shadow: 
          0 8px 32px -4px rgba(0, 0, 0, 0.3),
          0 0 24px -2px rgba(0, 224, 255, 0.25),
          inset 0 0 12px rgba(159, 86, 255, 0.3);
        
        &::before {
          transform: translateX(100%);
        }

        .menu-icon-box {
          transform: rotateY(180deg);
          background: linear-gradient(135deg, #00E0FF, #9F56FF);
          
          .emoji-icon {
            transform: rotateY(-180deg);
            filter: brightness(1.2);
          }
        }
      }

      .menu-icon-box {
        flex: 0 0 42px;
        height: 42px;
        background: linear-gradient(
          135deg,
          rgba(0, 224, 255, 0.2),
          rgba(159, 86, 255, 0.2)
        );
        border-radius: 14px;
        display: flex;
        align-items: center;
        justify-content: center;
        transition: all 0.5s cubic-bezier(0.4, 0, 0.2, 1);
        box-shadow: 
          0 4px 12px rgba(0, 0, 0, 0.2),
          inset 0 2px 4px rgba(255, 255, 255, 0.1);
        
        .emoji-icon {
          font-size: 22px;
          transition: all 0.5s cubic-bezier(0.4, 0, 0.2, 1);
          filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.3));
        }
      }

      .label {
        flex: 1;
        font-size: 16px;
        font-weight: 500;
        line-height: 1.4;
        padding: 0 4px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        background: linear-gradient(90deg, #fff 60%, rgba(255, 255, 255, 0.8));
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        position: relative;
        
        &::after {
          content: '';
          position: absolute;
          bottom: -4px;
          left: 0;
          width: 0;
          height: 2px;
          background: linear-gradient(90deg, #00E0FF, #9F56FF);
          transition: width 0.3s ease;
          border-radius: 1px;
        }
        
        &:hover::after {
          width: 100%;
        }
      }
    }

    .submenu-wrapper {
      .submenu-content {
        max-height: 0;
        opacity: 0;
        overflow: hidden;
        transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
        padding: 0;
        margin: 0;
        
        &.is-expanded {
          max-height: 500px;
          opacity: 1;
          padding: 4px 0 4px 20px;
          margin: 4px 0;
          
          .sub-menu-item {
            animation: slideInAndFade 0.4s ease forwards;
            
            @for $i from 1 through 10 {
              &:nth-child(#{$i}) {
                animation-delay: #{$i * 0.05}s;
              }
            }
          }
        }

        .sub-menu-item {
          display: flex;
          align-items: center;
          padding: 12px;
        
          border: none;
         
          border-radius: 14px;
          transition: all 0.3s;
          gap: 12px;
          opacity: 0;
          transform: translateX(-10px);
          
          .menu-icon-box {
            flex: 0 0 32px;
            height: 32px;
            border-radius: 10px;
            font-size: 16px;
          }
          
          .label {
            flex: 1;
            font-size: 14px;
            font-weight: 500;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            padding: 0;
            line-height: 1.4;
          }
          
          &:hover {
            background: linear-gradient(
              135deg,
              rgba(255, 255, 255, 0.06),
              rgba(255, 255, 255, 0.02)
            );
            transform: translateX(8px);
            box-shadow: 
              0 6px 16px -4px rgba(0, 0, 0, 0.15),
              0 0 12px -2px rgba(0, 224, 255, 0.12),
              inset 0 0 8px rgba(159, 86, 255, 0.12);
            
            .menu-icon-box {
              transform: rotateY(180deg);
              background: linear-gradient(135deg, #00E0FF, #9F56FF);
              
              .emoji-icon {
                transform: rotateY(-180deg);
                filter: brightness(1.2);
              }
            }
          }
        }
      }
    }
  }
}

@keyframes slideInAndFade {
  0% {
    opacity: 0;
    transform: translateX(-10px);
  }
  100% {
    opacity: 1;
    transform: translateX(0);
  }
}

// 添加一些辅助动画类
.hover-scale {
  transition: transform 0.3s;
  &:hover {
    transform: scale(1.05);
  }
}

.hover-rotate {
  transition: transform 0.3s;
  &:hover {
    transform: rotate(5deg);
  }
}

.hover-bounce {
  &:hover {
    animation: animate__bounce 0.5s;
  }
}

.sub-menu-item {
  .label {
    font-size: 14px;
    padding-left: 0;
  }
}
</style>
