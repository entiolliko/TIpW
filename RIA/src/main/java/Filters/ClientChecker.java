package Filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import Utils.UserRoleEnum;
import Beans.User;
 
/**
 * Servlet Filter implementation class ClientChecker
 */
@WebFilter("/ClientChecker")
public class ClientChecker extends HttpFilter implements Filter {
	/** 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    /**
     * @see HttpFilter#HttpFilter()
     */
    public ClientChecker() {
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
		
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute("user");
		String path = req.getServletContext().getContextPath() + "/EmployeeHome.html";

		if(user.getRole() != UserRoleEnum.Client) {
			res.sendRedirect(path);
			return;
		}
		
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
