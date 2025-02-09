<template>
  <el-container class="admin-layout">
    <!-- 侧边栏 -->
    <el-aside :width="isCollapse ? '64px' : '240px'" class="sidebar-container">
      <div class="logo-container">
        <div class="logo-text">JZ</div>
        <span class="title" v-show="!isCollapse">兼职平台管理系统</span>
      </div>

      <el-scrollbar>
        <el-menu
          :default-active="activeMenu"
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
          :collapse="isCollapse"
          :unique-opened="true"
          @select="handleMenuSelect"
        >
          <el-menu-item index="/admin/dashboard">
            <el-icon><DataLine /></el-icon>
            <template #title>
              <span>控制台</span>
            </template>
          </el-menu-item>

          <el-sub-menu index="user">
            <template #title>
              <el-icon><User /></el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/admin/candidateList">
              <el-icon><User /></el-icon>员工列表
            </el-menu-item>
            <el-menu-item index="/admin/bossList">
              <el-icon><Briefcase /></el-icon>企业列表
            </el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="job">
            <template #title>
              <el-icon><Briefcase /></el-icon>
              <span>职位管理</span>
            </template>
            <el-menu-item index="/admin/jobAudit">
              <el-icon><Check /></el-icon>职位审核
              <el-badge value="12" class="item" type="danger" />
            </el-menu-item>
            <el-menu-item index="/admin/jobList">
              <el-icon><Document /></el-icon>职位列表
            </el-menu-item>
          </el-sub-menu>

          <el-menu-item index="/admin/category">
            <el-icon><Collection /></el-icon>
            <template #title>
              <span>分类管理</span>
            </template>
          </el-menu-item>

          <el-menu-item index="/admin/report">
            <el-icon><Warning /></el-icon>
            <template #title>
              <span>举报处理</span>
              <el-badge value="5" class="item" type="danger" />
            </template>
          </el-menu-item>

          <el-menu-item index="/admin/system">
            <el-icon><Setting /></el-icon>
            <template #title>
              <span>系统设置</span>
            </template>
          </el-menu-item>
        </el-menu>
      </el-scrollbar>
    </el-aside>

    <!-- 主要内容区 -->
    <el-container class="main-container">
      <el-header height="60px">
        <div class="header-left">
          <el-icon class="collapse-btn" @click="toggleSidebar">
            <Fold v-if="!isCollapse" />
            <Expand v-else />
          </el-icon>
          <breadcrumb />
        </div>

        <div class="header-right">
          <!-- 通知中心 -->
          <el-popover placement="bottom" :width="300" trigger="click">
            <template #reference>
              <el-badge :value="4" class="notice-badge">
                <el-icon class="notice-icon"><Bell /></el-icon>
              </el-badge>
            </template>
            <div class="notification-panel">
              <div class="notification-header">
                <span>通知中心</span>
                <el-link type="primary" :underline="false">全部标记为已读</el-link>
              </div>
              <el-scrollbar height="300px">
                <div v-for="i in 4" :key="i" class="notification-item">
                  <el-icon class="notification-icon"><Message /></el-icon>
                  <div class="notification-content">
                    <div class="notification-title">新的职位审核请求</div>
                    <div class="notification-time">10分钟前</div>
                  </div>
                </div>
              </el-scrollbar>
            </div>
          </el-popover>

          <!-- 用户信息 -->
          <el-dropdown trigger="click">
            <div class="avatar-container">
              <el-avatar :size="32" :src="adminInfo.avatar" />
              <div class="user-info">
                <span class="username">{{ adminInfo.name }}</span>
                <span class="role">超级管理员</span>
              </div>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>
                  <el-icon><User /></el-icon>个人信息
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-icon><Lock /></el-icon>修改密码
                </el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout">
                  <el-icon><SwitchButton /></el-icon>退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main>
        <router-view v-slot="{ Component }">
          <transition name="fade-transform" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import {
  DataLine,
  User,
  Briefcase,
  Collection,
  Warning,
  Setting,
  Fold,
  Expand,
  Bell,
  Message,
  Lock,
  List,
  Check,
  Document,
  SwitchButton
} from '@element-plus/icons-vue'
import Breadcrumb from '@/components/layout/Breadcrumb.vue'
import useWebSocket from '@/utils/websocket.js'

















