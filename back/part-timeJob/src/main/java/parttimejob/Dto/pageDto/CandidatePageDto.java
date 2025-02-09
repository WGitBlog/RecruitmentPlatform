package parttimejob.Dto.pageDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import parttimejob.Dto.PageDto;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CandidatePageDto extends PageDto {
    /**
     * 学历类别，如“中专/中技”
     */
    private String degreecategory;
    /**
     * 年龄，如“20岁”
     */
    private String age;
    /**
     * 经验
     */
    private String workyears;
    /**
     * 期望薪资
     */
    private  String desiredSalary;




}
