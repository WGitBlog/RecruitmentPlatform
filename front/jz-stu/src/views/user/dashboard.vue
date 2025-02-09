<template>
  <div class="dashboard-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>总用户数</span>
            </div>
          </template>
          <div class="card-body">
            <div class="number">{{ statistics.userCount }}</div>
            <div class="compare">
              较昨日
              <span :class="statistics.userIncrease >= 0 ? 'up' : 'down'">
                {{ Math.abs(statistics.userIncrease) }}%
              </span>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>企业用户数</span>
            </div>
          </template>
          <div class="card-body">
            <div class="number">{{ statistics.bossCount }}</div>
            <div class="compare">
              较昨日
              <span :class="statistics.bossIncrease >= 0 ? 'up' : 'down'">
                {{ Math.abs(statistics.bossIncrease) }}%
              </span>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>待审核职位</span>
            </div>
          </template>
          <div class="card-body">
            <div class="number">{{ statistics.pendingJobs }}</div>
          </div>
          <div class="compare">
            较昨日
            <span :class="statistics.bossIncrease >= 0 ? 'up' : 'down'">
              {{ Math.abs(statistics.bossIncrease) }}%
            </span>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>今日举报</span>
            </div>
          </template>
          <div class="card-body">
            <div class="number">{{ statistics.todayReports }}</div>
          </div>
          <div class="compare">
            较昨日
            <span :class="statistics.bossIncrease >= 0 ? 'up' : 'down'">
              {{ Math.abs(statistics.bossIncrease) }}%
            </span>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>用户增长趋势</span>
            </div>
          </template>
          <!-- 这里放置用户增长趋势图表 -->
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>职位分类统计</span>
            </div>
          </template>
          <!-- 这里放置职位分类饼图 -->
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getStatistics } from '@/api/admin'

const statistics = ref({
  userCount: 0, //总用户数
  userIncrease: 0, //用户增长比率
  bossCount: 0, //boss总数
  bossIncrease: 0, //boss增长比率
  pendingJobs: 0, //待审恶核职位数
  // jobIncrease: 0, //职位增长比率
  todayReports: 0 //待处理举报数
  // reportsIncrease: 0 //举报增长比率
})

onMounted(async () => {
  try {
    const res = await getStatistics()
    if (res.code === 1) {
      console.log('11111')
      statistics.value = res.data
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
})

// const getStatistics = async () => {
//   // 调用API获取统计数据
//   statistics.value = {
//     userCount: 1234,
//     userIncrease: 5.2,
//     bossCount: 456,
//     bossIncrease: -2.1,
//     pendingJobs: 23,
//     todayReports: 7
//   }
// }
</script>

<style scoped lang="scss">
.dashboard-container {
  .el-card {
    margin-bottom: 20px;
  }

  .card-header {
    font-weight: bold;
  }

  .card-body {
    .number {
      font-size: 24px;
      font-weight: bold;
      margin-bottom: 10px;
    }

    .compare {
      font-size: 14px;
      color: #909399;

      .up {
        color: #67c23a;
      }

      .down {
        color: #f56c6c;
      }
    }
  }

  .chart-row {
    margin-top: 20px;
  }
}
</style>
