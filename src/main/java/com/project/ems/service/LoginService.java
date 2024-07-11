package com.project.ems.service;

import com.project.ems.model.Admin;
import com.project.ems.repository.AdminRepository;
import com.project.ems.security.PassEncTech4;
import com.project.ems.model.Employee;
import com.project.ems.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class LoginService {

    private EmployeeRepository employeeRepository;
    private JavaMailSender javaMailSender;
    private AdminRepository adminRepository;

    @Autowired
    public LoginService(EmployeeRepository employeeRepository,JavaMailSender javaMailSender,AdminRepository adminRepository) {
        this.employeeRepository = employeeRepository;
        this.adminRepository=adminRepository;
        this.javaMailSender=javaMailSender;
    }

    @Transactional
    public String validateUser(String empId, String password) {
        Employee employee = employeeRepository.findByEId(empId);


        if (employee != null) {
            if (PassEncTech4.verifyUserPassword(password,employee.getPassword(),employee.getSaltValue())) {

                return employee.getUserRole();
            }
            return null;

        }
        return null;
    }


    public String sendMail(String mail) {
        System.out.println("start");
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("akashbaghel650@gmail.com");
        simpleMailMessage.setSubject("Login OTP");
        String otp=generateOTP();
        simpleMailMessage.setText("Your Otp for Login: "+otp);
        simpleMailMessage.setTo(mail);
        javaMailSender.send(simpleMailMessage);
        System.out.println("true");
        return otp;
    }
    public static String generateOTP()
    {
        Random random=new Random();

        StringBuilder sb=new StringBuilder();

        for (int i=0;i<6;i++)
        {
            sb.append(random.nextInt(10));
        }
        String otp=sb.toString();

        return otp;
    }

@Transactional
    public String validateAdmin(String empId, String password) {
        System.out.println("in");
        Admin admin = adminRepository.findByEId(empId);
        System.out.println(admin.toString());

        if (admin != null) {
            if (PassEncTech4.verifyUserPassword(password,admin.getPassword(),admin.getSaltValue())) {
                System.out.println("in");
                return admin.getUserRole();

            }
            return null;

        }
        System.out.println("null");
        return null;
    }
}
