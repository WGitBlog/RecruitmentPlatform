<template>
  <div class="common-layout">
    <el-container>
      <el-header><h1>用戶聊天</h1> </el-header>
      <el-container class="main">
        <el-aside width="400px">
          <div class="searchDialogue">
            <el-row>
              <el-col :style="{ margin: '3px auto' }">
                <el-input v-model="input" style="width: 350px" placeholder="Type something">
                  <template #suffix>
                    <el-icon class="el-input__icon"><Search /></el-icon>
                  </template>
                </el-input>
              </el-col>
            </el-row>
          </div>
          <div class="selectDialogue">
            <el-row>
              <el-col>
                <ul>
                  <li>
                    <span
                      :class="{ gray: selectDialosgue !== 1, active: selectDialosgue === 1 }"
                      @click="spanDeal(1)"
                      >全部</span
                    >
                  </li>
                  <li>
                    <span
                      :class="{ gray: selectDialosgue !== 2, active: selectDialosgue === 2 }"
                      @click="spanDeal(2)"
                      >仅沟通</span
                    >
                  </li>
                  <li>
                    <span
                      :class="{ gray: selectDialosgue !== 3, active: selectDialosgue === 3 }"
                      @click="spanDeal(3)"
                      >有交换</span
                    >
                  </li>
                  <li>
                    <span
                      :class="{ gray: selectDialosgue !== 4, active: selectDialosgue === 4 }"
                      @click="spanDeal(4)"
                      >有面试</span
                    >
                  </li>
                  <li>
                    <span
                      :class="{ gray: selectDialosgue !== 5, active: selectDialosgue === 5 }"
                      @click="spanDeal(5)"
                      >感兴趣</span
                    >
                  </li>
                </ul>
              </el-col>
            </el-row>
          </div>
          <div class="boosList">
            <el-row>
              <el-col>
                <ul v-if="candidateId">
                  <li v-for="item in items" :key="item.id" @click="changeMessage(item)">
                    <div class="friend_context">
                      <div class="friend_img">
                        ·
                        <img
                          src="../../assets/a.jpg"
                          alt=""
                          :style="{ borderRadius: '50%', objectFit: 'cover' }"
                        />
                      </div>
                      <div class="friend_info">
                        <el-row class="friend_top" justify="space-between">
                          <el-col span="10">
                            <span :style="{ fontSize: '20px', marginRight: '10px' }">{{
                              item.boosName
                            }}</span>
                            <span>{{ item.companyName }} |</span>
                            <span> {{ item.companyPosition }}</span>
                          </el-col>
                          <el-col span="10">
                            <span>14:43</span>
                          </el-col>
                        </el-row>
                        <el-row class="friend_bom">
                          <el-col class="bom_icon">
                            <span class="iconfont icon-yuanquan" v-show="true"></span>
                            <span :style="{ fontSize: '12px' }"
                              >您好，我是搜索中专/中技生，可以...</span
                            >
                            <el-popover placement="top" trigger="hover" :style="{ width: '40px' }">
                              <span :style="{ fontSize: '15px' }">置顶</span>
                              <span>删除</span>
                              <template #reference>
                                <a href="#" class="iconfont icon-shenglvehao1 omit_function"></a>
                              </template>
                            </el-popover>
                          </el-col>
                        </el-row>
                      </div>
                    </div>
                  </li>
                </ul>

                <ul v-else>
                  <li v-for="item in itemsCandidate" :key="item.id" @click="changeMessage(item)">
                    <div class="friend_context">
                      <div class="friend_img">
                        ·
                        <img
                          src="../../assets/b.jpg"
                          alt=""
                          :style="{ borderRadius: '50%', objectFit: 'cover' }"
                        />
                      </div>
                      <div class="friend_info">
                        <el-row class="friend_top" justify="space-between">
                          <el-col span="10">
                            <span :style="{ fontSize: '20px', marginRight: '10px' }">{{
                              item.name
                            }}</span>
                            <span>{{ item.degreecategory }} |</span>
                            <span> 学生</span>
                          </el-col>
                          <el-col span="10">
                            <span>14:43</span>
                          </el-col>
                        </el-row>
                        <el-row class="friend_bom">
                          <el-col class="bom_icon">
                            <span class="iconfont icon-yuanquan" v-show="true"></span>
                            <span :style="{ fontSize: '12px' }"
                              >您好，我是搜索中专/中技生，可以...</span
                            >
                            <el-popover placement="top" trigger="hover" :style="{ width: '40px' }">
                              <span :style="{ fontSize: '15px' }">置顶</span>
                              <span>删除</span>
                              <template #reference>
                                <a href="#" class="iconfont icon-shenglvehao1 omit_function"></a>
                              </template>
                            </el-popover>
                          </el-col>
                        </el-row>
                      </div>
                    </div>
                  </li>
                </ul>
              </el-col>
            </el-row>
          </div>
        </el-aside>
        <el-main>
          <el-row v-if="candidateId">
            <el-col :span="23" class="baseInfo_bom">
              <el-row justify="space-between">
                <el-col :span="7" class="baseInfo_left">
                  <span :style="{ marginRight: '30px' }">{{ defaultItem.boosName }}</span>
                  <span>{{ defaultItem.companyName }}|{{ defaultItem.companyPosition }}</span>
                </el-col>
                <el-col :span="2" class="baseInfo_right">
                  <span><el-button type="primary" plain>置顶</el-button></span>
                </el-col>
              </el-row>
            </el-col>
          </el-row>

          <el-row v-else>
            <el-col :span="23" class="baseInfo_bom">
              <el-row justify="space-between">
                <el-col :span="7" class="baseInfo_left">
                  <span :style="{ marginRight: '12px' }">{{ defaultItemCds.name }}</span>
                  <span>{{ defaultItemCds.degreecategory }}|学生</span>
                </el-col>
                <el-col :span="2" class="baseInfo_right">
                  <span><el-button type="primary" plain>置顶</el-button></span>
                </el-col>
              </el-row>
            </el-col>
          </el-row>

          <el-row v-if="candidateId">
            <el-col :span="23" class="baseInfo_bom">
              <el-row justify="space-between">
                <el-col :span="7" class="baseSpan_left">
                  <span>{{ defaultItem.jobTitle }}</span>
                  <span>{{ defaultItem.salaryRange }}</span>
                  <span>{{ defaultItem.workLocation }}</span>
                </el-col>
                <el-col :span="5">
                  <span :style="{ fontSize: '10px' }" class="a">下载App, 不错过Boss每一条消息</span>
                </el-col>
              </el-row>
            </el-col>
          </el-row>

          <el-row v-else>
            <el-col :span="23" class="baseInfo_bom">
              <el-row justify="space-between">
                <el-col :span="7" class="baseSpan_left">
                  <span>{{ defaultItemCds.age }}岁</span>
                  <span v-html="defaultItemCds.gender == 1 ? '男' : '女'"></span>
                  <span>{{ defaultItemCds.address }}</span>
                </el-col>
                <el-col :span="5">
                  <span :style="{ fontSize: '10px' }" class="a">下载App, 不错过Boss每一条消息</span>
                </el-col>
              </el-row>
            </el-col>
          </el-row>

          <div class="dialogue_frame">
            <div class="main_message" ref="messageScroll">
              <div
                v-for="(msg, index) in message"
                :key="index"
                :class="msg.senderId === Number(UserId) ? 'message-right' : 'message-left'"
              >
                <div class="name">
                  <span>{{ msg.createTime }}</span>
                </div>

                <div v-if="msg.msgType === 4" class="resume-preview boss-green">
                  <div class="resume-icon">
                    <el-icon><Document /></el-icon>
                  </div>
                  <div class="resume-info">
                    <div class="resume-name">{{ msg.msgContent.split('-').pop() }}</div>
                    <div class="resume-action">
                      <el-button
                        class="preview-btn"
                        type="primary"
                        size="small"
                        @click="previewResume(msg.msgContent)"
                      >
                        点击预览附件简历
                      </el-button>
                    </div>
                  </div>
                </div>

                <div v-else-if="msg.msgType === 3" class="resume-preview boss-green">
                  <div class="resume-icon">
                    <el-icon><ChatDotRound /></el-icon>
                  </div>
                  <div class="resume-info">
                    <div class="resume-name">{{ msg.msgContent }}</div>
                    <div class="resume-action">
                      <el-button
                        class="preview-btn"
                        type="primary"
                        size="small"
                        @click="copyWechat(msg.msgContent)"
                      >
                        点击复制微信号
                      </el-button>
                    </div>
                  </div>
                </div>

                <div v-else-if="msg.msgType === 2" class="resume-preview boss-green">
                  <div class="resume-icon">
                    <el-icon><Phone /></el-icon>
                  </div>
                  <div class="resume-info">
                    <div class="resume-name">{{ msg.msgContent }}</div>
                    <div class="resume-action">
                      <el-button
                        class="preview-btn"
                        type="primary"
                        size="small"
                        @click="copyPhone(msg.msgContent)"
                      >
                        点击复制手机号
                      </el-button>
                    </div>
                  </div>
                </div>

                <div v-else class="chat_message">
                  {{ msg.msgContent }}
                </div>
              </div>
            </div>
          </div>

          <div class="message_box">
            <el-row>
              <el-col :span="23" class="function_module">
                <el-row>
                  <el-col :span="5" class="emo_module">
                    <ul>
                      <li>
                        <el-popover
                          placement="top"
                          trigger="click"
                          :width="240"
                          popper-class="emoji-popover"
                        >
                          <template #reference>
                            <div class="icon-wrapper">
                              <i class="iconfont icon-biaoqing"></i>
                            </div>
                          </template>
                          <div class="emoji-container">
                            <div class="emoji-grid">
                              <span
                                v-for="emoji in emojiList"
                                :key="emoji"
                                class="emoji-item"
                                @click="selectEmoji(emoji)"
                              >
                                {{ emoji }}
                              </span>
                            </div>
                          </div>
                        </el-popover>
                      </li>

                      <li>
                        <el-popover
                          placement="top"
                          trigger="click"
                          :width="240"
                          popper-class="phrases-popover"
                        >
                          <template #reference>
                            <div class="icon-wrapper">
                              <i class="iconfont icon-changyongyu"></i>
                            </div>
                          </template>
                          <div class="phrases-container">
                            <div v-if="isCandidate" class="phrases-list">
                              <div
                                v-for="phrase in candidatePhrases"
                                :key="phrase"
                                class="phrase-item"
                                @click="selectPhrase(phrase)"
                              >
                                {{ phrase }}
                              </div>
                            </div>
                            <div v-else class="phrases-list">
                              <div
                                v-for="phrase in bossPhrases"
                                :key="phrase"
                                class="phrase-item"
                                @click="selectPhrase(phrase)"
                              >
                                {{ phrase }}
                              </div>
                            </div>
                          </div>
                        </el-popover>
                      </li>

                      <li>
                        <el-popover
                          placement="top"
                          trigger="click"
                          :width="240"
                          popper-class="notes-popover"
                        >
                          <template #reference>
                            <div class="icon-wrapper">
                              <i class="iconfont icon-tixing"></i>
                            </div>
                          </template>
                          <div class="notes-container">
                            <div class="notes-header">
                              <span>添加备注</span>
                            </div>
                            <el-input
                              v-model="noteInput"
                              type="textarea"
                              :rows="3"
                              placeholder="添加重要备注..."
                              resize="none"
                            />
                            <div class="reminder-section">
                              <el-checkbox v-model="needReminder">设置提醒时间</el-checkbox>
                              <el-date-picker
                                v-if="needReminder"
                                v-model="reminderTime"
                                type="datetime"
                                placeholder="选择提醒时间"
                                format="YYYY-MM-DD HH:mm"
                                :disabled-date="disabledDate"
                                size="small"
                              />
                            </div>
                            <div class="notes-footer">
                              <el-button size="small" @click="clearNote">清空</el-button>
                              <el-button type="primary" size="small" @click="saveNote"
                                >保存</el-button
                              >
                            </div>
                          </div>
                        </el-popover>
                      </li>

                      <li>
                        <div class="icon-wrapper">
                          <input
                            type="file"
                            ref="fileInput"
                            accept="image/*"
                            style="display: none"
                            @change="handleImageSelected"
                          />
                          <i class="iconfont icon-tupian" @click="triggerFileInput"></i>
                        </div>
                      </li>
                    </ul>
                  </el-col>
                  <el-col v-if="candidateId" :span="9" class="button_module">
                    <span @click="sendResumeMessage">发简历</span>
                    <span @click="sendAccountMessage">换电话</span>
                    <span @click="sendWeixinBlurMessage">换微信</span>
                  </el-col>

                  <el-col v-else :span="8" class="button_module">
                    <!-- <span @click="sendResumeMessage">发简历</span>
                    <span @click="sendAccountMessage">换电话</span>
                    <span @click="sendWeixinBlurMessage">换微信</span> -->
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="24" class="chat_content">
                    <textarea
                      rows="3"
                      v-model="messageInput"
                      @keyup.enter="sendMessage()"
                    ></textarea>
                  </el-col>
                </el-row>
                <el-row justify="end">
                  <el-col :span="10" class="chat_footer">
                    按Enter键发送，按Ctrl+Enter键换行
                  </el-col>
                  <el-col :span="2" class="msg_send">
                    <el-button @click="sendMessage()">发送</el-button>
                  </el-col>
                </el-row>
              </el-col>
            </el-row>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>


