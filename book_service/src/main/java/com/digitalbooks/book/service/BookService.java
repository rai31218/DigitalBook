package com.digitalbooks.book.service;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitalbooks.book.model.Books;
import com.digitalbooks.book.repository.BookRepository;

@Service
public class BookService {

	@Autowired 
	private BookRepository bookRepository;
	
	public void saveBook(Books book) {
	//	byte[] byteArray = book.getLogo();
		bookRepository.save(book);
		
	}

	public void updateBook(int authorId, int bookId, Books book) {
		// TODO Auto-generated method stub
		
	}

}
