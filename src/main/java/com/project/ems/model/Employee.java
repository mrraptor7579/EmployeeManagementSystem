package com.project.ems.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Employee {

    @Id
    private String empId;
    private String firstName;
    private String lastName;
    private String contact;
    private String dob;
    private String email;
    private String userRole;
    private String department;
    private String designation;
    private String password;
    private String reportingRole;
    private String reportingTeamLeader;
    private String reportingManager;
    private String saltValue;
    @Lob
    private String empPic;
    @Lob
    private String empDetailQr;


}
