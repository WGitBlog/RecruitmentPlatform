package parttimejob.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import parttimejob.Entity.Boos;
import parttimejob.service.BoosService;
import parttimejob.mapper.BoosMapper;
import org.springframework.stereotype.Service;

/**
* @author 86151
* @description 针对表【boos】的数据库操作Service实现
* @createDate 2024-10-17 09:51:02
*/
@Service
public class BoosServiceImpl extends ServiceImpl<BoosMapper, Boos>
    implements BoosService{

    @Autowired
    private BoosMapper boosMapper;
    //根据id查询Boos
    @Override
    public Boos getBoosById(Long boosId) {
        Boos boos = boosMapper.getBoosById(boosId);
        return boos;
    }
    //存储boos并返回boosId
    @Override
    public Long saveReturnId(Boos boos) {
        Long boosId=boosMapper.saveReturnId(boos);
        return boosId;
    }
}




