package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import Beans.Preventive;
import Beans.User;
import DAO.PreventiveDAO;
import Utils.ConnectionHandler;

/**
 * Servlet implementation class GetClientHomePage
 */
@WebServlet("/GetPreventivesCreatedByUser")
public class GetPreventivesCreatedByUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	String contextPath;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetPreventivesCreatedByUser() {
        super();
    }
 
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Preventive> preventivesCreatedByUser = new ArrayList<>();
		PreventiveDAO preventiveDAO = new PreventiveDAO(connection);
		User user = (User)request.getSession().getAttribute("user");
		
		try {
			preventivesCreatedByUser = preventiveDAO.getPreventivesByClient(user.getUsername());
		}
		catch(SQLException e){
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Not possible to recover preventives created by you");
			return;
		}
		
		String SerializedPreventives = new Gson().toJson(preventivesCreatedByUser);
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(SerializedPreventives);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	public void init() throws ServletException {
		contextPath = getServletContext().getContextPath();
		connection = ConnectionHandler.getConnection(getServletContext());
	}

}
