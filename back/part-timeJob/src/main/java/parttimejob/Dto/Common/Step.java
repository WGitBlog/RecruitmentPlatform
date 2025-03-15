package parttimejob.Dto.Common;
import lombok.Data;

@Data
public class Step {
    private String title;
    private Integer status; // 1--success--完成  2--process--待处理   3--wait--需等待
    private String desc;
}
