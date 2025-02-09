package parttimejob.service;

import parttimejob.Entity.Messages;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 86151
* @description 针对表【messages】的数据库操作Service
* @createDate 2024-10-29 10:30:48
*/
public interface MessagesService extends IService<Messages> {
    //根据senderId和recipientId查询之间的聊天信息
    List<Messages> getMessagesByIds(Long senderId, Long recipientId);




}
