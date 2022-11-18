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
 * @create 2022-11-16 11:11
 */
public class UpdateServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String fname = req.getParameter("fname");
        Integer price = Integer.parseInt(req.getParameter("price"));
        Integer fcount = Integer.parseInt(req.getParameter("fcount"));
        String remark = req.getParameter("remark");
        Integer fid = Integer.parseInt(req.getParameter("fid"));
        Fruit fruit = new Fruit(fid, fname, price, fcount, remark);
        FruitDAO fruitdao=new FruitDAOImpl();
        fruitdao.updateFruit(fruit);
//        req.getRequestDispatcher("/leaf").forward(req,resp);

        resp.sendRedirect("leaf");
//        req.getRequesqDispatcher("session").forward(req,resp);
    }
}
