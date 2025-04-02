package parttimejob.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import parttimejob.Dto.BoosDto;
import parttimejob.Dto.PageDto;
import parttimejob.Dto.RegisterDto;
import parttimejob.Entity.*;
import parttimejob.Result.R;

import parttimejob.Utils.minio.FileStorageService;
import parttimejob.Utils.saltMd5.PasswordTools;
import parttimejob.service.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RestController
@RequestMapping("/boos")
@CrossOrigin("http://localhost:5173")
public class BoosController {
    @Autowired
    private UserService userService;
    @Autowired
    private BoosService boosService;
    @Autowired
    private JobService jobService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private CandidateService candidateService;
    @Autowired
    private ApplicantCdsService applicantCdsService;
    @Autowired
    private FileStorageService fileStorageService;


    //根据role获取所有user+boos信息
    @GetMapping("/boosUPage")
    public R<Page<BoosDto>> boosUPage(PageDto pageDto) {
        Integer page = pageDto.getPage();
        Integer pageSize = pageDto.getPageSize();

        // 1. 创建分页对象
        Page<User> userPage = new Page<>(page, pageSize);

        // 2. 构建查询条件
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getRole, "boos");

        // 3. 执行分页查询
        Page<User> pageUser = userService.page(userPage, queryWrapper);

        // 4. 创建结果分页对象
        Page<BoosDto> resultPage = new Page<>();
        BeanUtils.copyProperties(pageUser, resultPage, "records");

        // 5. 转换记录
        List<BoosDto> dtoList = pageUser.getRecords().stream().map(user -> {
            BoosDto boosDto = new BoosDto();
            // 获取并复制 Boos 信息
            Boos boos = boosService.getById(user.getBoosId());
            if (boos != null) {
                BeanUtils.copyProperties(boos, boosDto);
            }
            // 设置来自 User 的字段
            boosDto.setId(user.getId());
            boosDto.setStatus(user.getStatus());
            return boosDto;
        }).collect(Collectors.toList());

        // 6. 设置转换后的记录
        resultPage.setRecords(dtoList);

