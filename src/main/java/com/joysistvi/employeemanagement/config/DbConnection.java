package com.joysistvi.employeemanagement.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {

    private final static String URL = "jdbc:mysql://localhost:3306/employee_management_db";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "";

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}






