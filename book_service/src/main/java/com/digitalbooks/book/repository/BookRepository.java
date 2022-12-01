package com.digitalbooks.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.digitalbooks.book.model.Books;

public interface BookRepository extends JpaRepository<Books, Integer> {

}
