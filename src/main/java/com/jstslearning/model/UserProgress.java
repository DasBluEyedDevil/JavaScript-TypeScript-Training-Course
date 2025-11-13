package com.jstslearning.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Tracks a user's progress through the course
 */
public class UserProgress {
    private String userId;
    private Set<String> completedLessons; // Lesson IDs: "1.1", "2.3", etc.
    private String currentLessonId;
    private LocalDateTime lastAccessedAt;

    public UserProgress() {
        this.completedLessons = new HashSet<>();
        this.lastAccessedAt = LocalDateTime.now();
    }

    public UserProgress(String userId) {
        this.userId = userId;
        this.completedLessons = new HashSet<>();
        this.lastAccessedAt = LocalDateTime.now();
    }

    public void markLessonComplete(String lessonId) {
        completedLessons.add(lessonId);
    }

    public boolean isLessonComplete(String lessonId) {
        return completedLessons.contains(lessonId);
    }

    public int getCompletedLessonCount() {
        return completedLessons.size();
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
}
