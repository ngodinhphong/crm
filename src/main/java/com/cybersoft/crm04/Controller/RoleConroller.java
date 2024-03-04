package com.cybersoft.crm04.Controller;

import com.cybersoft.crm04.Services.RoleService;
import com.cybersoft.crm04.Services.UserService;
import com.cybersoft.crm04.entity.RolesEntity;
import com.cybersoft.crm04.entity.UsersEntity;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Bước 1: Xác định câu truy vấn
 * Bớc 2 : xác định câu truy vấn
 *  Bước 3 : Xác định đươc số lượng tham số sẻ sử dụng cho controller ( đường dẫn đã khai báo)
 *  Bước 4 : để thực hiện được các câu truy vấn đã xác định được ở bước 2 thì phải tạo ra file repository
 *  để quản lý các câu truy vấn nếu chưa có.
 *  Bước 5 : xác định hàm tương ứng với câu truy vấn bước 2 của JPA
 *
 * @return
 */

/**
 * - chỉnh link/role thành /role/add: fix lỗi liên quan đến js bên file html
 * - Nếu thêm role thành cong thì phải xuất thông báo ra màng hình thông báo "thêm thành công" hoặc "thêm thất bại"
 *
 */

@Controller
@RequestMapping("/role")
public class RoleConroller {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @GetMapping("/add")
    public String add(HttpSession session, Model model){

        UsersEntity users = userService.getUserBySession(session);
        String avatarPath = userService.getPathAvata(users);
        model.addAttribute("avatarPath",avatarPath);
        return "role-add";
    }

    @PostMapping("/add")
    public String processAnd(@RequestParam String roleName, @RequestParam String desc, HttpSession session, Model model){

        UsersEntity users = userService.getUserBySession(session);
        String avatarPath = userService.getPathAvata(users);
        model.addAttribute("avatarPath",avatarPath);

        String notification = roleService.notificationSave(roleName,desc);
        model.addAttribute("notification", notification);

        boolean checckIsSuccess = roleService.insertRole(roleName,desc);
        model.addAttribute("checckIsSuccess", checckIsSuccess);

        model.addAttribute("roleName", roleName);
        model.addAttribute("desc", desc);

        return "role-add";
    }

    // Yêu cầu lấy toàn bộ danh sách role và hển thị lên giao diện role-table.html
    @GetMapping("/show")
    public String showRole(HttpSession session, Model model){

        UsersEntity users = userService.getUserBySession(session);
        String avatarPath = userService.getPathAvata(users);
        model.addAttribute("avatarPath",avatarPath);

        List<RolesEntity> listRole = roleService.getAllRole();
        model.addAttribute("roles", listRole);

        return "role-table";
    }

    @GetMapping("/delete/{id}")
    public String removeRole(@PathVariable int id){

        roleService.deleteRole(id);

        return "redirect:/role/show";
    }

    @GetMapping("/update/{id}")
    public String editRole(@PathVariable int id, HttpSession session, Model model){

        UsersEntity users = userService.getUserBySession(session);
        String avatarPath = userService.getPathAvata(users);
        model.addAttribute("avatarPath",avatarPath);

        RolesEntity rolesEntity = roleService.getRoleById(id);
        model.addAttribute("roleEntity", rolesEntity);

        return "role-update";
    }

    @PostMapping("/update/{id}")
    public String progressRole(@PathVariable int id, @RequestParam String roleName,
                               @RequestParam String desc, HttpSession session, Model model){

        UsersEntity users = userService.getUserBySession(session);
        String avatarPath = userService.getPathAvata(users);
        model.addAttribute("avatarPath",avatarPath);

        RolesEntity role = roleService.getRoleById(id);
        RolesEntity rolesEntity = new RolesEntity();
        rolesEntity.setId(id);
        rolesEntity.setName(roleName);
        rolesEntity.setDescription(desc);

        String notification = roleService.notificationUpdate(roleName,desc, role);
        model.addAttribute("notification", notification);

        boolean checckIsSuccess = roleService.updateRole(roleName,desc, rolesEntity, role);
        model.addAttribute("checckIsSuccess", checckIsSuccess);

        model.addAttribute("roleEntity", rolesEntity);

        return "role-update";
    }




}

/**
 * Controller: chỉ dùng để khai báo đường dẫn và xác nhận tham số của người dùng truyền lên ( không sử lý logic code ở đây)
 * Service: Chịu trách nhiệm xử lý logic code về Controller
 * Respository : Nơi định nghĩa câu truy vấn liên tới CSDL và trả kết quả của truy vấn cho Service. (Không xử lý code)
 */
