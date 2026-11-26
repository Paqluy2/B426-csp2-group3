package com.joysistvi.employeemanagement.repository;

import com.joysistvi.employeemanagement.config.DbConnection;
import com.joysistvi.employeemanagement.model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepositoryImpl implements EmployeeRepository {
    private final DbConnection dbConnection;


    private static final String SELECT_EMPLOYEES =
            "SELECT e.employee_id, e.first_name, e.middle_name, e.last_name, e.birthday, " +
                    "e.contact_number, e.email, e.hire_date, e.department_id, d.department_name, " +
                    "e.position_id, p.position_name, e.address, e.employment_status, e.leave_credits, e.is_archived " +
                    "FROM employees e " +
                    "JOIN departments d ON e.department_id = d.department_id " +
                    "JOIN positions p ON e.position_id = p.position_id ";

    public EmployeeRepositoryImpl(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public List<Employee> findAllActiveEmployees() {
        List<Employee> employees = new ArrayList<>();
        String query = SELECT_EMPLOYEES + "WHERE e.is_archived = 0 ORDER BY e.employee_id ASC";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query);
             ResultSet rs = prep.executeQuery()) {

            while (rs.next()) {
                employees.add(mapEmployee(rs));
            }
        } catch (SQLException e) {
            System.out.println("Repository Error (findAllActiveEmployees): " + DbConnection.friendlyMessage(e));
        }
        return employees;
    }

    @Override
    public List<Employee> findAllArchivedEmployees() {
        List<Employee> employees = new ArrayList<>();
        String query = SELECT_EMPLOYEES + "WHERE e.is_archived = 1 ORDER BY e.employee_id ASC";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query);
             ResultSet rs = prep.executeQuery()) {

            while (rs.next()) {
                employees.add(mapEmployee(rs));
            }
        } catch (SQLException e) {
            System.out.println("Repository Error (findAllArchivedEmployees): " + DbConnection.friendlyMessage(e));
        }
        return employees;
    }

    @Override
    public Employee findEmployeeById(int employeeId) {
        String query = SELECT_EMPLOYEES + "WHERE e.employee_id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, employeeId);

            try (ResultSet rs = prep.executeQuery()) {
                if (rs.next()) {
                    return mapEmployee(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Repository Error (findEmployeeById): " + DbConnection.friendlyMessage(e));
        }
        return null;
    }

    @Override
    public boolean addEmployee(Employee employee) {
        String query = "INSERT INTO employees (first_name, middle_name, last_name, birthday, contact_number, " +
                "email, hire_date, department_id, position_id, address, employment_status, leave_credits) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setString(1, employee.getFirstName());
            prep.setString(2, employee.getMiddleName());
            prep.setString(3, employee.getLastName());
            prep.setDate(4, java.sql.Date.valueOf(employee.getBirthday()));
            prep.setString(5, employee.getContactNumber());
            prep.setString(6, employee.getEmail());
            prep.setDate(7, java.sql.Date.valueOf(employee.getHireDate()));
            prep.setInt(8, employee.getDepartmentId());
            prep.setInt(9, employee.getPositionId());
            prep.setString(10, employee.getAddress());
            prep.setString(11, employee.getEmploymentStatus());
            prep.setInt(12, employee.getLeaveCredits());
            return prep.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Repository Error (addEmployee): " + DbConnection.friendlyMessage(e));
            return false;
        }
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        String query = "UPDATE employees SET first_name = ?, middle_name = ?, last_name = ?, birthday = ?, " +
                "contact_number = ?, email = ?, hire_date = ?, department_id = ?, position_id = ?, " +
                "address = ?, employment_status = ?, leave_credits = ? WHERE employee_id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setString(1, employee.getFirstName());
            prep.setString(2, employee.getMiddleName());
            prep.setString(3, employee.getLastName());
            prep.setDate(4, java.sql.Date.valueOf(employee.getBirthday()));
            prep.setString(5, employee.getContactNumber());
            prep.setString(6, employee.getEmail());
            prep.setDate(7, java.sql.Date.valueOf(employee.getHireDate()));
            prep.setInt(8, employee.getDepartmentId());
            prep.setInt(9, employee.getPositionId());
            prep.setString(10, employee.getAddress());
            prep.setString(11, employee.getEmploymentStatus());
            prep.setInt(12, employee.getLeaveCredits());
            prep.setInt(13, employee.getId());
            return prep.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Repository Error (updateEmployee): " + DbConnection.friendlyMessage(e));
            return false;
        }
    }

    @Override
    public boolean archiveEmployee(Employee employee) {
        String query = "UPDATE employees SET is_archived = 1 WHERE employee_id = ?";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, employee.getId());
            return prep.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Repository Error (archiveEmployee): " + DbConnection.friendlyMessage(e));
            return false;
        }
    }

    @Override
    public boolean deleteArchivedEmployee(Employee employee) {
        String query = "DELETE FROM employees WHERE employee_id = ? AND is_archived = 1";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            prep.setInt(1, employee.getId());
            return prep.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Repository Error (deleteArchivedEmployee): " + DbConnection.friendlyMessage(e));
            return false;
        }
    }

    @Override
    public List<Employee> searchEmployeesByName(String name) {
        List<Employee> employees = new ArrayList<>();
        String query = SELECT_EMPLOYEES +
                "WHERE e.is_archived = 0 AND (e.first_name LIKE ? OR e.middle_name LIKE ? " +
                "OR e.last_name LIKE ? OR CONCAT(e.first_name, ' ', e.last_name) LIKE ?) " +
                "ORDER BY e.employee_id ASC";

        try (Connection conn = dbConnection.connect();
             PreparedStatement prep = conn.prepareStatement(query)) {

            String keyword = "%" + name + "%";
            prep.setString(1, keyword);
            prep.setString(2, keyword);
            prep.setString(3, keyword);
            prep.setString(4, keyword);

            try (ResultSet rs = prep.executeQuery()) {
                while (rs.next()) {
                    employees.add(mapEmployee(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Repository Error (searchEmployeesByName): " + DbConnection.friendlyMessage(e));
        }
        return employees;
    }

    private Employee mapEmployee(ResultSet rs) throws SQLException {
        Employee employee = new Employee(
                rs.getInt("employee_id"),
                rs.getString("first_name"),
                rs.getString("middle_name"),
                rs.getString("last_name"),
                rs.getDate("birthday").toLocalDate(),
                rs.getString("contact_number"),
                rs.getString("email"),
                rs.getDate("hire_date").toLocalDate(),
                rs.getInt("department_id"),
                rs.getInt("position_id"),
                rs.getString("address"),
                rs.getString("employment_status"),
                rs.getInt("leave_credits"),
                rs.getBoolean("is_archived")
        );
        employee.setDepartmentName(rs.getString("department_name"));
        employee.setPositionName(rs.getString("position_name"));
        return employee;
    }
}