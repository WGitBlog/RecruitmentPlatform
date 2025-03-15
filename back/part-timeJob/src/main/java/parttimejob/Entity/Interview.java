package parttimejob.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;
import org.hibernate.annotations.Type;
import parttimejob.Dto.Common.Step;

import javax.persistence.Column;

/**
 * 面试流程表，用于存储应聘者的面试信息
 * @TableName interview
 */
@TableName(value ="interview")
@Data
public class Interview implements Serializable {
    /**
     * 每个面试流程的唯一标识符
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 外键，指向职位表 job，表示应聘者面试的职位
     */
    private Long jobId;

    /**
     * 外键，指向应聘者表 candidate，表示哪个应聘者参与了这次面试
     */
    private Long candidateId;

    /**
     * 外键，指向招聘者（Boss）表 boss，表示哪个招聘者安排了这次面试
     */
    private Long boosId;

    /**
     * 冗余存储，便于快速查询
     */
    private Long companyId;

    /**
     * 面试时间
     */
    private Date date;

    /**
     * 面试类型（1: 线上面试, 2: 线下面试）
     */
    private Integer type;

    /**
     * 面试时长（单位：小时）
     */
    private Integer time;

    /**
     * 面试平台（如果是线上面试）
     */
    private String platform;

    /**
     * 面试链接（如果是线上面试）
     */
    private String link;

    /**
     * 面试地点（如果是线下面试）
     */
    private String address;

    /**
     * 面试详细（线上+线下）
     */
    private String details;

    /**
     * 对应面试者来说:面试状态（0: 已取消, 1: 待面试, 2: 面试中, 3: 等待结果）---对应boss来说:面试状态（0: 已取消, 1: 待面试, 2: 面试中, 3: 发布结果）
     */
    private Integer status;

    /**
     * 面试评分（1-5）
     */
    private Integer rating;

    /**
     * 企业反馈内容（可为空）
     */
    private String feedbackContent;

    /**
     * 面试流程步骤
     */
    @Type(type = "json")
    @Column(columnDefinition = "json")
    private List<Step> steps; // 确保 Step 类有正确的字段定义

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Interview other = (Interview) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getJobId() == null ? other.getJobId() == null : this.getJobId().equals(other.getJobId()))
            && (this.getCandidateId() == null ? other.getCandidateId() == null : this.getCandidateId().equals(other.getCandidateId()))
            && (this.getBoosId() == null ? other.getBoosId() == null : this.getBoosId().equals(other.getBoosId()))
            && (this.getCompanyId() == null ? other.getCompanyId() == null : this.getCompanyId().equals(other.getCompanyId()))
            && (this.getDate() == null ? other.getDate() == null : this.getDate().equals(other.getDate()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getTime() == null ? other.getTime() == null : this.getTime().equals(other.getTime()))
            && (this.getPlatform() == null ? other.getPlatform() == null : this.getPlatform().equals(other.getPlatform()))
            && (this.getLink() == null ? other.getLink() == null : this.getLink().equals(other.getLink()))
            && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()))
            && (this.getDetails() == null ? other.getDetails() == null : this.getDetails().equals(other.getDetails()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getRating() == null ? other.getRating() == null : this.getRating().equals(other.getRating()))
            && (this.getFeedbackContent() == null ? other.getFeedbackContent() == null : this.getFeedbackContent().equals(other.getFeedbackContent()))
            && (this.getSteps() == null ? other.getSteps() == null : this.getSteps().equals(other.getSteps()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getJobId() == null) ? 0 : getJobId().hashCode());
        result = prime * result + ((getCandidateId() == null) ? 0 : getCandidateId().hashCode());
        result = prime * result + ((getBoosId() == null) ? 0 : getBoosId().hashCode());
        result = prime * result + ((getCompanyId() == null) ? 0 : getCompanyId().hashCode());
        result = prime * result + ((getDate() == null) ? 0 : getDate().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getTime() == null) ? 0 : getTime().hashCode());
        result = prime * result + ((getPlatform() == null) ? 0 : getPlatform().hashCode());
        result = prime * result + ((getLink() == null) ? 0 : getLink().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        result = prime * result + ((getDetails() == null) ? 0 : getDetails().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getRating() == null) ? 0 : getRating().hashCode());
        result = prime * result + ((getFeedbackContent() == null) ? 0 : getFeedbackContent().hashCode());
        result = prime * result + ((getSteps() == null) ? 0 : getSteps().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", jobId=").append(jobId);
        sb.append(", candidateId=").append(candidateId);
        sb.append(", boosId=").append(boosId);
        sb.append(", companyId=").append(companyId);
        sb.append(", date=").append(date);
        sb.append(", type=").append(type);
        sb.append(", time=").append(time);
        sb.append(", platform=").append(platform);
        sb.append(", link=").append(link);
        sb.append(", address=").append(address);
        sb.append(", details=").append(details);
        sb.append(", status=").append(status);
        sb.append(", rating=").append(rating);
        sb.append(", feedbackContent=").append(feedbackContent);
        sb.append(", steps=").append(steps);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}