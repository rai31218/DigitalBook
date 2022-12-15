package com.digitalbooks.book.service;


import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitalbooks.book.model.Books;
import com.digitalbooks.book.payload.response.BookContentResponse;
import com.digitalbooks.book.payload.response.BooksResponse;
import com.digitalbooks.book.payload.response.BooksWithByteFile;
import com.digitalbooks.book.payload.response.MessageResponse;
import com.digitalbooks.book.repository.BookRepository;

@Service
@Transactional
public class BookService {

	@Autowired 
	private BookRepository bookRepository;
	
	public int saveBook(Blob blob, BooksWithByteFile book, int authorId) {
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
			Books savedBooks= bookRepository.save(bookToBeSaved);
			System.out.println("saved book Id: "+savedBooks.getId() );
			return savedBooks.getId();
					
		}
		else {
			return 0;
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

	public byte[][] getCreatedBookForLogo(int userId) {
		byte[] byteArray[] = new byte[1000][];
		try {
			Optional<List<Books>> book =	bookRepository.findByAuthorId(userId);
			if(book.isEmpty()) {
				byteArray=null;
			}
			else {
				System.out.println("length: "+byteArray.length);
				for(int i=0; i<book.get().size();i++) {
					byteArray[i]=book.get().get(i).getLogo().getBytes(1, (int)book.get().get(i).getLogo().length());
				}
			}
			
		//	byteArray = book.isEmpty()? null : book.get().getLogo().getBytes(1,(int)book.get().getLogo().length());
		} catch (SQLException e) {
			
		}
		return byteArray;
	}

	public List<Books> getCreatedBook(int userId) {
		Optional<List<Books>> book =	bookRepository.findByAuthorId(userId);
		return book.isEmpty()?null:book.get();
	}

	public int getCount(int userId) {
		return bookRepository.fetchCountOfBooks(userId);
		
	}
	
	public byte[] getBookLogoForUpdation(int bookId) {

		byte[] byteArray = null;
		try {
			Optional<Books> book =	bookRepository.findById(bookId);
			byteArray = book.isEmpty()? null : book.get().getLogo().getBytes(1,(int)book.get().getLogo().length());
		} catch (SQLException e) {
			
		}
		return byteArray;
	}

	public Books getBookForUpdation(int bookId) {
		Optional<Books> book1 = bookRepository.findById(bookId);
		if(book1.isPresent()) {
			return book1.get();
		}
		else {
			return null;
		}
		
	}



}
