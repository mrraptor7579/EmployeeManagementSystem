package com.project.ems.controller;

import com.project.ems.model.Admin;
import com.project.ems.model.Employee;
import com.project.ems.model.Leave;
import com.project.ems.model.Notification;
import com.project.ems.service.EmployeeService;
import com.project.ems.service.LeaveService;
import com.project.ems.service.NotificationService;
import jakarta.servlet.http.HttpSession;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class AdminController {
    private EmployeeService employeeService;
    private NotificationService notificationService;
    @Autowired
    HttpSession session;
    private LeaveService leaveService;
    @Autowired
    public AdminController(EmployeeService employeeService,NotificationService notificationService, LeaveService leaveService) {
        this.employeeService = employeeService;
        this.notificationService=notificationService;
        this.leaveService=leaveService;
    }



    @GetMapping("/ems/dashboard/{user}/allEmployees/{empId}")
    public  String getAllEmployees(@PathVariable("user")String user,@PathVariable("empId")String empId ,Model model) {
        String userRole = (String)session.getAttribute("user");
        if (userRole.equals("admin")) {
            List<Employee> employee = employeeService.findAllEmployees();
            model.addAttribute("employee", employee);
            model.addAttribute("roleId",empId);


            return "AllEmployees";
        }
        return "redirect:/login";
    }



    @GetMapping("/ems/addAdmin/adminForm")
    public String showAddAdminForm(Model model){
        Admin admin = new Admin();
        model.addAttribute("admin",admin);
        return "add-adminForm";
    }


    @PostMapping("/ems/addAdmin/save")
    public String saveAdmin(@ModelAttribute("admin")Admin admin, Model model){
        String empId=employeeService.saveAdmin(admin);
        model.addAttribute("empId",empId);
        model.addAttribute("userRole",admin.getUserRole());
        return "upload-adminPic";
    }

    @PostMapping("/ems/admin/uploadImage")
    public String saveAdminImage(@RequestParam("imageFile") MultipartFile imageFile,@RequestParam("empId")String empId,@RequestParam("userRole")String userRole){
        try {
            employeeService.saveAdminImage(imageFile,empId,userRole);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return"redirect:/ems/dashboard/allEmployees/"+userRole;
    }

    @GetMapping("/ems/admin/generateEmployeeIDCard/{empId}/{roleId}")
    public String generateEmployeeIDCard(@PathVariable("empId")String empId,@PathVariable("roleId")String roleId,Model model){
      String userRole= employeeService.generateIdCard(empId);

        return "redirect:/ems/dashboard/admin/allEmployees/"+roleId;
    }
@GetMapping("/ems/admin/leaveManagement/{roleId}")
    public String getLeaveDetailsAdmin(@PathVariable("roleId")String roleId, Model model){
        List<Leave> leave = employeeService.findAllLeaves();
        model.addAttribute("leave",leave);
        model.addAttribute("roleId",roleId);
        return "processLeave";
}
    @GetMapping("/ems/manager/leaveManagement/{empId}")
    public String getLeaveDetailsManager(@PathVariable("empId")String empId, Model model){
        List<Leave> leave = employeeService.findAllLeaves();
        model.addAttribute("leave",leave);
        model.addAttribute("empId",empId);
        return "processLeaveManager";
    }

@GetMapping("/ems/admin/updateLeave/{roleId}/{leaveId}/{status}")
    public String updateLeaveStatus(@PathVariable("leaveId")String leaveId,@PathVariable("status")String status, @PathVariable("roleId")String roleId, Model model){

        String response=leaveService.updateLeave(Long.valueOf(leaveId),status);
        Boolean notificationResponse=leaveService.sendNotificationLeave(Long.valueOf(leaveId));
        return "redirect:/ems/admin/leaveManagement/"+roleId;
}

@GetMapping("/ems/manager/search/{empId}")
    public String searchTeam(@PathVariable("empId")String empId, Model model){
        model.addAttribute("empId",empId);
        return"search-team";
}
    @GetMapping("/ems/teamLeader/search/{empId}")
    public String searchTeamByTeamLeader(@PathVariable("empId")String empId, Model model){
        
        model.addAttribute("empId",empId);
        return"search-team-teamLeader";
    }
@GetMapping("/ems/manager/search/team/{empId}")
    public String searchTeams(@PathVariable("empId")String empId,@RequestParam("managerId")String managerId,@RequestParam("tlId")String tlId, Model model){
        List<Employee>employees=employeeService.searchTeam(managerId,tlId);
    List<Notification> notifications=notificationService.getNotification(empId);
    model.addAttribute("notification",notifications);
        System.out.println(empId);
        model.addAttribute("employee",employees);
        model.addAttribute("empId",empId);

        return "search-team";
}
    @GetMapping("/ems/teamLeader/search/team/{empId}")
    public String searchTeamsByTeamLeader(@PathVariable("empId")String empId,@RequestParam("managerId")String managerId,@RequestParam("tlId")String tlId, Model model){
        List<Employee>employees=employeeService.searchTeam(managerId,tlId);
        List<Notification> notifications=notificationService.getNotification(empId);
        model.addAttribute("notification",notifications);
        System.out.println(empId);
        model.addAttribute("employee",employees);
        model.addAttribute("empId",empId);

        return "search-team-teamLeader";
    }
@GetMapping("/ems/calender")
public String openCalender(){
        return "Calender";
}

@GetMapping("/ems/admin/users/{roleId}")
public String getAllUsers(@PathVariable("roleId")String roleId,Model model){
    List<Admin> admins=employeeService.findAllAdmins();
    model.addAttribute("admins",admins);
    model.addAttribute("roleId",roleId);
    return "allAdmins";
}
}