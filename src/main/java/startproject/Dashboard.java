package startproject;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Dashboard {
    private ArrayList<Task> tasks = new ArrayList<>();
    private User userManager;
    private NotificationSystem notificationSystem;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma");

    public Dashboard(User userManager) {
        this.userManager = userManager;
        this.notificationSystem = new NotificationSystem();
        loadTasksFromFile();
    }

      public void saveToDb(String user,String tname,String priority,LocalDateTime date) {
        if (userManager.getLoggedInUser() == null) {
           System.out.println("No user is logged in. Please log in to save tasks to the database.");
           return;
        }

        String url = "jdbc:mysql://localhost:3306/mytask";
        String dbUser = "root";
        String dbPassword = "@abdur";

        String query = "INSERT INTO task (assignedUser, taskName,dueDateTime,priority) VALUES (?, ?, ?, ?) " +
                       "ON DUPLICATE KEY UPDATE priority = VALUES(priority), dueDateTime = VALUES(dueDateTime);";

        try (Connection connection = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement statement = connection.prepareStatement(query)) {

            
                statement.setString(1, user);
                statement.setString(2, tname);
                statement.setString(4, priority);
                statement.setString(3, date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

                statement.executeUpdate();
            
            System.out.println("Tasks saved to the database successfully.");

        } catch (SQLException e) {
            System.err.println("Database save failed: " + e.getMessage());
        }
    }

    public void startNotifications() {
        if (userManager.getLoggedInUser() != null) {
            notificationSystem.scheduleNotifications(tasks, userManager.getLoggedInUser());
        }
    }

    private void saveTasksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Tasks.txt"))) {
            for (Task task : tasks) {
                writer.write(task.getTaskName() + "," + task.getDescription() + "," +
                        task.getDueDateTime().format(DATE_TIME_FORMATTER) + "," +
                        task.getPriority() + "," + task.getStatus() + "," +
                        task.getAssignedUser());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving tasks to file: " + e.getMessage());
        }
    }

    private void loadTasksFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("Tasks.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    String taskName = parts[0];
                    String description = parts[1];
                    LocalDateTime dueDateTime = LocalDateTime.parse(parts[2], DATE_TIME_FORMATTER);
                    String priority = parts[3];
                    String status = parts[4];
                    String assignedUser = parts[5];

                    Task task;
                    if ("High".equalsIgnoreCase(priority)) {
                        task = new HighPriorityTask(taskName, description, dueDateTime,priority,status, assignedUser);
                    } else {
                        task = new LowPriorityTask(taskName, description, dueDateTime,priority,status, assignedUser);
                    }
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading tasks from file: " + e.getMessage());
        }
    }

    public void displayDashboardToGUI(StringBuilder output) {
        if (tasks.isEmpty()) {
            output.append("No tasks available.");
        } else {
            for (Task task : tasks) {
                if (task.getAssignedUser().equals(userManager.getLoggedInUser())) {
                    output.append("Task Name: ").append(task.getTaskName()).append("\n")
                            .append("Description: ").append(task.getDescription()).append("\n")
                            .append("Due Date: ").append(task.getDueDateTime()).append("\n")
                            .append("Priority: ").append(task.getPriority()).append("\n")
                            .append("Status: ").append(task.getStatus()).append("\n")
                            .append("-----------------------------------\n");
                }
            }
        }
    }

    public void addTaskToGUI(String taskName, String description, String dueDateStr, String priority) throws Exception {
        LocalDateTime dueDateTime;
        try {
            dueDateTime = LocalDateTime.parse(dueDateStr, DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new Exception("Invalid date format. Please use 'YYYY-MM-DD h:mma'.");
        }

        String loggedInUser = userManager.getLoggedInUser();
        if (loggedInUser == null) {
            throw new Exception("No user is logged in.");
        }

        Task task = priority.equalsIgnoreCase("High")
                ? new HighPriorityTask(taskName, description, dueDateTime,priority, "Not Started", loggedInUser)
                : new LowPriorityTask(taskName, description, dueDateTime,priority, "Not Started", loggedInUser);

        tasks.add(task);
        saveTasksToFile();
        saveToDb(loggedInUser,taskName,priority,dueDateTime);
    }

    public boolean removeTaskByName(String taskName) {
        if (userManager.getLoggedInUser() == null) {
            return false;
        }
        boolean removed = tasks.removeIf(task -> task.getTaskName().equalsIgnoreCase(taskName)
                && task.getAssignedUser().equals(userManager.getLoggedInUser()));
        if (removed) {
            saveTasksToFile();
        }
        return removed;
    }

    public boolean updateTaskStatusByName(String taskName, String newStatus) {
        if (userManager.getLoggedInUser() == null) {
            return false;
        }
        for (Task task : tasks) {
            if (task.getTaskName().equalsIgnoreCase(taskName)
                    && task.getAssignedUser().equals(userManager.getLoggedInUser())) {
                task.setStatus(newStatus);
                saveTasksToFile();
                return true;
            }
        }
        return false;
    }

    public User getUserManager() {
        return userManager;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

}
