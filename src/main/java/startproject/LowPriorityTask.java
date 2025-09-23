package startproject;
import java.time.LocalDateTime;

public class LowPriorityTask extends Task {

    public LowPriorityTask(String taskName, String description, LocalDateTime dueDateTime,String priority,String status, String assignedUser) {
        super(taskName, description, dueDateTime,"Low",status, assignedUser);
    }

    @Override
    public void displayDetails() {
        System.out.println("Low Priority Task: " + getTaskName());
        System.out.println("Description: " + getDescription());
        System.out.println("Due Date and time: " + getDueDateTime());
        System.out.println("Assigned to: " + getAssignedUser());
        System.out.println("Priority: " + getPriority());
        System.out.println("Status: " + getStatus());
    }
}

