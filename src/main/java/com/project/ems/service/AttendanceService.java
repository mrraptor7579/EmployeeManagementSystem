package com.project.ems.service;

import com.project.ems.model.Attendance;
import com.project.ems.model.Employee;
import com.project.ems.repository.AttendanceRepository;
import com.project.ems.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Service
public class AttendanceService {

    private AttendanceRepository attendanceRepository;
    private EmployeeRepository employeeRepository;


    @Autowired
    public AttendanceService(AttendanceRepository attendanceRepository,EmployeeRepository employeeRepository) {
        this.attendanceRepository = attendanceRepository;
        this.employeeRepository=employeeRepository;

    }

    @Transactional
    public String processEmployeeAttendance(String empId) {
        String userRole=checkEmployeIsPresent(empId);
        if(userRole!=null) {
            Attendance attendance = new Attendance();
            attendance.setEmpId(empId);
            attendance.setUserRole(userRole);
            attendance.setStatus("Present");
            LocalDate currentDate = LocalDate.now();

            Month currentMonth = currentDate.getMonth();
            DayOfWeek currentWeekday = currentDate.getDayOfWeek();
            String weekdayName = currentWeekday.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            String monthName = currentMonth.getDisplayName(TextStyle.FULL, Locale.ENGLISH);

            attendance.setMonth(monthName);
            attendance.setWeekDay(weekdayName);
            System.out.println(attendance);
            attendanceRepository.save(attendance);
            return "markedPresent";
        }
        return "notPresent";
    }

    @Transactional
    protected String checkEmployeIsPresent(String empId) {
        Employee employee= employeeRepository.findByEId(empId);
        if(employee==null){

                return null;
            }
            return employee.getUserRole();

    }

    public List<Attendance> getEmployeeAttendance(String empId) {
        List<Attendance> attendance=attendanceRepository.findByEmpId(empId);
        return attendance;

    }

    public List<Attendance> findAllAttendance() {
        List<Attendance> attendances=attendanceRepository.findAll();
        return attendances;
    }

    public void updateAttendance(String empId, String month, String weekDay, String status) {
        Attendance attendance = new Attendance();
        attendance.setEmpId(empId);
        attendance.setMonth(month);
        attendance.setWeekDay(weekDay);
        attendance.setStatus(status);
        attendanceRepository.save(attendance);
    }

    public Double getAttendance(String empId) {
        List<Attendance> attendanceCount=attendanceRepository.findByEmpId(empId);
        Long totalAttendance= (long) attendanceCount.size();
        Double presentCount= 0.0;
        Double tAttendancce=(double)totalAttendance;
        for (Attendance item : attendanceCount) {
            if(item.getStatus().equals("Present")){

                presentCount=presentCount+1;
            }

        }
        if(presentCount.compareTo(tAttendancce)==0){
            return 100.00;
        }
        else{

            Double attendancePercent=(presentCount/totalAttendance)*100;
            return attendancePercent;
        }


    }
}
