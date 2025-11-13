package com.jstslearning.controller;

import com.jstslearning.model.Lesson;
import com.jstslearning.model.Module;
import com.jstslearning.model.UserProgress;
import com.jstslearning.service.CodeExecutor;
import com.jstslearning.service.ContentLoader;
import com.jstslearning.service.ProgressTracker;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;

import java.util.Optional;

/**
 * Main controller for the application window
 */
public class MainController {

    @FXML
    private TreeView<String> moduleTreeView;

    @FXML
    private BorderPane contentPane;

    @FXML
    private WebView lessonWebView;

    @FXML
    private TextArea codeEditor;

    @FXML
    private TextArea outputArea;

    @FXML
    private Button runCodeButton;

    @FXML
    private Button submitButton;

    @FXML
    private Button showSolutionButton;

    @FXML
    private Label progressLabel;

    @FXML
    private ProgressBar progressBar;

    private ContentLoader contentLoader;
    private ProgressTracker progressTracker;
    private CodeExecutor codeExecutor;
    private UserProgress userProgress;
    private Lesson currentLesson;

    /**
     * Initialize the controller with services
     */
    public void initialize(ContentLoader contentLoader, ProgressTracker progressTracker) {
        this.contentLoader = contentLoader;
        this.progressTracker = progressTracker;
        this.codeExecutor = new CodeExecutor();
        this.userProgress = progressTracker.loadProgress();

        setupModuleTree();
        setupCodeEditor();
        updateProgressDisplay();

        // Load the first lesson if available
        if (!contentLoader.getModules().isEmpty()) {
            Module firstModule = contentLoader.getModules().get(0);
            if (!firstModule.getLessons().isEmpty()) {
                loadLesson(firstModule.getLessons().get(0));
            }
        }
    }

