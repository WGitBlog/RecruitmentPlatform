<template>
  <div class="interview-management-container">
    <!-- 状态导航 -->
    <div class="status-nav">
      <el-button
        v-for="tab in tabs"
        :key="tab.status"
        :type="activeTab === tab.status ? 'primary' : ''"
        @click="activeTab = tab.status"
      >
        {{ tab.label }} ({{ getCount(tab.status) }})
      </el-button>
    </div>

    <!-- 面试列表 -->
    <div class="interview-list">
      <div v-for="interview in filteredInterviews" :key="interview.id" class="interview-card">
        <div class="card-header">
          <!-- 面试者信息 -->
          <div class="company-info" v-if="boosId">
            <el-avatar :size="50" :src="interview.candidateImgUrl" />
            <div class="company-detail">
              <h3>{{ interview.candidateName }}</h3>
              <div class="job-meta">
                <span class="interview-type">面试时间</span>
                <span class="interview-date">{{ formatFullDate(interview.date) }}</span>
              </div>
            </div>
          </div>
          <div class="company-info" v-else>
            <el-avatar :size="50" :src="interview.companyImgUrl" />
            <div class="company-detail">
              <h3>{{ interview.companyName }}</h3>
              <div class="job-meta">
                <span class="interview-type">面试时间</span>
                <span class="interview-date">{{ formatFullDate(interview.date) }}</span>
              </div>
            </div>
          </div>

          <!-- 操作区 -->
          <div class="actions">
            <div class="status-action-row">
              <el-tag :type="statusColors[interview.status]" effect="dark">
                {{ statusMap[interview.status] }}
              </el-tag>
              <div class="action-buttons">
                <!-- 修改1: 状态2时只显示查看详情 -->
                <template v-if="interview.status !== 2">
                  <el-button
                    v-if="interview.status === 1"
                    type="danger"
                    size="small"
                    @click="cancelInterview(interview)"
                  >
                    取消面试
                  </el-button>
                  <el-button
                    v-if="interview.status === 3 && boosId"
                    type="primary"
                    size="small"
                    @click="showFeedbackDialog(interview)"
                  >
                    填写反馈
                  </el-button>
                </template>
                <el-button type="info" size="small" @click="showDetailsDialog(interview)">
                  查看详情
                </el-button>
              </div>
            </div>
          </div>
        </div>

        <!-- 面试详细信息 -->
        <div class="interview-detail">
          <div class="detail-item">
            <span class="label">面试职位：</span>
            <span class="value">{{ interview.jobTitle }}</span>
          </div>
          <div class="detail-item">
            <span class="label">面试方式：</span>
            <span class="value">{{ typeMap[interview.type] }}</span>
          </div>
          <div v-if="interview.type === 1" class="detail-item">
            <span class="label">会议平台：</span>
            <span class="value highlight">{{ interview.platform }}</span>
          </div>
          <div v-if="interview.type === 2" class="detail-item">
            <span class="label">面试地址：</span>
            <span class="value highlight">{{ interview.address }}</span>
          </div>
          <div class="detail-item">
            <span class="label">预计时长：</span>
            <span class="value">{{ interview.time }}小时</span>
          </div>
        </div>

        <!-- 反馈弹窗 -->
        <el-dialog v-model="interview.showFeedback" title="面试反馈" width="500px">
          <el-form :model="feedbackForm">
            <el-form-item label="综合评分">
              <el-rate v-model="feedbackForm.rating" />
            </el-form-item>
            <el-form-item label="面试结果">
              <el-radio-group v-model="feedbackForm.result">
                <el-radio :value="1">通过</el-radio>
                <el-radio :value="2">不通过</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="评语">
              <el-input v-model="feedbackForm.feedbackContent" type="textarea" :rows="4" />
            </el-form-item>
          </el-form>
          <template #footer>
            <el-button @click="interview.showFeedback = false">取消</el-button>
            <el-button type="primary" @click="submitFeedback(interview)">提交反馈</el-button>
          </template>
        </el-dialog>

        <!-- 详细信息弹窗 -->
        <el-dialog
          v-model="interview.showDetails"
          title="面试详情"
          width="500px"
        >
          <div class="dialog-content">
            <div class="detail-row">
              <span class="label">公司名称：</span>
              <span class="value">{{ interview.companyName }}</span>
            </div>
            <div class="detail-row">
              <span class="label">面试时间：</span>
              <span class="value">{{ formatFullDate(interview.date) }}</span>
            </div>
            <div class="detail-row">
              <span class="label">面试形式：</span>
              <span class="value">{{ typeMap[interview.type] }}</span>
            </div>
            <template v-if="interview.type === 1">
              <div class="detail-row">
                <span class="label">会议平台：</span>
                <span class="value">{{ interview.platform }}</span>
              </div>
              <div class="detail-row">
                <span class="label">会议链接：</span>
                <a :href="interview.link" target="_blank" class="value link">{{
                  interview.link
                }}</a>
              </div>
            </template>
            <template v-else>
              <div class="detail-row">
                <span class="label">面试地址：</span>
                <span class="value">{{ interview.address }}</span>
              </div>
            </template>
            <div class="detail-row">
              <span class="label">补充说明：</span>
              <p class="value description">{{ interview.details }}</p>
            </div>
          </div>
          <!-- 新增评分和反馈显示 -->
          <div class="detail-row" v-if="interview.rating">
            <span class="label">面试评分：</span>
            <span class="value">
              <el-rate :model-value="interview.rating" disabled />
              ({{ interview.rating }}星)
            </span>
          </div>
          <div class="detail-row" v-if="interview.result">
            <span class="label">面试结果：</span>
            <p class="value description">{{ interview.result==1? '通过' : '未通过' }}</p>
          </div>
          <div class="detail-row" v-if="interview.feedbackContent">
            <span class="label">面试反馈：</span>
            <p class="value description">{{ interview.feedbackContent }}</p>
          </div>
          <template #footer>
            <el-button type="primary" @click="interview.showDetails = false">关闭</el-button>
          </template>
        </el-dialog>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, reactive } from 'vue'
