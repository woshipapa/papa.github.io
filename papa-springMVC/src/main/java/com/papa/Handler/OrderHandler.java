package com.papa.Handler;

import com.papa.entity.User;
import com.papa.springMVC.annotation.Controller;
import com.papa.springMVC.annotation.RequestMapping;
import com.papa.springMVC.annotation.RequestParameter;
import com.papa.springMVC.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderHandler {

    @RequestMapping("/order/list")
    public void listOrder(HttpServletRequest req, HttpServletResponse resp, @RequestParameter("name")String oname,@RequestParameter String age){
        try {
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().write("<h1>展示订单列表</h1>" +
                    "oname = "+oname +"\n age = "+age);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @RequestMapping("/order/add")
    public String addOrder(HttpServletRequest req, HttpServletResponse resp){
        try {
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().write("<h1>添加</h1>");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "login_ok";
    }
    @RequestMapping("/order/list/json")
    @ResponseBody
    public List<User> listByJson(){
        List<User> userList=new ArrayList<>();
        userList.add(new User("小明",18));
        userList.add(new User("小美",20));
        return userList;
    }
}
