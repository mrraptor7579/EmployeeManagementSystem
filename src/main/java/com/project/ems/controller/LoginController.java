package com.project.ems.controller;

import com.project.ems.model.Admin;
import com.project.ems.model.Employee;
import com.project.ems.service.EmployeeService;
import com.project.ems.service.LoginService;


import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Enumeration;
import java.util.Objects;


@Controller
public class LoginController {
    @Autowired
    private HttpSession session;

    private LoginService loginService;
    private EmployeeService employeeService;
    @Autowired
    public LoginController(LoginService loginService,EmployeeService employeeService) {
        this.loginService = loginService;
        this.employeeService=employeeService;
    }

    @GetMapping("/ems/login")
    public String getLoginPage(){
        return "login1";
    }
    @PostMapping("/ems/loginPage")
    public String loginPage(@RequestParam("empId")String empId, @RequestParam("password")String password, Model model){
        String userRole= loginService.validateUser(empId,password);
        if(userRole==null){
            return "redirect:/ems/login?fail";
        }
        return "redirect:/ems/login/validate/"+empId+"/"+userRole;
        //return "redirect:/ems/employee/dashboard/"+userRole+"/"+empId;

    }

    @GetMapping("/ems/login/validate/{empId}/{userRole}")
    public String validateOtp(@PathVariable("empId")String empId,@PathVariable("userRole")String userRole, Model model)  {
        if(Objects.equals(userRole, "admin")) {
            Admin admin = employeeService.findAdmin(empId);
            String genV = loginService.sendMail(admin.getEmail());
            model.addAttribute("empId", empId);
            model.addAttribute("userRole", userRole);
            model.addAttribute("genV",genV);
            return "enterOTp";
        }
        else {
            Employee employee = employeeService.findEmployee(empId);

            String genV = loginService.sendMail(employee.getEmail());
            model.addAttribute("empId", empId);
            model.addAttribute("userRole", userRole);
            model.addAttribute("genV", genV);
            return "enterOTp";
        }
        }


        @PostMapping("/ems/login/validate/{userRole}/{empId}")
        public String validateOtp(@RequestParam("genV")String genV,@RequestParam("otp")String otp,@PathVariable("empId")String empId,@PathVariable("userRole")String userRole){
        if(genV.equals(otp))
        {
            if(userRole.equals("employee")){
                session.setAttribute("user",userRole);
                return"redirect:/ems/dashboard/employee/"+empId;
            }
            if(userRole.equals("admin")){
                session.setAttribute("user",userRole);
                return "redirect:/ems/dashboard/admin/"+empId;
            }
            if(userRole.equals("teamLeader")){
                session.setAttribute("user",userRole);
                return "redirect:/ems/dashboard/teamLeader/"+empId;
            }
            if(userRole.equals("manager")){
                System.out.println(userRole);
                session.setAttribute("user",userRole);
                return "redirect:/ems/dashboard/manager/"+empId;
            }
        }
            return "redirect:/ems/login/validate/"+empId+"/"+userRole+"?fail";
        }
        @GetMapping("/ems/error")
        public String errorPage(){
            return "errorPage";
        }
        @GetMapping("/ems/logout")
        public String logout(){
        session.invalidate();
        return "redirect:/ems/login";
        }

@GetMapping("/ems/login/admin")
    public String adminLogin(){
        return "login";
}
    @PostMapping("/ems/login/admin")
    public String adminLogin(@RequestParam("empId")String empId, @RequestParam("password")String password, Model model){
        String userRole= loginService.validateAdmin(empId,password);
        if(userRole==null){
            return "redirect:/ems/login/admin?fail";
        }
        return "redirect:/ems/login/validate/"+empId+"/"+userRole;
        //return "redirect:/ems/employee/dashboard/"+userRole+"/"+empId;

    }
    }



