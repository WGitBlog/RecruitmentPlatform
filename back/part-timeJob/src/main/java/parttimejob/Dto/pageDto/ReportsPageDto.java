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
public class ReportsPageDto extends PageDto {
    private Integer status;
}
