package parttimejob.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName boos
 */
@TableName(value ="boos")
@Data
public class Boos implements Serializable {
    /**
     * 招聘者 ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 招聘者姓名
     */
    private String boosName;
    /**
     * 招聘者照片
     */
    private String boosImg;
    /**
     * 招聘者邮箱
     */
    private String boosEmail;

    /**
     * 招聘者电话
     */
    private String boosPhone;

    /**
     * 公司 ID
     */
    private Long companyId;
    /**
     * 公司职位
     */
    private String companyPosition;
    /**
     * 消息组 ID
     */
    private Long applicantCdsId;





    @TableField(exist = false)
    private static final long serialVersionUID = 1L;



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", boosName=").append(boosName);
        sb.append(", boosEmail=").append(boosEmail);
        sb.append(", boosPhone=").append(boosPhone);
        sb.append(", companyId=").append(companyId);

        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}