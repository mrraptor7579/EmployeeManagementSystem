package com.project.ems.controller;


import com.project.ems.extras.News;
import com.project.ems.model.Admin;
import com.project.ems.model.Employee;
import com.project.ems.model.Leave;
import com.project.ems.model.Notification;
import com.project.ems.repository.AttendanceRepository;
import com.project.ems.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

@Controller
public class EmployeeController {

    private EmployeeService employeeService;
    private NotificationService notificationService;
    private LeaveService leaveService;
    private AttendanceService attendanceService;
    @Autowired
    HttpSession session;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    @Autowired
    public EmployeeController(EmployeeService employeeService,LeaveService leaveService,NotificationService notificationService, AttendanceService attendanceService) {
        this.employeeService =  employeeService;
        this.leaveService= leaveService;
        this.notificationService=notificationService;
        this.attendanceService=attendanceService;
    }
    @GetMapping("/ems/addEmployee/employeeForm/{roleId}")
    public String showAddEmployeeForm(@PathVariable("roleId")String roleId, Model model){
        Employee employee = new Employee();
        model.addAttribute("employee",employee);
        model.addAttribute("empId",roleId);
        return "add-employeeForm";
    }
    @PostMapping("/ems/addEmployee/save/{roleId}")
    public String saveEmployee(@ModelAttribute("employee")Employee employee, @PathVariable("roleId")String roleId, Model model){
        String empId=employeeService.saveEmployee(employee);
        model.addAttribute("empId",empId);
        model.addAttribute("roleId",roleId);
        model.addAttribute("userRole",employee.getUserRole());
        return "upload-employeePic";
    }
    @PostMapping("/ems/employee/uploadImage/{roleId}")
    public String saveEmployeeImage(@RequestParam("imageFile") MultipartFile imageFile,@PathVariable("roleId")String roleId, @RequestParam("empId")String empId, @RequestParam("userRole")String userRole){
        try {
            employeeService.saveEmployeeImage(imageFile,empId,userRole);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return"redirect:/ems/dashboard/admin/allEmployees/"+roleId;
    }
    @GetMapping("/ems/editImage/{empId}/{roleId}")
    public String updateImageForm(@PathVariable("empId")String empId,@PathVariable("roleId")String roleId, Model model){
        model.addAttribute("empId",empId);
        model.addAttribute("roleId",roleId);
        return "upload-employeePic";
    }
    @GetMapping("/ems/updateEmployee/employeeForm/{empId}/{roleId}")
    public String showUpdateEmployeeForm(@PathVariable("empId")String empId,@PathVariable("roleId")String roleId ,Model model){
        Employee employee = employeeService.getEmployee(empId);
        model.addAttribute("employee",employee);
        model.addAttribute("roleId",roleId);
        return "update-employeeForm";
    }
    @PostMapping("/ems/updateEmployee/update/{empId}/{roleId}")
    public String UpdateEmployee(@ModelAttribute("employee")Employee employee,@PathVariable("roleId")String roleId,@PathVariable("empId")String empId,Model model){
        String userRole=employeeService.updateEmployee(employee,empId);
        return "redirect:/ems/dashboard/admin/allEmployees/"+roleId;
    }
    @GetMapping("/ems/deleteEmployee/{empId}/{roleId}")
    public String deleteEmployee(@PathVariable("empId")String empId,@PathVariable("roleId")String roleId, Model model){
        employeeService.deleteEmployee(empId);
        return"redirect:/ems/dashboard/admin/allEmployees/"+roleId;
    }
    @GetMapping("/ems/searchEmployees")
    public String searchEmployee(@RequestParam(value="query")String query, Model model){
        List <Employee> employee = employeeService.searchEmployee(query);
        model.addAttribute("employee",employee);
        return "allEmployees";
    }
    @GetMapping("/ems/dashboard/admin/{empId}")
    public String showAdminDashboard(@PathVariable("empId")String empId, Model model){
        Double attendance=attendanceService.getAttendance(empId);
        Long totalEmployees=employeeService.getTotalEmployee();
        String userRole = (String) session.getAttribute("user");
        if(userRole=="null") {
            return "redirect:/ems/error";
        }
        else if(Objects.equals(userRole, "admin")){
            System.out.println(userRole);
            Admin admin = employeeService.findAdmin(empId);
            model.addAttribute("roleId", empId);
            model.addAttribute("attendance",df.format(attendance));
            model.addAttribute("employee", admin);
            model.addAttribute("totalEmployees",totalEmployees);
            return "AdminDashboard";
        }
        else
            return "redirect:/ems/error";

    }
    @GetMapping("/ems/dashboard/employee/{empId}")
    public String showEmployeeDashboard(@PathVariable("empId")String empId, Model model){
        Double attendance=attendanceService.getAttendance(empId);
        List<Notification>notifications=notificationService.getNotification(empId);
        String userRole = (String) session.getAttribute("user");
        if(userRole=="null") {
            return "redirect:/ems/error";
        }
       else if(Objects.equals(userRole, "employee")){
            System.out.println(userRole);
            Employee employee = employeeService.getEmployee(empId);
            model.addAttribute("empId", empId);
            model.addAttribute("employee", employee);
            model.addAttribute("notification",notifications);
            model.addAttribute("attendance",df.format(attendance));
            return "EmployeeDashboard";
        }
       else
            return "redirect:/ems/error";

    }
    @GetMapping("/ems/dashboard/manager/{empId}")
    public String showManagerDashboard(@PathVariable("empId")String empId, Model model){
        Double attendance=attendanceService.getAttendance(empId);
        List<Notification>notifications=notificationService.getNotification(empId);
        String userRole = (String) session.getAttribute("user");
        System.out.println(userRole);
        if(userRole=="null") {

            return "redirect:/ems/error";
        }
        else if(Objects.equals(userRole, "manager")){
            System.out.println(userRole);
            Employee employee = employeeService.getEmployee(empId);
            model.addAttribute("empId", empId);
            model.addAttribute("notification",notifications);
            model.addAttribute("attendance",df.format(attendance));
            model.addAttribute("employee", employee);
            return "ManagerDashboard";
        }
        else
            return "redirect:/ems/error";

    }
    @GetMapping("/ems/dashboard/teamLeader/{empId}")
    public String showTeamLeaderDashboard(@PathVariable("empId")String empId, Model model){
        Double attendance=attendanceService.getAttendance(empId);
        List<Notification>notifications=notificationService.getNotification(empId);
        String userRole = (String) session.getAttribute("user");
        System.out.println(userRole);
        if(userRole=="null") {

            return "redirect:/ems/error";
        }
        else if(Objects.equals(userRole, "teamLeader")){
            System.out.println(userRole);
            Employee employee = employeeService.getEmployee(empId);
            model.addAttribute("empId", empId);
            model.addAttribute("notification",notifications);
            model.addAttribute("attendance",df.format(attendance));
            model.addAttribute("employee", employee);
            return "TeamLeaderDashboard";
        }
        else
            return "redirect:/ems/error";

    }
    @PostMapping("/ems/employee/updatePassword/{empId}")
    public String UpdatePassWord(@PathVariable("empId")String empId,@RequestParam("pass")String pass,@RequestParam("confirmPass")String confirmPass, Model model){
        String status=employeeService.updateEmployeePass(empId,pass,confirmPass);
        return "redirect:/ems/employee/dashboard/user/"+empId+"?"+status;
    }
    @PostMapping("/ems/admin/updatePassword/{empId}")
    public String UpdateAdminPassWord(@PathVariable("empId")String empId,@RequestParam("pass")String pass,@RequestParam("confirmPass")String confirmPass, Model model){
        String status=employeeService.updateAdminPass(empId,pass,confirmPass);
        return "redirect:/ems/dashboard/admin/"+empId+"?"+status;
    }

    @GetMapping("/ems/employee/applyLeave/{empId}")
    public String applyLeaveFormEmployee(@PathVariable("empId")String empId, Model model){
        List<Leave> leave = leaveService.findHistory(empId);
        List<Notification>notifications=notificationService.getNotification(empId);
        Leave leave1= new Leave();
        leave1.setEmpId(empId);
        model.addAttribute("leave",leave);
        model.addAttribute("empId",empId);
        model.addAttribute("notification",notifications);
        model.addAttribute("leave1",leave1);
        return "employLeaveForm";
    }
    @GetMapping("/ems/manager/applyLeave/{empId}")
    public String applyLeaveFormManager(@PathVariable("empId")String empId, Model model){
        List<Leave> leave = leaveService.findHistory(empId);
        List<Notification>notifications=notificationService.getNotification(empId);
        Leave leave1= new Leave();
        leave1.setEmpId(empId);
        model.addAttribute("leave",leave);
        model.addAttribute("notification",notifications);
        model.addAttribute("empId",empId);
        model.addAttribute("leave1",leave1);
        return "managerLeaveForm";
    }
    @GetMapping("/ems/teamLeader/applyLeave/{empId}")
    public String applyLeaveFormTeamLeader(@PathVariable("empId")String empId, Model model){
        List<Leave> leave = leaveService.findHistory(empId);
        List<Notification>notifications=notificationService.getNotification(empId);
        Leave leave1= new Leave();
        leave1.setEmpId(empId);
        model.addAttribute("leave",leave);
        model.addAttribute("empId",empId);
        model.addAttribute("notification",notifications);
        model.addAttribute("leave1",leave1);
        return "teamLeaderLeaveForm";
    }
    @PostMapping("/ems/employee/applyLeave/{empId}")
    public String applyLeave(@PathVariable("empId")String empId, @ModelAttribute("leave1")Leave leave1, Model model,
                             @RequestParam("leaveType")String leaveType,@RequestParam("startDate")String startDate,@RequestParam("endDate")String endDate,
                             @RequestParam("reason")String reason){
        leave1.setReason(reason);
        leave1.setEndDate(endDate);
        leave1.setStatus("pending");
        leave1.setEmpId(empId);
        leave1.setStartDate(startDate);
        leaveService.saveLeave(leave1);


       return "redirect:/ems/employee/applyLeave/"+empId;
    }
    @PostMapping("/ems/manager/applyLeave/{empId}")
    public String applyLeaveManager(@PathVariable("empId")String empId, @ModelAttribute("leave1")Leave leave1, Model model,
                             @RequestParam("leaveType")String leaveType,@RequestParam("startDate")String startDate,@RequestParam("endDate")String endDate,
                             @RequestParam("reason")String reason){
        leave1.setReason(reason);
        leave1.setEndDate(endDate);
        leave1.setStatus("pending");
        leave1.setEmpId(empId);
        leave1.setStartDate(startDate);
        leaveService.saveLeave(leave1);


        return "redirect:/ems/manager/applyLeave/"+empId;
    }
    @PostMapping("/ems/teamLeader/applyLeave/{empId}")
    public String applyLeaveTeamLeader(@PathVariable("empId")String empId, @ModelAttribute("leave1")Leave leave1, Model model,
                             @RequestParam("leaveType")String leaveType,@RequestParam("startDate")String startDate,@RequestParam("endDate")String endDate,
                             @RequestParam("reason")String reason){
        leave1.setReason(reason);
        leave1.setEndDate(endDate);
        leave1.setStatus("pending");
        leave1.setEmpId(empId);
        leave1.setStartDate(startDate);
        leaveService.saveLeave(leave1);


        return "redirect:/ems/teamLeader/applyLeave/"+empId;
    }
    @GetMapping("/ems/employee/getNews")
    public String getAllNews(Model model){
        News news = new News();
        List getNews = news.getNews();
        model.addAttribute("news",getNews);
        return "news";
    }
}
