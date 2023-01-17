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
import DAO.PreventiveDAO;
import Utils.ConnectionHandler;

/**
 * Servlet implementation class GetUnmanagedPreventives
 */
@WebServlet("/GetUnmanagedPreventives")
public class GetUnmanagedPreventives extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetUnmanagedPreventives() {
        super();
    }

	/** 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Preventive> unmanagedPreventives = new ArrayList<>();
		PreventiveDAO preventiveDAO = new PreventiveDAO(connection);
		
		try {
			unmanagedPreventives = preventiveDAO.getUnmanagedPreventives();
		}
		catch(SQLException e){
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "We cannot reach the server to get your preventives");
			return;
		}
		
		String serialized_courses = new Gson().toJson(unmanagedPreventives);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(serialized_courses);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
	}

}
