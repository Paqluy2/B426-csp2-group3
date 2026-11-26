package com.joysistvi.employeemanagement.service;

import com.joysistvi.employeemanagement.model.Employee;

import java.util.List;

public interface EmployeeService {

    // Add a new employee
    void addEmployee(Employee employee);

    // Display all employees
    List<Employee> getAllEmployees();

    // Search employee by ID
    Employee getEmployeeById(int employeeId);

    // Update employee information
    void updateEmployee(Employee employee);

    // Archive an employee
    void archiveEmployee(int employeeId);
}
