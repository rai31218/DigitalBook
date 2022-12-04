package com.digitalbooks.user.pyload.request;

public class SubscriptionPayLoad {
	private int bookId;
	private String email ;
	
	
	
	public SubscriptionPayLoad() {
		super();
		// TODO Auto-generated constructor stub
	}



	public SubscriptionPayLoad(int bookId, String email) {
		super();
		this.bookId = bookId;
		this.email = email;
	}



	public int getBookId() {
		return bookId;
	}



	public void setBookId(int bookId) {
		this.bookId = bookId;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}
	
	

	

}
