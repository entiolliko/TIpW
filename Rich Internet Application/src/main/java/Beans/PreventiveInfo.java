package Beans;

import java.util.*;

public class PreventiveInfo {
	private String preventiveID; //ID da 10 caratteri
	private String productCode;	//ID da 10 caratteri
	private String productName;
	private String clientUsername;
	private Float price = 0.0f;
	private String employeeUsername = null;
	private String imgPath;
	private List<Option> optionsInPreventive;
	
	public String getPreventiveID() {
		return this.preventiveID;
	} 
	
	public String getProductCode() {
		return this.productCode;
	} 
	
	public String getProductName() {
		return this.productName;
	} 
	
	public String getClientUsername() {
		return this.clientUsername;
	} 
	
	public Float getPrice() {
		return this.price;
	} 
	
	public String getEmployeeUsername() {
		return this.employeeUsername;
	}
	
	public String getImgPath() {
		return this.imgPath;
	} 
	
	public List<Option> getOptionsInPreventive() {
		return this.optionsInPreventive;
	} 
	
	public void setPreventiveID(String preventiveID) {
		this.preventiveID = preventiveID;
	} 
	
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	} 
	
	public void setProductName(String productName) {
		this.productName = productName; 
	} 
	
	public void setClientUsername(String clientUsername) {
		this.clientUsername = clientUsername;
	} 
	
	public void setPrice(Float price) {
		this.price = price;
	} 
	
	public void setEmployeeUsername(String employeeUsername) {
		this.employeeUsername = employeeUsername;
	} 
	
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	
	public void setOptionsInPreventive(List<Option> optionsInPreventive) {
		this.optionsInPreventive = optionsInPreventive;
	} 
	
	
}
