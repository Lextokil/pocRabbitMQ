import core.mdfe.servicetwo.ServiceTwo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = "core")
@EnableMongoRepositories(basePackages = "core")
public class ServicetwoApplication {

    private static ServiceTwo serviceTwo;

    public ServicetwoApplication(ServiceTwo serviceTwo) {
        this.serviceTwo = serviceTwo;
    }

    public static void main(String[] args) {
        SpringApplication.run(ServicetwoApplication.class, args);
        log.info("ServiceTwo Started! ");
        serviceTwo.createConnection();

    }

}
