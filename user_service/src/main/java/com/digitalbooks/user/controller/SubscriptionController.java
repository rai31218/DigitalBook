package com.digitalbooks.user.controller;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.digitalbooks.user.dto.BookContentResponse;
import com.digitalbooks.user.dto.Books;
import com.digitalbooks.user.dto.BooksWithLogo;
import com.digitalbooks.user.model.Subscription;
import com.digitalbooks.user.payload.response.MessageResponse;
import com.digitalbooks.user.pyload.request.SubscriptionPayLoad;
import com.digitalbooks.user.service.SubscriptionService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(value = "/digitalbooks")
public class SubscriptionController {

	@Autowired
	SubscriptionService subscriptionService;

	@Autowired
	RestTemplate restTemplate;

	String bookUrl = "http://localhost:8082/digitalbooks/getBook/";

	public static final String USER_NOT_FOUND = "User is not found";
	public static final String BOOK_NOT_FOUND = "No book is available for current selection";
	public static final String SUBSCRIPTION_NOT_FOUND ="Subscription is not found";

	@PostMapping("/{book-id}/subscribe")
	public ResponseEntity<?> subscribe(@PathVariable("book-id") int bookId,
			@RequestBody SubscriptionPayLoad subscribe) {
		Books responseBook = null;

		boolean isUserExist = subscriptionService.checkUserExists(subscribe);

		try {
			responseBook = restTemplate.getForObject(bookUrl + bookId, Books.class);
		} catch (Exception ex) {
			return ResponseEntity.internalServerError().body(new com.digitalbooks.user.payload.response.MessageResponse(
					"There is some issue in fetching book with mentioned id"));
		}

		if (responseBook != null) {
			if (isUserExist) {
				boolean isDuplicate = subscriptionService.checkduplicateSubscription(bookId, subscribe);
				if (!isDuplicate) {
					boolean isReader = subscriptionService.checkUser(subscribe);
					if (isReader) {

						return subscriptionService.subscribe(bookId, responseBook, subscribe);
					} else {
						return ResponseEntity.badRequest()
								.body(new com.digitalbooks.user.payload.response.MessageResponse(
										"User does not exist with Reader Role"));
					}
				} else {
					return ResponseEntity.badRequest().body(new com.digitalbooks.user.payload.response.MessageResponse(
							"User has already subscribed for this book"));
				}
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(USER_NOT_FOUND));
			}
		}

