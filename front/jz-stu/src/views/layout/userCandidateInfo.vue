<script lang="ts" setup>
import { onMounted } from 'vue'
import { getBoosInfo } from '@/api/boos.js'
import { getCandidateInfo, changeUserdetails, updateResume, deleteResume } from '@/api/candidate.js'
import { reactive, ref } from 'vue'
import { useCandidateStore } from '@/stores/candidate'
import type { ComponentSize, FormInstance, FormRules } from 'element-plus'
import { RuleForm, UserInfo } from '@/interface/index.js'
import { ElMessage } from 'element-plus'
import 'element-plus/theme-chalk/el-message.css'
import 'element-plus/theme-chalk/el-message-box.css'
import { ru } from 'element-plus/es/locale'
const boosId = sessionStorage.getItem('boosId')
const candidateStore = useCandidateStore()
const candidateId = sessionStorage.getItem('candidateId')
//修改信息发送请求
const changeConfirm = async (ruleForm) => {
  //将修改信息页面关闭
  centerDialogVisible.value = false
  //发送修改请求
  const { data } = await changeUserdetails(ruleForm)
  //将修改的数据赋值给userInfo：放着修改后页面是之前的数据
  updateUserInfo()

  console.log(data)
}
const changeWorkStatus = async (command) => {
  ruleForm.workstatus = command
  const { data } = await changeUserdetails(ruleForm)
  updateUserInfo()
  console.log(command)
}

const userInfo = ref<UserInfo>({})

// 简历相关信息
const resumeFiles = ref([])
onMounted(async () => {
  const boosId = sessionStorage.getItem('boosId')
  const candidateId = sessionStorage.getItem('candidateId')
  if (boosId) {
    const { data } = await getBoosInfo(boosId)
    data.birthday = dealDateFormater(data.birthday)
    userInfo.value = data

    console.log(data)
  } else {
    const { data } = await getCandidateInfo(candidateId)
    data.birthday = dealDateFormater(data.birthday)
    userInfo.value = data

    // 检查用户信息中是否有简历
    if (userInfo.value && userInfo.value.resume) {
      // 如果有简历，创建文件对象--有简历则不需要上传
      const fileName = userInfo.value.resume.split('-').pop() // 从URL中获取文件名
      resumeFiles.value = [
        {
          name: fileName || '已上传的简历.pdf', // 如果无法获取文件名，使用默认名称
          url: userInfo.value.resume
        }
      ]
    }
  }
  //进行时间戳处理
  updateRuleForm()
})

// 将改变的值ruleForm.birthday进行formatter格式化
const handleDateChange = (newValue) => {
  // 解析传入的日期字符串
  const date = new Date(newValue)

  // 获取年、月、日
  const year = date.getFullYear()
  const month = date.getMonth() + 1 >= 10 ? date.getMonth() + 1 : '0' + (date.getMonth() + 1) // getMonth() 返回的月份是从0开始的，所以需要加1
  const day = date.getDay() + 1 >= 10 ? date.getDay() + 1 : '0' + (date.getDay() + 1)
  // 构建新的日期格式
  const formattedDate = `${year}-${month}-${day}`
  ruleForm.birthday = formattedDate
  // 输出格式化后的日期
  console.log(formattedDate)
}

//处理时间戳转换为以本地时区为条件的 YYYY-MM-DD 字符串格式
const dealDateFormater = (timestamp) => {
  return new Date(timestamp).toLocaleDateString('en-CA')
}

//编辑个人信息
const centerDialogVisible = ref(false)

