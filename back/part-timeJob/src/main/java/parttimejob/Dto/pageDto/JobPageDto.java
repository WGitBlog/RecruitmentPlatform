package parttimejob.Dto.pageDto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import parttimejob.Dto.PageDto;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobPageDto extends PageDto {
    private String workLocation;
    private String salaryRange;
    private String jobCategory;
    private Integer weeklyDays;
    private String jobTitle;
    private Integer review;
}
