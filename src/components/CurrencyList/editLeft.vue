<template>
  <div class="editLeft">
    <!-- <svg-load name="btc" class="leftImg"></svg-load> -->
    <image-load :filePath="data.logo" v-if="showLeftImg" class="leftImg" />
    <div class="leftNames">
      <div class="fw-bold">{{ data.showSymbol }}</div>

      <!-- <div class="fw-bold">{{ data.coinUpperCase }}</div>
      <div class="second">/{{ data.baseCoinUpperCase }}</div> -->
    </div>
    <!-- <div class="leftName ff-num">10x</div> -->
  </div>
</template>
<script setup>
const props = defineProps({
  showLeftImg: {
    type: Boolean,
    default: true
  },
  data: {
    type: Object,
    default: () => {}
  }
})
</script>
<style lang="scss" scoped>
.editLeft {
  flex: 1;
  display: flex;
  align-items: center;
  padding: 12px;
  background: linear-gradient(145deg, #1a1a1a, #2a2a2a);
  border-radius: 12px;
  box-shadow: 5px 5px 15px rgba(0, 0, 0, 0.3),
              -5px -5px 15px rgba(255, 255, 255, 0.02);
  transition: all 0.3s ease;
  
  &:active {
    transform: scale(0.98);
    box-shadow: 3px 3px 10px rgba(0, 0, 0, 0.4),
                -3px -3px 10px rgba(255, 255, 255, 0.02);
  }

  .leftImg {
    width: 28px;
    height: 28px;
    margin-right: 12px;
    border-radius: 50%;
    box-shadow: 2px 2px 8px rgba(0, 0, 0, 0.2);
    transition: transform 0.3s ease;
    
    &:hover {
      transform: rotate(360deg);
    }
  }

  .leftNames {
    display: flex;
    align-items: baseline;
    font-size: 16px;
    color: #ffffff;
    text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.3);
    
    .fw-bold {
      background: linear-gradient(45deg, #ffffff, #cccccc);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      font-weight: 600;
    }

    .second {
      color: #888888;
      font-size: 13px;
      margin-left: 4px;
      opacity: 0.8;
    }
  }

  .leftName {
    margin-left: 8px;
    padding: 4px 8px;
    font-size: 12px;
    color: #ffffff;
    border-radius: 4px;
    background: linear-gradient(145deg, #2a2a2a, #333333);
    box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.2);
    transition: all 0.3s ease;
    
    &:hover {
      background: linear-gradient(145deg, #333333, #2a2a2a);
      transform: translateY(-1px);
    }
  }
}

// 添加暗色主题变量覆盖
:root {
  --ex-default-font-color: #ffffff;
  --ex-passive-font-color: #888888;
  --ex-font-color2: #cccccc;
  --ex-div-bgColor6: #2a2a2a;
}
</style>
