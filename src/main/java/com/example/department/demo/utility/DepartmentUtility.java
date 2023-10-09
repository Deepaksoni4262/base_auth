package com.example.department.demo.utility;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;


@Getter
@Setter
@Component
@Log4j2
public class DepartmentUtility {

    public static Map<String, String> emailMap = new HashMap<>();
    public static Map<String,Map<Integer,String>> emailOtpMappingMap = new HashMap<>();

    public  static void setEmailMap() {
        emailMap.put("toEmail","");
        emailMap.put("subject","");
        emailMap.put("body","");
    }

    public  static void setEmailOtpMappingMap() {
        emailMap.put("","");
    }

    public static String validateReturnObject(String[][] variables) {
        log.trace("Executing validateReturnObject() method");
        String[][] var1 = variables;
        int var2 = variables.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            String[] variable = var1[var3];
            if (TextUtils.isEmpty(variable[1])) {
                return variable[0];
            }
        }

        log.trace("Returning from validateReturnObject() method");
        return "";
    }


    public static String OTP_FOR_VERIFICATION_IS="otp for verification";
    public static String TO_EMAIL="toEmail";
    public static String BODY="body";
    public static String SUBJECT="subject";
    public static String EMAIL="subject";
    public static String OTP="subject";
    public static String ERROR_MESSAGE="Please provide valid ";
}
