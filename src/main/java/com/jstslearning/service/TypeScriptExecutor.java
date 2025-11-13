package com.jstslearning.service;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Value;

import java.io.ByteArrayOutputStream;

/**
 * Executes TypeScript code by transpiling to JavaScript using GraalVM
 * Supports TypeScript 5.7 syntax
 */
public class TypeScriptExecutor {

    // Embedded simple TypeScript transpiler (strips types for basic support)
    private static final String TS_TRANSPILER = """
        function transpileTypeScript(code) {
            // Simple type-stripping transpiler for basic TypeScript support
            // Handles: type annotations, interfaces, enums, as assertions

            let result = code;

            // Remove interface declarations
            result = result.replace(/interface\\s+\\w+\\s*\\{[^}]*\\}/g, '');

            // Remove type declarations
            result = result.replace(/type\\s+\\w+\\s*=\\s*[^;]+;/g, '');

            // Remove enum declarations (convert to object)
            result = result.replace(/enum\\s+(\\w+)\\s*\\{([^}]*)\\}/g, function(match, name, body) {
                let entries = body.split(',').map(e => {
                    let parts = e.trim().split('=');
                    if (parts.length === 2) {
                        return parts[0].trim() + ': ' + parts[1].trim();
                    } else if (e.trim()) {
                        return e.trim() + ': "' + e.trim() + '"';
                    }
                    return '';
                }).filter(e => e).join(', ');
                return 'const ' + name + ' = { ' + entries + ' };';
            });

            // Remove variable type annotations: let x: number = 5
            result = result.replace(/(let|const|var)\\s+(\\w+)\\s*:\\s*[^=]+=/g, '$1 $2 =');

            // Remove function parameter types: function foo(x: number)
            result = result.replace(/(\\w+)\\s*:\\s*[^,)]+/g, '$1');

            // Remove function return type: ): number {
            result = result.replace(/\\)\\s*:\\s*[^{]+\\{/g, ') {');

            // Remove arrow function return types: (): number =>
            result = result.replace(/\\)\\s*:\\s*[^=]+=>/g, ') =>');

            // Remove as Type assertions
            result = result.replace(/\\s+as\\s+\\w+/g, '');

            // Remove generic type parameters: Array<string>, Map<K, V>
            result = result.replace(/<[^>]+>/g, '');

            // Remove readonly keyword
            result = result.replace(/\\breadonly\\s+/g, '');

            // Remove public/private/protected keywords
            result = result.replace(/\\b(public|private|protected)\\s+/g, '');

            // Remove ! non-null assertions
            result = result.replace(/!(\\.|\\[|;|,|\\)|\\s)/g, '$1');

            // Remove ? optional chaining on types (but keep runtime optional chaining)
            result = result.replace(/(\\w+)\\?\\s*:/g, '$1:');

            return result;
        }
    """;

    private final CodeExecutor jsExecutor;
    private final Context transpilerContext;

    public TypeScriptExecutor() {
        this.jsExecutor = new CodeExecutor();

        // Initialize TypeScript transpiler context
        this.transpilerContext = Context.newBuilder("js")
                .allowAllAccess(false)
                .build();

        try {
            transpilerContext.eval("js", TS_TRANSPILER);
        } catch (Exception e) {
            System.err.println("Failed to initialize TypeScript transpiler: " + e.getMessage());
        }
    }

    /**
     * Execute TypeScript code
     */
    public CodeExecutor.ExecutionResult execute(String tsCode) {
        try {
            // Transpile TypeScript to JavaScript
            Value transpilerFunc = transpilerContext.getBindings("js").getMember("transpileTypeScript");
            String jsCode = transpilerFunc.execute(tsCode).asString();

            // Execute the transpiled JavaScript
            return jsExecutor.execute(jsCode);

        } catch (PolyglotException e) {
            return new CodeExecutor.ExecutionResult(false, "",
                "TypeScript error: " + e.getMessage(), null);
        } catch (Exception e) {
            return new CodeExecutor.ExecutionResult(false, "",
                "Transpilation error: " + e.getMessage(), null);
        }
    }

    /**
     * Detect if code is TypeScript (contains type annotations)
     */
    public static boolean isTypeScript(String code) {
        // Simple heuristics to detect TypeScript
        return code.contains(": string") ||
               code.contains(": number") ||
               code.contains(": boolean") ||
               code.contains("interface ") ||
               code.contains("type ") ||
               code.contains("enum ") ||
               code.contains("<") && code.contains(">") || // Generics
               code.contains(" as "); // Type assertions
    }

    /**
     * Close the transpiler context
     */
    public void close() {
        if (transpilerContext != null) {
            transpilerContext.close();
        }
    }
}
