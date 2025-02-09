package parttimejob.mapper;

import org.apache.ibatis.annotations.Mapper;
import parttimejob.Entity.Reports;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 86151
* @description 针对表【reports】的数据库操作Mapper
* @createDate 2024-12-15 14:12:56
* @Entity parttimejob.Entity.Reports
*/
@Mapper
public interface ReportsMapper extends BaseMapper<Reports> {

}




