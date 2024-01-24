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
    public String addJob(Model model, @RequestParam String nameProject, @RequestParam String startDay, @RequestParam String endtDay){

        boolean checckIsSuccess = jobService.saveJob(nameProject, startDay, endtDay);
        model.addAttribute("checckIsSuccess", checckIsSuccess);

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
    public String updateData(@PathVariable int id, @RequestParam String nameProject, @RequestParam String startDay, @RequestParam String endtDay, Model model){
        JobsEntity job = jobService.getJobById(id);

        JobsEntity jobsEntity = new JobsEntity();
        jobsEntity.setId(id);
        jobsEntity.setName(nameProject);
        jobsEntity.setStartDate(jobService.convertStringToDate(startDay));
        jobsEntity.setEndDate(jobService.convertStringToDate(endtDay));
        boolean checckIsSuccess = jobService.updatejob(nameProject, jobsEntity, job);
        model.addAttribute("checckIsSuccess", checckIsSuccess);

        model.addAttribute("job", job);

        return "groupwork-update";
    }
}
