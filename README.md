# 🎓 JavaScript/TypeScript Learning Platform

A **self-contained, interactive desktop application** built with JavaFX that teaches complete beginners to become job-ready full-stack developers. This platform takes students from "What is console.log()?" to deploying production-ready MERN/PERN-stack applications.

## 🌟 Core Philosophy: "Concept First, Jargon Last"

Every lesson follows a strict pedagogical approach:
1. **Real-world analogy** before any technical term
2. **Simple code example** with detailed comments
3. **Line-by-line breakdown** explaining the syntax
4. **Interactive coding challenge** with scaffolded support
5. **Solution & common mistakes** to prevent frustration

## ✨ Features

### 🖥️ JavaFX Desktop Application
- Modern, responsive GUI with split-pane layout
- Native cross-platform support (Windows, macOS, Linux)
- No internet connection required - runs completely offline

### 📚 Hybrid Learning System
- **Concept explanations** with analogy-first teaching
- **Live code editor** with syntax highlighting
- **Interactive challenges** with instant feedback
- **Real-time JavaScript execution** using GraalVM

### 📊 Progress Tracking
- SQLite-backed persistence (survives app restarts)
- Visual progress bar showing completion percentage
- Resume exactly where you left off
- Checkmarks for completed lessons

### 🚀 Complete Curriculum (14 Modules)
1. **Module 1-2** (Included): Basics, Variables, Data Types
2. **Module 3-6** (Expandable): Control Flow, Loops, Arrays, Functions
3. **Module 7-8** (Expandable): DOM Manipulation, Async JavaScript
4. **Module 9** (Expandable): TypeScript Introduction
5. **Module 10-11** (Expandable): Node.js, Express, Databases
6. **Module 12-13** (Expandable): React, Full-Stack Integration
7. **Module 14** (Expandable): Deployment & Professional Tools

## 🏗️ Architecture

```
JavaScript-TypeScript-Training-Course/
├── src/main/
│   ├── java/com/jstslearning/
│   │   ├── JSTypeScriptLearningApp.java    (Main entry point)
│   │   ├── model/                           (Domain models)
│   │   │   ├── Module.java
│   │   │   ├── Lesson.java
│   │   │   ├── Challenge.java
│   │   │   └── UserProgress.java
│   │   ├── service/                         (Business logic)
│   │   │   ├── ContentLoader.java           (JSON → Objects)
│   │   │   ├── ProgressTracker.java         (SQLite persistence)
│   │   │   └── CodeExecutor.java            (GraalVM JS engine)
│   │   └── controller/                      (JavaFX controllers)
│   │       └── MainController.java
│   └── resources/
│       ├── content/                         (Lesson JSON files)
│       │   ├── module1.json
│       │   └── module2.json
│       ├── views/                           (FXML layouts)
│       │   └── main.fxml
│       └── styles/                          (CSS)
│           └── main.css
└── pom.xml                                  (Maven dependencies)
```

## 🛠️ Technology Stack

- **Java 17+** - Modern LTS version with text blocks, records
- **JavaFX 21** - Cross-platform GUI framework
- **GraalVM JavaScript Engine** - Execute student code safely
- **Gson** - JSON parsing for lesson content
- **SQLite** - Lightweight database for progress tracking
- **RichTextFX** - Enhanced code editor capabilities
- **Maven** - Build automation and dependency management

## 📦 Prerequisites

Before building, ensure you have:

1. **Java Development Kit (JDK) 17 or higher**
   ```bash
   java -version  # Should show 17 or higher
   ```

2. **Maven 3.6+**
   ```bash
   mvn -version
   ```

### Installing Prerequisites

#### macOS (using Homebrew)
```bash
brew install openjdk@17
brew install maven
```

#### Ubuntu/Debian
```bash
sudo apt update
sudo apt install openjdk-17-jdk maven
```

