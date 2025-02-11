<script setup>
import { onMounted, ref } from 'vue'
import { useBoosStore } from '@/stores/boos.js'
import { getDeliveriesJobs } from '@/api/candidate.js'
import logoImage from '@/assets/a.jpg'
import {getReportList} from '@/api/report'
import { formatDate } from '@/utils/date'
const boosStore = useBoosStore()
const DeliveriesJobs = ref([])
const Deliveries = ref(true)

const Interest = ref([]); // 处理显隐继续沟通
const handleMouseOver = (index) => {
  Interest.value[index] = true; // 设置当前索引为 true，表示悬停
};
const handleMouseleave = (index) => {
  Interest.value[index] = false; // 设置当前索引为 false，表示离开
};

const isEmpty = ref(false) //判断Deliveries中的数据是否为空








// 举报列表数据
const reportList = ref([])
// 获取举报状态对应的类型
const getStatusType = (status) => {
  const statusMap = {
    0: 'info',    // 待处理
    1: 'success', // 已处理
    2: 'danger'   // 已驳回
  }
  return statusMap[status] || 'info'
}

// 获取举报状态对应的文本
const getStatusText = (status) => {
  const statusMap = {
    0: '待处理',
    1: '已处理',
    2: '已驳回'
  }
  return statusMap[status] || '未知状态'
}

// 控制详情弹窗的显示
const dialogVisible = ref(false)
// 当前选中的举报详情
const currentReport = ref(null)

// 查看详情的处理函数
const handleViewDetails = (report) => {
  currentReport.value = report
  dialogVisible.value = true
}




onMounted(async () => {
  const candidateId = localStorage.getItem('candidateId')
  const res = await getDeliveriesJobs(candidateId)

  if (res !== null && res.data !== null && res.data.length > 0) {  
  const processedDeliveriesJobs = processedData(res) //处理getDeliveriesJobs请求的json转义问题
  DeliveriesJobs.value.push(...processedDeliveriesJobs) // 将处理后的数据放到数据列表里
    isEmpty.value = true // 如果处理后的数据不为空，则设置为false  
  } else {  
    isEmpty.value = false // 如果res为null或res.data为空数组，设置isEmpty为true  
  }  



 // 获取举报列表
 try {
    const candidateId = localStorage.getItem('candidateId')
    // 这里需要添加一个新的 API 调用来获取举报列表
    const res = await getReportList(candidateId)
    if (res.code === 1) {
      reportList.value = res.data
    }
  } catch (error) {
    console.error('获取举报列表失败:', error)
  }





})

const processedData = (
  res //处理getCommunicatedJobs请求的json转义问题
) =>
  res.data.map((record) => {
    if (record.jobDescription && typeof record.jobDescription === 'string') {
      try {
        record.jobDescription = JSON.parse(record.jobDescription)
      } catch (e) {
        console.error('JSON parsing error:', e)
      }
    }
    if (record.jobDetails && typeof record.jobDetails === 'string') {
      record.jobDetails = record.jobDetails.replace(/\\n/g, '<br>')
    }

    return record
  })


</script>

