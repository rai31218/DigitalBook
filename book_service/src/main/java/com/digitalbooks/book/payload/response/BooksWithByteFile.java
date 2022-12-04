package com.digitalbooks.book.payload.response;

import com.digitalbooks.book.model.Books;


public class BooksWithByteFile {


    public byte[] file; 
	
	private Books books;

	public BooksWithByteFile(byte[] file, Books books) {
		super();
		this.file = file;
		this.books = books;
	}

	public BooksWithByteFile() {
		super();
		// TODO Auto-generated constructor stub
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public Books getBooks() {
		return books;
	}

	public void setBooks(Books books) {
		this.books = books;
	}


}
