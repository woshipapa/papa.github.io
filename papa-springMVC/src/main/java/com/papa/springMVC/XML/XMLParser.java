package com.papa.springMVC.XML;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.lang.annotation.Documented;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLParser {
    public static String getBasePackage(String xmlPath) throws DocumentException {
        SAXReader saxReader=new SAXReader();
        InputStream resourceAsStream = XMLParser.class.getClassLoader().getResourceAsStream(xmlPath);
        Document document=saxReader.read(resourceAsStream);
        Element element=document.getRootElement();
        List<Element> elementList=element.elements();
        String basePackage=null;
        for(Element e:elementList){
            if("component-scan".equalsIgnoreCase(e.getName())){
                Attribute attribute = e.attribute("base-package");
                if(basePackage==null) {
                    basePackage = attribute.getText();
                }else{
                    //说明有多个component-scan标签,多个包用,隔开
                    basePackage=basePackage+","+attribute.getText();
                }
            }
        }
        return basePackage;
    }
    public static Map<String,Object> scanAllBean(String xmlPath) throws DocumentException, ClassNotFoundException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        Map<String,Object> map=new HashMap<>();
        SAXReader saxReader = new SAXReader();
        Document document=saxReader.read(XMLParser.class.getClassLoader().getResourceAsStream(xmlPath));
        Element rootElement = document.getRootElement();
        List<Element> elementList=rootElement.elements();
        for(Element e:elementList){
            if("bean".equals(e.getName())){
                Attribute beanClassFullPathAtt=e.attribute("class");
                Attribute beanIdAtt=e.attribute("name");
                String beanClassFullPath=beanClassFullPathAtt.getText();
                System.out.println("获取到的bean的 class = "+beanClassFullPath);
                String beanId=beanIdAtt.getText();
                Class<?> clazz = Class.forName(beanClassFullPath);
                Object obj = clazz.newInstance();
                List<Element> childElements = e.elements();
                for(Element ee:childElements){
                    if("property".equals(ee.getName())){
                        Attribute nameAtt = ee.attribute("name");
                        Attribute valueAtt = ee.attribute("value");
                        String name=nameAtt.getText();
                        String value=valueAtt.getText();
                        Field declaredField = clazz.getDeclaredField(name);
                        declaredField.setAccessible(true);
                        declaredField.set(obj,value);
                    }
                }
                map.put(beanId,obj);
            }


        }


        return map;
    }
}
