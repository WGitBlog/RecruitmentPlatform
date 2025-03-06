package parttimejob.Controller;

import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/captcha")
@CrossOrigin("http://localhost:5173")
public class CaptchaController {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 生成验证码
     * @param uuid 前端生成的uuid
     * @param response HttpServletResponse对象
     */
   @GetMapping("/get")
    public void generate(@RequestParam String uuid, HttpServletResponse response) throws IOException {
        Captcha captcha = new SpecCaptcha(140, 38);
        captcha.setLen(4);
        captcha.setCharType(Captcha.TYPE_DEFAULT);
        String code = captcha.text();
        // 将验证码转为小写（忽略大小写）
        code = code.toLowerCase();
        System.out.println(code);
        // 根据uuid拼接前缀得到验证码的key
        String key = "login_captcha:" + uuid;
        System.out.println("CaptchaController.key:"+key);
        System.out.println("CaptchaController.code"+code);
        // 缓存验证码
        redisTemplate.opsForValue().set(key , code);
        // 设置验证码3分钟后过期
        redisTemplate.expire(key, 3, TimeUnit.MINUTES);
        // 设置响应头
        response.setContentType("image/png");
        response.setDateHeader("Expires", 0);
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
       // 输出图片流
        captcha.out(response.getOutputStream());
    }




}
