<script setup lang='ts' >
import { useUserStore } from '@/stores/user'
import { useJobStore } from '@/stores/job'
import { getJobInfo } from '@/api/job.js'
import { computed, onMounted, ref, reactive } from 'vue'
import useWebSocket from '@/utils/websocket.js'
import { getCandidateInfo, getCandidatePage } from '@/api/candidate.js'
import { getBoosInfo } from '@/api/boos.js'
import { updateInterested } from '@/api/applicant.js'
import router from '@/router'
import 'element-plus/theme-chalk/el-message.css'
import 'element-plus/theme-chalk/el-message-box.css'
import { h } from 'vue'
import { ElMessageBox ,FormInstance} from 'element-plus'
import { watch } from 'vue'
import type { CSSProperties } from 'vue'
import { useCandidateStore } from '@/stores/candidate.js'
import { resetPassword, updatePassword } from '@/api/user.js'
import { updateApplicantCdsCommunication } from '@/api/applicantCds.js'
const candidateStore = useCandidateStore()
const candidateId = sessionStorage.getItem('candidateId')
const boosId = sessionStorage.getItem('boosId')

const filters = ref([
  {
    label: '学历要求',
    options: ['不限', '专科', '本科', '研究生', '硕士', '博士', '自定义'],
    selected: '不限'
  },
  {
    label: '年龄要求',
    options: ['不限', '20-25', '25-30', '30-35', '35-40', '40-50', '50以上', '自定义'],
    selected: '不限'
  },
  {
    label: '经验要求',
    options: ['不限', '无经验', '在校应届生', '1-3年', '3-5年', '5-10年', '10年以上', '自定义'],
    selected: '不限'
  },
  {
    label: '薪资要求',
    options: ['不限', '3k-5k', '5k-10k', '10k-15k', '15k-20k', '20k以上', '自定义'],
    selected: '不限'
  }
])

const selectOption = async (index: number, option: string) => {
  filters.value[index].selected = option
  loading.value = false
  hasMore.value = true

  let processedOption = option
  if (index === 2 && option.includes('年')) {
    processedOption = option.replace('年', '')
  }
  switch (index) {
    case 0:
      if (option == '不限') {
        candidateStore.candidateInfo.degreecategory = null
      } else {
        candidateStore.candidateInfo.degreecategory = option
      }
      items.value = []
      candidateStore.page = 1
      //发出分页请求
      await getCandidatePage(
        candidateStore.page,
        candidateStore.pageSize,
        candidateStore.candidateInfo.degreecategory,
        candidateStore.candidateInfo.age,
        candidateStore.candidateInfo.workyears,
        candidateStore.candidateInfo.desiredSalary
      ).then((res) => {
        candidateStore.page++
        items.value.push(...res.data.records)
      })
      console.log('学历要求:', option)
      console.log('candidateInfo:', candidateStore.candidateInfo)
      break
    case 1:
      if (option == '不限') {
        candidateStore.candidateInfo.age = null
      } else {
        candidateStore.candidateInfo.age = option
      }
      items.value = []
      candidateStore.page = 1
      //发出分页请求
      await getCandidatePage(
        candidateStore.page,
        candidateStore.pageSize,
        candidateStore.candidateInfo.degreecategory,
        candidateStore.candidateInfo.age,
        candidateStore.candidateInfo.workyears,
        candidateStore.candidateInfo.desiredSalary
      ).then((res) => {
        candidateStore.page++
        items.value.push(...res.data.records)
      })
      console.log('年龄要求:', option)
      console.log('candidateInfo:', candidateStore.candidateInfo)
      break
    case 2:
      if (option == '不限') {
        candidateStore.candidateInfo.workyears = null
      } else {
        candidateStore.candidateInfo.workyears = processedOption
      }
      items.value = []
      candidateStore.page = 1
      //发出分页请求
      await getCandidatePage(
        candidateStore.page,
        candidateStore.pageSize,
        candidateStore.candidateInfo.degreecategory,
        candidateStore.candidateInfo.age,
        candidateStore.candidateInfo.workyears,
        candidateStore.candidateInfo.desiredSalary
      ).then((res) => {
        candidateStore.page++
        items.value.push(...res.data.records)
      })
      console.log('经验要求:', option)
      console.log('candidateInfo:', candidateStore.candidateInfo)
      break
    case 3:
      if (option == '不限') {
        candidateStore.candidateInfo.desiredSalary = null
      } else {
        candidateStore.candidateInfo.desiredSalary = option
      }
      items.value = []
      candidateStore.page = 1
      //发出分页请求
      await getCandidatePage(
        candidateStore.page,
        candidateStore.pageSize,
        candidateStore.candidateInfo.degreecategory,
        candidateStore.candidateInfo.age,
        candidateStore.candidateInfo.workyears,
        candidateStore.candidateInfo.desiredSalary
      ).then((res) => {
        candidateStore.page++
        items.value.push(...res.data.records)
      })
      console.log('薪资要求:', option)
      console.log('candidateInfo:', candidateStore.candidateInfo)
      break

    default:
      console.log('未知选项')
  }
}

