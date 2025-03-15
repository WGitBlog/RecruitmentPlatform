package parttimejob.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import parttimejob.Dto.Common.Step;
import parttimejob.Entity.Interview;
import parttimejob.Result.R;
import parttimejob.service.InterviewService;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/interview")
public class InterviewController {
    @Autowired
    private InterviewService interviewService;


    @PostMapping("/sendInterview")
    public R<String> sendInterview(@RequestBody Interview interview){
        System.out.println("interview:"+interview);
        List<Step> steps = interview.getSteps();
        System.out.println("steps:"+steps);

        return null;
    }











}
