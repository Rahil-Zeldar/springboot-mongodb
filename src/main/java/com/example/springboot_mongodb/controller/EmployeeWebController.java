package com.example.springboot_mongodb.controller;

import com.example.springboot_mongodb.entity.Employee;
import com.example.springboot_mongodb.entity.Projects;
import com.example.springboot_mongodb.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/employee")
public class EmployeeWebController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public String listEmployee(Model model) {
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "employee";
    }

    public String showCreateForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("projectNameString", "");
        model.addAttribute("projectAllocationsString", "");

        return "employee-form";
    }

    @PostMapping
    public String createEmployee(@ModelAttribute Employee employee,
                                 @RequestParam(value = "projectNames", required = false) String projectNames,
                                 @RequestParam(value = "projectAllocations", required = false) String projectAllocations) {

        populateProjectsFormInputs(employee, projectNames, projectAllocations);
        employeeService.createEmployee(employee);

        return "redirect:/employees";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        Optional<Employee> optionalEmployee = employeeService.getEmployeeById(id);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            model.addAttribute("employee", employee);
            model.addAttribute("projectNamesString", joinProjectNames(employee.getProjects()));
            model.addAttribute("projectAllocationString", joinProjectAllocations(employee.getProjects()));

            return "employee-form";
        } else {
            return "redirect:/employees";
        }
    }

    @PostMapping("/update/{id}")
    public String updateEmployee(@PathVariable String id,
                                 @ModelAttribute Employee employee,
                                 @RequestParam(value = "projectNames", required = false) String projectNames,
                                 @RequestParam(value = "projectAllocations", required = false) String projectAllocations) {

        populateProjectsFormInputs(employee, projectNames, projectAllocations);
        employeeService.updateEmployee(id,employee);

        return "redirect:/employees";
    }

    @PostMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable String id){
        employeeService.deleteEmployee(id);

        return "redirect:/employees";
    }



    private void populateProjectsFormInputs(Employee employee, String projectNames, String projectAllocations) {
        if (projectNames == null || projectNames.trim().isEmpty()) {
            employee.setProjects(new ArrayList<>());
            return;
        }

        List<String> names = List.of(projectNames.split("\\s*, \\s*"));
        List<String> alloc = (projectAllocations == null || projectAllocations.trim().isEmpty())
                ? new ArrayList<>()
                : List.of(projectAllocations.split("\\s*, \\s*"));

        List<Projects> projects = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            Projects project = new Projects();
            project.setName(names.get(i));
            if (i < alloc.size()) {
                try {
                    project.setAllocation(Integer.parseInt(alloc.get(i)));
                } catch (NumberFormatException e) {
                    project.setAllocation(0);
                }
            } else {
                project.setAllocation(0);
            }

            projects.add(project);
        }

        employee.setProjects(projects);

    }

    private String joinProjectNames(List<Projects> projects) {
        if(projects == null || projects.isEmpty()) return "";

        return projects.stream()
                .map(Projects::getName)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(", "));
    }

    private String joinProjectAllocations(List<Projects> projects) {
        if(projects == null || projects.isEmpty()) return "";

        return projects.stream()
                .map(p -> String.valueOf(p.getAllocation()))
                .collect(Collectors.joining(", "));
    }

}