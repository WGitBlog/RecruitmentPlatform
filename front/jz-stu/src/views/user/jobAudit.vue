<template>
  <div class="job-audit">
    <el-card class="box-card">
      <!-- 搜索区域 -->
      <div class="search-box">
        <el-input
          v-model="searchInput"
          placeholder="请输入职位名称"
          class="search-input"
          clearable
          @keyup.enter="handleQuery"
        />
        <el-button type="primary" @click="handleQuery">
          <el-icon><Search /></el-icon>搜索
        </el-button>
        <el-button @click="resetQuery">
          <el-icon><Refresh /></el-icon>重置
        </el-button>
      </div>

      <!-- 表格区域 -->
      <el-table :data="jobList" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="jobName" label="职位名称" />
        <el-table-column prop="companyName" label="公司名称" />
        <el-table-column prop="salary" label="薪资" />
        <el-table-column prop="location" label="工作地点" />
        <el-table-column prop="workTime" label="工作时间" />
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'warning' : scope.row.status === 2 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '待审核' : scope.row.status === 2 ? '已通过' : '已拒绝' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button
              type="success"
              size="small"
              @click="handleApprove(scope.row)"
             v-if="scope.row.status === 1"
            >
              通过
            </el-button>
            <el-button
              type="danger"
              size="small"
              @click="handleReject(scope.row)"
          v-if="scope.row.status === 1"
            >
              拒绝
            </el-button>
            <el-button
              type="primary"
              size="small"
              @click="handleDetail(scope.row)"
              
            >
              详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页区域 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 30, 40]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 职位详情对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="职位详情"
      width="50%"
    >
      <div class="job-detail" v-if="currentJob">
        <div class="detail-item">
          <span class="label">职位名称：</span>
          <span>{{ currentJob.jobName }}</span>
        </div>
        <div class="detail-item">
          <span class="label">公司名称：</span>
          <span>{{ currentJob.companyName }}</span>
        </div>
        <div class="detail-item">
          <span class="label">薪资：</span>
          <span>{{ currentJob.salary }}</span>
        </div>
        <div class="detail-item">
          <span class="label">工作地点：</span>
          <span>{{ currentJob.location }}</span>
        </div>
        <div class="detail-item">
          <span class="label">工作时间：</span>
          <span>{{ currentJob.workTime }}</span>
        </div>
        <div class="detail-item">
          <span class="label">职位描述：</span>
          <span>{{ currentJob.description }}</span>
        </div>
        <div class="detail-item">
          <span class="label">要求：</span>
          <span>{{ currentJob.requirements }}</span>
        </div>
      </div>
    </el-dialog>
  </div>
</template>



<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh } from '@element-plus/icons-vue'
import { getJobInfo, updateJobReview } from '@/api/job'
import 'element-plus/theme-chalk/el-message.css'
import 'element-plus/theme-chalk/el-message-box.css'



// 定义数据
const loading = ref(false)
const jobList = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const currentJob = ref(null)
const currentPage = ref(1)
const pageSize = ref(8)
const searchInput = ref('')


// 获取职位列表
const getJobList = async () => {
  loading.value = true
  try {
    const res = await getJobInfo(currentPage.value, pageSize.value, '' ,'', 1)
    jobList.value = res.data.records.map(item => ({
      id: item.id,
      jobName: item.jobTitle,
      companyName: item.companyName,
      salary: item.salaryRange,
      location: item.workLocation,
      workTime: `每周${item.weeklyDays}天`,
      status: item.review,
      description: item.jobDescription,
      requirements: item.requiredTechnology
    }))
    total.value = res.data.total
  } catch (error) {
    console.error('获取职位列表失败：', error)
    ElMessage.error('获取职位列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleQuery = () => {
  currentPage.value = 1
  getJobList()
}

// 重置
const resetQuery = () => {
  searchInput.value = ''
  currentPage.value = 1
  getJobList()
}

// 分页
const handleSizeChange = (val) => {
  pageSize.value = val
  getJobList()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  getJobList()
}

// 查看详情
const handleDetail = (row) => {
  currentJob.value = row
  dialogVisible.value = true
}

// 通过审核
const handleApprove = async (row) => {
  try {
    await ElMessageBox.confirm('确认通过该职位审核？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    
    // 调用实际的API
    await updateJobReview(row.id, 2)
    
    // 更新本地数据
    const index = jobList.value.findIndex(item => item.id === row.id)
    if (index !== -1) {
      jobList.value[index].status = 2
    }
    
    ElMessage.success('审核通过成功')
    getJobList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('审核失败：', error)
      ElMessage.error('审核失败')
    }
  }
}

// 拒绝审核
const handleReject = async (row) => {
  try {
    await ElMessageBox.confirm('确认拒绝该职位审核？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    
    // 调用实际的API
    await updateJobReview(row.id, 4)
    
    // 更新本地数据
    const index = jobList.value.findIndex(item => item.id === row.id)
    if (index !== -1) {
      jobList.value[index].status = 4
    }
    
    ElMessage.success('已拒绝该职位')
    getJobList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('操作失败：', error)
      ElMessage.error('操作失败')
    }
  }
}

// 页面加载时获取数据
onMounted(() => {
  getJobList()
})
</script>









<style scoped>
.job-audit {
  padding: 20px;
}

.search-box {
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
}

.search-input {
  width: 200px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.job-detail {
  padding: 20px;
}

.detail-item {
  margin-bottom: 15px;
  line-height: 1.5;
}

.detail-item .label {
  font-weight: bold;
  margin-right: 10px;
  color: #606266;
}
</style>