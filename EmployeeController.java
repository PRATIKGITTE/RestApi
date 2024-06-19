package com.cdac.training.ems.controller;

import com.cdac.training.ems.exception.ResourceNotFoundException;
import com.cdac.training.ems.model.Employee;
import com.cdac.training.ems.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ems")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/employees")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee savedEmployee = employeeService.saveEmployee(employee);
        return ResponseEntity.ok(savedEmployee);
    }

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        return employee.map(ResponseEntity::ok)
                       .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeDetails) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        if (employee.isPresent()) {
            Employee updatedEmployee = employee.get();
            updatedEmployee.setFirstName(employeeDetails.getFirstName());
            updatedEmployee.setLastName(employeeDetails.getLastName());
            updatedEmployee.setEmail(employeeDetails.getEmail());
            employeeService.saveEmployee(updatedEmployee);
            return ResponseEntity.ok(updatedEmployee);
        } else {
            throw new ResourceNotFoundException("Employee not found with id: " + id);
        }
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        if (employee.isPresent()) {
            employeeService.deleteEmployee(id);
            return ResponseEntity.noContent().build();
        } else {
            throw new ResourceNotFoundException("Employee not found with id: " + id);
        }
    }
}
