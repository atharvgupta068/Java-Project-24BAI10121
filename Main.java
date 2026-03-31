import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Student Task & Deadline Tracker
 * ─────────────────────────────────────────────────────────────────────────────
 * Entry point for the application. Provides a menu-driven command-line
 * interface for managing academic tasks and deadlines.
 *
 * Concepts demonstrated:
 *   - Object-Oriented Programming (Task, TaskManager, FileHandler classes)
 *   - Collections           (ArrayList inside TaskManager)
 *   - File I/O              (CSV persistence via FileHandler)
 *   - Exception Handling    (invalid input, missing file, parse errors)
 * ─────────────────────────────────────────────────────────────────────────────
 */
public class Main {

    private static final String DATA_FILE = "data/tasks.csv";
    private static final String DIVIDER   = "─".repeat(60);

    private static TaskManager manager;
    private static Scanner      scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        manager = new TaskManager(DATA_FILE);

        printBanner();
        showUrgentAlert();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter choice: ");
            System.out.println();

            switch (choice) {
                case 1 -> addTask();
                case 2 -> viewAllTasks();
                case 3 -> viewPendingTasks();
                case 4 -> markComplete();
                case 5 -> deleteTask();
                case 6 -> showSummary();
                case 7 -> {
                    System.out.println("  Goodbye! Stay on top of your deadlines. 👋");
                    running = false;
                }
                default -> System.out.println("  Invalid option. Please enter 1–7.");
            }
            System.out.println();
        }

        scanner.close();
    }

    // ─── Menu Handlers ─────────────────────────────────────────────────────────

    private static void addTask() {
        System.out.println(DIVIDER);
        System.out.println("  ADD NEW TASK");
        System.out.println(DIVIDER);

        System.out.print("  Subject (e.g. Java, Maths): ");
        String subject = scanner.nextLine().trim();

        System.out.print("  Description: ");
        String description = scanner.nextLine().trim();

        LocalDate deadline = null;
        while (deadline == null) {
            System.out.print("  Deadline (YYYY-MM-DD): ");
            String input = scanner.nextLine().trim();
            try {
                deadline = LocalDate.parse(input, Task.DATE_FORMAT);
                if (deadline.isBefore(LocalDate.now())) {
                    System.out.println("  ⚠  That date is in the past. Enter a future date.");
                    deadline = null;
                }
            } catch (DateTimeParseException e) {
                System.out.println("  Invalid format. Use YYYY-MM-DD (e.g. 2025-04-15).");
            }
        }

        Task created = manager.addTask(subject, description, deadline);
        System.out.println("\n  ✓ Task added successfully!");
        System.out.println(created);
    }

    private static void viewAllTasks() {
        List<Task> tasks = manager.getAllTasksSorted();
        System.out.println(DIVIDER);
        System.out.println("  ALL TASKS  (sorted by deadline)");
        System.out.println(DIVIDER);
        if (tasks.isEmpty()) {
            System.out.println("  No tasks found. Add one from the menu!");
        } else {
            tasks.forEach(System.out::println);
        }
    }

    private static void viewPendingTasks() {
        List<Task> pending = manager.getPendingTasks();
        List<Task> overdue = manager.getOverdueTasks();

        System.out.println(DIVIDER);
        System.out.println("  PENDING TASKS");
        System.out.println(DIVIDER);

        if (!overdue.isEmpty()) {
            System.out.println("  !! OVERDUE !!");
            overdue.forEach(System.out::println);
            System.out.println();
        }

        if (pending.isEmpty()) {
            System.out.println("  No pending tasks. Great job!");
        } else {
            pending.forEach(System.out::println);
        }
    }

    private static void markComplete() {
        System.out.println(DIVIDER);
        System.out.println("  MARK TASK AS COMPLETE");
        System.out.println(DIVIDER);
        viewPendingTasks();
        System.out.println();

        int id = readInt("  Enter Task ID to mark complete (0 to cancel): ");
        if (id == 0) return;

        if (manager.completeTask(id)) {
            System.out.println("  ✓ Task #" + id + " marked as complete!");
        } else {
            System.out.println("  Task with ID " + id + " not found.");
        }
    }

    private static void deleteTask() {
        System.out.println(DIVIDER);
        System.out.println("  DELETE TASK");
        System.out.println(DIVIDER);
        viewAllTasks();
        System.out.println();

        int id = readInt("  Enter Task ID to delete (0 to cancel): ");
        if (id == 0) return;

        System.out.print("  Are you sure you want to delete Task #" + id + "? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("yes")) {
            if (manager.deleteTask(id)) {
                System.out.println("  ✓ Task #" + id + " deleted.");
            } else {
                System.out.println("  Task with ID " + id + " not found.");
            }
        } else {
            System.out.println("  Deletion cancelled.");
        }
    }

    private static void showSummary() {
        System.out.println(DIVIDER);
        System.out.println("  SUMMARY");
        System.out.println(DIVIDER);
        System.out.println("  Total tasks   : " + manager.totalCount());
        System.out.println("  Pending       : " + manager.pendingCount());
        System.out.println("  Completed     : " + manager.completedCount());
        System.out.println("  Overdue       : " + manager.getOverdueTasks().size());
        System.out.println("  Due in 2 days : " + manager.getUrgentTasks().size());

        List<Task> urgent = manager.getUrgentTasks();
        if (!urgent.isEmpty()) {
            System.out.println("\n  ⚠  URGENT:");
            urgent.forEach(System.out::println);
        }
    }

    // ─── Helpers ───────────────────────────────────────────────────────────────

    private static void printBanner() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("       STUDENT TASK & DEADLINE TRACKER");
        System.out.println("═".repeat(60));
        System.out.println("  Tasks loaded: " + manager.totalCount()
                + "  |  Pending: " + manager.pendingCount());
        System.out.println("═".repeat(60));
    }

    private static void printMenu() {
        System.out.println(DIVIDER);
        System.out.println("  MENU");
        System.out.println(DIVIDER);
        System.out.println("  1. Add new task");
        System.out.println("  2. View all tasks");
        System.out.println("  3. View pending tasks");
        System.out.println("  4. Mark task as complete");
        System.out.println("  5. Delete task");
        System.out.println("  6. Summary & urgent alerts");
        System.out.println("  7. Exit");
        System.out.println(DIVIDER);
    }

    private static void showUrgentAlert() {
        List<Task> urgent = manager.getUrgentTasks();
        if (!urgent.isEmpty()) {
            System.out.println("\n  ⚠  You have " + urgent.size() + " task(s) due within 2 days!");
            urgent.forEach(System.out::println);
        }
    }

    /**
     * Reads an integer from the user safely, handling non-numeric input.
     */
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int val = Integer.parseInt(scanner.nextLine().trim());
                return val;
            } catch (NumberFormatException e) {
                System.out.println("  Please enter a valid number.");
            }
        }
    }
}
