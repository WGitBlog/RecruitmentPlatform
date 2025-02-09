import request from '@/utils/request'

// 提交职位举报
export const submitJobReport = (data) => {
    return request.post('/report/addInfo', data)
}

// 获取用户的举报列表
export const getReportList = (candidateId) => {
    return request.get(`/report/list/${candidateId}`)
}

// 获取用户的举报列表
export const getReportPage = (page, pageSize) => {
    const params = new URLSearchParams({
        page,
        pageSize,
    });

    return request.get(`/report/reportPage?${params.toString()}`)
}








export const updateReport = (id, status, feedback = '') => {
    // 构建查询参数
    const params = new URLSearchParams({
        id,
        status,
    });

    // 仅在 location 和 salary 不为空时添加它们
    if (feedback) {
        params.append('feedback', feedback);
    }

    // 发送请求
    return request.put(`/report/updateReport?${params.toString()}`);
};