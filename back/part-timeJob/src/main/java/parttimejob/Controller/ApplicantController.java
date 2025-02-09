package parttimejob.Controller;

import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import parttimejob.Entity.*;
import parttimejob.Result.R;
import parttimejob.service.ApplicantService;
import parttimejob.service.CandidateService;
import parttimejob.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/applicant")
public class ApplicantController {
    @Autowired
    private CandidateService candidateService;
    @Autowired
    private ApplicantService applicantService;
    @Autowired
    private UserService userService;

    //根据applicantId获取applicant信息
    @GetMapping("/{applicantId}")
    public R<Applicant> getApplicationById(@PathVariable Long applicantId) {
        if (Objects.isNull(applicantId)) {
            return R.error("参数错误");
        }
        Applicant applicant = applicantService.getById(applicantId);
        return R.success(applicant);
    }

    //根据jobId添加interested中数组信息
    @PutMapping("/updateInterested")
    public R<String> updateInterestJobs(@RequestParam Integer candidateId, @RequestParam Integer jobId) {
        System.out.println("candidateId:" + candidateId + " jobId:" + jobId);
        Candidate candidate = candidateService.getById(candidateId);
        Long applicantId = candidate.getApplicantId();
        Applicant applicant = applicantService.getById(applicantId);
        // 获取感兴趣的工作并转换为列表
        String interestedJobs = (String) applicant.getInterestedJobs();
        List<Integer> collected = JSONArray.parseArray(interestedJobs).stream()
                .map(item -> (Integer) item)
                .collect(Collectors.toList());
        // 添加新的jobId
        collected.add(jobId);
        // 更新interestedJobs字段
        applicant.setInterestedJobs(collected.toString());
        applicantService.updateById(applicant);
        return R.success("成功添加感兴趣的工作");
    }

    @PutMapping("/updateApplicantCommunication")
    public R<String>  updateApplicantCdsCommunication( @RequestParam Long jobId,@RequestParam Integer candidateId){
        System.out.println("candidateId"+candidateId+"jobId"+jobId);

        //获取candidate的关系表
        Candidate candidate = candidateService.getById(candidateId);
        Long applicantId = candidate.getApplicantId();
        Applicant applicant = applicantService.getById(applicantId);

        //将boos的userId添加到Communicated数组里
        String communicatedJobs = (String)applicant.getCommunicatedJobs();
        List<Long> collect = JSONArray.parseArray(communicatedJobs).stream()
                .map(item -> ((Number) item).longValue()) // 将每个元素转换为 Long
                .collect(Collectors.toList());
        if (collect.contains(jobId)){
            return R.success("已包含无需添加");
        }else {
            collect.add(jobId);
            Collections.reverse(collect); // 倒序排列
            applicant.setCommunicatedJobs(collect.toString());
            applicantService.saveOrUpdate(applicant);
            return R.success("成功添加到communicatedJobs");
        }
    }


}
