package com.papa.springMVC.servlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.papa.springMVC.annotation.RequestParameter;
import com.papa.springMVC.annotation.ResponseBody;
import com.papa.springMVC.context.papaApplicationContext;
import com.papa.springMVC.handler.handler;
import com.papa.springMVC.handler.handlerMapping;
import com.papa.springMVC.view.View;
import com.papa.springMVC.view.viewResolver.InternalResourceViewResolver;
import com.papa.springMVC.view.viewResolver.RedirectViewResolver;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class papaDispatcherServlet extends HttpServlet{
    private final String REQUEST_PARAMETER="HttpServletRequest";
    private final String RESPONSE_PARAMETER="HttpServletResponse";
    private handlerMapping handlerMapping=new handlerMapping();
    private String contextConfigLocation=null;
    papaApplicationContext ioc = papaApplicationContext.getInstance();
    private String getContextConfigLocation(){
        ServletConfig config=getServletConfig();
        String springXmlPath=config.getInitParameter("contextConfigLocation");
        System.out.println("读取到的web.xml文件中的spring配置文件的信息 : "+springXmlPath);
        return springXmlPath;
    }

    @Override
    public void init() throws ServletException {
        super.init();
        //完成读取配置文件以及对容器的初始化

        String springXmlPath=getContextConfigLocation();
        ioc.init(springXmlPath);
        System.out.println(ioc.singletonObjects);
        handlerMapping.initHandlerMapping(getServletContext());
        System.out.println(handlerMapping.getHandlerList());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("doPost()...");
        doDispatcher(req,resp);
    }
    private void doDispatcher(HttpServletRequest req,HttpServletResponse resp){
        try {
            req.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        //请求的分发
        handler h= handlerMapping.getHandler(req);
        try {
            if (h == null) {
                resp.getWriter().write("<h1>404 NOT FOUND");
            } else {
                //反射调用handler中的方法
//                h.getMethod().invoke(h.getController(), req, resp);
                Method method=h.getMethod();
                Class[] paramTypes=method.getParameterTypes();
                Object[] params=new Object[paramTypes.length];//参数数组

                for(int i=0;i<paramTypes.length;i++){
                    if(REQUEST_PARAMETER.equals(paramTypes[i].getSimpleName())){
                        params[i]=req;
                    }else if(RESPONSE_PARAMETER.equals(paramTypes[i].getSimpleName())){
                        params[i]=resp;
                    }
                }

                //进行@RequestParamter的注入
                //先获取到请求参数的map
                Map<String,String[]> reqMap=req.getParameterMap();
                for(Map.Entry<String,String[]> entry: reqMap.entrySet()){
                    String requestName=entry.getKey();//获得了请求参数的名字
                    String [] values=entry.getValue();
                    String value=values[0];
                    if(values.length>1){
                        for(int i=1;i< values.length;i++)
                            value=value+","+values[i];
                    }
                    //然后要去方法的参数中去寻找进行匹配带有注解的
                    int in=getIndex(method,requestName);
                    if(in!=-1) params[in]=value;
                    else{
                        //进行默认的参数匹配，按照形参名字
                        List<String> paramNames=getParamNames(method);
                        //获得参数名字之后进行匹配
                        for(int i=0;i<paramNames.size();i++){
                            if(requestName.equals(paramNames.get(i))){
                                params[i]=value;
                                break;
                            }
                        }
                    }
                }
                Object res=method.invoke(h.getController(),params);//获得返回值，这里暂时只处理String类型，还可能是ModelAndView，JSON等格式
                if(res instanceof String){

                    View view=resolveName((String) res);
                    view.render(req,resp);
                }else if(res instanceof List){
                    if(method.isAnnotationPresent(ResponseBody.class)) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        String json = objectMapper.writeValueAsString(res);
                        System.out.println("获取到的json数据 = " + json);
                        resp.setContentType("text/json;charset=utf-8");
                        resp.getWriter().write(json);
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private View resolveName(String name){
        if(name.contains(":")){
            String first=name.split(":")[0];
            if("redirect".equals(first)) {
                return new RedirectViewResolver().resolveViewName(name);
            }
        }
        InternalResourceViewResolver internalResourceViewResolver = (InternalResourceViewResolver) ioc.singletonObjects.get("internalResourceViewResolver");
        return internalResourceViewResolver.resolveViewName(name);

    }
    private int getIndex(Method method,String requestName){
        Parameter[] parameters = method.getParameters();
        for(int i=0;i<parameters.length;i++){
            if(parameters[i].isAnnotationPresent(RequestParameter.class)){
                RequestParameter annotation = parameters[i].getAnnotation(RequestParameter.class);
                String value=annotation.value();
                //要判断value与请求参数的名字是否匹配
                if(value.equals("")){
                    //默认按照形参名字
                    value=parameters[i].getName();
                }
                if(value.equals(requestName)) return i;
            }
        }
        return -1;
    }
    private List<String> getParamNames(Method method){
        List<String> paramNames=new ArrayList<>();
        Parameter[] parameters=method.getParameters();
        for(int i=0;i<parameters.length;i++){
            paramNames.add(parameters[i].getName());
        }
        return paramNames;


    }
}
