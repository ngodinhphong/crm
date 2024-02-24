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
import org.springframework.web.bind.annotation.*;

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

        String notification = taskService.notificationSave(jobsEntity, usersEntity, nameTask, startDate, endDate);
        model.addAttribute("notification", notification);

        boolean checckIsSuccess = taskService.saveTask(jobsEntity, nameTask, usersEntity, startDate, endDate, statusEntity);
        model.addAttribute("checckIsSuccess", checckIsSuccess);

        List<JobsEntity> jobs = jobService.getAlljob();
        model.addAttribute("job", jobs);

        List<UsersEntity> users = userService.getAllUser();
        model.addAttribute("user", users);

        List<StatusEntity> statusEntities = statusService.getAllStatus();
        model.addAttribute("statusEntitie", statusEntities);

        model.addAttribute("nameTask", nameTask);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "task-add";
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable int id, Model model){
        taskService.deleteTassk(id);
        return "redirect: /task/show";
    }

    @GetMapping("/update/{id}")
    public String editUser(@PathVariable int id, Model model){
        TasksEntity tasksEntity = taskService.getTaskById(id);
        model.addAttribute("task", tasksEntity);

        List<JobsEntity> jobsEntities = jobService.getAlljob();
        model.addAttribute("jobs", jobsEntities);
        model.addAttribute("jobsSelected", tasksEntity.getJobsEntity().getId());

        List<UsersEntity> usersEntities = userService.getAllUser();
        model.addAttribute("users", usersEntities);
        model.addAttribute("userSelected", tasksEntity.getUsersEntity().getId());

        List<StatusEntity> statusEntities = statusService.getAllStatus();
        model.addAttribute("status", statusEntities);
        model.addAttribute("statusSelected", tasksEntity.getStatusEntity().getId());

        return "task-update";
    }

    @PostMapping("/update/{id}")
    public String editTask(@PathVariable int id, @RequestParam int idJob,@RequestParam String nameTask,
                           @RequestParam int idUser, @RequestParam String startDate, @RequestParam String endDate,
                           @RequestParam int idStatus, Model model ){
        TasksEntity tasksEntity = taskService.getTaskById(id);
        JobsEntity jobsEntity = jobService.getJobById(idJob);
        UsersEntity usersEntity = userService.getUserById(idUser);
        StatusEntity statusEntity = statusService.getStatusById(idStatus);

        TasksEntity task = new TasksEntity();
        task.setId(id);
        task.setName(nameTask);
        task.setJobsEntity(jobsEntity);
        task.setUsersEntity(usersEntity);
        task.setStartDate(taskService.convertStringToDate(startDate));
        task.setEndDate(taskService.convertStringToDate(endDate));
        task.setStatusEntity(statusEntity);

        String notification = taskService.notificationUpdate(jobsEntity, usersEntity, tasksEntity, nameTask, startDate, endDate);
        model.addAttribute("notification", notification);

        boolean checckIsSuccess = taskService.updateTask(tasksEntity, task, jobsEntity, nameTask, usersEntity, startDate, endDate, statusEntity);
        model.addAttribute("checckIsSuccess", checckIsSuccess);

        model.addAttribute("task", tasksEntity);

        List<JobsEntity> jobsEntities = jobService.getAlljob();
        model.addAttribute("jobs", jobsEntities);
        model.addAttribute("jobsSelected", tasksEntity.getJobsEntity().getId());

        List<UsersEntity> usersEntities = userService.getAllUser();
        model.addAttribute("users", usersEntities);
        model.addAttribute("userSelected", tasksEntity.getUsersEntity().getId());

        List<StatusEntity> statusEntities = statusService.getAllStatus();
        model.addAttribute("status", statusEntities);
        model.addAttribute("statusSelected", tasksEntity.getStatusEntity().getId());

        return "task-update";
    }
}
