<script setup lang="ts">
import { useUserStore } from '@/stores/user'
import { useJobStore } from '@/stores/job'
import { useCandidateStore } from '@/stores/candidate'
import { getJobInfo } from '@/api/job.js'
import { computed, onBeforeUnmount, onMounted, ref, reactive } from 'vue'
import useWebSocket from '@/utils/websocket.js'
import { getCandidateInfo } from '@/api/candidate.js'
import { getBoosInfo } from '@/api/boos.js'
import { resetPassword, updatePassword } from '@/api/user.js'
import { updateInterested, updateApplicantCommunication } from '@/api/applicant.js'
import { getCompanyInfo } from '@/api/company.js'
import router from '@/router'
import 'element-plus/theme-chalk/el-message.css'
import 'element-plus/theme-chalk/el-message-box.css'
import { h } from 'vue'
import { ElMessageBox, ElMessage, FormInstance } from 'element-plus'
import messageBox from '@/components/layout/messageBox.vue'
import { watch } from 'vue'
import { JobDto } from '@/interface/index.js'
import { report, ReportResponse } from '@/interface'
import { submitJobReport } from '@/api/report.js'
import { Location, Iphone, Message, Avatar, Discount, Coin } from '@element-plus/icons-vue'
const userStore = useUserStore()
const jobStore = useJobStore()
const candidateStore = useCandidateStore()
const UserId = sessionStorage.getItem('userId')
const candidateId = sessionStorage.getItem('candidateId')
const boosId = sessionStorage.getItem('boosId')
const searchValue = ref('')

// 修改密码弹窗显示控制
const dialogVisible = ref(false)
// 修改密码表单验证规则
// 表单数据
const resetform = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})
// 新增表单 ref 引用
const formRef = ref<FormInstance>() // 需要导入 FormInstance 类型
const rules = {
  oldPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少6位', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少6位', trigger: 'blur' }
  ]
}

const handleCommand = (command) => {
  switch (command) {
    case 'personalCenter':
      // 跳转到个人中心页面
      router.push('/userInfo')
      break
    case 'changePassword':
      // 修改密码api
      dialogVisible.value = true
      break
    case 'logout':
      // 处理退出登录逻辑
      router.push('/login')
      console.log(' 退出登录')
      break
    default:
      break
  }
}

// 提交修改密码表单
const submitForm = async () => {
  // 验证表单
  const isValid = await formRef.value.validate()
  if (!isValid) {
    return
  }
  // 这里添加密码修改API调用
  await updatePassword({
    id: sessionStorage.getItem('userId'),
    oldPassword: resetform.oldPassword,
    password: resetform.newPassword
  })
    .then((res) => {
      if (res.code !== 1) {
        ElMessage.error(res.msg)
        return
      } else {
        ElMessage.success('修改密码成功')
        dialogVisible.value = false
      }
    })
    .finally(() => {
      // 重置表单数据
      resetform.oldPassword = ''
      resetform.newPassword = ''
      resetform.confirmPassword = ''
      // 重置表单验证状态
      formRef.value.resetFields()
    })
  dialogVisible.value = false
}
// 重置表单
const cancelForm = () => {
  formRef.value.resetFields()
  resetform.oldPassword = ''
  resetform.newPassword = ''
  resetform.confirmPassword = ''
  dialogVisible.value = false
}

//massageBox弹框事件
const open = () => {
  ElMessageBox({
    title: '请选择城市',
    message: () => h(messageBox),
    showCancelButton: false,
    showClose: true,
    showConfirmButton: false,
    customStyle: { maxWidth: '840px', width: 'auto' }
  })
}

const items = ref([]) //数据列表
const defaultJob = ref<JobDto>({
  boosId: 0,
  boosImg: '',
  boosName: '',
  companyId: 0,
  companyImg: '',
  companyMarket: false,
  companyName: '',
  companyPosition: '',
  companyScale: '',
  companyType: '',
  educationRequirements: '',
  id: 0,
  jobCategory: '',
  jobDescription: [],
  jobDetails: '',
  jobTitle: '',
  publishTime: Date.now(),
  requiredTechnology: '',
  salaryRange: '',
  weeklyDays: 0,
  workLocation: '',
  review: 0
})
//默认大模板
const loading = ref(false) //判断是否正在加载
const hasMore = ref(true) //判断是否还有数据没有数据就无需加载
const initialLoad = ref(true) //为了防止上来就发请求
//处理getJobInfo请求的json转义问题
const processedData = (res) =>
  res.data.records.map((record) => {
    //const jobDescription = JSON.parse(response.data.jobDescription);
    if (record.jobDescription && typeof record.jobDescription === 'string') {
      try {
        record.jobDescription = JSON.parse(record.jobDescription)
      } catch (e) {
        console.error('JSON parsing error:', e)
      }
    }

    // 对 jobDetails 字段进行转义处理
    if (record.jobDetails && typeof record.jobDetails === 'string') {
      record.jobDetails = record.jobDetails.replace(/\\n/g, '<br>')
    }

    return record
  })

