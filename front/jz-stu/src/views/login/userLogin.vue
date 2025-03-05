<script setup>
import { ElImage } from 'element-plus'
import 'element-plus/es/components/image/style/css'
import request from '@/utils/request'
import { User, Lock } from '@element-plus/icons-vue'
import { onMounted, ref, watch } from 'vue'
import { userLogin } from '@/api/user'
import { useUserStore } from '@/stores/user'
import { useRegisterStore } from '@/stores/register'
import { useRouter } from 'vue-router'
import { getCandidateInfo } from '@/api/candidate.js'
import logoImage from '@/assets/a.jpg' // 确保这个路径是正确的
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
  localStorage.clear()
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
  return localStorage.getItem('login-captcha:uuid')
}

/**
 * 设置验证码
 */
function setCaptcha(captcha) {
  localStorage.setItem('login-captcha:uuid', captcha)
}

const generateCaptcha = async () => {
  // 生成随机的uuid
  let uuid = generateRandomString(11)

  // 保存uuid到localStorage
  setCaptcha(uuid)

  // 构造图片请求的基本 URL
  const baseUrl = 'http://localhost:8080/captcha/get?uuid='

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
  const res = await userLogin(formModel.value) // 传递两个参数
  if (res.code === 1) {
    sessionStorage.setItem('token', res.data.token) //将token存入session
    localStorage.setItem('userId', res.data.id) //将userId存储到session
    //判断role
    if (res.data.role === 'boos') {
      localStorage.setItem('boosId', res.data.boosId)
      router.push('/boosLayout') // 跳转到布局页面
    } else {
      localStorage.setItem('candidateId', res.data.candidateId)
      router.push('/layout') // 跳转到布局页面
    }
  } else {
    console.log('登录失败') // 登录失败的处理
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
            <el-checkbox>记住我</el-checkbox>
            <el-link type="primary" :underline="false">忘记密码？</el-link>
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
</style>