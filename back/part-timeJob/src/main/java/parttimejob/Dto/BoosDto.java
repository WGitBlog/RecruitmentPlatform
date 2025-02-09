package parttimejob.Dto;

import lombok.Data;
import parttimejob.Entity.Boos;

@Data
public class BoosDto extends Boos {
    //user表的Id
    private Long id;
    private Integer status;
}
