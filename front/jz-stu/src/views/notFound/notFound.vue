<template>
  <div class="container">


    
    <div class="icon-list">
      <ul>
        <li>
          <el-popover placement="top" trigger="click" :width="240" popper-class="emoji-popover">
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
          <el-popover placement="top" trigger="click" :width="240" popper-class="phrases-popover">
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
          <el-popover placement="top" trigger="click" :width="240" popper-class="notes-popover">
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
                <el-button type="primary" size="small" @click="saveNote">保存</el-button>
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
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'

const emojiList = ref([
  '😀',
  '😃',
  '😄',
  '😁',
  '😆',
  '😅',
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
  '🤤',
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
  return localStorage.getItem('candidateId') !== null
})

const candidatePhrases = ref([
  '您好,我对这个职位很感兴趣',
  '请问该职位还在���聘吗？',
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

const selectEmoji = (emoji) => {
  console.log('选择的表情:', emoji)
}

const selectPhrase = (phrase) => {
  console.log('选择的快捷语:', phrase)
}

const noteInput = ref('')
const needReminder = ref(false)
const reminderTime = ref('')

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
    ElMessage.warning('请输入备注内容或设���提醒��间')
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

const fileInput = ref(null)
const imageUrl = ref('')
const imageFile = ref(null)

// 触发文件选择
const triggerFileInput = () => {
  fileInput.value.click()
}

// 处理选中的图片
const handleImageSelected = (event) => {
  const file = event.target.files[0]
  if (!file) return

  // 验证文件类型和大小
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

  // 创建预览URL
  imageFile.value = file
  imageUrl.value = URL.createObjectURL(file)
}
</script>

<style lang="scss" scoped>
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
}

.icon-list {
  ul {
    display: flex;
    gap: 16px;
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
        border-radius: 4px;
        transition: all 0.2s;
        cursor: pointer;

        &:hover {
          background-color: #f5f5f5;

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
}

.notes-container {
  padding: 12px;

  .notes-header {
    font-size: 14px;
    font-weight: 500;
    margin-bottom: 12px;
    color: #303133;
  }

  .reminder-section {
    margin: 12px 0;
    display: flex;
    flex-direction: column;
    gap: 8px;

    .el-date-picker {
      width: 100%;
    }
  }

  .notes-footer {
    display: flex;
    justify-content: flex-end;
    gap: 8px;
    margin-top: 12px;
  }
}

:deep(.emoji-popover),
:deep(.phrases-popover),
:deep(.notes-popover) {
  padding: 2px !important;
  background-color: white !important;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15) !important;
}

// 确保滚动条样式正确
.emoji-container,
.phrases-container {
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

.image-preview-trigger {
  position: fixed;
  bottom: 60px;
  right: 20px;
  background: white;
  border-radius: 4px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 8px;

  .preview-thumbnail {
    width: 100px;
    height: 100px;
    object-fit: cover;
    border-radius: 4px;
    display: block;
  }

  .preview-actions {
    display: flex;
    gap: 8px;
    margin-top: 8px;
    justify-content: center;
  }
}

.image-preview-container {
  .preview-image {
    width: 100%;
    max-height: 300px;
    object-fit: contain;
    border-radius: 4px;
  }
}

:deep(.image-preview-popover) {
  padding: 8px !important;
  background-color: white !important;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15) !important;
}
</style>
