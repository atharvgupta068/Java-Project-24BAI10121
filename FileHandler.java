import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles reading and writing tasks to a CSV file.
 * Demonstrates File I/O and Exception Handling concepts.
 */
public class FileHandler {

    private final String filePath;

    public FileHandler(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Saves a list of tasks to the CSV file.
     * Creates the file (and parent directories) if they don't exist.
     *
     * @param tasks the list of tasks to persist
     * @throws IOException if the file cannot be written
     */
    public void saveTasks(List<Task> tasks) throws IOException {
        // Ensure the parent directory exists
        File file = new File(filePath);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Task task : tasks) {
                writer.write(task.toCsv());
                writer.newLine();
            }
        }
    }

    /**
     * Loads tasks from the CSV file.
     * Returns an empty list if the file does not yet exist (first run).
     *
     * @return list of tasks read from disk
     * @throws IOException if the file exists but cannot be read
     */
    public List<Task> loadTasks() throws IOException {
        List<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

        // First-run: file doesn't exist yet — that's fine
        if (!file.exists()) {
            return tasks;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty()) continue;     // skip blank lines
                try {
                    tasks.add(Task.fromCsv(line));
                } catch (Exception e) {
                    // Warn but continue — don't let one bad line crash everything
                    System.out.println("  Warning: skipping malformed line " + lineNumber + " in data file.");
                }
            }
        }
        return tasks;
    }
}
