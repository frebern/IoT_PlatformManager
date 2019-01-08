package selab.hanyang.ac.kr.platformmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class PlatformManagerApplication {

	public static void main(String[] args) {
		System.out.println("[SELab IoT - Platform Manager Starting...]");
		SpringApplication.run(PlatformManagerApplication.class, args);
	}

}
