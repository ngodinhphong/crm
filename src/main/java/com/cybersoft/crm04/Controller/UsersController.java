package com.cybersoft.crm04.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UsersController {

    @GetMapping("")
    public String userTable(){

        return "user-table";
    }

    @GetMapping("/add")
    public String userTableAdd(){

        return "user-add";
    }

}
