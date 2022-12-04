package com.digitalbooks.user.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="SUBSCRIPTION")
public class Subscription {
	
	@Id
	private String id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "USER_FID")
	private Users user;
	
	@Column(name="BOOK_ID")
	private int bookId;
	
	@Column(name="DATE_OF_SUBSCRIPTION")
	private Date dateOfSubscription;
	
	@Column(name="CANCELLED")
	private boolean isCancelled;
	
	@Column(name="DATE_OF_CANCEL")
	private Date dateOfCancellation;

	public Subscription(String id, Users user, int bookId, Date dateOfSubscription, boolean isCancelled,
			Date dateOfCancellation) {
		super();
		this.id = id;
		this.user = user;
		this.bookId = bookId;
		this.dateOfSubscription = dateOfSubscription;
		this.isCancelled = isCancelled;
		this.dateOfCancellation = dateOfCancellation;
	}

	public Subscription() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public Date getDateOfSubscription() {
		return dateOfSubscription;
	}

	public void setDateOfSubscription(Date dateOfSubscription) {
		this.dateOfSubscription = dateOfSubscription;
	}

	public boolean isCancelled() {
		return isCancelled;
	}

	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

	public Date getDateOfCancellation() {
		return dateOfCancellation;
	}

	public void setDateOfCancellation(Date dateOfCancellation) {
		this.dateOfCancellation = dateOfCancellation;
	}
	
	
	

}
