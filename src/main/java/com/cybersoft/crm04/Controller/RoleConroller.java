package com.cybersoft.crm04.Controller;

import com.cybersoft.crm04.entity.RolesEntity;
import com.cybersoft.crm04.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/role")
public class RoleConroller {

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
    @Autowired
    private RolesRepository rolesRepository;

    /**
     * Save() : có2 chức năng vừa thêm mới dữ liệu và cập nhật dữ liệu
     *  - Thêm mới : khóa chính của class entity truyền vào hàm save() không có giá trị khóa chính
     *  - cập nhật : khóa chính của class entity truyền vào hàm save() có giá trị
     *
     * @return
     */

    @GetMapping("/add")

    public String add(){

//        RolesEntity rolesEntity = new RolesEntity();
//        rolesEntity.setName("Test");
//        rolesEntity.setDescription("Hello test");
//        rolesRepository.save(rolesEntity);

        return "role-add.html";
    }

    @PostMapping("/add")
    public String processAnd(@RequestParam String roleName, @RequestParam String desc, Model model){

        RolesEntity rolesEntity = new RolesEntity();
        rolesEntity.setName(roleName);
        rolesEntity.setDescription(desc);
        if(rolesEntity != null){
            model.addAttribute("notification", "Data added successfully!");
        }

        try {
            rolesRepository.save(rolesEntity);
//            model.addAttribute("notification", "Add to success");
        } catch (Exception e){
            // Code bên trong chỉ chạy khi các đoạn code bên trong try bị lỗi liên quan đến Runtime Error
            System.out.println("Lỗi Thêm dữ lệu" + e.getMessage());
            model.addAttribute("notification", "Data added failed!");
        }


        /**
         * - chỉnh link/role thành /role/add: fix lỗi liên quan đến js bên file html
         * - Nếu thêm role thành cong thì phải xuất thông báo ra màng hình thông báo "thêm thành công" hoặc "thêm thất bại"
         *
         */

        return "role-add.html";
    }


}
