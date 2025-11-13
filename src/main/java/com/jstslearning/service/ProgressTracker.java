package com.jstslearning.service;

import com.jstslearning.model.UserProgress;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Manages user progress persistence using SQLite
 */
public class ProgressTracker {
    private static final String DB_URL = "jdbc:sqlite:user_progress.db";
    private static final String DEFAULT_USER_ID = "default_user";

    public ProgressTracker() {
        initializeDatabase();
    }

    /**
     * Create the database tables if they don't exist
     */
    private void initializeDatabase() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS progress (
                user_id TEXT PRIMARY KEY,
                current_lesson_id TEXT,
                last_accessed_at TEXT
            );

            CREATE TABLE IF NOT EXISTS completed_lessons (
                user_id TEXT,
                lesson_id TEXT,
                completed_at TEXT,
                PRIMARY KEY (user_id, lesson_id)
            );
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTableSQL);
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Load progress for the default user
     */
    public UserProgress loadProgress() {
        return loadProgress(DEFAULT_USER_ID);
    }

    /**
     * Load progress for a specific user
     */
    public UserProgress loadProgress(String userId) {
        UserProgress progress = new UserProgress(userId);

        // Load basic progress
        String progressSQL = "SELECT current_lesson_id, last_accessed_at FROM progress WHERE user_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(progressSQL)) {
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                progress.setCurrentLessonId(rs.getString("current_lesson_id"));
                String lastAccessed = rs.getString("last_accessed_at");
                if (lastAccessed != null) {
                    progress.setLastAccessedAt(LocalDateTime.parse(lastAccessed));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading progress: " + e.getMessage());
        }

        // Load completed lessons
        String completedSQL = "SELECT lesson_id FROM completed_lessons WHERE user_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(completedSQL)) {
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();

            Set<String> completedLessons = new HashSet<>();
            while (rs.next()) {
                completedLessons.add(rs.getString("lesson_id"));
            }
            progress.setCompletedLessons(completedLessons);
        } catch (SQLException e) {
            System.err.println("Error loading completed lessons: " + e.getMessage());
        }

        return progress;
    }

    /**
     * Save user progress
     */
    public void saveProgress(UserProgress progress) {
        String userId = progress.getUserId() != null ? progress.getUserId() : DEFAULT_USER_ID;

        // Save basic progress
        String progressSQL = """
            INSERT OR REPLACE INTO progress (user_id, current_lesson_id, last_accessed_at)
            VALUES (?, ?, ?)
        """;
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(progressSQL)) {
            pstmt.setString(1, userId);
            pstmt.setString(2, progress.getCurrentLessonId());
            pstmt.setString(3, progress.getLastAccessedAt().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving progress: " + e.getMessage());
        }

        // Save completed lessons
        String completedSQL = """
            INSERT OR IGNORE INTO completed_lessons (user_id, lesson_id, completed_at)
            VALUES (?, ?, ?)
        """;
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(completedSQL)) {
            for (String lessonId : progress.getCompletedLessons()) {
                pstmt.setString(1, userId);
                pstmt.setString(2, lessonId);
                pstmt.setString(3, LocalDateTime.now().toString());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error saving completed lessons: " + e.getMessage());
        }
    }

    /**
     * Mark a lesson as complete
     */
    public void markLessonComplete(String userId, String lessonId) {
        UserProgress progress = loadProgress(userId);
        progress.markLessonComplete(lessonId);
        saveProgress(progress);
    }
}
