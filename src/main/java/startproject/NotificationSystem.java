package startproject;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationSystem {
    private SystemTray systemTray;
    private TrayIcon trayIcon;

    public NotificationSystem() {
        if (SystemTray.isSupported()) {
            systemTray = SystemTray.getSystemTray();
            Image icon = Toolkit.getDefaultToolkit().createImage("icon.png"); // Use a valid image path
            trayIcon = new TrayIcon(icon, "Task Notifications");
            trayIcon.setImageAutoSize(true);

            try {
                systemTray.add(trayIcon);
            } catch (AWTException e) {
                System.out.println("Error initializing notification system: " + e.getMessage());
            }
        } else {
            System.out.println("System tray not supported on this platform.");
        }
    }

    public void displayNotification(String title, String message) {
        if (trayIcon != null) {
            trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);
        }
    }

    public void scheduleNotifications(List<Task> tasks, String loggedInUser) {
        Timer timer = new Timer(true); // Run as a daemon thread
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                LocalDateTime now = LocalDateTime.now();

                for (Task task : tasks) {
                    if (task.getAssignedUser().equals(loggedInUser)) {
                        LocalDateTime dueDate = task.getDueDateTime();
                        String taskName = task.getTaskName();

                        if (task.isOverdue()) {
                            displayNotification("Overdue Task Alert", "Task: " + taskName + " is overdue!");
                        } else if (now.plusDays(1).isAfter(dueDate) && !now.isAfter(dueDate)) {
                            displayNotification("Task Due Soon", "Task: " + taskName + "'s deadline is only 1 day away!");
                        } else if (now.plusDays(3).isAfter(dueDate) && !now.plusDays(1).isAfter(dueDate)) {
                            displayNotification("Task Reminder", "Task: " + taskName + "'s deadline is only 3 days away!");
                        }
                    }
                }
            }
        }, 0, 60 * 1000); // Check every minute
    }
}
