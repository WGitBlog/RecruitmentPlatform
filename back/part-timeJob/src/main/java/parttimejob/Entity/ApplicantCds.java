package parttimejob.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName applicant_cds
 */
@TableName(value ="applicant_cds")
@Data
public class ApplicantCds implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private Object communicatedCandidates;

    /**
     * 
     */
    private Object deliveriesCandidates;

    /**
     * 
     */
    private Object interviewedCandidates;

    /**
     * 
     */
    private Object interestedCandidates;

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
        ApplicantCds other = (ApplicantCds) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCommunicatedCandidates() == null ? other.getCommunicatedCandidates() == null : this.getCommunicatedCandidates().equals(other.getCommunicatedCandidates()))
            && (this.getDeliveriesCandidates() == null ? other.getDeliveriesCandidates() == null : this.getDeliveriesCandidates().equals(other.getDeliveriesCandidates()))
            && (this.getInterviewedCandidates() == null ? other.getInterviewedCandidates() == null : this.getInterviewedCandidates().equals(other.getInterviewedCandidates()))
            && (this.getInterestedCandidates() == null ? other.getInterestedCandidates() == null : this.getInterestedCandidates().equals(other.getInterestedCandidates()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCommunicatedCandidates() == null) ? 0 : getCommunicatedCandidates().hashCode());
        result = prime * result + ((getDeliveriesCandidates() == null) ? 0 : getDeliveriesCandidates().hashCode());
        result = prime * result + ((getInterviewedCandidates() == null) ? 0 : getInterviewedCandidates().hashCode());
        result = prime * result + ((getInterestedCandidates() == null) ? 0 : getInterestedCandidates().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", communicatedCandidates=").append(communicatedCandidates);
        sb.append(", deliveriesCandidates=").append(deliveriesCandidates);
        sb.append(", interviewedCandidates=").append(interviewedCandidates);
        sb.append(", interestedCandidates=").append(interestedCandidates);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}