package space.bielsolososdev.rdl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RdlApplication {

	public static void main(String[] args) {
		SpringApplication.run(RdlApplication.class, args);
	}

}
