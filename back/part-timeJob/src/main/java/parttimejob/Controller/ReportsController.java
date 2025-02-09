package parttimejob.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import parttimejob.Dto.pageDto.ReportsPageDto;
import parttimejob.Entity.Reports;
import parttimejob.Result.R;
import parttimejob.service.ReportsService;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/report")
@CrossOrigin("http://localhost:5173")
public class ReportsController {

    @Autowired
    private ReportsService reportsService;



    @PostMapping("/addInfo")
    public R<String> addInfo(@RequestBody Reports reports) {
        System.out.println("report:" + reports);
        if (Objects.isNull(reports)) {
            return R.error("提交举报信息失败");
        }

        // 检查是否已经举报过该职位
        LambdaQueryWrapper<Reports> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Reports::getJobId, reports.getJobId())  // 修改这里：使用 jobId 而不是 id
                    .eq(Reports::getReporterId, reports.getReporterId())
                    .eq(Reports::getStatus, 0);

        Reports existingReport = reportsService.getOne(queryWrapper);
        if (existingReport != null) {
            return R.error("您已经举报过该职位，不可重复举报");
        }

        // 保存举报信息
        reportsService.save(reports);  // 使用 save 而不是 saveOrUpdate
        return R.success("举报成功");
    }

    @GetMapping("/reportPage")
    public R<Page> reportPage(ReportsPageDto pageDto){
        System.out.println(1);
        if (Objects.isNull(pageDto)){
            R.error("没接收到pageDto");
        }
        Integer page = pageDto.getPage();
        Integer pageSize = pageDto.getPageSize();
//        Integer status = pageDto.getStatus();
        Page<Reports> reportsPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Reports> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Reports::getStatus,status);
        Page<Reports> reportsList = reportsService.page(reportsPage, queryWrapper);
        return R.success(reportsList);
    }



    @PutMapping("/updateFeedback")
    public R<String> updateFeedBack(Long id,String feedback,Integer status){
        System.out.println("status"+status);
        LambdaUpdateWrapper<Reports> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Reports::getId,id);
        updateWrapper.set(Reports::getFeedback,feedback);
        updateWrapper.set(Reports::getStatus,status);
        boolean update = reportsService.update(updateWrapper);
        if (!update){
           return R.error("处理失败");
        }
       return R.success("处理成功");

    }




    @PutMapping("/updateReport")
    public R<String> updateReport(Long id,Integer status,String feedback){

        LambdaUpdateWrapper<Reports> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Reports::getId,id);
        updateWrapper.set(Reports::getFeedback,feedback);
        updateWrapper.set(Reports::getStatus,status);
        boolean update = reportsService.update(updateWrapper);
        if (!update){
            return R.error("处理失败");
        }
        return R.success("处理成功");

    }













    @GetMapping("/list/{candidateId}")
    public R<List<Reports>> list(@PathVariable Long candidateId){
        if (Objects.isNull(candidateId)){
            return R.error("查询举报信息失败");
        }
        LambdaQueryWrapper<Reports> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Reports::getReporterId,candidateId);
        List<Reports> list = reportsService.list(queryWrapper);
        return R.success(list);
    }
}
