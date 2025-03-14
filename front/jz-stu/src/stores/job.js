import { defineStore } from "pinia"
import { ref } from "vue"
export const useJobStore = defineStore("useJobStore", () => {
    //处理请求任意的数据
    const res = ref()
    //page页面地点参数
    const workLocation = ref("");
    //workLocation更新方法
    const updateWorkLocation = (newValue) => {
        workLocation.value = newValue
    }
    //weeklyDays薪资参数
    const weeklyDays = ref(null)
    //weeklyDays更新方法
    const updateWeeklyDays = (newValue) => {
        weeklyDays.value = newValue
    }
//jobCategory工作类型
    const jobCategory = ref("")

    //jobCategory更新方法
    const updateJobCategory = (newValue) => {
        jobCategory.value = newValue
    }

    //page页面薪资参数
    const salaryRange = ref("")
    //workLocation更新方法
    const updateSalaryRange = (newValue) => {
        salaryRange.value = newValue
    }
    //jobTitle工作标题参数
    const jobTitle = ref("")
    //jobTitle更新方法
    const updateJobTitle = (newValue) => {
        jobTitle.value = newValue
    }
    //page页面状态参数
    const review = ref(2)
    //workLocation更新方法
    const updateReview = (newValue) => {
        review.value = newValue
    }
    const page = ref(1) //当前页码
    const pageSize = 5 //页码大小

    //任意请求的返回数据的更新方法
    const setResData = (newValue) => {
        console.log("这是传入setResData的newValue数据:")
        console.log(newValue)
        res.value = newValue
        console.log("这是赋值之后的vuex的res数据:")
        console.log(res.value)
    }

    //返回
    return {
        res,
        page,
        pageSize,
        weeklyDays,
        workLocation,
        salaryRange,
        review,
        jobCategory,
        jobTitle,
        updateJobTitle,
        updateJobCategory,
        updateWeeklyDays,
        setResData,
        updateSalaryRange,
        updateWorkLocation,
        updateReview
    };
})