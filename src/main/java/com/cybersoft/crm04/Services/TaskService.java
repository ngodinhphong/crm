package com.cybersoft.crm04.Services;

import com.cybersoft.crm04.entity.JobsEntity;
import com.cybersoft.crm04.entity.StatusEntity;
import com.cybersoft.crm04.entity.TasksEntity;
import com.cybersoft.crm04.entity.UsersEntity;
import com.cybersoft.crm04.repository.TasksRepositiory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TasksRepositiory tasksRepositiory;

    public List<TasksEntity> getAllTask() {
        return tasksRepositiory.findAll();
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
            System.out.println("Kiểm tra name 1");
            // Kiểm tra điều kiện không thể vừa trùng tên công việc, tên user và tên dự án
            if(task.getName().equalsIgnoreCase(name) && task.getUsersEntity().getId() == usersEntity.getId()
                && task.getJobsEntity().getId() == jobsEntity.getId()){
                System.out.println("Kiểm tra name 2");
                isSuccess = false;
            }
        }
        System.out.println("Kiểm tra dk name " + isSuccess);
        return isSuccess;
    }

    public boolean checkConditionsDate(JobsEntity jobsEntity, String startDate, String endDate){
        boolean isSuccess = true;
            //Ngày bắt đầu không thể sau ngày kết thúc
        if(convertStringToDate(startDate).after(convertStringToDate(endDate))){
            System.out.println("Kiểm tra Date 1");
            // Ngày bắt đầu không thể bằng ngày kết thúc
        } else if (convertStringToDate(startDate) == (convertStringToDate(endDate))) {
            System.out.println("Kiểm tra Date 2");
            isSuccess = false;
        }else {
            System.out.println("Kiểm tra Date 3");
            // Kiểm tra điều kiện ngày bắt đầu công việc không thể nằm ngoài ngày bắt đầu dự án và ngày kết thúc dự án,
            // ngày kết thúc thì ngược lại
            if(convertStringToDate(startDate).before(jobsEntity.getStartDate()) && convertStringToDate(startDate).after(jobsEntity.getEndDate())
                    && convertStringToDate(endDate).after(jobsEntity.getEndDate())){
                System.out.println("Kiểm tra Date 4");
                isSuccess = false;
            }
        }
        System.out.println("Kiểm tra dk date " + isSuccess);
        return isSuccess;
    }

    public boolean saveTask(JobsEntity jobsEntity, String nameTask, UsersEntity usersEntity, String startDate, String endDate, StatusEntity statusEntity) {
        boolean isSuccess = false;

        TasksEntity tasksEntity = new TasksEntity();
        tasksEntity.setJobsEntity(jobsEntity);
        tasksEntity.setName(nameTask);
        tasksEntity.setUsersEntity(usersEntity);
        tasksEntity.setStartDate(convertStringToDate(startDate));
        tasksEntity.setEndDate(convertStringToDate(endDate));
        tasksEntity.setStatusEntity(statusEntity);
        System.out.println("Kiểm tra 1");

        if(checkConditions(nameTask, usersEntity, jobsEntity)
                && checkConditionsDate(jobsEntity, startDate, endDate)){
            System.out.println("Kiểm tra 2");
            try {
                tasksRepositiory.save(tasksEntity);
                isSuccess = true;
                System.out.println("Kiểm tra 3");
            }catch (Exception e) {
                System.out.println("Lỗi Thêm dữ lệu" + e.getMessage());
            }
        }
        return isSuccess;
    }
}
