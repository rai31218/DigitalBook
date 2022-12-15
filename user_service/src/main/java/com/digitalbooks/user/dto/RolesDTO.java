package com.digitalbooks.user.dto;


public class RolesDTO {

	private int id;

	private String roleName;

	public RolesDTO() {
		super();
		
	}

	public RolesDTO(int id) {
		this.id = id;
		
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
