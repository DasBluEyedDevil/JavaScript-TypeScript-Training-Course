package com.jstslearning.util;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Syntax highlighter for JavaScript and TypeScript code
 * Supports ES2024 and TypeScript 5.7 syntax
 */
public class SyntaxHighlighter {

    // JavaScript/TypeScript keywords (ES2024 + TS 5.7)
    private static final String[] KEYWORDS = new String[]{
        "abstract", "any", "as", "async", "await", "boolean", "break", "case", "catch",
        "class", "const", "continue", "debugger", "declare", "default", "delete", "do",
        "else", "enum", "export", "extends", "false", "finally", "for", "from", "function",
        "get", "if", "implements", "import", "in", "infer", "instanceof", "interface",
        "is", "keyof", "let", "module", "namespace", "never", "new", "null", "number",
        "of", "package", "private", "protected", "public", "readonly", "require", "return",
        "set", "static", "string", "super", "switch", "symbol", "this", "throw", "true",
        "try", "type", "typeof", "undefined", "unknown", "var", "void", "while", "with",
        "yield"
    };

    // Built-in objects and common APIs
    private static final String[] BUILTINS = new String[]{
        "Array", "ArrayBuffer", "Boolean", "Date", "Error", "Function", "JSON", "Map",
        "Math", "Number", "Object", "Promise", "Proxy", "RegExp", "Set", "String",
        "Symbol", "WeakMap", "WeakSet", "console", "document", "window", "setTimeout",
        "setInterval", "clearTimeout", "clearInterval", "fetch", "localStorage",
        "sessionStorage", "parseInt", "parseFloat", "isNaN", "isFinite"
    };

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String BUILTIN_PATTERN = "\\b(" + String.join("|", BUILTINS) + ")\\b";
    private static final String PAREN_PATTERN = "[()]";
    private static final String BRACE_PATTERN = "[{}]";
    private static final String BRACKET_PATTERN = "[\\[\\]]";
    private static final String SEMICOLON_PATTERN = ";";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"|'([^'\\\\]|\\\\.)*'|`([^`\\\\]|\\\\.)*`";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";
    private static final String NUMBER_PATTERN = "\\b\\d+\\.?\\d*\\b";
    private static final String OPERATOR_PATTERN = "[+\\-*/%=<>!&|^~?:]+";

    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
            + "|(?<BUILTIN>" + BUILTIN_PATTERN + ")"
            + "|(?<PAREN>" + PAREN_PATTERN + ")"
            + "|(?<BRACE>" + BRACE_PATTERN + ")"
            + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
            + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
            + "|(?<STRING>" + STRING_PATTERN + ")"
            + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
            + "|(?<NUMBER>" + NUMBER_PATTERN + ")"
            + "|(?<OPERATOR>" + OPERATOR_PATTERN + ")"
    );

    /**
     * Apply syntax highlighting to a CodeArea
     */
    public static void applyHighlighting(CodeArea codeArea) {
        // Add line numbers
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));

        // Apply syntax highlighting on text changes
        codeArea.multiPlainChanges()
                .successionEnds(Duration.ofMillis(100))
                .subscribe(ignore -> {
                    codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText()));
                });
    }

    /**
     * Compute syntax highlighting spans for the given text
     */
    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();

        while (matcher.find()) {
            String styleClass =
                matcher.group("KEYWORD") != null ? "keyword" :
                matcher.group("BUILTIN") != null ? "builtin" :
                matcher.group("PAREN") != null ? "paren" :
                matcher.group("BRACE") != null ? "brace" :
                matcher.group("BRACKET") != null ? "bracket" :
                matcher.group("SEMICOLON") != null ? "semicolon" :
                matcher.group("STRING") != null ? "string" :
                matcher.group("COMMENT") != null ? "comment" :
                matcher.group("NUMBER") != null ? "number" :
                matcher.group("OPERATOR") != null ? "operator" :
                null; // Never happens

            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }
}
