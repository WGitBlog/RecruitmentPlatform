<script setup>
import { ElImage } from 'element-plus'
import 'element-plus/es/components/image/style/css'
import request from '@/utils/request'
import { User, Lock, Message } from '@element-plus/icons-vue'
import { onMounted, ref, watch } from 'vue'
import { userLogin, resetPassword } from '@/api/user'
import { useUserStore } from '@/stores/user'
import { useRegisterStore } from '@/stores/register'
import { useRouter } from 'vue-router'
import { getCandidateInfo } from '@/api/candidate.js'
import logoImage from '@/assets/a.jpg' // 确保这个路径是正确的
import { getBoosInfo } from '@/api/boos'
import { useBoosStore } from '@/stores/boos'
import { useCandidateStore } from '@/stores/candidate'



const candidateStore = useCandidateStore()
const boosStore=useBoosStore()
const rememberMe = ref(false)

// 处理记住我的变化
const handleRememberMeChange = (val) => {
  localStorage.setItem('rememberMe', val)
  if (!val) {
    // 如果取消记住我，清除存储的账号密码
    localStorage.removeItem('username')
    localStorage.removeItem('password')
  }
}

const router = useRouter()
const registerStore = useRegisterStore()
const captchaUrl = ref('') //图片的http路径
const CURegister = async () => {
  //判断注册角色
  registerStore.userRole = 'candidate'
  console.log(registerStore.userRole)
  if (
    formModel.value.password == formModel.value.repassword &&
    formModel.value.password != '' &&
    formModel.value.repassword != ''
  ) {
    ElMessage.success('注册Candidate成功')
    isRegister.value = false

    formModel.value.role = 'candidate'
    registerStore.userInfo = formModel.value
    router.push('/register')
  } else {
    ElMessage.error('注册Candidate失败')
  }
}

const CBRegister = async () => {
  //判断注册角色
  registerStore.userRole = 'boos'
  console.log(registerStore.userRole)
  if (
    formModel.value.password == formModel.value.repassword &&
    formModel.value.password != '' &&
    formModel.value.repassword != ''
  ) {
    ElMessage.success('注册Boos成功')
    isRegister.value = false

    formModel.value.role = 'boos'
    registerStore.userInfo = formModel.value

    router.push('/register')
  } else {
    ElMessage.error('注册Boos失败')
  }
}

onMounted(() => {
  sessionStorage.clear()

  // 初始化记住我的状态
  const remembered = localStorage.getItem('rememberMe') === 'true'
  rememberMe.value = remembered

  // 如果之前记住了账号密码，则填充表单
  if (remembered) {
    const savedUsername = localStorage.getItem('username')
    const savedPassword = localStorage.getItem('password')
    if (savedUsername && savedPassword) {
      formModel.value.username = savedUsername
      formModel.value.password = savedPassword
    }
  }

  generateCaptcha()
})

/**
 * 生成随机字符串
 * @param length 字符串的长度，默认11
 * @returns {string}
 */
function generateRandomString(length = 11) {
  let charset = 'abcdefghijklmnopqrstuvwxyz-_ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789'
  let values = new Uint32Array(length)

  window.crypto.getRandomValues(values)

  let str = ''

  for (let i = 0; i < length; i++) {
    str += charset[values[i] % charset.length]
  }

  return str
}

/**
 * 获取验证码
 */
function getCaptcha() {
  return sessionStorage.getItem('login-captcha:uuid')
}

/**
 * 设置验证码
 */
function setCaptcha(captcha) {
  sessionStorage.setItem('login-captcha:uuid', captcha)
}

const generateCaptcha = async () => {
  // 生成随机的uuid
  let uuid = generateRandomString(11)

  // 保存uuid到localStorage
  setCaptcha(uuid)

  // 构造图片请求的基本 URL
  const baseUrl = 'http://localhost:8080/captcha/loginCaptcha?uuid='

  // 获取图片响应，假设返回的是 Blob 数据
  const response = await request.get(baseUrl + uuid, { responseType: 'blob' })

  // 创建一个 URL 对象，将 Blob 数据作为图片源
  const imageUrl = URL.createObjectURL(response)

  // 更新 captchaUrl，Vue 会自动更新 <img> 标签的 src
  captchaUrl.value = imageUrl
}

const login = async () => {
  // 获取最新的uuid
  formModel.value.uuid = getCaptcha()
  const res = await userLogin(formModel.value)
  if (res.code === 1) {
    sessionStorage.setItem('token', res.data.token)
    sessionStorage.setItem('userId', res.data.id)

    // 处理记住我的逻辑
    if (rememberMe.value) {
      localStorage.setItem('username', formModel.value.username)
      localStorage.setItem('password', formModel.value.password)
    } else {
      // 如果没有勾选记住我，清除之前可能存储的信息
      localStorage.removeItem('username')
      localStorage.removeItem('password')
    }

    // 判断role
    if (res.data.role === 'boos') {
      sessionStorage.setItem('boosId', res.data.boosId)
      await getBoosInfo(res.data.boosId).then((res1) => {
        boosStore.boosInfo = res1.data
      }) 
      router.push('/boosLayout')
    } else {
      sessionStorage.setItem('candidateId', res.data.candidateId)
      await getCandidateInfo(res.data.candidateId).then((res2) => {
        candidateStore.candidateInfo = res2.data
      })
      router.push('/layout')
    }
  } else {
    console.log('登录失败')
    ElMessage.error(res.msg)
  }
}

