import request from '@/utils/request'

// 修正后的 getStatistics 函数
export const getStatistics = () => {
    return request.get('/user/statistics')
}