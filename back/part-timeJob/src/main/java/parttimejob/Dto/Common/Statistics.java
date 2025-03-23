package parttimejob.Dto.Common;

import lombok.Data;

import java.util.List;

@Data
public class Statistics {
    private Long userCount;        // 总用户数
    private Double userIncrease;      // 用户增长率
    private Long bossCount;        // 企业用户数
    private Double bossIncrease;      // 企业用户增长率
    private Long pendingJobs;      // 待审核职位数
    private Long todayReports;     // 今日举报数

    private List<String> dateList; //近七日日期
    private List<Integer> userGrowthData;   // 用户增长

    private List<String> CategoryList;//职位分类
    private List<Integer> jobCategoryData;// 职位增长


}
