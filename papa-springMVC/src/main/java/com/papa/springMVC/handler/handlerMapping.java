package com.papa.springMVC.handler;

import com.papa.springMVC.annotation.Controller;
import com.papa.springMVC.annotation.RequestMapping;
import com.papa.springMVC.context.papaApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class handlerMapping {
    private List<handler> handlerList=new ArrayList<>();
    public void initHandlerMapping(ServletContext servletContext){
        papaApplicationContext ioc=papaApplicationContext.getInstance();
        Set<String> keys=ioc.singletonObjects.keySet();
        for(String s:keys){
            Object obj=ioc.singletonObjects.get(s);
            Class clazz=obj.getClass();
            if(clazz.isAnnotationPresent(Controller.class)){
                //此时是Controller
                for(Method method:clazz.getDeclaredMethods()){
                    if(method.isAnnotationPresent(RequestMapping.class)){
                        RequestMapping requestMapping=method.getAnnotation(RequestMapping.class);
                        String url=requestMapping.value();
                        url=servletContext.getContextPath()+url;//拼接上项目路径，就变成了URI
                        handlerList.add(new handler(url,obj,method));
                    }
                }

            }
        }

    }
    public handler getHandler(HttpServletRequest req){
        String uri=req.getRequestURI();
        System.out.println("本次请求的uri : "+uri);
        for(handler h:handlerList){
            if(uri.equals(h.getUrl())){
                return h;
            }
        }
        return null;
    }

    public List<handler> getHandlerList() {
        return handlerList;
    }
}
