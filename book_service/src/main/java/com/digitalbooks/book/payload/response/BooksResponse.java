package com.digitalbooks.book.payload.response;

import java.sql.Blob;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;


public class BooksResponse {
	private String title;

	private String category;

	private double price;

	private int authorId;

	private String publisher;

	private Date publishedDate;
	
	private boolean isActive;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public Date getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(Date publishedDate) {
		this.publishedDate = publishedDate;
	}
	

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public BooksResponse(String title, String category, double price, int authorId, String publisher,
			Date publishedDate, Boolean isActive) {
		super();
		this.title = title;
		this.category = category;
		this.price = price;
		this.authorId = authorId;
		this.publisher = publisher;
		this.publishedDate = publishedDate;
		this.isActive = isActive;
	}

	public BooksResponse() {
		super();
		// TODO Auto-generated constructor stub
	} 
	
	





}
