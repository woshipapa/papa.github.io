package com.papa.springMVC.view.viewResolver;

import com.papa.springMVC.view.View;

import javax.servlet.http.HttpServletRequest;

public interface ViewResolver {
    View resolveViewName(String name);
}
