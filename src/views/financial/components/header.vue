<template>
  <div class="header">
   
    <div class="navigationList">
      <div
        class="item"
        v-for="item in listNav"
        :key="item.icon"
        @click="$router.push(item.clickName)"
      >
        <svg-load :name="item.icon" class="itemImg"></svg-load>
        <div class="itemName">{{ item.name }}</div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, computed, onMounted } from 'vue'
import { useI18n } from 'vue-i18n'
import { _t18 } from '@/utils/public'
import { publiceNotice } from '@/api/common/index'
// 增值收益 会员专区 普通产品 我的投资
const listNav = computed(() => {
  return [
    {
      icon: 'zhenzhi',
      name: _t18(`value_added _income`),
      clickName: '/addIncome'
    },
    {
      icon: 'huiyuan',
      name: _t18(`members_only`),
      clickName: '/membersOnly'
    },
    {
      icon: 'putong',
      name: _t18(`normal_product`),
      clickName: '/generalMerchandise'
    },
    {
      icon: 'touzhi',
      name: _t18(`my_invest`),
      clickName: '/myInvestment'
    }
  ]
})
const finaImg = ref('')
onMounted(async () => {
  try {
    const res = await publiceNotice('ACTIVITY_NOTICE', 'FINANCE_ACTIVITY ')
    if (res.code === 200) {
      finaImg.value = res.data[0].imgUrl
    }
  } catch (error) {}
})
</script>
<style lang="scss" scoped>
.header {
  background: #1a1a1a;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  padding: 20px 0;

  .bgImg {
    margin: 0 15px;
    height: 173px;
    border-radius: 16px;
    overflow: hidden;
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-5px);
    }

    .img {
      width: 100%;
      height: 100%;
      object-fit: cover;
      transition: transform 0.5s ease;

      &:hover {
        transform: scale(1.05);
      }
    }
  }

  .navigationList {
    padding: 19px 15px 0;
    display: flex;
    justify-content: space-between;
    gap: 10px;

    .item {
      flex: 1;
      text-align: center;
      align-items: center;
      font-size: 12px;
      justify-content: center;
      padding: 12px 5px;
      background: rgba(255, 255, 255, 0.05);
      border-radius: 12px;
      transition: all 0.3s ease;
      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);

      &:hover {
        transform: translateY(-3px);
        background: rgba(255, 255, 255, 0.08);
        box-shadow: 0 8px 25px rgba(0, 0, 0, 0.3);
      }

      .itemImg {
        width: 40px;
        height: 40px;
        margin-bottom: 10px;
        transition: transform 0.3s ease;

        &:hover {
          transform: scale(1.1);
        }
      }

      .itemName {
        text-align: center;
        word-wrap: break-word;
        color: rgba(255, 255, 255, 0.9);
        font-weight: 500;
        transition: color 0.3s ease;

        &:hover {
          color: #fff;
        }
      }
    }
  }
}
</style>
