<script setup>
import { _t18 } from '@/utils/public'
import HeaderBar from '@/components/HeaderBar/index.vue'
// import Success from './components/success.vue'
import { reactive, ref, onMounted, computed } from 'vue'
import { _div, _mul, priceFormat } from '@/utils/decimal.js'
import { getLoanProductList, getLoan } from '@/api/loan.js'
import { dict } from '@/api/common/index'
import { uploadImg } from '@/api/common/index.js'
import { showToast } from 'vant'
import { useToast } from '@/hook/useToast'
const { _toast } = useToast()
import { _toView } from '@/utils/public'
import { useRouter } from 'vue-router'
const router = useRouter()
const ProductList = ref([])
const amount = ref('')

// 当前选择还款周期
const currentProduct = ref({})
// 当前商品
const productDetail = ref({})
const repaymentCycle = ref([])
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
//利息
const interest = computed(() => {
  let tempValue = 0
  if (amount.value && productDetail?.value.odds && productDetail?.value.cycleType) {
    tempValue = priceFormat(
      _div(_mul(_mul(amount.value, productDetail.value.odds), productDetail.value.cycleType), 100),
      2,
      'up'
    )
  }

  return tempValue
})
const cuttentRight = {
  iconRight: [
    { iconName: 'guize', clickTo: '/loan-rule' },
    { iconName: 'jilu', clickTo: '/loan-record' }
  ]
}
const showNation = ref(false)
onMounted(async () => {
  await getDict()
  getProductList()
})
const dictList = ref()
const getDict = async () => {
  const res = await dict('t_repay_type')
  if (res.code == '200') {
    dictList.value = res.data
  }
}
/**借贷产品 */
const getProductList = async () => {
  const res = await getLoanProductList()
  if (res.code == '200') {
    ProductList.value = res.rows
    repaymentCycle.value = res.rows.map((elem, index) => {
      return {
        id: index,
        name: elem.cycleType, //周期
        value: elem.id,
        amountMin: elem.amountMin,
        amountMax: elem.amountMax,
        repayTypeLabel: dictList.value?.filter((item) => {
          return parseInt(item.dictValue) == elem.repayType
        })[0].dictLabel
      }
    })
    selectorAction(repaymentCycle.value[0])
  }
}

const selectorAction = (item) => {
  currentProduct.value = item
  productDetail.value = ProductList.value.find((elem) => elem.id == item.value)
  showNation.value = false
}
const submit = () => {
  if (
    amount.value == '' ||
    amount.value > productDetail.value.amountMax ||
    amount.value < productDetail.value.amountMin
  ) {
    // showErr.value = true
    _toast(`loan_amountErr`)
    return
  }
  if (fileList1.value.length == 0) {
    _toast(`loan_idcardFront`)
    return
  }
  if (fileList2.value.length == 0) {
    _toast(`loan_idcardReverse`)
    return
  }
  if (fileList3.value.length == 0) {
    _toast(`loan_hand`)
    return
  }
  const file1 = fileList1.value[0] || {}
  let filePath1 = file1.res
  const file2 = fileList2.value[0] || {}
  let filePath2 = file2.res
  const file3 = fileList3.value[0] || {}
  let filePath3 = file3.res

  getLoan({
    proId: productDetail.value.id,
    cycleType: productDetail.value.cycleType,
    cardUrl: filePath1,
    cardBackUrl: filePath2,
    capitalUrl: filePath3,
    amount: amount.value
  }).then((res) => {
    if (res.code == '200') {
      _toast(`loan_applySuccess`)
      setTimeout(() => {
        router.push('/loan-record')
      }, 500)
    } else {
      showToast(res.msg)
    }
  })
}

