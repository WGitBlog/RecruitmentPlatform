package parttimejob.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName applicant
 */
@TableName(value ="applicant")
@Data
public class Applicant implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private Object communicatedJobs;

    /**
     * 
     */
    private Object deliveriesJobs;

    /**
     * 
     */
    private Object interviewedJobs;

    /**
     * 
     */
    private Object interestedJobs;

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
        Applicant other = (Applicant) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCommunicatedJobs() == null ? other.getCommunicatedJobs() == null : this.getCommunicatedJobs().equals(other.getCommunicatedJobs()))
            && (this.getDeliveriesJobs() == null ? other.getDeliveriesJobs() == null : this.getDeliveriesJobs().equals(other.getDeliveriesJobs()))
            && (this.getInterviewedJobs() == null ? other.getInterviewedJobs() == null : this.getInterviewedJobs().equals(other.getInterviewedJobs()))
            && (this.getInterestedJobs() == null ? other.getInterestedJobs() == null : this.getInterestedJobs().equals(other.getInterestedJobs()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCommunicatedJobs() == null) ? 0 : getCommunicatedJobs().hashCode());
        result = prime * result + ((getDeliveriesJobs() == null) ? 0 : getDeliveriesJobs().hashCode());
        result = prime * result + ((getInterviewedJobs() == null) ? 0 : getInterviewedJobs().hashCode());
        result = prime * result + ((getInterestedJobs() == null) ? 0 : getInterestedJobs().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", communicatedJobs=").append(communicatedJobs);
        sb.append(", deliveriesJobs=").append(deliveriesJobs);
        sb.append(", interviewedJobs=").append(interviewedJobs);
        sb.append(", interestedJobs=").append(interestedJobs);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}