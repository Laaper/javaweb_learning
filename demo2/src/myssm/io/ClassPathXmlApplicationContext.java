package myssm.io;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

import static java.lang.Class.forName;

/**
 * @author panhai
 * @create 2022-11-17 15:47
 */
public class ClassPathXmlApplicationContext implements BeanFactory{

    private Map<String, Object> beanMap = new HashMap<>();

    public ClassPathXmlApplicationContext() {

        try {
            //通过类加载去读取资源文件
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("applicationContext.xml");
            //创建工厂
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            //创建DocumentBuilder对象
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            //创建document对象
            Document document=documentBuilder.parse(inputStream);
            //获取所有bean节点
            NodeList beanNodeList = document.getElementsByTagName("bean");
            for (int i = 0; i < beanNodeList.getLength(); i++) {
                Node beanNode = beanNodeList.item(i);
                if (beanNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element beanElement = (Element) beanNode;
                    String beanId = beanElement.getAttribute("id");
                    String className = beanElement.getAttribute("class");
                    //通过类名反射创建一个实例对象
                    Class beanClass = Class.forName(className);
                    Object beanObj = beanClass.newInstance();
                    beanMap.put(beanId, beanObj);

                }
            }
            //组装每个bean之间的依赖关系
            for (int i = 0; i < beanNodeList.getLength(); i++) {
                Node beanNode = beanNodeList.item(i);
                if (beanNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element beanElement = (Element) beanNode;
                    String beanId = beanElement.getAttribute("id");
                    NodeList beanChildNodeList = beanElement.getChildNodes();
                    for (int j = 0; j < beanChildNodeList.getLength(); j++) {
                        Node beanChildNode = beanChildNodeList.item(j);
                        if (beanChildNode.getNodeType() == Node.ELEMENT_NODE && beanChildNode.getNodeName().equals("property")) {
                            Element propertyNode = (Element) beanChildNode;
                            String propertyName = propertyNode.getAttribute("name");
                            String propertyRef = propertyNode.getAttribute("ref");
                            //拿到引用对象的实例
                            Object refObj = beanMap.get(propertyRef);
                            //拿到当前bean节点的实例
                            Object beanObj = beanMap.get(beanId);
                            //把refobj设置到当前bean实例的property属性上
                            Class beanClass=beanObj.getClass();
                            Field propertyField = beanClass.getDeclaredField(propertyName);
                            propertyField.setAccessible(true);
                            propertyField.set(beanObj,refObj);
                        }
                    }


                }
            }
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Object getBean(String id) {
        return beanMap.get(id);
    }
}
