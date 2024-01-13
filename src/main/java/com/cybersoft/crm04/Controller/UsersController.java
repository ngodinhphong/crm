package com.cybersoft.crm04.Controller;

import com.cybersoft.crm04.Services.UserService;
import com.cybersoft.crm04.entity.UsersEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UsersController {

    @Autowired
    private UserService userService;

    @GetMapping("/show")
    public String showUser(Model model){

        List<UsersEntity> listUser = userService.getUser();
        model.addAttribute("users", listUser);

        return "user-table";
    }

    @GetMapping("/add")
    public String userTableAdd(){

        return "user-add";
    }

}
