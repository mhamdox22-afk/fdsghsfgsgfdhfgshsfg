<template>
  <div :style="containerStyle" class="dropdown-container" @click="onClick">
    <span class="icon-text" style="margin: 0 auto;">{{ icon }}</span>
    <div class="dropdown-list">
      <div v-for="(item, index) in menuItems" 
           :key="index"
           class="dropdown-item"
           @click.stop="item.onClick && item.onClick()">
        <span class="item-content">
          <span class="item-icon">{{ item.icon }}</span>
          {{ item.label }}
        </span>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  icon: {
    type: String,
    required: true
  },
  menuItems: {
    type: Array,
    required: true
  },
  containerStyle: {
    type: Object,
    default: () => ({})
  },
  onClick: {
    type: Function,
    default: () => {}
  }
})
</script>

<style scoped>
.dropdown-container {
  position: relative;
  display: flex;
  align-items: center;
  height: 100%;
}

.dropdown-list {
  position: absolute;
  top: 100%;
  left: 0;
  background: #262626;
  border-radius: 6px;
  padding: 8px 0;
  display: none;
  min-width: 180px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.15);
}

.dropdown-container:hover .dropdown-list {
  display: block;
}

.dropdown-item {
  padding: 12px 16px;
  cursor: pointer;
}

.dropdown-item:hover {
  background: rgba(255,255,255,0.1);
}

.item-content {
  display: flex;
  align-items: center;
  color: #7A7A97;
  font-size: 14px;
}

.item-icon {
  margin-right: 8px;
  font-size: 16px;
}

.icon-text {
  font-size: 16px;
  cursor: pointer;
}
</style> 