const a = ref([0, 1])

interface Mark {
  style: CSSProperties
  label: string
}

type Marks = Record<number, Mark | string>

const value = ref([0, 0])
const marks = reactive<Marks>({
  0: '',
  25: '',
  50: '',
  100: {
    style: {
      color: '#1989FA'
    },
    label: ''
  }
})
const items = ref([]) //数据列表
//boos的信息
const defaultUser = ref({})
onMounted(async () => {
  candidateStore.page = 1
  //发出分页请求
  await getCandidatePage(
    candidateStore.page,
    candidateStore.pageSize,
    candidateStore.candidateInfo.degreecategory,
    candidateStore.candidateInfo.age,
    candidateStore.candidateInfo.workyears,
    candidateStore.candidateInfo.desiredSalary
  ).then((res) => {
    candidateStore.page++
    items.value.push(...res.data.records)
  })
  await getBoosInfo(boosId).then((res) => {
    defaultUser.value = res.data
    sessionStorage.setItem('companyId', res.data.companyId)
  })
})
const loading = ref(false) //判断是否正在加载
const hasMore = ref(true) //判断是否还有数据没有数据就无需加载
const initialLoad = ref(true) //为了防止上来就发请求

const load = async () => {
  if (initialLoad.value) {
    // 判断 true 则更改成 false
    initialLoad.value = false
    // 并空返回
    return
  }
  if (loading.value || !hasMore.value) return
  loading.value = true // 设置加载状态为 true
  try {
    const { data } = await getCandidatePage(
      candidateStore.page,
      candidateStore.pageSize,
      candidateStore.candidateInfo.degreecategory,
      candidateStore.candidateInfo.age,
      candidateStore.candidateInfo.workyears,
      candidateStore.candidateInfo.desiredSalary
    )

    items.value.push(...data.records)
    console.log(items.value)
    candidateStore.page++
    // 检查是否还有更多数据
    if (data.records.length < candidateStore.pageSize) {
      hasMore.value = false // 设置 hasMore 为 false
    }
  } catch (error) {
    console.error('加载数据失败:', error)
  } finally {
    loading.value = false // 重置加载状态
  }
}

const commouncationTa = async (item) => {
  //将该item加入到boos的applicant的关系表里的沟通数组
  await updateApplicantCdsCommunication(item.id, boosId)
  //代参跳转到消息页面
  router.push(`/dialogue?candidateInfo=${encodeURIComponent(JSON.stringify(item))}`)
}
//短暂的去除不想看的工作
const empClose= async (item)=>{
  items.value=items.value.filter((a)=>{
    return a.id!=item.id
  })
}



// 修改密码弹窗显示控制
const dialogVisible = ref(false)
// 修改密码表单验证规则
// 表单数据
const resetform = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})
// 新增表单 ref 引用
const formRef = ref<FormInstance>() // 需要导入 FormInstance 类型
const rules = {
  oldPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少6位', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少6位', trigger: 'blur' }
  ]
}

