package com.digitalbooks.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.digitalbooks.user.model.Users;

public interface UserRepository extends JpaRepository<Users, Integer> {

	Users findByUserName(String userName);
	Users findByEmail(String email);

}
