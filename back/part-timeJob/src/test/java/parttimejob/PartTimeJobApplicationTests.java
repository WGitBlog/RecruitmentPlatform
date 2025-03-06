package parttimejob;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;


//只有需要启动整个项目的时候才会加这个注解
@SpringBootTest
public class PartTimeJobApplicationTests {
    @Autowired
    private JavaMailSender sender;  // 注入 JavaMailSender

    @Test
    public void testUpdateImgFile() {
        SimpleMailMessage message = new SimpleMailMessage();
        // 发送邮件的标题
        message.setSubject("【验证码】");

        // 发送邮件的内容
        message.setText("你的验证码为:666");
        // 指定要接收邮件的用户邮箱账号
        message.setTo("663993801@qq.com");
        // 发送邮件的邮箱账号
        message.setFrom("2084933310@qq.com");
        // 调用send方法发送邮件即可
        sender.send(message);


    }

}