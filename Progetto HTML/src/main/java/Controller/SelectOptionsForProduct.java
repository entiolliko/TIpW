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

import Beans.User;
import DAO.ProductDAO;
import Utils.ConnectionHandler;

/**
 * Servlet implementation class SelectOptionsForProduct
 */
@WebServlet("/SelectOptionsForProduct")
public class SelectOptionsForProduct extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;
	String contextPath;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SelectOptionsForProduct() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User)request.getSession().getAttribute("user");
		String productCode = request.getParameter("productCode");
		List<String> optionsForPreventive = new ArrayList<>();	
		String path;
		
		try {
			optionsForPreventive = (new ProductDAO(connection)).getOptionCodesByProductID(productCode);
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "We cannot reach the server to get your preventives");
			return;
		}	
		
		if(optionsForPreventive.size() != 0) {
			if(request.getParameter("goBackButton") == null) {
				user.addLastVisitedPage(contextPath + "/SelectOptionsForProduct", "productCode", productCode);
			}
			else {
				user.updateGoBackPage(contextPath + "/SelectOptionsForProduct", "productCode", productCode);
			}
			
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());	
			path ="/WEB-INF/SelectOptionsForPreventive.html";
			ctx.setVariable("productCode", productCode);
			ctx.setVariable("options", optionsForPreventive);
			templateEngine.process(path, ctx, response.getWriter());
		}
		else{
			user.addLastVisitedPage("", "", "");
			String Path ="/WEB-INF/ErrorPage.html";
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("errorMessage", "There is the problem with the choosen product.\nPlease choose another product");
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
