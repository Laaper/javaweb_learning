package servlets;

import myssm.io.BeanFactory;
import myssm.io.ClassPathXmlApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author panhai
 * @create 2022-11-16 20:40
 */
@WebServlet("*.do")
public class DispatcherServlet extends ViewBaseServlet {
    private BeanFactory beanFactory;

    public DispatcherServlet(){

    }

    @Override
    public void init() throws ServletException {
        super.init();
        Object beanFactoryObj = getServletContext().getAttribute("beanFactory");
        if (beanFactoryObj != null) {
            beanFactory = (BeanFactory) beanFactoryObj;
        }
        else{
            System.out.println("工厂创建失败");
        }
//        beanFactory = new ClassPathXmlApplicationContext();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        String servletpath = req.getServletPath();
        String servletPath = servletpath.substring(1, servletpath.lastIndexOf(".do"));

        Object controllerBeanobj = beanFactory.getBean(servletPath);
        String operate = req.getParameter("operate");
        if(operate==null||"".equals(operate)){
            operate = "index";
        }
        try {
            Method[] methods = controllerBeanobj.getClass().getDeclaredMethods();
            //获取每个方法的形参，并传入实参
            for (Method method : methods) {
                if (method.getName().equals(operate)) {
                    Parameter[] parameters = method.getParameters();
                    Object[] parametersValues = new Object[parameters.length];
                    for (int i = 0; i < parameters.length; i++) {
                        Parameter parameter = parameters[i];
                        if (parameter.getName().equals("req")) {
                            parametersValues[i] = req;
                        }else if(parameter.getName().equals("resp")){
                            parametersValues[i] = resp;
                        }else if(parameter.getName().equals("session")){
                            parametersValues[i] = req.getSession();
                        }
                        else {
                            Object paraValue;
                            //针对类型名来初始化对应实参的类型
                            if (parameter.getType().getName().equals("java.lang.Integer")) {
                                 paraValue= Integer.parseInt(req.getParameter(parameter.getName()));
                            }
                            parametersValues[i] = req.getParameter(parameter.getName());
                        }
                    }
                    //方法调用
                    method.setAccessible(true);
                    System.out.println(parametersValues);
                    Object returnObj = method.invoke(controllerBeanobj, parametersValues);
                    String methodReturnStr = (String) returnObj;
                    //视图处理
                    if (methodReturnStr.startsWith("redirect:")) {
                        String redirectStr=methodReturnStr.substring("redirect:".length());
                        resp.sendRedirect(redirectStr);
                    }
                    else {
                        super.processTemplate(methodReturnStr, req, resp);

                    }
                }
            }
                //同一获取方法形参的参数


            }
       catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
