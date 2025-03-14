import request from "@/utils/request"
//获取工作信息
// export const getJobInfo = (page,pageSize) =>{
//     return request.get(`/job/jobPage?page=${page}&pageSize=${pageSize}`)
// }


//job的分页查询
export const getJobInfo = (page, pageSize, workLocation = '', salaryRange = '',jobCategory='',weeklyDays=null,jobTitle='', review = null) => {
  // 构建查询参数
  const params = new URLSearchParams({
    page,
    pageSize,
  });

  // 仅在 location 和 salary 不为空时添加它们
  if (workLocation) {
    params.append('workLocation', workLocation);
  }

  if (salaryRange) {
    params.append('salaryRange', salaryRange);
  }

  if(jobCategory){
    params.append('jobCategory', jobCategory);
  }

  if(weeklyDays){
    params.append('weeklyDays', weeklyDays);
  }
  
  if(jobTitle){
    params.append('jobTitle', jobTitle); 
  }
  
  if (review) {
    params.append('review', review);
  }
  // 发送请求
  return request.get(`/job/jobPage?${params.toString()}`);
};


// 根据BossId信息获取其发布的工作信息
export const getJobsByBoosId = (boosId) => {

  return request.get(`/job/boosJobsInfo?boosId=${boosId}`)

};

export const addJobInfo = (jobForm) => {
  return request.post("/job/addJobInfo", jobForm)
}



export const updateJobReview = (jobId, review) => {
  return request.put(`/job/updateJobReview?jobId=${jobId}&review=${review}`)
}

export const updateDeleteJob = (jobId) => {
  return request.delete(`/job/updateDeleteJob?jobId=${jobId}`)
}
export const updateJob = (job) => {
  return request.post(`/job/updateJob`, job)
}