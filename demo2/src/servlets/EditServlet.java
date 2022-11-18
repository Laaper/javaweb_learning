package servlets;

import dao.Fruit;
import dao.FruitDAO;
import dao.impl.FruitDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author panhai
 * @create 2022-11-16 10:07
 */

@WebServlet("/edit")
public class EditServlet extends ViewBaseServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fidstr = req.getParameter("fid");
        if(fidstr!=null&&!"".equals(fidstr)){
            int fid=Integer.parseInt(fidstr);
            FruitDAO fruitdao=new FruitDAOImpl();
            Fruit fruit=fruitdao.getFruitByFid(fid);
            req.setAttribute("fruit", fruit);
            super.processTemplate("edit",req,resp);
        }
    }
}
