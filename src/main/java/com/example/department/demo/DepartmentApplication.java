package com.example.department.demo;

import com.example.department.demo.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

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

    @Bean
    public RestTemplate setRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.exceptionHandling().authenticationEntryPoint((swe, e) ->
                        Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)))
                .accessDeniedHandler((swe, e) -> Mono.fromRunnable(() -> swe.getResponse()
                        .setStatusCode(HttpStatus.FORBIDDEN))).and().csrf().disable().formLogin().disable()
                .httpBasic().disable().authorizeExchange()
                .pathMatchers("/department/**")
                .permitAll().anyExchange().authenticated().and().oauth2ResourceServer().jwt();

        return http.build();


    }
}
