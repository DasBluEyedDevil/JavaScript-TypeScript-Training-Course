package com.jstslearning.model;

import java.util.List;

/**
 * Represents a learning module (e.g., "Module 1: The Absolute Basics")
 */
public class Module {
    private int id;
    private String title;
    private String description;
    private String goal;
    private List<Lesson> lessons;

    public Module() {
    }

    public Module(int id, String title, String description, String goal, List<Lesson> lessons) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.goal = goal;
        this.lessons = lessons;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    @Override
    public String toString() {
        return title;
    }
}