<template>
  <div class="commounited" v-if="isEmpty">
    <el-row>
      <el-col :span="18" class="col_left">
        <div class="baseJob">
          <ul>
            <li
              v-for="(item, index) in DeliveriesJobs"
              :key="item.id"
              @mouseover="handleMouseOver(index)"
              @mouseleave="handleMouseleave(index)"
            >
              <el-row
                type="flex"
                justify="space-between"
                :style="{
                  height: '40px',
                  lineHeight: '40px',
                  backgroundColor: 'white',
                  boxSizing: ' border-box',
                  borderRadius: '10px 10px 0 0'
                }"
              >
                <el-col :span="6">
                  <div class="top_left">
                    <img :src="logoImage" alt="" />
                    <span>{{ item.boosName }}</span>
                    <span>{{ item.companyPosition }}</span>
                  </div>
                </el-col>
                <el-col :span="3" :style="{ display: 'flex', alignItems: 'center' }">
                  <el-button type="success" v-show="Interest[index]" color="#bbe1fb" @click="$router.push(`/dialogue?item=${encodeURIComponent(JSON.stringify(item))}`)">继续沟通</el-button>
                </el-col>
              </el-row>
              <el-row :style="{ height: '80px', lineHeight: '80px' }">
                <el-col :span="15">
                  <div class="job_base_text">
                    <span
                      ><a href="#">{{ item.jobTitle }}</a></span
                    >
                    <span>[{{ item.workLocation }}]</span>
                  </div>
                  <div class="job_price">
                    <p>{{ item.salaryRange }}</p>
                    <ul class="price_ul">
                      <li v-for="span in item.jobDescription" :key="span.id">{{ span }}</li>
                    </ul>
                  </div>
                </el-col>
                <el-col :span="9">
                  <div class="company_info">
                    <img
                      :src="logoImage"
                      alt=""
                      :style="{
                        height: '70px',
                        objectFit: 'cover',
                        borderRadius: '50%',
                        marginBottom: '5px'
                      }"
                    />

                    <div class="company_text">
                      <div>
                        <a href="#" class="company_name">{{ item.companyName }}</a>
                      </div>
                      <div class="company_tag">
                        <span>{{ item.companyType }}</span>
                        <span>
                          {{ item.companyMarket ? '已上市' : '未上市' }}
                        </span>

                        <span>{{ item.companyScale }}</span>
                      </div>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </li>
          </ul>
        </div>
      </el-col>
      <el-col :span="6" class="col_right">
        <!-- 添加举报反馈区域 -->
        <div class="report-feedback">
          <h3 class="report-title">举报反馈</h3>
          <div v-if="reportList.length > 0" class="report-list">
            <el-card v-for="report in reportList" 
                     :key="report.id" 
                     class="report-item"
                     shadow="hover">
              <div class="report-header">
                <span class="job-title">{{ report.jobTitle }}</span>
                <el-tag :type="getStatusType(report.status)" size="small">
                  {{ getStatusText(report.status) }}
                </el-tag>
                <div class="action-buttons">
                  <el-button 
                    type="primary" 
                    link 
                    @click="handleViewDetails(report)"
                  >
                    查看
                  </el-button>
                </div>
              </div>
              <div class="report-content">
                <p class="company-info">
                  <span>发布者：{{ report.boosName }}</span>
                  <span>公司：{{ report.companyName }}</span>
                </p>
                <p class="report-reason">
                  举报原因：{{ report.reason }}
                </p>
                <p class="report-time">
                  举报时间：{{ formatDate(report.createTime) }}
                </p>
                <p v-if="report.feedback" class="feedback-content">
                  处理结果：{{ report.feedback }}
                </p>
              </div>
            </el-card>
          </div>
          <el-empty v-else description="暂无举报记录" />
        </div>
      </el-col>
    </el-row>
  </div>

  <!-- <div class="no_commounited" v-else>
    <el-row>
      <el-col :span="18" class="col_left">
        <div class="baseJob">
          <el-empty description="暂无投递" class="empty_info">
            <el-button type="success">查看更多职位</el-button>
          </el-empty>
        </div>
      </el-col>
      <el-col :span="6" class="col_right">
        <div></div>
      </el-col>
    </el-row>
  </div> -->


  
  <!-- 添加详情弹窗 -->
  <el-dialog
    v-model="dialogVisible"
    width="500px"
  >
    <template v-if="currentReport">
      <div class="report-detail-content">
        <p class="detail-item">
          <span class="label">职位编号：</span>
          <span>{{ currentReport.jobId }}</span>
        </p>
        <p class="detail-item">
          <span class="label">职位名称：</span>
          <span>{{ currentReport.jobTitle }}</span>
        </p>
        <p class="detail-item">
          <span class="label">发布者：</span>
          <span>{{ currentReport.boosName }}</span>
        </p>
        <p class="detail-item">
          <span class="label">公司名称：</span>
          <span>{{ currentReport.companyName }}</span>
        </p>
        <p class="detail-item">
          <span class="label">举报原因：</span>
          <span>{{ currentReport.reason }}</span>
        </p>
        <p class="detail-item">
          <span class="label">举报时间：</span>
          <span>{{ formatDate(currentReport.createTime) }}</span>
        </p>
        <div class="detail-item details-section">
          <span class="label">详细说明：</span>
          <p class="details-text">{{ currentReport.reportDetails }}</p>
        </div>
        <div v-if="currentReport.feedback" class="detail-item">
          <span class="label">处理结果：</span>
          <p class="feedback-text">{{ currentReport.feedback }}</p>
        </div>
      </div>
    </template>
  </el-dialog>
</template>



