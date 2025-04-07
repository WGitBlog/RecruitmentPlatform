<template>
  <div class="report-container">
    <div class="search-section">
      <div class="title">
        <h2>举报管理</h2>
      </div>
      <!-- <div class="search-input">
        <el-input
          v-model="searchText"
          placeholder="请输入职位名称"
          class="search-input"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="handleReset">重置</el-button>
      </div>
      <div class="filter-section">
        <el-select v-model="filterStatus" placeholder="处理状态"  clearable>
          <el-option label="全部" value="" />
          <el-option label="待处理" value="0" />
          <el-option label="已处理" value="1" />
          <el-option label="已驳回" value="2" />
        </el-select>
      </div> -->
    </div>

    <el-table
      :data="reportList"
      style="width: 100%"
      border
      v-loading="loading"
      class="report-table"
    >
      <el-table-column prop="jobId" label="职位ID" min-width="80" />
      <el-table-column prop="jobTitle" label="职位名称" min-width="150" />
      <el-table-column prop="boosName" label="发布者" min-width="120" />
      <el-table-column prop="companyName" label="公司名称" min-width="150" />
      <el-table-column prop="reason" label="举报原因" min-width="150" />
      <el-table-column label="举报时间" min-width="160">
        <template #default="scope">
          {{ formatDate(scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column label="状态" min-width="100">
        <template #default="scope">
          <el-tag :type="getStatusType(scope.row.status)">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" min-width="150">
        <template #default="scope">
          <el-button
            type="primary"
            link
            @click="handleView(scope.row)"
          >
            查看
          </el-button>
          <el-button
            v-if="scope.row.status === 0"
            type="success"
            link
            @click="handleProcess(scope.row)"
          >
            处理
          </el-button>
          <el-button
            v-if="scope.row.status === 0"
            type="danger"
            link
            @click="handleReject(scope.row)"
          >
            驳回
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 30, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 查看详情弹窗 -->
    <el-dialog
      v-model="viewDialogVisible"
      title="举报详情"
      width="500px"
    >
      <div class="report-detail" v-if="currentReport">
        <div class="detail-item">
          <span class="label">职位编号：</span>
          <span>{{ currentReport.jobId }}</span>
        </div>
        <div class="detail-item">
          <span class="label">职位名称：</span>
          <span>{{ currentReport.jobTitle }}</span>
        </div>
        <div class="detail-item">
          <span class="label">发布者：</span>
          <span>{{ currentReport.boosName }}</span>
        </div>
        <div class="detail-item">
          <span class="label">公司名称：</span>
          <span>{{ currentReport.companyName }}</span>
        </div>
        <div class="detail-item">
          <span class="label">举报原因：</span>
          <span>{{ currentReport.reason }}</span>
        </div>
        <div class="detail-item">
          <span class="label">详细说明：</span>
          <p class="details-text">{{ currentReport.reportDetails }}</p>
        </div>
        <div class="detail-item" v-if="currentReport.feedback">
          <span class="label">处理结果：</span>
          <p class="feedback-text">{{ currentReport.feedback }}</p>
        </div>
      </div>
    </el-dialog>

    <!-- 处理举报弹窗 -->
    <el-dialog
      v-model="processDialogVisible"
      title="处理举报"
      width="500px"
    >
      <el-form
        ref="processFormRef"
        :model="processForm"
        :rules="processRules"
        label-width="80px"
      >
        <el-form-item label="处理结果" prop="feedback">
          <el-input
            v-model="processForm.feedback"
            type="textarea"
            :rows="4"
            placeholder="请输入处理结果说明"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="processDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitProcess">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import { formatDate } from '@/utils/date'
import {getReportPage,updateReport} from '@/api/report'
import 'element-plus/theme-chalk/el-message.css'
import 'element-plus/theme-chalk/el-message-box.css'
const reportList = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(8)
const total = ref(0)
const filterStatus = ref(0)
const searchText = ref('')

// 弹窗控制
const viewDialogVisible = ref(false)
const processDialogVisible = ref(false)
const currentReport = ref(null)

// 处理表单
const processFormRef = ref(null)
const processForm = ref({
  feedback: ''
})

const processRules = {
  feedback: [
    { required: true, message: '请输入处理结果', trigger: 'blur' },
    { min: 5, message: '处理结果至少5个字', trigger: 'blur' }
  ]
}

// 获取状态类型
const getStatusType = (status) => {
  const statusMap = {
    0: 'info',    // 待处理
    1: 'success', // 已处理
    2: 'danger'   // 已驳回
  }
  return statusMap[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const statusMap = {
    0: '待处理',
    1: '已处理',
    2: '已驳回'
  }
  return statusMap[status] || '未知状态'
}

// 查看详情
const handleView = (row) => {
  currentReport.value = row
  viewDialogVisible.value = true
}

// 处理举报
const handleProcess = (row) => {
  currentReport.value = row
  processForm.value.feedback = ''
  processDialogVisible.value = true
}

// 驳回举报
const handleReject = async (row) => {
  currentReport.value = row

  try {
    await ElMessageBox.confirm('确定要驳回该举报吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })





    //驳回请求api
   await  updateReport(currentReport.value.id,2)
    fetchReportList()
    // TODO: 调用驳回API
    ElMessage.success('驳回成功')

  } catch (error) {
    console.error('驳回失败:', error)
  }
}

// 提交处理结果
const submitProcess = async () => {
  if (!processFormRef.value) return
  
  await processFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // TODO: 处理举报Api

        updateReport(currentReport.value.id,1,processForm.value.feedback)
        ElMessage.success('处理成功')
        processDialogVisible.value = false
        fetchReportList()
      } catch (error) {
        console.error('处理失败:', error)
        ElMessage.error('处理失败，请重试')
      }
    }
  })
}

// 分页处理
const handleSizeChange = (val) => {
  pageSize.value = val
  fetchReportList()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchReportList()
}

// 获取举报列表
const fetchReportList = async () => {
  loading.value = true
  try {
    // TODO: 调用获取列表API
    const res = await getReportPage(
      currentPage.value,
      pageSize.value
    )
    reportList.value = res.data.records
    total.value = res.data.total


  } catch (error) {
    console.error('获取列表失败:', error)
    ElMessage.error('获取列表失败')
  } finally {
    loading.value = false
  }
}

// 监听状态筛选变化
watch(filterStatus, () => {
  currentPage.value = 1
  fetchReportList()
})

// 搜索处理
const handleSearch = () => {
  currentPage.value = 1
  fetchReportList()
}

// 重置搜索
const handleReset = () => {
  searchText.value = ''
  filterStatus.value = ''
  currentPage.value = 1
  fetchReportList()
}

onMounted(() => {
  fetchReportList()
})
</script>

<style lang="scss" scoped>
.report-container {
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  min-height: calc(100vh - 40px);

  .search-section {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    flex-wrap: wrap;
    margin-bottom: 20px;
    gap: 20px;

    .title {
      h2 {
        font-size: 18px;
        font-weight: bold;
        color: #000;
        margin: 0;
      }
    }

    .search-input {
      display: flex;
      gap: 10px;
      
      .el-input {
        width: 300px;
      }
    }

    .filter-section {
      display: flex;
      gap: 15px;
      
      .el-select {
        width: 150px;
      }
    }
  }

  .report-table {
    margin-top: 20px;
    border-radius: 4px;
    
    :deep(.el-table__inner-wrapper) {
      border-right: none;
      
      &::before {
        display: none;
      }
    }

    :deep(.el-table__header) {
      th.el-table__cell {
        border-right: none;
      }
    }

    :deep(.el-table__body) {
      td.el-table__cell {
        border-right: none;
      }
    }

    :deep(.el-table__cell) {
      text-align: center;
    }
  }

  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}

.report-detail {
  .detail-item {
    margin-bottom: 15px;

    .label {
      font-weight: bold;
      color: #333;
      display: inline-block;
      width: 80px;
    }

    .details-text {
      margin: 10px 0;
      padding: 12px;
      background-color: #f8f8f8;
      border-radius: 4px;
      line-height: 1.6;
    }

    .feedback-text {
      margin: 10px 0;
      padding: 8px;
      background-color: #f0f9eb;
      border-radius: 4px;
      color: #67c23a;
    }
  }
}

.dialog-footer {
  .el-button {
    margin-left: 12px;
  }
}
</style>
