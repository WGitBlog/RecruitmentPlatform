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
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>用户增长趋势</span>
            </div>
          </template>
          <div class="first_main"></div>
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
          <div class="second_main"></div>
          <!-- 这里放置职位分类饼图 -->
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getStatistics } from '@/api/admin'
import * as echarts from 'echarts'
const statistics = ref({
  userCount: 0, //总用户数
  userIncrease: 0, //用户增长比率
  bossCount: 0, //boss总数
  bossIncrease: 0, //boss增长比率
  pendingJobs: 0, //待审恶核职位数
  // jobIncrease: 0, //职位增长比率
  todayReports: 0, //待处理举报数
  // reportsIncrease: 0 //举报增长比率
  dateList: [], // 用于存储日期数据
  userGrowthData: [], // 用于存储用户增长数据
  categoryList: [], // 用于存储职位分类名称
  jobCategoryData: [] // 用于存储职位分类数据
})

onMounted(async () => {
  try {
    const res = await getStatistics()
    if (res.code === 1) {
      statistics.value = res.data
      console.log(statistics.value)
      console.log(statistics.value.CategoryList)
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }

  // 用户增长趋势图
  var chartDom1 = document.querySelector('.first_main')
  var myChart1 = echarts.init(chartDom1)
  var option1

  option1 = {
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: statistics.value.dateList, // 日期数据
      axisLabel: {
        fontSize: 8 // 设置横坐标字体大小
      }
    },
    yAxis: {
      type: 'value',
      name: '新增用户数'
    },
    series: [
      {
        name: '新增用户',
        data: statistics.value.userGrowthData, // 使用获取的数据
        type: 'line',
        smooth: true
      }
    ]
  }

  myChart1.setOption(option1)
  // 职位分类统计图
  var chartDom2 = document.querySelector('.second_main')
  var myChart2 = echarts.init(chartDom2)
  var option2

  option2 = {
    tooltip: {
      trigger: 'item'
    },
    xAxis: {
      type: 'category',
      data: statistics.value.categoryList, // 职位分类
      axisLabel: {
        fontSize: 7 // 设置横坐标字体大小
      }
    },
    yAxis: {
      type: 'value',
      name: '职位数量'
    },
    series: [
      {
        name: '职位数量',
        type: 'bar',
        data: statistics.value.jobCategoryData, // 使用获取的数据
        emphasis: {
          focus: 'series'
        }
      }
    ]
  }
  myChart2.setOption(option2)
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
  .first_main {
    height: 337px;
    width: 100%;
  }
  .second_main {
    height: 337px;
    width: 100%;
  }
}
</style>