const showErr = ref(false)
const blur = () => {
  if (
    amount.value > productDetail.value.amountMax ||
    amount.value < productDetail.value.amountMin
  ) {
    showErr.value = true
    // 借贷金额超出可借范围
    _toast(`loan_amountMore`)
    amount.value = ''
  }
}
</script>
<template>
  <!-- 助力贷 -->
  <HeaderBar
    :currentName="_t18('asset_loan')"
    :cuttentRight="cuttentRight"
    :border_bottom="true"
  ></HeaderBar>
  <div class="content">
    <div class="top">
      <!-- 经平台审核，您可向平台申请一笔借款！ -->
      <strong class="tip">{{ _t18('loan_msg') }}</strong>
      <!-- 借贷产品 -->
      <div class="type">{{ _t18('loan_product') }} <span></span></div>
      <div class="form">
        <div class="formInput" @click="showNation = true">
          <!-- <input v-model="productDetail.amount" class="form-input" disabled /> -->
          <p>{{ productDetail.amountMin }}-{{ productDetail.amountMax }}</p>
        </div>
      </div>
      <!-- 借款金额 -->
      <div class="type">{{ _t18('loan_amount') }} <span>(USDT)</span></div>
      <div class="form">
        <div class="formInput">
          <!-- 输入贷款金额 -->
          <input
            v-model="amount"
            class="form-input"
            :placeholder="_t18('loan_pleaseInput')"
            @blur="blur"
          />
          <!-- <span v-if="showErr">*金额不符合</span> -->
        </div>
      </div>

      <van-action-sheet
        v-model:show="showNation"
        style="max-width: var(--ex-max-width); left: 50%; translate: -50%"
        ><template #default>
          <ul class="action">
            <li v-for="item in repaymentCycle" :key="item.id" @click="selectorAction(item)">
              <div class="selector">
                <span>{{ item.amountMin }}-{{ item.amountMax }} USDT</span>
              </div>
            </li>
          </ul>
        </template>
      </van-action-sheet>
      <!-- 还款周期 -->
      <div class="type">{{ _t18('loan_cycle') }}</div>
      <div class="form">
        <div class="formInput">
          <input v-model="productDetail.cycleType" class="form-input" disabled />
        </div>
      </div>
    </div>

    <!-- 借贷信息 -->
    <div class="loan-detail">
      <div class="item">
        <!-- 日利率 -->
        <div class="left">{{ _t18('loan_rateDay') }}</div>
        <div class="right">{{ productDetail.odds }}%</div>
      </div>
      <div class="item">
        <!-- 利息 -->
        <div class="left">{{ _t18('loan_interest') }}</div>
        <div class="right">{{ interest }} USDT</div>
      </div>
      <div class="item">
        <!-- 还款方式 -->
        <div class="left">{{ _t18('loan_repayType') }}</div>
        <!-- 到期一次还本息 -->
        <div class="right">{{ currentProduct.repayTypeLabel || '' }}</div>
      </div>
      <div class="item">
        <!-- 放款机构 -->
        <div class="left">{{ _t18('loan_repayOrg') }}</div>
        <div class="right">{{ productDetail.repayOrg }}</div>
      </div>
    </div>

    <div class="bottom">
      <div class="upload">
        <!-- 证件照 请确保证件照清晰可见-->
        <div class="photo">
          {{ _t18('loan_idImg') }} <span class="hui">({{ _t18('loan_idMsg') }})</span>
        </div>
      </div>
      <div class="upload-box">
        <div class="item">
          <!-- 上传正面 -->
          <van-uploader :after-read="afterRead1" v-model="fileList1">
            <img src="@/assets/defi/delete.png" alt="" class="img" />
            <div class="tit">{{ _t18('loan_front') }}</div>
          </van-uploader>
        </div>
        <div class="item">
          <!-- 上传反面 -->
          <van-uploader :after-read="afterRead2" v-model="fileList2">
            <img src="@/assets/defi/delete.png" alt="" class="img" />
            <div class="tit">{{ _t18('loan_reverse') }}</div>
          </van-uploader>
        </div>
        <div class="item">
          <!-- 上传手持证件照 -->
          <van-uploader :after-read="afterRead3" v-model="fileList3">
            <img src="@/assets/defi/delete.png" alt="" class="img" />
            <div class="tit">{{ _t18('loan_hand') }}</div>
          </van-uploader>
        </div>
      </div>
      <!-- 确认上传 -->
      <div class="btn" @click="submit">{{ _t18('loan_require') }}</div>
    </div>
  </div>
  <!-- <Success :text="'认证成功'" :imgUrl="'/src/assets/defi/success.png'"></Success> -->
