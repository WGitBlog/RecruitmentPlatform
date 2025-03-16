<template> 
  <li> 
    <!-- 使用 el-dropdown 组件 --> 
    <el-dropdown 
      trigger="hover" 
      @command="handleCommand" 
    > 
      <!-- 下拉菜单的触发元素 --> 
      <el-link :underline="false" @click="$router.push('/userInfo')">  
        <img 
          src="../../assets/a.jpg"  
          alt="" 
          :style="{ borderRadius: '50%', objectFit: 'cover' }" 
        /> 
        <span v-html="defaultUser.name"></span>  
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
</template> 
 
<script> 
export default { 
  data() { 
    return { 
      // 模拟用户信息 
      defaultUser: { 
        name: 'John Doe' 
      } 
    }; 
  }, 
  methods: { 
    // 处理下拉菜单选项的点击事件 
    handleCommand(command) { 
      switch (command) { 
        case 'personalCenter': 
          // 跳转到个人中心页面 
          this.$router.push('/personalCenter');  
          break; 
        case 'changePassword': 
          // 跳转到修改密码页面 
          this.$router.push('/changePassword');  
          break; 
        case 'logout': 
          // 处理退出登录逻辑 
          console.log(' 退出登录'); 
          break; 
        default: 
          break; 
      } 
    } 
  } 
}; 
</script> 
 
<style scoped> 
/* 可以根据需要添加一些样式 */ 
.el-dropdown { 
  cursor: pointer; 
} 
</style> 