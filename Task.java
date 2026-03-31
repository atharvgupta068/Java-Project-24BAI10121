import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single academic task or assignment.
 * This is the core data model of the Student Task Tracker.
 */
public class Task {

    // ─── Fields ────────────────────────────────────────────────────────────────

    private int id;
    private String subject;
    private String description;
    private LocalDate deadline;
    private boolean completed;

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // ─── Constructor ───────────────────────────────────────────────────────────

    public Task(int id, String subject, String description, LocalDate deadline) {
        this.id          = id;
        this.subject     = subject;
        this.description = description;
        this.deadline    = deadline;
        this.completed   = false;
    }

    // ─── Getters & Setters ─────────────────────────────────────────────────────

    public int getId()                    { return id; }
    public String getSubject()            { return subject; }
    public String getDescription()        { return description; }
    public LocalDate getDeadline()        { return deadline; }
    public boolean isCompleted()          { return completed; }
    public void setCompleted(boolean val) { this.completed = val; }

    // ─── Utility ───────────────────────────────────────────────────────────────

    /**
     * Returns true if this task is due within the next 'days' days and not yet done.
     */
    public boolean isDueWithin(int days) {
        LocalDate today = LocalDate.now();
        return !completed
                && !deadline.isBefore(today)
                && !deadline.isAfter(today.plusDays(days));
    }

    /**
     * Returns true if the deadline has already passed and task is not completed.
     */
    public boolean isOverdue() {
        return !completed && deadline.isBefore(LocalDate.now());
    }

    /**
     * Converts this task to a CSV line for file storage.
     * Format: id,subject,description,deadline,completed
     */
    public String toCsv() {
        return id + "," + subject + "," + description + "," + deadline.format(DATE_FORMAT) + "," + completed;
    }

    /**
     * Parses a CSV line back into a Task object.
     */
    public static Task fromCsv(String line) {
        String[] parts = line.split(",", 5);   // limit 5 so commas in description are safe
        int        id          = Integer.parseInt(parts[0].trim());
        String     subject     = parts[1].trim();
        String     description = parts[2].trim();
        LocalDate  deadline    = LocalDate.parse(parts[3].trim(), DATE_FORMAT);
        boolean    completed   = Boolean.parseBoolean(parts[4].trim());

        Task t = new Task(id, subject, description, deadline);
        t.setCompleted(completed);
        return t;
    }

    @Override
    public String toString() {
        String status   = completed ? "[DONE]" : isOverdue() ? "[OVERDUE]" : "[PENDING]";
        String urgency  = isDueWithin(2) ? " ⚠ DUE SOON" : "";
        return String.format("  [%d] %s | %s | %s | Due: %s%s",
                id, status, subject, description, deadline.format(DATE_FORMAT), urgency);
    }
}
