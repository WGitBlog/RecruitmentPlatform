package parttimejob.Dto;

import lombok.Data;
import parttimejob.Entity.Job;

@Data
public class JobDto extends Job {


    /**
     * 招聘者姓名
     */
    private String boosName;
    /**
     * 招聘者照片
     */
    private String boosImg;

    /**
     * 公司Id
     */
    private Long companyId;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 公司职位
     */
    private String companyPosition;
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





}
