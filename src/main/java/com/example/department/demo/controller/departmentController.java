package com.example.department.demo.controller;

import com.example.department.demo.service.DepartmentService;
import com.example.department.demo.service.UserService;
import lombok.extern.log4j.Log4j2;
import net.telematics.request.json.handler.RequestJsonHandler;
import net.telematics.response.json.handler.ResponseJsonHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/department")
public class departmentController {

    private DepartmentService departmentService;
    private UserService userService;


    @Autowired
    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }


    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/save-department")
    public ResponseJsonHandler saveDepartment(@RequestBody RequestJsonHandler requestJsonHandler) {
        log.info("executing savedepartment() Method with input{}", requestJsonHandler);
        return departmentService.saveDepartment(requestJsonHandler);
    }


    @GetMapping("/get-all-department")
    public ResponseJsonHandler getAllDepartment() {
        log.info("executing getAllDepartment() Method ");
        return departmentService.getAllDepartment();
    }


    @GetMapping("/get-token")
    public ResponseJsonHandler getToken() {
        log.info("executing getToken() Method ");
        return departmentService.getToken();
    }


}
