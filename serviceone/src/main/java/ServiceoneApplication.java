import com.rabbitmq.client.Connection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import serviceone.ServiceOne;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Slf4j
@SpringBootApplication
@ComponentScan(value = "serviceone")
public class ServiceoneApplication {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        SpringApplication.run(ServiceoneApplication.class, args);
        log.info("ServiceOne Started! ");
        Connection connection = ServiceOne.createConnection();
        for (int i = 0; i < 1000; i++) {
            Thread.sleep(6000);
            ServiceOne.postXmlinARow(connection, (i+1));
        }
    }

}
