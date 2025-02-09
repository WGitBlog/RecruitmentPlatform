<template>
  <div class="user-list-container">
    <el-card class="filter-card">
      <el-form :inline="true" :model="filterForm" class="filter-form">
        <el-form-item label="用户名">
          <el-input v-model="filterForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="filterForm.status" placeholder="请选择状态">
            <el-option label="全部" value="" />
            <el-option label="正常" value="1" />
            <el-option label="已禁用" value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleFilter">查询</el-button>
          <el-button @click="resetFilter">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>员工用户列表</span>
        </div>
      </template>

      <el-table :data="tableData" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="tiny" label="头像" width="80">
          <template #default="{ row }">
            <el-avatar :size="40" :src="row.tiny" />
          </template>
        </el-table-column>
        <el-table-column prop="name" label="姓名" width="100"/>
        <el-table-column prop="account" label="手机号" width="150" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="degreecategory" label="学历" width="80"/>
        <el-table-column prop="address" label="地址" width="180"/>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '正常' : '已禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button 
              :type="row.status === 1 ? 'danger' : 'success'" 
              link 
              @click="handleStatusChange(row)"
            >
              {{ row.status === 1 ? '禁用' : '启用' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getcandidateUPage } from '@/api/candidate.js'
import { updateUserStatus } from '@/api/user.js'

// 表格数据
const tableData = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(5)
const total = ref(0)

// 获取数据的方法
const fetchData = async () => {
  loading.value = true
  try {
    const res = await getcandidateUPage(currentPage.value, pageSize.value)
    if (res.code === 1) {
      tableData.value = res.data.records
      total.value = res.data.total
    }
  } catch (error) {
    console.error('获取数据失败:', error)
  } finally {
    loading.value = false
  }
}

// 在组件挂载时获取数据
onMounted(() => {
  fetchData()
})

// 筛选表单数据
const filterForm = reactive({
  username: '',
  status: ''
})

// 处理筛选
const handleFilter = () => {
  currentPage.value = 1
  fetchData()
}

// 重置筛选
const resetFilter = () => {
  filterForm.username = ''
  filterForm.status = ''
  handleFilter()
}

// 处理状态变更
const handleStatusChange = async (row) => {
  row.status = row.status === 1 ? 0 : 1
  // TODO: 调用后端接口更新状态
  await updateUserStatus(row.id).then((res)=>{
    console.log(JSON.stringify(res.data))
  })

}

// 处理分页
const handleSizeChange = (val) => {
  pageSize.value = val
  fetchData()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchData()
}
</script>

<style scoped lang="scss">
.user-list-container {
  .filter-card {
    margin-bottom: 20px;
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .pagination-container {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}
</style> 