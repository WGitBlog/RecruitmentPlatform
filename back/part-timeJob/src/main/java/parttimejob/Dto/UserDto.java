package parttimejob.Dto;

import lombok.Data;

@Data
public class UserDto {
    //userId
    private Long id;
    //userRole
    private Object role;
    //token
    private String token;
    /**
     * 关联的应聘者ID，若为应聘者，则填入candidates表的id
     */
    private Long candidateId;

    /**
     * 关联的雇主ID，若为雇主，则填入boss表的id
     */
    private Long boosId;
}