const handleCommand = (command) => {
  switch (command) {
    case 'personalCenter':
      // 跳转到个人中心页面
      router.push('/userBInfo')
      break
    case 'changePassword':
      // 修改密码api
      dialogVisible.value = true
      break
    case 'logout':
      // 处理退出登录逻辑
      router.push('/login')
      console.log(' 退出登录')
      break
    default:
      break
  }
}

// 提交修改密码表单
const submitForm = async () => {
  // 验证表单
  const isValid = await formRef.value.validate()
  if (!isValid) {
    return
  }
  // 这里添加密码修改API调用
  await updatePassword({
    id: sessionStorage.getItem('userId'),
    oldPassword: resetform.oldPassword,
    password: resetform.newPassword,
  })
    .then((res) => {
      if (res.code !== 1) {
        ElMessage.error(res.msg)
        return
      } else {
        ElMessage.success('修改密码成功')
        dialogVisible.value = false
      }
    })
    .finally(() => {
      // 重置表单数据
      resetform.oldPassword = ''
      resetform.newPassword = ''
      resetform.confirmPassword = ''
      // 重置表单验证状态
      formRef.value.resetFields()
    })
  dialogVisible.value = false
}
// 重置表单
const cancelForm = () => {
  formRef.value.resetFields()
  resetform.oldPassword = ''
  resetform.newPassword = ''
  resetform.confirmPassword = ''
  dialogVisible.value = false
}

</script>

