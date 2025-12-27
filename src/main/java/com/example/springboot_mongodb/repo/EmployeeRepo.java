package com.example.springboot_mongodb.repo;

import com.example.springboot_mongodb.entity.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepo extends MongoRepository<Employee, String>  {
}