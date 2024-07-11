package com.project.ems.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import com.project.ems.extras.PDFBox;
import com.project.ems.model.Admin;
import com.project.ems.model.EmpId;
import com.project.ems.repository.*;
import com.project.ems.security.PassEncTech4;
import com.project.ems.model.Employee;
import com.project.ems.model.Leave;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.DelegatingServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

import static java.lang.String.valueOf;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;
    private EmpIdRepository empIdRepository;
    private AdminRepository adminRepository;
    private LeaveRepository leaveRepository;
    private AttendanceRepository attendanceRepository;



    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository,AttendanceRepository attendanceRepository,EmpIdRepository empIdRepository,LeaveRepository leaveRepository,AdminRepository adminRepository) {
        this.employeeRepository = employeeRepository;
        this.adminRepository=adminRepository;
        this.leaveRepository=leaveRepository;
        this.empIdRepository=empIdRepository;
        this.attendanceRepository=attendanceRepository;


    }

    @Transactional
    public List<Employee> findAllEmployees(){
        List<Employee> employees= employeeRepository.findAll();
        return employees;
    }
    public String saveEmployee(Employee employee) {
        String saltValue=PassEncTech4.getSaltvalue(30);
        employee.setPassword(PassEncTech4.generateSecurePassword("123",saltValue));
        employee.setSaltValue(saltValue);

        employee.setEmpId(generateEmployeeId());
        employee.setEmpDetailQr(generateEmployeeDetailQR(employee.getEmpId()));
        if(employee.getUserRole().equals("employee")){
            employee.setReportingRole("teamLeader");
        } else if (employee.getUserRole().equals("teamLeader")) {
            employee.setReportingRole("manager");

        }

        employeeRepository.save(employee);


        return employee.getEmpId();
    }

    private String generateEmployeeDetailQR(String empId) {
        try {
           FileInputStream file= generateQRCodeImage(empId, 350, 350, "./MyQRCode.png");
            String encodedImage=Base64.getEncoder().encodeToString(file.readAllBytes());
            return encodedImage;
        } catch (WriterException e) {
            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
        }


        return "null";
    }

    @Transactional
    public void saveEmployeeImage(MultipartFile imageFile, String empId, String userRole) throws Exception {


            Employee employee = employeeRepository.findById(empId).get();
            employee.setEmpPic(Base64.getEncoder().encodeToString(imageFile.getBytes()));
            employeeRepository.save(employee);
    }


    public String generateEmployeeId() {
        EmpId emp = empIdRepository.findAllid();
        Long ee = emp.getEmpId();
        Long ea =ee+1;
        EmpId newEmp=new EmpId();
        newEmp.setEmpId(ea);
        empIdRepository.save(newEmp);

        return valueOf(ea);
    }

    @Transactional
    public Employee findEmployee(String empId) {
        Employee employee = employeeRepository.findById(empId).get();
        return employee;
    }



    @Transactional
    public String generateIdCard(String empId){
        Employee employee = employeeRepository.findById(empId).get();
        PDFBox pdfBox = new PDFBox();
        pdfBox.generateIDCard(employee);
        return employee.getUserRole();
    }
    public String updateEmployee(Employee employee, String empId) {
        employee.setEmpId(empId);
        employeeRepository.save(employee);
        return employee.getUserRole();
    }
    @Transactional
    public Employee getEmployee(String empId) {
        Employee employee= employeeRepository.findById(empId).get();
        return employee;
    }
    public String updateEmployeePass(String empId, String pass, String confirmPass) {
        Employee employee=employeeRepository.findById(empId).get();
        if(pass.equals(confirmPass)){
            String saltValue=PassEncTech4.getSaltvalue(30);
            employee.setPassword(PassEncTech4.generateSecurePassword(pass,saltValue));
            employee.setSaltValue(saltValue);
            employeeRepository.save(employee);
            return "success";
        }
        return "fail";
    }
    public String updateAdminPass(String empId, String pass, String confirmPass) {
        Admin admin=adminRepository.findById(empId).get();
        if(pass.equals(confirmPass)){
            String saltValue=PassEncTech4.getSaltvalue(30);
            admin.setPassword(PassEncTech4.generateSecurePassword(pass,saltValue));
            admin.setSaltValue(saltValue);
            adminRepository.save(admin);
            return "success";
        }
        return "fail";
    }
    public void deleteEmployee(String empId) {
        employeeRepository.deleteById(empId);
        attendanceRepository.deleteByEmpId(empId);
        leaveRepository.deleteByEmpId(empId);

    }

    @Transactional
    public List<Employee> searchEmployee(String query) {

        List<Employee> employeesByFirstName = employeeRepository.searchEmployeeByFirstName(query);
        List<Employee> employeesByLastName = employeeRepository.searchEmployeeByLastName(query);
        List<Employee> employeesByDepartment = employeeRepository.searchEmployeeByDepartment(query);
        List<Employee> employeesByDesignation = employeeRepository.searchEmployeeByDesignation(query);
        List<Employee> employeesByEmpid = employeeRepository.searchEmployeeByEmpId(query);

        if (employeesByFirstName.isEmpty()) {
            if (employeesByLastName.isEmpty()) {
                if (employeesByDepartment.isEmpty()) {
                    if (employeesByDesignation.isEmpty()) {
                        if (employeesByEmpid.isEmpty()) {
                            System.out.println("not Found");
                        }
                        return employeesByEmpid;

                    }
                    return employeesByDesignation;
                }
                return employeesByDepartment;
            }
            return employeesByLastName;
        }
        return employeesByFirstName;
    }
    public FileInputStream generateQRCodeImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        File file=new File(filePath);
        FileInputStream fileInputStream=new FileInputStream(file);
        return fileInputStream;
    }


    public List<Leave> findAllLeaves() {
        List<Leave>leave=leaveRepository.findByStatus("pending");
        return leave;
    }

    @Transactional
    public Admin findAdmin(String empId) {
        Admin admin = adminRepository.findByEId(empId);
        return admin;
    }


    public String saveAdmin(Admin admin) {
        String saltValue=PassEncTech4.getSaltvalue(30);
        admin.setPassword(PassEncTech4.generateSecurePassword("123",saltValue));
        admin.setSaltValue(saltValue);

        admin.setEmpId(generateEmployeeId());
        admin.setEmpDetailQr(generateEmployeeDetailQR(admin.getEmpId()));




        adminRepository.save(admin);
        return admin.getEmpId();
    }

    @Transactional
    public void saveAdminImage(MultipartFile imageFile, String empId, String userRole) throws IOException {
        Admin admin = adminRepository.findById(empId).get();
        admin.setEmpPic(Base64.getEncoder().encodeToString(imageFile.getBytes()));
        adminRepository.save(admin);
    }

    @Transactional
    public List<Employee> searchTeam(String managerId, String tlId) {
        List<Employee>employeesTl=employeeRepository.findByCategoryTl(tlId);
        List<Employee>employeesMn=employeeRepository.findByCategoryManager(managerId);
        List<Employee>employeesBoth = employeeRepository.findByBothCategory(tlId, managerId);
        List<Employee>employees = employeeRepository.findAll();

       if(employeesBoth.isEmpty()){
           if(employeesMn.isEmpty()){
               if(employeesTl.isEmpty()){
                   return employees;
               }
               return employeesTl;

           }
           return employeesMn;
       }
return employeesBoth;
    }

    public String getEmployeeFirstName(String empId) {
        String firstName=employeeRepository.findFirstNameById(empId);
        return firstName;
    }


    @Transactional
    public List<Admin> findAllAdmins() {
        List<Admin>admins=adminRepository.findAll();
        return admins;
    }

    public Long getTotalEmployee() {
        Long totalEmployees=employeeRepository.count();
        return totalEmployees;
    }
}


