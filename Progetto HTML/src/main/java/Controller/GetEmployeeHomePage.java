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

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import Beans.Preventive;
import Beans.User;
import DAO.PreventiveDAO;
import Utils.ConnectionHandler;

/**
 * Servlet implementation class GetEmployeeHomePage
 */
@WebServlet("/GetEmployeeHomePage")
public class GetEmployeeHomePage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;
	String contextPath;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetEmployeeHomePage() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Preventive> preventivesManagedByEmployee = new ArrayList<>();
		List<Preventive> unmanagedPreventives = new ArrayList<>();
		
		PreventiveDAO preventiveDAO = new PreventiveDAO(connection);
		
		User user = (User)request.getSession().getAttribute("user");
		
		try {
			preventivesManagedByEmployee = preventiveDAO.getPreventivesByEmployee(user.getUsername());
			unmanagedPreventives = preventiveDAO.getUnmanagedPreventives();
		}
		catch(SQLException e){
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "We cannot reach the server to get your preventives");
			return;
		}
		
		if(request.getParameter("goBackButton") == null) {
			user.addLastVisitedPage(contextPath + "/GetEmployeeHomePage", "", "");
		}
		else {
			user.updateGoBackPage(contextPath + "/GetEmployeeHomePage", "", "");
		}
		
		String ClientHomePath ="/WEB-INF/HomePageEmployee.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("preventives", preventivesManagedByEmployee);
		ctx.setVariable("unmanagedPreventives", unmanagedPreventives);
		templateEngine.process(ClientHomePath, ctx, response.getWriter());
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
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

}
