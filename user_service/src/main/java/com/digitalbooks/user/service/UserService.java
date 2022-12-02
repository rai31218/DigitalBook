package com.digitalbooks.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitalbooks.user.model.Users;
import com.digitalbooks.user.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	public void saveUser(Users user) {
		userRepository.save(user);
	}

	public Users duplicateUserNameAnsEmail(String userName, String email) {
		return userRepository.findByUserNameAndEmail(userName, email);
		
	}
	
}
