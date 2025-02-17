<template>
    <div class="userAmount">
      <p class="userInfo ff-num animate-in">{{ priceFormat(amount) }}</p>
      <!-- 可用余额 -->
      <span class="animate-in-delayed">{{_t18('swap_available')}}(<i>{{ coin }}</i>)</span>
    </div>
  </template>
  
  <script setup>
  import { _t18 } from '@/utils/public'
  import { priceFormat } from '@/utils/decimal.js'
  const props = defineProps({
    amount: {
      type: Number
    },
    coin: {
      type: String,
      default: 'USDT'
    }
  })
  </script>
  
  <style lang="scss" scoped>
  * {
    font-size: 14px;
    color: #ffffff;
  }
  .userAmount {
    padding: 40px 20px;
    margin: 15px;
    border-radius: 20px;
    background: linear-gradient(145deg, #1a1a1a, #2d2d2d);
    box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.36);
    display: flex;
    flex-direction: column;
    align-items: center;
    position: relative;
    overflow: hidden;
    
    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: -100%;
      width: 100%;
      height: 100%;
      background: linear-gradient(
        90deg,
        transparent,
        rgba(255, 255, 255, 0.1),
        transparent
      );
      animation: shine 3s infinite;
    }

    p {
      margin-bottom: 20px;
      font-size: 46px;
      font-weight: 700;
      background: linear-gradient(90deg, #fff, #e0e0e0);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      text-shadow: 0 2px 10px rgba(255, 255, 255, 0.1);
    }

    span,
    i {
      font-size: 16px;
      color: rgba(255, 255, 255, 0.7);
      transition: all 0.3s ease;
    }
  }

  .animate-in {
    animation: fadeInUp 0.6s ease forwards;
  }

  .animate-in-delayed {
    animation: fadeInUp 0.6s ease forwards;
    animation-delay: 0.2s;
    opacity: 0;
  }

  @keyframes fadeInUp {
    from {
      opacity: 0;
      transform: translateY(20px);
    }
    to {
      opacity: 1;
      transform: translateY(0);
    }
  }

  @keyframes shine {
    0% {
      left: -100%;
    }
    20% {
      left: 100%;
    }
    100% {
      left: 100%;
    }
  }

  // 适配深色主题
  :root[theme-mode="dark"] {
    .userAmount {
      background: linear-gradient(145deg, #1a1a1a, #2d2d2d);
      border: 1px solid rgba(255, 255, 255, 0.1);
    }
  }

  // 添加触摸反馈
  @media (hover: hover) {
    .userAmount {
      transition: transform 0.3s ease, box-shadow 0.3s ease;
      
      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 12px 40px 0 rgba(0, 0, 0, 0.45);
      }
    }
  }
  </style>
  