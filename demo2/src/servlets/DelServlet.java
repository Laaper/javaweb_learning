package servlets;

import dao.Fruit;
import dao.FruitDAO;
import dao.impl.FruitDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author panhai
 * @create 2022-11-16 14:43
 */
@WebServlet("/del")
public class DelServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer fid = Integer.parseInt(req.getParameter("fid"));
        FruitDAO fruitdao=new FruitDAOImpl();
        fruitdao.delFruit(fid);
        resp.sendRedirect("leaf");
    }
}