<template>
  <el-container>
    <el-header>
      <el-row type="flex" justify="center" class="header_flex-row">
        <el-col :span="12" class="header_row_col1">
          <ul>
            <li>
              <el-link :underline="false" href="#" target="_blank"><h1>全国招聘平台</h1></el-link>
            </li>
            <li><el-link :underline="false">首页</el-link></li>
            <li><el-link :underline="false">推荐</el-link></li>
            <li><el-link :underline="false">搜索</el-link></li>
            <li><el-link :underline="false">校园</el-link></li>
            <li><el-link :underline="false">App</el-link></li>
            <li><el-link :underline="false">资讯</el-link></li>
            <li><el-link :underline="false">有了</el-link></li>
            <li><el-link :underline="false">海外</el-link></li>
          </ul>
        </el-col>
        <el-col :span="9" class="header_row_col2">
          <ul>
            <li><el-link :underline="false" @click="$router.push('/dialogue')">消息</el-link></li>
            <li><el-link :underline="false" @click="$router.push('/interviews')">面试</el-link></li>
            <!-- <li>
              <el-link :underline="false" @click="$router.push('/userBInfo')">
                <img
                  src="../../assets/a.jpg"
                  alt=""
                  :style="{ borderRadius: '50%', objectFit: 'cover' }"
                />
              </el-link>
            </li> -->


            <li style="lineHeight: 25px">
              <!-- 使用 el-dropdown 组件 -->
              <el-dropdown trigger="hover" @command="handleCommand">
                <!-- 下拉菜单的触发元素 -->
                <el-link :underline="false">
                  <img
                    src="../../assets/a.jpg"
                    alt=""
                    :style="{ borderRadius: '50%', objectFit: 'cover' }"
                  />
                  <span v-html="defaultUser.boosName"></span>
                </el-link>
                <!-- 下拉菜单内容 -->
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="personalCenter">个人中心</el-dropdown-item>
                    <el-dropdown-item command="changePassword">修改密码</el-dropdown-item>
                    <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </li>
          </ul>
        </el-col>
      </el-row>
       <!-- 密码修改弹窗 -->
      <el-dialog v-model="dialogVisible" title="修改密码" width="30%">
        <el-form :model="resetform" label-width="79px" :rules="rules" ref="formRef">
          <el-form-item label="旧密码" prop="oldPassword">
            <el-input v-model="resetform.oldPassword" type="password" show-password />
          </el-form-item>
          <el-form-item label="新密码" prop="newPassword">
            <el-input v-model="resetform.newPassword" type="password" show-password />
          </el-form-item>
          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input v-model="resetform.confirmPassword" type="password" show-password />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="cancelForm" style="width: 140px">取消</el-button>
            <el-button type="primary" @click="submitForm" style="width: 140px">提交</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
    </el-header>

    <el-container>
      <el-main>
        <div class="filtrate">
          <el-row class="slider_module" v-for="(item, index) in filters" :key="index">
            <el-col :span="2">
              <span class="label">{{ item.label }}</span>
            </el-col>
            <el-col :span="22" class="select_box">
              <ul>
                <li
                  v-for="(option, idx) in item.options"
                  :key="idx"
                  :class="{ active: item.selected === option }"
                  @click="selectOption(index, option)"
                >
                  {{ option }}
                </li>
              </ul>
            </el-col>
          </el-row>
        </div>

        <div class="candidate_detail">
          <ul v-infinite-scroll="load" :infinite-scroll-distance="2" style="overflow: auto">
            <li v-for="item in items" :key="item.id">
              <div class="all_info">
                <span :style="{ padding: '0 20px' }">
                  <img
                    src="../../assets/a.jpg"
                    alt=""
                    :style="{ borderRadius: '50%', objectFit: 'cover', height: '80px' }"
                  />
                </span>

                <div class="emp_info">
                  <div class="emp_top">
                    <div>
                      <span :style="{ padding: '5px 10px', fontSize: '25px' }">{{
                        item.name
                      }}</span>
                      <span :style="{ padding: '5px 10px' }">热搜</span>
                      <span :style="{ padding: '5px 10px' }">刚刚活跃</span>
                    </div>
                    <div :style="{ height: '10px', lineHeight: '10px' }">
                      <span :style="{ padding: '0 10px' }">{{ item.age }}岁</span>|
                      <span :style="{ padding: '0 10px' }">{{ item.workyears }}年</span>|
                      <span :style="{ padding: '0 10px' }">{{ item.degreecategory }}</span
                      >|
                      <span :style="{ padding: '0 10px' }">{{ item.desiredSalary }}</span>
                    </div>

                    <div class="emp_detail">
                      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{{ item.skillsIntroduction }}
                    </div>
                    <div class="top_right">
                      <div :style="{ height: '25px', marginBottom: '4px' }">
                        <span :style="{ padding: '0 10px' }"
                          >期望城市&nbsp;:&nbsp;&nbsp;{{ item.desiredCity }}</span
                        >
                      </div>
                      <div :style="{ height: '25px', marginBottom: '4px' }">
                        <span :style="{ padding: '0 10px' }"
                          >期望行业&nbsp;:&nbsp;&nbsp;{{ item.desiredIndustry }}</span
                        >
                      </div>
                      <div :style="{ height: '25px' }">
                        <span :style="{ padding: '0 10px' }"
                          >期望职位&nbsp;:&nbsp;&nbsp;{{ item.desiredPosition }}</span
                        >
                      </div>
                    </div>

                    <div class="emp_commouncation">
                      <el-button type="primary" @click="commouncationTa(item)">联系TA</el-button>
                    </div>
                  </div>
                  <el-divider class="custom-divider" />
                  <div class="emp_bom">
                    <div class="iconfont icon-cuowuguanbiquxiao emp_close" @click="empClose(item)"></div>
                  </div>
                </div>
              </div>
            </li>
          </ul>
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>



<style lang="scss" scoped>
.el-container {
  height: 97vh;
}
.custom-divider {
  margin: 0; /* 更改上下的间距 */
}

