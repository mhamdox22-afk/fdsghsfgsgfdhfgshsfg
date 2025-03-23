<!-- 高级认证 -->
<script setup>
import { reactive, ref, onMounted } from 'vue'
import { computed } from 'vue'
import { REALNAME } from '@/config'
import { nation } from './components/nation.js'
import HeaderBar from '@/components/HeaderBar/index.vue'
import { uploadKYC } from '@/api/user'
import Success from './components/success.vue'
import { uploadImg } from '@/api/common/index.js'
import { useUserStore } from '@/store/user/index'
import { storeToRefs } from 'pinia'
import { showToast } from 'vant'
import { _t18 } from '@/utils/public'
import { useToast } from '@/hook/useToast'
const { _toast } = useToast()
const userStore = useUserStore()
userStore.getUserInfo()
/**
 * 多语言国家列表
 */
const nationList = computed(() => {
  return nation.map((elem) => {
    elem.title = `nation_${elem.name}`
    return elem
  })
})

/**
 * 国家搜索功能
 */
const nationSearch = ref('')
const filteredNationList = computed(() => {
  if (!nationSearch.value) {
    return nationList.value
  }
  return nationList.value.filter((item) => {
    // 将翻译后的国家名转为小写进行比较
    const translatedName = _t18(item.title).toLowerCase()
    const searchTerm = nationSearch.value.toLowerCase()
    return translatedName.includes(searchTerm)
  })
})

const handleNationSearch = () => {
  // 实时搜索，不需要额外操作
}

// 用户信息
const { userInfo } = storeToRefs(userStore)

const formData = reactive({
  userName: '',
  number: ''
})
const cuttentRight = { iconRight: [{ iconName: 'kefu', clickTo: 'event_serviceChange' }] }
// 是否高级认证
const advancedAuth = ref(userInfo.value.detail?.auditStatusAdvanced)
const title = ref(_t18('advanced_IdCard'))
const type = ref('1')
const show = ref(false)
const actions = [
  { name: _t18('advanced_IdCard'), type: 1 }, //身份证
  { name: _t18('advanced_paper'), type: 2 }, //护照
  { name: _t18('advanced_driver_license'), type: 3 } //驾驶证
]
const onSelect = (item) => {
  show.value = false
  title.value = item.name
  type.value = item.type
}

/**
 * 当前选中
 */
const nationName = ref('')
/**
 * 显示国家弹窗
 */
const showNation = ref(false)
/**
 * 选择国家
 * @param {*} item
 */
const onSelectNation = (item) => {
  showNation.value = false
  nationName.value = item.title
}
const fileList1 = ref([])
const fileList2 = ref([])
const fileList3 = ref([])
const afterRead1 = (file) => {
  file.status = 'uploading'
  // 此时可以自行将文件上传至服务器
  // console.log(file)
  let formData = new FormData()
  formData.append('file', file.file)
  uploadImg(formData).then((res) => {
    res = res.data
    if (res.code == '200') {
      file.res = res.data.url
      file.status = 'success'
    } else {
      showToast(res.msg)
      fileList1.value = []
    }
  })
}
const afterRead2 = (file) => {
  file.status = 'uploading'
  // 此时可以自行将文件上传至服务器
  // console.log(file)
  let formData = new FormData()
  formData.append('file', file.file)
  uploadImg(formData).then((res) => {
    res = res.data
    if (res.code == '200') {
      file.res = res.data.url
      file.status = 'success'
    } else {
      showToast(res.msg)
      fileList2.value = []
    }
  })
}
const afterRead3 = (file) => {
  file.status = 'uploading'
  // 此时可以自行将文件上传至服务器
  // console.log(file)
  let formData = new FormData()
  formData.append('file', file.file)
  uploadImg(formData).then((res) => {
    res = res.data
    if (res.code == '200') {
      file.res = res.data.url
      file.status = 'success'
    } else {
      showToast(res.msg)
      fileList3.value = []
    }
  })
}

