package com.digitalbooks.book.repository;

import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.digitalbooks.book.model.Books;

public interface BookRepository extends JpaRepository<Books, Integer> {


	@Query(value="Select B.ID, B.TITLE, B.LOGO, B.AUTHOR, B.CATEGORY, B.PRICE, B.PUBLISHER, B.PUBLISHED_DATE, B.active, B.content from BOOKS B where B.category=:category and B.title=:title and B.author=:authorId and B.price=:price and B.publisher=:publisher and B.active=1", nativeQuery = true)
	public Optional<Books> searchBook(String category, String title, int authorId, int price, String publisher);

	public Optional<Books> findByIdAndAuthorId(int bookId, int authorId);

	public Optional<Books> findByTitleAndAuthorId(String title, int authorId);

	@Query(value="Select B.id, B.TITLE, B.LOGO, B.AUTHOR, B.CATEGORY, B.PRICE, B.PUBLISHER, B.PUBLISHED_DATE, B.active, B.content from BOOKS B where  B.author=:authorId and B.id=:bookId and B.active=1", nativeQuery = true)
	public Optional<Books> searchByIdAndAuthorId(int bookId, int authorId);

	public Optional<List<Books>> findByAuthorId(int userId);

	@Query(value="Select COUNT(*) from BOOKS B where  B.author=:userId and B.active=1", nativeQuery = true)
	public int fetchCountOfBooks(int userId);



}
