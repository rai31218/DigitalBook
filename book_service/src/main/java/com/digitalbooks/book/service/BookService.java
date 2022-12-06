package com.digitalbooks.book.service;


import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitalbooks.book.model.Books;
import com.digitalbooks.book.payload.response.BookContentResponse;
import com.digitalbooks.book.payload.response.BooksResponse;
import com.digitalbooks.book.payload.response.BooksWithByteFile;
import com.digitalbooks.book.repository.BookRepository;

@Service
@Transactional
public class BookService {

	@Autowired 
	private BookRepository bookRepository;
	
	public Books saveBook(Blob blob, BooksWithByteFile book, int authorId) {
		Optional<Books> existingBook = bookRepository.findByTitleAndAuthorId(book.getBooks().getTitle(),authorId);
		if(existingBook.isEmpty()) { // author with same book title check
			Books bookToBeSaved = new Books();
			bookToBeSaved.setLogo(blob);
			bookToBeSaved.setActive(book.getBooks().isActive());
			bookToBeSaved.setAuthorId(authorId);
			bookToBeSaved.setCategory(book.getBooks().getCategory());
			bookToBeSaved.setContent(book.getBooks().getContent());
			bookToBeSaved.setPrice(book.getBooks().getPrice());
			bookToBeSaved.setPublishedDate(new Date());
			bookToBeSaved.setPublisher(book.getBooks().getPublisher());
			bookToBeSaved.setTitle(book.getBooks().getTitle());
			return bookRepository.save(bookToBeSaved);
		}
		else {
			return null;
		}

	}

	public Books updateBook(Blob blob, BooksWithByteFile book, int authorId, int bookId) {
		
		Optional<Books> existingBookForUser = bookRepository.searchByIdAndAuthorId(bookId,authorId);
		if(!existingBookForUser.isEmpty()) {


			existingBookForUser.get().setLogo(blob);
			existingBookForUser.get().setTitle(book.getBooks().getTitle());
			existingBookForUser.get().setPrice(book.getBooks().getPrice());
			existingBookForUser.get().setPublisher(book.getBooks().getPublisher());
			existingBookForUser.get().setActive(book.getBooks().isActive());
			existingBookForUser.get().setContent(book.getBooks().getContent());
			existingBookForUser.get().setCategory(book.getBooks().getCategory());
			return bookRepository.save(existingBookForUser.get());
		}
		else {
			return null;
         }
		
	}

	public BooksResponse getBookById(int bookId) {
	Optional<Books> book =	bookRepository.findById(bookId);
	if(!book.isEmpty()) {
		BooksResponse bookResponse =  new BooksResponse();
		bookResponse.setAuthorId(book.get().getAuthorId());
		bookResponse.setCategory(book.get().getCategory());
		bookResponse.setPrice(book.get().getPrice());
		bookResponse.setPublishedDate(book.get().getPublishedDate());
		bookResponse.setPublisher(book.get().getPublisher());
		bookResponse.setTitle(book.get().getTitle());
		bookResponse.setActive(book.get().isActive());
		return bookResponse;
	}
	else {
		return null;
	}

		
	}

	public byte[] getBookForLogo(String category, String title, int authorId, int price, String publisher) {
		
		byte[] byteArray = null;
		try {
			Optional<Books> book =	bookRepository.searchBook(category,title,authorId,price,publisher);
			byteArray = book.isEmpty()? null : book.get().getLogo().getBytes(1,(int)book.get().getLogo().length());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return byteArray;
	}

	public Books getBookForSearch(String category, String title, int authorId, int price, String publisher) {
		Optional<Books> book =	bookRepository.searchBook(category,title,authorId,price,publisher);
		return book.isEmpty()?null:book.get();
	}

	public byte[] getSubscribedBookForLogo(int bookId) {

		byte[] byteArray = null;
		try {
			Optional<Books> book =	bookRepository.findById(bookId);
			byteArray = book.isEmpty()? null : book.get().getLogo().getBytes(1,(int)book.get().getLogo().length());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return byteArray;
	}

	public Books getSubscribedBook(int bookId) {
		Optional<Books> book =	bookRepository.findById(bookId);
		return book.isEmpty()?null:book.get();
	}

	public BookContentResponse getSubscribedBookContent(int bookId) {
		Optional<Books> book =	bookRepository.findById(bookId);
		BookContentResponse bookContent = new BookContentResponse();
		if(!book.isEmpty()) {
			bookContent.setContent(book.get().getContent());
			bookContent.setTitle(book.get().getTitle());
			bookContent.setActive(book.get().isActive());
			return bookContent;
		}
		
		else{
			return null;
		}
	}

	public Books checkBookAvailability( int bookId) {
		Optional<Books> book =	bookRepository.findById(bookId);
		if(!book.isEmpty()) {
			return book.get();
		}
		else {
			return null;
		}
		
	}

	public Books checkBookUserAvailability(int userId, int bookId) {
		Optional<Books> book =	bookRepository.findById(bookId);
		if(!book.isEmpty() && book.get().getAuthorId()==userId) {		
				return book.get();	
		}
		
			return null;
				
	}

	public Books blockBook(int bookId, String block) {
		Optional<Books> book =	bookRepository.findById(bookId);
		Books savedBook  = null; 
		if(block.equalsIgnoreCase("yes") && !book.isEmpty()) {
			
				book.get().setActive(false);
				 savedBook = bookRepository.save(book.get());
			
			
		}
		if(block.equalsIgnoreCase("no") && !book.isEmpty()) {
			
				book.get().setActive(true);
				 savedBook = bookRepository.save(book.get());
			
		}
		
		return savedBook;
		
	}



}
