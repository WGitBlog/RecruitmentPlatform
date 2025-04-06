<template>
  <div class="boss-profile">
    <!-- 顶部个人信息卡片 -->
    <div class="profile-card">
      <el-card>
        <div class="profile-header">
          <el-avatar :size="64" :src="boosInfo?.boosImg" />
          <div class="profile-info">
            <div class="name-position">
              <h2>{{ boosInfo?.boosName }}</h2>
              <el-tag type="success">{{ boosInfo?.companyPosition }}</el-tag>
            </div>
            <div class="contact-info">
              <p><i class="el-icon-phone" /> {{ boosInfo?.boosPhone }}</p>
              <p><i class="el-icon-message" /> {{ boosInfo?.boosEmail }}</p>
            </div>
          </div>
          <el-button
            type="primary"
            size="small"
            style="marginbottom: 50px"
            @click="handleEditBoosInfo"
          >
            编辑个人信息
          </el-button>
        </div>
      </el-card>
    </div>

    <!-- 公司信息卡片 -->
    <div class="company-info">
      <el-card>
        <template #header>
          <div class="card-header">
            <div class="company-title">
              <img :src="companyInfo?.companyImg" class="company-logo" />
              <h2>{{ companyInfo?.companyName }}</h2>
              <el-tag v-if="companyInfo?.companyMarket" type="success">已上市</el-tag>
            </div>
            <el-button type="primary" size="small" @click="handleEditCompany">
              编辑公司信息
            </el-button>
          </div>
        </template>

        <el-descriptions :column="2" border>
          <el-descriptions-item label="公司规模">
            {{ companyInfo?.companyScale }}
          </el-descriptions-item>
          <el-descriptions-item label="成立年份">
            {{ companyInfo?.establishedYear }}年
          </el-descriptions-item>
          <el-descriptions-item label="所属行业">
            {{ companyInfo?.industry }}
          </el-descriptions-item>
          <el-descriptions-item label="公司类型">
            {{ companyInfo?.companyType }}
          </el-descriptions-item>
          <el-descriptions-item label="联系电话">
            {{ companyInfo?.contactNumber }}
          </el-descriptions-item>
          <el-descriptions-item label="公司邮箱">
            {{ companyInfo?.email }}
          </el-descriptions-item>
          <el-descriptions-item label="公司地址" :span="2">
            {{ companyInfo?.location }}
          </el-descriptions-item>
          <el-descriptions-item label="上市状态">
            <el-tag :type="companyInfo?.companyMarket ? 'success' : 'info'">
              {{ companyInfo?.companyMarket ? '已上市' : '未上市' }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </el-card>
    </div>

    <!-- 简历管理 -->
    <div class="collect">
      <el-card>
        <template #header>
          <div class="card-header">
            <h3>简历收藏</h3>
            <el-button type="primary" @click="handleAddJob"> 简历管理 </el-button>
          </div>
        </template>
        <resume-list :users="collectedUsers" @view="handleViewResume" @delete="handleDelResume" />
      </el-card>
    </div>

    <!-- 职位管理 -->
    <div class="job-management">
      <el-card>
        <template #header>
          <div class="card-header">
            <h3>职位管理</h3>
            <el-button type="primary" @click="handleAddJob"> 发布新职位 </el-button>
          </div>
        </template>

        <el-tabs v-model="activeTab">
          <el-tab-pane label="在招职位" name="active">
            <job-list :jobs="activeJobPosts" @edit="handleEditJob" @close="handleCloseJob" />
          </el-tab-pane>
          <el-tab-pane label="待审核" name="pending">
            <job-list
              :jobs="pendingJobs"
              @view="handleViewJob"
              @edit="handleEditJob"
              @close="handleCloseJob"
            />
          </el-tab-pane>
          <el-tab-pane label="已拒绝" name="rejected">
            <job-list
              :jobs="rejectedJobs"
              @edit="handleEditJob"
              @delete="handleDeleteJob"
              @reopen="handleReopenJob"
            />
          </el-tab-pane>
          <el-tab-pane label="已关闭" name="closed">
            <job-list :jobs="closedJobs" @delete="handleDeleteJob" @reopen="handleReopenJob" />
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </div>

    <!-- 添加个人信息的弹窗 -->
    <el-dialog v-model="boosDialogVisible" title="编辑个人信息" width="50%">
      <el-form ref="formRef" :model="boosEditForm" :rules="boosRules" label-width="100px">
        <el-form-item label="姓名" prop="boosName">
          <el-input v-model="boosEditForm.boosName" />
        </el-form-item>
        <el-form-item label="邮件" prop="boosEmail">
          <el-input v-model="boosEditForm.boosEmail" />
        </el-form-item>
        <el-form-item label="电话" prop="boosPhone">
          <el-input v-model="boosEditForm.boosPhone" />
        </el-form-item>
        <el-form-item label="职位" prop="companyPosition">
          <el-input v-model="boosEditForm.companyPosition" />
        </el-form-item>
        <el-form-item label="头像" prop="boosImg">
          <el-upload
            class="avatar-uploader"
            action="http://localhost:8080/boos/uploadImg"
            :show-file-list="false"
            :on-success="handleBoosLogoSuccess"
            :before-upload="beforeLogoUpload"
          >
            <img v-if="boosEditForm.boosImg" :src="boosEditForm.boosImg" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="boosDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleBoosSubmit">确认</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 添加编辑公司信息的弹窗 -->
    <el-dialog v-model="dialogVisible" title="编辑公司信息" width="50%">
      <el-form ref="formRef" :model="editForm" :rules="rules" label-width="100px">
        <el-form-item label="公司名称" prop="companyName">
          <el-input v-model="editForm.companyName" />
        </el-form-item>

        <el-form-item label="公司规模" prop="companyScale">
          <el-select v-model="editForm.companyScale" placeholder="请选择公司规模">
            <el-option label="0-20人" value="0-20人" />
            <el-option label="20-99人" value="20-99人" />
            <el-option label="100-499人" value="100-499人" />
            <el-option label="500-1000人" value="500-1000人" />
            <el-option label="1000人以上" value="1000人以上" />
          </el-select>
        </el-form-item>

        <el-form-item label="所属行业" prop="industry">
          <el-input v-model="editForm.industry" />
        </el-form-item>

        <el-form-item label="公司类型" prop="companyType">
          <el-select v-model="editForm.companyType" placeholder="请选择公司类型">
            <el-option label="国企" value="国企" />
            <el-option label="私企" value="私企" />
            <el-option label="合资" value="合资" />
            <el-option label="外企" value="外企" />
            <el-option label="股份制企业" value="股份制企业" />
          </el-select>
        </el-form-item>

        <el-form-item label="成立年份" prop="establishedYear">
          <el-date-picker
            v-model="editForm.establishedYear"
            type="year"
            placeholder="选择年份"
            value-format="YYYY"
            :clearable="false"
          />
        </el-form-item>

        <el-form-item label="上市状态" prop="companyMarket">
          <el-switch v-model="editForm.companyMarket" active-text="已上市" inactive-text="未上市" />
        </el-form-item>

        <el-form-item label="公司邮箱" prop="email">
          <el-input v-model="editForm.email" />
        </el-form-item>

        <el-form-item label="公司地址" prop="location">
          <el-input v-model="editForm.location" type="textarea" :rows="2" />
        </el-form-item>

        <el-form-item label="公司Logo" prop="companyImg">
          <el-upload
            class="avatar-uploader"
            action="http://localhost:8080/company/uploadImg"
            :show-file-list="false"
            :on-success="handleLogoSuccess"
            :before-upload="beforeLogoUpload"
          >
            <img v-if="editForm.companyImg" :src="editForm.companyImg" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确认</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 添加发布新职位的弹窗 -->
    <el-dialog
      v-model="jobDialogVisible"
      :title="partition === 1 ? '发布新工作' : '修改工作信息'"
      width="50%"
    >
      <el-form :model="jobForm" :rules="jobRules" label-width="100px">
        <el-form-item label="职位名称" prop="jobTitle">
          <el-input v-model="jobForm.jobTitle" placeholder="请输入职位名称" />
        </el-form-item>

        <el-form-item label="薪资范围" prop="salaryRange">
          <el-input v-model="jobForm.salaryRange" placeholder="例如：15k-25k" />
        </el-form-item>

        <el-form-item label="职位类别" prop="jobCategory">
          <el-select v-model="jobForm.jobCategory" placeholder="请选择职位类别">
            <el-option label="咨询" value="咨询" />
            <el-option label="医疗" value="医疗" />
            <el-option label="金融" value="金融" />
            <el-option label="教育" value="教育" />
            <el-option label="零售" value="零售" />
            <el-option label="工程" value="工程" />
            <el-option label="研发" value="研发" />
            <el-option label="旅游" value="旅游" />
            <el-option label="服务" value="服务" />
            <el-option label="建筑" value="建筑" />
            <el-option label="科技" value="科技" />
            <el-option label="设计" value="设计" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>

        <el-form-item label="工作地点" prop="workLocation">
          <el-input v-model="jobForm.workLocation" placeholder="请输入工作地点" />
        </el-form-item>

        <el-form-item label="学历要求" prop="educationRequirements">
          <el-select v-model="jobForm.educationRequirements" placeholder="请选择学历要求">
            <el-option label="大专及以上" value="大专及以上" />
            <el-option label="本科及以上" value="本科及以上" />
            <el-option label="硕士及以上" value="硕士及以上" />
            <el-option label="博士及以上" value="博士及以上" />
          </el-select>
        </el-form-item>

        <el-form-item label="所需技术" prop="requiredTechnology">
          <el-input v-model="jobForm.requiredTechnology" placeholder="请输入所需技术" />
        </el-form-item>

        <el-form-item label="工作天数" prop="weeklyDays">
          <el-input-number v-model="jobForm.weeklyDays" :min="1" :max="7" />
        </el-form-item>

        <el-form-item label="职位描述" prop="jobDescription">
          <el-input
            v-model="jobForm.jobDescription"
            type="textarea"
            :rows="4"
            placeholder="请输入职位描述，每条描述用回车分隔"
          />
        </el-form-item>

        <el-form-item label="职位详情" prop="jobDetails">
          <el-input
            v-model="jobForm.jobDetails"
            type="textarea"
            :rows="4"
            placeholder="请输入职位详细信息"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="jobDialogVisible = false">取消</el-button>
          <el-button
            type="primary"
            @click="partition === 1 ? handleJobSubmit() : handleSubmitEditJob()"
          >
            {{ partition === 1 ? '确认发布' : '确认修改' }}</el-button
          >
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { storeToRefs } from 'pinia'
import { useBoosStore } from '@/stores/boos'
import { useCompanyStore } from '@/stores/company'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import 'element-plus/theme-chalk/el-message.css'
import 'element-plus/theme-chalk/el-message-box.css'
import { getBoosInfo, updateBoosInfo, updateDelResume } from '@/api/boos'
import { getCompanyInfo, updateCompanyInfo } from '@/api/company'
import { getJobsByBoosId, addJobInfo, updateJobReview, updateDeleteJob, updateJob } from '@/api/job'
import type { Job } from '@/interface/index'
import { getCandidateInfo, getCandidatesByIds } from '@/api/candidate'

// Store 实例初始化
const bossStore = useBoosStore() // Boss相关的状态管理
const companyStore = useCompanyStore() // 公司相关的状态管理

const userId = sessionStorage.getItem('userId')

const activeTab = ref('active') // 当前激活的标签页
const partition = ref(1) // 1：代表发布新工作  2：代表更改工作信息

// 从 store 中解构需要的状态，保持响应性
const { boosInfo, activeJobPosts, closedJobs, pendingJobs, rejectedJobs, jobPosts } =
  storeToRefs(bossStore) // Boss信息和在招职位
const { companyInfo } = storeToRefs(companyStore) // 公司信息
const collectedUsers = ref([]) // 收藏的用户信息

// 组件挂载时初始化数据
onMounted(async () => {
  //获取boosId
  const boosId = sessionStorage.getItem('boosId')
  await getBoosInfo(boosId).then(async (res) => {
    boosInfo.value = res.data
    //根据收藏的简历userid查询出对应的candidate的resume信息
    if (JSON.parse(res.data.collections)!=null&&JSON.parse(res.data.collections)!='') {
      await getCandidatesByIds(JSON.parse(res.data.collections)).then((res1) => {
        collectedUsers.value = res1.data
      })
    }

    await getCompanyInfo(boosInfo.value.companyId).then((res) => {
      companyInfo.value = res.data
    })
  })
  // 根据BossId信息获取其发布的工作信息
  await getJobsByBoosId(boosId).then((res) => {
    // 处理时间戳为日期格式
    const jobs = res.data.map((job) => ({
      ...job,
      publishTime: new Date(parseInt(job.publishTime)).toLocaleString()
    }))
    //将数据放入boos的store.JobPost中
    jobPosts.value.push(...jobs)
  })
})

// 职位管理相关方法
const handleAddJob = async () => {
  //设置
  partition.value = 1
  // 重置职位表单数据
  jobForm.value = {
    boosId: Number(boosId),
    educationRequirements: '',
    id: null,
    jobCategory: '',
    jobDescription: [],
    jobDetails: '',
    jobTitle: '',
    publishTime: new Date().getTime().toString(), // 设置为当前时间戳
    requiredTechnology: '',
    salaryRange: '',
    weeklyDays: null,
    workLocation: '',
    review: 1 // 设置为待审核状态
  }
  jobDialogVisible.value = true // 显示添加职位对话框
}

const handleCloseJob = async (jobId: number) => {
  try {
    // 调用 API 更新职位状态为已关闭
    await updateJobReview(
      jobId,
      3 // 0 表示已关闭
    )

    // 更新本地状态
    const job = jobPosts.value.find((job) => job.id === jobId)
    if (job) {
      job.review = 3
    }

    ElMessage.success('职位已关闭')
  } catch (error) {
    ElMessage.error('操作失败')
    console.error(error)
  }
}

const handleDeleteJob = async (jobId: number) => {
  // 实现刪除职位逻辑
  try {
    // 调用 API 删除职位
    await updateDeleteJob(jobId)

    // 更新本地状态
    jobPosts.value = jobPosts.value.filter((job) => job.id !== jobId)

    ElMessage.success('职位已删除')
  } catch (error) {
    ElMessage.error('删除失败')
    console.error(error)
  }
}

// 职位操作方法
const handleEditJob = async (jobId: number) => {
  //设置
  partition.value = 2
  // 找到要编辑的职位
  const job = jobPosts.value.find((job) => job.id === jobId)
  if (!job) {
    ElMessage.error('未找到该职位')
    return
  }

  // 填充表单数据
  jobForm.value = {
    ...job,
    publishTime: new Date().getTime().toString() // 更新时间戳为当前时间
  }

  // 显示对话框，复用发布职位的对话框
  jobDialogVisible.value = true
}

const handleSubmitEditJob = async () => {
  // 找到编辑的职位
  const job = jobPosts.value.find((job) => job.id === jobForm.value.id)
  if (!job) {
    ElMessage.error('未找到该职位')
    return
  }

  await updateJob(jobForm.value)
  // 更新本地状态，并转换时间戳为日期格式
  const updatedJob = {
    ...jobForm.value,
    publishTime: new Date(parseInt(jobForm.value.publishTime.toString())).toLocaleString()
  }
  Object.assign(job, updatedJob)
  // 关闭对话框
  jobDialogVisible.value = false // 加这一行
}

const handleReopenJob = async (jobId: number) => {
  try {
    // 调用 API 更新职位状态为待审核
    await updateJobReview(
      jobId,
      1 // 改为 1，表示重新进入审核状态
    )

    // 更新本地状态
    const job = jobPosts.value.find((job) => job.id === jobId)
    if (job) {
      job.review = 1
    }

    ElMessage.success('职位已提交审核')
  } catch (error) {
    ElMessage.error('操作失败')
    console.error(error)
  }
}

//查看相应的简历
const handleViewResume = async (resume: string) => {
  if (resume) {
    window.open(resume)
  } else {
    ElMessage.warning('简历暂不可预览')
  }
}
//删除相应的简历
const handleDelResume = async (userId: number) => {
  await updateDelResume(boosId, userId).then((res) => {
    if (res.code == 1) {
      ElMessage.success('删除简历成功')
      //删除成功后更新数据
      collectedUsers.value = collectedUsers.value.filter((user) => user.id !== userId)
    } else {
      ElMessage.error('删除简历失败')
    }
  })
}

// 公司信息编辑相关
const dialogVisible = ref(false) // 编辑对话框显示状态

// 编辑表单数据
const editForm = ref({
  id: null,
  companyName: null,
  location: null,
  industry: null,
  establishedYear: null,
  contactNumber: null,
  email: null,
  companyImg: null,
  companyScale: null,
  companyMarket: null,
  companyType: null
})

// 表单验证规则
const rules = {
  companyName: [
    { required: true, message: '请输入公司名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ]
  // ... 其他验证规则
}

// 编辑公司信息
const handleEditCompany = () => {
  // 用当前公司信息填充表单
  editForm.value = {
    ...companyInfo.value,
    establishedYear: String(companyInfo.value?.establishedYear)
  }
  dialogVisible.value = true
}

//编辑boos信息
const boosEditForm = ref({
  id: null,
  boosName: null,
  boosImg: null,
  boosEmail: null,
  boosPhone: null,
  companyId: null,
  companyPosition: null,
  applicantCdsId: null
})

const handleEditBoosInfo = () => {
  boosEditForm.value = boosInfo.value
  boosDialogVisible.value = true
}

// Logo上传相关方法
const handleLogoSuccess = (res: any) => {
  // 确保res.data返回的是完整的URL路径
  if (res.data && !res.data.startsWith('http')) {
    // 如果返回的不是完整URL，需要拼接服务器地址
    editForm.value.companyImg = `http://your-server-domain${res.data}`
  } else {
    editForm.value.companyImg = res.data
  }
}
const handleBoosLogoSuccess = (res: any) => {
  // 确保res.data返回的是完整的URL路径
  if (res.data && !res.data.startsWith('http')) {
    // 如果返回的不是完整URL，需要拼接服务器地址
    boosEditForm.value.boosImg = `http://your-server-domain${res.data}`
  } else {
    boosEditForm.value.boosImg = res.data
  }
}

const beforeLogoUpload = (file: File) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPG) {
    ElMessage.error('上传头像图片只能是 JPG/PNG 格式!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('上传头像图片大小不能超过 2MB!')
    return false
  }
  return true
}

// 提交公司信息
const handleSubmit = async () => {
  //先发送更新请求
  await updateCompanyInfo(editForm.value)
  //将请求结果扶着给Store中的companyInfo
  editForm.value.id = boosInfo.value.companyId
  companyInfo.value = editForm.value
  dialogVisible.value = false
}

const handleBoosSubmit = async () => {
  console.log(boosEditForm.value)
  //先发送更新请求
  await updateBoosInfo(boosEditForm.value)
  //将请求结果扶着给Store中的companyInfo
  boosInfo.value = boosEditForm.value
  boosDialogVisible.value = false
}

const jobDialogVisible = ref(false) // 职位对话框显示状态
const boosDialogVisible = ref(false) // boos信息对话框显示状态
const boosId = sessionStorage.getItem('boosId')
// 职位单数据
const jobForm = ref<Job>({
  boosId: Number(boosId),
  educationRequirements: '',
  id: null,
  jobCategory: '',
  jobDescription: [],
  jobDetails: '',
  jobTitle: '',
  publishTime: new Date().getTime().toString(), // 设置为当前时间戳
  requiredTechnology: '',
  salaryRange: '',
  weeklyDays: null,
  workLocation: '',
  review: null
})

// 职位表单验证规则
const jobRules = {
  jobTitle: [
    { required: true, message: '请输入职位名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字', trigger: 'blur' }
  ],
  salaryRange: [{ required: true, message: '请输入薪资范围', trigger: 'blur' }],
  jobCategory: [{ required: true, message: '请选择职位类别', trigger: 'change' }],
  workLocation: [{ required: true, message: '请输入工作地点', trigger: 'blur' }],
  educationRequirements: [{ required: true, message: '请选择学历要求', trigger: 'change' }],
  requiredTechnology: [{ required: true, message: '请输入所需技术', trigger: 'blur' }],
  weeklyDays: [{ required: true, message: '请输入每周工作天数', trigger: 'change' }],
  jobDescription: [{ required: true, message: '请输入职位描述', trigger: 'blur' }],
  jobDetails: [{ required: true, message: '请输入职位详情', trigger: 'blur' }]
}

// 提交职位信息
const handleJobSubmit = async () => {
  try {
    // 设置发布时间为当前时间戳
    jobForm.value.publishTime = new Date().getTime().toString()
    jobForm.value.jobDetails = jobForm.value.jobDetails.replace(/\n/g, '\\n')
    const res = await addJobInfo(jobForm.value)

    // 转换时间戳为日期格式后添加到本地状态

    // 更新本地状态
    jobPosts.value.push(jobForm.value)

    console.log(jobPosts.value)

    // 关闭对话框并显示成功消息
    jobDialogVisible.value = false
    ElMessage.success('职位发布成功，等待审核')

    // 重置表单
    jobForm.value = {
      boosId: Number(boosId),
      educationRequirements: '',
      id: null,
      jobCategory: '',
      jobDescription: [],
      jobDetails: '',
      jobTitle: '',
      publishTime: '',
      requiredTechnology: '',
      salaryRange: '',
      weeklyDays: null,
      workLocation: '',
      review: null
    }
  } catch (error) {
    ElMessage.error('职位发布失败')
    console.error(error)
  }
}

// 在 script setup 中添加处理查看的方法
const handleViewJob = (jobId: number) => {
  // 实现查看职位详情的逻辑
  const job = jobPosts.value.find((job) => job.id === jobId)
  if (job) {
    // 这里可以显示一个对话框展示职位详情
    ElMessage.info('查看职位详情：' + job.jobTitle)
  }
}
</script>







<style>
/* 添加一个全局样式 (注意这里不使用 scoped) */
body {
  margin: 0;
  padding: 0;
  min-height: 100vh;
  background: linear-gradient(180deg, #e6f3ff 0%, #ffffff 100%);
}
</style>

<style scoped>
.boss-profile {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
  /* 移除之前的背景渐变，因为已经应用到 body 上了 */
}

.profile-card {
  margin-bottom: 20px;
}

.profile-header {
  display: flex;
  align-items: center;
  gap: 20px;
}

.profile-info {
  flex: 1;
}

.name-position {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}

.name-position h2 {
  margin: 0;
  font-size: 20px;
}

.contact-info {
  color: #666;
}

.contact-info p {
  margin: 4px 0;
}

.company-title {
  display: flex;
  align-items: center;
  gap: 16px;
}

.company-logo {
  width: 40px;
  height: 40px;
  object-fit: cover;
  border-radius: 50%;
}

.company-title h2 {
  margin: 0;
  font-size: 18px;
}

.company-info,
.job-management,
.collect,
.statistics {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  margin: 0;
}

.statistic-value {
  font-size: 24px;
  font-weight: bold;
  text-align: center;
  color: #409eff;
}

.avatar-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 178px;
  height: 178px;
}

.avatar-uploader:hover {
  border-color: #409eff;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
  line-height: 178px;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}

.dialog-footer {
  padding-top: 20px;
  text-align: right;
}
</style> 