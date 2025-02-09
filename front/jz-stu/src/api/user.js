import request from "@/utils/request"
//用户登录
export const userLogin = (username, password) => {
    return request.post('/user/login', { username, password })
}
//根据BoosId获取User信息
export const getUserByBoosId = (boosId) => {
    return request.get(`/user/boosInfo/boos/${boosId}`)
}

//根据CandidateId获取User信息
export const getUserByCandidateId = (candidateId) => {
    return request.get(`/user/boosInfo/candidate/${candidateId}`)
}

export const updateUserStatus = (userId) => {
    return request.put(`/user/updateStatus/${userId}`)
}

