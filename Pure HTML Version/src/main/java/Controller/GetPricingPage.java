package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

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

import Beans.PreventiveInfo;
import Beans.User;
import DAO.PreventiveDAO;
import Utils.ConnectionHandler;

/**
 * Servlet implementation class ManagePricingForEmployee
 */
@WebServlet("/GetPricingPage")
public class GetPricingPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;
	String contextPath;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetPricingPage() {
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
			preventiveInfo = preventiveDAO.getUnmanagedPreventiveByID(preventiveID);
		}
		catch(SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "The server is not available. Impossible to get the informations of the preventive.");
			return;
		}
		
		if(preventiveInfo != null) {
			
			if(request.getParameter("goBackButton") == null) {
				user.addLastVisitedPage(contextPath + "/GetPricingPage", "preventiveID", preventiveID);
			}
			else {
				user.updateGoBackPage(contextPath + "/GetPricingPage", "preventiveID", preventiveID);
			}
			
			String Path ="/WEB-INF/PricePreventivePage.html";
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("preventiveInfo", preventiveInfo);
			templateEngine.process(Path, ctx, response.getWriter());
		
		}
		
		else {
			user.addLastVisitedPage("", "", "");
			String ClientHomePath ="/WEB-INF/ErrorPage.html";
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("errorMessage", "There are no unmanaged preventives with this ID");
			templateEngine.process(ClientHomePath, ctx, response.getWriter());
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
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

}
