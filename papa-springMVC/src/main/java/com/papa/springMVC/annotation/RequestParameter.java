package com.papa.springMVC.annotation;

import jdk.nashorn.internal.ir.annotations.Reference;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParameter {
    String value() default "";

}
