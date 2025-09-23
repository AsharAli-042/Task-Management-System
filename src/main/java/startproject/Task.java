package startproject;

import java.time.LocalDateTime;

public abstract class Task {
    private String taskName, description, assignedUser, priority;
    private String status; 
    private LocalDateTime dueDateTime;

    public Task(String taskName, String description, LocalDateTime dueDateTime, String priority, String status, String user) {
        this.taskName = taskName;
        this.description = description;
        this.dueDateTime = dueDateTime;
        this.priority = priority;
        this.status = status;
        this.assignedUser = user;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDueDateTime() {
        return dueDateTime;
    }

    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getAssignedUser() {
        return assignedUser;
    }

    public abstract void displayDetails();

    public boolean isOverdue() {
        return LocalDateTime.now().isAfter(dueDateTime) && !status.equalsIgnoreCase("Completed");
    }

}
