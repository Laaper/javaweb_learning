package controller;

import dao.Fruit;
import dao.FruitDAO;
import dao.impl.FruitDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * @author panhai
 * @create 2022-11-16 20:47
 */
public class FruitController  {
    private FruitDAO fruitdao = null;
    private String index(HttpSession session) throws  IOException {
        Integer page=0;
        List<Fruit> fruitList=fruitdao.getFruitList();
        session.setAttribute("fruitlist",fruitList);
        Integer lastpage=(int)Math.ceil((double)fruitList.size()/4)-1;
        session.setAttribute("lastpage",lastpage);
        session.setAttribute("page",page);
        //thymeleaf会把逻辑视图名称加上物理视图名称，此处视图名称时index
        //物理名称：view-prefix+逻辑视图名称+view-suffix
//        super.processTemplate("index", req, resp);
        return "index";
    }
    protected String page(String pid,HttpSession session) {
        Integer page = Integer.parseInt(pid);
//      Integer lastpage = Integer.parseInt(String.valueOf(session.getAttribute("lastpage")));
        Integer lastpage = Integer.parseInt(session.getAttribute("lastpage").toString());
        page = page > lastpage ? lastpage :( page < 0 ? 0 : page);

        List<Fruit> fruitList = fruitdao.getLimitList(page);
        session.setAttribute("fruitlist",fruitList);
        session.setAttribute("page",page);
//        super.processTemplate("index", req, resp);
        return "index";
    }
    protected String edit(String fid,HttpServletRequest req)  {
        if(fid!=null&&!"".equals(fid)){
            Fruit fruit=fruitdao.getFruitByFid(Integer.parseInt(fid));
            req.setAttribute("fruit", fruit);
//            super.processTemplate("edit", req, resp);
            return "edit";
        }
        return "error";
    }
    protected String add(String fname,String price,String fcount,String remark)  {
        Integer priceNum = Integer.parseInt(price);
        Integer fcountNum = Integer.parseInt(fcount);
        Fruit fruit = new Fruit(0, fname, priceNum, fcountNum, remark);
        fruitdao.addFruit(fruit);
//        resp.sendRedirect("fruit");
        return "redirect:fruit.do";
    }
    protected String del(String fid)  {
        Integer fidNum = Integer.parseInt(fid);
        fruitdao.delFruit(fidNum);
//        resp.sendRedirect("fruit");
        return "redirect:fruit.do";
    }
    protected String update(String fid,String fname,String price,String fcount,String remark,HttpServletRequest req)  {
        Integer priceNum = Integer.parseInt(price);
        Integer fcountNum = Integer.parseInt(fcount);
        Integer fidNum = Integer.parseInt(fid);
        Fruit fruit = new Fruit(fidNum, fname, priceNum, fcountNum, remark);
        fruitdao.updateFruit(fruit);
//        req.getRequestDispatcher("/leaf").forward(req,resp);
        return "redirect:fruit.do";
//        resp.sendRedirect("fruit");
    }
}

