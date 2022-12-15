package com.digitalbooks.user.dto;

import java.sql.Blob;



public class BooksWithLogoandUserName {


    public Blob logo; 
	
	private Books books;
	
	private String userName;

	public BooksWithLogoandUserName(Blob logo, Books books, String userName) {
		super();
		this.logo = logo;
		this.books = books;
		this.userName = userName;
	}

	public BooksWithLogoandUserName(Blob logo, Books books) {
		super();
		this.logo = logo;
		this.books = books;
	}

	public BooksWithLogoandUserName() {
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	

}
