package com.cybersoft.crm04.Services;
// Lưu ý tên Service sẻ giống vói têncontroller. bởi vì Service là nơi sử lsy code cho Controller

import com.cybersoft.crm04.entity.RolesEntity;
import com.cybersoft.crm04.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RolesRepository rolesRepository;

    public List<RolesEntity> getAllRole() {

        return rolesRepository.findAll();
    }

    public void deleteRole(int id) {

        rolesRepository.deleteById(id);
    }

    public void insertRole(String roleName, String desc, Model model) {
        RolesEntity rolesEntity = new RolesEntity();
        rolesEntity.setName(roleName);
        rolesEntity.setDescription(desc);
        if (rolesEntity != null) {
            model.addAttribute("notification", "Data added successfully!");
        }

        try {
            rolesRepository.save(rolesEntity);
//            model.addAttribute("notification", "Add to success");
        } catch (Exception e) {
            // Code bên trong chỉ chạy khi các đoạn code bên trong try bị lỗi liên quan đến Runtime Error
            System.out.println("Lỗi Thêm dữ lệu" + e.getMessage());
            model.addAttribute("notification", "Data added failed!");
        }
    }

    public RolesEntity getRoleById(int id) {
        // Optional: có hoặc không có cũng được.
        // Optional chứa các hàm hỗ trợ sẵn giúp kiểm tra giá trị có null hay không để tránh bị lỗi null dữ liệu trong quá trình sử lí
        RolesEntity dataRole = null;
        Optional<RolesEntity> rolesEntity = rolesRepository.findById(id);
        // isPresent : kiểm tra xem biến có giá trị hay không nếu là true tức biến có giá trị, nếu là false th sẻ không có giá trị

        if (rolesEntity.isPresent()) {
            dataRole = rolesEntity.get();
        }

        return dataRole;
    }

    public void updateRole(RolesEntity rolesEntity){
        rolesRepository.save(rolesEntity);
    }

}