const defaultUser = ref({})

onMounted(async () => {
  const res = await getJobInfo(
    jobStore.page,
    jobStore.pageSize,
    jobStore.workLocation,
    jobStore.salaryRange,
    jobStore.jobCategory,
    jobStore.weeklyDays,
    jobStore.jobTitle,
    jobStore.review
  ) // 当进入页面请求一次数据
  jobStore.page++
  const processedRecords = processedData(res) //处理getJobInfo请求的json转义问题
  items.value.push(...processedRecords) // 将处理后的数据放到数据列表里
  defaultJob.value = { ...items.value[0] } // 并初始化为默认数据

  if (candidateId) {
    const res = await getCandidateInfo(candidateId)
    defaultUser.value = res.data
    candidateStore.candidateInfo = res.data
  }
  if (boosId) {
    const res = await getBoosInfo(boosId)
    defaultUser.value = res.data
  }
})
// 添加组件卸载时的清理
onBeforeUnmount(() => {
  if (hoverTimer.value) {
    clearTimeout(hoverTimer.value)
  }
})
//处理滑轮到底部发送新的分页请求
const load = async () => {
  if (initialLoad.value) {
    // 判断 true 则更改成 false
    initialLoad.value = false
    // 并空返回
    return
  }
  if (loading.value || !hasMore.value) return
  loading.value = true // 设置加载状态为 true
  try {
    const res = await getJobInfo(
      jobStore.page,
      jobStore.pageSize,
      jobStore.workLocation,
      jobStore.salaryRange,
      jobStore.jobCategory,
      jobStore.weeklyDays,
      jobStore.jobTitle,
      jobStore.review
    )
    const processedRecords = processedData(res)
    items.value.push(...processedRecords)
    console.log(items.value)
    jobStore.page++

    // 检查是否还有更多数据
    if (processedRecords.length < jobStore.pageSize) {
      hasMore.value = false // 设置 hasMore 为 false
    }
  } catch (error) {
    console.error('加载数据失败:', error)
  } finally {
    loading.value = false // 重置加载状态
  }
}

const defaultShow = (item) => {
  // 将 defaultJob 置为空对象
  defaultJob.value = {
    boosId: 0,
    boosImg: '',
    boosName: '',
    companyId: 0,
    companyImg: '',
    companyMarket: false,
    companyName: '',
    companyPosition: '',
    companyScale: '',
    companyType: '',
    educationRequirements: '',
    id: 0,
    jobCategory: '',
    jobDescription: [],
    jobDetails: '',
    jobTitle: '',
    publishTime: Date.now(),
    requiredTechnology: '',
    salaryRange: '',
    weeklyDays: 0,
    workLocation: '',
    review: 0
  }

  // 更新 defaultJob 的属性
  defaultJob.value.boosId = item.boosId
  defaultJob.value.boosImg = item.boosImg
  defaultJob.value.boosName = item.boosName
  defaultJob.value.companyId = item.companyId
  defaultJob.value.companyImg = item.companyImg
  defaultJob.value.companyMarket = item.companyMarket
  defaultJob.value.companyName = item.companyName
  defaultJob.value.companyPosition = item.companyPosition
  defaultJob.value.companyScale = item.companyScale
  defaultJob.value.companyType = item.companyType
  defaultJob.value.educationRequirements = item.educationRequirements
  defaultJob.value.id = item.id
  defaultJob.value.jobCategory = item.jobCategory
  defaultJob.value.jobDescription = item.jobDescription
  defaultJob.value.jobDetails = item.jobDetails
  defaultJob.value.jobTitle = item.jobTitle
  defaultJob.value.publishTime = item.publishTime
  defaultJob.value.requiredTechnology = item.requiredTechnology
  defaultJob.value.salaryRange = item.salaryRange
  defaultJob.value.weeklyDays = item.weeklyDays
  defaultJob.value.workLocation = item.workLocation
}

// popover跟它中哇哦的元素绑定的必要变量
const triggerRef = ref<HTMLElement | null>(null) // 确保响应式
// 响应式状态
const popoverVisible = ref(false)
const currloading = ref(false)
const companyData = ref({}) //岗位列表中当鼠标经过公司显示的公司信息
const hoverTimer = ref() //公司展示定时器
// 鼠标进入处理
const handleMouseEnter = async (companyId, event) => {
  // 清除已有定时器
  if (hoverTimer.value) {
    clearTimeout(hoverTimer.value)
    hoverTimer.value = null
  }
  triggerRef.value = event.currentTarget // 设置当前触发元素
  hoverTimer.value = setTimeout(async () => {
    try {
      currloading.value = true
      // 模拟异步数据加载
      await getCompanyInfo(companyId).then((res) => {
        if (res.code != 1) {
          ElMessage.error('获取公司信息失败')
          return
        }
        companyData.value = res.data
      })
    } catch (error) {
      console.error('数据加载失败:', error)
    } finally {
      currloading.value = false
      popoverVisible.value = true
    }
  }, 500)
}

