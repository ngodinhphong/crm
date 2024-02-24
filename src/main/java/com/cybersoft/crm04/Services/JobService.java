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

    public Date convertStringToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Hoặc có thể ném ngoại lệ tùy thuộc vào yêu cầu của bạn
        }
    }

    public boolean checkNameJob(String nameJob){
        List<JobsEntity> jobsEntities = jobsRepository.findAll();
        return jobsEntities.stream().anyMatch(jobs -> jobs.getName().equalsIgnoreCase(nameJob)) ;
    }

    public boolean checkDate(String startDate, String endDate){
        return convertStringToDate(startDate).before(convertStringToDate(endDate));
    }

    private boolean checkForNull(String nameProject, String startDate, String endDate){
        return nameProject != null && !nameProject.isEmpty()
                && startDate != null && !startDate.isEmpty()
                && endDate != null && !endDate.isEmpty();
    }

    public String notificationSave(String nameProject, String startDate, String endDate){
        if(nameProject == null || nameProject.isEmpty()){
            return "Vui lòng nhập tên dự án!";
        } else if (startDate == null || startDate.isEmpty()) {
            return "Vui lòng nhập ngày bắt đầu!";
        }else if (endDate == null || endDate.isEmpty()) {
            return "Vui lòng nhập ngày kết thúc!";
        }else if (checkNameJob(nameProject)) {
            return "Tên dự án đã tồn tại!";
        }else if (!checkDate(startDate, endDate)) {
            return "Ngày bắt đầu phải nhỏ hơn ngày kết thúc!";
        } else {
            return "";
        }
    }

    public boolean saveJob(String nameProject, String startDate, String endDate) {
        boolean isSuccess = false;

        JobsEntity jobs = new JobsEntity();
        jobs.setName(nameProject);
        jobs.setStartDate(convertStringToDate(startDate));
        jobs.setEndDate(convertStringToDate(endDate));

        if (checkForNull(nameProject, startDate, endDate) && !checkNameJob(nameProject) && checkDate(startDate, endDate)){
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

    public String notificationUpdate(String nameProject, String startDate, String endDate, JobsEntity job){
        if(nameProject == null || nameProject.isEmpty()){
            return "Vui lòng nhập tên dự án!";
        } else if (startDate == null || startDate.isEmpty()) {
            return "Vui lòng nhập ngày bắt đầu!";
        } else if (endDate == null || endDate.isEmpty()) {
            return "Vui lòng nhập ngày kết thúc!";
        }else if (!checkDate(startDate, endDate)) {
            return "Ngày bắt đầu phải nhỏ hơn ngày kết thúc!";
        }else if (nameProject.equalsIgnoreCase(job.getName())) {
            return "";
        } else if (checkNameJob(nameProject)) {
            return "Tên dự án đã tồn tại!";
        } else {
            return "";
        }
    }

    public boolean updatejob(String nameProject, String startDay, String endDay, JobsEntity jobsEntity, JobsEntity job) {
        boolean isSuccess = false;

        if (checkForNull(nameProject, startDay, endDay) && (nameProject.equalsIgnoreCase(job.getName()) || !checkNameJob(nameProject)) && checkDate(startDay, endDay)){
            try {
                jobsRepository.save(jobsEntity);
                isSuccess = true;
            }catch (Exception e) {
                System.out.println("Lỗi Thêm dữ lệu" + e.getMessage());
            }
        }
        return isSuccess;

    }
}
