package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Beans.*;



public class PreventiveDAO {
	private Connection connection;
	
	public PreventiveDAO(Connection connection) {
		this.connection = connection;
	}
	
	public List<Preventive> getPreventivesByClient(String clientUsername) throws SQLException {
		List<Preventive> preventives = new ArrayList<Preventive>();
		
		String query = "SELECT ID, ProductCode FROM TIW_Progetto.PREVENTIVE WHERE ClientUsername = ?";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query)) {
			pstatement.setString(1, clientUsername);
			try (ResultSet result = pstatement.executeQuery()) {
				while(result.next()) {
					Preventive temp = new Preventive();
					temp.setPreventiveID(result.getString("ID"));
					temp.setProductCode(result.getString("ProductCode"));
					temp.setProductName(getProductNameByCode(result.getString("ProductCode")));
					preventives.add(temp);
				}
			}
		}
		return preventives;
	}
	
	public List<Preventive> getPreventivesByEmployee(String employeeUsername) throws SQLException {
		List<Preventive> preventives = new ArrayList<Preventive>();
		
		String query = "SELECT ID, ProductCode FROM TIW_Progetto.PREVENTIVE WHERE EmployeeUsername = ?";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query)) {
			pstatement.setString(1, employeeUsername);
			try (ResultSet result = pstatement.executeQuery()) {
				while(result.next()) {
					Preventive temp = new Preventive();
					temp.setPreventiveID(result.getString("ID"));
					temp.setProductCode(result.getString("ProductCode"));
					temp.setProductName(getProductNameByCode(result.getString("ProductCode")));
					preventives.add(temp);
				}
			}
		}
		return preventives;
	}
	
	public PreventiveInfo getPreventiveByID(String preventiveID) throws SQLException {
		PreventiveInfo preventiveInfo = null;
		String query = "SELECT * FROM TIW_Progetto.PREVENTIVE WHERE ID = ?";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query)) {
			pstatement.setString(1, preventiveID);
			try (ResultSet result = pstatement.executeQuery()) {
				if(result.isBeforeFirst()) {
					result.next();
					preventiveInfo = new PreventiveInfo();
					preventiveInfo.setPreventiveID(result.getString("ID"));
					preventiveInfo.setProductCode(result.getString("ProductCode"));
					preventiveInfo.setProductName(getProductNameByCode(result.getString("ProductCode")));
					preventiveInfo.setClientUsername(result.getString("ClientUsername"));
					preventiveInfo.setEmployeeUsername(result.getString("EmployeeUsername"));
					preventiveInfo.setPrice(result.getFloat("Price"));
					preventiveInfo.setOptionsInPreventive(getOptionsInPreventive(result.getString("ID")));
				}
			}
		}
		return preventiveInfo;
	}
	
	public PreventiveInfo getPreventiveByIDAndClientUsername(String preventiveID, String username) throws SQLException{
		PreventiveInfo preventiveInfo = null;
		ProductDAO productDAO = new ProductDAO(connection);
		
		String query = "SELECT * FROM TIW_Progetto.PREVENTIVE WHERE ID=? AND ClientUsername=?";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query)) {
			pstatement.setString(1, preventiveID);
			pstatement.setString(2, username);
			try (ResultSet result = pstatement.executeQuery()) {
				if(result.next()) {
					preventiveInfo = new PreventiveInfo();
					preventiveInfo.setPreventiveID(result.getString("ID"));
					preventiveInfo.setProductCode(result.getString("ProductCode"));
					preventiveInfo.setClientUsername(result.getString("ClientUsername"));
					preventiveInfo.setEmployeeUsername(result.getString("EmployeeUsername"));
					preventiveInfo.setPrice(result.getFloat("Price"));
					preventiveInfo.setProductName(getProductNameByCode(result.getString("ProductCode")));
					preventiveInfo.setImgPath(productDAO.getImgPathFromProductCode(result.getString("ProductCode")));
					preventiveInfo.setOptionsInPreventive(getOptionsInPreventive(result.getString("ID")));
				}
			}
		}
		return preventiveInfo;
	}
	
	public PreventiveInfo getPreventiveByIDAndEmployeeUsername(String preventiveID, String username) throws SQLException{
		PreventiveInfo preventiveInfo = null;
		ProductDAO productDAO = new ProductDAO(connection);
		
		String query = "SELECT * FROM TIW_Progetto.PREVENTIVE WHERE ID=? AND EmployeeUsername=?";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query)) {
			pstatement.setString(1, preventiveID);
			pstatement.setString(2, username);
			try (ResultSet result = pstatement.executeQuery()) {
				if(result.next()) {
					preventiveInfo = new PreventiveInfo();
					preventiveInfo.setPreventiveID(result.getString("ID"));
					preventiveInfo.setProductCode(result.getString("ProductCode"));
					preventiveInfo.setClientUsername(result.getString("ClientUsername"));
					preventiveInfo.setEmployeeUsername(result.getString("EmployeeUsername"));
					preventiveInfo.setPrice(result.getFloat("Price"));
					preventiveInfo.setProductName(getProductNameByCode(result.getString("ProductCode")));
					preventiveInfo.setImgPath(productDAO.getImgPathFromProductCode(result.getString("ProductCode")));
					preventiveInfo.setOptionsInPreventive(getOptionsInPreventive(result.getString("ID")));
				}
			}
		}
		return preventiveInfo;
	}
	
	public List<Option> getOptionsInPreventive(String preventiveID) throws SQLException{
		List<Option> resultList = new ArrayList<>();
		
		String query = "SELECT OptionCode FROM TIW_Progetto.OPTIONSINPREVENTIVE WHERE IDPreventive=?";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query)) {
			pstatement.setString(1, preventiveID);
			try (ResultSet result = pstatement.executeQuery()) {
				while(result.next()) {
					String temp = result.getString("OptionCode");
					Option option = (new ProductDAO(connection)).getOptionByID(temp); //Qui abbiamo un errore
					resultList.add(option);
				}
			}
		}
		return resultList;
	}
	
	public List<Preventive> getUnmanagedPreventives() throws SQLException {
		List<Preventive> preventives = new ArrayList<Preventive>();
		
		String query = "SELECT ID, ProductCode FROM TIW_Progetto.PREVENTIVE WHERE EmployeeUsername IS NULL";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query)) {
			
			try (ResultSet result = pstatement.executeQuery()) {
				while(result.next()) {
					Preventive temp = new Preventive();
					temp.setPreventiveID(result.getString("ID"));
					temp.setProductCode(result.getString("ProductCode"));
					temp.setProductName(getProductNameByCode(result.getString("ProductCode")));
					preventives.add(temp);
				}
			}
		}
		return preventives;
	}
	
	public PreventiveInfo getUnmanagedPreventiveByID(String preventiveID) throws SQLException{
		PreventiveInfo preventiveInfo = null;
		String query = "SELECT * FROM TIW_Progetto.PREVENTIVE WHERE EmployeeUsername IS NULL AND ID=?";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query)) {
			pstatement.setString(1, preventiveID);
			try (ResultSet result = pstatement.executeQuery()) {
				if(result.isBeforeFirst()) {
					result.next();
					preventiveInfo = new PreventiveInfo();
					preventiveInfo.setPreventiveID(result.getString("ID"));
					preventiveInfo.setProductCode(result.getString("ProductCode"));
					preventiveInfo.setClientUsername(result.getString("ClientUsername"));
					preventiveInfo.setEmployeeUsername("");
					preventiveInfo.setPrice(-1.0f);
					preventiveInfo.setProductName(getProductNameByCode(result.getString("ProductCode")));
					preventiveInfo.setOptionsInPreventive(getOptionsInPreventive(result.getString("ID")));
				}
			}
		}
		//if(preventiveInfo == null)
			//throw new SQLException("There is no unmanaged preventive with the specified ID");
		return preventiveInfo;
	}
	
	public boolean isPreventiveUnmanaged(String preventiveID) throws SQLException{
		String query = "SELECT * FROM TIW_Progetto.PREVENTIVE WHERE EmployeeUsername IS NULL AND ID=?";
		try (PreparedStatement pstatement = connection.prepareStatement(query)) {
			pstatement.setString(1, preventiveID);
			try (ResultSet result = pstatement.executeQuery()) {
				if(result.isBeforeFirst()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean addPreventive(PreventiveInfo toAdd) throws SQLException {
		String query = "INSERT INTO `TIW_Progetto`.`PREVENTIVE` (`ID`, `ClientUsername`, `ProductCode`, `EmployeeUsername`, `Price`) VALUES (?, ?, ?, ?, ?)";
		String queryOptions = "INSERT INTO `TIW_Progetto`.`OPTIONSINPREVENTIVE` (`IDPreventive`, `OptionCode`) VALUES (?, ?)";
		try (PreparedStatement pstatementAdd = connection.prepareStatement(query)) {
			pstatementAdd.setString(1, toAdd.getPreventiveID());
			pstatementAdd.setString(2, toAdd.getClientUsername());
			pstatementAdd.setString(3, toAdd.getProductCode());
			pstatementAdd.setString(4, toAdd.getEmployeeUsername());
			pstatementAdd.setFloat(5, toAdd.getPrice());
			pstatementAdd.executeUpdate();
		}
		
		List<String> optionCodes = toAdd.getOptionsInPreventive().stream().map(x -> x.getOptionCode()).toList();
		
		try(PreparedStatement pstatement = connection.prepareStatement(queryOptions)){
			pstatement.setString(1, toAdd.getPreventiveID());
			for(int i = 0; i < optionCodes.size(); i++) {
				pstatement.setString(2, optionCodes.get(i));
				pstatement.executeUpdate();
			}
		}
		return true;
	}
	
	public boolean updatePriceOfPreventive(String preventiveID, String employeeUsername, Float price) throws SQLException {
		String query = "UPDATE TIW_Progetto.PREVENTIVE SET EmployeeUsername=?, Price=? WHERE (ID = ?)";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query)) {
			pstatement.setString(1, employeeUsername);
			pstatement.setFloat(2, price);
			pstatement.setString(3, preventiveID);
			pstatement.executeUpdate();
		}
		return true;
	}
	
	public String getProductNameByCode(String productCode) throws SQLException{
		String res = null;
		String query = "SELECT Name FROM TIW_Progetto.PRODUCT WHERE Code = ?";
		
		try(PreparedStatement pstatement = connection.prepareStatement(query)){
			pstatement.setString(1, productCode);
			try(ResultSet result = pstatement.executeQuery()){
				result.next();
				res = result.getString("Name");
			}
		}
		if(res == null)
			throw new SQLException("Every product must have a name.");
		return res;
	}
	
	
}
