package com.example.surgery;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class Time {
    static String[] timing = {"10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00"};


    public static String[] checkTime(String date) {
        String[] bookedTimes = {}; // Initialize an empty array to store booked times
        String query = "SELECT * FROM calendar WHERE date = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/surgery", "mohammed", "1429015");
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, date);
            ResultSet resultSet = statement.executeQuery();

            // Store booked times in the bookedTimes array
            while (resultSet.next()) {
                String time = resultSet.getString("time");
                bookedTimes = Arrays.copyOf(bookedTimes, bookedTimes.length + 1);
                bookedTimes[bookedTimes.length - 1] = time;
                System.out.println(Arrays.toString(bookedTimes));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create a new array to store available times
        String[] availableTimes = new String[timing.length - bookedTimes.length];
        int index = 0;

        // Populate the availableTimes array with times that are not booked
        for (String time : timing) {
            if (!Arrays.asList(bookedTimes).contains(time)) {
                availableTimes[index++] = time;
            }
        }
        String[] ms = {};
        System.out.println(availableTimes.length);
        // Print available times for testing
        System.out.println("Available times: " + availableTimes.length);
        return availableTimes.length == 0 ? ms : availableTimes;

    }


    public static int insertDate(String date, String time) {
        String query = "INSERT INTO calendar (calender_id, date, time) VALUES (?, ?, ?)";
        int dateId = 0;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/surgery", "mohammed", "1429015");
             PreparedStatement statement = connection.prepareStatement(query)) {

            int y = Integer.parseInt(date.substring(2, 4));
            int m = Integer.parseInt(date.substring(5, 7));
            int d = Integer.parseInt(date.substring(8, 10));
            int h = Integer.parseInt(String.valueOf(time.charAt(1)));
            int min = Integer.parseInt(String.valueOf(time.charAt(3)));
            String timeChosen = String.valueOf(h) + min;
            dateId = Integer.parseInt(y + m + d + String.valueOf(y) + timeChosen);


            statement.setInt(1, dateId);
            statement.setString(2, date);
            statement.setString(3, time);


            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("time booked successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dateId;
    }

    public static void main(String[] args) {
        Time timer = new Time();
        System.out.println(Arrays.toString(checkTime("2024-05-14")));

//        insertDate("2024-05-28", "12:00");
    }

}
