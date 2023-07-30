package com.papa.springMVC.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RedirectView implements View{
    private String urlPath;
    public RedirectView(String path){
        urlPath=path;
    }
    @Override
    public void render(HttpServletRequest request, HttpServletResponse response) {
        urlPath= request.getContextPath()+urlPath;
        try {
            response.sendRedirect(urlPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
