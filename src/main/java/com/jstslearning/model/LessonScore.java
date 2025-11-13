package com.jstslearning.model;

import java.time.LocalDateTime;

/**
 * Represents a student's score/performance on a specific lesson
 */
public class LessonScore {
    private String lessonId;
    private int attempts;  // Number of times the challenge was attempted
    private int score;     // Score out of 100
    private long timeSpent; // Time spent in seconds
    private LocalDateTime completedAt;
    private boolean perfect; // Completed on first try with 100%

    public LessonScore() {
    }

    public LessonScore(String lessonId) {
        this.lessonId = lessonId;
        this.attempts = 0;
        this.score = 0;
        this.timeSpent = 0;
        this.perfect = false;
    }

    // Getters and setters
    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public void incrementAttempts() {
        this.attempts++;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
        if (score == 100 && attempts == 1) {
            this.perfect = true;
        }
    }

    public long getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(long timeSpent) {
        this.timeSpent = timeSpent;
    }

    public void addTimeSpent(long seconds) {
        this.timeSpent += seconds;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public boolean isPerfect() {
        return perfect;
    }

    public void setPerfect(boolean perfect) {
        this.perfect = perfect;
    }

    /**
     * Get a letter grade for the score
     */
    public String getGrade() {
        if (score >= 90) return "A";
        if (score >= 80) return "B";
        if (score >= 70) return "C";
        if (score >= 60) return "D";
        return "F";
    }
}