<script  lang="ts" setup>
import { nextTick, onMounted, ref, computed } from 'vue'
import { useCandidateStore } from '@/stores/candidate'
import { Calendar, Search } from '@element-plus/icons-vue'
import { Document, ChatDotRound, Phone } from '@element-plus/icons-vue'
import {
  getAllJobs,
  getCommunicatedJobs,
  getDeliveriesJobs,
  getInterestsJobs,
  getInterviewsJobs
} from '@/api/candidate.js'
import {
  getAllCds,
  getCommunicatedCds,
  getDeliveriesCds,
  getInterestsCds,
  getInterviewsCds
} from '@/api/boos.js'
import { getMessagesByIds } from '@/api/messages.js'
import { getUserByBoosId, getUserByCandidateId } from '@/api/user.js'
import { useRoute } from 'vue-router'
import useWebSocket from '@/utils/websocket.js'
import { id } from 'element-plus/es/locale'
import { sortUserPlugins } from 'vite'
import { JobDto, messages, candidate } from '@/interface/index.js'

const route = useRoute() // 获取路由对象
const candidateStore = useCandidateStore()
const UserId = sessionStorage.getItem('userId')
const candidateId = sessionStorage.getItem('candidateId')
const boosId = sessionStorage.getItem('boosId')

const message = ref<messages[]>([])

