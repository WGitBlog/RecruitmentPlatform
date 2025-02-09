<template>
  <el-breadcrumb class="breadcrumb">
    <el-breadcrumb-item v-for="(item, index) in breadcrumbData" :key="index" :to="item.path">
      {{ item.meta.title }}
    </el-breadcrumb-item>
  </el-breadcrumb>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const breadcrumbData = ref([])

// 监听路由变化，更新面包屑数据
watch(
  () => route.path,
  () => {
    breadcrumbData.value = route.matched.filter(item => item.meta && item.meta.title)
  },
  {
    immediate: true
  }
)
</script>

<style scoped>
.breadcrumb {
  margin-bottom: 15px;
  padding: 8px 15px;
  background-color: #fff;
  border-radius: 4px;
}
</style> 