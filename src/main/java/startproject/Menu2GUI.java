package startproject;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class Menu2GUI {
    private Dashboard dashboard;
    private JPanel contentPanel;

    public Menu2GUI(Dashboard dashboard) {
        this.dashboard = dashboard;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Task Management - Menu 2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLayout(new BorderLayout(10, 10));

        JPanel navPanel = new JPanel();
        navPanel.setLayout(new GridLayout(5, 1, 10, 10));
        navPanel.setBackground(new Color(60, 63, 65));
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton viewDashboardButton = createStyledButton("View Dashboard");
        JButton addTaskButton = createStyledButton("Add Task");
        JButton removeTaskButton = createStyledButton("Remove Task");
        JButton changeTaskStatusButton = createStyledButton("Change Task Status");
        JButton logoutButton = createStyledButton("Logout");

        navPanel.add(viewDashboardButton);
        navPanel.add(addTaskButton);
        navPanel.add(removeTaskButton);
        navPanel.add(changeTaskStatusButton);
        navPanel.add(logoutButton);

        contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        frame.add(navPanel, BorderLayout.WEST);
        frame.add(contentPanel, BorderLayout.CENTER);

        viewDashboardButton.addActionListener(e -> displayDashboard());
        addTaskButton.addActionListener(e -> displayAddTaskUI());
        removeTaskButton.addActionListener(e -> displayRemoveTaskUI());
        changeTaskStatusButton.addActionListener(e -> displayChangeTaskStatusUI());
        logoutButton.addActionListener(e -> {
            dashboard.getUserManager().logout();
            JOptionPane.showMessageDialog(frame, "Logged out successfully.");
            frame.dispose();
            SwingUtilities.invokeLater(() -> new Menu1GUI(dashboard.getUserManager(), dashboard));
        });

        frame.setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(new Color(75, 110, 175));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 63, 65)),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        return button;
    }

    private void displayDashboard() {
        contentPanel.removeAll();

        JTextArea dashboardTextArea = new JTextArea();
        dashboardTextArea.setEditable(false);
        dashboardTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        dashboardTextArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        StringBuilder dashboardContent = new StringBuilder();
        dashboard.displayDashboardToGUI(dashboardContent);
        dashboardTextArea.setText(dashboardContent.toString());

        JScrollPane scrollPane = new JScrollPane(dashboardTextArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        contentPanel.add(scrollPane);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void displayAddTaskUI() {
        contentPanel.removeAll();

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Add Task"));
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel taskNameLabel = new JLabel("Task Name:");
        JTextField taskNameField = new JTextField(15);
        JLabel descriptionLabel = new JLabel("Description:");
        JTextField descriptionField = new JTextField(15);
        JLabel dueDateLabel = new JLabel("Due Date (YYYY-MM-DD h:mma):");
        JTextField dueDateField = new JTextField(15);
        JLabel priorityLabel = new JLabel("Priority:");
        JComboBox<String> priorityComboBox = new JComboBox<>(new String[]{"High", "Low"});

        JButton submitButton = createStyledButton("Add Task");
        submitButton.addActionListener(e -> {
            String taskName = taskNameField.getText();
            String description = descriptionField.getText();
            String dueDate = dueDateField.getText();
            String priority = (String) priorityComboBox.getSelectedItem();

            try {
                dashboard.addTaskToGUI(taskName, description, dueDate, priority);
                JOptionPane.showMessageDialog(null, "Task added successfully!");
                displayDashboard();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Add Task Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(taskNameLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(taskNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(descriptionLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(descriptionField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(dueDateLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(dueDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(priorityLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(priorityComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(submitButton, gbc);

        contentPanel.add(formPanel);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void displayRemoveTaskUI() {
        contentPanel.removeAll();

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Remove Task"));
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel taskNameLabel = new JLabel("Task Name:");
        JComboBox<String> taskNameDropdown = new JComboBox<>(getTaskNames().toArray(new String[0]));
        JButton submitButton = createStyledButton("Remove Task");

        submitButton.addActionListener(e -> {
            String taskName = (String) taskNameDropdown.getSelectedItem();
            if (taskName != null) {
                boolean removed = dashboard.removeTaskByName(taskName);
                if (removed) {
                    JOptionPane.showMessageDialog(null, "Task removed successfully!");
                    displayDashboard();
                } else {
                    JOptionPane.showMessageDialog(null, "Task not found or you do not have permission to remove it.", "Remove Task Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(taskNameLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(taskNameDropdown, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(submitButton, gbc);

        contentPanel.add(formPanel);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void displayChangeTaskStatusUI() {
        contentPanel.removeAll();

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Change Task Status"));
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel taskNameLabel = new JLabel("Task Name:");
        JComboBox<String> taskNameDropdown = new JComboBox<>(getTaskNames().toArray(new String[0]));
        JLabel newStatusLabel = new JLabel("New Status:");
        JComboBox<String> newStatusDropdown = new JComboBox<>(new String[]{"Not Started", "In Progress", "Completed", "Overdue"});
        JButton submitButton = createStyledButton("Change Status");

        submitButton.addActionListener(e -> {
            String taskName = (String) taskNameDropdown.getSelectedItem();
            String newStatus = (String) newStatusDropdown.getSelectedItem();
            if (taskName != null && newStatus != null) {
                boolean updated = dashboard.updateTaskStatusByName(taskName, newStatus);
                if (updated) {
                    JOptionPane.showMessageDialog(null, "Task status updated successfully!");
                    displayDashboard();
                } else {
                    JOptionPane.showMessageDialog(null, "Task not found or you do not have permission to update it.", "Update Task Status Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(taskNameLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(taskNameDropdown, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(newStatusLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(newStatusDropdown, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(submitButton, gbc);

        contentPanel.add(formPanel);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private List<String> getTaskNames() {
        return dashboard.getTasks().stream()
                .filter(task -> task.getAssignedUser().equals(dashboard.getUserManager().getLoggedInUser()))
                .map(Task::getTaskName)
                .collect(Collectors.toList());
    }

}
