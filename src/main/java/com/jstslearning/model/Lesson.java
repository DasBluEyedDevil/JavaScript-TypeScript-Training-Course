package com.jstslearning.model;

/**
 * Represents a single lesson within a module
 * Follows the format: Concept -> Code Example -> Syntax Breakdown -> Exercise -> Solution
 */
public class Lesson {
    private String id; // e.g., "1.1", "2.3"
    private String title;
    private String conceptAnalogy; // The Simplifier: Plain-English analogy
    private String codeExample; // The Coder: Commented code block
    private String syntaxBreakdown; // The Simplifier: Line-by-line explanation
    private Challenge challenge; // The Coder: Interactive exercise
    private String solution; // The Coder: Correct answer
    private String commonStickingPoints; // The Coder: Why beginners struggle

    public Lesson() {
    }

    public Lesson(String id, String title, String conceptAnalogy, String codeExample,
                  String syntaxBreakdown, Challenge challenge, String solution,
                  String commonStickingPoints) {
        this.id = id;
        this.title = title;
        this.conceptAnalogy = conceptAnalogy;
        this.codeExample = codeExample;
        this.syntaxBreakdown = syntaxBreakdown;
        this.challenge = challenge;
        this.solution = solution;
        this.commonStickingPoints = commonStickingPoints;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getConceptAnalogy() {
        return conceptAnalogy;
    }

    public void setConceptAnalogy(String conceptAnalogy) {
        this.conceptAnalogy = conceptAnalogy;
    }

    public String getCodeExample() {
        return codeExample;
    }

    public void setCodeExample(String codeExample) {
        this.codeExample = codeExample;
    }

    public String getSyntaxBreakdown() {
        return syntaxBreakdown;
    }

    public void setSyntaxBreakdown(String syntaxBreakdown) {
        this.syntaxBreakdown = syntaxBreakdown;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getCommonStickingPoints() {
        return commonStickingPoints;
    }

    public void setCommonStickingPoints(String commonStickingPoints) {
        this.commonStickingPoints = commonStickingPoints;
    }

    @Override
    public String toString() {
        return id + " - " + title;
    }
}
