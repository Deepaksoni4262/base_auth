package com.example.department.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name ="projectUser")
public class User {

    @Id
    private  String userId;
    private  String userName;
    private  String password;
    private  String email;

}