// 鼠标离开处理（带防抖）
const handleMouseLeave = () => {
  // 清除定时器
  if (hoverTimer.value) {
    clearTimeout(hoverTimer.value)
    hoverTimer.value = null
  }

  setTimeout(() => {
    if (popoverVisible.value) {
      popoverVisible.value = false
    }
  }, 200)
}

//添加applicant的interested数组中的数据
const addInterested = async () => {
  const jobId = defaultJob.value.id
  await updateInterested(Number(candidateId), jobId)
}

//监听job.js中res数据的变化并赋值
watch(
  () => jobStore.res,
  (newValue, oldValue) => {
    //清空item数据
    items.value = []
    const processedRecords = processedData(newValue)
    items.value.push(...processedRecords)
    defaultJob.value = { ...items.value[0] } // 并初始化为默认数据
  }
)

//薪资待遇处理方法
async function handleCommandsalaryRange(command) {
  //清空item数据
  items.value = []
  jobStore.page = 1
  jobStore.salaryRange = command
  //再次查询job的page信息（带参数）
  const res = await getJobInfo(
    jobStore.page,
    jobStore.pageSize,
    jobStore.workLocation,
    jobStore.salaryRange,
    jobStore.jobCategory,
    jobStore.weeklyDays,
    jobStore.jobTitle,
    jobStore.review
  )
  jobStore.page++
  const processedRecords = processedData(res)
  items.value.push(...processedRecords)
  defaultJob.value = { ...items.value[0] } // 并初始化为默认数据
}
//工作类型处理方法
async function handleCommandJobCategory(command) {
  //清空item数据
  items.value = []
  jobStore.page = 1
  jobStore.jobCategory = command
  //再次查询job的page信息（带参数）
  const res = await getJobInfo(
    jobStore.page,
    jobStore.pageSize,
    jobStore.workLocation,
    jobStore.salaryRange,
    jobStore.jobCategory,
    jobStore.weeklyDays,
    jobStore.jobTitle,
    jobStore.review
  )
  jobStore.page++
  const processedRecords = processedData(res)
  items.value.push(...processedRecords)
  defaultJob.value = { ...items.value[0] } // 并初始化为默认数据
}
//周工作日处理方法
async function handleCommandWeeklyDays(command) {
  //清空item数据
  items.value = []
  jobStore.page = 1
  console.log('handleCommandWeeklyDays-command:' + command)
  if (command === '') {
    jobStore.weeklyDays = null
    console.log("handleCommandWeeklyDays-jobStore.weeklyDays'':" + jobStore.weeklyDays)
  } else {
    jobStore.weeklyDays = parseInt(command)
    console.log('handleCommandWeeklyDays-jobStore.weeklyDays:' + jobStore.weeklyDays)
  }

  //再次查询job的page信息（带参数）
  const res = await getJobInfo(
    jobStore.page,
    jobStore.pageSize,
    jobStore.workLocation,
    jobStore.salaryRange,
    jobStore.jobCategory,
    jobStore.weeklyDays,
    jobStore.jobTitle,
    jobStore.review
  )
  jobStore.page++
  const processedRecords = processedData(res)
  items.value.push(...processedRecords)
  defaultJob.value = { ...items.value[0] } // 并初始化为默认数据
}

const handleSearch = async () => {
  //清空item数据
  items.value = []
  jobStore.page = 1
  //因为单单搜素一家公司没必要带其他条件
  jobStore.jobTitle = searchValue.value
  jobStore.workLocation = ''
  jobStore.salaryRange = ''
  jobStore.jobCategory = ''
  jobStore.weeklyDays = null
  //再次查询job的page信息（带参数）
  const res = await getJobInfo(
    jobStore.page,
    jobStore.pageSize,
    jobStore.workLocation,
    jobStore.salaryRange,
    jobStore.jobCategory,
    jobStore.weeklyDays,
    jobStore.jobTitle,
    jobStore.review
  )
  const processedRecords = processedData(res)
  items.value.push(...processedRecords)
  defaultJob.value = { ...items.value[0] } // 并初始化为默认数据
}

const commouncationTa = async (defaultJob) => {
  //将该item加入到boos的applicant的关系表里的沟通数组
  await updateApplicantCommunication(defaultJob.id, candidateId)
  //代参跳转到消息页面
  router.push(`/dialogue?defaultJob=${encodeURIComponent(JSON.stringify(defaultJob))}`)
}