</template>
<style lang="scss" scoped>
.content {
  padding-top: 20px;
  background: #121212;
  min-height: 100vh;
  position: relative;
  overflow: hidden;
  
  // Add background image
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-image: url('@/assets/icons/bg_5_mobile.png');
    background-size: cover;
    background-position: center;
    background-attachment: fixed;
    opacity: 0.4;
    z-index: 0;
    pointer-events: none;
  }

  // Make sure all content stays above the background
  .top, .loan-detail, .bottom {
    position: relative;
    z-index: 1;
  }

  .top {
    padding: 0 15px;

    .tip {
      display: block;
      font-size: 14px;
      color: rgba(255, 255, 255, 0.95);
      margin-bottom: 30px;
      animation: fadeIn 0.8s ease;
    }

    .type {
      font-size: 14px;
      color: rgba(255, 255, 255, 0.95);
      margin-bottom: 10px;
      animation: slideIn 0.5s ease;

      span {
        font-size: 14px;
        color: rgba(255, 255, 255, 0.8);
      }
    }

    .form {
      .formInput {
        padding: 0 15px;
        display: flex;
        justify-content: space-between;
        background: rgba(255, 255, 255, 0.05);
        border-radius: 8px;
        align-items: center;
        margin-bottom: 20px;
        backdrop-filter: blur(10px);
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
        transition: all 0.3s ease;

        &:hover {
          transform: translateY(-2px);
          box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
          background: rgba(255, 255, 255, 0.08);
        }

        input {
          flex: 1;
          width: 100%;
          height: 50px;
          font-size: 14px;
          background: transparent;
          color: rgba(255, 255, 255, 0.95);

          &::placeholder {
            color: rgba(255, 255, 255, 0.7);
          }
        }

        p {
          width: 100%;
          height: 50px;
          font-size: 14px;
          line-height: 50px;
          color: rgba(255, 255, 255, 0.95);
        }
      }
    }
  }

  .loan-detail {
    margin-top: 30px;
    border-top: 1px solid rgba(255, 255, 255, 0.1);
    border-bottom: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(255, 255, 255, 0.03);
    backdrop-filter: blur(10px);
    animation: fadeIn 1s ease;

    .item {
      padding: 0 15px;
      margin: 30px 0;
      display: flex;
      justify-content: space-between;
      align-items: center;
      transition: transform 0.3s ease;

      &:hover {
        transform: translateX(5px);
      }

      .left {
        font-size: 14px;
        color: rgba(255, 255, 255, 0.8);
      }

      .right {
        font-size: 14px;
        color: rgba(255, 255, 255, 0.95);
        text-align: right;
      }
    }
  }

  .bottom {
    padding: 0 15px;

    .upload {
      margin: 30px 0 10px 0;

      .photo {
        font-size: 14px;
        color: rgba(255, 255, 255, 0.95);
      }

      span {
        font-size: 14px;
        color: rgba(255, 255, 255, 0.8);
      }
    }

    .upload-box {
      .item {
        height: 194px;
        background: rgba(255, 255, 255, 0.03);
        border-radius: 12px;
        border: 1px solid rgba(255, 255, 255, 0.1);
        margin-bottom: 15px;
        display: flex;
        flex-direction: column;
        align-items: center;
        overflow: hidden;
        transition: all 0.3s ease;
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);

        &:hover {
          transform: translateY(-3px);
          box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
          border-color: rgba(255, 255, 255, 0.2);
        }

        .van-uploader {
          width: 100%;

          :deep(.van-uploader__wrapper) {
            width: 100%;
            height: 100%;
            display: block;

            .van-uploader__input-wrapper {
              display: flex;
              flex-direction: column;
              align-items: center;
            }

            .van-uploader__preview {
              width: 100%;
              height: 100%;
              margin: auto;

              .van-uploader__preview-image {
                width: 100%;
                height: 194px;
              }
            }

            .van-uploader__preview-delete {
              width: 20px;
              height: 20px;

              .van-uploader__preview-delete-icon {
                font-size: 22px;
              }
            }
          }
        }

        .img {
          // width: 100%;
          // height: 100%;
          width: 50px;
          height: 50px;
          min-width: none;
          min-height: none;
          object-fit: contain;
          margin: 50px 0 20px 0;
        }

        .tit {
          box-sizing: border-box;
          width: 100%;
          text-align: center;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 14px;
          font-weight: 400;
          color: rgba(255, 255, 255, 0.9);
        }
      }
    }

    .btn {
      font-size: 16px;
      height: 54px;
      display: flex;
      justify-content: center;
      align-items: center;
      background: linear-gradient(45deg, #2196F3, #1E88E5);
      border-radius: 8px;
      color: rgba(255, 255, 255, 0.95);
      font-weight: 500;
      margin: 50px 0;
      transition: all 0.3s ease;
      box-shadow: 0 4px 15px rgba(33, 150, 243, 0.3);

      &:active {
        transform: scale(0.98);
      }

      &:hover {
        background: linear-gradient(45deg, #1E88E5, #1976D2);
        box-shadow: 0 6px 20px rgba(33, 150, 243, 0.4);
      }
    }
  }
}

// 添加动画关键帧
@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes slideIn {
  from {
    transform: translateX(-20px);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

// Action Sheet 样式优化
:deep(.van-action-sheet__content) {
  background: #1a1a1a;
  border-radius: 16px 16px 0 0;
}

.action {
  li {
    color: rgba(255, 255, 255, 0.95);
    text-align: center;
    line-height: 54px;
    font-size: 16px;
    border-bottom: 1px solid rgba(255, 255, 255, 0.1);
    transition: all 0.3s ease;

    &:hover {
      background: rgba(255, 255, 255, 0.05);
    }

    &:active {
      background: rgba(255, 255, 255, 0.08);
    }
  }
}
</style>
