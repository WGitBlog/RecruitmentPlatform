import request from '@/utils/request'
export const getCityData = () => {
    return request.get('https://yjy-oss-files.oss-cn-zhangjiakou.aliyuncs.com/tuxian/area.json');
}
