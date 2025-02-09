<template>
  <div class="job-list">
    <el-table :data="jobs" style="width: 100%">
      <el-table-column prop="jobTitle" label="职位名称" />
      <el-table-column prop="salaryRange" label="薪资范围" width="120" />
      <el-table-column prop="jobCategory" label="工作类型" width="100">
        <template #default="{ row }">
          <el-tag >
            {{ row.jobCategory }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="publishTime" label="发布时间" width="120" />
      <el-table-column label="操作" width="250">
        <template #default="{ row }">
          <!-- 审核中状态 -->
          <template v-if="row.review === 1">
            <el-button 
              size="small" 
              type="primary"
              @click="$emit('view', row.id)"
            >
              查看
            </el-button>
          </template>
          
          <!-- 已拒绝状态 -->
          <template v-else-if="row.review === 4">
            <el-button-group>
              <el-button 
                size="small" 
                type="danger"
                @click="$emit('delete', row.id)"
              >
                删除
              </el-button>
              <el-button 
                size="small" 
                type="primary"
                @click="$emit('edit', row.id)"
              >
                编辑
              </el-button>
              <el-button 
                size="small" 
                type="success"
                @click="$emit('reopen', row.id)"
              >
                重新开放
              </el-button>
            </el-button-group>
          </template>
          
          <!-- 已发布状态 -->
          <template v-else-if="row.review === 2">
            <el-button-group>
              <el-button 
                size="small" 
                type="primary"
                @click="$emit('edit', row.id)"
              >
                编辑
              </el-button>
              <el-button 
                size="small" 
                type="danger"
                @click="$emit('close', row.id)"
              >
                关闭
              </el-button>
            </el-button-group>
          </template>

          <!-- 已关闭状态 -->
          <template v-else-if="row.review === 3">
            <el-button-group>
              <el-button 
                size="small" 
                type="danger"
                @click="$emit('delete', row.id)"
              >
                删除
              </el-button>
              <el-button 
                size="small" 
                type="success"
                @click="$emit('reopen', row.id)"
              >
                重新开放
              </el-button>
            </el-button-group>
          </template>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import type { Job } from '@/interface/index'

interface Props {
  jobs: Job[]
}

const props = defineProps<Props>()

const emit = defineEmits<{
  (e: 'edit', id: number): void
  (e: 'close', id: number): void
  (e: 'reopen', id: number): void
  (e: 'delete', id: number): void
  (e: 'view', id: number): void
}>()
</script> 