package parttimejob.Entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

/**
 * 
 * @TableName job
 */
@TableName(value ="job")
@Data
public class Job implements Serializable {
    /**
     * 职位 ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 职位名称
     */
    private String jobTitle;

    /**
     * 需技术
     */
    private String requiredTechnology;

    /**
     * 职位类别
     */
    private String jobCategory;



    /**
     * 工作地区
     */
    private String workLocation;

    /**
     * 薪资范围
     */
    private String salaryRange;


    /**
     * 每周工作天数
     */
    private Integer weeklyDays;
    /**
     * 工作详细
     */
    private String  jobDetails;


    /**
     * 学历要求
     */
    private String educationRequirements;

    /**
     * 发布时间，默认当前时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date publishTime;

    /**
     * 招聘者 ID
     */
    private Long boosId;
    /**
     * 审核
     */
    private Integer review;
    /**
     * 职位描述，存储 JSON 格式的数据
     */
    @TableField(value = "job_description")
    private String jobDescription;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", jobTitle=").append(jobTitle);
        sb.append(", requiredTechnology=").append(requiredTechnology);
        sb.append(", jobCategory=").append(jobCategory);
        sb.append(", workLocation=").append(workLocation);
        sb.append(", salaryRange=").append(salaryRange);
        sb.append(", educationRequirements=").append(educationRequirements);
        sb.append(", publishTime=").append(publishTime);
        sb.append(", boosId=").append(boosId);
        sb.append(", review=").append(review);
        sb.append(", jobDescription=").append(jobDescription);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}