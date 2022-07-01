package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Beans.User;
import DAO.PreventiveDAO;
import Utils.ConnectionHandler;

/**
 * Servlet implementation class UpdatePreventivePrice
 */
@WebServlet("/UpdatePreventivePrice")
public class UpdatePreventivePrice extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	String contextPath;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdatePreventivePrice() { 
        super();
        // TODO Auto-generated constructor stub
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
		User user = (User)request.getSession().getAttribute("user");
		String preventiveID = request.getParameter("preventiveID");
		String price = request.getParameter("price"); 
		PreventiveDAO preventiveDAO = new PreventiveDAO(connection);
		try{
			if(preventiveID == null) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().println("The preventive ID must be non null");
				return;
			}else if(Float.parseFloat(price) <= 0) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().println("The price must be higher than 0");
				return;
			}else{
				try {
					preventiveDAO.updatePriceOfPreventive(preventiveID, user.getUsername(), Float.parseFloat(price));
				}catch(SQLException e) {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().println("Not possible to update price");
					return;
				}
			}
		}catch(Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("There has been an internal error. Not possible to update price");
			return;
		}
	}
	
	public void init() throws ServletException {
		contextPath = getServletContext().getContextPath();
		connection = ConnectionHandler.getConnection(getServletContext());
	}

}
