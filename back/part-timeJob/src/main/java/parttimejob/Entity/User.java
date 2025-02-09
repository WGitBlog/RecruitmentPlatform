package parttimejob.Entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 用户唯一标识符
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码，���议加密存储
     */
    private String password;

    /**
     * 用户角色，区分应聘者和雇主
     */
    private Object role;

    /**
     * 关联的应聘者ID，若为应聘者，则填入candidates表的id
     */
    private Long candidateId;

    /**
     * 关联的雇主ID，若为雇主，则填入boss表的id
     */
    private Long boosId;

    /**
     * 用户创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createdAt;
    /**
     * 用户信息更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updatedAt;


    /**
     * 账号禁用状态
     */
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", username=").append(username);
        sb.append(", password=").append(password);
        sb.append(", role=").append(role);
        sb.append(", candidateId=").append(candidateId);
        sb.append(", boosId=").append(boosId);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", status=").append(status);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}