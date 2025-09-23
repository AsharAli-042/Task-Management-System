package startproject;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class TaskFilter {
    private ArrayList<Task> tasks;
    private String loggedInUser;

    public TaskFilter(ArrayList<Task> tasks, String loggedInUser) {
        this.tasks = tasks;
        this.loggedInUser = loggedInUser;
    }

    public void displayAllTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks available.");
        } else {
            tasks.stream()
                .filter(task -> task.getAssignedUser().equals(loggedInUser))  // Filter tasks assigned to the logged-in user
                .forEach(task -> {
                    task.displayDetails();
                    System.out.println();
                });
        }
    }

    public void displayHighPriorityTasks() {
        boolean found = false;
        for (Task task : tasks) {
            if (task.getAssignedUser().equals(loggedInUser) && task.getPriority().equalsIgnoreCase("High")) {
                task.displayDetails();
                System.out.println();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No high priority tasks available.");
        }
    }

    public void displayLowPriorityTasks() {
        boolean found = false;
        for (Task task : tasks) {
            if (task.getAssignedUser().equals(loggedInUser) && task.getPriority().equalsIgnoreCase("Low")) {
                task.displayDetails();
                System.out.println();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No low priority tasks available.");
        }
    }

    public void displayOverdueTasks() {
        LocalDateTime currentTime = LocalDateTime.now();
        boolean found = false;
        for (Task task : tasks) {
            if (task.getAssignedUser().equals(loggedInUser) && task.getDueDateTime().isBefore(currentTime) && !task.getStatus().equalsIgnoreCase("Completed")) {
                task.displayDetails();
                System.out.println();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No overdue tasks available.");
        }
    }
}
