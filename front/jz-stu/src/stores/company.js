import { defineStore } from 'pinia'
import { ref } from 'vue'


export const useCompanyStore = defineStore('companyStore', () => {
  const companyInfo = ref({
    id:null,
    companyName:null,
    location:null,
    industry:null,
    establishedYear:null,
    contactNumber:null,
    email:null,
    companyImg:null,
    companyScale:null,
    companyMarket:null,
    companyType:null
  })

  const resumeHandleRate = ref(70)

  const fetchCompanyInfo = async () => {
    // 模拟API调用
    // companyInfo.value = await api.getCompanyInfo()
  }

  const updateCompanyInfo = async () => {
    // 模拟API调用
    // await api.updateCompanyInfo(data)
    // companyInfo.value = { ...companyInfo.value, ...data }
  }

  return {
    companyInfo,
    resumeHandleRate,
    fetchCompanyInfo,
    updateCompanyInfo
  }
}) 