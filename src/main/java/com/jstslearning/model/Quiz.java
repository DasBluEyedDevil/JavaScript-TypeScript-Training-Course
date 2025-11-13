package com.jstslearning.model;

import java.util.List;

/**
 * Represents a quiz/assessment at the end of each module
 */
public class Quiz {
    private String id; // e.g., "quiz-1", "quiz-2"
    private String moduleId;
    private String title;
    private String description;
    private List<QuizQuestion> questions;
    private int passingScore; // Percentage required to pass (e.g., 80)

    public Quiz() {
    }

    public Quiz(String id, String moduleId, String title, String description,
                List<QuizQuestion> questions, int passingScore) {
        this.id = id;
        this.moduleId = moduleId;
        this.title = title;
        this.description = description;
        this.questions = questions;
        this.passingScore = passingScore;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<QuizQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuizQuestion> questions) {
        this.questions = questions;
    }

    public int getPassingScore() {
        return passingScore;
    }

    public void setPassingScore(int passingScore) {
        this.passingScore = passingScore;
    }

    /**
     * Represents a quiz question
     */
    public static class QuizQuestion {
        private String question;
        private QuestionType type;
        private List<String> options; // For multiple choice
        private String correctAnswer;
        private String explanation; // Shown after answering

        public enum QuestionType {
            MULTIPLE_CHOICE,
            TRUE_FALSE,
            CODE_OUTPUT,  // What will this code output?
            FILL_IN_BLANK // Complete the code
        }

        public QuizQuestion() {
        }

        public QuizQuestion(String question, QuestionType type, List<String> options,
                           String correctAnswer, String explanation) {
            this.question = question;
            this.type = type;
            this.options = options;
            this.correctAnswer = correctAnswer;
            this.explanation = explanation;
        }

        // Getters and setters
        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public QuestionType getType() {
            return type;
        }

        public void setType(QuestionType type) {
            this.type = type;
        }

        public List<String> getOptions() {
            return options;
        }

        public void setOptions(List<String> options) {
            this.options = options;
        }

        public String getCorrectAnswer() {
            return correctAnswer;
        }

        public void setCorrectAnswer(String correctAnswer) {
            this.correctAnswer = correctAnswer;
        }

        public String getExplanation() {
            return explanation;
        }

        public void setExplanation(String explanation) {
            this.explanation = explanation;
        }
    }
}
