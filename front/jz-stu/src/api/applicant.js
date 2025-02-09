import request from '@/utils/request'
export const updateInterested=(candidateId,jobId)=>{
    return request.put(`/applicant/updateInterested?candidateId=${candidateId}&jobId=${jobId}`);

}


export const updateApplicantCommunication=(jobId,candidateId)=>{
    return request.put(`/applicant/updateApplicantCommunication?jobId=${jobId}&candidateId=${candidateId}`)
}
