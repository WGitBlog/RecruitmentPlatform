package parttimejob.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName reports
 */
@TableName(value ="reports")
@Data
public class Reports implements Serializable {
    /**
     * 举报记录的唯一标识符
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 举报用户的唯一标识符USERID
     */
    private Long reporterId;

    /**
     * 职位的唯一标识符
     */
    private Integer jobId;

    /**
     * 职位名称
     */
    private String jobTitle;

    /**
     * 公司唯一标识符
     */
    private Long companyId;

    /**
     * 发布者姓名
     */
    private String boosName;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 举报原因
     */
    private String reason;

    /**
     * 举报状态（0: 待处理, 1: 已处理, 2: 已驳回）
     */
    private Integer status;

    /**
     * 举报创建时间
     */
    private Date createTime;

    /**
     * 处理反馈
     */
    private String feedback;

    /**
     * 举报详细说明
     */
    private String reportDetails;

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
        Reports other = (Reports) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getReporterId() == null ? other.getReporterId() == null : this.getReporterId().equals(other.getReporterId()))
            && (this.getJobId() == null ? other.getJobId() == null : this.getJobId().equals(other.getJobId()))
            && (this.getJobTitle() == null ? other.getJobTitle() == null : this.getJobTitle().equals(other.getJobTitle()))
            && (this.getCompanyId() == null ? other.getCompanyId() == null : this.getCompanyId().equals(other.getCompanyId()))
            && (this.getBoosName() == null ? other.getBoosName() == null : this.getBoosName().equals(other.getBoosName()))
            && (this.getCompanyName() == null ? other.getCompanyName() == null : this.getCompanyName().equals(other.getCompanyName()))
            && (this.getReason() == null ? other.getReason() == null : this.getReason().equals(other.getReason()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getFeedback() == null ? other.getFeedback() == null : this.getFeedback().equals(other.getFeedback()))
            && (this.getReportDetails() == null ? other.getReportDetails() == null : this.getReportDetails().equals(other.getReportDetails()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getReporterId() == null) ? 0 : getReporterId().hashCode());
        result = prime * result + ((getJobId() == null) ? 0 : getJobId().hashCode());
        result = prime * result + ((getJobTitle() == null) ? 0 : getJobTitle().hashCode());
        result = prime * result + ((getCompanyId() == null) ? 0 : getCompanyId().hashCode());
        result = prime * result + ((getBoosName() == null) ? 0 : getBoosName().hashCode());
        result = prime * result + ((getCompanyName() == null) ? 0 : getCompanyName().hashCode());
        result = prime * result + ((getReason() == null) ? 0 : getReason().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getFeedback() == null) ? 0 : getFeedback().hashCode());
        result = prime * result + ((getReportDetails() == null) ? 0 : getReportDetails().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", reporterId=").append(reporterId);
        sb.append(", jobId=").append(jobId);
        sb.append(", jobTitle=").append(jobTitle);
        sb.append(", companyId=").append(companyId);
        sb.append(", boosName=").append(boosName);
        sb.append(", companyName=").append(companyName);
        sb.append(", reason=").append(reason);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append(", feedback=").append(feedback);
        sb.append(", reportDetails=").append(reportDetails);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}