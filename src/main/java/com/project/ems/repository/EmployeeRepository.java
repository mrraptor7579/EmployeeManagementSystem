package com.project.ems.repository;

import com.project.ems.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee,String> {
    @Query("SELECT e from Employee e WHERE e.firstName LIKE CONCAT('%', :query, '%')")
    List<Employee> searchEmployeeByFirstName(String query);

    @Query("SELECT e from Employee e WHERE e.lastName LIKE CONCAT('%', :query, '%')")
    List<Employee> searchEmployeeByLastName(String query);

    @Query("SELECT e from Employee e WHERE e.department LIKE CONCAT('%', :query, '%')")
    List<Employee> searchEmployeeByDepartment(String query);

    @Query("SELECT e from Employee e WHERE e.designation LIKE CONCAT('%', :query, '%')")
    List<Employee> searchEmployeeByDesignation(String query);

    @Query("SELECT e from Employee e WHERE e.empId LIKE CONCAT('%', :query, '%')")
    List<Employee> searchEmployeeByEmpId(String query);


    @Query("SELECT e FROM Employee e WHERE e.userRole = :role")
    List<Employee> findByUserRole(@Param("role") String role);

    @Query("SELECT e From Employee e WHERE e.empId=:empId")
    Employee findByEId(String empId);

    @Query("SELECT e from Employee e where e.reportingTeamLeader=:searchType")
    List<Employee> findByCategoryTl(String searchType);

    @Query("SELECT e from Employee e where e.reportingManager=:searchType")
    List<Employee> findByCategoryManager(String searchType);

    @Query("SELECT e from Employee e where e.reportingManager=:managerId and e.reportingTeamLeader=:tlId")
    List<Employee> findByBothCategory(String tlId, String managerId);

    @Query("SELECT e.firstName from Employee e where e.empId=:empId")
    String findFirstNameById(String empId);
}
