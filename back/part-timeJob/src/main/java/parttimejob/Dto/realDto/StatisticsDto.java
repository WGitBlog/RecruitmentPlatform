package parttimejob.Dto.realDto;

import lombok.Data;

@Data
public class StatisticsDto {
    private Long userCount;        // 总用户数
    private Double userIncrease;      // 用户增长率
    private Long bossCount;        // 企业用户数
    private Double bossIncrease;      // 企业用户增长率
    private Long pendingJobs;      // 待审核职位数
//    private Double jobIncrease;       // 职位增长率
    private Long todayReports;     // 今日举报数
//    private Double reportsIncrease;   // 举报增长率
}
