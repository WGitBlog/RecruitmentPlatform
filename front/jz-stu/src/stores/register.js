import{defineStore} from "pinia"
import { ref } from "vue"

export const useRegisterStore=defineStore("useRegisterStore",()=>{
const userRole=ref(null)
    
const active = ref(0)

const companyInfo = ref({
    companyName:null,
    location:null,
    industry:null,
    establishedYear:null,
    contactNumber:null,
    email:null,
    companyImg:null,//自己定义的
    companyScale:null,
    companyType:null

})

const boosInfo = ref({
    boosName:null,
    boosEmail:null,
    boosPhone:null,
    companyId:null,
    boosImg:null,//自己定义的
    companyPosition:null,

})


const jobInfo = ref({
 jobTitle:null,
 requiredTechnology:null,
 jobCategory:null,
 workLocation:null,
 salaryRange:null,
 educationRequirements:null,
 publishTime:null,
 boosId:null,
 jobDescription:null,
 jobDetails:null,
 weeklyDays:null,
 review:null,

})


const candidateInfo=ref({
    account:null,
    age:null,
    birthday:null,
    degreecategory:null,
    email:null,
    freshGraduate:null,
    gender:null,
    address:null,
    name:null,
    startWorkDate:null,
    tiny:null,
    weixinblur:null,
    workyears:null,
    resume:null,
    applicantId:null,
    desiredSalary:null,
    desiredCity:null,
    desiredPosition:null,
    desiredIndustry:null,
    skillsIntroduction:null

})

const  userInfo=ref({
    username: null,
    password: null,
    repassword:null,
    role:null
})



    return{
        userRole,active,boosInfo,companyInfo,jobInfo,candidateInfo,userInfo
    }
})
    //处理请求任意的数据