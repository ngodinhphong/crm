package com.cybersoft.crm04.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("blank")
public class BlankController {

    @GetMapping("")
    public String designate(){

        return "blank";
    }
}
