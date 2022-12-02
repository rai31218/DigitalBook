package com.digitalbooks.book.service;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Optional;

import javax.sql.rowset.serial.SerialException;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.digitalbooks.book.model.Books;
import com.digitalbooks.book.payload.response.MessageResponse;
import com.digitalbooks.book.repository.BookRepository;

@Service
public class BookService {

	@Autowired 
	private BookRepository bookRepository;
	
	public void saveBook(Books book) {
	//	byte[] byteArray = book.getLogo();
		bookRepository.save(book);
		
	}

	public ResponseEntity<?> updateBook(MultipartFile file, int authorId, int bookId, Books book) {
		Optional<Books> existingBook=  bookRepository.findById(bookId);
		
		if(!existingBook.isEmpty()) {
			byte[] bytes=null;
		    Blob blob= null;
			try {
				
			if(file!=null) {
				bytes = file.getBytes();
				blob = new javax.sql.rowset.serial.SerialBlob(bytes);
			}
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SerialException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		   
			book.setLogo(blob);
			existingBook.get().setLogo(book.getLogo());
			existingBook.get().setTitle(book.getTitle());
			existingBook.get().setPrice(book.getPrice());
			existingBook.get().setPublisher(book.getPublisher());
			existingBook.get().setActive(book.isActive());
			existingBook.get().setContent(book.getContent());
			existingBook.get().setLogo(book.getLogo());
			existingBook.get().setCategory(book.getCategory());
			bookRepository.save(existingBook.get());
			return ResponseEntity.ok().body("Book is updated successfully");
	
		}
		else {
			return ResponseEntity.badRequest().body(new MessageResponse("Book does not exist!"));
            
		}
		
	}

}
