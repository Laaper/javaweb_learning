package servlets;

import dao.Fruit;
import dao.FruitDAO;
import dao.impl.FruitDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 * @author panhai
 * @create 2022-11-16 15:35
 */
@WebServlet("/page")
public class PageServlet extends ViewBaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String res = req.getParameter("pid");
        int page = Integer.parseInt(req.getParameter("pid"));
        HttpSession session = req.getSession();
//        int lastpage = Integer.parseInt(String.valueOf(session.getAttribute("lastpage")));
        int lastpage = Integer.parseInt(session.getAttribute("lastpage").toString());
        page = page > lastpage ? lastpage :( page < 0 ? 0 : page);

        FruitDAO fruitdao=new FruitDAOImpl();
        List<Fruit> fruitList = fruitdao.getLimitList(page);
        req.getSession().setAttribute("fruitlist",fruitList);
        req.getSession().setAttribute("page",page);
        super.processTemplate("index",req,resp);
    }
}
