package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import Beans.Option;
import Beans.PreventiveInfo;
import Beans.User;
import DAO.PreventiveDAO;
import DAO.ProductDAO;
import Utils.ConnectionHandler;
import Utils.GenerateID;

/**
 * Servlet implementation class AddPreventiveToDB
 */
@WebServlet("/AddPreventiveToDB")
public class AddPreventiveToDB extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;
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
		String target = "/GetClientHomePage";
		String contextPath = getServletContext().getContextPath();
		response.sendRedirect(contextPath + target);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String productCode = request.getParameter("productCode");
		List<String> allOptions = new ArrayList<>();
		List<Option> selectedOptions = new ArrayList<>();
		PreventiveInfo preventiveToAdd = new PreventiveInfo();
		
		PreventiveDAO preventiveDAO = new PreventiveDAO(connection);
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		
		try {
			allOptions = (new ProductDAO(connection)).getOptionCodesByProductID(productCode);
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "We cannot identify the type of user.");
			return;
		}
		
		for(int i = 0; i < allOptions.size(); i++) {
			String temp = request.getParameter(allOptions.get(i));
			if(temp != null) {
				Option tempO = new Option();
				tempO.setOptionCode(temp);
				selectedOptions.add(tempO);
			}
		}
		
		if(selectedOptions.size() == 0) {
			user.addLastVisitedPage("", "", "");
			
			String Path ="/WEB-INF/ErrorPage.html";
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("errorMessage", "You have to select at least one option");
			templateEngine.process(Path, ctx, response.getWriter());
			return;

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
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "We cannot get the product name at the moment.");
			return;
		}

		try {
			preventiveDAO.addPreventive(preventiveToAdd);
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "We cannot add the preventive to the DB at the moment.");
			return;
		}
		
		String target = "/GetClientHomePage";
		response.sendRedirect(contextPath + target);
		
	}
	
	public void init() throws ServletException {
		contextPath = getServletContext().getContextPath();
		connection = ConnectionHandler.getConnection(getServletContext());
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

}
