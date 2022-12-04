package com.digitalbooks.book.controller;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;

import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

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
		System.out.println("Calling User service");
		return "It is User Service";
	}
	
	@PostMapping("/author/{author-id}")
	// public Books saveBook(@RequestParam(value="file", required=false)
	// MultipartFile file, @ModelAttribute Books book, @PathVariable("author-id")
	// int authorId) {

	public Books saveBook( @RequestBody BooksWithByteFile book,
			@PathVariable("author-id") int authorId) {
		Blob blob = null;
		try {
     	blob = new javax.sql.rowset.serial.SerialBlob(book.getFile());
			
		}  catch (SerialException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		Books savedBook = bookService.saveBook(blob, book, authorId);

		return savedBook;

	}

	@PostMapping("/author/{author-id}/{book-id}")
	public Books updateBook(@RequestBody BooksWithByteFile book,
			@PathVariable("author-id") int authorId, @PathVariable("book-id") int bookId
			) {
		Blob blob = null;
		try {
     	blob = new javax.sql.rowset.serial.SerialBlob(book.getFile());
			
		}  catch (SerialException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Books response = bookService.updateBook(blob, book,authorId,bookId);
		System.out.println("Response:::::::::::::::::" +response);
		return response;
	}

	@GetMapping("/searchBook/logo/{category}/{title}/{author}/{price}/{publisher}")
	public byte[] getBookLogo(@PathVariable("category") String category, @PathVariable("title") String title,
			@PathVariable("author") int authorId, @PathVariable("price") int price,
			@PathVariable("publisher") String publisher) {
		byte byteArray[] = bookService.getBookForLogo(category, title, authorId, price, publisher);
		return byteArray;

	}

	@GetMapping("/searchBook/{category}/{title}/{author}/{price}/{publisher}")

	public Books getBook(@PathVariable("category") String category, @PathVariable("title") String title,
			@PathVariable("author") int authorId, @PathVariable("price") int price,
			@PathVariable("publisher") String publisher) {
		Books books = bookService.getBookForSearch(category, title, authorId, price, publisher);
		return books;

	}

	@GetMapping("/getBook/{book-id}")
	public BooksResponse getBookById(@PathVariable("book-id") int bookId) {
		BooksResponse bookResponse = bookService.getBookById(bookId);
		return bookResponse;
	}

	@GetMapping("/getBook/subscribed/logo/{book-id}")
	public byte[] getBookLogoForSubscribedBook(@PathVariable("book-id") int bookId) {
		byte byteArray[] = bookService.getSubscribedBookForLogo(bookId);
		return byteArray;

	}

	@GetMapping("/getBook/subscribed/{book-id}")

	public Books getBookForSubscribedBook(@PathVariable("book-id") int bookId) {
		Books books = bookService.getSubscribedBook(bookId);
		return books;
	}

	@GetMapping("/getBook/subscribed/content/{book-id}")
	public BookContentResponse getSubscribedBookContent(@PathVariable("book-id") int bookId) {
		BookContentResponse bookContent = bookService.getSubscribedBookContent(bookId);
		return bookContent;
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
			return ResponseEntity.ok(new MessageResponse("Updated book status\n" + savedBook));
			//return ResponseEntity.badRequest().body(new MessageResponse("Not a proper instruction"));

		}
		
		else {
			//Books savedBook = bookService.blockBook(bookId, block);
			//return ResponseEntity.ok(new MessageResponse("Updated book status\n" + savedBook));
			return ResponseEntity.ok(new MessageResponse("Not a proper instruction"));
			//return ResponseEntity.badRequest().body(new MessageResponse("Not a proper instruction"));
		}
	}

}
