package com.joysistvi.employeemanagement.repository;

import com.joysistvi.employeemanagement.model.Employee;

import java.util.List;

public interface EmployeeRepository {
    List<Employee> findAllActiveEmployees();
    List<Employee> findAllArchivedEmployees();
    Employee findEmployeeById(int employeeId);
    boolean addEmployee(Employee employee);
    boolean updateEmployee(Employee employee);
    boolean archiveEmployee(Employee employee);
    boolean deleteArchivedEmployee(Employee employee);
    List<Employee> searchEmployeesByName(String name);
}
