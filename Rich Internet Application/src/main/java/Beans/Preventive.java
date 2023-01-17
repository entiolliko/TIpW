package Beans;

public class Preventive {
	private String preventiveID;
	private String productCode;
	private String productName;
	
	
	public String getPreventiveID() {
		return this.preventiveID;
	}
	
	public String getProductCode() {
		return this.productCode;
	}
	
	public String getProductName() {
		return this.productName;
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