//处理时间格式yyyy-MM-dd HH:mm:ss
const formatDate = (date) => {
  const pad = (num) => (num < 10 ? '0' + num : num) // 补零函数
  const year = date.getFullYear()
  const month = pad(date.getMonth() + 1) // 月份从0开始
  const day = pad(date.getDate())
  const hours = pad(date.getHours())
  const minutes = pad(date.getMinutes())
  const seconds = pad(date.getSeconds())

  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
}

const ws = useWebSocket((e) => {
  // 解析 e.data
  const messageData = JSON.parse(e.data) // 解 JSON 字符串为对象
  console.log('message数据:', messageData)

  // 格式化时间
  messageData.createTime = formatDate(new Date(messageData.createTime)) // 将时间戳转换为日期并格式化
  console.log('格式化后的时间:', messageData.createTime)
  // 判断接收到的消息是否属于当前对话
  if (candidateId) {
    // 求职者端
    if (messageData.recipientId === idStorage.value || messageData.senderId === idStorage.value) {
      message.value.push(messageData)
    }
  } else if (boosId) {
    console.log(JSON.stringify(defaultItemCds.value))
    console.log(messageData.recipientId + '====' + idStorage.value)
    console.log(messageData.senderId + '====' + idStorage.value)

    // boss端
    if (messageData.recipientId === idStorage.value || messageData.senderId === idStorage.value) {
      console.log(3)
      message.value.push(messageData)
    }
  }

  scrollToBottom()
})

//用来存储接收者的UserId
const idStorage = ref(null)
//消息框数据绑定
const messageInput = ref('')
//定义生成消息的方法
const createMessage = () => {
  return {
    msgContent: messageInput.value,
    msgId: null,
    msgType: 1,
    received: 0,
    recipientId: idStorage.value,
    senderId: Number(UserId),
    //发送时候是字符串接收是时间戳的问题：因为数据会发送到后端会有框架将其封装成Messages类并发送给相应的channel并且变成时间戳
    createTime: formatDate(new Date())
  }
}
const sendMessage = () => {
  // 在发送前先去除字符串两端的空白字符
  messageInput.value = messageInput.value.trim()
  //获取到生成的消息
  const oneMessage = createMessage()
  //不准发null消息
  if (messageInput.value === '') {
    ElMessage.warning('消息不能为空')
    return
  }
  // 调试输出
  console.log('生成的消息对象:', oneMessage) // 检查 oneMessage 的内容
  console.log('createTime:', oneMessage.createTime) // 检查 createTime 的值
  ws.send(JSON.stringify(oneMessage))

  //将消息oneMessage存入到message中
  message.value.push(oneMessage)

  console.log('msg:' + JSON.stringify(message.value.at(-1)))

  //清空数据框
  messageInput.value = ''
  //发送消息滑轮到最低部
  scrollToBottom()
}

// 发送简历消息
const sendResumeMessage = async () => {
  console.log('candidateStore.resume:', JSON.stringify(candidateStore.resume))

  //查询resume的链接
  const resumeUrl = candidateStore.candidateInfo.resume
  //获取到生成的消息
  const oneMessage = createMessage()
  oneMessage.msgType = 4
  oneMessage.msgContent = resumeUrl

  ws.send(JSON.stringify(oneMessage))
  message.value.push(oneMessage)
  //清空数据框
  messageInput.value = ''
  //发送消息滑轮到最低部
  scrollToBottom()
}

const sendWeixinBlurMessage = async () => {
  //查询resume的链接
  const weixinblur = candidateStore.candidateInfo.weixinblur
  //获取到生成的消息
  const oneMessage = createMessage()
  oneMessage.msgType = 3
  oneMessage.msgContent = weixinblur

  ws.send(JSON.stringify(oneMessage))
  message.value.push(oneMessage)
  //清空数据框
  messageInput.value = ''
  //发送消息滑轮到最低部
  scrollToBottom()
}

