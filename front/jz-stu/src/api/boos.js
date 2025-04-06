import request from '@/utils/request'
//根据获取Boos用户信息
export const getBoosInfo = (boosId) => {
    return request.get(`/boos/baseInfo/${boosId}`)
}


//根据boosId获取用户关联的所有数据
export const getAllCds = (boosId) => {
    return request.get("/boos/all", {
        params: {
            boosId
        }
    });
}




//根据candidateId获取用户关联的communicated数据
export const getCommunicatedCds = (boosId) => {
    return request.get("/boos/communicated", {
        params: {
            boosId
        }
    });
}

//根据candidateId获取用户关联的deliveries数据
export const getDeliveriesCds = (boosId) => {
    return request.get("/boos/deliveries", {
        params: {
            boosId
        }
    });
}

//根据candidateId获取用户关联的interests数据
export const getInterestsCds = (boosId) => {
    return request.get("/boos/interests", {
        params: {
            boosId
        }
    });
}

//根据candidateId获取用户关联的interviews数据
export const getInterviewsCds = (boosId) => {
    return request.get("/boos/interviews", {
        params: {
            boosId
        }
    });
}



export const boosRegister = (companyInfo, boosInfo, jobInfo, userInfo) => {
    return request.post('/boos/boosRegister', {
        company: companyInfo,
        boos: boosInfo,
        job: jobInfo,
        user: userInfo
    })
}

//根据用户类型获取Users信息
export const getboosUPage = (page, pageSize) => {
    const params = new URLSearchParams({
        page,
        pageSize
    });
    return request.get(`/boos/boosUPage?${params.toString()}`)
}

//更新boos信息
export const updateBoosInfo=(boos)=>{
    return request.put(`/boos/updateBoosInfo`,boos)
}
//更新boos信息中的收藏列表
export const updateDelResume=(boosId,delUserId)=>{
    return request.delete(`/boos/delCollectResume?boosId=${boosId}&delUserId=${delUserId}`)
}



//收藏简历功能
export const collectResumes=(boosId,collectId)=>{
    return request.post(`/boos/collectResume?boosId=${boosId}&collectId=${collectId}`)
}