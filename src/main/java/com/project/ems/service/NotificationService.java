package com.project.ems.service;

import com.project.ems.model.Employee;
import com.project.ems.model.Leave;
import com.project.ems.model.Notification;
import com.project.ems.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    private JavaMailSender javaMailSender;
    private NotificationRepository notificationRepository;
    private EmployeeService employeeService;


    @Autowired
   public NotificationService(NotificationRepository notificationRepository,EmployeeService employeeService,JavaMailSender javaMailSender){
        this.notificationRepository=notificationRepository;
        this.employeeService=employeeService;
        this.javaMailSender=javaMailSender;
    }


    public boolean sendNotificationLeave(String empId, Leave leave){
        Employee employee=employeeService.getEmployee(empId);
        String text="Dear "+employee.getFirstName()+"\n"
                + "I hope this message finds you well. We would like to inform you that your leave request has been "+leave.getStatus()+". Your time off has been scheduled according to the submitted dates.\n" +
                "\n" +
                "Leave Details:\n" +
                "\n" +
                "Start Date: "+leave.getStartDate() +"\n" +
                "End Date: "+leave.getEndDate()+"\n" +
                "Number of Days: "+leave.getLeaveDays()+"\n" +
                "If you have any further questions or concerns, please feel free to reach out to [Mr Raptor] at [email@gmail.com].\n" +
                "\n" +
                "Thank you.\n" +
                "\n" +
                "Best regards,\n" +
                "Crust To Core Technologies(CTC)";
        sendNotification(employee,leave,text);
        return true;
    }
    public Boolean sendNotification(Employee employee, Leave leave,String text) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("YourEmail@gmail.com");
        simpleMailMessage.setSubject("CTC Notification");

        simpleMailMessage.setText(text);
        simpleMailMessage.setTo(employee.getEmail());
        javaMailSender.send(simpleMailMessage);
        saveNotification(employee.getEmpId(),text);

        return true;
    }

    private void saveNotification(String empId, String text) {
        Notification notification=new Notification();
        notification.setNotificationText(text);
        notification.setEmpId(empId);
        notification.setIsRead("unread");
        notificationRepository.save(notification);
    }

    public List<Notification> getNotification(String empId) {
        List<Notification> notifications=notificationRepository.findByEmpId(empId);
        return notifications;
    }
}
