package parttimejob.Controller;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import parttimejob.Entity.ApplicantCds;
import parttimejob.Entity.Boos;
import parttimejob.Entity.User;
import parttimejob.Result.R;
import parttimejob.service.ApplicantCdsService;
import parttimejob.service.BoosService;
import parttimejob.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/applicantCds")
public class ApplicantCdsController {
    @Autowired
    private BoosService boosService;

    @Autowired
    private UserService userService;
    @Autowired
    private ApplicantCdsService applicantCdsService;

    @PutMapping("/updateApplicantCdsCommunication")
    public R<String>  updateApplicantCdsCommunication(@RequestParam Integer candidateId, @RequestParam Integer boosId){
        System.out.println("candidateId"+candidateId+"boosId"+boosId);

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getCandidateId,candidateId);
        User user = userService.getOne(queryWrapper);
        Long userId = user.getId();


        Boos boos = boosService.getById(boosId);
        Long applicantCdsId = boos.getApplicantCdsId();
        ApplicantCds applicantCds = applicantCdsService.getById(applicantCdsId);

        String communicatedCandidates = (String)applicantCds.getCommunicatedCandidates();
        List<Long> collect = JSONArray.parseArray(communicatedCandidates).stream()
                .map(item -> ((Number) item).longValue()) // 将每个元素转换为 Long
                .collect(Collectors.toList());
        if (collect.contains(userId)){
            return R.success("已包含无需添加");
        }else {
            collect.add(userId);
            Collections.reverse(collect); // 倒序排列
            applicantCds.setCommunicatedCandidates(collect.toString());
            applicantCdsService.saveOrUpdate(applicantCds);
            return R.success("成功添加到communicatedCandidates");
        }
    }




}
