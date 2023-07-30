package com.papa.springMVC.view.viewResolver;

import com.papa.springMVC.view.RedirectView;
import com.papa.springMVC.view.View;

public class RedirectViewResolver implements ViewResolver {
    @Override
    public View resolveViewName(String name) {
        return new RedirectView(name.split(":")[1]);
    }
}
