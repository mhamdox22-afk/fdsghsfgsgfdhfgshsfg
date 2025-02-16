<template>
  <div class="slidebar">
    <div class="close">
      <svg-load name="guanbi" class="closeImg" @click="closeSideBar"></svg-load>
    </div>
    <UserLogin v-if="isSign"></UserLogin>
    <Navigation></Navigation>
    <!-- 未登录 -->
    <div v-if="!isSign" class="notLogged">
      <div class="btnBox" @click="_toView('/sign-in')">
        <!-- 登录 -->
        <ButtonBar :btnValue="_t18('login')" />
      </div>
      <div class="btnBox" @click="_toView('/sign-up')">
        <!-- 注册 -->
        <ButtonBar :btnReverse="false" :btnValue="_t18('register')" :btnColor="'#fff'" />
      </div>
    </div>
    <!-- 已登录 -->
    <div v-if="isSign && !userInfo.addressFlag" class="logged" @click="exit">
      <svg-load name="tuichu" class="loggedImg"></svg-load>
      <!-- 退出登录 -->
      <div>{{ _t18('layout', ['aams']) }}</div>
    </div>

    <!-- 退出弹窗 -->
    <!-- 您确定要退出登录吗 -->
    <Dialog
      v-model:value="showDialog"
      :title="``"
      :content="_t18('layout_require')"
      confirm-button-color="#17ac74"
      @cancelBtn="cancelBtn"
      @confirmBtn="confirmBtn"
      :confirmButtonText="_t18('btnConfirm', ['bitmake'])"
      :cancelButtonText="_t18('cancel')"
      z-index="200"
    ></Dialog>
  </div>
</template>
<script setup>
import { _back, _t18, _toView, _toReplace } from '@/utils/public'
import ButtonBar from '@/components/common/ButtonBar/index.vue'
import { signOut } from '@/api/user'
import { useUserStore } from '@/store/user/index'
import UserLogin from '../components/Sidebar/userLogin.vue'
import Navigation from '../components/Sidebar/navigation.vue'
import Dialog from '@/components/Dialog/index.vue'
import { useToast } from '@/hook/useToast'
const { _toast } = useToast()

const userStore = useUserStore()
const { userInfo } = storeToRefs(userStore)
// 判断是否登录
const isSign = ref(userStore.isSign)
/**
 * 显示弹窗
 */
const showDialog = ref(false)
// 退出登录
const exit = () => {
  showDialog.value = true
}
const confirmBtn = () => {
  showDialog.value = false
  signOut()
    .then((res) => {
      if (res.code == '200') {
        _toast('layout_success')
        // 清除token
        userStore.signOut()
        _toReplace('/')
        closeSideBar()
        isSign.value = false
        setTimeout(() => location.reload(), 10)
      }
    })
    .catch((err) => {
      console.log(err)
    })
}
const cancelBtn = () => {
  showDialog.value = false
}
const emit = defineEmits(['closeSideBar'])
const closeSideBar = () => {
  emit('closeSideBar')
}
</script>
<style lang="scss" scoped>
.slidebar {
  height: 100vh;
  padding-bottom: 100px;
  overflow: auto;
  background: #1a1a1a; // 深色背景
  color: #ffffff;
  box-shadow: -5px 0 15px rgba(0, 0, 0, 0.3);
  transition: all 0.3s ease;
  
  // 添加滚动条样式
  &::-webkit-scrollbar {
    width: 5px;
  }
  
  &::-webkit-scrollbar-track {
    background: #2a2a2a;
  }
  
  &::-webkit-scrollbar-thumb {
    background: #444;
    border-radius: 3px;
  }
}

.close {
  padding: 17px 15px 20px;
  display: flex;
  justify-content: flex-end;

  .closeImg {
    width: 24px;
    height: 24px;
    cursor: pointer;
    transition: transform 0.3s ease;
    filter: invert(1); // 将SVG图标改为白色

    &:hover {
      transform: rotate(90deg);
    }
  }
}

// 未登录
.notLogged {
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  padding: 50px 15px 30px;
  background: linear-gradient(180deg, #1a1a1a 0%, #252525 100%);

  .btnBox {
    margin-bottom: 20px;
    transform: translateY(0);
    transition: transform 0.2s ease;

    &:hover {
      transform: translateY(-2px);
    }
  }
}

// 登录
.logged {
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  margin-top: 50px;
  padding: 30px 15px;
  font-size: 14px;
  color: #ffffff;
  display: flex;
  align-items: center;
  cursor: pointer;
  transition: all 0.3s ease;
  background: linear-gradient(180deg, #252525 0%, #1a1a1a 100%);

  &:hover {
    background: #2a2a2a;
  }

  .loggedImg {
    width: 20px;
    height: 20px;
    margin-right: 20px;
    filter: invert(1); // 将SVG图标改为白色
    transition: transform 0.3s ease;
  }

  &:hover .loggedImg {
    transform: translateX(-5px);
  }

  div {
    color: #ffffff;
    font-weight: 300;
    letter-spacing: 0.5px;
  }
}

// 添加进入动画
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

.slidebar {
  animation: slideIn 0.3s ease-out;
}

// 添加玻璃拟态效果
.notLogged, .logged {
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
}

// 按钮悬停效果
:deep(.button-bar) {
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
  }
}
</style>
