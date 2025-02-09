import { defineStore } from 'pinia'
import { ref } from 'vue'
export const useCandidateStore = defineStore('useCandidateStore', () => {

    const resume = ref([])

    const page = ref(1) //当前页码
    const pageSize = 5 //页码大小
    const candidateInfo = ref({
        account: null,
        age: null,
        birthday: null,
        degreecategory: null,
        email: null,
        freshGraduate: null,
        gender: null,
        address: null,
        name: null,
        startWorkDate: null,
        tiny: null,
        weixinblur: null,
        workyears: null,
        resume: null,
        applicantId: null,
        desiredSalary: null,
        desiredCity: null,
        desiredPosition: null,
        desiredIndustry: null,
        skillsIntroduction: null

    })

    return {
        resume,
        page,
        pageSize,
        candidateInfo
    }
})