package Beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import Utils.UserRoleEnum;

public class User {
	private final String username;
	private final UserRoleEnum role;
	private Stack<List<String>> lastVisitedPages;
	private String toAdd[];
	private List<String> goBackPage;
	
	public User(String username, UserRoleEnum role, String lastVisitedPage) {
		this.username = username;
		this.role = role;
		
		this.toAdd = new String[3];
		this.goBackPage = new ArrayList<>();
		this.lastVisitedPages = new Stack<List<String>>();
		
		this.toAdd[0] = "";
		this.toAdd[1] = "";
		this.toAdd[2] = "";
		
		this.goBackPage.addAll(List.of(lastVisitedPage,"",""));
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public UserRoleEnum getRole() {
		return role;
	}
	
	public void addLastVisitedPage(String lastVisitedPage, String paramName, String param) {
		if(toAdd[0] != "") {
			this.lastVisitedPages.add(List.of(this.toAdd[0], this.toAdd[1], this.toAdd[2]));
		}
		
		toAdd[0] = lastVisitedPage;
		toAdd[1] = paramName;
		toAdd[2] = param;
		
		if(this.lastVisitedPages.size() != 0)
			this.goBackPage = this.lastVisitedPages.lastElement();
	}
	
	public void updateGoBackPage(String lastVisitedPage, String paramName, String param) {
		if(this.lastVisitedPages.size() != 0 ) {
			List<String> temp = this.lastVisitedPages.pop();
			
			if(this.toAdd[0] != lastVisitedPage && this.toAdd[2] != param) {
				toAdd[0] = lastVisitedPage;
				toAdd[1] = paramName;
				toAdd[2] = param;
			}
			
			if(this.lastVisitedPages.size() != 0)
				this.goBackPage = this.lastVisitedPages.lastElement();
			else
				this.goBackPage = temp;
		}
	}
	
	public String getLastVisitedPage() {
		return this.goBackPage.get(0);
	}
	
	public String getParamName() {
		return this.goBackPage.get(1);
	}
	
	public String getParam() {
		return this.goBackPage.get(2);
	}
}
