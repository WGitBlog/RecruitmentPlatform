import axios from 'axios'
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




    // const userStore=useUserStore()
    // const isLoginRequest=config.url.includes('/login')
    // //判断是否有token如果有则放行-没有判断是否是登录接口
    // if(userStore.token){
    //   config.headers.Authorization=userStore.token
    //   return config
    // }
    // //判断是否是登录接口如果是则放行-不是报错
    // if(!isLoginRequest){
    //   Promise.reject(new Error("p:不是登录请求,也没有token"))
    // }



    // TODO 2. 携带token
    return config
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