// 举报相关的数据
const reportDialogVisible = ref(false)
const reportFormRef = ref(null)

const reportForm = reactive({
  reason: '',
  details: ''
})

const reportRules = {
  reason: [{ required: true, message: '请选择举报原因', trigger: 'change' }],
  details: [
    { required: true, message: '请填写详细说明', trigger: 'blur' },
    { min: 10, message: '详细说明至少10个字', trigger: 'blur' }
  ]
}

const reportInfo = ref<report>({
  id: null,
  jobId: null,
  jobTitle: '',
  companyId: null,
  boosName: '',
  companyName: '',
  reason: '',
  status: null,
  createTime: new Date(),
  feedback: '',
  reportDetails: '',
  reporterId: Number(candidateId)
})

//取消举报
const cancelReportDialog = () => {
  reportDialogVisible.value = false
  reportForm.details = ''
  reportForm.reason = ''
}

// 提交举报的处理函数
const handleSubmitReport = async () => {
  if (!reportFormRef.value) return

  await reportFormRef.value.validate(async (valid) => {
    if (valid) {
      Object.keys(reportInfo.value).forEach((key) => {
        // 如果 defaultJob 中有相同的字段，则赋值
        reportInfo.value['jobId'] = defaultJob.value['id']

        if (defaultJob.value.hasOwnProperty(key) && key !== 'id') {
          reportInfo.value[key] = defaultJob.value[key]
        }
      })
      reportInfo.value.reason = reportForm.reason
      reportInfo.value.reportDetails = reportForm.details

      await submitJobReport(reportInfo.value).then((res) => {
        if (res.code === 1) {
          ElMessage.success(res.data)
        } else if (res.code === 0) {
          // 假设1是重复举报的错误代码
          ElMessage.error('您已经举报过该职位，不可重复举报')
        }
      })
      reportForm.details = ''
      reportForm.reason = ''
      reportDialogVisible.value = false
    }
  })
}
</script>

