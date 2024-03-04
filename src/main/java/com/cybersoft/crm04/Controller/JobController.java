package com.cybersoft.crm04.Controller;

import com.cybersoft.crm04.Services.JobService;
import com.cybersoft.crm04.Services.UserService;
import com.cybersoft.crm04.entity.JobsEntity;
import com.cybersoft.crm04.entity.TasksEntity;
import com.cybersoft.crm04.entity.UsersEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/job")
public class JobController {

    @Autowired
    private JobService jobService ;

    @Autowired
    private UserService userService;

    @GetMapping("/show")
    public String designate(Model model, HttpSession session){

        UsersEntity users = userService.getUserBySession(session);
        String avatarPath = userService.getPathAvata(users);
        model.addAttribute("avatarPath",avatarPath);

        List<JobsEntity> listjob = jobService.getJobByRole(session);

        model.addAttribute("jobs", listjob);
        return "groupwork";
    }

    @GetMapping("/look/{id}")
    public String showJobs(@PathVariable int id, Model model, HttpSession session){

        JobsEntity job = jobService.getJobById(id);

        UsersEntity users = userService.getUserBySession(session);
        String avatarPath = userService.getPathAvata(users);
        model.addAttribute("avatarPath",avatarPath);

        List<TasksEntity> listTask = job.getTasks();

        List<UsersEntity> listUser = jobService.getUserByTask(listTask);
        model.addAttribute("listUsers", listUser);

        int quantityUnfulfilled = jobService.getTaskUnfulfilled(job);
        model.addAttribute("quantityUnfulfilled", quantityUnfulfilled);

        int quantityProcessing = jobService.getTaskProcessing(job);
        model.addAttribute("quantityProcessing", quantityProcessing);

        int quantityCompleted = jobService.getTaskCompleted(job);
        model.addAttribute("quantityCompleted", quantityCompleted);

        String currentTime = jobService.getCurrentTime();
        model.addAttribute("currentTime", currentTime);

        return "groupwork-details";
    }

    @GetMapping("/add")
    public String showAddJob(HttpSession session, Model model){

        UsersEntity users = userService.getUserBySession(session);
        String avatarPath = userService.getPathAvata(users);
        model.addAttribute("avatarPath",avatarPath);

        return "groupwork-add";
    }

    @PostMapping("/add")
    public String addJob(@RequestParam String nameProject, @RequestParam String startDate,
                         @RequestParam String endDate, HttpSession session, Model model){

        UsersEntity users = userService.getUserBySession(session);
        String avatarPath = userService.getPathAvata(users);
        model.addAttribute("avatarPath",avatarPath);

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

        JobsEntity job = jobService.getJobById(id);

        jobService.deletJobById(id);

        return "redirect:/job/show" ;
    }

    @GetMapping("/update/{id}")
    public String getData(@PathVariable int id, HttpSession session, Model model){

        JobsEntity job = jobService.getJobById(id);
        model.addAttribute("job", job);

        UsersEntity users = userService.getUserBySession(session);
        String avatarPath = userService.getPathAvata(users);
        model.addAttribute("avatarPath",avatarPath);

        return "groupwork-update";
    }

    @PostMapping("/update/{id}")
    public String updateData(@PathVariable int id, @RequestParam String nameProject,
                             @RequestParam String startDate, @RequestParam String endtDate,
                             HttpSession session, Model model){

        UsersEntity users = userService.getUserBySession(session);
        String avatarPath = userService.getPathAvata(users);
        model.addAttribute("avatarPath",avatarPath);

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
