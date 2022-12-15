package com.digitalbooks.user.dto;

import java.util.Date;



public class UsersDTO {

	private int id;

	private String userName;

	private String password;

	private String email;

	private RolesDTO roles;

	private Date createdDate;
	
	
	
	public UsersDTO() {
		
	}
	public UsersDTO(int id, String userName, String password, String email, RolesDTO roles, Date createdDate) {
		super();
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.roles = roles;
		this.createdDate = createdDate;
	}
	public UsersDTO(String userName, String email, String password) {
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
	
	
	public RolesDTO getRoles() {
		return roles;
	}
	public void setRoles(RolesDTO roles) {
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
