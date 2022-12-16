package com.digitalbooks.book.controller;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.digitalbooks.book.model.Books;
import com.digitalbooks.book.payload.response.BookContentResponse;
import com.digitalbooks.book.payload.response.BooksResponse;
import com.digitalbooks.book.payload.response.BooksWithByteFile;
import com.digitalbooks.book.payload.response.MessageResponse;
import com.digitalbooks.book.service.BookService;

@RestController
@RequestMapping("/digitalbooks")
public class BookServiceController {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	BookService bookService;

	
	@GetMapping("/test")
	public String test() {
		return "It is User Service";
	}
	
	@PostMapping("/author/{author-id}")
	public int saveBook( @RequestBody BooksWithByteFile book,
			@PathVariable("author-id") int authorId) {
		Blob blob = null;
		try {
	    if(book!=null && book.getFile()!=null) {
	    	blob = new javax.sql.rowset.serial.SerialBlob(book.getFile());
	    }
     	
			
		}  catch ( Exception e) {
			e.printStackTrace();
		}
		return bookService.saveBook(blob, book, authorId);

	}

	@PostMapping("/author/{author-id}/{book-id}")
	public Books updateBook(@RequestBody BooksWithByteFile book,
			@PathVariable("author-id") int authorId, @PathVariable("book-id") int bookId
			) {
		Blob blob = null;
		try {
			 if(book!=null && book.getFile()!=null) {
			    	blob = new javax.sql.rowset.serial.SerialBlob(book.getFile());
			    }
		}  catch (SQLException e) {
			e.printStackTrace();
		}
		return bookService.updateBook(blob, book,authorId,bookId);
	}

	@GetMapping("/searchBook/logo/{category}/{title}/{author}/{price}/{publisher}")
	public byte[] getBookLogo(@PathVariable("category") String category, @PathVariable("title") String title,
			@PathVariable("author") int authorId, @PathVariable("price") int price,
			@PathVariable("publisher") String publisher) {
		System.out.println("here");
		return bookService.getBookForLogo(category, title, authorId, price, publisher);

	}

	@GetMapping("/searchBook/{category}/{title}/{author}/{price}/{publisher}")

	public Books getBook(@PathVariable("category") String category, @PathVariable("title") String title,
			@PathVariable("author") int authorId, @PathVariable("price") int price,
			@PathVariable("publisher") String publisher) {
		Books book= bookService.getBookForSearch(category, title, authorId, price, publisher);
		return book ;

	}

	@GetMapping("/getBook/{book-id}")
	public BooksResponse getBookById(@PathVariable("book-id") int bookId) {
		return bookService.getBookById(bookId);
	}

	@GetMapping("/getBook/subscribed/logo/{book-id}")
	public byte[] getBookLogoForSubscribedBook(@PathVariable("book-id") int bookId) {
		return  bookService.getSubscribedBookForLogo(bookId);

	}

	@GetMapping("/getBook/subscribed/{book-id}")

	public Books getBookForSubscribedBook(@PathVariable("book-id") int bookId) {
		return bookService.getSubscribedBook(bookId);
	}

	@GetMapping("/getBook/subscribed/content/{book-id}")
	public BookContentResponse getSubscribedBookContent(@PathVariable("book-id") int bookId) {
		return bookService.getSubscribedBookContent(bookId);
	}

	@GetMapping("searchBook/cancel/{user-id}/{book-id}/{block}")
	public ResponseEntity<MessageResponse> blockBook(@PathVariable("user-id") int userId,
			@PathVariable("book-id") int bookId, @PathVariable("block") String block) {
		Books book = bookService.checkBookAvailability(bookId);
		Books bookAndUser = bookService.checkBookUserAvailability(userId, bookId);
		if (book == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("The book does not exist"));
		} else if (bookAndUser == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new MessageResponse("The book does not belong to the mentioned author"));

		} else if (block.equalsIgnoreCase("yes") || block.equalsIgnoreCase("no") ) {
			Books savedBook = bookService.blockBook(bookId, block);
			return ResponseEntity.ok(new MessageResponse("Updated book status"));
		}
		
		else {
			return ResponseEntity.ok(new MessageResponse("Not a proper instruction"));

		}
	}
	
	
	
	@GetMapping("searchBook/count/book/{user-id}")
	public int getCountofWrittenBook(@PathVariable("user-id") int userId) {
		return bookService.getCount(userId);
		
	}
	@GetMapping("searchBook/createdbook/logo/{user-id}")
	public byte[][] getBookLogoForCreatedBook(@PathVariable("user-id") int userId) {
		return  bookService.getCreatedBookForLogo(userId);

	}

	@GetMapping("searchBook/createdbook/{user-id}")

	public List<Books> getBookForCreatedBook(@PathVariable("user-id") int userId) {
		return bookService.getCreatedBook(userId);
	}
	
	
	@GetMapping("searchBook/logo/{book-id}")
	public byte[] getBookLogoForUpdation(@PathVariable("book-id") int bookId) {
		return  bookService.getBookLogoForUpdation(bookId);

	}
	
	@GetMapping("searchBook/{book-id}")
	public Books getBookForUpdation(@PathVariable("book-id") int bookId) {
		return bookService.getBookForUpdation(bookId);
	}
	

}
