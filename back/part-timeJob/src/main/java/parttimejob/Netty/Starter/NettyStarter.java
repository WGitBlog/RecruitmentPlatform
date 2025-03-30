package parttimejob.Netty.Starter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import parttimejob.Netty.NettyServer;

@Slf4j
@Component
public class NettyStarter  implements CommandLineRunner {
    @Autowired
    private NettyServer nettyServer;
    @Override
    public void run(String... args) throws Exception {
        nettyServer.start();
    }
}
