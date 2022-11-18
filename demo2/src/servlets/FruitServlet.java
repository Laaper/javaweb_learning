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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author panhai
 * @create 2022-11-16 19:05
 */
@WebServlet("/fruit")
public class FruitServlet extends ViewBaseServlet{
    private FruitDAO fruitdao = new FruitDAOImpl();
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        String operate = req.getParameter("operate");
        if(operate==null||"".equals(operate)){
            operate = "index";
        }

        Method[] methods = this.getClass().getDeclaredMethods();
        for (Method m : methods) {
            String methodname = m.getName();
            if (methodname.equals(operate)) {
                try {
                    m.invoke(this, req, resp);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
        }
//
//        switch (operate) {
//            case "index":
//                index(req, resp);
//                break;
//            case "page":
//                page(req, resp);
//                break;
//            case "edit":
//                edit(req, resp);
//                break;
//            case "add":
//                add(req, resp);
//                break;
//            case "del":
//                del(req, resp);
//                break;
//            case "update":
//                update(req, resp);
//                break;
//            default:
//                throw new RuntimeException("非法操作");
//        }
    }
    private void index(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer page=0;
        HttpSession session = req.getSession();
        List<Fruit> fruitList=fruitdao.getFruitList();
        session.setAttribute("fruitlist",fruitList);
        Integer lastpage=(int)Math.ceil((double)fruitList.size()/4)-1;
        session.setAttribute("lastpage",lastpage);
        session.setAttribute("page",page);
        //thymeleaf会把逻辑视图名称加上物理视图名称，此处视图名称时index
        //物理名称：view-prefix+逻辑视图名称+view-suffix
        super.processTemplate("index",req,resp);
    }
    protected void page(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String res = req.getParameter("pid");
        int page = Integer.parseInt(req.getParameter("pid"));
        HttpSession session = req.getSession();
//        int lastpage = Integer.parseInt(String.valueOf(session.getAttribute("lastpage")));
        int lastpage = Integer.parseInt(session.getAttribute("lastpage").toString());
        page = page > lastpage ? lastpage :( page < 0 ? 0 : page);

        List<Fruit> fruitList = fruitdao.getLimitList(page);
        req.getSession().setAttribute("fruitlist",fruitList);
        req.getSession().setAttribute("page",page);
        super.processTemplate("index",req,resp);
    }
    protected void edit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fidstr = req.getParameter("fid");
        if(fidstr!=null&&!"".equals(fidstr)){
            int fid=Integer.parseInt(fidstr);
            Fruit fruit=fruitdao.getFruitByFid(fid);
            req.setAttribute("fruit", fruit);
            super.processTemplate("edit",req,resp);
        }
    }
    protected void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fname = req.getParameter("fname");
        Integer price = Integer.parseInt(req.getParameter("price"));
        Integer fcount = Integer.parseInt(req.getParameter("fcount"));
        String remark = req.getParameter("remark");
        Fruit fruit = new Fruit(0, fname, price, fcount, remark);
        fruitdao.addFruit(fruit);
        resp.sendRedirect("fruit");
    }
    protected void del(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer fid = Integer.parseInt(req.getParameter("fid"));
        fruitdao.delFruit(fid);
        resp.sendRedirect("fruit");
    }
    protected void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String fname = req.getParameter("fname");
        Integer price = Integer.parseInt(req.getParameter("price"));
        Integer fcount = Integer.parseInt(req.getParameter("fcount"));
        String remark = req.getParameter("remark");
        Integer fid = Integer.parseInt(req.getParameter("fid"));
        Fruit fruit = new Fruit(fid, fname, price, fcount, remark);
        fruitdao.updateFruit(fruit);
//        req.getRequestDispatcher("/leaf").forward(req,resp);

        resp.sendRedirect("fruit");
//        req.getRequesqDispatcher("session").forward(req,resp);
    }
}
