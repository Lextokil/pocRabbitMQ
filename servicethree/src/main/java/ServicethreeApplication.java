import core.servicethree.ServiceThree;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = "core")
@EnableMongoRepositories(basePackages = "core")
public class ServicethreeApplication {

    private static ServiceThree serviceThree;

    public ServicethreeApplication(ServiceThree serviceThree) {
        this.serviceThree = serviceThree;
    }

    public static void main(String[] args) {

        SpringApplication.run(ServicethreeApplication.class, args);
        log.info("ServiceThree Started!");
        serviceThree.createConnection();
    }

}