.candidate_detail {
  width: 1080px;
  background-color: 255, 255, 255;
  margin: 0 auto;

  box-sizing: border-box;
  border-radius: 7px;
  padding: 5px 6px 5px 10px;
  ul {
    margin: 0;
    padding: 0;
    overflow-y: auto;
    height: 450px;
    li {
      display: block;
      background-color: white;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
      border: 1px solid #ebeef5;
      border-radius: 8px;
      height: 150px;
      margin-bottom: 4px;
      .all_info {
        display: flex;
        width: 100%;
        height: 100%;

        .emp_info {
          width: 88%;

          .emp_top {
            position: relative;
            text-align: left;

            height: 80%;
            line-height: 40px;
            .emp_commouncation {
              position: absolute;

              width: 80px;
              top: 0px;
              right: 10px;
            }

            .emp_detail {
              width: 400px;
              height: 56px;
              line-height: 20px;

              margin: 7px 0 0 10px;
              font-size: 12px;
              white-space: normal; /* Allow text to wrap */
              overflow: hidden; /* Hide overflow if necessary */
              word-wrap: break-word; /* Break long words if necessary */
            }

            .top_right {
              position: absolute;

              height: 80px;
              line-height: 20px;
              width: 300px;
              top: 40px;
              left: 500px;
            }
          }
          .emp_bom {
            position: relative;
            height: 20%;
            line-height: 20px;

            .emp_close {
              position: absolute;
              cursor: pointer;
              user-select: none;
              outline: none;
              right: 5px;
              top: 5px;
              width: 20px;
              height: 20px;
            }
          }
        }
      }
    }

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
  }
}

.filtrate {
  width: 1000px;
  margin: 0 auto;
  background: #fff;

  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border: 1px solid #ebeef5;
  border-radius: 8px;
  padding: 15px 30px;

  .slider_module {
    height: 25px;
    line-height: 25px;
    margin-bottom: 10px;

    &:last-child {
      margin-bottom: 0;
    }

    .label {
      display: flex;
      font-size: 14px;
      color: #606266;
      font-weight: 500;
      width: 120px;
    }

    .select_box {
      display: flex;
      font-size: 14px;
      align-items: center;
      flex-wrap: nowrap;

      ul {
        margin: 0;
        padding: 0;
        display: flex;
        align-items: center;
        flex-wrap: nowrap;

        li {
          display: inline-block;
          line-height: 32px;
          margin-right: 20px;
          width: auto;
          cursor: pointer;
          padding: 0 15px;
          border-radius: 16px;
          transition: all 0.3s;
          color: #606266;

          &:hover {
            color: #409eff;
            background: rgba(64, 158, 255, 0.1);
          }

          &.active {
            color: #409eff;
            background: rgba(64, 158, 255, 0.1);
          }

          &.custom {
            margin-right: 0;
            color: #909399;
          }
        }
      }
    }
  }
}

.header_imgInfo {
  display: inline-block;
  width: 30px;
  background-color: aqua;
}

.header_img_span {
  height: 28px;
}
.header_row_col2 ul {
  padding: 0;
  margin: 0;
  white-space: nowrap; /* 防止列表项换行 */
  overflow: hidden; /* 隐藏超出部分 */
  li {
    display: inline-block;
    margin-left: 20px;
    margin-bottom: 10px;
    line-height: 40px; //解决高度不对
    height: 40px;
  }
  img {
    height: 28px;
    width: 28px;
  }
}

.header_row_col1 ul {
  padding: 0;
  margin: 0;
  white-space: nowrap; /* 防止列表项换行 */
  overflow: hidden; /* 隐藏超出部分 */
}
.header_row_col1 ul li {
  display: inline-block;
  margin-left: 20px;
  line-height: 5px;
}
.el-header,
.el-footer {
  color: #333;
  text-align: center;
  line-height: 60px;
  background: linear-gradient(90deg, rgba(238, 225, 255, 0.22) 0, rgba(164, 211, 255, 0.22) 100%)
    top left/100% 100% no-repeat;
}

.el-aside {
  background-color: #d3dce6;
  color: #333;
  text-align: center;
  line-height: 200px;
}

.el-main {
  margin: 0;
  padding: 0;
  background: linear-gradient(90deg, rgba(238, 225, 255, 0.22) 0, rgba(164, 211, 255, 0.22) 100%)
    top left/100% 100% no-repeat;
  color: #333;
  text-align: center;
  line-height: 160px;
}

body > .el-container {
  margin-bottom: 40px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}
</style>