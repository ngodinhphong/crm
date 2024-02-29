package com.cybersoft.crm04.Controller;


import com.cybersoft.crm04.Services.IndexService;
import com.cybersoft.crm04.Services.UserService;
import com.cybersoft.crm04.entity.UsersEntity;
import jakarta.servlet.http.HttpSession;
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

    @Autowired
    UserService userService;

    @GetMapping("")
    public String index(Model model, HttpSession session){

        UsersEntity user = userService.getUserBySession(session);
        model.addAttribute("img", user.getAvatar());

        int unfulfilled = indexService.getTaskUnfulfilled();
        model.addAttribute("unfulfilled", unfulfilled);

        int unfulfilledPercent = indexService.getTaskUnfulfilledPercent();
        model.addAttribute("unfulfilledPercent", unfulfilledPercent);

        int processing = indexService.getTaskProcessing();
        model.addAttribute("processing", processing);

        int processingPercent = indexService.getTaskProcessingPercent();
        model.addAttribute("processingPercent", processingPercent);

        int completed = indexService.getTaskCompleted();
        model.addAttribute("completed", completed);

        int completedPercent = indexService.getTaskCompletedPercent();
        model.addAttribute("completedPercent", completedPercent);

        return "index";
    }

}
