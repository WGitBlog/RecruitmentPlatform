package parttimejob.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName candidate
 */
@TableName(value ="candidate")
@Data
public class Candidate implements Serializable {
    /**
     * 应聘者唯一标识符
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 手机号码，格式为151******00
     */
    private String account;

    /**
     * 年龄，如“20岁”
     */
    private Integer age;

    /**
     * 出生日期，格式为YYYY-MM-DD
     */
    private LocalDate birthday;

    /**
     * 个人信息是否完整
     */
    private Integer workstatus;

    /**
     * 学历类别，如“中专/中技”
     */
    private String degreecategory;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 是否为应届生，数字表示不同状态
     */
    private Integer freshgraduate;

    /**
     * 性别，0表示女性，1表示男性
     */
    private Integer gender;

    /**
     * 地址
     */
    private String address;

    /**
     * 姓名
     */
    private String name;

    /**
     * 开始工作日期，0表示无工作经验
     */
    private Integer startworkdate;

    /**
     * 头像链接
     */
    private String tiny;

    /**
     * 模糊处理的微信号
     */
    private String weixinblur;



    /**
     * 工作年限
     */
    private Integer workyears;

    /**
     * 简历内容，存储简历的详细信息
     */
    private String resume;

    /**
     * job关系表id
     */
    private Long applicantId;

    /**
     *期望城市
     */
    private String desiredCity;
    /**
     *期望行业
     */
    private String desiredIndustry;
    /**
     *期望职位
     */
    private String desiredPosition;


    /**
     * 期望薪资
     */
    private  String desiredSalary;

    /**
     * 技能介绍
     */
    private  String skillsIntroduction;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", account=").append(account);
        sb.append(", age=").append(age);
        sb.append(", birthday=").append(birthday);
        sb.append(", workstatus=").append(workstatus);
        sb.append(", degreecategory=").append(degreecategory);
        sb.append(", email=").append(email);
        sb.append(", freshgraduate=").append(freshgraduate);
        sb.append(", gender=").append(gender);
        sb.append(", address=").append(address);
        sb.append(", name=").append(name);
        sb.append(", startworkdate=").append(startworkdate);
        sb.append(", tiny=").append(tiny);
        sb.append(", weixinblur=").append(weixinblur);
        sb.append(", desiredSalary=").append(desiredSalary);
        sb.append(", workyears=").append(workyears);
        sb.append(", resume=").append(resume);
        sb.append(", desiredCity=").append(desiredCity);
        sb.append(", desiredIndustry=").append(desiredIndustry);
        sb.append(", skillsIntroduction=").append(skillsIntroduction);
        sb.append(", desiredPosition=").append(desiredPosition);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}