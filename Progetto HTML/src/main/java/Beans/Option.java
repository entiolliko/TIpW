package Beans;

import Utils.OptionType;

public class Option {
	
	private String optionCode; //Codice da 10 caratteri
	private String productCode; //Codice da 10 caratteri
	private String optionName; 
	private OptionType optionType;
	
	public String getOptionCode(){
		return this.optionCode;
	}
	
	public String getProductCode(){
		return this.productCode;
	}
	
	public String getOptionName(){
		return this.optionName;
	}
	
	public OptionType getOptionType(){
		return this.optionType;
	}
	
	public void setOptionCode(String optionCode){
		this.optionCode = optionCode;
	}
	
	public void setProductCode(String productCode){
		this.productCode = productCode;
	}
	
	public void setOptionName(String optionName){
		this.optionName = optionName;
	}
	
	public void setOptionType(OptionType optionType){
		this.optionType = optionType;
	}
}
