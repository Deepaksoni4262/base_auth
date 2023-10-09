package com.example.department.demo.controller;


import com.example.department.demo.service.UserService;
import lombok.extern.log4j.Log4j2;
import net.telematics.request.json.handler.RequestJsonHandler;
import net.telematics.response.json.handler.ResponseJsonHandler;
import net.telematics.response.json.handler.util.ResponseJsonUtil;
import net.telematics.validations.Validations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static com.example.department.demo.utility.DepartmentUtility.*;

@RestController
@Log4j2
@RequestMapping("/user")
public class UserController {
    private UserService userService;


    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/sign-up")
    public ResponseJsonHandler saveUser(@RequestBody RequestJsonHandler requestJsonHandler) {
        String email= requestJsonHandler.getStringValue("email");
        String password= requestJsonHandler.getStringValue("password");
        String paramStatus = Validations.validateReturnObject(new String[][]{{EMAIL, email},{"password",password}});
        if (!(paramStatus.isEmpty())) {
            log.warn("Validation error at verifyUser() method={}", ResponseJsonUtil.getResponse(ERROR_MESSAGE + paramStatus));
            return ResponseJsonUtil.getResponse(ERROR_MESSAGE + paramStatus);
        }
        log.info("executing saveUser() Method with input{}", requestJsonHandler);
        return userService.saveUser(requestJsonHandler);
    }


    @PostMapping("/login")
    public ResponseJsonHandler login(@RequestBody RequestJsonHandler requestJsonHandler) {
        String email= requestJsonHandler.getStringValue("email");
        String password= requestJsonHandler.getStringValue("password");
        String paramStatus = Validations.validateReturnObject(new String[][]{{EMAIL, email},{"password",password}});
        if (!(paramStatus.isEmpty())) {
            log.warn("Validation error at login() method={}", ResponseJsonUtil.getResponse(ERROR_MESSAGE + paramStatus));
            return ResponseJsonUtil.getResponse(ERROR_MESSAGE + paramStatus);
        }
        log.info("executing login() Method with input{}", requestJsonHandler);
        return userService.login(requestJsonHandler);
    }


    @GetMapping("/get-all-user")
    public ResponseJsonHandler getAllUser() {
        log.info("executing getAllUser() Method ");
        return userService.getAllUser();
    }

    @PostMapping("/forget-password")
    public ResponseJsonHandler forgetPassword(@RequestBody RequestJsonHandler requestJsonHandler) {

        String email= requestJsonHandler.getStringValue("email");
        String paramStatus = Validations.validateReturnObject(new String[][]{{EMAIL, email}});
        if (!(paramStatus.isEmpty())) {
            log.warn("Validation error at forgetPassword() method={}", ResponseJsonUtil.getResponse(ERROR_MESSAGE + paramStatus));
            return ResponseJsonUtil.getResponse(ERROR_MESSAGE + paramStatus);
        }
        log.info("executing forgetPassword() Method with input{}",requestJsonHandler);
        return userService.forgetPassword(requestJsonHandler);
    }


    @PostMapping("/verify-user")
    public ResponseJsonHandler verifyUser(@RequestBody RequestJsonHandler requestJsonHandler) {
        log.info("executing verifyUser() Method with input{}",requestJsonHandler);
        String email= requestJsonHandler.getStringValue("email");
        String otp= requestJsonHandler.getStringValue("otp");
        String newPassword= requestJsonHandler.getStringValue("newPassword");

        String paramStatus = Validations.validateReturnObject(new String[][]{{EMAIL, email},{OTP,otp},{"newPassword",newPassword}});
        if (!(paramStatus.isEmpty())) {
            log.warn("Validation error at verifyUser() method={}", ResponseJsonUtil.getResponse(ERROR_MESSAGE + paramStatus));
            return ResponseJsonUtil.getResponse(ERROR_MESSAGE + paramStatus);
        }

        return userService.verifyPasswordForgettedUser(email, Integer.parseInt(otp),newPassword);
    }

}
