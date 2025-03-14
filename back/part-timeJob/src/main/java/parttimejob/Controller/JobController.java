package parttimejob.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import parttimejob.Dto.JobDto;
import parttimejob.Dto.pageDto.JobPageDto;
import parttimejob.Entity.Boos;
import parttimejob.Entity.Company;
import parttimejob.Entity.Job;
import parttimejob.Result.R;
import parttimejob.service.BoosService;
import parttimejob.service.CompanyService;
import parttimejob.service.JobService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Slf4j
@RestController
@RequestMapping("/job")
@CrossOrigin("http://localhost:5173")
public class JobController {
    @Autowired
    private JobService jobService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private BoosService boosService;
    //job工作查询
    @GetMapping("/jobPage")
    public R<Page<JobDto>> jobPage(JobPageDto pageDto){
        System.out.println("pageDto:"+pageDto);
        Integer page = pageDto.getPage();
        Integer pageSize = pageDto.getPageSize();
        String workLocation = pageDto.getWorkLocation();
        String salaryRange = pageDto.getSalaryRange();
        String jobCategory = pageDto.getJobCategory();
        Integer weeklyDays = pageDto.getWeeklyDays();
        String jobTitle = pageDto.getJobTitle();
        Integer review = pageDto.getReview();


        Page<Job> jobPage = new Page<>(page,pageSize);
        //创建字段健全的page
        Page<JobDto> JobDtoPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Job> queryWrapper = new LambdaQueryWrapper<>();


        //默认返回的是else中的review=2--审核通过的job数据
        if (review!=null){
            queryWrapper.eq(Job::getReview,review);
        }
        if (workLocation!=null){
            queryWrapper.eq(Job::getWorkLocation,workLocation);
        }
        if (salaryRange!=null){
            queryWrapper.eq(Job::getSalaryRange,salaryRange);
        }
        if (jobCategory!=null){
            queryWrapper.eq(Job::getJobCategory,jobCategory);
        }
        if (weeklyDays!=null){
            queryWrapper.eq(Job::getWeeklyDays,weeklyDays);
        }
        if (jobTitle!=null){
            queryWrapper.like(Job::getJobTitle,jobTitle);
        }
        Page<Job> pageInfo = jobService.page(jobPage, queryWrapper);
        //除了查出来的数据其他都需要拷贝进去
        BeanUtils.copyProperties(jobPage,JobDtoPage,"records");
        //获取不健全的数据
        List<Job> records = pageInfo.getRecords();
        //向page页中records字段进行补全
        List<JobDto> JobDtoRecords = records.stream().map((item) -> {
            //job相当与records  jobDto健全的records--创建健全的jobDto壳子
            JobDto jobDto = new JobDto();
            //将records 的数据拷贝到健全的jobDto
            BeanUtils.copyProperties(item, jobDto);
            //获取数据中的boosId
            Long boosId = item.getBoosId();
            //根据id查询boos数据
            Boos boos = boosService.getBoosById(boosId);
            //将boos相关数据填写到健全的jobDto壳子
            jobDto.setBoosName(boos.getBoosName());
            jobDto.setBoosImg(boos.getBoosImg());
            jobDto.setCompanyPosition(boos.getCompanyPosition());
            //根据boos中的companyId获取company信息
            Long companyId = boos.getCompanyId();
            //根据boosId查询company相关信息
            Company company = companyService.getCompanyById(companyId);
            //将company相关数据填写到健全的jobDto壳子
            jobDto.setCompanyId(company.getId());
            jobDto.setCompanyName(company.getCompanyName());
            jobDto.setCompanyImg(company.getCompanyImg());
            jobDto.setCompanyScale(company.getCompanyScale());
            jobDto.setCompanyMarket(company.getCompanyMarket());
            jobDto.setCompanyType(company.getCompanyType());
            //将健全的数据返回
            return jobDto;
        }).collect(Collectors.toList());
        //将健全的数据放进健全的JobDto壳子里
        JobDtoPage.setRecords(JobDtoRecords);
        //TODO:在登录时已经存储，但是在Job里拿不到
        //System.out.println( BaseThread.getThreadLocal());
        return R.success(JobDtoPage);
    }
    @GetMapping("/boosJobsInfo")
    public R<List<Job>> getJobsByBoosId(Long boosId){
        if (Objects.isNull(boosId)){
            return R.error("boosId为空");
        }
        LambdaQueryWrapper<Job> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Job::getBoosId,boosId).orderByDesc(Job::getPublishTime);
        List<Job> list = jobService.list(queryWrapper);

        return R.success(list);
    }



    @PostMapping("/addJobInfo")
    public R<String> addJobInfo(@RequestBody Job job){
        if (Objects.isNull(job)){
            return R.error("job为空");
        }
        jobService.save(job);
        return R.success("Job添加成功");
    }



    @PostMapping("/updateJob")
    public R<String> updateJobInfo(@RequestBody Job job){
        if (Objects.isNull(job)){
            return R.error("job为空");
        }
        jobService.saveOrUpdate(job);
        return R.success("Job更新成功");
    }


    @PutMapping("/updateJobReview")
    public R<String> updateCloseJobReview(@RequestParam Long jobId,@RequestParam Integer review) {
        if (Objects.isNull(jobId)&&Objects.isNull(review)) {
            return R.error("jobId为空");
        }
        LambdaUpdateWrapper<Job> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Job::getId, jobId)
                .set(Job::getReview, review);  // 设置为关闭状态

        boolean success = jobService.update(updateWrapper);
        if (success) {
            return R.success("关闭工作成功");
        } else {
            return R.error("关闭工作失败");
        }
    }
    @DeleteMapping("/updateDeleteJob")
    public R<String> updateDeleteJob(@RequestParam Long jobId) {
        if (Objects.isNull(jobId)){
            return R.error("jobId为空");
        }
        boolean success = jobService.removeById(jobId);
        if (success) {
            return R.success("删除工作成功");
        } else {
            return R.error("删除工作失败");
        }
    }



}






