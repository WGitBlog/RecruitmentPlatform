import request from '@/utils/request'

// 发送面试邀约
export const sendInterview = (data) => {
    return request.post("/interview/sendInterview", data)
}

// 获取面试邀约
export const getInterviews = (userId) => {
    return request.get(`/interview/getInterview/${userId}`)
}

// 获取面试邀约
export const submitFeedbackInfo = (data) => {
    return request.post("/interview/submitFeedbackInfo", data)
}
// 取消面试邀约
export const cancelInterviewInfo = (interviewId) => {
    return request.put(`/interview/cancelInterviewInfo/${interviewId}`)
}