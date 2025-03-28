package parttimejob.Config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import parttimejob.Intercepter.WebMvcInterceptor;

import java.util.List;

@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

   @Autowired
   private WebMvcInterceptor webMvcInterceptor;
    // 当前跨域请求最大有效时长。这里默认1天
//    private static final long MAX_AGE = 24 * 60 * 60;
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:5137")
//                .allowedMethods("*")
//                .allowedHeaders("*")
//                .allowCredentials(true)
//                .maxAge(MAX_AGE);
//    }
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(webMvcInterceptor)
                .addPathPatterns("/**")
                //除了(登录/注册/退出)都需要鉴权
                .excludePathPatterns(
                        "/user/login",
                        "/user/logout",
                        "/user/resetPassword",
                        "/boos/boosRegister",
                        "/candidate/candidateRegister",
                        "/candidate/uploadImg",
                        "/candidate/uploadResume",
                        "/company/uploadImg",
                        "/boos/uploadImg",
                        "/captcha/loginCaptcha",
                        "/captcha/emailCaptcha"
                );
    }
//    /**
//     * 消息转换器
//     * @param converters
//     */
//    @Override
//    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//        log.info("扩展消息转换器");
//        //创建一个消息转换器对象
//        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//        //设置对象转换器类
//        converter.setObjectMapper(new JsonObjectMapper());
//        //将我们自己的消息转换器加到mvc容器中--优先使用自己的消息转换器
//        converters.add(0,converter);
//
//    }
}