const formSize = ref<ComponentSize>('default')
const ruleFormRef = ref<FormInstance>()
const ruleForm = reactive<RuleForm>({
  id: null,
  account: '',
  age: null,
  birthday: '',
  degreecategory: '',
  email: '',
  workstatus: null,
  gender: null,
  address: '',
  name: '',
  weixinblur: '',
  resume: '',
  workyears: null,
  desiredSalary: '',
  desiredCity: '', // 期望城市
  desiredIndustry: '', // 期望行业
  desiredPosition: '' // 期望职位
})
const statusMap = {
  0: '暂未选择',
  1: '在校-随时到岗',
  2: '在校-月内到岗',
  3: '在校-考虑机会',
  4: '在校-暂不考虑' // 如果 4 也是 "在校-暂不考虑"
}
//发送请求后将用户信息进行更新
const updateUserInfo = () => {
  userInfo.value.name = ruleForm.name
  userInfo.value.account = ruleForm.account
  userInfo.value.age = ruleForm.age
  userInfo.value.birthday = ruleForm.birthday
  userInfo.value.degreecategory = ruleForm.degreecategory
  userInfo.value.email = ruleForm.email
  userInfo.value.gender = ruleForm.gender
  userInfo.value.address = ruleForm.address
  userInfo.value.weixinblur = ruleForm.weixinblur
  userInfo.value.resume = ruleForm.resume
  userInfo.value.workyears = ruleForm.workyears
  userInfo.value.workstatus = ruleForm.workstatus
  //表格中还没完成
  userInfo.value.desiredSalary = ruleForm.desiredSalary
  userInfo.value.desiredCity = ruleForm.desiredCity
  userInfo.value.desiredIndustry = ruleForm.desiredIndustry
  userInfo.value.desiredPosition = ruleForm.desiredPosition
}

// 更新 ruleForm 的默认值
const updateRuleForm = () => {
  ruleForm.id = userInfo.value.id
  ruleForm.name = userInfo.value.name || '' // 设置默认值
  ruleForm.account = userInfo.value.account || '' // 设置默认值
  ruleForm.age = userInfo.value.age // 将年龄转换为字符串
  ruleForm.birthday = userInfo.value.birthday // 设置默认值
  ruleForm.degreecategory = userInfo.value.degreecategory || '' // 设置默认值
  ruleForm.email = userInfo.value.email || '' // 设置默认值
  ruleForm.gender = userInfo.value.gender //    将性别转换为字符串
  ruleForm.address = userInfo.value.address || '' // 设置默认值
  ruleForm.weixinblur = userInfo.value.weixinblur || '' // 设置默认值
  ruleForm.resume = userInfo.value.resume || '' // 设置默认值
  ruleForm.workyears = userInfo.value.workyears
  ruleForm.workstatus = userInfo.value.workstatus
  ruleForm.desiredSalary = userInfo.value.desiredSalary
  ruleForm.desiredCity = userInfo.value.desiredCity
  ruleForm.desiredIndustry = userInfo.value.desiredIndustry
  ruleForm.desiredPosition = userInfo.value.desiredPosition
}

const locationOptions = ['Home', 'Company', 'School']

const rules = reactive<FormRules<RuleForm>>({
  name: [
    { required: true, message: 'Please input Activity name', trigger: 'blur' },
    { min: 3, max: 5, message: 'Length should be 3 to 5', trigger: 'blur' }
  ],
  account: [
    { required: true, message: '请输入正确的电话号码', trigger: 'blur' },
    { len: 11, message: 'Length should be 11', trigger: 'blur' } // 修改为11位
  ],
  email: [
    { required: true, message: '请输入电子邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入有效的邮箱地址', trigger: ['blur', 'change'] } // 添加邮箱格式验证
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' } // 性别必填
  ],
  address: [
    { required: true, message: '请输入居住地址', trigger: 'blur' } // 地址必填
  ],
  weixinblur: [
    { required: true, message: '请输入微信号', trigger: 'blur' } // 微信号必填
  ],
  degreecategory: [
    { required: true, message: '请选择学历类别', trigger: 'change' } // 学历类别必填
  ]
})

const submitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      console.log('submit!')
    } else {
      console.log('error submit!', fields)
    }
  })
}

const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.resetFields()
}

const options = Array.from({ length: 10000 }).map((_, idx) => ({
  value: `${idx + 1}`,
  label: `${idx + 1}`
}))

// 上传前验证
const beforeUploadPDF = (file) => {
  const isPDF = file.type === 'application/pdf'
  if (!isPDF) {
    ElMessage.error('只能上传 PDF 文件!')
    return false
  }
  const isLt10M = file.size / 1024 / 1024 < 10
  if (!isLt10M) {
    ElMessage.error('文件大小不能超过 10MB!')
    return false
  }
  return true
}