const sendAccountMessage = async () => {
  //查询resume的链接
  const account = candidateStore.candidateInfo.account
  //获取到生成的消息
  const oneMessage = createMessage()
  oneMessage.msgType = 2
  oneMessage.msgContent = account

  ws.send(JSON.stringify(oneMessage))
  message.value.push(oneMessage)
  //清空数据框
  messageInput.value = ''
  //发送消息滑轮到最低部
  scrollToBottom()
}

const input = ref('')
//聊天框滑轮划到最低部
const messageScroll: any = ref(null)
function scrollToBottom() {
  nextTick(() => {
    messageScroll.value.scrollTop =
      messageScroll.value.scrollHeight - messageScroll.value.offsetHeight
    console.log(messageScroll.value.scrollTop) //正常来说应该是937.5999755859375  0
    console.log(messageScroll.value.offsetHeight) //1547                         1547
    console.log(messageScroll.value.scrollHeight) //2553                        1540
  })
}

const items = ref([]) //boos数据列表     ���candidate登录）
const itemsCandidate = ref([]) //candidate数据列表（boos     登录）

const defaultItem = ref<JobDto>({
  boosId: 0,
  boosImg: '',
  boosName: '',
  companyId: 0,
  companyImg: '',
  companyMarket: false,
  companyName: '',
  companyPosition: '',
  companyScale: '',
  companyType: '',
  educationRequirements: '',
  id: 0,
  jobCategory: '',
  jobDescription: [],
  jobDetails: '',
  jobTitle: '',
  publishTime: Date.now(),
  requiredTechnology: '',
  salaryRange: '',
  weeklyDays: 5,
  workLocation: '',
  review: 0
}) //默认聊天框展示对象

const defaultItemCds = ref<candidate>({
  id: 0, //candidateId
  account: '',
  age: 0,
  birthday: '',
  complete: 0,
  degreecategory: '',
  email: '',
  freshgraduate: 0,
  gender: 0,
  address: '',
  name: '',
  startworkdate: 0,
  tiny: '',
  weixinblur: '',
  workyears: 0,
  resume: '',
  applicantId: 0,
  desiredCity: '',
  desiredIndustry: '',
  desiredPosition: ''
}) //默认聊天框展示对象

onMounted(async () => {
  const candidateId = sessionStorage.getItem('candidateId')
  const boosId = sessionStorage.getItem('boosId')

  console.log('item:' + route.query.item)
  console.log('defaultJob:' + route.query.defaultJob)

  if (candidateId) {
    //判断如果是candidate
    const res = await getAllJobs(candidateId)

    const processedRecords = processedData(res) //处理getAllJobs请求的json转义问题
    items.value.push(...processedRecords) //将元素遍历加入itemlist里面

    //判断路由是否带参数
    if (route.query.item) {
      // 解析传递的参数
      const item = JSON.parse(decodeURIComponent(route.query.item as string)) // 使用 route 而不是 $route
      defaultItem.value = item
      console.log('从userInfo跳转参数:' + JSON.stringify(item))

      // 找到想要放到第一位的数组元素的索引----将目标元素插入到数组的第一位
      const foundItemIndex = items.value.findIndex((a) => a.id === item.id)
      console.log(foundItemIndex)

      // 判断查找的元素在数组中是否存在
      if (foundItemIndex !== -1) {
        // 应该是 !== -1
        // 将其从数组中取出
        const [foundItem] = items.value.splice(foundItemIndex, 1) // 使用解构赋值，取出单个元素

        // 将目标元素插入到数组的第一位
        items.value.unshift(foundItem)
      }

      //根据boosId将其左侧的boss列表将其置顶排序
    } else if (route.query.defaultJob) {
      //带参数从layoutContainer跳转出来的
      // 解析传递的参数
      const item = JSON.parse(decodeURIComponent(route.query.defaultJob as string)) // 使用 route 而不是 $route
      defaultItem.value = item
      console.log(`这里是layoutContainer带传参的数据`)
      console.log(defaultItem.value)

      // 找到想要放到第一位的数组元素的索引----将目标元素插入到数组的第一位
      const foundItemIndex = items.value.findIndex((a) => a.id === item.id)
      console.log(foundItemIndex)

      // 判断查找的元素在数组中是否存在
      if (foundItemIndex !== -1) {
        // 应该是 !== -1
        // 将其从数组中取出
        const [foundItem] = items.value.splice(foundItemIndex, 1) // 使用解构赋值，取出单个元素

        // 将目标元素插入到数组的第一位
        items.value.unshift(foundItem)
      }
    } else {
      //不带参数
      defaultItem.value = { ...items.value[0] } //设置默认展示的数据
      //获取boos和candidate的聊天数据数据
      console.log('这里是不带传参的数据')
    }
    //getMessage方法中也有await可以嵌套使用await

    await getMessage(defaultItem.value, defaultItemCds.value)
  }

  if (boosId) {
    //判断如果是boos
    const res = await getAllCds(boosId)
    itemsCandidate.value.push(...res.data) //将元素遍历加入itemlist里面
    //判断路由是否带参数
    if (route.query.candidateInfo) {
      //带参数从layoutContainer跳转出来的
      // 解析传递的参数
      const item = JSON.parse(decodeURIComponent(route.query.candidateInfo as string)) // 使用 route 而不是 $route
      defaultItemCds.value = item
      console.log(`这里是layoutContainer带传参的数据`)

      // 找到想要放到第一位的数组元素的索引----将目标元素插入到数组的第一位
      const foundItemIndex = itemsCandidate.value.findIndex((a) => a.id === item.id)
      console.log(foundItemIndex)

      // 判断查找的元素在数组中是否存在
      if (foundItemIndex !== -1) {
        // 应该是 !== -1
        // 将其从数组中取出
        const [foundItem] = itemsCandidate.value.splice(foundItemIndex, 1) // 使用解构赋值，取出单个元素

        // 将目标元素插入到数组的第一位
        itemsCandidate.value.unshift(foundItem)
      }
    } else {
      //没带参数
      defaultItemCds.value = { ...itemsCandidate.value[0] } //设置默认展示的数据
      console.log(defaultItemCds.value)
    }
    await getMessage(defaultItem.value, defaultItemCds.value)
  }
  //将滑轮滑到底部
  scrollToBottom()
})

