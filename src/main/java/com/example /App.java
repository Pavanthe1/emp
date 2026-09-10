package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App {

    // Employee class definition matching requirements
    public static class Employee {
        private String employeeId;
        private String name;
        private int age;
        private String department;
        private String employmentType; // "Active", "Inactive", "On Leave"
        private int securityClearanceLevel; // 1 to 5 (Higher is better)
        private boolean isIdValid;

        public Employee(String employeeId, String name, int age, String department, 
                        String employmentType, int securityClearanceLevel, boolean isIdValid) {
            this.employeeId = employeeId;
            this.name = name;
            this.age = age;
            this.department = department;
            this.employmentType = employmentType;
            this.securityClearanceLevel = securityClearanceLevel;
            this.isIdValid = isIdValid;
        }

        // Getenders and Setters
        public String getEmployeeId() { return employeeId; }
        public String getName() { return name; }
        public int getAge() { return age; }
        public String getDepartment() { return department; }
        public String getEmploymentType() { return employmentType; }
        public int getSecurityClearanceLevel() { return securityClearanceLevel; }
        public boolean isIdValid() { return isIdValid; }
    }

    // Result wrapper containing the eligibility classification and reasons
    public static class EvaluationResult {
        private String status; // Eligible, Conditionally Eligible, Not Eligible
        private List<String> reasons;

        public EvaluationResult(String status, List<String> reasons) {
            this.status = status;
            this.reasons = reasons;
        }

        public String getStatus() { return status; }
        public List<String> getReasons() { return reasons; }
    }

    private static final List<String> AUTHORIZED_DEPARTMENTS = Arrays.asList("IT", "HR", "FINANCE", "ADMINISTRATION");

    /**
     * Evaluates access eligibility based on company constraints.
     * Maps multiple error failures sequentially without short-circuiting.
     */
    public static EvaluationResult evaluateAccess(Employee emp, int requiredAccessLevel) {
        // Input Validation / Exception Handling
        if (emp == null) {
            throw new IllegalArgumentException("Employee record cannot be null");
        }
        if (emp.getEmployeeId() == null || emp.getEmployeeId().trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID cannot be empty");
        }

        List<String> rejectionReasons = new ArrayList<>();

        // Rule 1: Age verification
        if (emp.getAge() < 21) {
            rejectionReasons.add("Employee age is below 21");
        }

        // Rule 2: Department verification (Case-Insensitive)
        if (emp.getDepartment() == null || !AUTHORIZED_DEPARTMENTS.contains(emp.getDepartment().toUpperCase())) {
            rejectionReasons.add("Department is not authorized");
        }

        // Rule 3: Employment status validation
        if (emp.getEmploymentType() == null || !emp.getEmploymentType().equalsIgnoreCase("Active")) {
            rejectionReasons.add("Employment status is not active");
        }

        // Rule 4: ID validity status check
        if (!emp.isIdValid()) {
            rejectionReasons.add("Employee ID is invalid");
        }

        // Base checks status validation
        if (!rejectionReasons.isEmpty()) {
            return new EvaluationResult("Not Eligible", rejectionReasons);
        }

        // Rule 5: Security clearance mapping (Confidential Resource mapping)
        if (emp.getSecurityClearanceLevel() >= requiredAccessLevel) {
            return new EvaluationResult("Eligible", rejectionReasons);
        } else {
            // Fails only security context but passes core identity metrics
            rejectionReasons.add("Insufficient security clearance level");
            return new EvaluationResult("Conditionally Eligible", rejectionReasons);
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Employee Access Eligibility System Initialized ---");
    }
}
