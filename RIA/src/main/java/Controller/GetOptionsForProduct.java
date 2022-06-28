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

import DAO.ProductDAO;
import Utils.ConnectionHandler;

/**
 * Servlet implementation class SelectOptionsForProduct
 */
@WebServlet("/GetOptionsForProduct")
public class GetOptionsForProduct extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	String contextPath;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetOptionsForProduct() {
        super();
        // TODO Auto-generated constructor stub
    }

	/** 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String productCode = request.getParameter("productCode");
		List<String> optionsForPreventive = new ArrayList<>();
		
		try {
			optionsForPreventive = (new ProductDAO(connection)).getOptionCodesByProductID(productCode);
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("Not possible to get the options for the required product from the DB");
			return;
		}	
		
		if(optionsForPreventive.size() != 0) {
			String SerializedOptions = new Gson().toJson(optionsForPreventive);
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(SerializedOptions);
		}
		else{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("No options were found for the requested product");
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
