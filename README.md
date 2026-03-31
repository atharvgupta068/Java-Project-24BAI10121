# Student Task & Deadline Tracker 📚

A command-line Java application that helps students track assignments, deadlines, and academic tasks — with persistent storage, overdue alerts, and urgent deadline warnings.

---

## Problem Statement

Students juggle tasks across multiple subjects and frequently lose track of approaching deadlines. This tool provides a lightweight, offline solution to add, view, complete, and delete tasks — with data saved automatically between sessions.

---

## Features

- ➕ Add tasks with subject, description, and deadline
- 📋 View all tasks sorted by deadline
- ✅ Mark tasks as complete
- 🗑️ Delete tasks with confirmation
- ⚠️ Automatic alerts for tasks due within 2 days
- 🔴 Overdue task detection
- 💾 Persistent storage via CSV file (no database needed)
- 📊 Summary dashboard with counts

---

## Project Structure

```
StudentTaskTracker/
├── src/
│   ├── Task.java          # Data model – represents a single task
│   ├── TaskManager.java   # Business logic – manages task collection
│   ├── FileHandler.java   # File I/O – reads and writes CSV data
│   └── Main.java          # Entry point – CLI menu interface
├── data/
│   └── tasks.csv          # Auto-created on first run
└── README.md
```

---

## Java Concepts Used

| Concept | Where |
|---|---|
| Classes & OOP | `Task`, `TaskManager`, `FileHandler` |
| Encapsulation | Private fields with getters/setters in `Task` |
| Collections | `ArrayList<Task>` + Stream API in `TaskManager` |
| File I/O | `BufferedReader` / `BufferedWriter` in `FileHandler` |
| Exception Handling | `try-catch` for I/O errors, invalid input, parse failures |

---

## Prerequisites

- Java JDK 11 or higher
- A terminal / command prompt

---

## How to Run

### 1. Clone the repository
```bash
git clone https://github.com/madynamo/StudentTaskTracker.git
cd StudentTaskTracker
```

### 2. Compile
```bash
cd src
javac *.java
```

### 3. Run
```bash
# From inside the src/ folder:
java Main
```

> On first run, the `data/tasks.csv` file is created automatically. Your tasks persist between sessions.

---

## Usage Guide

When you start the app, you'll see a menu:

```
════════════════════════════════════════════════════════════
       STUDENT TASK & DEADLINE TRACKER
════════════════════════════════════════════════════════════
  Tasks loaded: 0  |  Pending: 0
════════════════════════════════════════════════════════════

  MENU
──────────────────────────────────────────────────────────
  1. Add new task
  2. View all tasks
  3. View pending tasks
  4. Mark task as complete
  5. Delete task
  6. Summary & urgent alerts
  7. Exit
```

### Adding a Task
Enter the subject, a short description, and a deadline in `YYYY-MM-DD` format.

### Urgent Alerts
Any task due within 2 days is flagged with `⚠ DUE SOON` automatically.

### Data File
All tasks are stored in `data/tasks.csv`. You can inspect or back it up at any time.

---

## Sample Data File (tasks.csv)

```
1,Java,Submit BYOP project,2025-03-31,false
2,Maths,Complete integration worksheet,2025-03-28,false
3,Physics,Lab report submission,2025-03-20,true
```

---

## Author

**ATHARV GUPTA**  
```
