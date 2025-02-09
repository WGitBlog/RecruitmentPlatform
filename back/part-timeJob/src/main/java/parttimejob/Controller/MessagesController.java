package parttimejob.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import parttimejob.Entity.Messages;
import parttimejob.Result.R;
import parttimejob.service.MessagesService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/messages")
@CrossOrigin("http://localhost:5173")
public class MessagesController {
    @Autowired
    private MessagesService messagesService;


    //根据senderId和recipientId查询之间的聊天信息
    @GetMapping()
    public R<List<Messages>> getMessagesByIds(Long senderId,Long recipientId){

        List<Messages> messages=messagesService.getMessagesByIds(senderId,recipientId);
        return R.success(messages);

    }



}
