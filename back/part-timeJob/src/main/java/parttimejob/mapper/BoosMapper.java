package parttimejob.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import parttimejob.Entity.Boos;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 86151
* @description 针对表【boos】的数据库操作Mapper
* @createDate 2024-10-17 09:51:02
* @Entity parttimejob.Entity.Boos
*/
@Mapper
public interface BoosMapper extends BaseMapper<Boos> {

    //根据id查询Boos
    @Select("select * from boos where id=#{id}")
    Boos getBoosById(Long boosId);
    //存储boos并返回boosId
    Long saveReturnId(Boos boos);
}




