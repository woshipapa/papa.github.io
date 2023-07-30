package com.papa.Handler;

import com.papa.Service.MonsterService;
import com.papa.springMVC.annotation.Autowired;
import com.papa.springMVC.annotation.Controller;
import com.papa.springMVC.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class MonsterHandler {
    @Autowired
    private MonsterService monsterService;
    @RequestMapping(value = "/monster")
    public String getMonster(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter printWriter=response.getWriter();
        printWriter.write("<h1>hello!</h1>");

        return "";
    }
}
