package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class TodoList {

    /** Internal Task class is immutable from the outside. */
    public static final class Task {
        private final int id;
        private final String description;
        private boolean completed;

        Task(int id, String description) {
            this.id = id;
            this.description = description;
            this.completed = false;
        }

        public int id() { return id; }
        public String description() { return description; }
        public boolean isCompleted() { return completed; }

        void markCompleted() { this.completed = true; }

        @Override
        public String toString() {
            return String.format("[%s] #%d %s", completed ? "x" : " ", id, description);
        }
    }

    private final Map<Integer, Task> items = new LinkedHashMap<>(); // preserves insertion order
    private int nextId = 1;

    /** Adds a new task. Throws IllegalArgumentException for null/blank input. Returns the created Task. */
    public Task add(String description) {
        String d = safeTrim(description);
        if (d == null || d.isEmpty()) {
            throw new IllegalArgumentException("Task description cannot be null or blank.");
        }
        Task t = new Task(nextId++, d);
        items.put(t.id(), t);
        return t;
    }

    /**
     * Marks a task complete.
     * - If key is a number (e.g., "3"), completes task with that id if it exists.
     * - Otherwise, completes the first unmatched task with exact (case-insensitive) description.
     * Returns true if something was completed, false otherwise.
     * Graceful handling: null/blank returns false instead of throwing.
     */
    public boolean complete(String key) {
        if (key == null || key.trim().isEmpty()) return false;

        // Try numeric id first
        try {
            int id = Integer.parseInt(key.trim());
            Task t = items.get(id);
            if (t != null && !t.isCompleted()) {
                t.markCompleted();
                return true;
            }
            return false;
        } catch (NumberFormatException ignored) {
            // Not an int, fall through to description match
        }

        String target = key.trim();
        for (Task t : items.values()) {
            if (!t.isCompleted() && t.description().equalsIgnoreCase(target)) {
                t.markCompleted();
                return true;
            }
        }
        return false;
    }

    /** Returns a snapshot list of all tasks. The list is unmodifiable. */
    public List<Task> all() {
        return Collections.unmodifiableList(new ArrayList<>(items.values()));
    }

    /** Returns a snapshot list of completed tasks. */
    public List<Task> complete() {
        return filterByCompletion(true);
    }

    /** Returns a snapshot list of incomplete tasks. */
    public List<Task> incomplete() {
        return filterByCompletion(false);
    }

    /** Clears the entire list. */
    public void clear() {
        items.clear();
    }

    // ---------- Pretty printing helpers (optional but nice) ----------

    public static String prettyFormat(List<Task> tasks, String emptyMessage) {
        if (tasks == null || tasks.isEmpty()) {
            return emptyMessage;
        }
        return tasks.stream().map(Task::toString).collect(Collectors.joining(System.lineSeparator()));
    }

    public String prettyAll() {
        return prettyFormat(all(), "(No tasks — your list is empty)");
    }

    public String prettyComplete() {
        return prettyFormat(complete(), "(No completed tasks yet)");
    }

    public String prettyIncomplete() {
        return prettyFormat(incomplete(), "(No incomplete tasks — great job!)");
    }

    // ---------- internals ----------

    private static String safeTrim(String s) {
        return s == null ? null : s.trim();
    }

    private List<Task> filterByCompletion(boolean completed) {
        List<Task> out = items.values().stream()
                .filter(t -> t.isCompleted() == completed)
                .collect(Collectors.toList());
        return Collections.unmodifiableList(out);
    }
}
