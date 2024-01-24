package com.cybersoft.crm04.Services;

import com.cybersoft.crm04.entity.JobsEntity;
import com.cybersoft.crm04.entity.TasksEntity;
import com.cybersoft.crm04.entity.UsersEntity;
import com.cybersoft.crm04.repository.JobsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobService {

    @Autowired
    private JobsRepository jobsRepository;


    public List<JobsEntity> getAlljob() {
        return jobsRepository.findAll();

    }

    public JobsEntity getJobById(int id) {
        JobsEntity datajobs = null;
        Optional<JobsEntity> jobsEntity = jobsRepository.findById(id);

        if (jobsEntity.isPresent()) {
            datajobs = jobsEntity.get();
        }
        return datajobs;
    }

    public List<UsersEntity> getUserByTask(List<TasksEntity> listTask){

        return listTask.stream().map(TasksEntity::getUsersEntity).distinct().collect(Collectors.toList());
    }

    public boolean checkNameJob(String nameJob){
        List<JobsEntity> jobsEntities = jobsRepository.findAll();
        return jobsEntities.stream().anyMatch(jobs -> jobs.getName().equalsIgnoreCase(nameJob));
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

    public boolean saveJob(String nameProject, String startDay, String endtDay) {
        boolean isSuccess = false;

        JobsEntity jobs = new JobsEntity();
        jobs.setName(nameProject);
        jobs.setStartDate(convertStringToDate(startDay));
        jobs.setEndDate(convertStringToDate(endtDay));

        if (!checkNameJob(nameProject)){
            try {
                jobsRepository.save(jobs);
                isSuccess = true;
            }catch (Exception e) {
                System.out.println("Lỗi Thêm dữ lệu" + e.getMessage());
            }
        }else {
            System.out.println("Email đã tồn tại");
        }
        return isSuccess;
    }

    public void deletJobById(int id) {

        jobsRepository.deleteById(id);
    }

    public boolean updatejob(String nameProject, JobsEntity jobsEntity, JobsEntity job) {
        boolean isSuccess = false;

        if (nameProject.equalsIgnoreCase(job.getName()) || !checkNameJob(nameProject)){
            try {
                jobsRepository.save(jobsEntity);
                isSuccess = true;
            }catch (Exception e) {
                System.out.println("Lỗi Thêm dữ lệu" + e.getMessage());
            }
        }else {
            System.out.println("Email đã tồn tại");
        }
        return isSuccess;

    }
}
