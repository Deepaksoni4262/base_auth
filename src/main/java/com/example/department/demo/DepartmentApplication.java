package com.example.department.demo;

import com.example.department.demo.email.EmailService;
import com.example.department.demo.utility.DepartmentUtility;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;

import static com.example.department.demo.utility.DepartmentUtility.setEmailMap;
import static com.example.department.demo.utility.DepartmentUtility.setEmailOtpMappingMap;


@SpringBootApplication
@ComponentScan(value = {
		"com.example.department.demo.controller",
		"com.example.department.demo.model",
		"com.example.department.demo.repository",
		"com.example.department.demo.service",
		"com.example.department.demo.email",
		"com.example.department.demo.utility"
})
public class DepartmentApplication {

	@Autowired
	private EmailService emailService;

	public static void main(String[] args) {
		setEmailMap();
		setEmailOtpMappingMap();
		SpringApplication.run(DepartmentApplication.class, args);

	}


}
