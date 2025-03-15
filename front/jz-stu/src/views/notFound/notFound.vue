<template> 
  <div class="interview-management-container"> 
    <!-- 状态导航 --> 
    <div class="status-nav"> 
      <el-button 
        v-for="tab in tabs" 
        :key="tab.status"  
        :type="activeTab === tab.status  ? 'primary' : ''" 
        @click="activeTab = tab.status"  
      > 
        {{ tab.label  }} ({{ getCount(tab.status)  }}) 
      </el-button> 
    </div> 
    <!-- 面试列表 --> 
    <div class="interview-list"> 
      <div v-for="interview in filteredInterviews" :key="interview.id"  class="interview-card"> 
        <div class="card-header"> 
          <!-- 应聘者信息 --> 
          <div class="applicant-info"> 
            <el-avatar :size="50" :src="interview.applicantAvatar"  /> 
            <div class="applicant-detail"> 
              <h3>{{ interview.applicantName  }}</h3> 
              <div class="job-title">{{ interview.jobTitle  }}</div> 
              <div class="interview-method">{{ interview.interviewMethod  }}</div> 
            </div> 
          </div> 
          <!-- 操作区 --> 
          <div class="actions"> 
            <el-tag :type="statusColors[interview.status]"> 
              {{ interview.status  }} 
            </el-tag> 
            <el-button 
              v-if="interview.status  === '待面试'" 
              type="danger" 
              size="small" 
              @click="cancelInterview(interview)" 
            > 
              取消面试 
            </el-button> 
            <el-button 
              v-if="interview.currentStep  === 1 && !interview.isEvaluated"  
              type="primary" 
              size="small" 
              @click="showFeedbackDialog(interview)" 
            > 
              填写反馈 
            </el-button> 
            <el-button 
              v-if="interview.isEvaluated"  
              type="info" 
              size="small" 
              @click="showDetailsDialog(interview)" 
            > 
              查看详细 
            </el-button> 
          </div> 
        </div> 
        <!-- 流程进度 --> 
      <el-steps :active="currentStepIndex(interview)" finish-status="success">
<el-step 
  v-for="(step, index) in interview.steps"  
  :key="index"
  :title="step.title" 
  :description="step.desc" 
  :status="statusMap[step.status]" 
/>
  </el-steps>
        <!-- 反馈弹窗 --> 
        <el-dialog v-model="interview.showFeedback"  title="面试反馈" width="30%"> 
          <el-form :model="feedbackForm"> 
            <el-form-item label="综合评分"> 
              <el-rate v-model="feedbackForm.score"  :size="30" /> 
            </el-form-item> 
            <el-form-item label="面试结果"> 
              <el-radio-group v-model="feedbackForm.result">  
                <el-radio :label="true">通过</el-radio> 
                <el-radio :label="false">不通过</el-radio> 
              </el-radio-group> 
            </el-form-item> 
            <el-form-item label="评语"> 
              <el-input v-model="feedbackForm.comment"  type="textarea" rows="4" /> 
            </el-form-item> 
          </el-form> 
          <template #footer> 
            <el-button @click="interview.showFeedback  = false">取消</el-button> 
            <el-button type="primary" @click="submitFeedback(interview)">提交反馈</el-button> 
          </template> 
        </el-dialog> 
        <!-- 详细信息弹窗 --> 
        <el-dialog v-model="interview.showDetails"  title="面试详细信息" width="30%"> 
          <div> 
            <p><strong>面试时间:</strong> {{ interview.date  }}</p> 
            <p><strong>面试类型:</strong> {{ interview.type  === 1 ? '线上面试' : '线下面试' }}</p> 
            <p v-if="interview.type  === 1"><strong>面试平台:</strong> {{ interview.platform  }}</p> 
            <p v-if="interview.type  === 1"><strong>面试链接:</strong> {{ interview.link  }}</p> 
            <p v-if="interview.type  === 2"><strong>面试地点:</strong> {{ interview.address  }}</p> 
            <p><strong>面试时长:</strong> {{ interview.time  }}</p> 
            <p><strong>面试评分:</strong> {{ interview.rating  }}</p> 
            <p><strong>企业反馈内容:</strong> {{ interview.feedback_content  }}</p> 
          </div> 
          <template #footer> 
            <el-button @click="interview.showDetails  = false">关闭</el-button> 
          </template> 
        </el-dialog> 
      </div> 
    </div> 
  </div> 
</template> 
 
<script setup> 
import { ref, reactive, computed } from 'vue' 
 