// 无简历--需要上传成功处理
const handleUploadSuccess = async (response, file) => {
  if (response.code === 1) {
    resumeFiles.value = [
      {
        name: file.name,
        url: response.data
      }
    ]

    sessionStorage.setItem('userResume', JSON.stringify(resumeFiles.value))

    if (userInfo.value) {
      userInfo.value.resume = response.data
      try {
        await updateResume(candidateId, response.data)
        ElMessage.success('简历上传成功')
      } catch (error) {
        ElMessage.error('更新简历信息失败')
        console.error(error)
      }
    }
  } else {
    ElMessage.error(response.message || '上传失败')
  }
}

// 移除文件处理
const handleRemove = async (file) => {
  try {
    // 可以添加删除文件的API请求

    resumeFiles.value = []
    sessionStorage.removeItem('userResume')

    // 更新用户信息
    if (userInfo.value) {
      userInfo.value.resume = ''
      // 更新数据库中的简历信息
      await deleteResume(candidateId)
    }

    ElMessage.success('简历已删除')
  } catch (error) {
    ElMessage.error('删除简历失败')
    console.error(error)
  }
}

// 预览PDF
const previewPDF = (file) => {
  if (file.url) {
    window.open(file.url)
  } else {
    ElMessage.warning('文件暂不可预览')
  }
}
</script>












