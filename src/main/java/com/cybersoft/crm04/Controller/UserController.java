package com.cybersoft.crm04.Controller;

import com.cybersoft.crm04.Services.RoleService;
import com.cybersoft.crm04.Services.UserService;
import com.cybersoft.crm04.entity.RolesEntity;
import com.cybersoft.crm04.entity.TasksEntity;
import com.cybersoft.crm04.entity.UsersEntity;
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
        boolean checckIsSuccess = userService.updateUsser(email, user, usersEntity);
        model.addAttribute("isSuccess", checckIsSuccess);

        model.addAttribute("usersEntity", usersEntity);

        List<RolesEntity> listRole = roleService.getAllRole();
        model.addAttribute("roleName", listRole);

        model.addAttribute("roleNameSelect", usersEntity.getRolesEntity().getId());

        return "user-update";
    }

    @GetMapping("/look/{id}")
    public String refer(@PathVariable int id, Model model){
        UsersEntity User = userService.getUserById(id);
        model.addAttribute("usersEntity", User);

        List<TasksEntity> taskUnfulfilled = userService.checkTasksUnfulfilled(User);
        model.addAttribute("unfulfilled", taskUnfulfilled);

        List<TasksEntity> taskProcessing = userService.checkTasksProcessing(User);
        model.addAttribute("processing", taskProcessing);

        List<TasksEntity> taskMade = userService.checkTasksMade(User);
        model.addAttribute("made", taskMade);
//
        return "user-details";
    }

    @GetMapping("/delete/{id}")
    public String removeUser(@PathVariable int id){
        UsersEntity User = userService.getUserById(id);

        userService.deleteUser(id);

        return "redirect:/user/show";
    }

}
