package parttimejob;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import parttimejob.Utils.saltMd5.PasswordTools;


//只有需要启动整个项目的时候才会加这个注解
//@SpringBootTest(classes = PartTimeJobApplication.class)
public class PartTimeJobApplicationTests {


    @Test
    public void testUpdateImgFile() {
        for (int i=0;i<10;i++){
            System.out.println(PasswordTools.encrypt("admin"));
        }

    }

}