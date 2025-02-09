package parttimejob.Config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {

    //    添加分页插件
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
//    创建MybatisPlus拦截器
        MybatisPlusInterceptor mybatisPlusInterceptor=new MybatisPlusInterceptor();
//    添加分页拦截器
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
//    返回MybatisPlusInterceptor方法类
        return mybatisPlusInterceptor;

    }

}