package com.joysistvi.employeemanagement.view;

import java.util.Scanner;

public class EmployeeView {

    private final Scanner scanner = new Scanner(System.in);

    public void displayEmployeeMenu() {
        System.out.println("\n===== EMPLOYEE MANAGEMENT =====");
        System.out.println("1. Add Employee");
        System.out.println("2. View All Employees");
        System.out.println("3. Search Employee");
        System.out.println("4. Update Employee");
        System.out.println("5. Archive Employee");
        System.out.println("0. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }

    public int getMenuChoice() {
        return scanner.nextInt();
    }


}
