import request from '@/utils/request'

export const updateApplicantCdsCommunication=(candidateId,boosId)=>{
    return request.put(`/applicantCds/updateApplicantCdsCommunication?candidateId=${candidateId}&boosId=${boosId}`)
}