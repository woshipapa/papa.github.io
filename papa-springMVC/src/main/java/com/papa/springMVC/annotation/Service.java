package com.papa.springMVC.annotation;

import java.lang.annotation.*;

@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface Service {
    String value() default "";
}
