package BLMS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BLMSAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(BLMSAppApplication.class, args);
	}
}
