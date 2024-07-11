package com.project.ems.repository;

import com.project.ems.model.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LeaveRepository extends JpaRepository<Leave,Long> {

    @Query("SELECT l FROM Leave l WHERE l.empId=:empId")
    List<Leave> findByEmpid(String empId);

    @Query("SELECT l FROM Leave l WHERE l.status=:status")
    List<Leave> findByStatus(String status);

    @Query("delete from Leave l where l.empId=:empId")
    void deleteByEmpId(String empId);
}
