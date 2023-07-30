package com.papa.springMVC.view.viewResolver;

import com.papa.springMVC.view.InternalResourceView;
import com.papa.springMVC.view.View;

import javax.servlet.http.HttpServletRequest;

public class InternalResourceViewResolver implements ViewResolver{
    private String prefix;
    private String suffix;
    public InternalResourceViewResolver(String prefix,String suffix){
        this.prefix=prefix;
        this.suffix=suffix;
    }
    public InternalResourceViewResolver(){}
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    @Override
    public View resolveViewName(String viewName) {
        if(viewName.contains(":")){
            // forward
            String urlPath=viewName.split(":")[1];
            return new InternalResourceView(urlPath);
        }else{
            //默认方式
            String urlPath=prefix+viewName+suffix;
            return new InternalResourceView(urlPath);
        }

    }
}
