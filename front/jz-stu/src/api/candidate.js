import request from '@/utils/request'
//根据candidateId获取Candidate用户信息
// export const getCandidateInfo = (candidateId) => {
//     return request.get('/candidate/baseInfo', { params: { candidateId } });
// }
export const getCandidateInfo = (candidateId) => {
    return request.get(`/candidate/baseInfo/${candidateId}`)
}

//根据candidateId获取用户关联的所有数据
export const getAllJobs = (candidateId) => {
    return request.get('/candidate/all', {
        params: {
            candidateId
        }
    })
}

//根据candidateId获取用户关联的communicated数据
export const getCommunicatedJobs = (candidateId) => {
    return request.get('/candidate/communicated', {
        params: {
            candidateId
        }
    })
}

//根据candidateId获取用户关联的deliveries数据
export const getDeliveriesJobs = (candidateId) => {
    return request.get('/candidate/deliveries', {
        params: {
            candidateId
        }
    })
}

//根据candidateId获取用户关联的interests数据
export const getInterestsJobs = (candidateId) => {
    return request.get('/candidate/interests', {
        params: {
            candidateId
        }
    })
}

//根据candidateId获取用户关联的interviews数据
export const getInterviewsJobs = (candidateId) => {
    return request.get('/candidate/interviews', {
        params: {
            candidateId
        }
    })
}

export const changeUserdetails = (ruleForm) => {
    return request.post('/candidate/changedetails', ruleForm)
}

//candidate的分页查询
export const getCandidatePage = (
    page,
    pageSize,
    degreecategory = '',
    age = '',
    workyears = '',
    desiredSalary = ''
) => {
    // 构建查询参数
    const params = new URLSearchParams({
        page,
        pageSize
    })
    if (degreecategory) {
        params.append('degreecategory', degreecategory)
    }
    if (age) {
        params.append('age', age)
    }
    if (workyears) {
        params.append('workyears', workyears)
    }
    if (desiredSalary) {
        params.append('desiredSalary', desiredSalary)
    }

    // 发送请求
    return request.get(`/candidate/candidatePage?${params.toString()}`)
}

export const candidateRegister = (candidateInfo, userInfo) => {
    return request.post('/candidate/candidateRegister', {
        candidate: candidateInfo,
        user: userInfo
    })
}

//根据用户类型获取Users信息
export const getcandidateUPage = (page, pageSize) => {
        const params = new URLSearchParams({
            page,
            pageSize
        })
        return request.get(`/candidate/candidateUPage?${params.toString()}`)
    }
    //更新或者重新上传简历
export const updateResume = (candidateId, resume) => {
        return request.put(`/candidate/updateResume?candidateId=${candidateId}&resume=${resume}`)
    }
    //删除简历
export const deleteResume = (candidateId) => {
    return request.put(`/candidate/deleteResume/${candidateId}`)
}