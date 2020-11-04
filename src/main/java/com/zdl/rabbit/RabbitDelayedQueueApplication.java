package com.zdl.rabbit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhoudeli
 */
@SpringBootApplication
public class RabbitDelayedQueueApplication {

	public static void main(String[] args) {
		SpringApplication.run(RabbitDelayedQueueApplication.class, args);
	}

}