		else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new MessageResponse("Book is not found to subscribe"));
		}

	}

	
	/**Get Subscription ID for front end**/
	@GetMapping("readers/{user-id}/{book-id}")
	public ResponseEntity<?> getSubscriptionId(@PathVariable("user-id") int userId, 
		@PathVariable("book-id") int bookId) {
		 Optional<List<Subscription>> id =subscriptionService.fetchSubscriptionIdByBookIdAndUserId(userId, bookId);
		 return ResponseEntity.ok(id.get().get(0));
	}
	
	
	
	
	
	
	
	/** Reader can fetch all subscribed books **/

	@GetMapping("readers/{emailId}/books")
	public ResponseEntity<?> getSubscribedBooks(@PathVariable("emailId") String email) throws Exception {
		Books responseBook = null;
		BooksWithLogo booksWithLogo = null;

		int userId = subscriptionService.getUserIdByEmail(email);
		if (userId == 0) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(USER_NOT_FOUND);
		} else {
			Optional<List<Subscription>> subscriptionList = subscriptionService.fetchSubscribedBooksForUser(userId);
			List<BooksWithLogo> listofBooks = new ArrayList<>();
			if (!subscriptionList.isEmpty() && !subscriptionList.get().isEmpty()) {
				for (int i = 0; i < subscriptionList.get().size(); i++) {
					int bookId = subscriptionList.get().get(i).getBookId();

					byte[] logo = restTemplate.getForObject(bookUrl + "subscribed/logo/" + bookId, byte[].class);
					responseBook = restTemplate.getForObject(bookUrl + "subscribed/" + bookId, Books.class);

					try {
						Blob blob = subscriptionService.fetchBlob(logo);
						booksWithLogo = new BooksWithLogo(blob, responseBook);
						listofBooks.add(booksWithLogo);
					} catch (Exception ex) {
						throw new Exception(ex.getMessage());
					}
					if (responseBook == null) {
						return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BOOK_NOT_FOUND);
					}
				}

				return ResponseEntity.ok(listofBooks);
			}

			else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No subscription found");
			}

		}

	}

	/**** Reader can fetch a subscribe book ****/

	@GetMapping("readers/{emailId}/books/{subscription-id}")
	public ResponseEntity<?> fetchBookContentBySubscriptionId(@PathVariable("emailId") String email,
			@PathVariable("subscription-id") String subscriptionId) throws Exception {
		Books responseBook = null;
		BooksWithLogo booksWithLogo = null;
		int userId = subscriptionService.getUserIdByEmail(email);
		Subscription subscription = subscriptionService.fetchSubscriptionById(subscriptionId);
		if (userId == 0) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(USER_NOT_FOUND);
		}

		if (subscription == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(SUBSCRIPTION_NOT_FOUND);
		}
		if (subscription.getUser().getId() != userId) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("The user does not have subscription for this book");
		} else {

			byte[] logo = restTemplate.getForObject(bookUrl + "subscribed/logo/" + subscription.getBookId(),
					byte[].class);
			responseBook = restTemplate.getForObject(bookUrl + "subscribed/" + subscription.getBookId(), Books.class);

			try {
				Blob blob = subscriptionService.fetchBlob(logo);
				booksWithLogo = new BooksWithLogo(blob, responseBook);
			} catch (Exception ex) {
				throw new Exception(ex.getMessage());
			}
			if (responseBook == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BOOK_NOT_FOUND);
			} else {
				return ResponseEntity.ok(booksWithLogo);
			}

		}

	}

	/**** Reader can read book content *****/
	@GetMapping("readers/{emailId}/books/{subscription-id}/read")
	public ResponseEntity<?> fetchBookContent(@PathVariable("emailId") String email,
			@PathVariable("subscription-id") String subscriptionId) throws Exception {

		BookContentResponse responseBook = null;
		int userId = subscriptionService.getUserIdByEmail(email);
		Subscription subscription = subscriptionService.fetchSubscriptionById(subscriptionId);
		if (userId == 0) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(USER_NOT_FOUND);
		}

		if (subscription == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(SUBSCRIPTION_NOT_FOUND);
		}
		if (subscription.getUser().getId() != userId) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("The user does not have subscription for this book");
		} else {

			responseBook = restTemplate.getForObject(bookUrl + "subscribed/content/" + subscription.getBookId(),
					BookContentResponse.class);

			if (responseBook == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BOOK_NOT_FOUND);
			} else if (!responseBook.isActive()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This book is blocked");
			} else {
				return ResponseEntity.ok(responseBook);
			}

		}

	}

	@PutMapping("readers/{email-id}/books/{subscription-id}/cancel-subscription")
	public ResponseEntity<?> cancelSubscription(@PathVariable("email-id") String email,
			@PathVariable("subscription-id") String subscriptionId) {

		int userId = subscriptionService.getUserIdByEmail(email);
		Subscription subscription = subscriptionService.fetchSubscriptionById(subscriptionId);
		if (userId == 0) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(USER_NOT_FOUND);
		}

		if (subscription == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(SUBSCRIPTION_NOT_FOUND);
		}
		if (subscription.getUser().getId() != userId) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("The user does not have subscription for this book");
		} else {
			ResponseEntity<?> subscriptionCancellation = subscriptionService.cancelSubscription(subscriptionId);
			return subscriptionCancellation;
		}

	}

}
