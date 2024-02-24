package com.cybersoft.crm04.Controller;


import com.cybersoft.crm04.Services.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("index")
public class IndexController {

    @Autowired
    private IndexService indexService;

    @GetMapping("")
    public String index(Model model){

        int unfulfilled = indexService.getTaskUnfulfilled();
        model.addAttribute("unfulfilled", unfulfilled);

        int processing = indexService.getTaskProcessing();
        model.addAttribute("processing", processing);

        int completed = indexService.getTaskCompleted();
        model.addAttribute("completed", completed);

        return "index";
    }

}
