package parttimejob.service;

import parttimejob.Entity.Boos;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 86151
* @description 针对表【boos】的数据库操作Service
* @createDate 2024-10-17 09:51:02
*/
public interface BoosService extends IService<Boos> {
    //根据id查询boos信息
    Boos getBoosById(Long boosId);
    //存储boos并返回boosId
    Long saveReturnId(Boos boos);
}
