package com.example.surgery;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthenticationService {

    public static boolean authenticate(String username, String password) {
        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/surgery";
        String dbUsername = "mohammed";
        String dbPassword = "1429015";

        // SQL query to retrieve user credentials from the database
        String sql = "SELECT COUNT(*) FROM account WHERE username = ? AND password = ?";

        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set parameters for the SQL query
            statement.setString(1, username);
            statement.setString(2, password);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            // If the query returns a count greater than 0, the user is authenticated
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // If an exception occurs or no matching user is found in the database, authentication fails
        return false;
    }
}
