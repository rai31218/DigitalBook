package com.digitalbooks.user.service;

import java.sql.Blob;
import java.util.Optional;

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
	public int findByUserName(String userName) {
		Users user =  userRepository.findByUserName(userName);
		return  user == null ? 0 : user.getId() ;
		
	}

	public Blob fetchBlob(byte[] logo) throws Exception {
		Blob blob= null;
		if(logo!=null) {
			try {
				blob = new javax.sql.rowset.serial.SerialBlob(logo);
					
			} catch (Exception e) {
				throw new Exception("Some issue in fetching book logo");
				 }
		
		return blob;
		}
		else {
			return blob;
		}
	}

	public boolean checkAuthorExists(int authorId) {
		Optional<Users> user = userRepository.findById(authorId);
		if(!user.isEmpty() && user.get().getRoles().getId()==1) {
			
				return true;
			
		}
		return false;
	}

	
}