/**
 * 提交认证信息
 * 审核状态 0未申请 1通过 3审核中 2拒绝
 */
const submit = () => {
  if (formData.userName == '') {
    // showToast('请填写姓名')
    _toast('advanced_please_name')
    return
  }
  if (!REALNAME.includes(__config._APP_ENV) && formData.number == '') {
    // showToast('请填写证件号码')
    _toast('advanced_please_number')
    return
  }
  if (fileList1.value.length == 0) {
    // showToast('请上传证件正面')
    _toast('loan_idcardFront')
    return
  }
  if (fileList2.value.length == 0) {
    // showToast('请上传证件反面')
    _toast('loan_idcardReverse')
    return
  }
  if (!REALNAME.includes(__config._APP_ENV) && fileList3.value.length == 0) {
    // showToast('请上传手持证件照')
    _toast('please_loan_hand')
    return
  }
  const file1 = fileList1.value[0] || {}
  let filePath1 = file1.res
  const file2 = fileList2.value[0] || {}
  let filePath2 = file2.res
  const file3 = fileList3.value[0] || {}
  let filePath3 = file3.res
  if (REALNAME.includes(__config._APP_ENV)) {
    file3.status = 'success'
  }
  if (file1.status != 'success' || file2.status != 'success' || file3.status != 'success') {
    // showToast('图片上传中,稍后重试')
    _toast('recharge_img_load')
    return
  }

  // realName 姓名 ,idCard 证件号码 ,flag 1 初级 2高级 ,frontUrl 正面照片 ,backUrl 反面照片 ,country 国家 ,handelUrl 手持照片 ,cardType 类型
  let params = `realName=${formData.userName}&idCard=${formData.number}&flag=2&frontUrl=${filePath1}&backUrl=${filePath2}&country=${nationName.value}&handelUrl=${filePath3}&cardType=${type.value}`
  uploadKYC(params).then((res) => {
    if (res.code == '200') {
      // showToast('提交成功，请等待...')
      _toast('please_wait')
      advancedAuth.value = 3
    } else {
      showToast(res.msg)
      advancedAuth.value = 2
    }
  })
}
/**
 * 认证失败 重新认证
 */
const reSubmit = () => {
  advancedAuth.value = '0'
}

/**
 * 初始化
 */
const init = () => {
  if (['aams'].includes(__config._APP_ENV)) {
    nationName.value = 'nation_United States of America'
  } else if (['das'].includes(__config._APP_ENV)) {
    nationName.value = 'nation_Singapore'
  } else {
    nationName.value = 'nation_Japan'
  }
}

