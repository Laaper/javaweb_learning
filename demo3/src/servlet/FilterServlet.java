package servlet;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author panhai
 * @create 2022-11-18 10:16
 */
public class FilterServlet implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String initParam = filterConfig.getInitParameter("encoding");
        System.out.println(initParam);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("in");
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("back");
    }

    @Override
    public void destroy() {

    }
}
