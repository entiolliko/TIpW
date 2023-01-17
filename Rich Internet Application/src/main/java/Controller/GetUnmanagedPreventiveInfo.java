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
import DAO.PreventiveDAO;
import Utils.ConnectionHandler;

/**
 * Servlet implementation class ManagePricingForEmployee
 */
@WebServlet("/GetUnmanagedPreventiveInfo")
public class GetUnmanagedPreventiveInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	String contextPath;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetUnmanagedPreventiveInfo() {
        super();
    }
  
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String preventiveID = request.getParameter("preventiveID"); 
		PreventiveDAO preventiveDAO = new PreventiveDAO(connection);
		PreventiveInfo preventiveInfo = null;
		
		try {
			preventiveInfo = preventiveDAO.getUnmanagedPreventiveByID(preventiveID);
		}
		catch(SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Not possible to recover unmanaged preventive info");
			return;
		}
		
		if(preventiveInfo != null) {
			String SerializedOptions = new Gson().toJson(preventiveInfo);
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(SerializedOptions);
		}
		else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("No preventive was found");
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
