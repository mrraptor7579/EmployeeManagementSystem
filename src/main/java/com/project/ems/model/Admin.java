package com.project.ems.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;

@Data
@Entity
public class Admin {
    @Id
    private String empId;
    private String firstName;
    private String lastName;
    private String contact;
    private String dob;
    private String email;
    private String userRole;
    private String password;
    private String saltValue;
    @Lob
    private String empPic;
    @Lob
    private String empDetailQr;
}
