package com.cybersoft.crm04.repository;

import com.cybersoft.crm04.entity.StatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepositiory extends JpaRepository<StatusEntity, Integer> {
}
