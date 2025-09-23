package startproject;

import javax.swing.*;
import java.awt.*;

public class Menu1GUI {
    private User userManager;
    private Dashboard dashboard;

    public Menu1GUI(User userManager, Dashboard dashboard) {
        this.userManager = userManager;
        this.dashboard = dashboard;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Task Management System - Login Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout(10, 10));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(75, 110, 175)); // Blue header
        JLabel headerLabel = new JLabel("Welcome to Task Management System", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        frame.add(headerPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 240, 240)); // Light gray background
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JButton loginButton = createStyledButton("Login");
        JButton registerButton = createStyledButton("Register");
        JButton exitButton = createStyledButton("Exit");

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(loginButton, gbc);

        gbc.gridy = 1;
        mainPanel.add(registerButton, gbc);

        gbc.gridy = 2;
        mainPanel.add(exitButton, gbc);

        frame.add(mainPanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(75, 110, 175));
        JLabel footerLabel = new JLabel("\u00A9 2024 Task Management System", JLabel.CENTER);
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        footerLabel.setForeground(Color.WHITE);
        footerPanel.add(footerLabel);
        frame.add(footerPanel, BorderLayout.SOUTH);

        loginButton.addActionListener(e -> loginDialog(frame));
        registerButton.addActionListener(e -> registerDialog());
        exitButton.addActionListener(e -> System.exit(0));

        frame.setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBackground(new Color(75, 110, 175)); // Light blue
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 63, 65)),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        return button;
    }

    private void loginDialog(JFrame frame) {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        Object[] message = {
                "Username:", usernameField,
                "Password:", passwordField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Login", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (userManager.login(username, password)) {
                dashboard.startNotifications();
                JOptionPane.showMessageDialog(null, "Login successful! Welcome, " + username);

                frame.dispose(); // Close the current frame

                SwingUtilities.invokeLater(() -> new Menu2GUI(dashboard));
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void registerDialog() {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        Object[] message = {
                "Username:", usernameField,
                "Password:", passwordField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Register", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (userManager.register(username, password)) {
                JOptionPane.showMessageDialog(null, "Registration successful!");
            } else {
                JOptionPane.showMessageDialog(null, "Username already exists. Please choose a different username.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
