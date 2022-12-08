package com.digitalbook.config.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class DigitalBookConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalBookConfigServerApplication.class, args);
	}

}