//获取boos和candidate的聊天数据数据
const getMessage = async (defaultItem, defaultItemCds) => {
  if (candidateId) {
    const boosId = defaultItem.boosId //从默认数据中获取BoosID

    const { data } = await getUserByBoosId(boosId) //这里需要获取boos的userID
    const boosUserId = data.id //boos的userID
    //将boosUserId存入idStorage
    idStorage.value = boosUserId
    //发送请求获取默认数据的聊天记录数据
    const messagesData = await getMessagesByIds(UserId, boosUserId)
    //将数据更新到message
    message.value = messagesData.data
  }

  if (boosId) {
    const candidateId = defaultItemCds.id
    //根据candidateId查询出对应的userId：因为getMessagesByIds接收的参数都是UserID
    const user = await getUserByCandidateId(candidateId)
    const candidateUserId = user.data.id
    //将candidateUserId存入idStorage
    idStorage.value = candidateUserId
    const messagesData = await getMessagesByIds(UserId, candidateUserId)
    //将数据更新到message
    message.value = messagesData.data
  }
}

// 处理点击li之后消息的变换
const changeMessage = async (item) => {
  if (candidateId) {
    // 将 defaultItem 置为空对
    defaultItem.value = {} as JobDto // 先清空对象
    // 更新 defaultItem 的属性
    defaultItem.value.boosId = item.boosId // 从 item 中获取 boosId
    defaultItem.value.boosImg = item.boosImg // 从 item 中获取 boosImg
    defaultItem.value.boosName = item.boosName // 从 item 中获取 boosName
    defaultItem.value.companyId = item.companyId // 从 item 中获取 companyId
    defaultItem.value.companyImg = item.companyImg // 从 item 中获取 companyImg
    defaultItem.value.companyMarket = item.companyMarket // 从 item 中获取 companyMarket
    defaultItem.value.companyName = item.companyName // 从 item 中获取 companyName
    defaultItem.value.companyPosition = item.companyPosition // 从 item 中获取 companyPosition
    defaultItem.value.companyScale = item.companyScale // 从 item 中获取 companyScale
    defaultItem.value.companyType = item.companyType // 从 item 中获取 companyType
    defaultItem.value.educationRequirements = item.educationRequirements // 从 item 中获取 educationRequirements
    defaultItem.value.id = item.id // 从 item 中获取 id
    defaultItem.value.jobCategory = item.jobCategory // 从 item 中获取 jobCategory
    defaultItem.value.jobDescription = item.jobDescription // 从 item 中获取 jobDescription
    defaultItem.value.jobDetails = item.jobDetails // 从 item 中获取 jobDetails
    defaultItem.value.jobTitle = item.jobTitle // 从 item 中获取 jobTitle
    defaultItem.value.publishTime = item.publishTime // 设置当前时间
    defaultItem.value.requiredTechnology = item.requiredTechnology // 从 item 中获取 requiredTechnology
    defaultItem.value.salaryRange = item.salaryRange // 从 item 中获取 salaryRange
    defaultItem.value.weeklyDays = item.weeklyDays // 从 item 中获取 weeklyDays
    defaultItem.value.workLocation = item.workLocation // 从 item 中获取 workLocation
    defaultItem.value.review = item.review // 从 item 中获取 workLocation
    getMessage(defaultItem.value, defaultItemCds.value)
  }

  if (boosId) {
    defaultItemCds.value = {} as candidate // 先清空对象
    defaultItemCds.value.id = item.id // 继续写下去
    defaultItemCds.value.account = item.account // 赋值账户
    defaultItemCds.value.age = item.age // 赋值年龄
    defaultItemCds.value.birthday = item.birthday // 赋值生日
    defaultItemCds.value.complete = item.complete // 赋值完成度
    defaultItemCds.value.degreecategory = item.degreecategory // 赋值学位类别
    defaultItemCds.value.email = item.email // 赋值电��邮件
    defaultItemCds.value.freshgraduate = item.freshgraduate // 赋值应届毕业生标识
    defaultItemCds.value.gender = item.gender // 赋值性别
    defaultItemCds.value.address = item.address // 赋值地址
    defaultItemCds.value.name = item.name // 赋值姓名
    defaultItemCds.value.startworkdate = item.startworkdate // 赋值开始工作日期
    defaultItemCds.value.tiny = item.tiny // 赋值小图标
    defaultItemCds.value.weixinblur = item.weixinblur // 赋值微信模糊信息·
    defaultItemCds.value.workyears = item.workyears // 赋值工作年限
    defaultItemCds.value.resume = item.resume // 赋值简历
    defaultItemCds.value.applicantId = item.applicantId // 赋值申请人ID
    defaultItemCds.value.desiredSalary = item.desiredSalary
    defaultItemCds.value.desiredCity = item.desiredCity
    defaultItemCds.value.desiredIndustry = item.desiredIndustry
    defaultItemCds.value.desiredPosition = item.desiredPosition
    getMessage(defaultItem.value, defaultItemCds.value)
  }
}

//处理getAllJobs请求的json转义问题
const processedData = (res) => {
  // 检查 res 和 res.data 是否存
  if (!res || !res.data) {
    console.warn('No data to process')
    return []
  }

  return res.data
    .map((item) => {
      // 检查 item 是否存在
      if (!item) {
        console.warn('Null item found in data')
        return null
      }

      try {
        // 处理 jobDescription
        if (item.jobDescription) {
          if (typeof item.jobDescription === 'string') {
            try {
              item.jobDescription = JSON.parse(item.jobDescription)
            } catch (e) {
              console.warn('Error parsing jobDescription:', e)
              item.jobDescription = [] // 设置默认值
            }
          } else if (Array.isArray(item.jobDescription)) {
            // 如果已经是数组，保持不变
            console.log('jobDescription is already an array:', item.jobDescription)
          } else {
            // 其他情况设置为空数组
            item.jobDescription = []
          }
        } else {
          item.jobDescription = [] // 如果不存在则设置默认值
        }

        // 处理 jobDetails
        if (item.jobDetails && typeof item.jobDetails === 'string') {
          item.jobDetails = item.jobDetails.replace(/\\n/g, '<br>')
        }

        return item
      } catch (e) {
        console.error('Error processing item:', e)
        return null
      }
    })
    .filter(Boolean) // 过滤掉 null 值
}

