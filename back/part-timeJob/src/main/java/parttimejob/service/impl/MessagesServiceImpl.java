package parttimejob.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import parttimejob.Entity.Messages;
import parttimejob.service.MessagesService;
import parttimejob.mapper.MessagesMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 86151
* @description 针对表【messages】的数据库操作Service实现
* @createDate 2024-10-29 10:30:48
*/
@Service
public class MessagesServiceImpl extends ServiceImpl<MessagesMapper, Messages>
    implements MessagesService{

    @Autowired
    private MessagesMapper  messagesMapper;//根据senderId和recipientId查询之间的聊天信息;

    //根据senderId和recipientId查询之间的聊天信息
    @Override
    public List<Messages> getMessagesByIds(Long senderId, Long recipientId) {
        List<Messages> list =messagesMapper.getMessagesByIds(senderId, recipientId);

        return list;
    }


}




