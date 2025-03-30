<template>
  <div class="container" v-if="registerStore.userRole === 'boos'">
    <el-card class="form-card" shadow="hover">
      <el-steps
        style="max-width: 600px"
        align-center
        :active="registerStore.active"
        finish-status="success"
      >
        <el-step title="公司信息" />
        <el-step title="基本信息" />
        <el-step title="工作信息" />
      </el-steps>

      <div v-if="registerStore.active === 0">
        <el-form :model="registerStore.companyInfo" ref="companyForm">
          <el-form-item label="公司名称" required>
            <el-input
              v-model="registerStore.companyInfo.companyName"
              placeholder="请输入公司名称"
            />
          </el-form-item>
          <el-form-item label="公司地址" required>
            <el-input v-model="registerStore.companyInfo.location" placeholder="请输入公司地址" />
          </el-form-item>

          <!-- 使用下拉选择框 -->
          <el-form-item label="隶属行业" required>
            <el-select v-model="registerStore.companyInfo.industry" placeholder="请选择行业">
              <el-option label="科技" value="科技"></el-option>
              <el-option label="制造" value="制造"></el-option>
              <el-option label="金融" value="金融"></el-option>
              <el-option label="教育" value="教育"></el-option>
              <el-option label="健康医疗" value="健康医疗"></el-option>
              <el-option label="零售" value="零售"></el-option>
              <el-option label="娱乐" value="娱乐"></el-option>
              <el-option label="旅游" value="旅游"></el-option>
              <el-option label="房地产" value="房地产"></el-option>
              <el-option label="交通运输" value="交通运输"></el-option>
              <el-option label="能源" value="能源"></el-option>
              <el-option label="政府" value="政府"></el-option>
              <el-option label="非营利组织" value="非营利组织"></el-option>
            </el-select>
          </el-form-item>

          <!-- 使用日期选择器 -->
          <el-form-item label="成立年份" required>
            <el-date-picker
              v-model="registerStore.companyInfo.establishedYear"
              type="year"
              placeholder="选择年份"
              format="YYYY"
              value-format="YYYY"
            >
            </el-date-picker>
          </el-form-item>

          <!-- 添加电话号码格式验证 -->
          <el-form-item label="联系电话" required>
            <el-input
              v-model="registerStore.companyInfo.contactNumber"
              placeholder="请输入联系电话"
            />
          </el-form-item>

          <!-- 添加邮箱格式验证 -->
          <el-form-item label="公司邮箱" required>
            <el-input v-model="registerStore.companyInfo.email" placeholder="请输入公司邮箱" />
          </el-form-item>

          <!-- 使用文件上传组件 -->
          <el-form-item label="公司头像" required>
            <el-upload
              class="avatar-uploader"
              action="http://localhost:8080/company/uploadImg"
              :show-file-list="false"
              :on-success="companyHandleAvatarSuccess"
            >
              <img v-if="companyImageUrl" :src="companyImageUrl" class="avatar" />
              <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
            </el-upload>
          </el-form-item>

          <!-- 使用下拉选择框 -->
          <el-form-item label="公司规模" required>
            <el-select
              v-model="registerStore.companyInfo.companyScale"
              placeholder="请选择公司规模"
            >
              <el-option label="1-50人" value="1-20"></el-option>
              <el-option label="20-50人" value="20-50"></el-option>
              <el-option label="50-100人" value="50-100"></el-option>
              <el-option label="100-500人" value="100-500"></el-option>
              <el-option label="500-1000人" value="500-1000"></el-option>
              <el-option label="1000以上" value="1000以上"></el-option>
              <!-- 添加其他规模选项 -->
            </el-select>
          </el-form-item>

          <!-- 使用下拉选择框 -->
          <el-form-item label="公司类型" required>
            <el-select v-model="registerStore.companyInfo.companyType" placeholder="请选择公司类型">
              <el-option label="私营" value="私营"></el-option>
              <el-option label="国有" value="国有"></el-option>
              <!-- 添加其他类型选项 -->
            </el-select>
          </el-form-item>
          <!-- 添加其他公司信息字段 -->
        </el-form>
      </div>

      <div v-if="registerStore.active === 1">
        <el-form :model="registerStore.boosInfo" ref="boosForm">
          <el-form-item label="姓名" required>
            <el-input v-model="registerStore.boosInfo.boosName" placeholder="请输入姓名" />
          </el-form-item>
          <el-form-item label="邮箱" required>
            <el-input v-model="registerStore.boosInfo.boosEmail" placeholder="请输入邮箱" />
          </el-form-item>
          <el-form-item label="电话" required>
            <el-input v-model="registerStore.boosInfo.boosPhone" placeholder="请输入电话" />
          </el-form-item>
          <el-form-item label="头像" required>
            <el-upload
              class="avatar-uploader"
              action="http://localhost:8080/boos/uploadImg"
              :show-file-list="false"
              :on-success="boosHandleAvatarSuccess"
            >
              <img v-if="boosImageUrl" :src="boosImageUrl" class="avatar" />
              <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
            </el-upload>
          </el-form-item>

          <el-form-item label="职位" required>
            <el-input v-model="registerStore.boosInfo.companyPosition" placeholder="请输入电话" />
          </el-form-item>
        </el-form>
      </div>

      <div v-if="registerStore.active === 2">
        <el-form :model="registerStore.jobInfo" ref="jobForm">
          <el-form-item label="职位名称" required>
            <el-input v-model="registerStore.jobInfo.jobTitle" placeholder="请输入职位名称" />
          </el-form-item>
          <el-form-item label="技术要求" required>
            <el-input
              v-model="registerStore.jobInfo.requiredTechnology"
              placeholder="请输入技术要求"
            />
          </el-form-item>
          <el-form-item label="职位类别" required>
            <el-input v-model="registerStore.jobInfo.jobCategory" placeholder="请输入职位类别" />
          </el-form-item>
          <el-form-item label="工作地区" required>
            <el-input v-model="registerStore.jobInfo.workLocation" placeholder="请输入工作地区" />
          </el-form-item>
          <el-form-item label="薪资范围" required>
            <el-radio-group v-model="registerStore.jobInfo.salaryRange">
              <el-radio value="0k-3k">0-3000</el-radio>
              <el-radio value="3k-5k">3000-5000</el-radio>
              <el-radio value="5k-10k">5000-10000</el-radio>
              <el-radio value="10k-20k">10000-20000</el-radio>
              <el-radio value="20k以上">20k以上</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="学历要求" required>
            <el-radio-group v-model="registerStore.jobInfo.educationRequirements">
              <el-radio value="大专">大专</el-radio>
              <el-radio value="本科">本科</el-radio>
              <el-radio value="研究生">研究生</el-radio>
              <el-radio value="硕士及以上">硕士及以上</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="职位描述" required>
            <el-input
              type="text"
              v-model="registerStore.jobInfo.jobDescription"
              placeholder='请输入职位描述，格式如 ["1","2","3"]'
            />
          </el-form-item>

          <el-form-item label="详细信息" required>
            <el-input
              type="textarea"
              v-model="registerStore.jobInfo.jobDetails"
              placeholder="请输入详细信息"
            />
          </el-form-item>
          <el-form-item label="工作天数" required>
            <el-input v-model="registerStore.jobInfo.weeklyDays" placeholder="请输入每周工作几天" />
          </el-form-item>
        </el-form>
      </div>

      <div class="button-group">
        <el-button v-if="registerStore.active > 0" @click="prev">上一步</el-button>
        <el-button type="primary" @click="next">{{
          registerStore.active < 3 ? '下一步' : '完成填写'
        }}</el-button>
      </div>
    </el-card>
  </div>

  <div class="container" v-else>
    <el-card class="form-card" :style="{ height: '600px', overflowY: 'auto' }" shadow="hover">
      <div class="candidate_info">
        <h1>填写基本信息</h1>
        <el-form :model="registerStore.candidateInfo" ref="candidateForm">
          <el-form-item label="姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名" required>
            <el-input v-model="registerStore.candidateInfo.name" placeholder="请输入姓名" />
          </el-form-item>
          <el-form-item label="邮&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;箱" required>
            <el-input v-model="registerStore.candidateInfo.email" placeholder="请输入邮箱" />
          </el-form-item>
          <el-form-item label="电&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;话" required>
            <el-input v-model="registerStore.candidateInfo.account" placeholder="请输入电话" />
          </el-form-item>
          <el-form-item label="微&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;信" required>
            <el-input v-model="registerStore.candidateInfo.weixinblur" placeholder="请输入微信号" />
          </el-form-item>
          <el-form-item label="年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;龄" required>
            <el-input v-model="registerStore.candidateInfo.age" placeholder="请输入年龄" />
          </el-form-item>
          <el-form-item label="头&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;像" required>
            <el-upload
              class="avatar-uploader"
              action="http://localhost:8080/candidate/uploadImg"
              :show-file-list="false"
              :on-success="candidateHandleAvatarSuccess"
            >
              <img v-if="candidateImageUrl" :src="candidateImageUrl" class="avatar" />
              <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
            </el-upload>
          </el-form-item>

          <el-form-item label="性&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别" required>
            <el-radio-group v-model="registerStore.candidateInfo.gender">
              <el-radio value="0">女性</el-radio>
              <el-radio value="1">男性</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="学&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;历" required>
            <el-radio-group v-model="registerStore.candidateInfo.degreecategory">
              <el-radio value="大专">大专</el-radio>
              <el-radio value="本科">本科</el-radio>
              <el-radio value="研究生">研究生</el-radio>
              <el-radio value="硕士及以上">硕士及以上</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="生&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日" required>
            <el-date-picker
              v-model="registerStore.candidateInfo.birthday"
              type="date"
              placeholder="选择生日"
              format="YYYY-MM-DD"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item label="地&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;址" required>
            <el-input v-model="registerStore.candidateInfo.address" placeholder="请输入地址" />
          </el-form-item>
          <el-form-item label="工作年限" required>
            <el-input
              v-model="registerStore.candidateInfo.workyears"
              placeholder="请输入工作年限"
            />
          </el-form-item>
          <el-form-item label="简历内容" required>
            <el-upload
              class="resume-uploader"
              action="http://localhost:8080/candidate/uploadResume"
              :show-file-list="true"
              :file-list="fileList"
              :on-success="handleResumeUploadSuccess"
              :before-upload="beforeResumeUpload"
              accept=".pdf,.doc,.docx"
            >
              <template #tip>
                <div class="el-upload__tip">jpg/png files with a size less than 500KB.</div>
              </template>
              <el-button size="small" type="primary">上传简历</el-button>
            </el-upload>
          </el-form-item>

          <el-form-item label="期望薪资" required>
            <el-radio-group v-model="registerStore.candidateInfo.desiredSalary">
              <el-radio value="0k-3k">0-3000</el-radio>
              <el-radio value="3k-5k">3000-5000</el-radio>
              <el-radio value="5k-10k">5000-10000</el-radio>
              <el-radio value="10k-20k">10000-20000</el-radio>
              <el-radio value="20k以上">20k以上</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="技能简介" required>
            <el-input
              type="textarea"
              v-model="registerStore.candidateInfo.skillsIntroduction"
              placeholder="请输入技能简介大约100个字左右"
            />
          </el-form-item>

          <el-form-item label="期望城市" required>
            <el-input
              v-model="registerStore.candidateInfo.desiredCity"
              placeholder="请输入期望薪资"
            />
          </el-form-item>
          <el-form-item label="期望行业" required>
            <el-input
              v-model="registerStore.candidateInfo.desiredIndustry"
              placeholder="请输入期望行业"
            />
          </el-form-item>
          <el-form-item label="期望职位" required>
            <el-input
              v-model="registerStore.candidateInfo.desiredPosition"
              placeholder="请输入期望薪资"
            />
          </el-form-item>
        </el-form>
      </div>

      <div class="button-group">
        <el-button type="primary" @click="next1">完成填写</el-button>
      </div>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { useRegisterStore } from '@/stores/register.js'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import type { UploadProps } from 'element-plus'
