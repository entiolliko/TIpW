package Beans;

public class Preventive {
	private String preventiveID; //ID da 10 caratteri
	private String productName; //Il nome del prodotto
	private String productCode;
	
	
	public String getPreventiveID() {
		return this.preventiveID;
	}
	
	public String getProductName() {
		return this.productName;
	}
	
	public String getProductCode() {
		return this.productCode;
	}
	
	public void setPreventiveID(String preventiveID) {
		this.preventiveID = preventiveID;
	}
	
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
}