const selectDialosgue = ref(1) //判断显示boosList的类型默认值是全部 1-全部-getAllJobs  2-已沟通-getCommunicatedJobs  3-已投递-getDeliveriesJobs 4-邀��试-getInterviewsJobs 5-感兴趣-getInterestsJobs

//对span的点击事件进行统一处理
const spanDeal = async (index) => {
  if (selectDialosgue.value !== index) {
    selectDialosgue.value = index
    const candidateId = sessionStorage.getItem('candidateId')
    const boosId = sessionStorage.getItem('boosId')
    if (candidateId) {
      //判断如果是candidate
      switch (index) {
        case 1: {
          const res = await getAllJobs(candidateId)
          const processedRecords = processedData(res)
          items.value = [] //将列表置空
          items.value.push(...processedRecords) //将元素遍历加入itemlist里面
          break
        }
        case 2: {
          const res = await getCommunicatedJobs(candidateId)
          const processedRecords = processedData(res)
          items.value = [] //将列表置空
          items.value.push(...processedRecords) //将元素遍历加入itemlist里面
          break
        }
        case 3: {
          const res = await getDeliveriesJobs(candidateId)
          const processedRecords = processedData(res)
          items.value = [] //将列表置空
          items.value.push(...processedRecords) //将元素遍历加入itemlist里面
          break
        }
        case 4: {
          const res = await getInterviewsJobs(candidateId)
          const processedRecords = processedData(res)
          items.value = [] //将列表置空
          items.value.push(...processedRecords) //将元素遍历加入itemlist里面
          break
        }
        case 5: {
          const res = await getInterestsJobs(candidateId)
          const processedRecords = processedData(res) //处理getAllJobs请求的json转义问题
          items.value = [] //将列表置空
          items.value.push(...processedRecords) //将元素遍历加入itemlist里面
          break
        }
      }
    }
    if (boosId) {
      //TODO:还需要操作
      //判断如果是boos
      switch (index) {
        case 1: {
          const res = await getAllCds(boosId)
          itemsCandidate.value = [] //将列表置空
          itemsCandidate.value.push(...res.data) //将元素遍历加入itemlist里面
          break
        }
        case 2: {
          const res = await getCommunicatedCds(boosId)
          itemsCandidate.value = [] //将列表置空
          itemsCandidate.value.push(...res.data) //将元素遍历加入itemlist里面
          break
        }
        case 3: {
          const res = await getDeliveriesCds(boosId)
          itemsCandidate.value = [] //将列表置空
          itemsCandidate.value.push(...res.data) //将元素遍历加入itemlist里面
          break
        }
        case 4: {
          const res = await getInterviewsCds(boosId)
          itemsCandidate.value = [] //将列表置空
          itemsCandidate.value.push(...res.data) //将元素遍历加入itemlist里面
          break
        }
        case 5: {
          const res = await getInterestsCds(boosId)
          itemsCandidate.value = [] //将列表置空
          itemsCandidate.value.push(...res.data) //将元素遍历加入itemlist里面
          break
        }
      }
    }
  }
}

// 预览简历
const previewResume = (url) => {
  if (url) {
    window.open(url)
  } else {
    ElMessage.warning('简历暂不可预览')
  }
}

const copyWechat = (wechat) => {
  const input = document.createElement('input')
  input.value = wechat
  document.body.appendChild(input)
  input.select()
  document.execCommand('copy')
  document.body.removeChild(input)
  ElMessage.success('微信号已复制到剪贴板')
}

const copyPhone = (phone) => {
  const input = document.createElement('input')
  input.value = phone
  document.body.appendChild(input)
  input.select()
  document.execCommand('copy')
  document.body.removeChild(input)
  ElMessage.success('手机号已复制到剪贴板')
}

const emojiList = ref([
  '😀',
  '😃',
  '😄',
  '😁',
  '😆',
  '😂',
  '🤣',
  '😊',
  '😇',
  '🙂',
  '🙃',
  '😉',
  '😌',
  '😍',
  '🥰',
  '😘',
  '😗',
  '😙',
  '😚',
  '😋',
  '😛',
  '😝',
  '😜',
  '🤪',
  '🤨',
  '🧐',
  '🤓',
  '😎',
  '🤩',
  '🥳',
  '😏',
  '😒',
  '😞',
  '😔',
  '😟',
  '😕',
  '🙁',
  '☹️',
  '😣',
  '😖',
  '😫',
  '😩',
  '🥺',
  '😢',
  '😭',
  '😤',
  '😠',
  '😡',
  '🤬',
  '🤯',
  '😳',
  '🥵',
  '🥶',
  '😱',
  '😨',
  '😰',
  '😥',
  '😓',
  '🤗',
  '🤔',
  '🤭',
  '🤫',
  '🤥',
  '😶',
  '😐',
  '😑',
  '😬',
  '🙄',
  '😯',
  '😦',
  '😧',
  '😮',
  '😲',
  '🥱',
  '😴',
  '😪',
  '😵',
  '🤐',
  '🥴',
  '🤢',
  '🤮',
  '🤧',
  '😷',
  '🤒',
  '🤕',
  '🤑',
  '🤠',
  '😈',
  '👿',
  '👹',
  '👺',
  '🤡',
  '💩',
  '👻',
  '💀',
  '☠️',
  '👽',
  '👾',
  '🤖',
  '🎃',
  '😺',
  '😸',
  '😹',
  '😻',
  '😼',
  '😽',
  '🙀',
  '😿',
  '😾',
  '🤲'
])

const isCandidate = computed(() => {
  return sessionStorage.getItem('candidateId') !== null
})

const candidatePhrases = ref([
  '您好,我对这个职位很感兴趣',
  '请问该职位还在����聘吗？',
  '我的工作经验很符合贵公司要求',
  '方便详细了解下职位要求吗？',
  '我的简历已发送,请查收',
  '请问什么时候可以安排面试？',
  '我的专业技能很匹配该岗位',
  '期待能有机会加入贵公司'
])

const bossPhrases = ref([
  '您好,感谢您对该职位的关注',
  '请问您方便发一份简历吗？',
  '您的经历很符合我们的要求',
  '请问您期望的薪资是多少？',
  '方便电话沟通一下吗？',
  '我们可以安排您来面试',
  '您的技能很匹配这个岗位',
  '期待您的加入'
])

