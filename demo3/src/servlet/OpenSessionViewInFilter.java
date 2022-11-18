package servlet;

import util.TransactionManage;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @author panhai
 * @create 2022-11-18 11:14
 */
@WebFilter("*.do")
public class OpenSessionViewInFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            TransactionManage.beginTrans();
            filterChain.doFilter(servletRequest, servletResponse);
            TransactionManage.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                TransactionManage.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void destroy() {

    }
}
