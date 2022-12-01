package com.digitalbooks.user;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import com.digitalbooks.user.model.Roles;
import com.digitalbooks.user.model.Users;
import com.digitalbooks.user.repository.UserRepository;

@SpringBootApplication
//@EnableEurekaClient
public class UserServiceApplication {

	
	@Autowired
	private UserRepository userRepository;
	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}
	
	
	@PostConstruct
	public void initUsesr() {
		List<Users> users= Arrays.asList(
			new Users(1, "Rudra", "pwd1", "rudra@gmail.com", new Roles(2, "Author"), new Date()),
			new Users(2, "RuIshanidra", "pwd2", "RuIshanidra@gmail.com", new Roles(3, "Reader"), new Date()),
			new Users(3, "Rai", "pwd3", "rai@gmail.com", new Roles(1, "Guest"), new Date()));
				
				
		userRepository.saveAll(users);
				
				
		
	}

}
