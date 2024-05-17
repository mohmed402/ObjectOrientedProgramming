package com.example.surgery;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Animals {
    String[][] animals = {
            {"Dog", "The MB Surgery, M15 8YT, Manchester", "Small Animal"},
            {"Cat", "The MB Surgery, M15 8YT, Manchester", "Small Animal"},
            {"Rabbit", "The MB Surgery, M15 8YT, Manchester", "Small Animal"},
            {"Hamster", "The MB Surgery, M15 8YT, Manchester", "Small Animal"},
            {"Giraffe", "The MB Zoo, M45 6AR, Manchester", "Zoo Veterinarian"},
            {"Horse", "The MB Zoo, M45 6AR, Manchester", "Equine Veterinarian" },
            {"Elephant", "The MB Zoo, M45 6AR, Manchester", "Zoo Veterinarian"},
            {"Monkey", "The MB Zoo, M45 6AR, Manchester", "Zoo Veterinarian"},
            {"Cow", "The MB Zoo, M45 6AR, Manchester", "Large Animal"},
            {"Hen", "The MB Zoo, M45 6AR, Manchester", "Small Animal"},
            {"Sheep", "The MB Zoo, M45 6AR, Manchester", "Large Animal"},
            {"Leopard", "The MB Zoo, M31 4AG, Manchester", "Surgical Specialist"},
            {"Lion", "The MB Zoo, M31 4AG, Manchester", "Surgical Specialist"},
            {"Fox", "The MB Zoo, M31 4AG, Manchester", "Zoo Veterinarian"},
            {"Bird", "The MB Zoo, M19 1LS, Manchester", "Exotic Animal"},
            {"Eagle", "The MB Zoo, M19 1LS, Manchester", "Exotic Animal"},
            {"Oel", "The MB Zoo, M19 1LS, Manchester", "Exotic Animal"},
            {"hawk", "The MB Zoo, M19 1LS, Manchester", "Exotic Animal"},
    };

    String[] breeds = {
            "Labrador Retriever",
            "German Shepherd",
            "Golden Retriever",
            "Bulldog",
            "Beagle",
            "Poodle",
            "Rottweiler",
            "Yorkshire Terrier",
            "Boxer",
            "Dachshund",
            "Siberian Husky",
            "Not listed"
    };

    public String getLocation(String theAnimal) {
        for (int i = 0; i < animals.length; i++) {
            if (animals[i][0].equals(theAnimal)) {
                System.out.println("Location: " + animals[i][1]);
                return animals[i][1];
            }
        }
        System.out.println("Location not found for " + theAnimal);
        return null;
    }

//    public static List<String> getVets(String type) {
//        List<String> vetsList = new ArrayList<>();
//        String query = "SELECT * FROM veterinary WHERE type = ?";
//
//        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/surgery", "mohammed", "1429015");
//             PreparedStatement statement = connection.prepareStatement(query)) {
//
//            statement.setString(1, type);
//            ResultSet resultSet = statement.executeQuery();
//
//            // Store veterinarian names in the list
//            while (resultSet.next()) {
//                String veterinaryId = resultSet.getString("veterinary_id");
//                String fName =  resultSet.getString("first_name");
//                String lName =  resultSet.getString("last_name");
//                vetsList.add(veterinaryId + " " + fName + " " + lName);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return vetsList;
//    }



    public String getType(String theAnimal) {
        for (int i = 0; i < animals.length; i++) {
            if (animals[i][0].equals(theAnimal)) {
                System.out.println("Type: " + animals[i][2]);
                return animals[i][2];
            }
        }
        System.out.println("Type not found for " + theAnimal);
        return null;
    }

//    public static void main(String[] args) {
//    }
}
