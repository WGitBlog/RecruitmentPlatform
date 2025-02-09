import request from '@/utils/request'
export const getCompanyInfo=(companyId)=>{
    return request.get(`/company/${companyId}`)
}
export const updateCompanyInfo=(company)=>{
    return request.put(`/company/updateCompany`,company)
}
