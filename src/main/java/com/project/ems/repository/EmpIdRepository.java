package com.project.ems.repository;

import com.project.ems.model.EmpId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmpIdRepository extends JpaRepository<EmpId,Long> {
    @Query("select e from EmpId e order by e.empId desc limit 1")
    EmpId findAllid();
}
