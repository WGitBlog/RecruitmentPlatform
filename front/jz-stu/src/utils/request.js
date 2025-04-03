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
  (config) => {
    // 定义无需token验证的API路径白名单
    const whitelist = [
      '/login',
      '/captcha',
      '/boosRegister',
      '/candidateRegister',
      '/resetPassword'
    ];

    // 检查当前请求是否在白名单中
    const isWhitelisted = whitelist.some(path => config.url.includes(path));
    
    // 调试信息合并为单条日志
    console.log(`请求拦截检测: URL=${config.url}, 白名单=${isWhitelisted}, Token存在=${!!sessionStorage.getItem("token")}`);

    // 统一校验逻辑
    if (!isWhitelisted && !sessionStorage.getItem("token")) {
      router.push('/login');
      return Promise.reject(new Error("未授权访问：缺少有效令牌"));
    }

    // 设置请求头（即使没有token也会设置空值，可根据需求调整）
    config.headers.Authorization = sessionStorage.getItem("token") || '';
    return config;
  },
  (err) => Promise.reject(err)
);

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
