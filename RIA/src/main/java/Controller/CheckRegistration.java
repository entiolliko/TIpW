package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import Beans.User;
import DAO.CredentialsDAO;
import Utils.ConnectionHandler;
import Utils.UserRoleEnum;

/**
 * Servlet implementation class CheckRegistration
 */
@WebServlet("/CheckRegistration")
public class CheckRegistration extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	
	String contextPath;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckRegistration() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String passwordCheck = request.getParameter("passwordCheck");
		String userRole = request.getParameter("userRole");
		
		CredentialsDAO credentialsDAO = new CredentialsDAO(connection);
		boolean success;
		
		if(username == null || username == "" || username.contains(" ")) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("The username must be not null/empty/contain spaces");
			return;
		}
		
		if(password == null || password == "" || password.contains(" ")) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("The password must be not null/empty/contain spaces");
			return;
		}
		
		if(password.length() < 8) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("The password must have at least 8 characters");
			return;
		}
		
		if(!(password.equals(passwordCheck))) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("The passwords have different values");
			return;	
		}
		
		
		if(userRole == null || userRole == "") {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("You have to choose a role");
			return;
		}
		
		try {
			success = credentialsDAO.addCredentials(username, password, UserRoleEnum.valueOf(userRole));
			
			if(success == false) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().println("The username has already been taken");
			}
			else {
				User user;

				if(userRole.equals(UserRoleEnum.Client.toString()))
					user = new User(username, UserRoleEnum.valueOf(userRole));
				else 
					user = new User(username, UserRoleEnum.valueOf(userRole));
				
				HttpSession session = request.getSession();
				session.setAttribute("user", user);
				
				String Serialized_User = new Gson().toJson(user);
				response.setStatus(HttpServletResponse.SC_OK);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().println(Serialized_User);
			}
		}catch(SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("DB Error");
			return;
		}
	}
	
	public void init() throws ServletException {
		contextPath = getServletContext().getContextPath();
		connection = ConnectionHandler.getConnection(getServletContext());
	}
}