<template>
  <el-container>
    <el-header><h1 :style="{ margin: '0' }">个人中心</h1></el-header>
    <el-main>
      <div class="main_top">
        <div class="main_left">
          <el-row>
            <el-col :span="3">
              <div class="portrait">
                <img
                  src="../../assets/a.jpg"
                  alt=""
                  :style="{ borderRadius: '50%', objectFit: 'cover', height: '80px' }"
                />
              </div>
              <div class="portrait_bottom">
                <a href="#" @click="centerDialogVisible = true"> 编辑信息</a>
              </div>

              <el-dialog v-model="centerDialogVisible" width="1000" center>
                <template #header>
                  <div style="height: 60px; line-height: 60px; font-size: 20px">修改信息</div>
                </template>

                <div class="changeUserInfo">
                  <el-form
                    ref="ruleFormRef"
                    style="max-width: 600px"
                    :model="ruleForm"
                    :rules="rules"
                    label-width="auto"
                    class="demo-ruleForm"
                    :size="formSize"
                    status-icon
                  >
                    <el-form-item label="姓名" prop="name">
                      <el-input v-model="ruleForm.name" />
                    </el-form-item>

                    <el-form-item label="电话" prop="account">
                      <el-input v-model="ruleForm.account" />
                    </el-form-item>

                    <el-form-item label="性别" prop="gender">
                      <el-radio-group v-model="ruleForm.gender">
                        <el-radio value="1">男</el-radio>
                        <el-radio value="0">女</el-radio>
                      </el-radio-group>
                    </el-form-item>
                    <el-form-item label="生日">
                      <el-date-picker
                        v-model="ruleForm.birthday"
                        type="date"
                        placeholder="Pick a date"
                        @change="handleDateChange"
                        clearable
                      />
                    </el-form-item>
                    <el-form-item label="居住地址" prop="address">
                      <el-input v-model="ruleForm.address" />
                    </el-form-item>

                    <!-- <el-form-item label="工作经验" prop="delivery">
                      <el-switch v-model="ruleForm.email" />     
                    </el-form-item> -->

                    <el-form-item label="电子邮箱" prop="email">
                      <el-input v-model="ruleForm.email" />
                    </el-form-item>

                    <!-- <el-form-item label="工作经验描述">
                      <el-input v-model="ruleForm.desc" type="textarea" />
                    </el-form-item> -->
                    <el-form-item label="工作年限" prop="workyears">
                      <el-select-v2
                        v-model="ruleForm.workyears"
                        placeholder="请选择工作年限"
                        :options="options"
                      />
                    </el-form-item>
                    <el-form-item label="Activity type" prop="type">
                      <el-checkbox-group v-model="ruleForm.type">
                        <el-checkbox value="Online activities" name="type">
                          Online activities
                        </el-checkbox>
                        <el-checkbox value="Promotion activities" name="type">
                          Promotion activities
                        </el-checkbox>
                        <el-checkbox value="Offline activities" name="type">
                          Offline activities
                        </el-checkbox>
                        <el-checkbox value="Simple brand exposure" name="type">
                          Simple brand exposure
                        </el-checkbox>
                      </el-checkbox-group>
                    </el-form-item>

                    <el-form-item label="微信号" prop="name">
                      <el-input v-model="ruleForm.weixinblur" />
                    </el-form-item>

                    <el-form-item label="学历类别" prop="region">
                      <el-select v-model="ruleForm.degreecategory" placeholder="Activity zone">
                        <el-option label="本科" value="本科" />
                        <el-option label="专科" value="专科" />
                      </el-select>
                    </el-form-item>
                  </el-form>
                </div>
                <template #footer>
                  <div class="dialog-footer">
                    <el-button @click="resetForm(ruleFormRef)">Reset</el-button>
                    <el-button @click="centerDialogVisible = false">Cancel</el-button>
                    <el-button type="primary" @click="changeConfirm(ruleForm)"> Confirm </el-button>
                  </div>
                </template>
              </el-dialog>
            </el-col>
            <el-col :span="7" :style="{ backgroundColor: 'white' }">
              <div class="username">
                <span>{{ userInfo.name }}</span>
                <span>升级</span>
              </div>
              <div class="baseInfo">
                <span>{{ userInfo.age }}岁</span>
                <i class="vline"></i>
                <span>23年应届生</span>
                <i class="vline"></i>
                <span>{{ userInfo.degreecategory }}</span>
              </div>

              <div class="workerstatus">
                <el-dropdown @command="changeWorkStatus">
                  <el-button type="primary">{{ statusMap[userInfo.workstatus] }} </el-button>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item :command="0">暂未选择</el-dropdown-item>
                      <el-dropdown-item :command="1">在校-随时到岗</el-dropdown-item>
                      <el-dropdown-item :command="2">在校-月内到岗</el-dropdown-item>
                      <el-dropdown-item :command="3">在校-考虑机会</el-dropdown-item>
                      <el-dropdown-item :command="4">在校-暂不考虑</el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </div>
            </el-col>
            <el-col :span="11">
              <div class="workbase">
                <ul>
                  <li>期望城市&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp;&nbsp;{{ userInfo.desiredCity }}</li>
                  <li>
                    期望职位&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp;&nbsp;{{ userInfo.desiredPosition }}
                  </li>
                  <li>期望薪资&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp;&nbsp;{{ userInfo.desiredSalary }}</li>
                </ul>
              </div>
            </el-col>
            <el-col :span="3" :style="{ backgroundColor: 'beige' }">
              <div class="nowInfo">
                <a href="#">在线简历</a>
              </div>
            </el-col>
          </el-row>
        </div>
        <div class="main_right">
          <el-upload
            class="resume-upload"
            drag
            action="http://localhost:8080/candidate/uploadResume"
            :on-success="handleUploadSuccess"
            :on-remove="handleRemove"
            :before-upload="beforeUploadPDF"
            :file-list="resumeFiles"
            :limit="1"
            accept=".pdf,.doc,.docx"
          >
            <template #trigger>
              <div class="upload-trigger" v-if="!resumeFiles.length">
                <div class="upload-icon-wrapper">
                  <el-icon class="upload-icon"><upload-filled /></el-icon>
                  <div class="upload-circle"></div>
                </div>
                <div class="upload-text">拖拽简历到此处或<em>点击上传</em></div>
                <div class="upload-tip">支持 PDF、DOC、DOCX 格式</div>
              </div>
            </template>
          </el-upload>

          <!-- 显示已上传的文件 -->
          <div v-if="resumeFiles.length" class="resume-file">
            <div class="file-info">
              <el-icon><document /></el-icon>
              <span class="filename">{{ resumeFiles[0].name }}</span>
            </div>
            <div class="file-actions">
              <el-button type="primary" link size="small" @click="previewPDF(resumeFiles[0])">
                预览
              </el-button>
              <el-button type="danger" link size="small" @click="handleRemove(resumeFiles[0])">
                删除
              </el-button>
            </div>
          </div>
        </div>
      </div>

      <div class="main_bom">
        <el-row>
          <el-col :span="3"
            ><el-button type="primary" @click="$router.push('/userInfo/commounited')"
              >沟通</el-button
            ></el-col
          >
          <el-col :span="3"
            ><el-button type="primary" @click="$router.push('/userInfo/deliveries')"
              >投递</el-button
            ></el-col
          >
          <el-col :span="3"
            ><el-button type="primary" @click="$router.push('/userInfo/interviews')"
              >面试</el-button
            ></el-col
          >
          <el-col :span="3"
            ><el-button type="primary" @click="$router.push('/userInfo/interests')"
              >感兴趣</el-button
            ></el-col
          >
          <el-col :span="10"></el-col>
          <el-col :span="2"></el-col>
        </el-row>
      </div>

      <div class="main_detail">
        <router-view></router-view>
      </div>
    </el-main>
  </el-container>
