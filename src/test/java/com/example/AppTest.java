package com.example;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;
import com.example.App.Employee;
import com.example.App.EvaluationResult;

public class AppTest {

    // --- POSITIVE & NORMAL SCENARIOS ---

    @Test
    public void testPerfectEligibleEmployee() {
        Employee emp = new Employee("EMP001", "John Doe", 30, "IT", "Active", 4, true);
        EvaluationResult res = App.evaluateAccess(emp, 3);
        
        assertEquals("Eligible", res.getStatus());
        assertTrue(res.getReasons().isEmpty());
    }

    @Test
    public void testConditionallyEligibleEmployee() {
        // Passes core metrics, but security clearance level (2) is less than required (4)
        Employee emp = new Employee("EMP002", "Jane Smith", 28, "HR", "Active", 2, true);
        EvaluationResult res = App.evaluateAccess(emp, 4);
        
        assertEquals("Conditionally Eligible", res.getStatus());
        assertEquals(1, res.getReasons().size());
        assertEquals("Insufficient security clearance level", res.getReasons().get(0));
    }

    // --- BOUNDARY SCENARIOS ---

    @Test
    public void testAgeBoundaryConditionExact21() {
        // Exactly 21 years old should pass
        Employee emp = new Employee("EMP003", "Alice Bond", 21, "Finance", "Active", 5, true);
        EvaluationResult res = App.evaluateAccess(emp, 5);
        
        assertEquals("Eligible", res.getStatus());
    }

    @Test
    public void testAgeBoundaryConditionUnder21() {
        // 20 years old should fail
        Employee emp = new Employee("EMP004", "Bob Miller", 20, "Administration", "Active", 3, true);
        EvaluationResult res = App.evaluateAccess(emp, 3);
        
        assertEquals("Not Eligible", res.getStatus());
        assertTrue(res.getReasons().contains("Employee age is below 21"));
    }

    // --- MULTIPLE REJECTION CRITERIA SCENARIOS (All flags checked) ---

    @Test
    public void testMultipleFailuresTrackedTogether() {
        // Fails: Age (19), Unauthorized Department (Marketing), Inactive Status, Invalid ID
        Employee emp = new Employee("EMP005", "Charlie Brown", 19, "Marketing", "Inactive", 3, false);
        EvaluationResult res = App.evaluateAccess(emp, 3);
        
        assertEquals("Not Eligible", res.getStatus());
        List<String> reasons = res.getReasons();
        
        // Assert all distinct parameters are identified concurrently
        assertEquals(4, reasons.size());
        assertTrue(reasons.contains("Employee age is below 21"));
        assertTrue(reasons.contains("Department is not authorized"));
        assertTrue(reasons.contains("Employment status is not active"));
        assertTrue(reasons.contains("Employee ID is invalid"));
    }

    // --- INVALID INPUTS / EXCEPTION HANDLING ---

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionOnNullEmployee() {
        App.evaluateAccess(null, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionOnEmptyEmployeeId() {
        Employee emp = new Employee("", "No ID", 25, "IT", "Active", 3, true);
        App.evaluateAccess(emp, 3);
    }
}
