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
	private TemplateEngine templateEngine;
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
		String target = "/GetEmployeeHomePage";
		String contextPath = getServletContext().getContextPath();
		response.sendRedirect(contextPath + target);
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
			if(Float.parseFloat(price) <= 0) {
				user.addLastVisitedPage("", "", "");
				
				String Path ="/WEB-INF/ErrorPage.html";
				ServletContext servletContext = getServletContext();
				final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
				ctx.setVariable("errorMessage", "The price must be bigger than 0");
				templateEngine.process(Path, ctx, response.getWriter());
				return;
			}else{
				try {
					preventiveDAO.updatePriceOfPreventive(preventiveID, user.getUsername(), Float.parseFloat(price));
				}catch(SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "The server is not available. Impossible to update the price of the preventive.");
					return;
					
				}
			}
		}catch(Exception e) {
			String Path ="/WEB-INF/ErrorPage.html";
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("errorMessage", "The price must be bigger than 0");
			templateEngine.process(Path, ctx, response.getWriter());
			return;
		}
		

		String target = "/GetEmployeeHomePage";
		String contextPath = getServletContext().getContextPath();
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
