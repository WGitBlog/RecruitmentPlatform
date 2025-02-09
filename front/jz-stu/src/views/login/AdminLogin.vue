<template>
  <div class="admin-login-container">
    <div class="login-left">
      <div class="welcome-text">
        <h1>欢迎使用</h1>
        <h2>兼职平台管理系统</h2>
        <p>Professional Part-time Job Management Platform</p>
      </div>
    </div>
    <div class="login-right">
      <div class="login-form-container">
        <div class="login-header">
          <!-- 修改图片引用方式 -->
          <img :src="logoImage" alt="logo" class="logo">
          <h3>管理员登录</h3>
          <p class="sub-title">请使用管理员账号登录系统</p>
        </div>
        
        <el-form 
          ref="loginFormRef"
          :model="loginForm"
          :rules="loginRules"
          class="login-form"
        >
          <el-form-item prop="adminName">
            <el-input 
              v-model="loginForm.adminName"
              placeholder="请输入管理员账号"
              :prefix-icon="User"
              size="large"
            />
          </el-form-item>
          
          <el-form-item prop="password">
            <el-input 
              v-model="loginForm.password"
              type="password"
              placeholder="请输入密码"
              :prefix-icon="Lock"
              show-password
              size="large"
              @keyup.enter="handleLogin"
            />
          </el-form-item>

          <div class="remember-forgot">
            <el-checkbox v-model="rememberMe">记住我</el-checkbox>
            <el-link type="primary" :underline="false">忘记密码？</el-link>
          </div>

          <el-form-item class="login-btn">
            <el-button 
              type="primary" 
              :loading="loading"
              @click="handleLogin"
              size="large"
              class="submit-btn"
            >
              {{ loading ? '登录中...' : '登 录' }}
            </el-button>
          </el-form-item>
        </el-form>

        <div class="login-footer">
          <p>登录即表示同意</p>
          <el-link type="primary" :underline="false">服务条款</el-link>
          <span>&</span>
          <el-link type="primary" :underline="false">隐私政策</el-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { userLogin } from '@/api/user'
import 'element-plus/theme-chalk/el-message.css'
import 'element-plus/theme-chalk/el-message-box.css'
import logoImage from '@/assets/a.jpg'  // 确保这个路径是正确的
const router = useRouter()
const loading = ref(false)
const loginFormRef = ref(null)
const rememberMe = ref(false)

onMounted(() => {
  localStorage.clear()
})
// const userId=localStorage.setItem("userId",)

// 登录表单数据
const loginForm = reactive({
  adminName: '',
  password: ''
})

// 表单验证规则
const loginRules = {
  adminName: [
    { required: true, message: '请输入管理员账号', trigger: 'blur' },
    { min: 5, max: 10, message: '账号长度应在 3 到 20 个字符之间', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 5, max: 10, message: '密码长度应在 6 到 20 个字符之间', trigger: 'blur' }
  ]
}

// 登录处理函数
const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        loading.value = true
        const res = await userLogin(loginForm.adminName, loginForm.password)
        
        if (res.code === 1) {
          ElMessage.success('登录成功') // 登录成功
          if (rememberMe.value) {
            localStorage.setItem('adminName', loginForm.adminName)
          }
          localStorage.setItem('userId', res.data.id)
          localStorage.setItem('adminToken', res.data.token)
          localStorage.setItem('adminInfo', JSON.stringify(res.data.adminInfo))
          router.push('/admin/dashboard') // 跳转
          return; // 退出函数，避免执行后续代码
        } else {
          ElMessage.error(res.message || '登录失败') // 登录失败
        }
      } catch (error) {
        ElMessage.error('服务器错误，请稍后重试')
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped lang="scss">
.admin-login-container {
  height: 100vh;
  display: flex;
  background: #f5f7f9;
}

.login-left {
  flex: 1;
  background: linear-gradient(135deg, #1890ff 0%, #36cfc9 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
  color: white;
  
  .welcome-text {
    text-align: center;
    h1 {
      font-size: 48px;
      margin-bottom: 20px;
      font-weight: 600;
    }
    h2 {
      font-size: 36px;
      margin-bottom: 15px;
      font-weight: 500;
    }
    p {
      font-size: 18px;
      opacity: 0.8;
    }
  }
}

.login-right {
  width: 500px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: white;
}

.login-form-container {
  width: 380px;
}

.login-header {
  text-align: center;
  margin-bottom: 40px;
  
  .logo {
    width: 80px;
    height: 80px;
    margin-bottom: 20px;
  }
  
  h3 {
    font-size: 24px;
    color: #303133;
    margin-bottom: 10px;
  }
  
  .sub-title {
    color: #909399;
    font-size: 14px;
  }
}

.login-form {
  :deep(.el-input__wrapper) {
    background-color: #f5f7fa;
    box-shadow: none;
    border: 1px solid #e4e7ed;
    
    &:hover, &:focus {
      border-color: #409EFF;
    }
  }
  
  :deep(.el-input__inner) {
    height: 45px;
  }
}

.remember-forgot {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.submit-btn {
  width: 100%;
  height: 45px;
  font-size: 16px;
  border-radius: 4px;
}

.login-footer {
  margin-top: 30px;
  text-align: center;
  color: #909399;
  font-size: 14px;
  
  p {
    margin-bottom: 10px;
  }
  
  span {
    margin: 0 5px;
  }
}

// 响应式设计
@media screen and (max-width: 1200px) {
  .login-left {
    display: none;
  }
  
  .login-right {
    width: 100%;
  }
}
</style> 