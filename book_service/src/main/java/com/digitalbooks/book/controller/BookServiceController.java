package com.digitalbooks.book.controller;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.digitalbooks.book.model.Books;
import com.digitalbooks.book.service.BookService;



@RestController
@RequestMapping("/api/v1/digitalbooks")
public class BookServiceController {
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	BookService bookService;
	
//	@GetMapping("/test")
//	public String test() {
//		System.out.println("Calling Book service");
//		String response= restTemplate.getForObject("http://USER-SERVICE/user/test", String.class);
//		return "Book Service and "+response+" from Book Service";
//	}
	
	@PostMapping("/author/{author-id}/books")
    public Books saveBook(@RequestParam(value="file", required=false) MultipartFile file, @ModelAttribute Books book, @PathVariable("author-id") int authorId) {


		    byte[] bytes=null;
		    Blob blob= null;
			try {
				
			if(file!=null) {
				bytes = file.getBytes();
				blob = new javax.sql.rowset.serial.SerialBlob(bytes);
			}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SerialException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   
		    
		book.setLogo(blob);
		book.setAuthorId(authorId);
		bookService.saveBook(book);
		System.out.println(blob);
		return book;
    	
    }
	@PostMapping("/author/{author-id}/books/{book-id}")
    public void updateBook(@PathVariable("author-id") int authorId , @PathVariable("book-id") int bookId,
    		@RequestBody Books book) {
		bookService.updateBook(authorId,bookId,book );
	}


	
	
	

}
