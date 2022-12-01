package com.digitalbooks.user.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="USERS")
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID")
	private int id;
	
	@Column(name="USERNAME")
	private String userName;
	
	@Column(name="PASSWORD")
	private String password;
	
	@Column(name="EMAIL")
	private String email;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ROLE_FID")
	private Roles roles;
	
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	
	
	public Users() {
		
	}
	public Users(int id, String userName, String password, String email, Roles roles, Date createdDate) {
		super();
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.roles = roles;
		this.createdDate = createdDate;
	}
	public Users(String userName, String email, String password) {
		this.userName = userName;
		this.password = password;
		this.email = email;
		
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	public Roles getRoles() {
		return roles;
	}
	public void setRoles(Roles roles) {
		this.roles = roles;
	}
	
	
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	@Override
	public String toString() {
		return "Users [id=" + id + ", userName=" + userName + ", password=" + password + ", email=" + email + ", roles="
				+ roles + ", createdDate=" + createdDate + "]";
	}
	
	
	
	
}
