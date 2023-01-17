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
 * Servlet implementation class CheckLogIn
 */
@WebServlet("/CheckLogIn")
public class CheckLogIn extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckLogIn() {
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
		
		CredentialsDAO credentialsDAO = new CredentialsDAO(connection);
		UserRoleEnum userRole;
		 
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
		
		try {
			userRole = credentialsDAO.checkCredentials(username, password);
		}catch(SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("DB Error");			
			return;
		}
		
		if(userRole == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().println("Incorrect credentials");
		}
		else { 
			User user;

			if(userRole.equals(UserRoleEnum.Client))
				user = new User(username, userRole);
			else 
				user = new User(username, userRole);
			
			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			String Serialized_User = new Gson().toJson(user);
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().println(Serialized_User);
		}
		 
	}
	 
	public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
	}

}
