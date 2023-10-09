package com.example.department.demo.repository;

import com.example.department.demo.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepo extends JpaRepository<Department,String> {
}
