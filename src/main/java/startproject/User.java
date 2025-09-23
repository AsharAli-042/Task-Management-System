package startproject;

import java.io.*;
import java.util.ArrayList;

public class User {
    private ArrayList<String> usernames = new ArrayList<>();
    private ArrayList<String> passwords = new ArrayList<>();
    private String loggedInUser;

    public User() {
        loadUsersFromFile();
    }

    private void loadUsersFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("Users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    usernames.add(parts[0]);
                    passwords.add(parts[1]);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("User file not found. Starting fresh.");
        } catch (IOException e) {
            System.out.println("Error reading user file: " + e.getMessage());
        }
    }

    private void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Users.txt"))) {
            for (int i = 0; i < usernames.size(); i++) {
                writer.write(usernames.get(i) + "," + passwords.get(i));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to user file: " + e.getMessage());
        }
    }

    public boolean register(String username, String password) {
        if (usernames.contains(username)) {
            System.out.println("Username already exists. Please choose a different username.");
            return false;
        }
        usernames.add(username);
        passwords.add(password);
        saveUsersToFile();
        System.out.println("Registration successful!");
        return true;
    }

    public boolean login(String username, String password) {
        for (int i = 0; i < usernames.size(); i++) {
            if (usernames.get(i).equals(username) && passwords.get(i).equals(password)) {
                System.out.println("Login successful! Welcome, " + username);
                loggedInUser = username;
                return true;
            }
        }
        System.out.println("Invalid username or password.");
        return false;
    }

    public void logout() {
        if (loggedInUser != null) {
            System.out.println("User " + loggedInUser + " has logged out.");
            loggedInUser = null;
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    public String getLoggedInUser() {
        return loggedInUser;
    }
}
