package com.jstslearning.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Enhanced user progress tracking with scores, streaks, and detailed analytics
 */
public class UserProgress {
    private String userId;
    private Set<String> completedLessons; // Lesson IDs: "1.1", "2.3", etc.
    private Map<String, LessonScore> lessonScores; // Detailed scores per lesson
    private Set<String> completedQuizzes; // Quiz IDs
    private Map<String, Integer> quizScores; // Quiz ID -> Score percentage
    private String currentLessonId;
    private LocalDateTime lastAccessedAt;
    private LocalDateTime createdAt;

    // Enhanced tracking
    private long totalTimeSpentSeconds;
    private int currentStreak; // Days in a row
    private int longestStreak;
    private LocalDate lastStudyDate;
    private int totalPerfectLessons; // Lessons completed on first try with 100%
    private int totalXP; // Experience points

    public UserProgress() {
        this.completedLessons = new HashSet<>();
        this.lessonScores = new HashMap<>();
        this.completedQuizzes = new HashSet<>();
        this.quizScores = new HashMap<>();
        this.lastAccessedAt = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.currentStreak = 0;
        this.longestStreak = 0;
        this.totalTimeSpentSeconds = 0;
        this.totalPerfectLessons = 0;
        this.totalXP = 0;
    }

    public UserProgress(String userId) {
        this();
        this.userId = userId;
    }

    public void markLessonComplete(String lessonId) {
        completedLessons.add(lessonId);
        updateStreak();
    }

    public void recordLessonScore(LessonScore score) {
        lessonScores.put(score.getLessonId(), score);
        if (score.isPerfect()) {
            totalPerfectLessons++;
            totalXP += 100; // Bonus for perfect score
        } else {
            totalXP += score.getScore() / 2; // XP based on score
        }
        totalTimeSpentSeconds += score.getTimeSpent();
    }

    public void markQuizComplete(String quizId, int scorePercentage) {
        completedQuizzes.add(quizId);
        quizScores.put(quizId, scorePercentage);
        totalXP += scorePercentage; // XP equals quiz score
    }

    public boolean isLessonComplete(String lessonId) {
        return completedLessons.contains(lessonId);
    }

    public int getCompletedLessonCount() {
        return completedLessons.size();
    }

    public double getAverageScore() {
        if (lessonScores.isEmpty()) return 0.0;
        return lessonScores.values().stream()
                .mapToInt(LessonScore::getScore)
                .average()
                .orElse(0.0);
    }

    public int getTotalAttempts() {
        return lessonScores.values().stream()
                .mapToInt(LessonScore::getAttempts)
                .sum();
    }

    /**
     * Update study streak
     */
    private void updateStreak() {
        LocalDate today = LocalDate.now();

        if (lastStudyDate == null) {
            currentStreak = 1;
            lastStudyDate = today;
        } else if (lastStudyDate.equals(today)) {
            // Already studied today, no change
            return;
        } else if (lastStudyDate.plusDays(1).equals(today)) {
            // Consecutive day!
            currentStreak++;
            lastStudyDate = today;
        } else {
            // Streak broken
            currentStreak = 1;
            lastStudyDate = today;
        }

        if (currentStreak > longestStreak) {
            longestStreak = currentStreak;
        }
    }

    /**
     * Get level based on XP (every 1000 XP = 1 level)
     */
    public int getLevel() {
        return (totalXP / 1000) + 1;
    }

    /**
     * Get XP progress to next level
     */
    public int getXPToNextLevel() {
        return 1000 - (totalXP % 1000);
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Set<String> getCompletedLessons() {
        return completedLessons;
    }

    public void setCompletedLessons(Set<String> completedLessons) {
        this.completedLessons = completedLessons;
    }

    public Map<String, LessonScore> getLessonScores() {
        return lessonScores;
    }

    public void setLessonScores(Map<String, LessonScore> lessonScores) {
        this.lessonScores = lessonScores;
    }

    public Set<String> getCompletedQuizzes() {
        return completedQuizzes;
    }

    public void setCompletedQuizzes(Set<String> completedQuizzes) {
        this.completedQuizzes = completedQuizzes;
    }

    public Map<String, Integer> getQuizScores() {
        return quizScores;
    }

    public void setQuizScores(Map<String, Integer> quizScores) {
        this.quizScores = quizScores;
    }

    public String getCurrentLessonId() {
        return currentLessonId;
    }

    public void setCurrentLessonId(String currentLessonId) {
        this.currentLessonId = currentLessonId;
        this.lastAccessedAt = LocalDateTime.now();
    }

    public LocalDateTime getLastAccessedAt() {
        return lastAccessedAt;
    }

    public void setLastAccessedAt(LocalDateTime lastAccessedAt) {
        this.lastAccessedAt = lastAccessedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public long getTotalTimeSpentSeconds() {
        return totalTimeSpentSeconds;
    }

    public void setTotalTimeSpentSeconds(long totalTimeSpentSeconds) {
        this.totalTimeSpentSeconds = totalTimeSpentSeconds;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public int getLongestStreak() {
        return longestStreak;
    }

    public void setLongestStreak(int longestStreak) {
        this.longestStreak = longestStreak;
    }

    public LocalDate getLastStudyDate() {
        return lastStudyDate;
    }

    public void setLastStudyDate(LocalDate lastStudyDate) {
        this.lastStudyDate = lastStudyDate;
    }

    public int getTotalPerfectLessons() {
        return totalPerfectLessons;
    }

    public void setTotalPerfectLessons(int totalPerfectLessons) {
        this.totalPerfectLessons = totalPerfectLessons;
    }

    public int getTotalXP() {
        return totalXP;
    }

    public void setTotalXP(int totalXP) {
        this.totalXP = totalXP;
    }
}