        return R.success(resultPage);
    }


    @PostMapping("/boosRegister")
    public R<String> boosRegister(@RequestBody RegisterDto registerDto) {
        Company company = registerDto.getCompany();
        //存储company并返回companyId
        companyService.saveReturnId(company);//这行会把上面的添加自增ID
        ApplicantCds applicantCds = new ApplicantCds();
        ArrayList<Long> list = new ArrayList<>();
        applicantCds.setCommunicatedCandidates(JSON.toJSONString(list));
        applicantCds.setDeliveriesCandidates(JSON.toJSONString(list));
        applicantCds.setInterestedCandidates(JSON.toJSONString(list));
        applicantCds.setInterviewedCandidates(JSON.toJSONString(list));
        applicantCdsService.saveReturnId(applicantCds);
        Boos boos = registerDto.getBoos();
        boos.setCompanyId(company.getId());
        boos.setApplicantCdsId(applicantCds.getId());
        boosService.saveReturnId(boos);
        Job job = registerDto.getJob();
        job.setBoosId(boos.getId());
        jobService.saveOrUpdate(job);

        User user = registerDto.getUser();
        user.setBoosId(boos.getId());
        //进行密码SaltMd5加密
        user.setPassword(PasswordTools.encrypt(user.getPassword()));
        userService.saveOrUpdate(user);
        return R.success("注册成功");
    }


    @PostMapping("/uploadImg")
    public R<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String filePath;
        try {
            filePath = fileStorageService.uploadImgFile("BoosImg", UUID
                    .randomUUID() + "-" + file.getOriginalFilename(), file.getInputStream());
            System.out.println(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 保存文件逻辑
        return R.success(filePath);
    }

    @PutMapping("/updateBoosInfo")
    public R<String> updateBoosInfo(@RequestBody Boos boos){
        System.out.println(boos);
        if (Objects.isNull(boos)){
            R.error("传参null");
        }
        boosService.updateById(boos);
        return R.success("更新boos成功");
    }
//    TODO:实现UserId贯穿全局ThreadLocal实现之后进行更改---防止图片无线添加
//    String filePath;
//    try {
//        // 生成唯一文件名，避免覆盖
//        String uniqueFileName = userId + "_" + file.getOriginalFilename();
//
//        // 检查文件是否已存在
//        boolean exists = fileStorageService.checkFileExists("CompanyImg", uniqueFileName);
//        if (exists) {
//            // 如果文件存在，选择覆盖或删除旧文件
//            fileStorageService.deleteFile("CompanyImg", uniqueFileName);
//        }
//
//        // 上传新文件
//        filePath = fileStorageService.uploadImgFile("CompanyImg", uniqueFileName, file.getInputStream());
//        System.out.println(filePath);
//    } catch (IOException e) {
//        throw new RuntimeException(e);
//    }
//    // 保存文件逻辑
//    return R.success(filePath);


    @GetMapping("/baseInfo/{id}")
    public R<Boos> baseInfo(@PathVariable Long id) {
        LambdaQueryWrapper<Boos> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Boos::getId, id);
        Boos boos = boosService.getOne(queryWrapper);
        return R.success(boos);
    }


    @GetMapping("/all")
    public R<ArrayList<Candidate>> allInfo(Long boosId) {
        System.out.println(boosId);
        ArrayList<Candidate> candidateArrayList = new ArrayList<>();
        LambdaQueryWrapper<Boos> boosQueryWrapper = new LambdaQueryWrapper<>();
        boosQueryWrapper.eq(Boos::getId, boosId);
        Boos boos = boosService.getOne(boosQueryWrapper);
        System.out.println(boos);
        Long applicantCdsId = boos.getApplicantCdsId();

        ApplicantCds applicantCds = applicantCdsService.getById(applicantCdsId);
        JSONArray jsonArray1 = JSONArray.parseArray((String) applicantCds.getCommunicatedCandidates());
        JSONArray jsonArray2 = JSONArray.parseArray((String) applicantCds.getDeliveriesCandidates());
        JSONArray jsonArray3 = JSONArray.parseArray((String) applicantCds.getInterestedCandidates());
        JSONArray jsonArray4 = JSONArray.parseArray((String) applicantCds.getInterviewedCandidates());

        // 合并所有的 List<Integer>，去重并简化代码
        List<Integer> collected = Stream.of(jsonArray1, jsonArray2, jsonArray3, jsonArray4)
                .flatMap(jsonArray -> jsonArray.stream().map(item -> (Integer) item))
                .distinct()
                .collect(Collectors.toList());

        System.out.println("collected" + collected);
        for (Integer userId : collected) {
            //根据userId查询candidateId
            User user = userService.getById(userId);
            System.out.println(user.toString());
            Long candidateId = user.getCandidateId();
            System.out.println(candidateId);
            Candidate candidate = candidateService.getById(candidateId);
            candidateArrayList.add(candidate);
        }
        return R.success(candidateArrayList);
    }


    //根据boosId查询applicantCds表并且查询communicated_cds
    @GetMapping("/communicated")
    public R<ArrayList<Candidate>> communicatedInfo(Long boosId) {
        //用来存储返回的数据
        ArrayList<Candidate> candidateArrayList = new ArrayList<>();
        //根据boosId查询applicantCds表
        LambdaQueryWrapper<Boos> candidateQueryWrapper = new LambdaQueryWrapper<>();
        candidateQueryWrapper.eq(Boos::getId, boosId);
        Boos boos = boosService.getOne(candidateQueryWrapper);
        Long applicantCdsId = boos.getApplicantCdsId();
        ApplicantCds applicantCds = applicantCdsService.getById(applicantCdsId);
        // 将取到的 communicatedCds 进行处理，返回的是 Object 转换成 String
        String communicatedCandidates = (String) applicantCds.getCommunicatedCandidates();
        // 将 JSON 字符串转换为 JSONArray
        JSONArray jsonArray = JSONArray.parseArray(communicatedCandidates);
        // 遍历 JSONArray将communicatedCandidates中的id全部取出来
        List<Integer> collected = jsonArray.stream().map(item -> (Integer) item).collect(Collectors.toList());
        //将communicatedCandidates数组中的数据全部遍历查询出来添加到candidateArrayList
        for (Integer userId : collected) {
            User user = userService.getById(userId);
            Long candidateId = user.getCandidateId();
            Candidate candidate = candidateService.getById(candidateId);
            candidateArrayList.add(candidate);
        }
        return R.success(candidateArrayList);
    }

    //根据boosId查询applicantCds表并且查询deliveries_cds
    @GetMapping("/deliveries")
    public R<ArrayList<Candidate>> deliveriesInfo(Long boosId) {
        ArrayList<Candidate> candidateArrayList = new ArrayList<>();
        LambdaQueryWrapper<Boos> candidateQueryWrapper = new LambdaQueryWrapper<>();
        candidateQueryWrapper.eq(Boos::getId, boosId);
        Boos boos = boosService.getOne(candidateQueryWrapper);
        Long applicantCdsId = boos.getApplicantCdsId();
        ApplicantCds applicantCds = applicantCdsService.getById(applicantCdsId);

        String deliveriesCandidates = (String) applicantCds.getDeliveriesCandidates();
        JSONArray jsonArray = JSONArray.parseArray(deliveriesCandidates);

        List<Integer> collected = jsonArray.stream().map(item -> (Integer) item).collect(Collectors.toList());
        for (Integer userId : collected) {
            User user = userService.getById(userId);
            Long candidateId = user.getCandidateId();
            Candidate candidate = candidateService.getById(candidateId);
            candidateArrayList.add(candidate);
        }
        return R.success(candidateArrayList);
    }

    //根据boosId查询applicantCds表并且查询interests_cds
    @GetMapping("/interests")
    public R<ArrayList<Candidate>> interestsInfo(Long boosId) {
        ArrayList<Candidate> candidateArrayList = new ArrayList<>();
        LambdaQueryWrapper<Boos> candidateQueryWrapper = new LambdaQueryWrapper<>();
        candidateQueryWrapper.eq(Boos::getId, boosId);
        Boos boos = boosService.getOne(candidateQueryWrapper);
        Long applicantCdsId = boos.getApplicantCdsId();
        ApplicantCds applicantCds = applicantCdsService.getById(applicantCdsId);

        String interestedCandidates = (String) applicantCds.getInterestedCandidates();
        JSONArray jsonArray = JSONArray.parseArray(interestedCandidates);

        List<Integer> collected = jsonArray.stream().map(item -> (Integer) item).collect(Collectors.toList());
        for (Integer userId : collected) {
            User user = userService.getById(userId);
            Long candidateId = user.getCandidateId();
            Candidate candidate = candidateService.getById(candidateId);
            candidateArrayList.add(candidate);
        }
        return R.success(candidateArrayList);
    }

    //根据boosId查询applicantCds表并且查询interviews_cds
    @GetMapping("/interviews")
    public R<ArrayList<Candidate>> interviewsInfo(Long boosId) {
        ArrayList<Candidate> candidateArrayList = new ArrayList<>();
        LambdaQueryWrapper<Boos> candidateQueryWrapper = new LambdaQueryWrapper<>();
        candidateQueryWrapper.eq(Boos::getId, boosId);
        Boos boos = boosService.getOne(candidateQueryWrapper);
        Long applicantCdsId = boos.getApplicantCdsId();
        ApplicantCds applicantCds = applicantCdsService.getById(applicantCdsId);
        String interviewedCandidates = (String) applicantCds.getInterviewedCandidates();
        JSONArray jsonArray = JSONArray.parseArray(interviewedCandidates);
        List<Integer> collected = jsonArray.stream().map(item -> (Integer) item).collect(Collectors.toList());
        for (Integer userId : collected) {
            User user = userService.getById(userId);
            Long candidateId = user.getCandidateId();
            Candidate candidate = candidateService.getById(candidateId);
            candidateArrayList.add(candidate);
        }
        return R.success(candidateArrayList);
    }


}
