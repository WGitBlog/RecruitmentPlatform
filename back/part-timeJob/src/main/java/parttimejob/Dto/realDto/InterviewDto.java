package parttimejob.Dto.realDto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import parttimejob.Entity.Interview;

@EqualsAndHashCode(callSuper = true)
@Data
public class InterviewDto extends Interview {
    private String JobTitle;
    private String CompanyName;
    private String CompanyImgUrl;
    private String CandidateName;
    private String CandidateImgUrl;
}
