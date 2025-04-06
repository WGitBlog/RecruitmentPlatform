<template>
  <div class="job-list">
    <el-table :data="users" style="width: 100%">
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="workstatus" label="工作状态" width="120" >
        <template #default="{ row }">
          {{ statusMap[row.workstatus] || '未知状态' }}
        </template>
        </el-table-column>
      <el-table-column prop="degreecategory" label="学历" width="100"/>
      
      <el-table-column prop="birthday" label="出生日期" width="120" />
      <el-table-column label="操作" width="250">
        <template #default="{ row }">
            <el-button-group>
            <el-button 
                size="small" 
                type="primary"
                @click="$emit('view', row.resume)"
              >
                查看简历
              </el-button>

              <el-button 
                size="small" 
                type="danger"
                @click="$emit('delete', row.id)"
              >
                删除
              </el-button>
            </el-button-group>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import type { UserInfo } from '@/interface/index'

interface Props {
  users: UserInfo[]
}

const props = defineProps<Props>()
//id接收的是candidateeId
const emit = defineEmits<{
  (e: 'view', resume: string): void
  (e: 'delete', id: number): void
  
}>()
const statusMap = {
  0: '暂未选择',
  1: '在校-随时到岗',
  2: '在校-月内到岗',
  3: '在校-考虑机会',
  4: '在校-暂不考虑' 
}

</script> 