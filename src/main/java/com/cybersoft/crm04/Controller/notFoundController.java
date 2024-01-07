package com.cybersoft.crm04.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("404")
public class notFoundController {
    @GetMapping("")
    public String notFound(){

        return "404";
    }
}
