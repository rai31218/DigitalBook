package com.digitalbooks.user.service;

import java.sql.Blob;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.digitalbooks.user.dto.Books;
import com.digitalbooks.user.model.Subscription;
import com.digitalbooks.user.model.Users;
import com.digitalbooks.user.payload.response.MessageResponse;
import com.digitalbooks.user.pyload.request.SubscriptionPayLoad;
import com.digitalbooks.user.repository.SubscriptionRepository;
import com.digitalbooks.user.repository.UserRepository;

@Service
public class SubscriptionService {
	
	@Autowired
	private SubscriptionRepository subscriptionRepository;
	
	@Autowired
	private UserRepository userRepository;
	

	public ResponseEntity<?> subscribe(int bookId,Books responseBook, SubscriptionPayLoad subscribe) {
       if(responseBook!=null) {
    	   
    	   Users user =userRepository.findByEmail(subscribe.getEmail());

    		   Subscription subscription = new Subscription();
    		   subscription.setId(UUID.randomUUID().toString());
    		   subscription.setBookId(bookId);
    		   subscription.setUser(user);
    		   subscription.setCancelled(false);
    		   subscription.setDateOfCancellation(null);
    		   subscription.setDateOfSubscription(new Date());
    		   Subscription savedSubscription = subscriptionRepository.save(subscription);
    		   
    		   return ResponseEntity.ok(new com.digitalbooks.user.payload.response.MessageResponse("Subscription is successfully added with subscription id: "+ savedSubscription.getId())); 	
		}
		else {
			return ResponseEntity.badRequest().body(new com.digitalbooks.user.payload.response.MessageResponse("Book does not exist"));
		}
	}


	public boolean checkUser(SubscriptionPayLoad subscribe) {
		 Users user =userRepository.findByEmail(subscribe.getEmail());
		 return user.getRoles().getId()==2;
		
	}


	public boolean checkduplicateSubscription(int bookId, SubscriptionPayLoad subscribe) {
		Users user =userRepository.findByEmail(subscribe.getEmail());
		Subscription subscription = subscriptionRepository.findByBookIdAndUser(bookId,user);
		boolean isDuplicate = false;
		
		if(subscription!=null && subscription.getUser().getId()==user.getId() &&  subscription.getBookId()==bookId
				&& !subscription.isCancelled()) {
			isDuplicate=true;
		}
		 
		return isDuplicate;
		
	}


	public int getUserIdByEmail(String email) {
		Optional<Integer> userId = subscriptionRepository.fetchUserByEmail(email);
		
		return userId.isEmpty()? 0 : userId.get();
		
	}


	public Optional<List<Subscription>> fetchSubscribedBooksForUser(int userId) {
		
		return subscriptionRepository.fetchSubscriptionByUser(userId);
		
	}


	public boolean checkUserExists(SubscriptionPayLoad subscribe) {
		Optional<Integer> userId= subscriptionRepository.fetchUserByEmail(subscribe.getEmail());
		
		return userId.isEmpty() ? false : true;
		
	}



	public Subscription fetchSubscriptionById(String subscriptionId) {
		Optional<Subscription> subscription = subscriptionRepository.findById(subscriptionId);
		if(!subscription.isEmpty()) {
			if( !subscription.get().isCancelled()) {
				return subscription.get();
			}
			else {
				return null;
			}
			
		}
		else {
			return null;
		}
		
		
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


	public ResponseEntity<MessageResponse> cancelSubscription(String subscriptionId) {
		Optional<Subscription> subscription = subscriptionRepository.findById(subscriptionId);
		if(!subscription.isEmpty()) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.HOUR_OF_DAY, -24);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
		    calendar.set(Calendar.MINUTE, 0);
		    calendar.set(Calendar.SECOND, 0);
		    calendar.set(Calendar.MILLISECOND, 0);
			Date twetlyfourHoursAgo = calendar.getTime();
			if(subscription.get().getDateOfSubscription().before(twetlyfourHoursAgo)) {
				return ResponseEntity.ok(new MessageResponse("Cancellation duration over"));
			}
			else {
				subscription.get().setCancelled(true); // cancelling subscription
				subscriptionRepository.save(subscription.get());
				return ResponseEntity.ok(new MessageResponse("Cancel request processed"));
			}
		}
		else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("NO Subscription is found"));
		}
		
		
	}


}
