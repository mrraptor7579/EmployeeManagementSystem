package com.project.ems.repository;


import com.project.ems.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
    @Query("SELECT a FROM Attendance a WHERE a.empId=:empId")
    List<Attendance> findByEmpId(String empId);

@Query("delete from Attendance a  where a.empId=:empId")
    void deleteByEmpId(String empId);
}
