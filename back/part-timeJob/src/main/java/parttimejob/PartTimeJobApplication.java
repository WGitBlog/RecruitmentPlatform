package parttimejob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import parttimejob.Netty.NettyClient;
import parttimejob.Netty.NettyServer;
@EnableWebMvc
@SpringBootApplication
@ConfigurationPropertiesScan
//@EnableTransactionManagement
public class PartTimeJobApplication implements CommandLineRunner {
    @Autowired
    private NettyServer nettyServer;
    public static void main(String[] args) {
        SpringApplication.run(PartTimeJobApplication.class, args);
    }
//    NettyChatServer nettyChatServer = (NettyChatServer)context.getBean("NettyChatServer");
//        nettyChatServer.run();

    @Override
    public void run(String... args) throws Exception {
        nettyServer.start();
    }
}
