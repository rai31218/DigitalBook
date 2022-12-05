package com.digitalbooks.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.digitalbooks.user.model.Subscription;
import com.digitalbooks.user.model.Users;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, String>{

	public Subscription findByBookId(int bookId);

	@Query(value = "Select U.id from Users U where U.email=:email",nativeQuery=true)
	public Optional<Integer> fetchUserByEmail(String email);

	@Query(value = "Select S.ID, S.USER_FID, S.BOOK_ID, S.DATE_OF_SUBSCRIPTION, S.CANCELLED, S.DATE_OF_CANCEL from Subscription S where S.USER_FID=:userId and cancelled=0",nativeQuery=true)
	public Optional<List<Subscription>> fetchSubscriptionByUser(int userId);


	public Subscription findByBookIdAndUser(int bookId, Users user);

}