import { log } from 'console'
import { boosRegister } from '@/api/boos.js'
import { candidateRegister } from '@/api/candidate.js'
import { useRouter } from 'vue-router'
const router = useRouter()
const registerStore = useRegisterStore()

//处理头像上传返回链接
const companyImageUrl = ref('')
const companyHandleAvatarSuccess: UploadProps['onSuccess'] = (res, uploadFile) => {
  companyImageUrl.value = res.data
  registerStore.companyInfo.companyImg = res.data
}

const boosImageUrl = ref('')
const boosHandleAvatarSuccess: UploadProps['onSuccess'] = (res, uploadFile) => {
  boosImageUrl.value = res.data
  registerStore.boosInfo.boosImg = res.data
}

const candidateImageUrl = ref('')
const candidateHandleAvatarSuccess: UploadProps['onSuccess'] = (res, uploadFile) => {
  candidateImageUrl.value = res.data
  registerStore.candidateInfo.tiny = res.data
}
const fileList = ref([])
const beforeResumeUpload = () => {
  if (fileList.value.length > 0) {
    fileList.value = [] // 清空文件列表
  }
  return true // 允许上传
}
const handleResumeUploadSuccess: UploadProps['onSuccess'] = (res, uploadFile) => {
  fileList.value.push({
    name: res.data,
    url: res.data
  })
  registerStore.candidateInfo.resume = res.data
}

