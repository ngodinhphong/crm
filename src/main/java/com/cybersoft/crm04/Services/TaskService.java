package com.cybersoft.crm04.Services;

import com.cybersoft.crm04.entity.JobsEntity;
import com.cybersoft.crm04.entity.StatusEntity;
import com.cybersoft.crm04.entity.TasksEntity;
import com.cybersoft.crm04.entity.UsersEntity;
import com.cybersoft.crm04.repository.TasksRepositiory;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TasksRepositiory tasksRepositiory;

    @Autowired
    private UserService userService;

    @Autowired
    private JobService jobService;

    public List<TasksEntity> getAllTask() {
        return tasksRepositiory.findAll();
    }

    public List<TasksEntity> getTaskByRole(HttpSession session) {
        List<TasksEntity> tasks = new ArrayList<>(List.of());
        UsersEntity users = userService.getUserBySession(session);
        if(users.getRolesEntity().getName().equals("ROLE_ADMIN")){
            return tasksRepositiory.findAll();
        } else if(users.getRolesEntity().getName().equals("ROLE_MANAGE")) {
            List<TasksEntity> tasksEntities = tasksRepositiory.findAll();
            List<JobsEntity> jobs = jobService.getJobByRole(session);
            tasks = tasksEntities.stream().filter(task -> jobs.stream().anyMatch(job -> task.getJobsEntity().getName().equals(job.getName()))).toList();
        } else {
            tasks = users.getTasks();
        }

        return tasks;
    }

    public Date convertStringToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Hoặc có thể ném ngoại lệ tùy thuộc vào yêu cầu của bạn
        }
    }

    public boolean checkConditions(String name, UsersEntity usersEntity, JobsEntity jobsEntity){
        List<TasksEntity> tasksEntities = getAllTask();
        boolean isSuccess = true;
        for (TasksEntity task : tasksEntities){
            // Kiểm tra điều kiện không thể vừa trùng tên công việc, tên user và tên dự án
            if (task.getName().equalsIgnoreCase(name) && task.getUsersEntity().getId() == usersEntity.getId()
                    && task.getJobsEntity().getId() == jobsEntity.getId()) {
                isSuccess = false;
                break;
            }
        }
        return isSuccess;
    }

    public boolean checkConditionsUpdate(TasksEntity tasksEntity, String nameTask, UsersEntity usersEntity, JobsEntity jobsEntity){
        List<TasksEntity> tasksEntities = getAllTask();
        boolean isSuccess = true;
        // Khi update tên công việc, tên dự án, tên user có thẻ trùng với dữ liệu đưa lên
        if(tasksEntity.getName().equalsIgnoreCase(nameTask) && tasksEntity.getUsersEntity().getId() == usersEntity.getId()
                && tasksEntity.getJobsEntity().getId() == jobsEntity.getId()){
            return isSuccess;
        }else {
            for (TasksEntity task : tasksEntities){
                // Kiểm tra điều kiện không thể vừa trùng tên công việc, tên user và tên dự án
                if (task.getName().equalsIgnoreCase(nameTask) && task.getUsersEntity().getId() == usersEntity.getId()
                        && task.getJobsEntity().getId() == jobsEntity.getId()) {
                    isSuccess = false;
                    break;
                }
            }
        }

        for (TasksEntity task : tasksEntities){
            // Kiểm tra điều kiện không thể vừa trùng tên công việc, tên user và tên dự án
            if(task.getName().equalsIgnoreCase(nameTask) && task.getUsersEntity().getId() == usersEntity.getId()
                    && task.getJobsEntity().getId() == jobsEntity.getId()){
                isSuccess = false;
            }
        }
        return isSuccess;
    }

    public boolean checkConditionsDate(JobsEntity jobsEntity, String startDate, String endDate){
        boolean isSuccess = true;
        //Ngày bắt đầu không thể sau ngày kết thúc của dự án
        if(convertStringToDate(startDate).after(convertStringToDate(endDate))){
            // Ngày bắt đầu không thể bằng ngày kết thúc của dự án
        } else if (convertStringToDate(startDate) == (convertStringToDate(endDate))) {
            isSuccess = false;
        }else {
            // Kiểm tra điều kiện ngày bắt đầu công việc không thể nằm ngoài ngày bắt đầu dự án và ngày kết thúc dự án,
            // ngày kết thúc thì ngược lại
            if(convertStringToDate(startDate).before(jobsEntity.getStartDate()) || convertStringToDate(startDate).after(jobsEntity.getEndDate())
                    || convertStringToDate(endDate).after(jobsEntity.getEndDate())){
                isSuccess = false;
            }
        }
        return isSuccess;
    }

    private boolean checkForNull(String nameTask, String startDate, String endDate, String description){
        return nameTask != null && !nameTask.isEmpty()
                && startDate != null && !startDate.isEmpty()
                && endDate != null && !endDate.isEmpty()
                && description != null && !description.isEmpty();
    }

    public String notificationSave(JobsEntity jobsEntity, UsersEntity usersEntity, String nameTask, String startDate, String endDate, String description){
        if(nameTask == null || nameTask.isEmpty()){
            return "Vui lòng nhập tên cng việc!";
        } else if (startDate == null || startDate.isEmpty()) {
            return "Vui lòng nhập ngày bắt đầu!";
        } else if (description == null || description.isEmpty()) {
            return "Vui lòng nhập mô tả công việc!";
        } else if (endDate == null || endDate.isEmpty()) {
            return "Vui lòng nhập ngày kết thúc!";
        } else if (!checkConditions(nameTask, usersEntity, jobsEntity)) {
            return "Công việc đã được người này đảm nhận!";
        } else if (!checkConditionsDate(jobsEntity, startDate, endDate)) {
            return "Ngày của công việc phải nằm trong khoảng ngày của dự án!";
        } else {
            return "";
        }
    }

    public boolean saveTask(JobsEntity jobsEntity, String nameTask, UsersEntity usersEntity, String startDate, String endDate, StatusEntity statusEntity, String description) {
        boolean isSuccess = false;

        TasksEntity tasksEntity = new TasksEntity();
        tasksEntity.setJobsEntity(jobsEntity);
        tasksEntity.setName(nameTask);
        tasksEntity.setDescription(description);
        tasksEntity.setUsersEntity(usersEntity);
        tasksEntity.setStartDate(convertStringToDate(startDate));
        tasksEntity.setEndDate(convertStringToDate(endDate));
        tasksEntity.setStatusEntity(statusEntity);

        if(checkForNull(nameTask, startDate, endDate, description) && checkConditions(nameTask, usersEntity, jobsEntity)
                && checkConditionsDate(jobsEntity, startDate, endDate)){
            try {
                tasksRepositiory.save(tasksEntity);
                isSuccess = true;
            }catch (Exception e) {
                System.out.println("Lỗi Thêm dữ lệu" + e.getMessage());
            }
        }
        return isSuccess;
    }

    public TasksEntity getTaskById(int id) {
        TasksEntity task = null;
        Optional<TasksEntity> usersEntity = tasksRepositiory.findById(id);

        if (usersEntity.isPresent()) {
            task = usersEntity.get();
        }

        return task;
    }

    public void deleteTassk(int id) {
        tasksRepositiory.deleteById(id);
    }

    public String notificationUpdate(JobsEntity jobsEntity, UsersEntity usersEntity, TasksEntity tasksEntity, String nameTask , String description, String startDate, String endDate){
        if(nameTask == null || nameTask.isEmpty()){
            return "Vui lòng nhập tên cng việc!";
        } else if (startDate == null || startDate.isEmpty()) {
            return "Vui lòng nhập ngày bắt đầu!";
        } else if (description == null || description.isEmpty()) {
            return "Vui lòng nhập mô tả công việc!";
        } else if (endDate == null || endDate.isEmpty()) {
            return "Vui lòng nhập ngày kết thúc!";
        } else if (!checkConditionsUpdate(tasksEntity, nameTask, usersEntity, jobsEntity)) {
            return "Công việc đã được người này đảm nhận!";
        } else if (!checkConditionsDate(jobsEntity, startDate, endDate)) {
            return "Ngày của công việc phải nằm trong khoảng ngày của dự án!";
        } else {
            return "";
        }
    }

    public boolean updateTask(TasksEntity tasksEntity, TasksEntity task, JobsEntity jobsEntity, String nameTask, UsersEntity usersEntity, String startDate, String endDate, String description) {
        boolean isSuccess = false;

        if(checkForNull(nameTask, startDate, endDate, description) && checkConditionsUpdate(tasksEntity, nameTask, usersEntity, jobsEntity)
                && checkConditionsDate(jobsEntity, startDate, endDate)){
            try {
                tasksRepositiory.save(task);
                isSuccess = true;
            }catch (Exception e) {
                System.out.println("Lỗi Thêm dữ lệu" + e.getMessage());
            }
        }
        return isSuccess;
    }
}
