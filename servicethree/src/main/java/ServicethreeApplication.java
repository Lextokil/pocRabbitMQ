import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import servicethree.ServiceThree;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = "servicethree")
public class ServicethreeApplication {

    public static void main(String[] args) {

        SpringApplication.run(ServicethreeApplication.class, args);
        log.info("ServiceThree Started!");
        ServiceThree.createConnection();
    }

}
