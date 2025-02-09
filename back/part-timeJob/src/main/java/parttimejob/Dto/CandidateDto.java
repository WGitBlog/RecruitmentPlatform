package parttimejob.Dto;

import lombok.Data;
import parttimejob.Entity.Candidate;

@Data
public class CandidateDto extends Candidate {
    //user表的Id
    private Long id;
    private Integer status;
}
