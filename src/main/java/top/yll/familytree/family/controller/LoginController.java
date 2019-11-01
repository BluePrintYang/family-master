package top.yll.familytree.family.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class LoginController {
    @RequestMapping("/")
    public String hello(){
        return "login";
    }


    @RequestMapping("/login")
    public String login(String username,String password,String permission){
        if (username.equals("admin") && password.equals("123") & permission.equals("admin"))return "admin";
        else if (username.equals("user") && password.equals("123") & permission.equals("user"))return "user";
        else return "error";
    }


}