</template>

<style lang="scss" scoped>
.el-container {
  min-height: 100vh !important;
  background: linear-gradient(180deg, #e6f3ff 0%, #ffffff 100%);
}
.dialog-footer {
  height: 40px;
  line-height: 40px;
}
// .demo-ruleFzorm {
//   display: inline-block; /* 使表单度自适应 */
// }
.main_bom {
  width: 1200px;
  height: 50px;
  margin: 20px auto;
  background-color: transparent;

  .el-row {
    height: 50px;
    display: flex;
    align-items: center;
    gap: 20px; // 增加按钮之间的间距
    padding: 0 20px;
  }

  .el-col {
    height: 50px;
    display: flex;
    align-items: center;

    .el-button {
      width: 100px; // 统一按钮宽度
      height: 36px; // 调整按钮高度
      border-radius: 18px; // 圆角按钮
      font-size: 14px;
      font-weight: 500;
      letter-spacing: 1px;
      background: linear-gradient(135deg, #a1c4fd 0%, #c2e9fb 100%); // 渐变背景，与页面主题呼应
      border: none;
      box-shadow: 0 2px 8px rgba(161, 196, 253, 0.3);
      transition: all 0.3s ease;

      &:hover {
        transform: translateY(-2px);
        background: linear-gradient(135deg, #89f7fe 0%, #66a6ff 100%); // 悬浮时改变渐变
        box-shadow: 0 4px 12px rgba(102, 166, 255, 0.4);
      }

      &:active {
        transform: translateY(0);
        box-shadow: 0 2px 6px rgba(102, 166, 255, 0.4);
      }
    }
  }
}
.main_detail {
  width: 1200px; /* 使用100%宽度 */
  margin: 20px 0; /* 添加上下间距 */
  margin: 0 auto;
}

//加浮动后还在一行显示的原因：父元素（main_top）不会自动扩展以包含它们的高度，这可能会导致后续的元素（如 main_bom）也在同一行显示
.main_top {
  display: flex; /* 使用Flexbox布局:可以让div一行显示就不用浮动了*/
  width: 1200px;
  height: 165px;
  margin: 0 auto;
  background-color: transparent;
  .main_left {
    width: 900px;
    border: none;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
    background: rgba(255, 255, 255, 0.95);
    border-radius: 10px;

    .changeUserInfo {
      text-align: center; /* 水平居中 */
    }

    .demo-ruleForm {
      display: inline-block; /* 使表单宽度自适应 */
    }

    .nowInfo {
      position: relative;
      a {
        position: absolute;
        height: 30px;
        width: 80px;
        top: 30px;
        right: 0px;
        text-decoration: none;
        line-height: 28px;
        border: 1px solid #dcdfe6;
        box-sizing: border-box;
        border-radius: 10px 0 0 10px;
        color: #606266;
        background: #fff;
        transition: all 0.3s;
        box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);

        &:hover {
          color: #409eff;
          border-color: #409eff;
          box-shadow: 0 2px 8px rgba(64, 158, 255, 0.2);
        }
      }
    }
    .workbase {
      height: 160px;
      display: flex;
      ul {
        margin: 0px;
        padding: 0;
        line-height: 20px;
        height: 135px;
        li {
          margin: 20px 0;
          display: block;
          height: 20px;
          background-color: #f5f7fa;
          text-align: left;
          border-radius: 4px;
          padding: 3px 15px;
          border: 1px solid #ebeef5;
          transition: all 0.3s;

          &:hover {
            background-color: #ecf5ff;
            border-color: #409eff;
          }
        }
      }
    }
    .username {
      display: flex; /* 使用 Flexbox 布局 */
      align-items: center; //和margin 0 auto 差不多---上下对齐
      justify-content: flex-start; /* 左对齐 */
      height: 40px;
      margin: 5px auto;

      span {
        margin-left: 10px;

        height: 40px;
        line-height: 40px;
        font-size: 20px;
      }
    }
    .baseInfo {
      display: flex;
      line-height: 40px;
      height: 40px;
      align-items: center;
      .vline {
        display: inline-block;
        margin: 0 5px;
        width: 1px;
        height: 12px;
        vertical-align: middle;
      }
      span {
        color: #666697;
      }
    }
    .workerstatus {
      margin-top: 15px;
      display: flex;
      height: 50px;
      line-height: 50px;

      .el-button {
        background-color: #9eb8f1;
        height: 40px;
        width: 140px;
      }
    }

    .portrait {
      margin: 0 auto;
      width: 80px;
      height: 90px;
    }

    .portrait_bottom {
      height: 70px;
      line-height: 70px;

      position: relative;

      a {
        position: absolute;
        top: 17px;
        left: 20px;
        line-height: 35px;
        display: block;
        text-decoration: none;
        height: 40px;
        width: 70px;
        border: 1px solid #dcdfe6;
        box-sizing: border-box;
        border-radius: 10px;
        color: #606266;
        background: #fff;
        transition: all 0.3s;
        box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);

        &:hover {
          color: #409eff;
          border-color: #409eff;
          box-shadow: 0 2px 8px rgba(64, 158, 255, 0.2);
        }
      }
    }
  }

  .main_right {
    background-color: transparent;
    width: 300px;
    height: 160px;
    border: none;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
    border-radius: 8px;
    margin-left: 10px;
    padding: 12px;
    box-sizing: border-box;
    display: flex;
    flex-direction: column;

    .resume-upload {
      height: 100%;

      :deep(.el-upload) {
        width: 100%;
        height: 100%;
      }

      :deep(.el-upload-dragger) {
        width: 100%;
        height: 100%;
        padding: 0;
      }
    }

    .upload-trigger {
      height: 100%;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      padding: 10px;

      .el-icon--upload {
        font-size: 24px;
        color: #409eff;
        margin-bottom: 8px;
      }

      .upload-text {
        font-size: 14px;
        color: #606266;
        margin-bottom: 4px;

        em {
          color: #409eff;
          font-style: normal;
          cursor: pointer;
        }
      }

      .upload-tip {
        font-size: 12px;
        color: #909399;
      }
    }

    .resume-file {
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      margin: 12px;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      background: #f5f7fa;
      border-radius: 8px;
      padding: 8px;
      box-sizing: border-box;
      height: calc(100% - 24px);
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);

      .file-info {
        display: flex;
        align-items: center;
        gap: 4px;
        padding-bottom: 4px;
        height: 40px;
        .el-icon {
          font-size: 16px;
          color: #909399;
          flex-shrink: 0;
        }

        .filename {
          flex: 1;
          font-size: 12px;
          color: #606266;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
      }

      .file-actions {
        display: flex;
        justify-content: flex-end;
        gap: 8px;
        margin-top: auto;

        .el-button {
          padding: 2px 6px;
          height: 22px;
          font-size: 12px;

          &.el-button--primary {
            margin-right: 0;
          }
        }
      }
    }

    position: relative;
  }
}

.el-header {
  background-color: transparent !important;
  color: #333;
  text-align: center;
  line-height: 60px;
}

.el-main {
  background-color: transparent !important;
  color: #333;
  text-align: center;
  line-height: 160px;
}

body > .el-container {
  margin-bottom: 40px;
}

.el-dialog {
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .el-input__inner {
    border-radius: 4px;
  }

  .el-button {
    &.el-button--primary {
      background: #409eff;
      border: none;
      box-shadow: 0 2px 8px rgba(64, 158, 255, 0.2);

      &:hover {
        box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
      }
    }
  }
}
</style>












