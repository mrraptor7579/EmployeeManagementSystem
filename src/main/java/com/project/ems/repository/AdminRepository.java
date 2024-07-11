package com.project.ems.repository;

import com.project.ems.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdminRepository extends JpaRepository<Admin,String> {
    @Query("SELECT a From Admin a WHERE a.empId=:empId")
    Admin findByEId(String empId);
}
