package com.jstslearning;

import com.jstslearning.controller.MainController;
import com.jstslearning.service.ContentLoader;
import com.jstslearning.service.ProgressTracker;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main entry point for the JavaScript/TypeScript Learning Platform
 */
public class JSTypeScriptLearningApp extends Application {

    private ContentLoader contentLoader;
    private ProgressTracker progressTracker;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize services
        contentLoader = new ContentLoader();
        progressTracker = new ProgressTracker();

        // Load course content
        System.out.println("Loading course content...");
        contentLoader.loadAllModules();
        System.out.println("Loaded " + contentLoader.getModules().size() + " modules");

        // Load FXML and set up the main window
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main.fxml"));
        Parent root = loader.load();

        // Inject services into controller
        MainController controller = loader.getController();
        controller.initialize(contentLoader, progressTracker);

        // Set up the stage
        primaryStage.setTitle("JavaScript/TypeScript Learning Platform - From Zero to Full-Stack");
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);
        primaryStage.show();
    }

    @Override
    public void stop() {
        // Clean up resources if needed
        System.out.println("Application shutting down...");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
