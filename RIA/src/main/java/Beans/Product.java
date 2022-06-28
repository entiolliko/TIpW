package Beans;

public class Product {
	private String productCode; //Codice da 10 caratteri
	private String productName;
	private String imgPath;
	
	public String getProductCode() {
		return this.productCode;
	}
	
	public String getProductName() {
		return this.productName;
	}
	
	public String getImgPath() {
		return this.imgPath;
	}
	
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	
}
