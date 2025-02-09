import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useJobStore } from '@/stores/job.js'
import Layout from '@/views/layout/AdminLayout.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    // 配置默认的路由
    { path: '/', redirect: "/login" },
    { path: '/login', component: () => import('@/views/login/userLogin.vue') },
    { path: '/adminLogin', component: () => import('@/views/login/AdminLogin.vue') },
    { path: '/register', component: () => import('@/views/login/userRegister.vue') },
    { path: '/layout', component: () => import('@/views/layout/layoutContainer.vue') },
    { path: '/adminLayout', component: () => import('@/views/layout/AdminLayout.vue') },
    {
      path: '/userInfo',
      component: () => import('@/views/layout/userCandidateInfo.vue'),
      children: [
        { path: '', redirect: '/userInfo/commounited' },
        { path: 'commounited', component: () => import('@/components/userInfo/commounited.vue') },
        { path: 'deliveries', component: () => import('@/components/userInfo/deliveries.vue') },
        { path: 'interviews', component: () => import('@/components/userInfo/interviews.vue') },
        { path: 'interests', component: () => import('@/components/userInfo/interests.vue') }
      ]
    },
    { path: '/userBInfo', component: () => import('@/views/layout/userBoosInfo.vue') },
    { path: '/dialogue', component: () => import('@/views/layout/dialogue.vue') },
    { path: '/boosLayout', component: () => import('@/views/layout/boosLayoutContainer.vue') },
    // //匹配没有配置的路径
    { path: '/:param(.*)', component: () => import('@/views/notFound/notFound.vue') },

    {
      path: '/admin',
      component: Layout,
      children: [
        {
          path: 'dashboard',
          component: () => import('@/views/user/dashboard.vue'),
          name: 'Dashboard',
          meta: { title: '控制台' }
        },
        {
          path: 'candidateList',
          component: () => import('@/views/user/candidateList.vue'),
          name: 'CandidateList',
          meta: { title: '员工列表' }
        },
        {
          path: 'bossList',
          component: () => import('@/views/user/bossList.vue'),
          name: 'BossList',
          meta: { title: '企业列表' }
        }

        ,
        {
          path: 'jobAudit',
          component: () => import('@/views/user/jobAudit.vue'),
          name: 'JobAudit',
          meta: { title: '职位审核' }
        }

        ,
        {
          path: 'jobList',
          component: () => import('@/views/user/jobList.vue'),
          name: 'JobList',
          meta: { title: '职位列表' }
        }
        ,
        {
          path: 'report',
          component: () => import('@/views/user/report.vue'),
          name: 'Report',
          meta: { title: '举报处理' }
        }
      ]
    }
  ]
})

//配置路由拦截器
router.beforeEach((to, from) => {
  console.log('路由跳转:', from.path, '->', to.path)
  const user = useUserStore()
  const jobStore = useJobStore()
  if (!user.token && to.path !== '/login') return '/login'

  //处理跳转之后page的增加导致返回的时候已经++
  jobStore.page = 1
  return
})




export default router
