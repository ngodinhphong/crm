package com.cybersoft.crm04.Services;

import com.cybersoft.crm04.entity.JobsEntity;
import com.cybersoft.crm04.entity.RolesEntity;
import com.cybersoft.crm04.entity.TasksEntity;
import com.cybersoft.crm04.entity.UsersEntity;
import com.cybersoft.crm04.repository.RolesRepository;
import com.cybersoft.crm04.repository.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UsersRepository usersRepository;

    public List<UsersEntity> getAllUser(){
        return usersRepository.findAll();
    }

    public List<UsersEntity> getUserForUpdate(HttpSession session){
        UsersEntity users = getUserBySession(session);
        if(users.getRolesEntity().getName().equals("ROLE_USER")){
            return Collections.singletonList(users);
        }else return usersRepository.findAll();
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

    public int getTaskUnfulfilled(UsersEntity user){

        List<TasksEntity> tasksEntities = user.getTasks();
        float quantity = 0;
        for (TasksEntity tasks : tasksEntities){
            if(tasks.getStatusEntity().getName().equals("Chưa thực hiện")){
                quantity += 1;
            }
        }
        if(quantity == 0){
            return 0;
        }else{
            return (int)(quantity/(float)tasksEntities.size()*100);
        }

    }

    public int getTaskProcessing(UsersEntity user){

        List<TasksEntity> tasksEntities = user.getTasks();
        float quantity = 0;
        for (TasksEntity tasks : tasksEntities){
            if(tasks.getStatusEntity().getName().equals("Đang thực hiện")){
                quantity += 1;
            }
        }
        if(quantity == 0){
            return 0;
        }else{
            return (int)(quantity/(float)tasksEntities.size()*100);
        }

    }

    public int getTaskCompleted(UsersEntity user){

        List<TasksEntity> tasksEntities = user.getTasks();
        float quantity = 0;
        for (TasksEntity tasks : tasksEntities){
            if(tasks.getStatusEntity().getName().equals("Đã hoàn thành")){
                quantity += 1;
            }
        }
        if(quantity == 0){
            return 0;
        }else{
            return (int)(quantity/(float)tasksEntities.size()*100);
        }

    }


    private boolean CheckEmailExists(String email){
        List<UsersEntity> usersList = usersRepository.findAll();
        return usersList.stream().anyMatch(users -> users.getEmail().equalsIgnoreCase(email));
    }

    private boolean checkForNull (String fullName, String userName, String email, String password, String phoneNo){

        return fullName != null && !fullName.isEmpty()
                && userName != null && !userName.isEmpty()
                && email != null && !email.isEmpty()
                && password != null && !password.isEmpty()
                && phoneNo != null && !phoneNo.isEmpty();
    }

    public String notificationSave (String fullName, String userName, String email, String password, String phoneNo){
        if(fullName == null || fullName.isEmpty()){
            return "Vui lòng nhập tên!";
        } else if (userName == null || userName.isEmpty()) {
            return "Vui lòng nhập UserName!";
        } else if (email == null || email.isEmpty()) {
            return "Vui lòng nhập Email!";
        } else if (password == null || password.isEmpty()) {
            return "Vui lòng nhập Password!";
        } else if (phoneNo == null || phoneNo.isEmpty()) {
            return "Vui lòng nhập số điện thoại!";
        }else if (CheckEmailExists(email)) {
            return "Email đã tồn tại!";
        } else{
            return "";
        }
    }

    @Value("${upload.path.user}")
    private String uploadPathUser;
    @Value("${upload.path.large}")
    private String uploadPathLarge;

    public String getPathAvatar(MultipartFile file){
        String newPath = null;
        try {
            byte[] bytes = file.getBytes();
            Path pathUser = Paths.get(uploadPathUser + file.getOriginalFilename());
            Files.write(pathUser, bytes);
            System.out.println("Check pathUser:" + pathUser.toString() + " " + Files.write(pathUser, bytes));
            Path pathLarge = Paths.get(uploadPathLarge + file.getOriginalFilename());
            Files.write(pathLarge, bytes);
            String originalPath = pathUser.toString();
            newPath = originalPath.replace("src\\main\\resources\\static\\", "");
            System.out.println("Check : " + newPath);
        } catch (Exception e){
            e.printStackTrace();
        }
        return newPath;
    }

    public boolean saveUsser(String fullName, String userName, String email, String password, String phoneNo, RolesEntity role, MultipartFile file) {
        boolean isSuccess = false;
        try {

            String newPath = getPathAvatar(file);
            UsersEntity usersEntity = new UsersEntity();
            usersEntity.setFullName(fullName);
            usersEntity.setUserName(userName);
            usersEntity.setAvatar(newPath);
            usersEntity.setEmail(email);
            usersEntity.setPassword(password);
            usersEntity.setPhoneNo(phoneNo);
            usersEntity.setRolesEntity(role);
            if (checkForNull(fullName, userName, email, password, phoneNo) && !CheckEmailExists(email)){
                    usersRepository.save(usersEntity);
                    isSuccess = true;
            }else {
                System.out.println("thêm thất bại!");
            }
        }catch (Exception e) {
            System.out.println("Lỗi Thêm dữ lệu" + e.getMessage());
        }
        return isSuccess;
    }
    public String notificationUpdate (String fullName, String userName, String email, String password, String phoneNo, UsersEntity user){
        if(fullName == null || fullName.isEmpty()){
            return "Vui lòng nhập tên!";
        } else if (userName == null || userName.isEmpty()) {
            return "Vui lòng nhập UserName!";
        } else if (email == null || email.isEmpty()) {
            return "Vui lòng nhập Email!";
        } else if (password == null || password.isEmpty()) {
            return "Vui lòng nhập Password!";
        } else if (phoneNo == null || phoneNo.isEmpty()) {
            return "Vui lòng nhập số điện thoại!";
        }else if (email.equalsIgnoreCase(user.getEmail())) {
            return "";
        }else if (CheckEmailExists(email)) {
            return "Email đã tồn tại!";
        } else{
            return "";
        }
    }

    public boolean updateUsser(String fullName, String userName, String email, String password, String phoneNo, UsersEntity user, UsersEntity usersEntity) {
        boolean isSuccess = false;
        if (checkForNull(fullName, userName, email, password, phoneNo) && (email.equalsIgnoreCase(user.getEmail()) || !CheckEmailExists(email))){
            try {
                usersRepository.save(usersEntity);
                isSuccess = true;
            }catch (Exception e) {
                System.out.println("Lỗi Thêm dữ lệu" + e.getMessage());
            }
        }
        return isSuccess;

    }

    public UsersEntity getUserBySession(HttpSession session) {
        UsersEntity usersEntity = null;
        String email = "";
        if(session != null && session.getAttribute("email")!= null && !session.getAttribute("email").equals("")){
            email = (String) session.getAttribute("email");
            usersEntity = usersRepository.getByEmail(email);
        } else {
            System.out.println("Không thấy email");
        }
        return usersEntity;
    }

    public void deleteSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    public String getPathAvata(UsersEntity users){
        String avatarPath = users.getAvatar();
        if(avatarPath != null){
            return avatarPath;
        } else return "plugins/images/users/avatar-vo-danh.jpg";
    }

}
