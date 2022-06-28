package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import Beans.PreventiveInfo;
import Beans.User;
import DAO.PreventiveDAO;
import Utils.ConnectionHandler;
import Utils.UserRoleEnum;

/**
 * Servlet implementation class MenagePreventivesForClient
 */
@WebServlet("/GetInformationAboutPreventive")
public class GetInformationAboutPreventive extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	String contextPath;
	/**
     * @see HttpServlet#HttpServlet()
     */
    public GetInformationAboutPreventive() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User)request.getSession().getAttribute("user");
		String preventiveID = request.getParameter("preventiveID");
		PreventiveDAO preventiveDAO = new PreventiveDAO(connection);
		PreventiveInfo preventiveInfo = null;
		
		try { 
			if(user.getRole() == UserRoleEnum.Client) {
				preventiveInfo = preventiveDAO.getPreventiveByIDAndClientUsername(preventiveID , user.getUsername());
			}else if(user.getRole() == UserRoleEnum.Employee) {
				preventiveInfo = preventiveDAO.getPreventiveByIDAndEmployeeUsername(preventiveID , user.getUsername());
			}else {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().println("It was not possible to find your role");
				return;
			}
		}
		catch(SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Not possible to get the preventive's informations from the DB");
			return;
		}
		
		if(preventiveInfo != null) {
			String SerializedPreventiveInfo = new Gson().toJson(preventiveInfo);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(SerializedPreventiveInfo);
		}
		else{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("You do not have access to any preventive with that ID");
			return;
		}
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
