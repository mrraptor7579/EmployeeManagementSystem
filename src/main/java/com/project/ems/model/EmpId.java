package com.project.ems.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class EmpId {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long empId;
}