// 等待处理管理员接收实时消息
const ws = useWebSocket((e) => {
  // 解析 e.data
  const messageData = JSON.parse(e.data) // 解 JSON 字符串为对象
  console.log('message数据:', messageData)
})









































const route = useRoute()
const router = useRouter()
const isCollapse = ref(false)
const adminInfo = ref({
  name: 'Admin User',
  avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
  role: '超级管理员'
})

// 当前激活的菜单
const activeMenu = computed(() => route.path)

// 切换侧边栏
const toggleSidebar = () => {
  isCollapse.value = !isCollapse.value
}

// 处理退出登录
const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    localStorage.removeItem('adminToken')
    localStorage.removeItem('adminInfo')
    router.push('/adminLogin')
  })
}

// 添加菜单点击处理函数
const handleMenuSelect = (index) => {
  router.push(index)
}
</script>

<style scoped lang="scss">
.admin-layout {
  height: 100vh;
}

.sidebar-container {
  transition: width 0.3s;
  background-color: #304156;
  overflow: hidden;

  .logo-container {
    height: 60px;
    padding: 10px;
    display: flex;
    align-items: center;
    background: #2b2f3a;
    overflow: hidden;

    .logo-text {
      width: 32px;
      height: 32px;
      background: #1890ff;
      border-radius: 4px;
      display: flex;
      align-items: center;
      justify-content: center;
      color: white;
      font-weight: bold;
      font-size: 16px;
      margin-right: 12px;
    }

    .title {
      color: #fff;
      font-size: 16px;
      font-weight: 600;
      white-space: nowrap;
      opacity: 1;
      transition: opacity 0.3s;
    }
  }

  .el-menu {
    border: none;

    .el-menu-item,
    .el-sub-menu__title {
      &:hover {
        background-color: #263445;
      }
    }

    .is-active {
      background-color: #263445;
    }
  }
}

.main-container {
  .el-header {
    background-color: #fff;
    box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 20px;

    .header-left {
      display: flex;
      align-items: center;

      .collapse-btn {
        font-size: 20px;
        cursor: pointer;
        margin-right: 20px;

        &:hover {
          color: #409eff;
        }
      }
    }

    .header-right {
      display: flex;
      align-items: center;
      gap: 20px;

      .notice-badge {
        cursor: pointer;

        .notice-icon {
          font-size: 20px;
          color: #666;

          &:hover {
            color: #409eff;
          }
        }
      }

      .avatar-container {
        display: flex;
        align-items: center;
        cursor: pointer;
        padding: 5px;
        border-radius: 4px;

        &:hover {
          background: #f6f6f6;
        }

        .user-info {
          margin-left: 8px;

          .username {
            font-size: 14px;
            color: #333;
            display: block;
          }

          .role {
            font-size: 12px;
            color: #999;
          }
        }
      }
    }
  }

  .el-main {
    background-color: #f0f2f5;
    padding: 20px;
  }
}

.notification-panel {
  .notification-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px;
    border-bottom: 1px solid #eee;
  }

  .notification-item {
    display: flex;
    align-items: center;
    padding: 10px;
    cursor: pointer;

    &:hover {
      background: #f5f7fa;
    }

    .notification-icon {
      font-size: 20px;
      color: #409eff;
      margin-right: 10px;
    }

    .notification-content {
      flex: 1;

      .notification-title {
        font-size: 14px;
        color: #333;
        margin-bottom: 4px;
      }

      .notification-time {
        font-size: 12px;
        color: #999;
      }
    }
  }
}

// 过渡动画
.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all 0.5s;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
</style>