<template>
  <el-container>
    <el-header>
      <el-row type="flex" justify="center" class="header_flex-row">
        <el-col :span="12" class="header_row_col1">
          <ul>
            <li>
              <el-link :underline="false" href="#" target="_blank"><h1>全国招聘平台</h1></el-link>
            </li>
            <li><el-link :underline="false">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</el-link></li>
            <li><el-link :underline="false">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</el-link></li>
            <li><el-link :underline="false">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</el-link></li>
            <li><el-link :underline="false">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</el-link></li>
            <li><el-link :underline="false">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</el-link></li>
            <li><el-link :underline="false">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</el-link></li>
            <li><el-link :underline="false">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</el-link></li>
            <li><el-link :underline="false">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</el-link></li>
          </ul>
        </el-col>
        <el-col :span="9" class="header_row_col2">
          <ul>
            <li>
              <el-link :underline="false" @click="$router.push('/dialogue')">消息</el-link>
            </li>
            <li>
              <el-link :underline="false" @click="$router.push('/interviews')">面试</el-link>
            </li>

            <li style="lineheight: 25px">
              <!-- 使用 el-dropdown 组件 -->
              <el-dropdown trigger="hover" @command="handleCommand">
                <!-- 下拉菜单的触发元素 -->
                <el-link :underline="false">
                  <img
                    src="../../assets/a.jpg"
                    alt=""
                    :style="{ borderRadius: '50%', objectFit: 'cover' }"
                  />
                  <span v-html="defaultUser.name"></span>
                </el-link>
                <!-- 下拉菜单内容 -->
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="personalCenter">个人中心</el-dropdown-item>
                    <el-dropdown-item command="changePassword">修改密码</el-dropdown-item>
                    <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </li>
          </ul>
        </el-col>
      </el-row>

      <!-- 密码修改弹窗 -->
      <el-dialog v-model="dialogVisible" title="修改密码" width="30%">
        <el-form :model="resetform" label-width="79px" :rules="rules" ref="formRef">
          <el-form-item label="旧密码" prop="oldPassword">
            <el-input v-model="resetform.oldPassword" type="password" show-password />
          </el-form-item>
          <el-form-item label="新密码" prop="newPassword">
            <el-input v-model="resetform.newPassword" type="password" show-password />
          </el-form-item>
          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input v-model="resetform.confirmPassword" type="password" show-password />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="cancelForm" style="width: 140px">取消</el-button>
            <el-button type="primary" @click="submitForm" style="width: 140px">提交</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
    </el-header>

    <el-container>
      <el-main>
        <el-row justify="space-between" class="main_first_top">
          <el-input
            v-model="searchValue"
            placeholder="请输入你需要搜索的职位..."
            style="flex: 1; margin-right: 10px"
            @keyup.enter="handleSearch"
          ></el-input>
          <el-button
            class="iconfont icon-sousu1"
            style="backgroundcolor: '#edf6ff'"
            @click="handleSearch"
          >
            &nbsp;&nbsp;搜&nbsp;索</el-button
          >
        </el-row>
        <el-row :style="{ backgroundColor: 'white' }" class="main_first_bom">
          <ul>
            <li>
              <el-dropdown>
                <span
                  class="el-dropdown-link iconfont icon-didian1"
                  :style="{ cursor: 'pointer' }"
                  @click="open"
                  v-text="jobStore.workLocation == '' ? ' 全 国 ' : jobStore.workLocation"
                >
                </span>
              </el-dropdown>
            </li>

            <li>
              <el-dropdown @command="handleCommandsalaryRange">
                <span
                  class="el-dropdown-link iconfont icon-xinzi1"
                  :style="{ cursor: 'pointer' }"
                  @click="open1"
                  v-text="jobStore.salaryRange == '' ? ' 薪资待遇' : jobStore.salaryRange"
                >
                </span>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="">不限</el-dropdown-item>
                    <el-dropdown-item command="0k-3k">0k-3k</el-dropdown-item>
                    <el-dropdown-item command="3k-5k">3k-5k</el-dropdown-item>
                    <el-dropdown-item command="5k-10k">5k-10k</el-dropdown-item>
                    <el-dropdown-item command="10k-20k">10k-20k</el-dropdown-item>
                    <el-dropdown-item command="20k">20k以上</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </li>
            <li>
              <el-dropdown @command="handleCommandJobCategory">
                <span
                  class="el-dropdown-link iconfont icon-leixing"
                  :style="{ cursor: 'pointer' }"
                  @click="open2"
                  v-text="
                    jobStore.jobCategory == '' ? ' 工作类型' : '\u00A0\u00A0' + jobStore.jobCategory
                  "
                >
                </span>
                <template #dropdown>
                  <el-dropdown-menu style="height: 185px">
                    <el-dropdown-item command="">全部类型</el-dropdown-item>
                    <el-dropdown-item command="科技">科技</el-dropdown-item>
                    <el-dropdown-item command="咨询">咨询</el-dropdown-item>
                    <el-dropdown-item command="医疗">医疗</el-dropdown-item>
                    <el-dropdown-item command="金融">金融</el-dropdown-item>
                    <el-dropdown-item command="教育">教育</el-dropdown-item>
                    <el-dropdown-item command="零售">零售</el-dropdown-item>
                    <el-dropdown-item command="工程">工程</el-dropdown-item>
                    <el-dropdown-item command="研发">研发</el-dropdown-item>
                    <el-dropdown-item command="旅游">旅游</el-dropdown-item>
                    <el-dropdown-item command="建筑">建筑</el-dropdown-item>
                    <el-dropdown-item command="其他">其他</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </li>
            <li>
              <el-dropdown @command="handleCommandWeeklyDays">
                <span
                  class="el-dropdown-link iconfont icon-gongzuonianxian"
                  :style="{ cursor: 'pointer' }"
                  @click="open3"
                  v-text="
                    jobStore.weeklyDays == null ? ' 周工作日' : '工作' + jobStore.weeklyDays + '天'
                  "
                >
                </span>
                <template #dropdown>
                  <el-dropdown-menu style="height: 185px">
                    <el-dropdown-item command="">不限</el-dropdown-item>
                    <el-dropdown-item command="1">一天</el-dropdown-item>
                    <el-dropdown-item command="2">两天</el-dropdown-item>
                    <el-dropdown-item command="3">三天</el-dropdown-item>
                    <el-dropdown-item command="4">四天</el-dropdown-item>
                    <el-dropdown-item command="5">双休</el-dropdown-item>
                    <el-dropdown-item command="6">六天</el-dropdown-item>
                    <el-dropdown-item command="7">满勤</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </li>
          </ul>
          <br />
        </el-row>
        <el-row type="flex" justify="center" class="main_flex-row">
          <el-col class="main_left" :span="10">
            <ul
              class="infinite-list"
              v-infinite-scroll="load"
              :infinite-scroll-distance="2"
              style="overflow: auto"
            >
              <li
                v-for="item in items"
                class="infinite-list-item"
                :key="item.id"
                @click="defaultShow(item)"
              >
                <span class="li_top_left">{{ item.jobTitle }}</span>
                <span class="li_top_right">{{ item.salaryRange }}</span>

                <ul class="li_top_li">
                  <li v-for="(des, index) in item.jobDescription" :key="des.id">
                    {{ index + 1 }}{{ des }}
                  </li>
                </ul>
                <div class="right_li_bottom">
                  <span
                    class="bottom_span_top"
                    ref="triggerRef"
                    @mouseenter="handleMouseEnter(item.companyId, $event)"
                    @mouseleave="handleMouseLeave"
                  >
                    <a href="#">頭像</a>
                    <a href="#" :style="{ marginLeft: '10px', width: '180px' }">{{
                      item.companyName
                    }}</a>
                  </span>
                  <span class="bottom_span_bom">{{ item.workLocation }}</span>
                </div>

                <!-- {{index+1}}
                {{ item }} -->
              </li>
            </ul>
          </el-col>

          <el-col class="main_right" :span="16">
            <div class="main_header">
              <div class="main_header_left">
                <ul>
                  <li>
                    <span class="left_left">{{ defaultJob.jobTitle }}</span>
                    <span class="left_right">{{ defaultJob.salaryRange }}</span>
                  </li>
                  <li class="left_li_bottom">
                    <span> {{ defaultJob.workLocation }} </span>
                    <span>{{ defaultJob.weeklyDays }}天/周</span>

                    <span>{{ defaultJob.educationRequirements }} </span>
                  </li>
                </ul>
              </div>

              <div class="main_header_right">
                <a href="#" class="right_interested" @click.prevent="addInterested()">
                  <!-- 感兴趣 iconfont icon-ganxingquzhiwei-->
                  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                </a>
                <a
                  href="#"
                  class="right_discussion iconfont icon-goutong1"
                  @click.prevent="commouncationTa(defaultJob)"
                  >立即沟通</a
                >
              </div>
            </div>
            <div class="main_details">
              <h3>
                招聘信息
                <div class="details_right">
                  <el-button type="danger" @click="reportDialogVisible = true">举报</el-button>
                </div>
              </h3>
              <ul>
                <li v-for="(item, index) in defaultJob.jobDescription" :key="item.id">
                  {{ index + 1 }}{{ item }}
                </li>
                <!-- v-for="(item,index)" in :key="item.id" -->
              </ul>
              <!-- <p v-for="(item) in jobLines" :key="item.id">{{ item }}</p> -->
              <p v-html="defaultJob.jobDetails.replace(/\\n/g, '\n')"></p>
            </div>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </el-container>

  <!-- 添加举报弹窗 -->
  <el-dialog v-model="reportDialogVisible" title="举报职位" width="500px">
    <el-form ref="reportFormRef" :model="reportForm" :rules="reportRules" label-width="80px">
      <el-form-item label="举报原因" prop="reason">
        <el-select v-model="reportForm.reason" placeholder="请选择举报原因" style="width: 100%">
          <el-option label="职位信息与实际不符" value="职位信息与实际不符" />
          <el-option label="涉嫌虚假招聘" value="涉嫌虚假招聘" />
          <el-option label="涉嫌诈骗" value="涉嫌诈骗" />
          <el-option label="其他原因" value="其他原因" />
        </el-select>
      </el-form-item>

      <el-form-item label="详细说明" prop="details">
        <el-input
          v-model="reportForm.details"
          type="textarea"
          :rows="4"
          placeholder="请详细描述问题（至少10个字）"
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="cancelReportDialog()">取消</el-button>
        <el-button type="primary" @click="handleSubmitReport">提交举报</el-button>
      </span>
    </template>
  </el-dialog>
  <!-- Company数据展示弹窗 -->
  <el-popover
    v-model:visible="popoverVisible"
    :virtual-ref="triggerRef"
    trigger="manual"
    placement="right"
    :width="420"
    popper-class="company-popover"
  >
    <template #default>
      <div class="company-info">
        <!-- 公司名称和基本信息 -->
        <div class="header">
          <h3 class="company-name">&nbsp;&nbsp;&nbsp;{{ companyData.companyName }}</h3>
          <div class="meta">
            <el-tag size="small" type="info">{{ companyData.companyType }}</el-tag>
            <el-tag size="small">
              {{ companyData.companyMarket === true ? '已上市' : '未上市' }}</el-tag
            >
          </div>
        </div>
        <!-- 详细信息 -->
        <el-descriptions :column="1" border>
          <el-descriptions-item label="企业编号">
            <el-icon><coin /></el-icon>
            {{ companyData.id || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="所属行业">
            <el-tag size="small">{{ companyData.industry }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="成立时间">
            <el-icon><discount /></el-icon>
            {{ companyData.establishedYear || '未知' }}
          </el-descriptions-item>
          <el-descriptions-item label="企业规模">
            <span v-if="companyData.companyScale">
              <el-icon><avatar /></el-icon>
              {{ companyData.companyScale }}
            </span>
          </el-descriptions-item>
          <el-descriptions-item label="联系方式">
            <div class="contact-info">
              <span v-if="companyData.contactNumber">
                <el-icon><iphone /></el-icon>
                {{ companyData.contactNumber }}
              </span>
              <span v-if="companyData.email">
                <el-icon><message /></el-icon>
                {{ companyData.email }}
              </span>
            </div>
          </el-descriptions-item>
          <el-descriptions-item label="所在地">
            <el-icon><location /></el-icon>
            {{ companyData.location }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </template>
  </el-popover>
</template>

<style lang="scss" scoped>
.main_right {
  margin-left: 20px;
  .main_header {
    border: #00bebd 1px solid;
    border-radius: 12px;
    position: relative;
    height: 80px;
    width: 590px;
    background-color: white;
    .main_header_left {
      float: left;
      ul li {
        display: block;
        height: 0px;
        margin: 0;
        padding: 1px;
      }

      .left_left {
        float: left;
        font-size: 20px;
        margin-left: 10px;
        padding: 0px;
        height: 40px;
        line-height: 15px;
      }
      .left_right {
        float: left;
        margin-left: 70px;
        line-height: 15px;
        font-size: 20px;
        padding: 0px;
        color: #fe574a;
      }
      .left_li_bottom span {
        padding: 0 10px;
      }

      .left_li_bottom {
        float: left;

        line-height: 0px;
        font-size: 20px;
      }
    }

    .main_header_right {
      position: absolute;
      left: 370px;
      bottom: 0px;
      height: 80px;
      line-height: 80px;
      .right_interested {
        display: inline-block;
        height: 20px;
        line-height: 20px;
        margin-right: 20px;
        font-size: 20px;
        text-decoration: none;
      }
      .right_discussion {
        display: inline-block;
        height: 20px;
        line-height: 20px;
        font-size: 20px;
        text-decoration: none;
      }
    }
  }
  .main_details {
    padding: 5px;
    height: 425px;
    width: 590px;
    background-color: white;
    margin-top: 5px;
    border-radius: 10px;
    text-align: left;
    line-height: 20px;
    flex-wrap: nowrap;
    white-space: nowrap;
    overflow-y: auto; //TODO:出现滚动条-给父盒子设置宽度并且配置此行
    border: #00bebd 1px solid;
    box-sizing: border-box;
    h3 {
      display: block;
      position: relative; //TODO-���绝父相：绝对定位的父元素必须有定位/绝对定位脱标相对定位不脱标
      height: 20px;
      margin: 10px 5px;
    }
    p {
      white-space: pre-wrap;
      word-break: break-all;
      font-size: 15px;
      padding: 0 5px;
      line-height: 2;
    }
    ul {
      margin: 0;
      padding: 0;

      li {
        display: inline-block;
        height: 17px;
        font-size: 11px;
        background-color: #f8f8f8;
        margin: 0 5px;
        color: #666666;
        border-radius: 3px;
      }
    }
    .details_right {
      width: 70px;
      position: absolute;
      top: 0px; /* 设置为0，使其覆盖在h3的顶部 */
      right: 0px; /* 可以设置为0或其他值来调整位置 */
    }
  }
  .main_details::-webkit-scrollbar {
    width: 8px; /* 设置滚动条的宽度 */
    // height: 10px; /* 设置滚动条的高度（对于水平滚动条） */
  }
  .main_details::-webkit-scrollbar-thumb {
    background-color: #cbcbcb; /* 滚动条的颜色 */
    border-radius: 15px; /* 滚动条的圆角 */
  }

  .main_details::-webkit-scrollbar-thumb:hover {
    background-color: #b3b2b2; /* 鼠标悬停时的颜色 */
  }
}

.main_first_top {
  margin: 0 auto;
  padding: 0;
  width: 1000px;
  height: 50px;
  flex-wrap: nowrap;
  background-color: white;
  display: flex;
  align-items: center;
}

.company-popover {
  .company-info {
    padding: 16px;

    .header {
      margin-bottom: 12px;
      display: flex;
      justify-content: space-between;
      align-items: center;

      .company-name {
        margin: 0;
        font-size: 18px;
        color: #303133;
      }

      .meta {
        .el-tag {
          margin-left: 8px;
        }
      }
    }

    .company-image {
      width: 100%;
      height: 160px;
      margin-bottom: 12px;
      border-radius: 4px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    }

    .contact-info {
      display: flex;
      flex-direction: column;

      span {
        display: flex;
        align-items: center;
        margin: 4px 0;

        .el-icon {
          margin-right: 6px;
          color: #909399;
        }
      }
    }

    .el-descriptions__item {
      padding: 8px 0;
    }
  }
}

.el-input {
  height: 38px;
  border-radius: 20px;
}

.el-button {
  height: 38px;
  border-radius: 20px;
}
.main_first_bom {
  margin: 10px auto;
  width: 1000px;
  height: 25px;
  flex-wrap: nowrap;
  ul {
    margin: 0;
    padding: 0;
    height: 25px;
    line-height: 25px;
    li {
      display: inline-block;
      line-height: 90px;
      height: 25px;
      margin-right: 13px;
      background-color: #f8f8f8;
      color: #222222;
      span {
        height: 25px;
        line-height: 25px;
      }
    }
  }
  .el-dropdown-link:hover {
    background-color: #e5f8f8;
    color: #00bebd;
  }
}

.main_flex-row {
  //TODO:直接给el-row设置宽度就可以控制el-col不受浏览器宽度的影响
  margin: 0 auto;
  width: 900px;
  flex-wrap: nowrap;
  .infinite-list {
    height: 490px;
    padding: 0;
    .infinite-list-item {
      &:hover {
        border: 1px solid #18c4c4; /* 设置边框为蓝色 */
        cursor: pointer;
      }
      display: block;
      height: 130px;
      padding: 0;
      border-radius: 10px;
      border: 1px solid transparent; /* 添加默认透明边框 */
      box-sizing: border-box; /* 确保��框不会撑大盒子 */
      background-color: white;
      margin-bottom: 10px;
      white-space: nowrap;
      overflow: hidden;
      line-height: 22px;

      .li_top_left {
        display: inline-block;
        margin: 7px 130px 7px 0px;
        text-align: left;
        width: 100.56px;
        color: #2eb5b6;
        line-height: 40px;
        font-size: 18px;
      }
      .li_top_right {
        display: inline-block;
        color: #fe574a;
        text-align: center;
        line-height: 40px;
        font-size: 18px;
      }
      .li_top_li {
        display: flex;
        padding-left: 30px;
        margin: 0 0 10px 0;
        width: 200px;

        li {
          background-color: #ededed;
          display: inline-block;
          margin-bottom: 10px;
          margin: 0 5px;
          height: 25px;
          border-radius: 6px;
          color: #666666;
        }
      }
      .right_li_bottom {
        height: 30px;
        text-align: left;
        margin-left: 10px;

        a {
          height: 30px;
          width: 30px;

          display: inline;
          font-size: 15px;
          color: #676767;
          text-decoration: none;
        }

        .bottom_span_top {
          margin-left: 10px;
          width: 220px;

          display: inline-block;
          text-align: left;
          height: 34px;
          font-size: 20px;
          color: #676767;
          a {
            font-size: 17px;
            display: inline-block;
          }
        }
        .bottom_span_bom {
          display: inline-block;
          height: 30px;
          text-align: center;
          width: 80px;
          margin-left: 40px;
        }
      }
    }
  }
}
.infinite-list::-webkit-scrollbar {
  width: 8px; /* 设置滚动条的宽度 */
  // height: 10px; /* 设置滚动条的高度（对于水平滚动条） */
}
.infinite-list::-webkit-scrollbar-thumb {
  background-color: #cbcbcb; /* 滚动条的颜色 */
  border-radius: 15px; /* 滚动条的圆角 */
}

.infinite-list::-webkit-scrollbar-thumb:hover {
  background-color: #b3b2b2; /* 鼠标悬停时的颜色 */
}

.header_imgInfo {
  display: inline-block;
  width: 30px;
  background-color: aqua;
}

.header_img_span {
  height: 28px;
}
.header_row_col2 ul {
  padding: 0;
  margin: 0;
  white-space: nowrap; /* 防止列表项换行 */
  overflow: hidden; /* 隐藏超出部分 */
  li {
    display: inline-block;
    margin-left: 20px;
    margin-bottom: 10px;
    line-height: 40px; //解决高度不对
    height: 40px;
  }
  img {
    height: 28px;
    width: 28px;
  }
}

.header_row_col1 ul {
  padding: 0;
  margin: 0;
  white-space: nowrap; /* 防止列表项换行 */
  overflow: hidden; /* 隐藏超出部分 */
}
.header_row_col1 ul li {
  display: inline-block;
  margin-left: 20px;
  line-height: 5px;
}
.el-header,
.el-footer {
  color: #333;
  text-align: center;
  line-height: 60px;
  background: linear-gradient(90deg, rgba(238, 225, 255, 0.22) 0, rgba(164, 211, 255, 0.22) 100%)
    top left/100% 100% no-repeat;
}

.el-aside {
  background-color: #d3dce6;
  color: #333;
  text-align: center;
  line-height: 200px;
}

.el-main {
  margin: 0;
  padding: 0;
  background: linear-gradient(90deg, rgba(238, 225, 255, 0.22) 0, rgba(164, 211, 255, 0.22) 100%)
    top left/100% 100% no-repeat;
  color: #333;
  text-align: center;
  line-height: 160px;
  height: 89vh;
}
html {
  margin: 0;
  padding: 0;
}
body > .el-container {
  margin-bottom: 40px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}

/* 媒体查询，调整样式以适应小屏幕 */
/* @media (max-width: 3000px) {
    .header_row_col2 ul {
        overflow: hidden;
    }
    } */

.dialog-footer {
  .el-button {
    margin-left: 12px;
  }
}

.details_right {
  .el-button {
    margin-left: 10px;
  }
}
</style>
