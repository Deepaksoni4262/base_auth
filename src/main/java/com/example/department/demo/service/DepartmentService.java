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
import net.telematics.aws.lambda.AwsLambdaUtil;
import net.telematics.request.json.handler.RequestJsonHandler;
import net.telematics.response.json.handler.ResponseJsonHandler;
import net.telematics.response.json.handler.util.ResponseJsonUtil;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.example.department.demo.utility.DepartmentUtility.emailMap;

@Service
@Log4j2
public class DepartmentService {



    @Autowired
    private RestTemplate restTemplate;

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




    public ResponseJsonHandler getAllProduct() {
        try{

            ObjectNode node=objectMapper.createObjectNode();
            ResponseEntity<Object> responseEntity  =restTemplate.getForEntity("https://fakestoreapi.com/products", Object.class);
            return ResponseJsonUtil.getResponse(responseEntity.getBody());
        }catch (Exception e){
            log.info(e.getMessage());
        }
        return null;
    }


    public ResponseJsonHandler getToken() {
        log.info("Executing getToken() method with code={} and redirectUri={}");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "client_credentials");
        map.add("client_id", "0oa8p7l05hW68Fmk2697");
        map.add("client_secret", "iFYAUPtNePgNYgZ9W4WOMB70RgvmF0s6QEVwjM6SE2KyyfBfY-oneKaujf_0IY4D");
        map.add("scope", "authProvider");
        log.info("Processed from initializeMultiValueMap() method with {}", map);
        final HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        try {
            RestTemplate resTemplate = new RestTemplate();
            log.info("Executing endpoint {} to get token with input:{}", "https://trial-4418758.okta.com/oauth2/default/v1/token", entity);

            ResponseEntity<ObjectNode> responseEntity = resTemplate.exchange("https://trial-4418758.okta.com/oauth2/default/v1/token", HttpMethod.POST, entity, ObjectNode.class);

            ObjectNode data = objectMapper.createObjectNode();
            ObjectNode tokeNode = objectMapper.valueToTree(responseEntity.getBody());
            log.info("tokenNode :{}",tokeNode);
            data.set("token", tokeNode);
            log.info("Processing received token node");
            log.info("Result return from getToken() method :{}", ResponseJsonUtil.getResponse(data));

//            log.info("before calling getUser {}",tokeNode.get("access_token").asText());
//            ObjectNode userNode = getUser(tokeNode.get("access_token").asText());
//            log.info("userNode--------------------{}",userNode);
//            log.trace("Checking if user exists at database or not");
//            User userInfo = saveUserInfo(userNode.get("params").get( "username").asText());
//            userNode.put("id", userInfo.getUserId());
//            userNode.put("userName", userInfo.getUserName());
//            data.set("user", userNode);
            return ResponseJsonUtil.getResponse(data);
        } catch (RestClientException e) {
            log.info("Exception occurred in getting token from One Login {}", e.getMessage());
            AwsLambdaUtil.alert("Exception occurred in getting token from One Login", Arrays.toString(e.getStackTrace()), "","info");
            return ResponseJsonUtil.getResponse("");
        }



    }




//    public ObjectNode getUser(String authorization) {
//        log.info("Executing getUser() with input authorization={}", authorization);
//        ObjectNode node;
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set( "Authorization", "Bearer " + authorization);
//        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//        final HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
//        try {
//            log.info("Executing endpoint {}/me to get user info with input:{}", "https://trial-4418758.okta.com/oauth2/default/v1/token", map);
//            RestTemplate resTmp = new RestTemplate();
//            ResponseEntity<ObjectNode> responseEntity = resTmp.exchange("https://trial-4418758.okta.com/oauth2/default/v1/token/" + "me",
//                    HttpMethod.GET, entity, ObjectNode.class);
//            log.info("Response received from endpoint {}/me is:{}", "https://trial-4418758.okta.com/oauth2/default/v1/token", responseEntity.getBody());
//            node = responseEntity.getBody();
//
//
//        } catch (Exception e) {
//            log.info("Exception occurred in getting user details from One Login {}", e.getMessage());
//            node = objectMapper.createObjectNode();
//        }
//        log.trace("Output from getUser() method: {}", node);
//        return node;
//    }




//    public User saveUserInfo(String username) {
//        log.info("Executing saveUserInfo() method for username={} ", username);
//        User userInfo = userRepo.findByUserName(username);
//        if (userInfo != null) {
//            log.info("Returning user info if exists in database");
//            return userInfo;
//        } else {
//            log.info("Creating user info if not exists in database");
//            userInfo = new User();
//            userInfo.setUserName(username);
//            userInfo.setUserId(UUID.randomUUID().toString());
//            log.info("adding user to database {}", userInfo.toString());
//            userRepo.save(userInfo);
//        }
//        log.info("Returning response from saveUserInfo() :{}", userInfo);
//        return userInfo;
//    }
}
