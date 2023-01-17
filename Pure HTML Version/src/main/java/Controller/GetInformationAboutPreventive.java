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
import Utils.UserRoleEnum;

/**
 * Servlet implementation class MenagePreventivesForClient
 */
@WebServlet("/GetInformationAboutPreventive")
public class GetInformationAboutPreventive extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;
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
		String path = "";
		
		
		try {
			if(user.getRole() == UserRoleEnum.Client) {
				preventiveInfo = preventiveDAO.getPreventiveByIDAndClientUsername(preventiveID , user.getUsername());
				path = "/TIW_-_Progetto_HTML/GetClientHomePage";
			}else if(user.getRole() == UserRoleEnum.Employee) {
				preventiveInfo = preventiveDAO.getPreventiveByIDAndEmployeeUsername(preventiveID , user.getUsername());
				path = "/TIW_-_Progetto_HTML/GetEmployeeHomePage";
			}else {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "The server is not available. Impossible to get the informations of the preventive.");
				return;
			}
		}
		catch(SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "The server is not available. Impossible to get the informations of the preventive.");
			return;
		}
		
		if(preventiveInfo != null) {
			if(request.getParameter("goBackButton") == null) {
				user.addLastVisitedPage(contextPath + "/GetInformationAboutPreventive", "preventiveID", preventiveID);
			}
			else {
				user.updateGoBackPage(contextPath + "/GetInformationAboutPreventive", "preventiveID", preventiveID);
			}
			String Path ="/WEB-INF/PreventiveInfoPage.html";
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("path", path);
			ctx.setVariable("preventiveInfo", preventiveInfo);
			templateEngine.process(Path, ctx, response.getWriter());
		}
		else{
			user.addLastVisitedPage("", "", "");
			String Path ="/WEB-INF/ErrorPage.html";
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("errorMessage", "You don't have any preventive with this ID");
			templateEngine.process(Path, ctx, response.getWriter());
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
