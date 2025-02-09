package parttimejob.Intercepter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class WebMvcInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断是否是访问静态资源
        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        String requestURI = request.getRequestURI();
        log.info("请求被Interceptor拦截：{}",requestURI);

        return true;
    }
}
