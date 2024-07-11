package com.project.ems.controller;

import com.project.ems.model.Attendance;
import com.project.ems.model.Notification;
import com.project.ems.service.AttendanceService;
import com.project.ems.service.EmployeeService;
import com.project.ems.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AttendanceController {

    private AttendanceService attendanceService;
    private NotificationService notificationService;
    private EmployeeService employeeService;
    @Autowired
    public AttendanceController(AttendanceService attendanceService,NotificationService notificationService,EmployeeService employeeService) {
        this.attendanceService = attendanceService;
        this.employeeService=employeeService;
        this.notificationService=notificationService;
    }

    @GetMapping("/ems/employee/attendance")
    public String openAttendance(Model model){
        return "employeeAttendance";
    }
    @PostMapping("/ems/employee/attendance")
    public String getEmployeeAttendance(@RequestParam("empId")String empId){
        return "redirect:/ems/employee/attendance/"+empId;
    }
    @GetMapping("/ems/employee/attendance/{empId}")
    public String processAttendance(@PathVariable("empId")String empId, Model  model){
        String status=attendanceService.processEmployeeAttendance(empId);
        return "redirect:/ems/employee/attendance?"+status;
    }
    @GetMapping("/ems/employee/checkAttendance/{empId}")
    public String  checkAttendance(@PathVariable("empId")String empId,Model model){
        List<Attendance> attendance = attendanceService.getEmployeeAttendance(empId);
        List<Notification> notifications=notificationService.getNotification(empId);
        model.addAttribute("notification",notifications);
        model.addAttribute("empId",empId);
        model.addAttribute("attendance",attendance);
        return "attendanceEmployee";

    }
    @GetMapping("/ems/teamLeader/checkAttendance/{empId}")
    public String  checkAttendanceTeamLeader(@PathVariable("empId")String empId,Model model) {
        List<Attendance> attendance = attendanceService.getEmployeeAttendance(empId);
        List<Notification> notifications=notificationService.getNotification(empId);
        model.addAttribute("notification",notifications);
        model.addAttribute("empId", empId);
        model.addAttribute("attendance", attendance);
        return "attendanceTeamLeader";
    }
    @GetMapping("/ems/manager/checkAttendance/{empId}")
    public String  checkAttendanceManager(@PathVariable("empId")String empId,Model model) {
        List<Attendance> attendance = attendanceService.getEmployeeAttendance(empId);
        List<Notification> notifications=notificationService.getNotification(empId);
        model.addAttribute("notification",notifications);
        model.addAttribute("empId", empId);
        model.addAttribute("attendance", attendance);
        return "attendanceManager";
    }
    @GetMapping("/ems/admin/manageAttendance/{empId}")
    public String getAllAttendance(@PathVariable("empId")String empId ,Model model){
        List<Attendance> attendances = attendanceService.findAllAttendance();
        List<Notification> notifications=notificationService.getNotification(empId);
        model.addAttribute("notification",notifications);
        model.addAttribute("attendance",attendances);
        model.addAttribute("roleId",empId);
        return "manageAttendence";
    }
    @GetMapping("/ems/manager/manageAttendance/{empId}")
    public String getTeamsAttendenceTeamLeader(@PathVariable("empId")String empId ,Model model){
        String firstName=employeeService.getEmployeeFirstName(empId);
        List<Attendance> attendances = attendanceService.findAllAttendance();
        model.addAttribute("attendance",attendances);
        model.addAttribute("empId",empId);
        return "manageAttendenceManager";
    }
    @GetMapping("/ems/TeamLeader/manageAttendance/{empId}")
    public String getTeamsAttendence(@PathVariable("empId")String empId ,Model model){
        String firstName=employeeService.getEmployeeFirstName(empId);
        List<Attendance> attendances = attendanceService.findAllAttendance();
        model.addAttribute("attendance",attendances);
        model.addAttribute("empId",empId);
        return "manageAttendenceTeamLeader";
    }
    @PostMapping("/ems/admin/updateAttendence/{empId}/{month}/{weekDay}")
    public String updateAttendancc(@PathVariable("empId")String empId, @PathVariable("month")String month
                                    ,@PathVariable("weekDay")String weekDay, Model model,@RequestParam("status")String status){
        System.out.println(status);
        attendanceService.updateAttendance(empId,month,weekDay,status);

        return "redirect:/ems/admin/manageAttendance/{empId}";
    }
}
