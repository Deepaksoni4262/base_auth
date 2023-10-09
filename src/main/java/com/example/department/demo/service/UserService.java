package com.example.department.demo.service;


import com.example.department.demo.email.EmailService;
import com.example.department.demo.model.User;
import com.example.department.demo.repository.UserRepo;
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

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static com.example.department.demo.utility.DepartmentUtility.*;

@Service
@Log4j2
public class UserService {


    private UserRepo userRepo;
    private ObjectMapper objectMapper;

    @Autowired
    public void setUserRepo(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    private EmailService emailService;

    public ResponseJsonHandler getAllUser() {
        return ResponseJsonUtil.getResponse(userRepo.findAll());
    }



    @EventListener(ApplicationReadyEvent.class)
    public void triggerMail() throws MessagingException {
        String toEmail="";
        String subject="";
        String body="";
        if (!TextUtils.isEmpty(emailMap.get("toEmail")) &&!TextUtils.isEmpty(emailMap.get("subject")) && !TextUtils.isEmpty(emailMap.get("body")) ){
            toEmail= emailMap.get("toEmail");
            subject= emailMap.get("subject");
            body= emailMap.get("body");
            emailService.sendSimpleEmail(toEmail, subject, body);
        }

    }

    public ResponseJsonHandler saveUser(RequestJsonHandler requestJsonHandler) {
        log.info("executing saveUser() method at service with input{}", requestJsonHandler);
        try {
            User user = new User();
            user.setUserId(UUID.randomUUID().toString());
            user.setUserName(requestJsonHandler.getStringValue("userName"));
            user.setPassword(requestJsonHandler.getStringValue("password"));
            user.setEmail(requestJsonHandler.getStringValue("email"));
            userRepo.save(user);
            log.info("user saved into db successfully{}",user);
            return ResponseJsonUtil.getResponse("user saved successfully");
        } catch (Exception e) {
            log.error("exception occurred at saveUser() method {}", e.getMessage());
        }
        return ResponseJsonUtil.getResponse("unable to save user ");
    }


    public ResponseJsonHandler login(RequestJsonHandler requestJsonHandler) {
        log.info("executing login() method at service with input{}", requestJsonHandler);
        try {
            ObjectNode node = objectMapper.createObjectNode();
            User user = null;
            String email = requestJsonHandler.getStringValue("email");
            user = userRepo.findByEmail(email);
            if (user != null) {
                log.info("user existed {}", user);
                String password = requestJsonHandler.getStringValue("password");
                if (user.getPassword().equalsIgnoreCase(password)) {
                    log.info("user's password matched {}", user);
                    node.put("Welcome ", user.getUserName());
                    return ResponseJsonUtil.getResponse(node);
                } else {
                    return ResponseJsonUtil.getResponse("password dosen't match ! please try again");
                }
            }
            return ResponseJsonUtil.getResponse("user not exist");
        } catch (Exception e) {
            log.error("exception occurred at login() method {}", e.getMessage());
        }
        return ResponseJsonUtil.getResponse(" ");
    }


    public ResponseJsonHandler forgetPassword(RequestJsonHandler requestJsonHandler) {
        log.info("executing forgetPassword() method at service with input{}", requestJsonHandler);
        String email= requestJsonHandler.getStringValue("email");
        User user=null;
        user=userRepo.findByEmail(email);
        if (user!=null){
            log.info("user found with associted mail {}",user);
            Random random=new Random();
            int otp= random.nextInt(100000);
            emailMap.put(TO_EMAIL,email);
            emailMap.put(SUBJECT,OTP_FOR_VERIFICATION_IS);
            emailMap.put(BODY, String.valueOf(otp));
            try {
                triggerMail();
                setEmailMapData(otp,email, Instant.now().toString());
                log.info("mail shared to respective mail{}",email);
                return ResponseJsonUtil.getResponse("opt sent to your respective mail");

            } catch (MessagingException e) {
                log.info("exception occurred at forgetPassword() method {}",e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return ResponseJsonUtil.getResponse("unable to send otp");
    }

    private void setEmailMapData(int otp, String email, String time) {
        log.info("executing setEmailMapData() method with otp{} mail{} and time{}",otp,email,time);
        Map<Integer, String> innerData = new HashMap<>();
        innerData.put(otp,time );
        emailOtpMappingMap.put(email,innerData);
        log.info("data inserted into emailOtpMappingMap map {}",emailOtpMappingMap);
    }


    public ResponseJsonHandler verifyPasswordForgettedUser(String email , int otp , String newPassword){
        log.info("executing verifyPasswordForgettedUser() method with otp {} mail {} and newPassword {}",otp,email,newPassword);
       try{
           Map<Integer, String> innerData = emailOtpMappingMap.get(email);
           if (innerData!=null){
               String before=innerData.get(otp);
               Instant beforeTime=Instant.parse(before);
               Instant now=Instant.now();
               beforeTime=beforeTime.plus(59, ChronoUnit.SECONDS);
               log.info("current time and before times are {} {}",now,beforeTime);
               if (now.isBefore(beforeTime)){
                   User user = userRepo.findByEmail(email);
                   updatePassword(user,newPassword);
                   return ResponseJsonUtil.getResponse("user authenticated successfully and password is updated");
               }else {
                   return ResponseJsonUtil.getResponse("timeout please try again");
               }
           }
       }catch (Exception e){
           log.error("exception ocurred at verifyPasswordForgettedUser() method {}",e.getMessage());
       }

        return ResponseJsonUtil.getResponse("");
    }

    private void updatePassword(User user , String newPassword) {
        log.info("executing updatePassword() method with user{} and password {}",user,newPassword);
        User user1=new User();
        user1.setUserId(user.getUserId());
        user1.setUserName(user.getUserName());
        user1.setPassword(newPassword);
        user1.setEmail(user.getEmail());
        userRepo.save(user1);
        log.info("user saved into db {}",user);
    }
}
