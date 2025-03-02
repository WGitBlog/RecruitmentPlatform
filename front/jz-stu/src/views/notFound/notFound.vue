<template>
  <div id="app">
    <!-- 显示用户增长趋势的容器 -->
    <div class="first_main" style="width: 600px; height: 400px"></div>
    <!-- 显示职位分类统计的容器 -->
    <div class="second_main" style="width: 600px; height: 400px; margin-top: 20px"></div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import * as echarts from 'echarts'
import axios from 'axios'





const userGrowthData = ref([]) // 用于存储用户增长数据
const jobCategoryData = ref([]) // 用于存储职位分类数据

onMounted(async () => {

  // 获取用户增长数据
  try {
    const response = await axios.get('你的用户增长API地址') // 替换为你的用户增长 API 地址
    userGrowthData.value = response.data // 假设返回的数据是一个数组
  } catch (error) {
    console.error('获取用户增长数据失败:', error)
  }

  // 获取职位分类数据
  try {
    const response = await axios.get('你的职位分类API地址') // 替换为你的职位分类 API 地址
    jobCategoryData.value = response.data // 假设返回的数据是一个数组
  } catch (error) {
    console.error('获取职位分类数据失败:', error)
  }


})
</script>
