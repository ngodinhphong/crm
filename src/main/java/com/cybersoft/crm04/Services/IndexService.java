package com.cybersoft.crm04.Services;

import com.cybersoft.crm04.entity.TasksEntity;
import com.cybersoft.crm04.repository.TasksRepositiory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndexService {

    @Autowired
    private TasksRepositiory tasksRepositiory;

    public int getTaskUnfulfilled(){
        int quantity = 0;
        List<TasksEntity> tasksEntities = tasksRepositiory.findAll();

        for (TasksEntity tasks : tasksEntities){
            if(tasks.getStatusEntity().getName().equals("Chưa thực hiện")){
                quantity += 1;
            }
        }
        return quantity;
    }

    public int getTaskProcessing(){
        int quantity = 0;
        List<TasksEntity> tasksEntities = tasksRepositiory.findAll();

        for (TasksEntity tasks : tasksEntities){
            if(tasks.getStatusEntity().getName().equals("Đang thực hiện")){
                quantity += 1;
            }
        }
        return quantity;
    }

    public int getTaskCompleted(){
        int quantity = 0;
        List<TasksEntity> tasksEntities = tasksRepositiory.findAll();

        for (TasksEntity tasks : tasksEntities){
            if(tasks.getStatusEntity().getName().equals("Đã hoàn thành")){
                quantity += 1;
            }
        }
        return quantity;
    }

}
