package parttimejob.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import parttimejob.Common.JwtProperties;
import parttimejob.Dto.UserDto;
import parttimejob.Dto.pageDto.UserPageDto;
import parttimejob.Dto.realDto.StatisticsDto;
import parttimejob.Entity.Job;
import parttimejob.Entity.Reports;
import parttimejob.Entity.User;
import parttimejob.Result.R;
import parttimejob.Utils.jwt.JwtUtil;
import parttimejob.Utils.thread.BaseThread;
import parttimejob.service.BoosService;
import parttimejob.service.JobService;
import parttimejob.service.ReportsService;
import parttimejob.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/user")
@CrossOrigin("http://localhost:5173")
public class UserController {
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private UserService userService;
    @Autowired
    private BoosService boosService;
    @Autowired
    private JobService jobService;
    @Autowired
    private ReportsService reportsService;


    @PostMapping("/login")
    public R<UserDto> login(@RequestBody User user){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,user.getUsername());
        User us = userService.getOne(queryWrapper);
        if(us==null){
            return R.error("登录失败");
        };
        if(!Objects.equals(us.getPassword(), user.getPassword())){
            return R.error("登录失败");
        };
        if(!Objects.equals(us.getStatus(), 1)){
            return R.error("账号冻结");
        };




        //TODO:问题无法将id存入ThreadLocal-------------------将userID存到ThreadLocal里
        BaseThread.setThreadLocal(us.getId());




        //创建返回类
        UserDto userDto = new UserDto();
        //TODO:问题BeanUtils.copyProperties拷贝不成功 ----------将相同的属性拷贝到返回类里
        //BeanUtils.copyProperties(user,userDto);
        userDto.setId(us.getId());
        userDto.setRole(us.getRole());
        userDto.setBoosId(us.getBoosId());
        userDto.setCandidateId(us.getCandidateId());
        //暂时设置null值
        Map<String, Object> claims = new HashMap<>();
        //创建token
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims
        );
        //设置token值
        userDto.setToken(token);
        log.info("user:{}",us);
        log.info("userDro:{}",userDto);








        return R.success(userDto);
    }
    //根据BoosId获取User信息
    @GetMapping("/boosInfo/boos/{boosId}")
    public R<User> boosBaseInfo(@PathVariable Long boosId){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getBoosId,boosId);
        User user = userService.getOne(queryWrapper);
        return R.success(user);
    }
    //根据CandidateId获取User信息
    @GetMapping("/boosInfo/candidate/{candidateId}")
    public R<User> candidateBaseInfo(@PathVariable Long candidateId){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getCandidateId,candidateId);
        User user = userService.getOne(queryWrapper);
        return R.success(user);
    }
    //获取全部User信息
    @GetMapping("/pageInfo")
    public R<Page<User>> pageInfo(UserPageDto pageDto){
        Integer page = pageDto.getPage();
        Integer pageSize = pageDto.getPageSize();
        String role = pageDto.getRole();
        Page<User> userPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getRole,role);
        Page<User> pageUser = userService.page(userPage, queryWrapper);
        return  R.success(pageUser);
    }
    @PutMapping("/updateStatus/{userId}")
    public R<String> updateUserStatus(@PathVariable Long userId) {
        // 1. 先查询当前用户状态
        User user = userService.getById(userId);
        if (user == null) {
            return R.error("用户不存在");
        }

        // 2. 设置相反的状态
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId, userId);
        updateWrapper.set(User::getStatus, user.getStatus() == 1 ? 0 : 1);

        // 3. 执行更新
        userService.update(updateWrapper);

        return R.success("状态更新成功");
    }

    @GetMapping("/statistics")
    public R<StatisticsDto> getStatistics(){
        StatisticsDto statisticsDto = new StatisticsDto();
        //今天和昨天的 00:00:00
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime yesterday = today.minusDays(1);

        //当前的数据
        long currentUserCount = userService.count();//当前的用户总数
        long currentBoosCount = boosService.count();//当前的企业总数
        long currentPendingCount = jobService.count(new LambdaQueryWrapper<Job>()
                .eq(Job::getReview, 1));//当前的待审核职位数
        long currentReportCount = reportsService.count(new LambdaQueryWrapper<Reports>()
                .eq(Reports::getStatus, 0));//当前的待处理举报数
        //昨天的数据
        long yesterdayUserCount = userService.count(new LambdaQueryWrapper<User>()
                .lt(User::getCreatedAt, yesterday));//昨日的用户总数
        long yesterdayBoosCount = userService.count(new LambdaQueryWrapper<User>()
                .eq(User::getRole,"boos")
                .lt(User::getCreatedAt, yesterday));//昨日的企业总数

        double UserIncrease = calculateIncrease(currentUserCount, yesterdayUserCount);
        double BoosIncrease = calculateIncrease(currentBoosCount, yesterdayBoosCount);
        System.out.println("当前的用户总数:"+currentUserCount+"当前的企业总数:"+currentBoosCount+"当前的待审核职位数:"+currentPendingCount+"当前的待处理举报数:"+currentReportCount);
        System.out.println("昨日的用户总数:"+yesterdayUserCount+"昨日的企业总数:"+yesterdayBoosCount);
        //封装结果
        statisticsDto.setUserCount(currentUserCount);
        statisticsDto.setBossCount(currentBoosCount);
        statisticsDto.setPendingJobs(currentPendingCount);
        statisticsDto.setTodayReports(currentReportCount);
        statisticsDto.setUserIncrease(UserIncrease);
        statisticsDto.setBossIncrease(BoosIncrease);

        System.out.println(statisticsDto);
        return R.success(statisticsDto);
    }
    // 计算增长率的辅助方法
    private double calculateIncrease(long current, long previous) {
        if (previous == 0) return 0.0;
        return ((double)(current - previous) / previous) * 100;
    };



}


