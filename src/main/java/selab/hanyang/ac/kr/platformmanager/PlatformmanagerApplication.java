package selab.hanyang.ac.kr.platformmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import selab.hanyang.ac.kr.platformmanager.util.Mockup;

@SpringBootApplication
public class PlatformmanagerApplication {

	public static void main(String[] args) {
		System.out.println("[SELab IoT - Platform Manager Starting...]");
		SpringApplication.run(PlatformmanagerApplication.class, args);
	}

}
