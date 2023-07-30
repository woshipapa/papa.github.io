package com.papa.springMVC.handler;

import java.lang.reflect.Method;

public class handler {
    private String url;
    private Object controller;
    private Method method;
    public handler(String url,Object controller,Method method){
        this.url=url;
        this.controller=controller;
        this.method=method;
    }

    public Method getMethod() {
        return method;
    }

    public Object getController() {
        return controller;
    }

    public String getUrl() {
        return url;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "handler{" +
                "url='" + url + '\'' +
                ", controller=" + controller +
                ", method=" + method +
                '}';
    }
}
