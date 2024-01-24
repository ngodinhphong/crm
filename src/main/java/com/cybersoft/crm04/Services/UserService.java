package com.cybersoft.crm04.Services;

import com.cybersoft.crm04.entity.RolesEntity;
import com.cybersoft.crm04.entity.TasksEntity;
import com.cybersoft.crm04.entity.UsersEntity;
import com.cybersoft.crm04.repository.RolesRepository;
import com.cybersoft.crm04.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UsersRepository usersRepository;

    public List<UsersEntity> getAllUser(){
        return usersRepository.findAll();
    }

    public UsersEntity getUserById(int id){

        UsersEntity dataUser = null;
        Optional<UsersEntity> usersEntity = usersRepository.findById(id);

        if (usersEntity.isPresent()) {
            dataUser = usersEntity.get();
        }

        return dataUser;
    }

    public List<TasksEntity> checkTasksUnfulfilled(UsersEntity listUser){
        List<TasksEntity> tasksEntities = listUser.getTasks();
        return tasksEntities.stream()
                .filter(tasks -> tasks.getStatusEntity().getName().equals("Chưa thực hiện")).toList();
    }

    public List<TasksEntity> checkTasksProcessing(UsersEntity listUser){
        List<TasksEntity> tasksEntities = listUser.getTasks();
        return tasksEntities.stream()
                .filter(tasks -> tasks.getStatusEntity().getName().equals("Đang thực hiện")).toList();
    }

    public List<TasksEntity> checkTasksMade(UsersEntity listUser){
        List<TasksEntity> tasksEntities = listUser.getTasks();
        return tasksEntities.stream()
                .filter(tasks -> tasks.getStatusEntity().getName().equals("Đã hoàn thành")).toList();
    }

    public void deleteUser(int id) {

        usersRepository.deleteById(id);
    }

    private boolean CheckEmailExists(String email){
        List<UsersEntity> usersList = usersRepository.findAll();
        return usersList.stream().anyMatch(users -> users.getEmail().equalsIgnoreCase(email));
    }

    public boolean saveUsser(String fullName, String userName, String email, String password, String phoneNo, RolesEntity role) {
        boolean isSuccess = false;

        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setFullName(fullName);
        usersEntity.setUserName(userName);
        usersEntity.setEmail(email);
        usersEntity.setPassword(password);
        usersEntity.setPhoneNo(phoneNo);
        usersEntity.setRolesEntity(role);
        if (!CheckEmailExists(email)){
            try {
                usersRepository.save(usersEntity);
                isSuccess = true;
            }catch (Exception e) {
                System.out.println("Lỗi Thêm dữ lệu" + e.getMessage());
            }
        }
        return isSuccess;
    }

    public boolean updateUsser(String email, UsersEntity user, UsersEntity usersEntity) {
        boolean isSuccess = false;
        if ( email.equalsIgnoreCase(user.getEmail()) || !CheckEmailExists(email)){
            try {
                usersRepository.save(usersEntity);
                isSuccess = true;
            }catch (Exception e) {
                System.out.println("Lỗi Thêm dữ lệu" + e.getMessage());
            }
        }
        return isSuccess;

    }
}