const next = async () => {
  if (registerStore.active <= 2) {
    registerStore.active++
  } else {
    // 提交表单数据
    await boosRegister(
      registerStore.companyInfo,
      registerStore.boosInfo,
      registerStore.jobInfo,
      registerStore.userInfo
    )
    ElMessage.success('注册Boos成功')

    router.push('/login')
  }
}
const next1 = async () => {
  registerStore.candidateInfo.workstatus=0//TODO:-------------------------------这个地方为什么数据库这个字段是not null default 0 为什么要赋值0才可以插入呢
  console.log('candidateInfo:' + JSON.stringify(registerStore.candidateInfo))
  await candidateRegister(registerStore.candidateInfo, registerStore.userInfo)
  // 提交表单数据
  ElMessage.success('注册candidate成功')

  router.push('/login')
}

const prev = () => {
  if (registerStore.active > 0) {
    registerStore.active--
  }
}
</script>

<style lang='less' scoped>
.el-upload__tip {
  margin-top: 0px;
}

.candidate_info {
  position: relative;
  h1 {
    text-align: center;
    margin-top: 0px;
  }
}

.container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh; /* 使容器充满视口高度 */
  background-color: #f5f7fa; /* 背景颜色 */
}

.form-card {
  width: 100%;
  max-width: 600px; /* 限制卡片最大宽度 */
  padding: 20px; /* 内边距 */
  border-radius: 8px; /* 圆角 */
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); /* 阴影效果 */
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

.button-group {
  display: flex;
  justify-content: center;
  gap: 10px; /* 控制按钮之间的间距 */
  margin-top: 12px;
}

.avatar-uploader {
  display: block;
  border: 1px dashed #d9d9d9; /* 添加边框样式 */
  border-radius: 4px; /* 可选：添加圆角 */
  width: 150px;
  height: 150px;
}

.avatar {
  display: block;
  width: 150px;
  height: 150px;
  border-radius: 4px; /* 可选：添加圆角 */
}

.el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 150px;
  height: 150px;
  text-align: center;
}
</style>
