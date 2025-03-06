import axios from 'axios'
import router from '@/router' // 导入 router 实例
//可以直接导入数据
//这是黑马提供的后端接口
// const baseURL = 'http://big-event-vue-api-t.itheima.net'

const baseURL = 'http://localhost:8080'


const instance = axios.create({
  baseURL,
  timeout: 100000
  // TODO 1. 基础地址，超时时间
})

instance.interceptors.request.use(
  //TODO还未进行验证
  (config) => {
    const isLoginRequest = config.url.includes('/login');
    const isCaptchaRequest = config.url.includes('/captcha');
    console.log("是否为login:", isLoginRequest);
    console.log("是否为captcha:", isCaptchaRequest);
    console.log("是否有token", sessionStorage.getItem("token"))
    // 判断是否是登录接口或者是否有token，如果不是/没有则放行-不是报错
    if (!isLoginRequest && !isCaptchaRequest && !sessionStorage.getItem("token")) {
      // 如果不是登录请求且没有token，则重定向到登录页面
      router.push('/login');
      return Promise.reject(new Error("p:不是登录请求,也没有token")); // 修正了Promise.reject的位置
    }
    config.headers.Authorization = sessionStorage.getItem("token");
    return config;
  },
  (err) => Promise.reject(err)
)

instance.interceptors.response.use(
  (res) => {
    // TODO 3. 处理业务失败
    // TODO 4. 摘取核心响应数据
    return res.data
  },
  (err) => {

    // TODO 5. 处理401错误
    return Promise.reject(err)
  }
)

export default instance