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

    private boolean checkRoleName(String roleName) {
        List<RolesEntity> rolesEntityList = rolesRepository.findAll();
        return rolesEntityList.stream().anyMatch(role -> role.getName().equalsIgnoreCase(roleName));
    }

    private boolean checkForNull(String roleName, String desc){
        return roleName != null && !roleName.isEmpty() && desc != null && !desc.isEmpty();
    }

    public String notificationSave(String roleName, String desc){
        if(roleName == null || roleName.isEmpty()){
            return "Vui lòng nhập tên quyền!";
        } else if (desc == null || desc.isEmpty()) {
            return "Vui lòng nhập mô tả!";
        } else if (checkRoleName(roleName)) {
            return "Tên quyền đã tồn tại!";
        } else {
            return "";
        }
    }

    public boolean insertRole(String roleName, String desc) {
        boolean isSuccess = false;

        RolesEntity rolesEntity = new RolesEntity();
        rolesEntity.setName(roleName);
        rolesEntity.setDescription(desc);
        if (checkForNull(roleName, desc) && !checkRoleName(roleName)) {

            try {
                rolesRepository.save(rolesEntity);
                isSuccess = true;
            } catch (Exception e) {
                // Code bên trong chỉ chạy khi các đoạn code bên trong try bị lỗi liên quan đến Runtime Error
                System.out.println("Lỗi Thêm dữ lệu" + e.getMessage());
            }
        }
        return isSuccess;
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

    public String notificationUpdate(String roleName, String desc, RolesEntity role){
        if(roleName == null || roleName.isEmpty()){
            return "Vui lòng nhập tên quyền!";
        } else if (desc == null || desc.isEmpty()) {
            return "Vui lòng nhập mô tả!";
        } else if (roleName.equalsIgnoreCase(role.getName())) {
            return "";
        } else if (checkRoleName(roleName)) {
            return "Tên quyền đã tồn tại!";
        } else {
            return "";
        }
    }

    public boolean updateRole(String roleName, String desc, RolesEntity rolesEntity, RolesEntity role){
        boolean isSuccess = false;

        if(checkForNull(roleName, desc) && (roleName.equalsIgnoreCase(role.getName()) || !checkRoleName(roleName))){
            try {
                rolesRepository.save(rolesEntity);
                isSuccess = true;
            }catch (Exception e){
                System.out.println("Lỗi Thêm dữ lệu" + e.getMessage());
            }
        }
        return isSuccess;
    }

}

