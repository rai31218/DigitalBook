package com.digitalbooks.user.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ROLES")
public class Roles {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="ROLE_NAME")
	private String roleName;
	
	
	
	public Roles() {
		super();
		
	}

	public Roles(int id, String roleName) {
		this.id = id;
		this.roleName = roleName;
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Override
	public String toString() {
		return "Roles [id=" + id + ", roleName=" + roleName + "]";
	}

	

}
