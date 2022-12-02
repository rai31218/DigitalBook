package com.digitalbooks.user.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digitalbooks.user.jwt.AuthRequest;
import com.digitalbooks.user.jwt.JwtResponse;
import com.digitalbooks.user.jwt.JwtUtil;
import com.digitalbooks.user.jwt.UserDetailsImpl;
import com.digitalbooks.user.model.Users;
import com.digitalbooks.user.payload.response.MessageResponse;
import com.digitalbooks.user.service.UserService;

@RestController
@RequestMapping(value="/api/v1/digitalbooks")
public class UserServiceController {
	
	

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserService userService;
    
    @Autowired
	PasswordEncoder encoder;
    
	@GetMapping("/test")
	public String test() {
		System.out.println("Calling User service");
		return "It is User Service";
	}
	
	@PostMapping("/signin")
	public String authenticate() {
		System.out.println("Calling User service");
		return "It is User Service";
	}
	

    @PostMapping("/sign-in")
    public ResponseEntity<?> generateToken(@RequestBody AuthRequest authRequest, HttpServletResponse httpServletResponse, HttpSession session) throws Exception {
        try {
        	Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword())
            );
        	
        	SecurityContextHolder.getContext().setAuthentication(authentication);
        	String jwt =jwtUtil.generateToken(authRequest.getUserName());
        	System.out.println(jwt);
        	UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
     		List<String> roles = userDetails.getAuthorities().stream()
     				.map(item -> item.getAuthority())
     				.collect(Collectors.toList());

     		return ResponseEntity.ok(new JwtResponse(jwt, 
     												 userDetails.getId(), 
     												 userDetails.getUsername(), 
     												 userDetails.getEmail(), 
     												 roles));
        } catch (Exception ex) {
        	return ResponseEntity.badRequest().body(new MessageResponse("Invalid username or password!"));
            //throw new Exception("inavalid username/password");
        }
    
    }
	
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody Users user) {
    	Users duplicateUser= userService.duplicateUserNameAnsEmail(user.getUserName(), user.getEmail());
    	if( duplicateUser!=null) {
    		return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Username and Email combination is already taken!"));
    	}
    	user.setPassword(encoder.encode(user.getPassword()));
    	userService.saveUser(user);
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    	
    }
	

}
