package parttimejob.Controller;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import parttimejob.Dto.CandidateDto;
import parttimejob.Dto.JobDto;
import parttimejob.Dto.PageDto;
import parttimejob.Dto.RegisterDto;
import parttimejob.Dto.pageDto.CandidatePageDto;
import parttimejob.Entity.*;
import parttimejob.Result.R;
import parttimejob.Utils.minio.FileStorageService;
import parttimejob.Utils.saltMd5.PasswordTools;
import parttimejob.service.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RestController
@RequestMapping("/candidate")
@CrossOrigin("http://localhost:5173")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;
    @Autowired
    private ApplicantService applicantService;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private UserService userService;

    //根据role获取所有user+candidate信息
    @GetMapping("/candidateUPage")
    public R<Page<CandidateDto>> candidateUPage(PageDto pageDto) {
        Integer page = pageDto.getPage();
        Integer pageSize = pageDto.getPageSize();

        // 1. 创建分页对象
        Page<User> userPage = new Page<>(page, pageSize);

        // 2. 构建查询条件
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getRole, "candidate");

        // 3. 执行分页查询
        Page<User> pageUser = userService.page(userPage, queryWrapper);

        // 4. 创建结果分页对象
        Page<CandidateDto> resultPage = new Page<>();
        BeanUtils.copyProperties(pageUser, resultPage, "records");

        // 5. 转换记录
        List<CandidateDto> dtoList = pageUser.getRecords().stream().map(user -> {
            CandidateDto candidateDto = new CandidateDto();
            // 获取并复制 Candidate 信息
            Candidate candidate = candidateService.getById(user.getCandidateId());
            if (candidate != null) {
                BeanUtils.copyProperties(candidate, candidateDto);
            }
            // 设置来自 User 的字段
            candidateDto.setId(user.getId());
            candidateDto.setStatus(user.getStatus());
            return candidateDto;
        }).collect(Collectors.toList());

        // 6. 设置转换后的记录
        resultPage.setRecords(dtoList);

        return R.success(resultPage);
    }

    // 查询 candidate 的数组
    @GetMapping("/candidatePage")
    public R<Page> candidatePage(CandidatePageDto pageDto) {
        Integer page = pageDto.getPage();
        Integer pageSize = pageDto.getPageSize();
        String degreecategory = pageDto.getDegreecategory();
        String age = pageDto.getAge();
        String workyears = pageDto.getWorkyears();
        String desiredSalary = pageDto.getDesiredSalary();
        Page<Candidate> jobPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Candidate> queryWrapper = new LambdaQueryWrapper<>();

        if (degreecategory != null) {
            queryWrapper.eq(Candidate::getDegreecategory, degreecategory);
        }

        if (age != null) {
            if (age.contains("-")) {
                String[] ageRange = age.split("-");
                int minAge = Integer.parseInt(ageRange[0]);
                int maxAge = Integer.parseInt(ageRange[1]);
                queryWrapper.between(Candidate::getAge, minAge, maxAge);
            } else if (age.equals("50以上")) {
                queryWrapper.ge(Candidate::getAge, 50);
            }
        }

        if (workyears != null) {
            System.out.println(1);
            if (workyears.contains("-")) {
                String[] workYearsRange = workyears.split("-");
                int minYears = Integer.parseInt(workYearsRange[0]);
                int maxYears = Integer.parseInt(workYearsRange[1]);
                queryWrapper.between(Candidate::getWorkyears, minYears, maxYears);
                System.out.println(2);
            } else if (workyears.equals("10以上")) {
                queryWrapper.ge(Candidate::getWorkyears, 10);
                System.out.println(3);
            }else if (workyears.equals("无经验")) {
                queryWrapper.eq(Candidate::getWorkyears, 0);
                System.out.println(4);
            }else if (workyears.equals("在校应届生")) {

                queryWrapper.eq(Candidate::getWorkyears, -1);
                System.out.println(5);
            }
        }

        if (desiredSalary != null) {
           queryWrapper.eq(Candidate::getDesiredSalary,desiredSalary);
        }

        Page<Candidate> pageInfo = candidateService.page(jobPage, queryWrapper);
        return R.success(pageInfo);
    }

    @PostMapping("/uploadImg")
    public R<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String filePath;
        try {
            filePath = fileStorageService.uploadImgFile("CandidateImg", UUID
                    .randomUUID()+"-"+file.getOriginalFilename(), file.getInputStream());
            System.out.println(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 保存文件逻辑
        return R.success(filePath);
    }


    @PostMapping("/uploadResume")
    public R<String> uploadResume(@RequestParam MultipartFile file) {

        String resumeName;
        try {
            resumeName = fileStorageService.uploadCommonFile("CandidateFile", UUID
                    .randomUUID()+"-"+file.getOriginalFilename(), file.getInputStream());
            System.out.println("resumeName:"+resumeName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return R.success(resumeName);
    }


    @PutMapping("/updateResume")
    public R<String> updateResume(Long candidateId ,String resume){
        if (Objects.isNull(candidateId) && Objects.isNull(resume)){
            return R.error("传来的参数为null");
        }
        LambdaUpdateWrapper<Candidate> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Candidate::getId,candidateId);
        updateWrapper.set(Candidate::getResume,resume);
        boolean update = candidateService.update(updateWrapper);
        if (!update){
            return R.error("上传失败");
        }
        return R.success("上传成功");
    }

    @PutMapping("/deleteResume/{candidateId}")
    public R<String> deleteResume(@PathVariable Long candidateId){
        if (Objects.isNull(candidateId)){
            return R.error("传来的参数为null");
        }
        LambdaUpdateWrapper<Candidate> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Candidate::getId,candidateId);
        updateWrapper.set(Candidate::getResume,null);
        boolean update = candidateService.update(updateWrapper);
        if (!update){
            return R.error("删除简历失败");
        }
        return R.success("删除简历成功");
    }









    @PostMapping("/candidateRegister")
    public R<String> candidateRegister(@RequestBody RegisterDto registerDto){


        System.out.println(registerDto.getCandidate().toString());
        System.out.println(registerDto.getUser().toString());
        Applicant applicant = new Applicant();
        ArrayList<Long> list = new ArrayList<>();
        applicant.setCommunicatedJobs(JSON.toJSONString(list));
        applicant.setDeliveriesJobs(JSON.toJSONString(list));
        applicant.setInterestedJobs(JSON.toJSONString(list));
        applicant.setInterviewedJobs(JSON.toJSONString(list));
        applicantService.saveReturnId(applicant);

        Candidate candidate = registerDto.getCandidate();
        candidate.setApplicantId(applicant.getId());
        candidateService.saveReturnId(candidate);


        User user = registerDto.getUser();
        user.setCandidateId(candidate.getId());
        //进行密码SaltMd5加密
        user.setPassword(PasswordTools.encrypt(user.getPassword()));
        userService.saveOrUpdate(user);
        return R.success("注册成功");

    }

    //根据登录的candidateId查询整条信息
    @GetMapping("/baseInfo/{id}")
    public R<Candidate> baseInfo(@PathVariable Long id){
        LambdaQueryWrapper<Candidate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Candidate::getId,id);
        Candidate candidate = candidateService.getOne(queryWrapper);
        return  R.success(candidate);
    }

    @GetMapping("/communicated")
    public R<ArrayList<JobDto>> communicatedInfo(Long candidateId){
        //用来存储返回的数据
        ArrayList<JobDto> jobDtoArrayList = new ArrayList<>();
        //根据candidateId查询applicant表
        LambdaQueryWrapper<Candidate> candidateQueryWrapper = new LambdaQueryWrapper<>();
        candidateQueryWrapper.eq(Candidate::getId,candidateId);
        Candidate candidate = candidateService.getOne(candidateQueryWrapper);
        Long applicantId = candidate.getApplicantId();
        Applicant applicant = applicantService.getById(applicantId);
        // 将取到的 communicatedJobs 进行处理，返回的是 Object 转换成 String
        String communicatedJobs = (String) applicant.getCommunicatedJobs();
        // 将 JSON 字符串转换为 JSONArray
        JSONArray jsonArray = JSONArray.parseArray(communicatedJobs);
        // 遍历 JSONArray将communicatedJobs中的id全部取出来
        List<Integer> collected = jsonArray.stream().map(item -> (Integer) item).collect(Collectors.toList());

        //将job数据全部转换成jobDto
        for (Integer jobId: collected) {

            //根据jobId查询相对应的company和boos数据
            JobDto jobDto =candidateService.getRelatedByJobId(jobId);
            //创建JobDto对象进行转换
            //            JobDto jobDto = new JobDto();
            //            Job job = jobService.getById(jobId);
            //            BeanUtils.copyProperties(job,jobDto);
            //            //根据job中字段boosId查询boos数据
            //            Boos boos = boosService.getBoosById(job.getBoosId());
            //            //将boos相关数据填写到健全的jobDto壳子
            //            jobDto.setBoosName(boos.getBoosName());
            //            jobDto.setBoosImg(boos.getBoosImg());
            //            jobDto.setCompanyPosition(boos.getCompanyPosition());
            //            //根据boos中的companyId获取company信息
            //            Long companyId = boos.getCompanyId();
            //            //根据boosId查询company相关信息
            //            Company company = companyService.getCompanyById(companyId);
            //            //将company相关数据填写到健全的jobDto壳子
            //            jobDto.setCompanyName(company.getCompanyName());
            //            jobDto.setCompanyImg(company.getCompanyImg());
            //            jobDto.setCompanyScale(company.getCompanyScale());
            //            jobDto.setCompanyMarket(company.getCompanyMarket());
            //            jobDto.setCompanyType(company.getCompanyType());

            //将转换完成的jobDto数据放到list列表返回
            jobDtoArrayList.add(jobDto);
        }
        return  R.success(jobDtoArrayList);
    }

    //根据candidateId查询applicant表并且查询communicated_jobs
    @GetMapping("/all")
    public R<ArrayList<JobDto>> allInfo(Long candidateId){
        ArrayList<JobDto> jobDtoArrayList = new ArrayList<>();
        LambdaQueryWrapper<Candidate> candidateQueryWrapper = new LambdaQueryWrapper<>();
        candidateQueryWrapper.eq(Candidate::getId,candidateId);
        Candidate candidate = candidateService.getOne(candidateQueryWrapper);
        Long applicantId = candidate.getApplicantId();
        Applicant applicant = applicantService.getById(applicantId);
        JSONArray jsonArray1 = JSONArray.parseArray((String) applicant.getCommunicatedJobs());
        JSONArray jsonArray2 = JSONArray.parseArray((String) applicant.getDeliveriesJobs());
        JSONArray jsonArray3 = JSONArray.parseArray((String) applicant.getInterviewedJobs());
        JSONArray jsonArray4 = JSONArray.parseArray((String) applicant.getInterestedJobs());

        // 合并所有的 List<Integer>，去重并简化代码
        List<Integer> collected = Stream.of(jsonArray1, jsonArray2, jsonArray3, jsonArray4)
                .flatMap(jsonArray -> jsonArray.stream().map(item -> (Integer) item))
                .distinct()
                .collect(Collectors.toList());
        for (Integer jobId: collected) {
            JobDto jobDto =candidateService.getRelatedByJobId(jobId);
            jobDtoArrayList.add(jobDto);
        }
        return  R.success(jobDtoArrayList);
    }

    //根据candidateId查询applicant表并且查询deliveries_jobs
    @GetMapping("/deliveries")
    public R<ArrayList<JobDto>> deliveriesInfo(Long candidateId){
        //用来存储返回的数据
        ArrayList<JobDto> jobDtoArrayList = new ArrayList<>();
        //根据candidateId查询applicant表
        LambdaQueryWrapper<Candidate> candidateQueryWrapper = new LambdaQueryWrapper<>();
        candidateQueryWrapper.eq(Candidate::getId,candidateId);
        Candidate candidate = candidateService.getOne(candidateQueryWrapper);
        Long applicantId = candidate.getApplicantId();
        Applicant applicant = applicantService.getById(applicantId);
        // 将取到的 communicatedJobs 进行处理，返回的是 Object 转换成 String
        String deliveriesJobs = (String) applicant.getDeliveriesJobs();
        // 将 JSON 字符串转换为 JSONArray
        JSONArray jsonArray = JSONArray.parseArray(deliveriesJobs);
        // 遍历 JSONArray将communicatedJobs中的id全部取出来
        List<Integer> collected = jsonArray.stream().map(item -> (Integer) item).collect(Collectors.toList());

        //将job数据全部转换成jobDto
        for (Integer jobId: collected) {

            //根据jobId查询相对应的company和boos数据
            JobDto jobDto =candidateService.getRelatedByJobId(jobId);
            //将转换完成的jobDto数据放到list列表返回
            jobDtoArrayList.add(jobDto);
        }
        return  R.success(jobDtoArrayList);
    }

    //根据candidateId查询applicant表并且查询interests_jobs
    @GetMapping("/interests")
    public R<ArrayList<JobDto>> interestsInfo(Long candidateId){
        //用来存储返回的数据
        ArrayList<JobDto> jobDtoArrayList = new ArrayList<>();
        //根据candidateId查询applicant表
        LambdaQueryWrapper<Candidate> candidateQueryWrapper = new LambdaQueryWrapper<>();
        candidateQueryWrapper.eq(Candidate::getId,candidateId);
        Candidate candidate = candidateService.getOne(candidateQueryWrapper);
        Long applicantId = candidate.getApplicantId();
        Applicant applicant = applicantService.getById(applicantId);
        // 将取到的 interestsJobs 进行处理，返回的是 Object 转换成 String
        String interestsJobs = (String) applicant.getInterestedJobs();
        // 将 JSON 字符串转换为 JSONArray
        JSONArray jsonArray = JSONArray.parseArray(interestsJobs);
        // 遍历 JSONArray将interestsJobs中的id全部取出来
        List<Integer> collected = jsonArray.stream().map(item -> (Integer) item).collect(Collectors.toList());

        //将job数据全部转换成jobDto
        for (Integer jobId: collected) {

            //根据jobId查询相对应的company和boos数据
            JobDto jobDto =candidateService.getRelatedByJobId(jobId);
            //将转换完成的jobDto数据放到list列表返回
            jobDtoArrayList.add(jobDto);
        }
        return  R.success(jobDtoArrayList);
    }

    //根据candidateId查询applicant表并且查询interests_jobs
    @GetMapping("/interviews")
    public R<ArrayList<JobDto>> interviewsInfo(Long candidateId){
        //用来存储返回的数据
        ArrayList<JobDto> jobDtoArrayList = new ArrayList<>();
        //根据candidateId查询applicant表
        LambdaQueryWrapper<Candidate> candidateQueryWrapper = new LambdaQueryWrapper<>();
        candidateQueryWrapper.eq(Candidate::getId,candidateId);
        Candidate candidate = candidateService.getOne(candidateQueryWrapper);
        Long applicantId = candidate.getApplicantId();
        Applicant applicant = applicantService.getById(applicantId);
        // 将取到的 interviewsJobs 进行处理，返回的是 Object 转换成 String
        String interviewsJobs = (String) applicant.getInterviewedJobs();

        // 将 JSON 字符串转换为 JSONArray
        JSONArray jsonArray = JSONArray.parseArray(interviewsJobs);
        // 遍历 JSONArray将interviewsJobs中的id全部取出来
        List<Integer> collected = jsonArray.stream().map(item -> (Integer) item).collect(Collectors.toList());

        //将job数据全部转换成jobDto
        for (Integer jobId: collected) {

            //根据jobId查询相对应的company和boos数据
            JobDto jobDto =candidateService.getRelatedByJobId(jobId);
            jobDtoArrayList.add(jobDto);
        }
        return  R.success(jobDtoArrayList);
    }

    @PostMapping("/changedetails")
    public R changedetails(@RequestBody Candidate candidate ){
//        Date birthday = candidate.getBirthday();
//        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
//        String format = sdf.format(birthday);
//        candidate.setBirthday();
        if (Objects.isNull(candidate)){
            return R.error("传入参数错误");
        }
        System.out.println(candidate);
        Boolean changed=candidateService.changeDetails(candidate);
        return R.success(changed);
    }


}
