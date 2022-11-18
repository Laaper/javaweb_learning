package servlets;

import dao.Fruit;
import dao.FruitDAO;
import dao.impl.FruitDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 * @author panhai
 * @create 2022-11-15 21:10
 */
public class ThymeleafServlet extends ViewBaseServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fname = "脐橙";
        Integer price = Integer.parseInt("30");
        Integer fcount = Integer.parseInt("33");
        String remark = "不乱码辣";
        Integer page=0;
        HttpSession session = req.getSession();
        session.setAttribute("fname", fname);
        session.setAttribute("price", price);
        session.setAttribute("fcount", fcount);
        session.setAttribute("remark", remark);
        FruitDAO fruitdao=new FruitDAOImpl();
        List<Fruit> fruitList=fruitdao.getFruitList();
        session.setAttribute("fruitlist",fruitList);
        Integer lastpage=(int)Math.ceil((double)fruitList.size()/4)-1;
        session.setAttribute("lastpage",lastpage);
        session.setAttribute("page",page);
        //thymeleaf会把逻辑视图名称加上物理视图名称，此处视图名称时index
        //物理名称：view-prefix+逻辑视图名称+view-suffix
        super.processTemplate("index",req,resp);

    }


}
