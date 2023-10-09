package com.example.department.demo.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name ="department")
public class Department {

    @Id
    private  String departmentId;
    private  String departmentName;
    private  String departmentService;
    private  String departmentLocation;
    private String employeeId;

}
