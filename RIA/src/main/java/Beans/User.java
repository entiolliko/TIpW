package Beans;

import Utils.UserRoleEnum;

public class User {
	private final String username;
	private final UserRoleEnum role;
	
	public User(String username, UserRoleEnum role) {
		this.username = username;
		this.role = role;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public UserRoleEnum getRole() {
		return role;
	}
}
