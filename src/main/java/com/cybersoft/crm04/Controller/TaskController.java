package com.cybersoft.crm04.Controller;

import com.cybersoft.crm04.Services.JobService;
import com.cybersoft.crm04.Services.StatusService;
import com.cybersoft.crm04.Services.TaskService;
import com.cybersoft.crm04.Services.UserService;
import com.cybersoft.crm04.entity.JobsEntity;
import com.cybersoft.crm04.entity.StatusEntity;
import com.cybersoft.crm04.entity.TasksEntity;
import com.cybersoft.crm04.entity.UsersEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private JobService jobService;

    @Autowired
    private UserService userService;

    @Autowired
    private StatusService statusService;

    @GetMapping("/show")
    public String designate(Model model){

        List<TasksEntity> tasks = taskService.getAllTask();
        model.addAttribute("task", tasks);

        List<UsersEntity> users = userService.getAllUser();
        model.addAttribute("user", users);

        return "task";
    }

    @GetMapping("/add")
    public String ShowAddTassk(Model model){
        List<JobsEntity> jobs = jobService.getAlljob();
        model.addAttribute("job", jobs);

        List<UsersEntity> users = userService.getAllUser();
        model.addAttribute("user", users);

        List<StatusEntity> statusEntities = statusService.getAllStatus();
        model.addAttribute("statusEntitie", statusEntities);

        return "task-add";
    }

    @PostMapping("/add")
    public String addTask(@RequestParam int idJob, @RequestParam String nameTask, @RequestParam int idUser,
                          @RequestParam String startDate, @RequestParam String endDate, @RequestParam int idStatus, Model model){
        JobsEntity jobsEntity = jobService.getJobById(idJob);
        UsersEntity usersEntity = userService.getUserById(idUser);
        StatusEntity statusEntity = statusService.getStatusById(idStatus);

        boolean checckIsSuccess = taskService.saveTask(jobsEntity, nameTask, usersEntity, startDate, endDate, statusEntity);
        model.addAttribute("checckIsSuccess", checckIsSuccess);

        boolean checkConditionnal = taskService.checkConditions(nameTask, usersEntity, jobsEntity);
        model.addAttribute("checkConditionnal", checkConditionnal);

        boolean checkConditionsDate = taskService.checkConditionsDate(jobsEntity, startDate, endDate);
        model.addAttribute("checkConditionsDate", checkConditionsDate);

        List<JobsEntity> jobs = jobService.getAlljob();
        model.addAttribute("job", jobs);

        List<UsersEntity> users = userService.getAllUser();
        model.addAttribute("user", users);

        model.addAttribute("nameTask", nameTask);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);


        return "task-add";
    }
}
