<template>
  <div class="job-list-container">
    <el-card class="filter-container">
      <el-form :inline="true" :model="queryParams" class="demo-form-inline">
        <!-- <el-form-item label="职位名称">
          <el-input v-model="queryParams.jobName" placeholder="请输入职位名称" clearable />
        </el-form-item>
        <el-form-item label="企业名称">
          <el-input v-model="queryParams.companyName" placeholder="请输入企业名称" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="审核中" :value="1" />
            <el-option label="已发布" :value="2" />
            <el-option label="已关闭" :value="3" />
            <el-option label="已删除" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item> -->
      </el-form>
    </el-card>

    <el-card class="table-container">
      <el-table :data="jobList" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="jobTitle" label="职位名称" />
        <el-table-column prop="companyName" label="企业名称" />
        <el-table-column prop="salaryRange" label="薪资" />
        <el-table-column prop="workLocation" label="工作地点" />
        <el-table-column prop="jobCategory" label="职位类别" />
        <el-table-column prop="review" label="状态">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.review)">{{ getStatusText(row.review) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">查看</el-button>
            <el-button type="warning" link @click="handleToggleStatus(row)">
              {{ row.review === 2 ? '关闭' : '发布' }}
            </el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="queryParams.pageNum"
          v-model:page-size="queryParams.pageSize"
          :page-sizes="[10, 20, 30, 50]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 职位详情弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      title="职位详情"
      width="50%"
    >
      <div class="job-detail" v-if="currentJob">
        <div class="detail-item">
          <span class="label">职位名称：</span>
          <span>{{ currentJob.jobTitle }}</span>
        </div>
        <div class="detail-item">
          <span class="label">公司名称：</span>
          <span>{{ currentJob.companyName }}</span>
        </div>
        <div class="detail-item">
          <span class="label">薪资：</span>
          <span>{{ currentJob.salaryRange }}</span>
        </div>
        <div class="detail-item">
          <span class="label">工作地点：</span>
          <span>{{ currentJob.workLocation }}</span>
        </div>
        <div class="detail-item">
          <span class="label">职位类别：</span>
          <span>{{ currentJob.jobCategory }}</span>
        </div>
        <div class="detail-item">
          <span class="label">职位描述：</span>
          <span>{{ currentJob.jobDescription }}</span>
        </div>
        <div class="detail-item">
          <span class="label">技术要求：</span>
          <span>{{ currentJob.requiredTechnology }}</span>
        </div>
        <div class="detail-item">
          <span class="label">发布时间：</span>
          <span>{{ currentJob.createTime }}</span>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getJobInfo } from '@/api/job'
import {updateJobReview,updateDeleteJob} from '@/api/job'
import 'element-plus/theme-chalk/el-message.css'
import 'element-plus/theme-chalk/el-message-box.css'
// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 8,
  jobName: '',
  companyName: '',
  status: ''
})

const loading = ref(false)
const total = ref(0)
const dialogVisible = ref(false)
const currentJob = ref(null)
const jobList = ref([])

// 获取职位列表
const getList = async () => {
  loading.value = true
  try {
    const res = await getJobInfo(queryParams.pageNum, queryParams.pageSize, '', '','',null,null, 2)
    if (res.data && res.data.records) {
      jobList.value = res.data.records
      total.value = res.data.total
    }
  } catch (error) {
    console.error('获取职位列表失败：', error)
    ElMessage.error('获取职位列表失败')
  } finally {
    loading.value = false
  }
}

// 处理函数
const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

const resetQuery = () => {
  queryParams.jobName = ''
  queryParams.companyName = ''
  queryParams.status = ''
  handleQuery()
}

const handleView = (row) => {
  currentJob.value = row
  dialogVisible.value = true
}

const handleToggleStatus = async (row) => {
  const action = row.review === 2 ? '关闭' : '恢复'


  
  ElMessageBox.confirm(`确认${action}该职位？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await updateJobReview(row.id, row.review === 2 ? 3 : 2)
    ElMessage.success(`${action}成功`)
    getList()
  })
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确认删除该职位？删除后不可恢复', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await updateDeleteJob(row.id)
    ElMessage.success('删除成功')
    getList()
  })
}

const handleSizeChange = (val) => {
  queryParams.pageSize = val
  getList()
}

const handleCurrentChange = (val) => {
  queryParams.pageNum = val
  getList()
}

// 添加状态处理函数
const getStatusType = (status) => {
  const map = {
    1: 'warning',   // 审核中
    2: 'success',   // 已发布
    3: 'info',      // 已关闭
    4: 'danger'     // 已删除
  }
  return map[status] || 'info'
}

const getStatusText = (status) => {
  const map = {
    1: '审核中',
    2: '已发布',
    3: '已关闭',
    4: '已删除'
  }
  return map[status] || '未知状态'
}

// 页面加载时获取数据
onMounted(() => {
  getList()
})
</script>

<style scoped lang="scss">
.job-list-container {
  .filter-container {
    margin-bottom: 20px;
  }
  
  .table-container {
    margin-bottom: 20px;
  }

  .pagination-container {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}

.job-detail {
  h3 {
    margin-bottom: 20px;
    color: #303133;
  }
  
  p {
    margin: 10px 0;
    color: #606266;
  }
}
</style> 