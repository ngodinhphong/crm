package com.cybersoft.crm04.Controller;

import com.cybersoft.crm04.Services.*;
import com.cybersoft.crm04.entity.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private JobService jobService;

    @Autowired
    private StatusService statusService;

    @GetMapping("/show")
    public String showUser(Model model){

        List<UsersEntity> listUser = userService.getAllUser();
        model.addAttribute("users", listUser);

        return "user-table";
    }

    @GetMapping("/add")
    public String userTableAdd(Model model){
        List<RolesEntity> listRole = roleService.getAllRole();
        model.addAttribute("roleName", listRole);
        model.addAttribute("roleNameSelect", 0);

        return "user-add";
    }

    @PostMapping("/add")
    public String showUsser(@RequestParam String fullName,@RequestParam String  userName,
                            @RequestParam String email, @RequestParam String password,
                            @RequestParam String phoneNo, @RequestParam int roleId, Model model){

        RolesEntity role = roleService.getRoleById(roleId);
        String notification = userService.notificationSave(fullName, userName, email, password, phoneNo);
        model.addAttribute("notification", notification);

        boolean checckIsSuccess = userService.saveUsser(fullName, userName, email, password, phoneNo, role);
        model.addAttribute("isSuccess", checckIsSuccess);

        model.addAttribute("fullname", fullName);
        model.addAttribute("userName", userName);
        model.addAttribute("email", email);
        model.addAttribute("password", password);
        model.addAttribute("phoneNo", phoneNo);

        List<RolesEntity> listRole = roleService.getAllRole();
        model.addAttribute("roleName", listRole);

        model.addAttribute("roleNameSelect", role.getId());

        return "user-add";
    }

    @GetMapping("/update/{id}")
    public String editUser(@PathVariable int id, Model model){
        UsersEntity usersEntity = userService.getUserById(id);
        model.addAttribute("usersEntity", usersEntity);

        List<RolesEntity> listRole = roleService.getAllRole();
        model.addAttribute("roleName", listRole);
        model.addAttribute("roleNameSelect", usersEntity.getRolesEntity().getId());

        return "user-update";
    }

    @PostMapping("/update/{id}")
    public String editUser(@PathVariable int id, @RequestParam String fullName,@RequestParam String  userName,
                           @RequestParam String email, @RequestParam String password,
                           @RequestParam String phoneNo, @RequestParam int roleId, Model model ){

        UsersEntity user = userService.getUserById(id);
        RolesEntity role = roleService.getRoleById(roleId);
        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setId(id);
        usersEntity.setFullName(fullName);
        usersEntity.setUserName(userName);
        usersEntity.setEmail(email);
        usersEntity.setPassword(password);
        usersEntity.setPhoneNo(phoneNo);
        usersEntity.setRolesEntity(role);
        String notification = userService.notificationUpdate(fullName, userName, email, password, phoneNo, user);
        model.addAttribute("notification", notification);

        boolean checckIsSuccess = userService.updateUsser(fullName, userName, email, password, phoneNo, user, usersEntity);
        model.addAttribute("isSuccess", checckIsSuccess);

        model.addAttribute("usersEntity", usersEntity);

        List<RolesEntity> listRole = roleService.getAllRole();
        model.addAttribute("roleName", listRole);

        model.addAttribute("roleNameSelect", usersEntity.getRolesEntity().getId());

        return "user-update";
    }

    @GetMapping("/look/{id}")
    public String refer(@PathVariable int id, Model model){
        UsersEntity user = userService.getUserById(id);
        model.addAttribute("usersEntity", user);

        List<TasksEntity> taskUnfulfilled = userService.checkTasksUnfulfilled(user);
        model.addAttribute("unfulfilled", taskUnfulfilled);

        List<TasksEntity> taskProcessing = userService.checkTasksProcessing(user);
        model.addAttribute("processing", taskProcessing);

        List<TasksEntity> taskMade = userService.checkTasksMade(user);
        model.addAttribute("made", taskMade);

        int quantityUnfulfilled = userService.getTaskUnfulfilled(user);
        model.addAttribute("quantityUnfulfilled", quantityUnfulfilled);

        int quantityProcessing = userService.getTaskProcessing(user);
        model.addAttribute("quantityProcessing", quantityProcessing);

        int quantityCompleted = userService.getTaskCompleted(user);
        model.addAttribute("quantityCompleted", quantityCompleted);

        return "user-details";
    }

    @GetMapping("/delete/{id}")
    public String removeUser(@PathVariable int id){
        userService.deleteUser(id);

        return "redirect:/user/show";
    }

    @GetMapping("/profile")
    public String ShowProfile(Model model, HttpServletRequest request){

        // Lấy lấy user từ sesion ở login
        HttpSession session = request.getSession();
        UsersEntity usersEntity = userService.getUserBySession(session);
        model.addAttribute("usersEntity", usersEntity );

        List<TasksEntity> tasksList = usersEntity.getTasks();
        model.addAttribute("tasksList", tasksList );

        int quantityUnfulfilled = userService.getTaskUnfulfilled(usersEntity);
        model.addAttribute("quantityUnfulfilled", quantityUnfulfilled);

        int quantityProcessing = userService.getTaskProcessing(usersEntity);
        model.addAttribute("quantityProcessing", quantityProcessing);

        int quantityCompleted = userService.getTaskCompleted(usersEntity);
        model.addAttribute("quantityCompleted", quantityCompleted);

        return "profile";
    }

    @GetMapping("/profile/update/{id}")
    public String showProfileUpdate(@PathVariable int id, Model model){

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

        return "profile-update";
    }

    @PostMapping("/profile/update/{id}")
    public String updateProfile(@PathVariable int id, @RequestParam int idJob,@RequestParam String nameTask,
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

        boolean checkConditionnal = taskService.checkConditionsUpdate(tasksEntity, nameTask, usersEntity, jobsEntity);
        model.addAttribute("checkConditionnal", checkConditionnal);

        boolean checkConditionsDate = taskService.checkConditionsDate(jobsEntity, startDate, endDate);
        model.addAttribute("checkConditionsDate", checkConditionsDate);

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

        return "profile-update";
    }

    @GetMapping("/detail")
    public String ShowProfileDetail(Model model, HttpServletRequest request){

        // Lấy lấy user từ sesion ở login
        HttpSession session = request.getSession();
        UsersEntity user = userService.getUserBySession(session);
        model.addAttribute("usersEntity", user);

        List<TasksEntity> taskUnfulfilled = userService.checkTasksUnfulfilled(user);
        model.addAttribute("unfulfilled", taskUnfulfilled);

        List<TasksEntity> taskProcessing = userService.checkTasksProcessing(user);
        model.addAttribute("processing", taskProcessing);

        List<TasksEntity> taskMade = userService.checkTasksMade(user);
        model.addAttribute("made", taskMade);

        int quantityUnfulfilled = userService.getTaskUnfulfilled(user);
        model.addAttribute("quantityUnfulfilled", quantityUnfulfilled);

        int quantityProcessing = userService.getTaskProcessing(user);
        model.addAttribute("quantityProcessing", quantityProcessing);

        int quantityCompleted = userService.getTaskCompleted(user);
        model.addAttribute("quantityCompleted", quantityCompleted);

        return "user-details";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {

        userService.deleteSession(request);
        return "redirect:/login";
    }
}