<style lang="scss" scoped>
.commounited {
  width: 100%;
  .col_left {
    .baseJob {
      width: 100%;

      border-radius: 10px;
      box-sizing: border-box;

      ul {
        margin: 0;
        padding: 0;
         height: 380px;
        overflow-y: auto; /* 允许垂直方向的滚动条 */  
        &::-webkit-scrollbar {
    width: 8px; /* 设置滚动条的宽度 */
    // height: 10px; /* 设置滚动条的高度（对于水平滚动条） */
  }
  &::-webkit-scrollbar-thumb {
    background-color: #cbcbcb; /* 滚动条的颜色 */
    border-radius: 15px; /* 滚动条的圆角 */
  }

  &::-webkit-scrollbar-thumb:hover {
    background-color: #b3b2b2; /* 鼠标悬停时的颜色 */
  }
        li {
          display: block;
          margin: 5px 0;
          width: 100%;
          height: 120px;
          line-height: 120px;
          border: 2px solid #d0e5fa;
          border-radius: 10px;
          box-sizing: border-box;
            overflow: hidden;
          .job_price {
            display: flex; /* 使用 Flexbox 布局 */
            align-items: center; //和margin 0 auto 差不多---上下对齐
            justify-content: left; /* 中心对齐 */
            height: 35px;

            border-radius: 0 0 0 10px;
            .price_ul {
              display: flex;
              align-items: center;
          
              height: 40px;
              line-height: 40px;
              li {
                margin: 0;
                display: inline-block;
                height: 23px;
                line-height: 20px;
                margin-right: 5px;
                font-size: 10px;
                border: 2px solid #d0e5fa;
                border-radius: 8px;
                box-sizing: border-box;
              }
            }
            p {
              height: 23px;
              line-height: 22px;
              margin: 0 15px;
              color: red;
              font-size: 20px;
            }
          }
          .company_info {
            display: flex;
            align-items: center;
            position: relative;
            width: 100%;
            height: 100%;
            .company_text {
              height: 100%;
              margin-left: 20px;
              div {
                height: 40px;
                line-height: 40px;
              }
              a {
                text-decoration: none;
                color: black;
                &:hover {
                  color: #00a6a7;
                }
              }
              .company_name {
                display: flex;
              }
              .company_tag {
                display: flex;
                height: 40px;
                width: 250px;
                span {
                  margin: 7px 8px 0 0;
                  height: 20px;
                  line-height: 20px;
                  border: 2px #d0e5fa solid;
                  border-radius: 5px;
                }
              }
            }
          }
          .job_base_text {
            display: flex; /* 使用 Flexbox 布局 */
            align-items: center; //和margin 0 auto 差不多---上下对齐
            justify-content: left; /* 中心对齐 */
            height: 40px;
            margin: 0;
            padding: 0;

            span {
              height: 30px;
              line-height: 30px;
              margin-left: 15px;
              a {
                text-decoration: none;
                color: black;
                &:hover {
                  color: #00a6a7;
                }
              }
            }
          }
          .top_left {
            display: flex; /* 使用 Flexbox 布局 */
            align-items: center; //和margin 0 auto 差不多---上下对齐
            justify-content: left; /* 中心对齐 */
            width: 100%;
            height: 100%;
            margin-left: 15px;
            img {
              height: 40px;
              width: 40px;
              border-radius: 50%;
              object-fit: cover;
            }
            span {
              margin-left: 10px;
            }
          }
        }
      }
    }
  }

  .col_right {
    width: 100%;

    border-radius: 10px;
    box-sizing: border-box;
    
    .report-feedback {
      position: relative;
      height: 380px;

      .report-title {
        position: sticky;
        height: 20px;
        line-height: 20px;
        top: 0;
        z-index: 1;
        padding: 15px;
        background-color: white;
          display: block;
        margin: 5px 0 5px 0;
        border: 2px solid #d0e5fa;
        border-radius: 10px;
        padding-bottom: 10px;
        border-bottom: 1px solid #eee;
        font-size: 16px;
        color: #333;
      }

      .report-list {
        padding: 0 15px;
        height: calc(100% - 55px);
        overflow-y: auto;
        margin-top: 10px;

        .report-item {
          margin-bottom: 12px;
          &:last-child {
            margin-bottom: 15px;
          }

          .report-header {
            display: flex;
            justify-content: space-between;
            height: 30px;
            align-items: center;
            margin-bottom: 8px;

            .job-title {
              height: 20px;
              line-height: 20px;
              font-weight: bold;
              color: #333;
            }
          }

          .report-content {
            font-size: 14px;
            color: #666;

            p {
              margin: 6px 0;
              line-height: 1.5;
            }

            .company-info {
              display: flex;
              justify-content: space-between;
            }

            .report-reason {
              color: #f56c6c;
            }

            .report-time {
              color: #999;
              font-size: 12px;
            }

            .feedback-content {
              margin-top: 10px;
              padding: 8px;
              background-color: #f8f8f8;
              border-radius: 4px;
            }
          }
        }
      }

      .el-empty {
        padding: 20px 15px;
      }
    }

    .report-list {
      &::-webkit-scrollbar {
        width: 6px;
      }

      &::-webkit-scrollbar-thumb {
        background-color: #ddd;
        border-radius: 3px;
      }
    }
  }
}
.no_commounited {
  width: 100%;
  .col_left {
    .baseJob {
      width: 100%;
      border: 2px solid black;
      border-radius: 10px;
      box-sizing: border-box;
      .empty_info {
        height: 380px;
        position: relative;
        .el-button {
          height: 36px;
          position: absolute;
          bottom: 40px;
          left: 390px;
        }
      }
    }
  }

  .col_right {
    width: 100%;

    border-radius: 10px;
    box-sizing: border-box;
  }
}



// 添加详情弹窗的样式
.report-detail-content {
  padding: 10px;
  
  .detail-item {
    margin-bottom: 12px;
    line-height: 1.5;
    text-align: left;
    .label {
      font-weight: bold;
      color: #333;
      margin-right: 8px;
      display: inline-block;
      width: 80px;
    }
  }
  
  .details-section {
    .label {
      display: block;
      margin-bottom: 8px;
    }
    
    .details-text {
      color: #666;
      background-color: #f8f8f8;
      padding: 12px;
      border-radius: 4px;
      margin: 8px 0;
      line-height: 1.6;
    }
  }
  
  .feedback-text {
    color: #67c23a;
    background-color: #f0f9eb;
    padding: 8px;
    border-radius: 4px;
    margin: 8px 0;
  }
}



</style>