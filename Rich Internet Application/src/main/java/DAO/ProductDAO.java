package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import Beans.*;
import Utils.*;


public class ProductDAO {
	private Connection connection;
	
	public ProductDAO(Connection connection) {
		this.connection = connection;
	}
	
	
	public List<Product> getAllProducts() throws SQLException{
		List<Product> products = new ArrayList<Product>();
		
		String query = "SELECT * FROM TIW_Progetto.PRODUCT";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query)) {
			try (ResultSet result = pstatement.executeQuery()) {
				while(result.next()) {
					Product temp = new Product();
					temp.setProductCode(result.getString("Code"));
					temp.setProductName(result.getString("Name"));
					temp.setImgPath(result.getString("ImgPath"));
					products.add(temp);
				}
			}
		}
		return products;
	}
	
	public boolean doesProductExist(String productCode) throws SQLException {
		String query = "SELECT * FROM TIW_Progetto.PRODUCT WHERE Code = ?";
		boolean isPresent = false;
		try (PreparedStatement pstatement = connection.prepareStatement(query)) {
			pstatement.setString(1, productCode);
			try (ResultSet result = pstatement.executeQuery()) {
				if(result.isBeforeFirst()) {
					isPresent = true;
				}
			}
		}
		return isPresent;
	}
	
	public Map<String, SimpleProduct> getAllSimpleProducts() throws SQLException{
		Map<String, SimpleProduct> products = new HashMap<>();
		String tempKey;
		SimpleProduct temp;
		
		String query = "SELECT Code, Name FROM TIW_Progetto.PRODUCT";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query)) {
			try (ResultSet result = pstatement.executeQuery()) {
				while(result.next()) {
					tempKey = result.getString("Code");
					temp = new SimpleProduct();
					temp.setProductCode(result.getString("Code"));
					temp.setProductName(result.getString("Name"));
					temp.setOptions((getOptionsByProductID(result.getString("Code")).stream().map(x -> x.getOptionCode()).toList()));
					products.put(tempKey, temp);
				}
			}
		}
		return products;
	}
	
	public String getImgPathFromProductCode(String productCode) throws SQLException {
		String path = "";
		
		String query = "SELECT ImgPath FROM TIW_Progetto.PRODUCT WHERE Code = ?";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query)) {
			pstatement.setString(1, productCode);
			try (ResultSet result = pstatement.executeQuery()) {
				if(result.isBeforeFirst()) {
					result.next();
					path = result.getString("ImgPath");
				}
			}
		}
		return path;
	}
	
	public List<Option> getOptionsByProductID(String productID) throws SQLException{
		List<Option> options = new ArrayList<Option>();
		
		String query = "SELECT * FROM TIW_Progetto.OPTION WHERE ProductCode = ?";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query)) {
			pstatement.setString(1, productID);
			try (ResultSet result = pstatement.executeQuery()) {
				while(result.next()) {
					Option temp = new Option();
					temp.setOptionCode(result.getString("Code"));
					temp.setOptionName(result.getString("Name"));
					temp.setProductCode(result.getString("ProductCode"));
					temp.setOptionType(OptionType.valueOf(result.getString("Type")));
					options.add(temp);
				}
			}
		}
		return options;
	}
	
	public List<String> getOptionCodesByProductID(String productCode) throws SQLException{
		String query = "SELECT Code FROM TIW_Progetto.OPTION WHERE ProductCode = ?";
		List<String> resultList = new ArrayList<>();
		
		try (PreparedStatement pstatement = connection.prepareStatement(query)) {
			pstatement.setString(1, productCode);
			try (ResultSet result = pstatement.executeQuery()) {
				while(result.next()) {
					resultList.add(result.getString("Code"));
				}
			}
		}
		return resultList;
	}
	
	public List<String> getProductsCodes() throws SQLException{
		String query = "SELECT Code FROM TIW_Progetto.PRODUCT";
		List<String> resultList = new ArrayList<>();
		
		try (PreparedStatement pstatement = connection.prepareStatement(query)) {
			try (ResultSet result = pstatement.executeQuery()) {
				while(result.next()) {
					resultList.add(result.getString("Code"));
				}
			}
		}
		return resultList;
	}
	
	public Map<String, List<String>> getOptionCodesByProductID() throws SQLException{
		String query ="SELECT Code FROM `TIW_Progetto`.`OPTION` WHERE ProductCode = ?";
		String productQuery = "SELECT Code FROM `TIW_Progetto`.`PRODUCT`";
		List<String> productCodes = new ArrayList<>();
		
		Map<String, List<String>> resultToReturn = new HashMap<>();
		List<String> optionCodes = new ArrayList<>();
		
		try (PreparedStatement pstatement = connection.prepareStatement(productQuery)) {
			try (ResultSet result = pstatement.executeQuery()) {
				while(result.next()) {
					productCodes.add(result.getString("Code"));
				}
			}
		}
		
		try (PreparedStatement pstatement = connection.prepareStatement(query)) {
			for(int i = 0; i < productCodes.size(); i++) {
				pstatement.setString(1, productCodes.get(i));
				optionCodes = new ArrayList<>();
				try (ResultSet result = pstatement.executeQuery()) {
					while(result.next()) {
						optionCodes.add(result.getString("Code"));
					}
				}
				resultToReturn.put(productCodes.get(i), optionCodes);
			}
		}
		
		return resultToReturn;
	}
	
	public Option getOptionByID(String optionID) throws SQLException{
		Option option = null;
		
		String query = "SELECT * FROM TIW_Progetto.OPTION WHERE Code=?";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query)) {
			pstatement.setString(1, optionID);
			try (ResultSet result = pstatement.executeQuery()) {
				result.next();
				option = new Option();
				option.setOptionCode(result.getString("Code"));
				option.setOptionName(result.getString("Name"));
				option.setProductCode(result.getString("ProductCode"));
				option.setOptionType(OptionType.valueOf(result.getString("Type")));	
			}
		}
		return option;
	}
}
