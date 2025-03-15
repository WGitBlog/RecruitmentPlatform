import request from '@/utils/request'

// 发送面试邀约
export const sendInterview = (data) => {
    return request.post("/interview/sendInterview",data)
}