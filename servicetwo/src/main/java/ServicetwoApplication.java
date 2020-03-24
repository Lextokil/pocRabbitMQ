import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import core.mdfe.servicetwo.ServiceTwo;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = "core")
@EnableMongoRepositories(basePackages = "core")
public class ServicetwoApplication {

    private static ServiceTwo serviceTwo;

    public ServicetwoApplication(ServiceTwo serviceTwo) {
        this.serviceTwo = serviceTwo;
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        SpringApplication.run(ServicetwoApplication.class, args);
        log.info("ServiceTwo Started! ");
        serviceTwo.createConnection();

    }

}
