package com.project.ems.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long leaveId;
    private String empId;
    private String leaveType;
    private String startDate;
    private String endDate;
    private String reason;
    private Long leaveDays;
    private String status;
}