onMounted(() => {
  init()
  if (REALNAME.includes(__config._APP_ENV)) {
    formData.userName = userInfo.value.user?.loginName
  }
})
</script>
<template>
  <!-- 导航条 -->
  <HeaderBar
    :currentName="_t18('sidebar_advanced', ['aams'])"
    :cuttentRight="cuttentRight"
    :border_bottom="true"
  ></HeaderBar>
  <div class="content" v-if="advancedAuth == '0' || advancedAuth == null">
    <div class="tip advanced_txt">{{ _t18('advanced_txt', ['aams']) }}</div>
    <div v-if="['coinsexpto'].includes(_getConfig('_APP_ENV'))" class="tip_info">
      {{
        `${_t18('advanced_certification_info_left')}${_getConfig('_APP_META_TITLE')}${_t18(
          'advanced_certification_info_right'
        )}`
      }}
    </div>
    <!-- 姓名 -->
    <!-- 隐藏实名认证姓名输入 -->
    <div class="form" v-if="!REALNAME.includes(_getConfig('_APP_ENV'))">
      <div class="formInput">
        <div class="label text-ellipsis2">{{ _t18('advanced_name') }}：</div>
        <input :placeholder="_t18('login_please')" v-model="formData.userName" class="form-input" />
      </div>
    </div>
    <!-- 身份证 -->
    <van-cell style="margin-top: 30px" is-link :title="title" @click="show = true" />
    <van-action-sheet
      v-model:show="show"
      :actions="actions"
      @select="onSelect"
      style="max-width: var(--ex-max-width); left: 50%; translate: -50%"
    >
    </van-action-sheet>
    <div class="form" v-if="!REALNAME.includes(_getConfig('_APP_ENV'))">
      <div class="formInput">
        <div class="label text-ellipsis2">{{ _t18('advanced_license_number') }}：</div>
        <input
          :placeholder="_t18('advanced_please_cardName')"
          v-model="formData.number"
          class="form-input"
        />
      </div>
    </div>
    <!-- 国家 -->
    <van-cell
      style="margin-top: 30px"
      is-link
      :title="_t18(nationName)"
      @click="showNation = true"
    />

    <!-- 自定义国家选择器 -->
    <div class="custom-nation-selector" v-show="showNation">
      <div class="selector-overlay" @click="showNation = false"></div>
      <div class="selector-content">
        <!-- 搜索框 -->
        <div class="selector-header">
          <div class="search-container">
            <i class="search-icon">
              <van-icon name="search" />
            </i>
            <input 
              type="text" 
              v-model="nationSearch" 
              :placeholder="_t18('enter_search_keywords')" 
              class="search-input"
            />
            <i class="clear-icon" v-if="nationSearch" @click="nationSearch = ''">
              <van-icon name="close" />
            </i>
          </div>
          <div class="close-button" @click="showNation = false">
            <van-icon name="cross" />
          </div>
        </div>
        
        <!-- 国家列表 -->
        <div class="nations-list">
          <div 
            v-for="(item, index) in filteredNationList" 
            :key="index"
            class="nation-item"
            @click="onSelectNation(item)"
          >
            {{ _t18(item.title) }}
          </div>
          <div class="no-results" v-if="filteredNationList.length === 0">
            {{ _t18('no_results_found') || '没有找到匹配结果' }}
          </div>
        </div>
      </div>
    </div>

    <div class="upload">
      <div class="photo">
        {{ _t18('advanced_id_photo') }} <span class="hui">({{ _t18('advanced_make_sure') }})</span>
      </div>
    </div>
    <div class="upload-box">
      <div class="item">
        <van-uploader :after-read="afterRead1" v-model="fileList1">
          <image-load filePath="defi/delete.png" name="delete" class="img"></image-load>
          <div class="tit">{{ _t18('upload_positive') }}</div>
        </van-uploader>
      </div>
      <div class="item">
        <van-uploader :after-read="afterRead2" v-model="fileList2">
          <image-load filePath="defi/delete.png" name="delete" class="img"></image-load>
          <div class="tit">{{ _t18('upload_reverse side') }}</div>
        </van-uploader>
      </div>
      <div class="item" v-if="!REALNAME.includes(_getConfig('_APP_ENV'))">
        <van-uploader :after-read="afterRead3" v-model="fileList3">
          <image-load filePath="defi/delete.png" name="delete" class="img"></image-load>
          <div class="tit" v-if="['coinsexpto'].includes(_getConfig('_APP_ENV'))">
            {{ _t18('Upload_passport_photo2') }}
          </div>
          <div class="tit" v-else>{{ _t18('Upload_passport_photo') }}</div>
        </van-uploader>
      </div>
    </div>
    <div class="btnBox" @click="submit" >
      <ButtonBar :btnValue="_t18('advanced_submit')"  />
    </div>
  </div>

  <div class="success" v-if="advancedAuth == '3'">
    <Success :text="_t18('under_review')" :imgUrl="'defi/zhong.png'" :color="'#333'"></Success>
  </div>
  <div class="success" v-if="advancedAuth == '2'">
    <Success :text="_t18('Audit_failure')" :imgUrl="'defi/failed.png'" :color="'#333'">
      <template #btn>
        <div class="btnBox" @click="reSubmit">
          <ButtonBar :btnValue="_t18('Resubmit')" />
        </div>
      </template>
    </Success>
  </div>
  <div class="success" v-if="advancedAuth == '1'">
    <Success
      :text="_t18('advanced_success')"
      :imgUrl="'defi/success.png'"
      :color="'#333'"
    ></Success>
  </div>
