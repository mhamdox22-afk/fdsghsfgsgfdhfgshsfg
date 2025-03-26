<!-- Tab切换组件 -->
<template>
  <div class="custom-tabs" :class="flexBetween ? 'betweenClass' : ''">
    <div class="tabs-nav">
      <div 
        v-for="(item, index) in props.tabList" 
        :key="index"
        class="tab-item"
        :class="{ 'active': props.active === index }"
        @click="change(index)"
      >
        {{ item }}
      </div>
    </div>
    <div class="tab-content">
      <slot name="tabContent"></slot>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  tabList: {
    type: Array,
    default: () => []
  },
  active: {
    type: Number
  },
  lineWidth: {
    type: String,
    default: '16'
  },
  // 两端对齐
  flexBetween: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['change'])
const change = (index) => {
  emit('change', index)
}
</script>

<style lang="scss" scoped>
.custom-tabs {
  background-color: #000;
  .tabs-nav {
    display: flex;
    background-color: #000;
    padding: 10px 0;
  }

  .tab-item {
    padding: 8px 16px;
    color: #fff;
    cursor: pointer;
    position: relative;
    text-align: center;

    &.active {
      color: #17AC74;
      
      &::after {
        content: '';
        position: absolute;
        bottom: -10px;
        left: 50%;
        transform: translateX(-50%);
        width: v-bind('props.lineWidth + "px"');
        height: 2px;
        background-color: #17AC74;
      }
    }
  }

  .tab-content {
    // border-top: 1px solid var(--ex-border-color);
    
    p {
      color: var(--ex-passive-font-color);
      padding: 30px 0;
      text-align: center;
    }
  }
}

.betweenClass {
  .tabs-nav {
    .tab-item:first-child {
      text-align: left;
      padding-left: 15px;
    }
    
    .tab-item:last-child {
      text-align: right;
      padding-right: 15px;
    }
  }
}
</style>
