package com.example.department.demo.service;

import com.example.department.demo.email.EmailService;
import com.example.department.demo.model.Department;
import com.example.department.demo.model.User;
import com.example.department.demo.repository.DepartmentRepo;
import com.example.department.demo.repository.UserRepo;
import com.example.department.demo.utility.DepartmentUtility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.mail.MessagingException;
import lombok.extern.log4j.Log4j2;
import net.telematics.request.json.handler.RequestJsonHandler;
import net.telematics.response.json.handler.ResponseJsonHandler;
import net.telematics.response.json.handler.util.ResponseJsonUtil;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.department.demo.utility.DepartmentUtility.emailMap;

@Service
@Log4j2
public class DepartmentService {
    @Autowired
    private EmailService emailService;

    @Autowired
    private DepartmentUtility departmentUtility;

    private ObjectMapper objectMapper;
    private DepartmentRepo departmentRepo;
    private UserRepo userRepo;

    @Autowired
    public void setDepartmentService(DepartmentRepo departmentRepo) {
        this.departmentRepo = departmentRepo;
    }

    @Autowired
    public void setUserRepo(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }





    public ResponseJsonHandler saveDepartment(RequestJsonHandler requestJsonHandler) {
        log.info("executing saveDepartment() method at service with input{}",requestJsonHandler);
        Department department = new Department();
        department.setDepartmentId(UUID.randomUUID().toString());
        department.setDepartmentService(requestJsonHandler.getStringValue("departmentService"));
        department.setDepartmentLocation(requestJsonHandler.getStringValue("departmentLocation"));
        department.setDepartmentName(requestJsonHandler.getStringValue("departmentName"));
        department.setEmployeeId(requestJsonHandler.getStringValue("employeeId"));
        departmentRepo.save(department);
        return ResponseJsonUtil.getResponse("department saved successfully");
    }



    public ResponseJsonHandler getAllDepartment() {
        List<Department>departmentList;
        departmentList=departmentRepo.findAll();
        return ResponseJsonUtil.getResponse(departmentList);
    }
}
