package com.cybersoft.crm04.Controller;

import com.cybersoft.crm04.Services.JobService;
import com.cybersoft.crm04.Services.UserService;
import com.cybersoft.crm04.entity.JobsEntity;
import com.cybersoft.crm04.entity.TasksEntity;
import com.cybersoft.crm04.entity.UsersEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("job")
public class JobController {

    @Autowired
    private JobService jobService ;

    @Autowired
    private UserService userService;

    @GetMapping("show")
    public String designate(Model model){
        List<JobsEntity> listjob = jobService.getAlljob();
        model.addAttribute("jobs", listjob);
        return "groupwork";
    }

    @GetMapping("/look/{id}")
    public String showJobs(@PathVariable int id, Model model){
        JobsEntity job = jobService.getJobById(id);

        List<TasksEntity> listTask = job.getTasks();

        List<UsersEntity> listUser = jobService.getUserByTask(listTask);
        model.addAttribute("listUsers", listUser);

        return "groupwork-details";
    }

    @GetMapping("/add")
    public String showAddJob(Model model){

        return "groupwork-add";
    }

    @PostMapping("/add")
    public String addJob(@RequestParam String nameProject, @RequestParam String startDate, @RequestParam String endDate, Model model){

        String notification = jobService.notificationSave(nameProject, startDate, endDate);
        model.addAttribute("notification", notification);

        boolean checckIsSuccess = jobService.saveJob(nameProject, startDate, endDate);
        model.addAttribute("checckIsSuccess", checckIsSuccess);

        model.addAttribute("nameProject", nameProject);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "groupwork-add";
    }

    @GetMapping("/delete/{id}")
    public String deleteJob(@PathVariable int id){
        jobService.deletJobById(id);

        return "redirect:/job/show";
    }

    @GetMapping("/update/{id}")
    public String getData(@PathVariable int id, Model model){
        JobsEntity job = jobService.getJobById(id);
        model.addAttribute("job", job);

        return "groupwork-update";
    }

    @PostMapping("/update/{id}")
    public String updateData(@PathVariable int id, @RequestParam String nameProject, @RequestParam String startDate, @RequestParam String endtDate, Model model){
        JobsEntity job = jobService.getJobById(id);

        JobsEntity jobsEntity = new JobsEntity();
        jobsEntity.setId(id);
        jobsEntity.setName(nameProject);
        jobsEntity.setStartDate(jobService.convertStringToDate(startDate));
        jobsEntity.setEndDate(jobService.convertStringToDate(endtDate));

        String notification = jobService.notificationUpdate(nameProject, startDate, endtDate, job);
        model.addAttribute("notification", notification);

        boolean checckIsSuccess = jobService.updatejob(nameProject, startDate, endtDate, jobsEntity, job);
        model.addAttribute("checckIsSuccess", checckIsSuccess);

        model.addAttribute("job", job);

        return "groupwork-update";
    }
}
