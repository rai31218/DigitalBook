package com.digitalbooks.user.dto;

import java.sql.Blob;
import java.util.Date;
import java.util.Optional;


public class Books {

	private int id;
	
	private String title;

	private String category;

	private double price;

	private int authorId;

	private String publisher;

	private Date publishedDate; 

	private boolean active;

	private String content;
	
	
	
	
	public Books() {
		super();
	}
	public Books(int id, String title, String category, double price, int authorId, String publisher,
			Date publishedDate, boolean active, String content
			) {
		super();
		this.id= id;
		this.title = title;
		this.category = category;
		this.price = price;
		this.authorId = authorId;
		this.publisher = publisher;
		this.publishedDate = publishedDate;
		this.active = active;
		this.content = content;
	}
	

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
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "Books [id=" + id + ", title=" + title + ", category=" + category + ", price=" + price + ", authorId="
				+ authorId + ", publisher=" + publisher + ", publishedDate=" + publishedDate + ", active=" + active
				+ ", content=" + content + "]";
	}



}
