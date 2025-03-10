package parttimejob.Controller;

import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.*;
import parttimejob.Result.R;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/captcha")
@CrossOrigin("http://localhost:5173")
public class CaptchaController {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired// 注入 JavaMailSender
    private JavaMailSender mailSender;
    @Autowired// 注入 Freemarker
    private Configuration freemarkerConfig;
    /**
     * 生成验证码
     * @param uuid 前端生成的uuid
     * @param response HttpServletResponse对象
     */
   @GetMapping("/loginCaptcha")
    public void loginCaptcha(@RequestParam String uuid, HttpServletResponse response) throws IOException {
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
    @GetMapping ("/emailCaptcha")
    public void emailCaptcha(@RequestParam String uuid,@RequestParam String email) throws IOException, MessagingException, TemplateException {
        Captcha captcha = new SpecCaptcha();
        captcha.setLen(6);
        captcha.setCharType(Captcha.TYPE_ONLY_NUMBER);
        String code = captcha.text();
        System.out.println(code);
        // 根据uuid拼接前缀得到验证码的key
        String key = "email_captcha:" + uuid;
        System.out.println("CaptchaController.key:"+key);
        System.out.println("CaptchaController.code"+code);
        // 缓存验证码
        redisTemplate.opsForValue().set(key , code);
        // 设置验证码1分钟后过期
        redisTemplate.expire(key, 1, TimeUnit.MINUTES);
        sendVerificationCodeEmail(email,code);
    }
    private void sendVerificationCodeEmail(String toEmail, String verificationCode) throws MessagingException, IOException, TemplateException {
        String subject = "【重置密码验证码】";
        Map<String, Object> model = new HashMap<>();
        model.put("verificationCode", verificationCode);
        Template template = freemarkerConfig.getTemplate("verification-code.ftl");
        String htmlContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("2084933310@qq.com");
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
















}
