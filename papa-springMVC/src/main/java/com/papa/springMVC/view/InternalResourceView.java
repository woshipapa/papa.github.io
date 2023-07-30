package com.papa.springMVC.view;

import com.papa.springMVC.view.viewResolver.InternalResourceViewResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class InternalResourceView implements View{
    private String urlPath;
    public InternalResourceView(String urlPath) {
        this.urlPath=urlPath;
    }
    public InternalResourceView(){}
    @Override
    public void render(HttpServletRequest request, HttpServletResponse response) {
        //String contextPath=request.getServletContext().getContextPath();
        String path=urlPath;
        System.out.println("render中的path = "+path);
        try {
            request.getRequestDispatcher(path).forward(request,response);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
