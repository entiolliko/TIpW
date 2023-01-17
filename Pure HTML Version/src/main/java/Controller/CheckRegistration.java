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
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import Beans.User;
import DAO.CredentialsDAO;
import Utils.ConnectionHandler;
import Utils.UserRoleEnum;

/**
 * Servlet implementation class CheckRegistration
 */
@WebServlet("/CheckRegistration")
public class CheckRegistration extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;
	String contextPath;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckRegistration() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String Path = "";
		HttpSession session = request.getSession(false);
		if(session == null || session.getAttribute("user") == null) {
			Path = "/WEB-INF/LogInPage.html";
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			templateEngine.process(Path, ctx, response.getWriter());
			
		}else{
			if(((User) session.getAttribute("user")).getRole() == UserRoleEnum.Client)
				Path = "/GetClientHomePage";
			else if(((User)session.getAttribute("user")).getRole() == UserRoleEnum.Employee) 
				Path = "/GetEmployeeHomePage";
			
			response.sendRedirect(contextPath + Path);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String passwordCheck = request.getParameter("passwordCheck");
		String userRole = request.getParameter("userRole");
		
		CredentialsDAO credentialsDAO = new CredentialsDAO(connection);
		boolean success;
		
		String errorMsg = "";
		boolean credentialsAreOk = true;
		
		if(username == null || username == "" || username.contains(" ")) {
			errorMsg = "You have to insert a username!";
			credentialsAreOk = false;
		}
		if(password == null || password == "" || password.contains(" ") || password.length() < 8) {
			errorMsg = "You have to insert a password with at least 8 characters";
			credentialsAreOk = false;
		}
		if(!(password.equals(passwordCheck))) {
			errorMsg = "The passwords must match";
			credentialsAreOk = false;
		}
		if(userRole == null || userRole == "") {
			errorMsg = "You have to choose a role!";
			credentialsAreOk = false;
		}
		
		if(!credentialsAreOk) {
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("errorMessageReg", errorMsg);
			String path = "/WEB-INF/LogInPage.html";
			templateEngine.process(path, ctx, response.getWriter());
			return;
		}
		
		try {
			success = credentialsDAO.addCredentials(username, password, UserRoleEnum.valueOf(userRole));
			
			if(success == false) {
				ServletContext servletContext = getServletContext();
				final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
				ctx.setVariable("errorMessageReg", "The username already exists.");
				String LogInPath = "/WEB-INF/LogInPage.html";
				templateEngine.process(LogInPath, ctx, response.getWriter());
			}
			else {
				User user;

				if(userRole.equals(UserRoleEnum.Client.toString()))
					user = new User(username, UserRoleEnum.valueOf(userRole), contextPath + "/GetClientHomePage");
				else 
					user = new User(username, UserRoleEnum.valueOf(userRole), contextPath + "/GetEmployeeHomePage");
				
				HttpSession session = request.getSession();
				session.setAttribute("user", user);
				
				String target;
				if (user.getRole() == UserRoleEnum.Client) {
					target = "/GetClientHomePage";
				} else if (user.getRole() == UserRoleEnum.Employee) {
					target = "/GetEmployeeHomePage";
				} else {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "We cannot identify the type of user.");
					return;
				}

				response.sendRedirect(contextPath + target);
			}
		}catch(SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "The server is not available. Impossible to check credentials.");
			return;
		}
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
