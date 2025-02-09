package parttimejob.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName company
 */
@TableName(value ="company")
@Data
public class Company implements Serializable {
    /**
     * 公司 ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 公司照片
     */
    private String companyImg;

    /**
     * 公司规模
     */
    private String companyScale;

    /**
     *是否上市
     */
    private Boolean companyMarket;
    /**
     * 公司类型
     */
    private String companyType;
    /**
     * 公司地址
     */
    private String location;

    /**
     * 行业
     */
    private String industry;

    /**
     * 成立年份
     */
    private Integer establishedYear;

    /**
     * 联系电话
     */
    private String contactNumber;

    /**
     * 公司邮箱
     */
    private String email;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;




}