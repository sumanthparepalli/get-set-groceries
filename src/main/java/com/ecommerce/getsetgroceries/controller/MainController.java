package com.ecommerce.getsetgroceries.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @GetMapping("/")
    @ResponseBody
    public String home()
    {
        return "Hello";
    }

    @GetMapping("/user")
    @ResponseBody
    public String userHome()
    {
        return "Hello User";
    }

    @GetMapping("/admin")
    @ResponseBody
    public String adminHome()
    {
        return "Hello admin";
    }
}
