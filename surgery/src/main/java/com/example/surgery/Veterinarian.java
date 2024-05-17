package com.example.surgery;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Veterinarian {

    public static List<String> getVets(String type) {
        List<String> vetsList = new ArrayList<>();
        String query = "SELECT veterinary_id, first_name, last_name FROM veterinary WHERE type = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/surgery", "mohammed", "1429015");
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, type);
            try (ResultSet resultSet = statement.executeQuery()) {
                // Store veterinarian names in the list
                while (resultSet.next()) {
                    String veterinaryId = resultSet.getString("veterinary_id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    vetsList.add(veterinaryId + " " + firstName + " " + lastName);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving veterinarians: " + e.getMessage());
        }
        return vetsList;
    }


}