// 完整假数据结构 
const interviews = ref([ 
  { 
    id: 1, 
    status: '待面试', 
    applicantName: '李晓明', 
    applicantAvatar: 'https://example.com/avatar1.jpg',  
    jobTitle: '前端开发工程师', 
    interviewMethod: '线上视频面试', 
    steps: [
      { title: '简历筛选', desc: '2024-03-01 已通过', status: 1 },
      { title: '技术面试', desc: '待反馈', status: 1 },
      { title: '最终结果', desc: '-', status: 1 }
    ], 
    currentStep: 1, 
    isEvaluated: false, 
    showFeedback: false, 
    showDetails: false, 
    date: '2024-03-15 10:00:00', 
    type: 1, 
    time: '01:00:00', 
    platform: 'Zoom', 
    link: 'https://zoom.us/j/123456789',  
    address: null, 
    rating: null, 
    feedback_content: null 
  }, 
  { 
    id: 2, 
    status: '已通过', 
    applicantName: '张伟', 
    applicantAvatar: 'https://example.com/avatar2.jpg',  
    jobTitle: 'Java开发工程师', 
    interviewMethod: '现场面试', 
  steps: [
      { title: '简历筛选', desc: '2024-03-01 已通过', status: 3 },
      { title: '技术面试', desc: '待反馈', status: 3 },
      { title: '最终结果', desc: '-', status: 3}
    ],
    currentStep: 3, 
    isEvaluated: true, 
    showFeedback: false, 
    showDetails: false, 
    date: '2024-03-20 14:00:00', 
    type: 2, 
    time: '01:30:00', 
    platform: null, 
    link: null, 
    address: '北京市朝阳区某大厦', 
    rating: 4, 
    feedback_content: '该候选人技术能力较强，沟通能力也不错，符合岗位要求。' 
  } 
]) 

//定义反向状态映射
const statusMap = {
  1: 'success',
  2: 'process',
  3: 'wait'
}



 
// 反馈表单 
const feedbackForm = reactive({ 
  score: 0, 
  result: null, 
  comment: '' 
}) 
 
// 状态配置 
const tabs = [ 
  { status: 'all', label: '全部' }, 
  { status: '待面试', label: '待处理' }, 
  { status: '已通过', label: '通过' }, 
  { status: '未通过', label: '未通过' }, 
  { status: '已取消', label: '已取消' } 
] 
 
const statusColors = { 
  '待面试': 'warning', 
  '已通过': 'success', 
  '未通过': 'danger', 
  '已取消': 'info' 
} 
 
const activeTab = ref('all') 
 // 新增当前步骤计算函数 
const currentStepIndex = (interview) => {
  return interview.steps.findIndex(step  => step.status  === 'process')
}
// 计算属性 
const filteredInterviews = computed(() => { 
  if (activeTab.value  === 'all') return interviews.value  
  return interviews.value.filter((i)  => i.status  === activeTab.value)  
}) 
 
const getCount = (status) => { 
  return status === 'all' 
    ? interviews.value.length  
    : interviews.value.filter((i)  => i.status  === status).length 
} 
 
// 完整交互方法 
const showFeedbackDialog = (interview) => { 
  interview.showFeedback  = true 
  feedbackForm.score  = 0 
  feedbackForm.result  = null 
  feedbackForm.comment  = '' 
} 
 
const submitFeedback = (interview) => { 

} 
 
// 修改后的取消面试逻辑 
const cancelInterview = (interview) => {
  const currentStep = interview.steps.find(step  => step.status  === 'process')
  if(currentStep) {
    currentStep.status  = 'error'
    currentStep.desc  = `${new Date().toLocaleDateString()} 已取消`
  }
}
 
const showDetailsDialog = (interview) => { 
  interview.showDetails  = true 
} 
</script> 
 
<style scoped> 
.interview-management-container { 
  padding: 20px; 
  max-width: 1200px; 
  margin: 0 auto; 
} 
 
.status-nav { 
  margin-bottom: 20px; 
} 
.status-nav .el-button { 
  margin-right: 10px; 
} 
 
.interview-card { 
  background: #fff; 
  border-radius: 8px; 
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1); 
  margin-bottom: 20px; 
  padding: 20px; 
} 
 
.card-header { 
  display: flex; 
  justify-content: space-between; 
  align-items: center; 
  margin-bottom: 15px; 
} 
 
.applicant-info { 
  display: flex; 
  align-items: center; 
} 
.applicant-info .applicant-detail { 
  margin-left: 15px; 
} 
.applicant-info .applicant-detail h3 { 
  margin: 0; 
  font-size: 16px; 
} 
.applicant-info .applicant-detail .job-title { 
  color: #666; 
  font-size: 14px; 
} 
.applicant-info .applicant-detail .interview-method { 
  color: #999; 
  font-size: 12px; 
} 
 
.actions { 
  display: flex; 
  align-items: center; 
  gap: 10px; 
} 
 
.el-steps { 
  margin: 20px 0; 
  padding: 0 15px; 
} 
 
@media (max-width: 768px) { 
  .el-dialog { 
    width: 90% !important; 
  } 
  .card-header { 
    flex-direction: column; 
    align-items: flex-start; 
    gap: 10px; 
  } 
  .applicant-info { 
    width: 100%; 
    justify-content: space-between; 
  } 
} 
</style> 