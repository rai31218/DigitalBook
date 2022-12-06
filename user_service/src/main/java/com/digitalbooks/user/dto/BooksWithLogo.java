package com.digitalbooks.user.dto;

import java.sql.Blob;



public class BooksWithLogo {


    public Blob logo; 
	
	private Books books;

	public BooksWithLogo(Blob logo, Books books) {
		super();
		this.logo = logo;
		this.books = books;
	}

	public BooksWithLogo() {
		super();
	}

	public Blob getLogo() {
		return logo;
	}

	public void setLogo(Blob logo) {
		this.logo = logo;
	}

	public Books getBooks() {
		return books;
	}

	public void setBooks(Books books) {
		this.books = books;
	}

}
