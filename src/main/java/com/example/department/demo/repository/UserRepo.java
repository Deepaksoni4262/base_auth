package com.example.department.demo.repository;

import com.example.department.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,String> {


    User findByEmail(String email);

    User findByUserName(String username);
}
