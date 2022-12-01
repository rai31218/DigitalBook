package com.digitalbooks.user.jwt;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.digitalbooks.user.model.Users;
import com.digitalbooks.user.repository.UserRepository;

@Component
class UserDetailsServiceImpl implements UserDetailsService {

   @Autowired
   UserRepository userRepo;
   
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Users user = userRepo.findByUserName(userName);
       // return new UserPrincipalImp(account);
        
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), new ArrayList());
    }

    // ...
}