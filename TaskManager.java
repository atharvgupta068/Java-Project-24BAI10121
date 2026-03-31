import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages the collection of tasks in memory and coordinates persistence.
 * Demonstrates OOP (encapsulation), Collections, and business logic separation.
 */
public class TaskManager {

    private List<Task>    tasks;
    private int           nextId;
    private final FileHandler fileHandler;

    // ─── Constructor ───────────────────────────────────────────────────────────

    public TaskManager(String dataFilePath) {
        this.fileHandler = new FileHandler(dataFilePath);
        this.tasks       = new ArrayList<>();
        this.nextId      = 1;
        loadFromFile();
    }

    // ─── Task Operations ───────────────────────────────────────────────────────

    /**
     * Adds a new task to the tracker.
     *
     * @param subject     the course / subject name
     * @param description brief description of the task
     * @param deadline    due date
     * @return the newly created Task
     */
    public Task addTask(String subject, String description, LocalDate deadline) {
        Task task = new Task(nextId++, subject, description, deadline);
        tasks.add(task);
        saveToFile();
        return task;
    }

    /**
     * Marks a task as completed by its ID.
     *
     * @param id task ID
     * @return true if found and marked, false otherwise
     */
    public boolean completeTask(int id) {
        Task task = findById(id);
        if (task == null) return false;
        task.setCompleted(true);
        saveToFile();
        return true;
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id task ID
     * @return true if removed, false if not found
     */
    public boolean deleteTask(int id) {
        boolean removed = tasks.removeIf(t -> t.getId() == id);
        if (removed) saveToFile();
        return removed;
    }

    // ─── Queries ───────────────────────────────────────────────────────────────

    /** Returns all tasks sorted by deadline (earliest first). */
    public List<Task> getAllTasksSorted() {
        return tasks.stream()
                .sorted(Comparator.comparing(Task::getDeadline))
                .collect(Collectors.toList());
    }

    /** Returns only pending (not completed) tasks sorted by deadline. */
    public List<Task> getPendingTasks() {
        return tasks.stream()
                .filter(t -> !t.isCompleted())
                .sorted(Comparator.comparing(Task::getDeadline))
                .collect(Collectors.toList());
    }

    /** Returns completed tasks. */
    public List<Task> getCompletedTasks() {
        return tasks.stream()
                .filter(Task::isCompleted)
                .collect(Collectors.toList());
    }

    /** Returns tasks due within the next 2 days (urgent). */
    public List<Task> getUrgentTasks() {
        return tasks.stream()
                .filter(t -> t.isDueWithin(2))
                .sorted(Comparator.comparing(Task::getDeadline))
                .collect(Collectors.toList());
    }

    /** Returns overdue incomplete tasks. */
    public List<Task> getOverdueTasks() {
        return tasks.stream()
                .filter(Task::isOverdue)
                .sorted(Comparator.comparing(Task::getDeadline))
                .collect(Collectors.toList());
    }

    /** Finds a task by ID, returns null if not found. */
    public Task findById(int id) {
        return tasks.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /** Total task count. */
    public int totalCount()     { return tasks.size(); }

    /** Pending task count. */
    public int pendingCount()   { return (int) tasks.stream().filter(t -> !t.isCompleted()).count(); }

    /** Completed task count. */
    public int completedCount() { return (int) tasks.stream().filter(Task::isCompleted).count(); }

    // ─── Persistence ───────────────────────────────────────────────────────────

    private void saveToFile() {
        try {
            fileHandler.saveTasks(tasks);
        } catch (IOException e) {
            System.out.println("  Warning: could not save tasks — " + e.getMessage());
        }
    }

    private void loadFromFile() {
        try {
            tasks = fileHandler.loadTasks();
            // Set nextId to one more than the current max ID so IDs stay unique
            nextId = tasks.stream()
                    .mapToInt(Task::getId)
                    .max()
                    .orElse(0) + 1;
        } catch (IOException e) {
            System.out.println("  Warning: could not load saved tasks — " + e.getMessage());
            tasks = new ArrayList<>();
        }
    }
}
