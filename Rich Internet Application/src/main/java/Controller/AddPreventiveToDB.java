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

import Beans.Option;
import Beans.PreventiveInfo;
import Beans.User;
import DAO.PreventiveDAO;
import Utils.ConnectionHandler;
import Utils.GenerateID;

/**
 * Servlet implementation class AddPreventiveToDB
 */
@WebServlet("/AddPreventiveToDB")
public class AddPreventiveToDB extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	String contextPath;
	 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddPreventiveToDB() {
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
		String productCode = request.getParameter("productCode");
		String options = request.getParameter("options");
;		List<String> selectedOptionCodes = (new Gson()).fromJson(options, List.class);
		

		List<Option> selectedOptions = new ArrayList<>();
		PreventiveInfo preventiveToAdd = new PreventiveInfo();
		PreventiveDAO preventiveDAO = new PreventiveDAO(connection);
		User user = (User)(request.getSession().getAttribute("user"));
		
		if(selectedOptionCodes.size() == 0) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().println("You have to select at least one option");
			return;
		}
		
		for(int i = 0; i < selectedOptionCodes.size(); i++) {
			Option option = new Option();
			option.setOptionCode(selectedOptionCodes.get(i));
			selectedOptions.add(option);
		}
		
		preventiveToAdd.setClientUsername(user.getUsername());
		preventiveToAdd.setEmployeeUsername(null);
		preventiveToAdd.setOptionsInPreventive(selectedOptions);
		preventiveToAdd.setPrice(0.0f);
		preventiveToAdd.setProductCode(productCode);
		preventiveToAdd.setPreventiveID(GenerateID.generateID());
		try {
			preventiveToAdd.setProductName((preventiveDAO).getProductNameByCode(productCode));
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("It was not possible to get product name");
			return;
		}

		try {
			preventiveDAO.addPreventive(preventiveToAdd);
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println("It was not possible to add the preventive");
			return;
		}
	}
	   
	public void init() throws ServletException {
		contextPath = getServletContext().getContextPath();
		connection = ConnectionHandler.getConnection(getServletContext());
	}

}