    /**
     * Set up the module/lesson tree view
     */
    private void setupModuleTree() {
        TreeItem<String> root = new TreeItem<>("Course Content");
        root.setExpanded(true);

        for (Module module : contentLoader.getModules()) {
            TreeItem<String> moduleItem = new TreeItem<>(module.getTitle());
            moduleItem.setExpanded(true);

            for (Lesson lesson : module.getLessons()) {
                String lessonText = lesson.getId() + ": " + lesson.getTitle();
                if (userProgress.isLessonComplete(lesson.getId())) {
                    lessonText += " ✓";
                }
                TreeItem<String> lessonItem = new TreeItem<>(lessonText);
                lessonItem.setValue(lesson.getId()); // Store lesson ID
                moduleItem.getChildren().add(lessonItem);
            }

            root.getChildren().add(moduleItem);
        }

        moduleTreeView.setRoot(root);
        moduleTreeView.setShowRoot(false);

        // Handle lesson selection
        moduleTreeView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.isLeaf()) {
                String lessonId = newVal.getValue();
                findAndLoadLesson(lessonId);
            }
        });
    }

    /**
     * Set up the code editor area
     */
    private void setupCodeEditor() {
        codeEditor.setStyle("-fx-font-family: 'monospace'; -fx-font-size: 14px;");
        outputArea.setStyle("-fx-font-family: 'monospace'; -fx-font-size: 12px;");
        outputArea.setEditable(false);

        runCodeButton.setOnAction(e -> runCode());
        submitButton.setOnAction(e -> submitChallenge());
        showSolutionButton.setOnAction(e -> showSolution());
    }

    /**
     * Find and load a lesson by ID
     */
    private void findAndLoadLesson(String lessonId) {
        for (Module module : contentLoader.getModules()) {
            Optional<Lesson> lessonOpt = module.getLessons().stream()
                    .filter(l -> l.getId().equals(lessonId))
                    .findFirst();

            if (lessonOpt.isPresent()) {
                loadLesson(lessonOpt.get());
                return;
            }
        }
    }

    /**
     * Load a lesson into the view
     */
    private void loadLesson(Lesson lesson) {
        this.currentLesson = lesson;
        userProgress.setCurrentLessonId(lesson.getId());
        progressTracker.saveProgress(userProgress);

        // Build HTML content for the lesson
        StringBuilder html = new StringBuilder();
        html.append("<html><head><style>");
        html.append("body { font-family: 'Segoe UI', Arial, sans-serif; padding: 20px; line-height: 1.6; }");
        html.append("h1 { color: #2c3e50; border-bottom: 3px solid #3498db; padding-bottom: 10px; }");
        html.append("h2 { color: #34495e; margin-top: 30px; }");
        html.append("pre { background: #f4f4f4; padding: 15px; border-left: 4px solid #3498db; overflow-x: auto; }");
        html.append("code { font-family: 'Courier New', monospace; background: #f4f4f4; padding: 2px 6px; }");
        html.append(".analogy { background: #e8f4f8; padding: 15px; border-radius: 5px; margin: 15px 0; }");
        html.append(".breakdown { background: #fff3cd; padding: 15px; border-radius: 5px; margin: 15px 0; }");
        html.append("</style></head><body>");

        html.append("<h1>").append(lesson.getTitle()).append("</h1>");

        html.append("<h2>💡 The Concept (Simple Analogy)</h2>");
        html.append("<div class='analogy'>").append(formatText(lesson.getConceptAnalogy())).append("</div>");

        html.append("<h2>💻 Code Example</h2>");
        html.append("<pre><code>").append(escapeHtml(lesson.getCodeExample())).append("</code></pre>");

        html.append("<h2>🔍 How This Works (Line by Line)</h2>");
        html.append("<div class='breakdown'>").append(formatText(lesson.getSyntaxBreakdown())).append("</div>");

        if (lesson.getChallenge() != null) {
            html.append("<h2>✏️ Now It's Your Turn!</h2>");
            html.append("<p>").append(formatText(lesson.getChallenge().getInstructions())).append("</p>");
            html.append("<p><em>Use the code editor below to complete the challenge.</em></p>");

            // Load starter code into editor
            codeEditor.setText(lesson.getChallenge().getStarterCode());
        }

        html.append("</body></html>");

        lessonWebView.getEngine().loadContent(html.toString());
        outputArea.clear();
    }

    /**
     * Run the code in the editor
     */
    private void runCode() {
        String code = codeEditor.getText();
        CodeExecutor.ExecutionResult result = codeExecutor.execute(code);

        if (result.isSuccess()) {
            outputArea.setText("✓ Output:\n" + result.getOutput());
            outputArea.setStyle("-fx-control-inner-background: #d4edda; -fx-font-family: 'monospace';");
        } else {
            outputArea.setText("✗ Error:\n" + result.getError());
            outputArea.setStyle("-fx-control-inner-background: #f8d7da; -fx-font-family: 'monospace';");
        }
    }

    /**
     * Submit and validate the challenge
     */
    private void submitChallenge() {
        if (currentLesson == null || currentLesson.getChallenge() == null) {
            return;
        }

        String code = codeEditor.getText();
        boolean allTestsPassed = true;
        StringBuilder feedback = new StringBuilder("Test Results:\n\n");

        for (var testCase : currentLesson.getChallenge().getTestCases()) {
            String fullCode = code + "\n" + testCase.getInput();
            CodeExecutor.ExecutionResult result = codeExecutor.execute(fullCode);

            boolean passed = result.isSuccess() &&
                    result.getOutput().trim().equals(testCase.getExpectedOutput().trim());

            feedback.append(passed ? "✓ " : "✗ ")
                    .append(testCase.getDescription())
                    .append("\n");

            if (!passed) {
                allTestsPassed = false;
                feedback.append("  Expected: ").append(testCase.getExpectedOutput()).append("\n");
                feedback.append("  Got: ").append(result.getOutput()).append("\n");
            }
        }

        if (allTestsPassed) {
            feedback.append("\n🎉 All tests passed! Lesson complete!");
            userProgress.markLessonComplete(currentLesson.getId());
            progressTracker.saveProgress(userProgress);
            updateProgressDisplay();
            outputArea.setStyle("-fx-control-inner-background: #d4edda; -fx-font-family: 'monospace';");
        } else {
            feedback.append("\n💡 Keep trying! Check the common sticking points if you need help.");
            outputArea.setStyle("-fx-control-inner-background: #fff3cd; -fx-font-family: 'monospace';");
        }

        outputArea.setText(feedback.toString());
    }

    /**
     * Show the solution for the current challenge
     */
    private void showSolution() {
        if (currentLesson == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Solution");
        alert.setHeaderText("Solution & Common Sticking Points");

        StringBuilder content = new StringBuilder();
        content.append("SOLUTION:\n\n");
        content.append(currentLesson.getSolution()).append("\n\n");
        content.append("COMMON STICKING POINTS:\n\n");
        content.append(currentLesson.getCommonStickingPoints());

        TextArea textArea = new TextArea(content.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);

        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();
    }

    /**
     * Update the progress display
     */
    private void updateProgressDisplay() {
        int totalLessons = contentLoader.getModules().stream()
                .mapToInt(m -> m.getLessons().size())
                .sum();
        int completedLessons = userProgress.getCompletedLessonCount();

        double progress = totalLessons > 0 ? (double) completedLessons / totalLessons : 0;
        progressBar.setProgress(progress);
        progressLabel.setText(String.format("Progress: %d/%d lessons completed (%.0f%%)",
                completedLessons, totalLessons, progress * 100));
    }

    /**
     * Format text with paragraph breaks
     */
    private String formatText(String text) {
        if (text == null) return "";
        return text.replace("\n\n", "</p><p>").replace("\n", "<br>");
    }

    /**
     * Escape HTML special characters
     */
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