//处理时间戳转换为以本地时区为条件的 YYYY-MM-DD 字符串格式
const dealDateFormater = (timestamp) => {
  return new Date(timestamp).toLocaleDateString('en-CA')
}

const isRegister = ref(false)

const formModel = ref({
  username: '',
  password: '',
  code: '',
  uuid: null,
  repassword: '',
  role: ''
})
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 5, max: 10, message: '用户名必须是5-10位的字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    {
      pattern: /^\S{6,15}$/,
      message: '密码必须是6-15位的非空字符',
      trigger: 'blur'
    }
  ],
  repassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    {
      pattern: /^\S{6,15}$/,
      message: '密码必须是6-15的非空字符',
      trigger: 'blur'
    },
    {
      validator: (rule, value, callback) => {
        if (value !== formModel.value.password) {
          callback(new Error('两次输入密码不一致!'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

watch(isRegister, () => {
  formModel.value = {
    username: '',
    password: '',
    repassword: ''
  }
})

// 添加新的响应式变量
const isForgetPassword = ref(false)
const isCountingDown = ref(false)
const countDown = ref(60)
const countDownText = ref('发送验证码')

// 修改忘记密码表单数据
const forgetForm = ref({
  email: '',
  code: '',
  uuid:'',
  password: '',
  repassword: ''
})

// 修改忘记密码表单验证规则
const forgetRules = {
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { len: 6, message: '验证码长度必须为6位', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    {
      pattern: /^\S{6,15}$/,
      message: '密码必须是6-15位的非空字符',
      trigger: 'blur'
    }
  ],
  repassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== forgetForm.value.password) {
          callback(new Error('两次输入密码不一致!'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 页面切换方法
const goToForgetPassword = () => {
  isForgetPassword.value = true
  isRegister.value = false
  //当点击忘记密码时候进行sessionStorage的存储
  // 生成随机的uuid
  let uuid = generateRandomString(11)
  //将其存入sessionStorage中
  sessionStorage.setItem('email-captcha:uuid', uuid)
  //将其存入forgetForm中
  forgetForm.value.uuid = uuid
}

const backToLogin = () => {
  isForgetPassword.value = false
  isRegister.value = false
  // 重置表单
  forgetForm.value = {
    email: '',
    code: '',
    uuid:'',
    password: '',
    repassword: ''
  }
  // 重置倒计时
  stopCountDown()
}

// 修改发送验证码方法
const sendEmailCode = async () => {
  // 验证邮箱
  if (!forgetForm.value.email) {
    ElMessage.warning('请先输入邮箱地址')
    return
  }

  // 验证邮箱格式
  const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/
  if (!emailRegex.test(forgetForm.value.email)) {
    ElMessage.warning('请输入正确的邮箱格式')
    return
  }

  const uuid = sessionStorage.getItem('email-captcha:uuid')
  // 发送验证码
  await request.get(
    `http://localhost:8080/captcha/emailCaptcha?uuid=${uuid}&email=${forgetForm.value.email}`
  )

  // 开始倒计时
  isCountingDown.value = true
  countDown.value = 60
  countDownText.value = `${countDown.value}s`

  const timer = setInterval(() => {
    countDown.value--
    countDownText.value = `${countDown.value}s`

    if (countDown.value <= 0) {
      stopCountDown()
      clearInterval(timer)
    }
  }, 1000)
}

// 停止倒计时
const stopCountDown = () => {
  isCountingDown.value = false
  countDown.value = 60
  countDownText.value = '发送验证码'
}

// 重置密码
const forgetFormRef = ref(null)
const resetPwd = async () => {
  // 验证表单
  const isValid = await forgetFormRef.value.validate()
  if (!isValid) {
    ElMessage.warning('请正确填写表单信息')
    return
  }
  const response = await resetPassword(forgetForm.value) // 调用重置密码的接口
  if (response.code !== 1) {
    ElMessage.warning('密码重置失败')
    return
  }
  ElMessage.success('密码重置成功')
  backToLogin()
}
</script>

<template>
  <el-row class="login-page">
    <el-col :span="12" class="bg"></el-col>
    <el-col :span="6" :offset="3" class="form">
      <el-form
        ref="form"
        size="large"
        autocomplete="off"
        v-if="isRegister"
        :rules="rules"
        :model="formModel"
      >
        <el-form-item>
          <h1>注册</h1>
        </el-form-item>

        <el-form-item prop="username">
          <el-input
            :prefix-icon="User"
            placeholder="请输入用户名"
            v-model="formModel.username"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            :prefix-icon="Lock"
            type="password"
            placeholder="请输入密码"
            v-model="formModel.password"
          ></el-input>
        </el-form-item>
        <el-form-item prop="repassword">
          <el-input
            :prefix-icon="Lock"
            type="password"
            placeholder="请输入再次密码"
            v-model="formModel.repassword"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button class="button" type="primary" auto-insert-space @click="CURegister">
            注册Candidate
          </el-button>
          <el-button class="button" type="primary" auto-insert-space @click="CBRegister">
            注册boss
          </el-button>
        </el-form-item>
        <el-form-item class="flex">
          <el-link type="info" :underline="false" @click="isRegister = false"> ← 返回 </el-link>
        </el-form-item>
      </el-form>

      <el-form
        ref="forgetFormRef"
        size="large"
        autocomplete="off"
        v-else-if="isForgetPassword"
        :rules="forgetRules"
        :model="forgetForm"
      >
        <el-form-item>
          <h1>找回密码</h1>
        </el-form-item>

        <el-form-item prop="email">
          <el-input
            :prefix-icon="Message"
            placeholder="请输入注册邮箱"
            v-model="forgetForm.email"
          ></el-input>
        </el-form-item>

        <el-form-item prop="code">
          <div class="flex">
            <el-input
              placeholder="请输入邮箱验证码"
              v-model="forgetForm.code"
              maxlength="6"
            ></el-input>
            <el-button
              type="primary"
              class="send-code-btn"
              :disabled="isCountingDown"
              @click="sendEmailCode"
            >
              {{ countDownText }}
            </el-button>
          </div>
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            :prefix-icon="Lock"
            type="password"
            placeholder="请输入新密码"
            v-model="forgetForm.password"
          ></el-input>
        </el-form-item>

        <el-form-item prop="repassword">
          <el-input
            :prefix-icon="Lock"
            type="password"
            placeholder="请确认新密码"
            v-model="forgetForm.repassword"
          ></el-input>
        </el-form-item>

        <el-form-item>
          <el-button class="reset-btn" type="primary" auto-insert-space @click="resetPwd">
            重置密码
          </el-button>
        </el-form-item>

        <el-form-item class="flex">
          <el-link type="info" :underline="false" @click="backToLogin"> ← 返回登录 </el-link>
        </el-form-item>
      </el-form>

      <el-form ref="form" size="large" autocomplete="off" v-else>
        <el-form-item>
          <h1>登录</h1>
        </el-form-item>
        <el-form-item prop="username">
          <el-input
            :prefix-icon="User"
            placeholder="请输入用户名"
            v-model="formModel.username"
          ></el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            name="password"
            :prefix-icon="Lock"
            type="password"
            placeholder="请输入密码"
            v-model="formModel.password"
          ></el-input>
        </el-form-item>

        <el-form-item prop="captcha">
          <div class="flex">
            <el-input
              name="captcha"
              placeholder="请输入验证码"
              v-model="formModel.code"
              maxlength="4"
            ></el-input>
            <el-image
              class="captcha_img"
              id="captcha"
              :src="captchaUrl"
              alt="验证码"
              style="width: 140px; height: 38px; margin-left: 10px"
              @click="generateCaptcha()"
            ></el-image>
          </div>
        </el-form-item>

        <el-form-item class="flex">
          <div class="flex">
            <el-checkbox v-model="rememberMe" @change="handleRememberMeChange">记住我</el-checkbox>
            <el-link type="primary" :underline="false" @click="goToForgetPassword"
              >忘记密码？</el-link
            >
          </div>
        </el-form-item>
        <el-form-item>
          <el-button class="button" type="primary" auto-insert-space @click="login()"
            >登录</el-button
          >
        </el-form-item>
        <el-form-item class="flex">
          <el-link type="info" :underline="false" @click="isRegister = true"> 注册 → </el-link>
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>
</template>

<style lang="scss" scoped>
.captcha_img {
  cursor: pointer;
}
.login-page {
  height: 100vh;
  background-color: #fff;
  .bg {
    background: url('@/assets/loginpic.jpg') no-repeat 10% center / cover;
    border-radius: 0 20px 20px 0;
  }
  .form {
    display: flex;
    flex-direction: column;
    justify-content: center;
    user-select: none;
    .title {
      margin: 0 auto;
    }
    .button {
      width: 48%;
    }
    .flex {
      width: 100%;
      display: flex;
      justify-content: space-between;
    }
  }
}

.send-code-btn {
  margin-left: 10px;
  width: 120px;
  font-size: 14px;
}

.flex {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.reset-btn {
  width: 100% !important; /* 覆盖原有的48%宽度 */
}
</style>