package com.example.springboot_mongodb.controller;

import com.example.springboot_mongodb.entity.Employee;
import com.example.springboot_mongodb.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<Employee> create(@RequestBody Employee employee){
        return ResponseEntity.ok(employeeService.createEmployee(employee));
    }

    @GetMapping
    public ResponseEntity<List<Employee>>getAll(){
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    public Optional<ResponseEntity<Employee>> getById(@PathVariable String id){
        return employeeService.getEmployeeById(id)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping({"/id"})
    public ResponseEntity<Void> delete(@PathVariable String id){
        employeeService.deleteEmployee(id);

        return ResponseEntity.noContent().build();
    }
}