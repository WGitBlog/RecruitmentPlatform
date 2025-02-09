import {defineStore} from "pinia"
import {ref} from 'vue'
// 用户模块
export const useUserStore = defineStore(
    'big-user',
    () => {
      const token = ref('1233') // 定义 token---->''这算是null
      const setToken = (t) => (token.value = t) // 设置 token
      return { token, setToken }
    }
    // ,
    // {
    //   persist: true // 持久化
    // }
  )




  