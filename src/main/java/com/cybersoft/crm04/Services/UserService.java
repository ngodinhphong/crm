package com.cybersoft.crm04.Services;

import com.cybersoft.crm04.entity.UsersEntity;
import com.cybersoft.crm04.repository.RolesRepository;
import com.cybersoft.crm04.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UsersRepository usersRepository;

    public List<UsersEntity> getUser(){
        return usersRepository.findAll();
    }

}
