package com.digitalbooks.user.dto;

import java.sql.Blob;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;


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
		// TODO Auto-generated constructor stub
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
