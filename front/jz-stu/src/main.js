

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import persist from 'pinia-plugin-persistedstate'
import App from './App.vue'
import router from './router'
import 'element-plus/dist/index.css';  // 引入 Element Plus 的全局样式
import InfiniteScroll from 'vue-infinite-scroll'; // 引入 vue-infinite-scroll
import 'element-plus/theme-chalk/el-icon.css'; // 引入图标样式
import ElementPlus from 'element-plus';
const app = createApp(App)

app.use(ElementPlus)
app.use(router)
app.use(createPinia().use(persist))
app.mount('#app')
