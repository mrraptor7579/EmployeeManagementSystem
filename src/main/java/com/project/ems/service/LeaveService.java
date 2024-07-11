package com.project.ems.service;


import com.project.ems.model.Leave;
import com.project.ems.repository.LeaveRepository;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Service
public class LeaveService {

    private LeaveRepository leaveRepository;
    private NotificationService notificationService;
    @Autowired
    public LeaveService(LeaveRepository leaveRepository, NotificationService notificationService) {
        this.leaveRepository = leaveRepository;
        this.notificationService=notificationService;
    }

    public List<Leave> findHistory(String empId) {
        List<Leave> leave =  leaveRepository.findByEmpid(empId);
        return leave;
    }

    public void saveLeave(Leave leave1) {
        leave1.setLeaveDays(findLeaveDays(leave1.getStartDate(), leave1.getEndDate()));
        leaveRepository.save(leave1);
    }
    public long findLeaveDays(String start_date , String end_date) {
        // Example date strings


        // Parse date strings into LocalDate objects
        LocalDate date1 = LocalDate.parse(start_date);
        LocalDate date2 = LocalDate.parse(end_date);

        // Calculate the difference between two dates in days
        long daysDifference = ChronoUnit.DAYS.between(date1, date2);

        // Output the result
        return daysDifference;
    }

    public String updateLeave(Long leaveId, String status) {
        Leave leave=leaveRepository.findById(leaveId).get();
        leave.setStatus(status);
        leaveRepository.save(leave);
        return "success";
    }

    public Boolean sendNotificationLeave(Long leaveId) {
      Leave leave=leaveRepository.findById(leaveId).get();

      Boolean status=notificationService.sendNotificationLeave(leave.getEmpId(),leave);
      return true;
    }
}
