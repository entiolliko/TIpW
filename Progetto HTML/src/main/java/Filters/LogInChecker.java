package Filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

/**
 * Servlet Filter implementation class LoginChecker
 */
@WebFilter("/LogInChecker")
public class LogInChecker extends HttpFilter implements Filter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	
    /**
     * @see HttpFilter#HttpFilter()
     */
    public LogInChecker() {
        super();
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		
		String logInPath = "/WEB-INF/LogInPage.html";
		
		HttpSession session = req.getSession(false);
		
		if(session == null || session.getAttribute("user") == null) {
			ServletContext servletContext = req.getServletContext();
			final WebContext ctx = new WebContext(req, res, servletContext, req.getLocale());
			ctx.setVariable("errorMessageLog", "You have to Log In before you can access the page.");
			templateEngine.process(logInPath,  ctx, res.getWriter());
			return;
		}
		
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		ServletContext servletContext = fConfig.getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

}