</template>
<style lang="scss" scoped>
// 删除所有CSS变量，直接使用具体颜色值
.content {
  background-color: #000000;
  padding: 20px 16px;
  min-height: 100vh;

  // 标签样式
  .姓名, .证件, .证件号, .国家, .证件照 {
    color: #ffffff;
    font-size: 16px;
    margin: 20px 0 10px 0;
    text-align: left;
  }

  // 提示文本
  .tip {
    font-size: 14px;
    color: #ffffff;
    margin-bottom: 25px;
  }
  
  .tip_info {
    font-size: 14px;
    color: #ffffff;
  }

  .advanced_txt {
    margin-bottom: 20px;
  }

  // 表单样式
  .form {
    .formInput {
      background: #1a1a1a;
      border-radius: 12px;
      border: 1px solid rgba(255, 255, 255, 0.1);
      display: flex;
      align-items: center;
      margin-bottom: 30px;
      padding: 0 15px;

      .label {
        min-width: 80px;
        font-size: 15px;
        color: #ffffff;
        text-align: left;
      }

      input {
        width: 100%;
        height: 54px;
        font-size: 15px;
        background-color: transparent;
        color: #ffffff;
        border: none;
        outline: none;

        &::placeholder {
          color: rgba(255, 255, 255, 0.4);
        }
      }
    }
  }

  // 选择器样式
  .van-cell {
    background: #1a1a1a !important;
    border-radius: 12px !important;
    height: 54px;
    margin-bottom: 30px;
    border: 1px solid rgba(255, 255, 255, 0.1) !important;
    padding: 0 15px;
    font-size: 15px;
    color: #ffffff !important;
    display: flex;
    align-items: center;
    
    .van-cell__right-icon {
      color: #ffffff !important;
    }
  }

  // 上传区域
  .upload {
    margin-bottom: 15px;

    .photo {
      font-size: 15px;
      color: #ffffff;
      margin-bottom: 10px;
    }

    span {
      font-size: 13px;
      color: rgba(255, 255, 255, 0.6);
    }
  }

  .upload-box {
    .item {
      background: #1a1a1a;
      border-radius: 12px;
      border: 1px solid rgba(255, 255, 255, 0.1);
      height: 180px;
      margin-bottom: 15px;
      overflow: hidden;

      .van-uploader {
        width: 100%;
        height: 100%;

        .img {
          width: 40px;
          height: 40px;
          margin: 0 auto 10px;
          display: block;
        }

        .tit {
          font-size: 14px;
          color: #ffffff;
          text-align: center;
        }
      }

      :deep(.van-uploader__wrapper) {
        width: 100%;
        height: 100%;
        display: flex;
        flex-direction: column;
        justify-content: center;
      }

      :deep(.van-uploader__upload) {
        margin: 0 auto;
        height: 100%;
        width: 100%;
        background: transparent;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
      }

      :deep(.van-uploader__preview) {
        width: 100%;
        height: 100%;
        margin: 0;

        .van-uploader__preview-image {
          width: 100%;
          height: 100%;
          object-fit: cover;
        }

        .van-uploader__preview-delete {
          top: 10px;
          right: 10px;
          padding: 4px;
          background: rgba(0, 0, 0, 0.5);
          border-radius: 50%;
        }
      }
    }
  }

  // 按钮样式
  .btnBox {
    width: 100%;
    margin: 40px 0 20px;
    
    :deep(.button-bar) {
      background: linear-gradient(90deg, #4a66f7 0%, #1c90f7 100%);
      border-radius: 12px;
      height: 50px;
      font-size: 16px;
      font-weight: 500;
      color: #ffffff;
    }
  }
}

// 弹出层样式 - 全面改进 van-action-sheet
:deep(.van-action-sheet) {
  background-color: #000000 !important;
  border-radius: 16px 16px 0 0 !important;
  overflow: hidden;
  box-shadow: 0 -4px 20px rgba(0, 0, 0, 0.6) !important;
  
  .van-action-sheet__content {
    padding: 8px 0;
  }
  
  .van-action-sheet__item {
    color: #ffffff !important;
    font-size: 16px;
    height: 56px;
    background-color: #2e2d2d !important;
    margin: 0;
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
    
    &::after {
      content: '';
      position: absolute;
      left: 15px;
      right: 15px;
      bottom: 0;
      height: 1px;
      background: rgba(255, 255, 255, 0.1);
    }
    
    &:last-child::after {
      display: none;
    }
    
    &:active {
      background-color: #1a1a1a !important;
    }
  }
  
  .van-action-sheet__cancel {
    color: #ffffff !important;
    background-color: #000000 !important;
    margin-top: 8px;
    position: relative;
    height: 56px;
    font-size: 16px;
    display: flex;
    align-items: center;
    justify-content: center;
    
    &::before {
      content: '';
      position: absolute;
      left: 0;
      right: 0;
      top: 0;
      height: 8px;
      background: #1a1a1a;
    }
    
    &:active {
      background-color: #1a1a1a !important;
    }
  }
  
  .van-action-sheet__header {
    color: #ffffff !important;
    background-color: #000000 !important;
    height: 56px;
    font-weight: 500;
    
    .van-action-sheet__close {
      color: #ffffff !important;
      opacity: 0.8;
      
      &:active {
        opacity: 1;
      }
    }
  }
  
  .van-hairline--top::after,
  .van-hairline--bottom::after,
  .van-hairline--left::after,
  .van-hairline--right::after,
  .van-hairline::after,
  .van-hairline--top-bottom::after {
    border-color: rgba(255, 255, 255, 0.1) !important;
  }
  
  // 为选项添加轻微动效
  .van-action-sheet__item {
    transition: background-color 0.2s;
    
    &:active {
      transition: background-color 0.1s;
    }
  }
  
  // 自定义模板中的内容
  .van-action-sheet__gap {
    background-color: #1a1a1a !important;
    height: 8px;
  }
  
  // 国家选择框样式
  div[key] {
    color: #ffffff !important;
    font-size: 16px;
  }
}

// 蒙层样式优化
:deep(.van-overlay) {
  background-color: rgba(0, 0, 0, 0.7) !important;
  backdrop-filter: blur(3px);
}

// 修改头部文字颜色
:deep(.header-bar) {
  .center {
    color: #ffffff !important;
  }
  
  .icon-kefu {
    color: #ffffff !important;
  }
}

// 成功状态页面
.success {
  margin: 100px 0;
  
  :deep(.success-component) {
    img {
      width: 80px;
      height: 80px;
      margin-bottom: 20px;
    }
    
    .text {
      color: #ffffff !important;
      font-size: 16px;
      margin-bottom: 30px;
    }
  }

  :deep(.button-bar) {
    background: linear-gradient(90deg, #4a66f7 0%, #1c90f7 100%);
    border-radius: 12px;
    height: 50px;
    font-size: 16px;
    font-weight: 500;
    color: #ffffff;
  }
}

// 确保模板中的标签文字也为白色
:deep(.text-ellipsis2) {
  color: #ffffff;
}

// 修复相机图标
:deep(.van-uploader) {
  .img[name="delete"] {
    filter: brightness(1.2) !important;
  }
}

// 添加国家搜索样式
.action-sheet-header {
  position: relative;
  padding: 12px 12px 12px 12px;
  background-color: #000000;
  display: flex;
  align-items: center;
  
  :deep(.van-search) {
    flex: 1;
    padding: 0;
    margin-right: 8px;
    
    .van-search__content {
      background-color: #1a1a1a;
      border: 1px solid rgba(255, 255, 255, 0.1);
      border-radius: 20px;
      height: 36px;
    }
    
    .van-field__control {
      color: #ffffff;
      
      &::placeholder {
        color: rgba(255, 255, 255, 0.4);
      }
    }
    
    .van-field__left-icon {
      color: rgba(255, 255, 255, 0.6);
    }

    .van-field__clear {
      color: rgba(255, 255, 255, 0.4);
    }
  }
  
  .close-icon {
    color: #ffffff;
    font-size: 20px;
    padding: 4px;
  }
}

// 移除原来的header样式
:deep(.van-action-sheet__header) {
  display: none;
}

// 自定义国家列表滚动高度
:deep(.van-action-sheet__content) {
  max-height: 60vh;
  overflow-y: auto;
}

// 自定义国家选择器样式
.custom-nation-selector {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 2000;
  display: flex;
  align-items: flex-end;
  
  .selector-overlay {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.7);
    backdrop-filter: blur(3px);
  }
  
  .selector-content {
    position: relative;
    width: 100%;
    max-width: var(--ex-max-width);
    max-height: 75vh;
    margin: 0 auto;
    background-color: #000000;
    border-radius: 16px 16px 0 0;
    box-shadow: 0 -4px 20px rgba(0, 0, 0, 0.6);
    display: flex;
    flex-direction: column;
    z-index: 2001;
    animation: slide-up 0.3s ease;
  }
  
  .selector-header {
    padding: 16px;
    display: flex;
    align-items: center;
    border-bottom: 1px solid rgba(255, 255, 255, 0.1);
    
    .search-container {
      flex: 1;
      position: relative;
      height: 40px;
      background-color: #1a1a1a;
      border-radius: 20px;
      border: 1px solid rgba(255, 255, 255, 0.1);
      display: flex;
      align-items: center;
      padding: 0 12px;
      
      .search-icon {
        color: rgba(255, 255, 255, 0.6);
        margin-right: 8px;
      }
      
      .search-input {
        flex: 1;
        height: 100%;
        background-color: transparent;
        border: none;
        outline: none;
        color: #ffffff;
        font-size: 14px;
        
        &::placeholder {
          color: rgba(255, 255, 255, 0.4);
        }
      }
      
      .clear-icon {
        color: rgba(255, 255, 255, 0.4);
        padding: 4px;
        cursor: pointer;
      }
    }
    
    .close-button {
      margin-left: 12px;
      color: #ffffff;
      font-size: 20px;
      padding: 4px;
      cursor: pointer;
    }
  }
  
  .nations-list {
    flex: 1;
    overflow-y: auto;
    padding: 8px 0;
    
    .nation-item {
      height: 56px;
      padding: 0 16px;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #ffffff;
      font-size: 16px;
      background-color: #2e2d2d;
      position: relative;
      
      &::after {
        content: '';
        position: absolute;
        left: 15px;
        right: 15px;
        bottom: 0;
        height: 1px;
        background: rgba(255, 255, 255, 0.1);
      }
      
      &:last-child::after {
        display: none;
      }
      
      &:active {
        background-color: #1a1a1a;
      }
    }
    
    .no-results {
      padding: 20px;
      text-align: center;
      color: rgba(255, 255, 255, 0.6);
      font-size: 14px;
    }
  }
}

// 添加动画效果
@keyframes slide-up {
  from {
    transform: translateY(100%);
  }
  to {
    transform: translateY(0);
  }
}

// 添加滚动条样式
.nations-list {
  &::-webkit-scrollbar {
    width: 4px;
  }
  
  &::-webkit-scrollbar-track {
    background: rgba(255, 255, 255, 0.05);
  }
  
  &::-webkit-scrollbar-thumb {
    background: rgba(255, 255, 255, 0.2);
    border-radius: 2px;
  }
  
  &::-webkit-scrollbar-thumb:hover {
    background: rgba(255, 255, 255, 0.3);
  }
}
</style>

