package proyect.logitrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "proyect")
@EnableJpaRepositories(basePackages = "proyect.repository")
@EntityScan(basePackages = "proyect.model")
public class LogitrackApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogitrackApplication.class, args);
    }
}