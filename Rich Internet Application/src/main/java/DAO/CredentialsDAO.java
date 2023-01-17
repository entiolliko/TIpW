package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Utils.UserRoleEnum;

public class CredentialsDAO {
	private Connection connection;
	
	public CredentialsDAO(Connection connection) {
		this.connection = connection;
	}
	
	
	public UserRoleEnum checkCredentials(String username, String password) throws SQLException { 
		String queryClient = "SELECT Username FROM TIW_Progetto.CLIENT WHERE Username = ? AND Password = ?";
		String queryEmployee = "SELECT Username FROM TIW_Progetto.EMPLOYEE WHERE Username = ? AND Password = ?";
		UserRoleEnum role = null;
		
		try (PreparedStatement pstatement = connection.prepareStatement(queryClient)){
			
			pstatement.setString(1, username); 
			pstatement.setString(2, password);
			try(ResultSet result = pstatement.executeQuery()){
				if (result.isBeforeFirst()) 
					role = UserRoleEnum.Client;
				result.close();
				pstatement.close();
				if(role != null)
					return role;
			}
		}
		
		try (PreparedStatement pstatement = connection.prepareStatement(queryEmployee)){
			
			pstatement.setString(1, username);
			pstatement.setString(2, password);
			try(ResultSet result = pstatement.executeQuery()){
				if (result.isBeforeFirst())
					role =  UserRoleEnum.Employee;
				result.close();
				pstatement.close();
				if(role != null)
					return role;
			}
		}
		return null;
	}
	
	public boolean addCredentials(String username, String password, UserRoleEnum role) throws SQLException {

		String query = "SELECT Username FROM TIW_Progetto.CLIENT WHERE Username = ? UNION SELECT Username FROM TIW_Progetto.EMPLOYEE WHERE Username = ?";
		
		try(PreparedStatement pstatement = connection.prepareStatement(query)){
			
			pstatement.setString(1, username);
			pstatement.setString(2, username);
			try(ResultSet result = pstatement.executeQuery()){
				if (result.isBeforeFirst()) {
					return false;
				}
			}
		}
		
		String inquery;
		if(role == UserRoleEnum.Client)
			inquery = "INSERT INTO TIW_Progetto.CLIENT (Username, Password) VALUES (?, ?)";
		else if(role == UserRoleEnum.Employee)
			inquery = "INSERT INTO TIW_Progetto.EMPLOYEE (Username, Password) VALUES (?, ?)";
		else
			throw new SQLException("Unknown user type.");
		
		try (PreparedStatement pstatementAdd = connection.prepareStatement(inquery)) {

			pstatementAdd.setString(1, username);
			pstatementAdd.setString(2, password);
			pstatementAdd.executeUpdate();
		}
		return true;
	}
}