const noteInput = ref('')
const needReminder = ref(false)
const reminderTime = ref('')
const fileInput = ref(null)

// 添加相关方法
const selectEmoji = (emoji) => {
  messageInput.value += emoji
}

const selectPhrase = (phrase) => {
  messageInput.value += phrase
}

const disabledDate = (time) => {
  return time.getTime() < Date.now()
}

const clearNote = () => {
  noteInput.value = ''
  needReminder.value = false
  reminderTime.value = ''
}

const saveNote = () => {
  if (!noteInput.value.trim() && !needReminder.value) {
    ElMessage.warning('请输入备注内容或设置提醒时间')
    return
  }

  const noteData = {
    content: noteInput.value,
    hasReminder: needReminder.value,
    reminderTime: reminderTime.value
  }

  console.log('保存的备注数据:', noteData)
  ElMessage.success('保存成功')
  clearNote()
}

const triggerFileInput = () => {
  fileInput.value.click()
}

const handleImageSelected = (event) => {
  const file = event.target.files[0]
  if (!file) return

  const isImage = file.type.startsWith('image/')
  const isLt5M = file.size / 1024 / 1024 < 5

  if (!isImage) {
    ElMessage.error('只能上传图片文件！')
    return
  }
  if (!isLt5M) {
    ElMessage.error('图片大小不能超过5MB！')
    return
  }

  // 这里可以添加发送图片的逻辑
  console.log('选择的图片:', file)
  event.target.value = ''
}
</script>







