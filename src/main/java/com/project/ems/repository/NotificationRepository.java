package com.project.ems.repository;

import com.project.ems.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    @Query("Select n from Notification n where n.empId=:empId")
    List<Notification> findByEmpId(String empId);
}
