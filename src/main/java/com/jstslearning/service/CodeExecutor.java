package com.jstslearning.service;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Value;

import java.io.ByteArrayOutputStream;

/**
 * Executes JavaScript code using GraalVM's JavaScript engine
 */
public class CodeExecutor {

    /**
     * Result of code execution
     */
    public static class ExecutionResult {
        private final boolean success;
        private final String output;
        private final String error;
        private final Value returnValue;

        public ExecutionResult(boolean success, String output, String error, Value returnValue) {
            this.success = success;
            this.output = output;
            this.error = error;
            this.returnValue = returnValue;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getOutput() {
            return output;
        }

        public String getError() {
            return error;
        }

        public Value getReturnValue() {
            return returnValue;
        }
    }

    /**
     * Execute JavaScript code and capture output
     */
    public ExecutionResult execute(String code) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (Context context = Context.newBuilder("js")
                .allowAllAccess(false) // Security: restrict file system access
                .out(outputStream)
                .err(outputStream)
                .build()) {

            // Add a custom console.log implementation
            String consoleSetup = """
                var console = {
                    log: function(...args) {
                        print(args.join(' '));
                    }
                };
            """;
            context.eval("js", consoleSetup);

            // Execute the user's code
            Value result = context.eval("js", code);

            String output = outputStream.toString().trim();
            return new ExecutionResult(true, output, null, result);

        } catch (PolyglotException e) {
            String errorMsg = e.getMessage();
            return new ExecutionResult(false, outputStream.toString(), errorMsg, null);
        } catch (Exception e) {
            return new ExecutionResult(false, outputStream.toString(),
                "Unexpected error: " + e.getMessage(), null);
        }
    }

    /**
     * Execute code and compare output to expected result
     */
    public boolean validateOutput(String code, String expectedOutput) {
        ExecutionResult result = execute(code);
        if (!result.isSuccess()) {
            return false;
        }

        String actualOutput = result.getOutput().trim();
        String expected = expectedOutput.trim();

        return actualOutput.equals(expected);
    }
}
