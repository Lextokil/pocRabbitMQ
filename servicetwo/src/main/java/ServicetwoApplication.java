import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import servicetwo.ServiceTwo;

@Slf4j
@SpringBootApplication
@ComponentScan(value = "servicetwo")
public class ServicetwoApplication {

	public static void main(String[] args) {

		SpringApplication.run(ServicetwoApplication.class, args);
		log.info("ServiceTwo Started! ");
		ServiceTwo.createConnection();

	}

}