<style lang="scss" scoped>
.common-layout {
  position: relative;

  &::before {
    content: '';
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: linear-gradient(180deg, #8dc5fa 0%, #faf4f4 100%);
    z-index: -1;
  }
}

html,
body {
  margin: 0;
  padding: 0;
}
.main {
  margin: 0 auto;
  width: 1200px;
  height: 90vh;
  .searchDialogue {
    height: 36px;
    margin: 8px auto;
    background-color: transparent;
  }
  .selectDialogue {
    height: 30px;
    margin: 5px auto;
    width: 95%;
    background-color: transparent;

    ul {
      margin: 0;
      padding: 0;
      width: 100%;
      line-height: 30px;
      li {
        display: inline-block;
        text-decoration: none;
        margin: 0px 11px;
        span {
          cursor: pointer;
          background-color: #d3dce6;
          border: 2px black transparent;
          box-sizing: border-box;
          border-radius: 5px;
          color: #333333;
          font-size: 14px;
          padding: 2px 4px;
        }

        span:hover {
          background-color: #e5f8f8;
          color: #51ada7;
        }

        .gray {
          background-color: #d3dce6;
          color: #999999;
        }

        .active {
          background-color: #51ada7;
          color: white;
        }
      }
    }
  }
}
.boosList {
  width: 95%;
  margin: 5px auto;
  height: 76vh;
  overflow-y: auto;
  background-color: transparent;
  &::-webkit-scrollbar {
    width: 8px;
  }
  &::-webkit-scrollbar-thumb {
    background-color: #cbcbcb;
    border-radius: 15px;
  }

  &::-webkit-scrollbar-thumb:hover {
    background-color: #b3b2b2;
  }

  ul {
    margin: 0;
    padding: 0;

    li {
      overflow: hidden;
      background-color: #ffffff;
      display: block;
      height: 85px;
      margin: 10px;
      cursor: pointer;
      border: 1px solid #ddd;

      .friend_context {
        display: flex;
        margin: 0 auto;
        margin-top: 10px;
        width: 90%;
        height: 80%;
        background-color: #1757aa;
        .friend_img {
          width: 50px;
          height: 50px;
          margin-top: 10px;
          line-height: 0px;
          img {
            width: 50px;
            height: 50px;
          }
        }
        .friend_info {
          background-color: antiquewhite;
          height: 56px;
          margin-top: 7px;
          width: 100%;

          .friend_bom {
            display: flex;
            margin-top: 5px;

            .bom_icon {
              display: flex;
              position: relative;
              a {
                text-decoration: none;
                background-color: #d9d9d9;
                border-radius: 50%;
                &:hover {
                  background-color: #00bebd;
                }
              }
            }
            .omit_function {
              position: absolute;
              right: 10px;
            }
          }
        }
      }
    }
  }
}
.dialogue_frame {
  height: 56vh;
  background-color: #f8f8f9;
  border: 1px solid #e4e7ed;
  .baseInfo_left {
    line-height: 30px;
  }
  .baseInfo_right {
    margin: 10px;
  }
  .main_message {
    height: 97%;
    background-color: #ffffff;
    width: 92%;
    margin: 0 auto;
    overflow-y: auto;
    display: flex;
    flex-direction: column;
    border: 1px solid #e4e7ed;
    border-radius: 3px;
    padding: 10px 16px 20px 16px;

    &::-webkit-scrollbar {
      width: 8px;
    }
    &::-webkit-scrollbar-thumb {
      background-color: #cbcbcb;
      border-radius: 15px;
    }

    &::-webkit-scrollbar-thumb:hover {
      background-color: #b3b2b2;
    }

    .name {
      font-family: PingFangSC-Regular;
      font-weight: 400;
      font-size: 12px;
      color: #909399;
      margin-bottom: 6px;
    }

    .chat_message {
      padding: 6px 12px;
      background: #f8f8f9;
      border-radius: 4px;
      margin-bottom: 15px;
      word-wrap: break-word;
      font-weight: 400;
      font-size: 14px;
      color: #303133;
    }

    .message-left {
      max-width: 418px;
      align-self: flex-start;

      display: flex;
      flex-direction: column;
      .chat_message {
        background-color: #f8f8f9;
      }
      .name {
        align-self: flex-start;
      }
    }

    .message-right {
      max-width: 418px;
      align-self: flex-end;
      display: flex;
      flex-direction: column;
      .chat_message {
        background-color: #ebf3ff;
      }
      .name {
        align-self: flex-end;
      }
    }
  }
}
.baseInfo_bom {
  margin: 0 auto;
  background-color: #fff;
  border: 2px solid #ccc;
  box-sizing: border-box;
  border-radius: 5px;
  .baseSpan_left {
    margin: 10px 0;
    span {
      margin-right: 10px;
    }
  }
}

.message_box {
  background-color: #f8f8f9;
  height: 21vh;
  border-top: 1px solid #e4e7ed;

  .function_module {
    margin: 0 auto;

    background-color: #f8f8f9;

    .emo_module {
      ul {
        display: flex;
        gap: 12px;
        margin: 0;
        padding: 0;
        list-style: none;

        li {
          cursor: pointer;

          .icon-wrapper {
            width: 32px;
            height: 32px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 6px;
            transition: all 0.2s;

            &:hover {
              background-color: #e8f4f4;

              .iconfont {
                color: #15b3b3;
              }
            }

            .iconfont {
              font-size: 20px;
              color: #666;
              transition: color 0.2s;
            }
          }
        }
      }
    }

    .button_module {
      display: flex;
      align-items: center;
      height: 30px;
      padding-left: 20px;

      span {
        cursor: pointer;
        padding: 5px 12px;
        border: 1px solid #dcdfe6;
        border-radius: 4px;
        margin-right: 12px;
        margin-top: 2px;
        font-size: 13px;
        color: #606266;
        transition: all 0.3s;

        &:hover {
          color: #15b3b3;
          border-color: #15b3b3;
          background-color: #e8f4f4;
        }

        &:active {
          background-color: #d5ebeb;
        }
      }
    }

    .chat_content {
      background-color: #ffffff;
      height: 13vh;
      border: 1px solid #dcdfe6;
      border-radius: 4px;
      textarea {
        height: 100%;
        width: 100%;
        resize: none;
        padding: 0px;
        font-size: 14px;
        color: #606266;
        border: none;
        box-shadow: none;
        outline: none;
        background-color: transparent;

        &:focus {
          border: none;
          box-shadow: none;
          outline: none;
        }

        &::placeholder {
          color: #c0c4cc;
        }
      }
    }

    .chat_footer {
      display: flex;
      align-items: center;
      justify-content: flex-end;
      padding-right: 12px;
      color: #909399;
      font-size: 12px;
    }

    .msg_send {
      .el-button {
        height: 25px;
        padding: 0 16px;
        font-size: 14px;
        color: white;
        background-color: #15b3b3;
        border: none;
        border-radius: 4px;
        transition: all 0.3s;

        &:hover {
          background-color: #129999;
        }

        &:active {
          background-color: #108888;
        }
      }
    }
  }
}

.el-header,
.el-footer {
  text-align: center;
  height: 7vh;
  display: flex;
  align-items: center;
  justify-content: center;

  h1 {
    margin: 0;
    line-height: 7vh;
    color: #333;
  }
}

.el-aside {
  background-color: #d3dce6;
  color: #333;
  text-align: center;
  height: 100%;
}

.el-main {
  background-color: #e9eef3;
  color: #333;
  text-align: center;
  height: 100%;
  padding: 0;
  padding-top: 10px;
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

.resume-preview {
  display: flex;
  align-items: center;
  width: 230px;
  border-radius: 6px;
  border: 1px solid #e6e8eb;
  position: relative;
  padding: 12px;
  word-break: break-word;

  &.boss-green {
    background: linear-gradient(180deg, rgba(8, 185, 185, 0.12), #fff 60%);
    border: 1px solid rgba(21, 179, 179, 0.2);
  }

  .resume-icon {
    width: 40px;
    height: 40px;
    background: rgba(21, 179, 179, 0.08);
    border-radius: 4px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 12px;

    .el-icon {
      font-size: 24px;
      color: #15b3b3;
    }
  }

  .resume-info {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 8px;

    .resume-name {
      font-size: 14px;
      color: #303133;
      font-weight: 500;
    }

    .resume-action {
      text-align: center;

      .preview-btn {
        height: 28px;
        padding: 0 12px;
        font-size: 13px;
        background: #15b3b3;
        border: none;
        border-radius: 4px;
        color: white;

        &:hover {
          background: rgba(3, 187, 187, 0.9);
        }
      }
    }
  }
}

.message-left {
  .resume-preview {
    align-self: flex-start;
    background: #f8f8f9;
  }
}

.message-right {
  .resume-preview {
    align-self: flex-end;
    background: #ebf3ff;
  }
}

// 修改 .emoji-container 和 .phrases-container 的样式
.emoji-container {
  padding: 2px;
  max-height: 160px;
  overflow-y: auto;
  background-color: white;

  .emoji-grid {
    display: grid;
    grid-template-columns: repeat(8, 1fr);
    gap: 2px;
    background-color: white;

    .emoji-item {
      cursor: pointer;
      font-size: 16px;
      padding: 1px;
      text-align: center;
      transition: all 0.2s;
      border-radius: 4px;
      background-color: white;
      position: relative;

      &:hover {
        background-color: #f5f5f5;
        transform: scale(1.1);
        z-index: 2;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
      }

      &:active {
        background-color: #e8e8e8;
      }
    }
  }

  &::-webkit-scrollbar {
    width: 4px;
    background-color: white;
  }

  &::-webkit-scrollbar-thumb {
    background: #ddd;
    border-radius: 2px;
  }

  &::-webkit-scrollbar-track {
    background: white;
  }
}

.phrases-container {
  padding: 2px;
  max-height: 240px;
  overflow-y: auto;
  background-color: white;

  .phrases-list {
    display: flex;
    flex-direction: column;
    gap: 2px;
    background-color: white;

    .phrase-item {
      cursor: pointer;
      font-size: 12px;
      padding: 6px 10px;
      background-color: white;
      border-radius: 4px;
      transition: all 0.2s;
      position: relative;

      &:hover {
        background-color: #f5f5f5;
        z-index: 2;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
      }

      &:active {
        background-color: #e8e8e8;
      }
    }
  }

  &::-webkit-scrollbar {
    width: 4px;
    background-color: white;
  }

  &::-webkit-scrollbar-thumb {
    background: #ddd;
    border-radius: 2px;
  }

  &::-webkit-scrollbar-track {
    background: white;
  }
}

// 修改弹出层样式
:deep(.emoji-popover),
:deep(.phrases-popover) {
  padding: 2px !important;
  background-color: white !important;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15) !important;
}
</style>