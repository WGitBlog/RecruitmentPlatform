package parttimejob.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import parttimejob.Entity.Messages;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 86151
* @description 针对表【messages】的数据库操作Mapper
* @createDate 2024-10-29 10:30:48
* @Entity parttimejob.Entity.Messages
*/
@Mapper
public interface MessagesMapper extends BaseMapper<Messages> {


//    @Override
//    public List<Messages> getMessagesByIds(Long senderId, Long recipientId) {
//        List<Messages> list =messagesMapper.getMessagesByIds(senderId, recipientId);
//        return list;
//    }
    //根据senderId和recipientId查询之间的聊天信息
List<Messages> getMessagesByIds(@Param("senderId") Long senderId, @Param("recipientId") Long recipientId);
}




