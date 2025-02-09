import { defineStore } from 'pinia'
import { ref, computed } from 'vue'


export const useBoosStore = defineStore('useBoosStore', () => {

  const boosInfo = ref({
    id: null,
    boosName: null,
    boosEmail: null,
    boosPhone: null,
    companyId: null,
    boosImg: null,
    companyPosition: null,
    applicantCdsId: null
  })

  const jobPosts = ref([])
  // 修改过滤条件以匹配 review 字段
  const pendingJobs = computed(() =>
    jobPosts.value.filter(job => job.review === 1)  // 使用 review === 1 表示待审核
  )
  const activeJobPosts = computed(() =>
    jobPosts.value.filter(job => job.review === 2)  // 使用 review === 2 表示在招
  )
  const closedJobs = computed(() =>
    jobPosts.value.filter(job => job.review === 3)  // 使用 review === 3表示已关闭
  )
  const rejectedJobs = computed(() =>
    jobPosts.value.filter(job => job.review === 4)  // 使用 review === 4 表示已拒绝
  )
  const totalApplicants = computed(() =>
    jobPosts.value.reduce((sum, job) => sum + job.applicantsCount, 0)
  )




  return {
    boosInfo,
    jobPosts,
    activeJobPosts, closedJobs, pendingJobs, rejectedJobs,
    totalApplicants,
    closedJobs
  }
}) 