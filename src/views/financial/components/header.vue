<template>
  <div class="header">
   
    <div class="navigationList">
      <div
        class="item"
        v-for="item in listNav"
        :key="item.icon"
        @click="$router.push(item.clickName)"
      >
        <div class="icon-wrapper">
          <img :src="item.icon" class="itemImg" />
        </div>
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
// 导入图标
import appreciationIcon from '@/assets/icons/goods_ic_appreciation.svg'
import vipIcon from '@/assets/icons/goods_ic_vip.svg'
import normalIcon from '@/assets/icons/goods_ic_Normal.svg'
import investIcon from '@/assets/icons/goods_ic_invest.svg'
// 增值收益 会员专区 普通产品 我的投资
const listNav = computed(() => {
  return [
    {
      icon: appreciationIcon,
      name: _t18(`value_added _income`),
      clickName: '/addIncome'
    },
    {
      icon: vipIcon,
      name: _t18(`members_only`),
      clickName: '/membersOnly'
    },
    {
      icon: normalIcon,
      name: _t18(`normal_product`),
      clickName: '/generalMerchandise'
    },
    {
      icon: investIcon,
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
  background: #000;
  padding: 20px 16px;

  .navigationList {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;

    .item {
      display: flex;
      align-items: center;
      gap: 12px;
      
      .icon-wrapper {
        width: 48px;
        height: 48px;
        background: #1A1A1A;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;

        .itemImg {
          width: 50px;
          height: 50px;
        }
      }

      .itemName {
        color: #fff;
        font-size: 14px;
      }
    }
  }
}
</style>
