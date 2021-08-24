package org.aleks4ay.hotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class HotelApplication {

	public static void main(String[] args) throws Throwable {
		SpringApplication.run(HotelApplication.class, args);
	}
}