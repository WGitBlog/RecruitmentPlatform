package parttimejob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
@EnableWebMvc
@SpringBootApplication
@ConfigurationPropertiesScan
//@EnableTransactionManagement
public class PartTimeJobApplication  {
    public static void main(String[] args) {
        SpringApplication.run(PartTimeJobApplication.class, args);
    }

}