import { getInterviews, submitFeedbackInfo,cancelInterviewInfo } from '@/api/interview'
import dayjs from 'dayjs'

const userId=sessionStorage.getItem("userId")
const candidateId=sessionStorage.getItem("candidateId")
const boosId=sessionStorage.getItem("boosId")



// 状态映射配置
const statusMap = {
  0: '已取消',
  1: '待面试',
  2: '面试中',
  3: '待反馈',
  4: '已结束'
}
//面试类型
const typeMap = {
  1: '线上面试',
  2: '线下面试'
}
// 状态导航配置
const tabs = [
  { status: 'all', label: '全部' },
  { status: 1, label: '待面试' },
  { status: 2, label: '面试中' },
  { status: 3, label: '待反馈' },
  { status: 4, label: '已结束' },
  { status: 0, label: '已取消' }
]
// 状态颜色映射
const statusColors = {
  0: 'info',
  1: 'warning',
  2: 'primary',
  3: 'danger',
  4: 'success'
}
//对应面试者来说:面试状态（0: 已取消, 1: 待面试, 2: 面试中,3: 待反馈, 4: 等待结果）---对应boss来说:面试状态（0: 已取消, 1: 待面试, 2: 面试中,3: 待反馈, 4: 发布结果）
// 反馈表单
const feedbackForm = reactive({
  rating: 0,
  result: null,
  status: 4,
  feedbackContent: ''
})
// 数据获取
const interviews = ref([])
onMounted(async () => {
  const res = await getInterviews(userId)
  interviews.value = res.data.map((item) => ({
    ...item,
    showDetails: false,
    showFeedback: false
  })).reverse()
})

// 日期格式化
const formatDate = (timestamp) => {
  return dayjs(timestamp).format('YYYY/MM/DD HH:mm')
}

const formatFullDate = (timestamp) => {
  return dayjs(timestamp).format('YYYY年MM月DD日 HH:mm')
}

// 过滤逻辑
const activeTab = ref('all')
const filteredInterviews = computed(() => {
  return activeTab.value === 'all'
    ? interviews.value
    : interviews.value.filter((i) => i.status === activeTab.value)
})

const getCount = (status) => {
  return status === 'all'
    ? interviews.value.length
    : interviews.value.filter((i) => i.status === status).length
}

// 操作方法
const cancelInterview = async (interview) => {
  interview.status = 0
  // 调用取消接口
  await cancelInterviewInfo(interview.id).then((res) => {
    if(res.code!==1){
      ElMessage.error(res.msg)
      return
    }
    ElMessage.success(res.data)
    // 取消成功后，更新状态
    interview.status = 0
  })

}

const showFeedbackDialog = (interview) => {
  interview.showFeedback = true
  feedbackForm.rating = interview.rating || 0
  feedbackForm.result = null
  feedbackForm.feedbackContent = ''
}

const submitFeedback = async (interview) => {
  // 更新状态和反馈内容
  await submitFeedbackInfo({
    id: interview.id,
    ...feedbackForm
  }).then((res) => {
 // 直接更新面试数据
  interview.rating = feedbackForm.rating
  interview.feedbackContent = feedbackForm.feedbackContent
  interview.status = 4 // 修改为已结束状态
  interview.result = feedbackForm.result
  // 根据结果更新状态
  if (feedbackForm.result !== null) {
    interview.status = feedbackForm.result ? 4 : 3 // 通过设为4，不通过保持3
  }
  })
  // 关闭弹窗
  interview.showFeedback = false
}

const showDetailsDialog = (interview) => {
  interview.showDetails = true
}
</script>

<style scoped>
.interview-management-container {
  padding: 24px;
  max-width: 1200px;
  margin: 0 auto;
}

.status-nav {
  margin-bottom: 24px;
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.interview-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
  padding: 24px;
  transition: box-shadow 0.3s;
}

.interview-card:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.company-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.company-detail h3 {
  margin: 0;
  font-size: 18px;
  color: #303133;
}

.job-meta {
  margin-top: 8px;
  display: flex;
  gap: 12px;
  color: #909399;
  font-size: 14px;
}

.status-action-row {
  display: flex;
  align-items: center;
  gap: 12px;
}

.action-buttons {
  display: flex;
  gap: 8px;
}

.interview-detail {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
  padding: 16px 0;
  border-top: 1px solid #ebeef5;
}

.detail-item {
  display: flex;
  align-items: center;
}

.label {
  color: #909399;
  min-width: 80px;
}

.value {
  color: #606266;
}

.highlight {
  color: #409eff;
  font-weight: 500;
}

.link {
  text-decoration: underline;
  cursor: pointer;
}

.dialog-content {
  display: grid;
  gap: 12px;
}

.detail-row {
  display: flex;
  align-items: baseline;
  margin-bottom: 12px;
}

.detail-row .label {
  width: 100px;
  flex-shrink: 0;
  color: #909399;
}

.description {
  white-space: pre-wrap;
  line-height: 1.6;
}

@media (max-width: 768px) {
  .card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .status-action-row {
    width: 100%;
    justify-content: space-between;
  }

  .interview-detail {
    grid-template-columns: 1fr;
  }
}
</style>