#### Windows
- Download JDK from [Adoptium](https://adoptium.net/) or [Oracle](https://www.oracle.com/java/technologies/downloads/)
- Download Maven from [Apache Maven](https://maven.apache.org/download.cgi)
- Add both to your PATH environment variable

## 🚀 Quick Start

### 1. Clone the Repository
```bash
git clone https://github.com/YOUR_USERNAME/JavaScript-TypeScript-Training-Course.git
cd JavaScript-TypeScript-Training-Course
```

### 2. Build the Application
```bash
mvn clean install
```

This will:
- Download all dependencies (first run takes longer)
- Compile Java source code
- Run tests (if any)
- Package into a JAR file

### 3. Run the Application

#### Option A: Using Maven (Development)
```bash
mvn javafx:run
```

#### Option B: Using the JAR (Production)
```bash
java -jar target/javascript-typescript-trainer-1.0.0.jar
```

#### Option C: Platform-Specific Executables (Coming Soon)
Native installers for Windows (.exe), macOS (.dmg), and Linux (.deb) will be available.

## 📖 Using the Application

### Interface Overview

1. **Top Bar**
   - Title and tagline
   - Progress indicator showing X/Y lessons completed

2. **Left Sidebar**
   - Tree view of all modules and lessons
   - Checkmarks (✓) indicate completed lessons
   - Click any lesson to load it

3. **Main Content Area (Top)**
   - Lesson content displayed in rich HTML
   - Sections: Concept Analogy, Code Example, Syntax Breakdown

4. **Code Editor (Bottom Left)**
   - Write and test JavaScript code
   - Comes pre-filled with challenge starter code
   - Syntax highlighting (monospace font)

5. **Output Console (Bottom Right)**
   - See results of your code
   - Error messages with helpful context
   - Test case validation feedback

### Workflow

1. **Read the lesson** - Start with the analogy, then review the code example
2. **Study the breakdown** - Understand each line before coding
3. **Write your solution** - Modify the starter code in the editor
4. **Run Code** - Test your code instantly with the "▶ Run Code" button
5. **Submit** - When ready, click "✓ Submit" to validate against test cases
6. **Get feedback** - See which tests passed/failed
7. **Show Solution** - If stuck, click "💡 Show Solution" for the answer and common mistakes

## 🧩 Extending the Platform

### Adding New Lessons

Lessons are defined in JSON format in `src/main/resources/content/moduleX.json`.

#### Lesson Structure
```json
{
  "id": "1.1",
  "title": "Lesson Title",
  "conceptAnalogy": "Plain-English explanation with real-world analogy...",
  "codeExample": "// Commented code example\nconsole.log('Hello');",
  "syntaxBreakdown": "Line-by-line explanation of how the code works...",
  "challenge": {
    "instructions": "What the student needs to do...",
    "starterCode": "// Pre-filled code template",
    "testCases": [
      {
        "description": "Test case description",
        "input": "console.log(result);",
        "expectedOutput": "42"
      }
    ],
    "hint": "Optional hint for struggling students"
  },
  "solution": "// Correct answer",
  "commonStickingPoints": "Explanation of common mistakes..."
}
```

### Adding New Modules

1. Create `src/main/resources/content/moduleX.json`
2. Update `ContentLoader.java` to load the new module:
   ```java
   for (int i = 1; i <= 3; i++) {  // Change 2 to 3
   ```

### Customizing the UI

- **Colors/Fonts**: Edit `src/main/resources/styles/main.css`
- **Layout**: Edit `src/main/resources/views/main.fxml`
- **Behavior**: Edit `src/main/java/com/jstslearning/controller/MainController.java`

## 🧪 Testing

### Manual Testing
The application includes built-in code execution for testing student submissions.

### Adding Unit Tests (Future)
```bash
mvn test
```

## 🐛 Troubleshooting

### JavaFX Not Found
If you see errors about JavaFX modules not being found:
```bash
mvn clean install -U  # Force update dependencies
```

### GraalVM JavaScript Errors
If student code execution fails:
- Check `CodeExecutor.java` has proper sandboxing
- Ensure GraalVM JS dependencies are in pom.xml

### SQLite Database Locked
If progress isn't saving:
- Close any other database connections
- Delete `user_progress.db` to reset (loses progress)

### UI Not Displaying Correctly
- Check that FXML and CSS files are in `src/main/resources/`
- Ensure paths in Java code match resource locations

## 📝 Development Roadmap

- [x] Core JavaFX application structure
- [x] Lesson content loading (JSON)
- [x] Code execution engine (GraalVM)
- [x] Progress tracking (SQLite)
- [x] Module 1: Absolute Basics (3 lessons)
- [x] Module 2: Variables & Data Types (2 lessons)
- [ ] Module 3-14: Complete curriculum (65 lessons)
- [ ] Enhanced code editor (autocomplete, multi-file support)
- [ ] TypeScript execution support
- [ ] Node.js/npm simulation for backend lessons
- [ ] Built-in browser preview for HTML/CSS lessons
- [ ] Export progress to PDF certificate
- [ ] Cloud backup of progress (optional)
- [ ] Native installers (.exe, .dmg, .deb)

## 🤝 Contributing

Contributions are welcome! Areas of focus:

1. **Content**: Write new lessons following the "Concept First, Jargon Last" philosophy
2. **Features**: Enhanced code editor, better error messages, hints system
3. **Testing**: Unit tests, integration tests, UI tests
4. **Documentation**: Tutorials, video walkthroughs
5. **Localization**: Translate lessons to other languages

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🙏 Acknowledgments

- **JavaFX** - For the excellent cross-platform GUI framework
- **GraalVM** - For safe, sandboxed JavaScript execution
- **RichTextFX** - For enhanced text editing capabilities
- **Every beginner developer** - This is built for you ❤️

## 📧 Support

- **Issues**: [GitHub Issues](https://github.com/YOUR_USERNAME/JavaScript-TypeScript-Training-Course/issues)
- **Discussions**: [GitHub Discussions](https://github.com/YOUR_USERNAME/JavaScript-TypeScript-Training-Course/discussions)

---

**Built with 🎓 to make learning programming accessible to everyone.**

*"The best time to start learning was yesterday. The second best time is now."*
