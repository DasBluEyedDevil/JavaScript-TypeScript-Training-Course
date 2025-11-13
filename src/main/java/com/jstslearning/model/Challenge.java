package com.jstslearning.model;

import java.util.List;

/**
 * Represents an interactive coding challenge for students
 */
public class Challenge {
    private String instructions;
    private String starterCode; // Pre-filled code template
    private List<TestCase> testCases; // Expected outputs for validation
    private String hint; // Optional hint for struggling students

    public Challenge() {
    }

    public Challenge(String instructions, String starterCode, List<TestCase> testCases, String hint) {
        this.instructions = instructions;
        this.starterCode = starterCode;
        this.testCases = testCases;
        this.hint = hint;
    }

    // Getters and setters
    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getStarterCode() {
        return starterCode;
    }

    public void setStarterCode(String starterCode) {
        this.starterCode = starterCode;
    }

    public List<TestCase> getTestCases() {
        return testCases;
    }

    public void setTestCases(List<TestCase> testCases) {
        this.testCases = testCases;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    /**
     * Represents a test case for validating student code
     */
    public static class TestCase {
        private String description;
        private String input; // JS code to set up test (e.g., function call)
        private String expectedOutput;

        public TestCase() {
        }

        public TestCase(String description, String input, String expectedOutput) {
            this.description = description;
            this.input = input;
            this.expectedOutput = expectedOutput;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getInput() {
            return input;
        }

        public void setInput(String input) {
            this.input = input;
        }

        public String getExpectedOutput() {
            return expectedOutput;
        }

        public void setExpectedOutput(String expectedOutput) {
            this.expectedOutput = expectedOutput;
        }
    }
}
