package parttimejob.Dto;

import lombok.Data;
import parttimejob.Entity.*;

@Data
public class RegisterDto {
    private Company company;
    private Boos boos;
    private Job job;
    private User user;
    private Candidate candidate;
}
