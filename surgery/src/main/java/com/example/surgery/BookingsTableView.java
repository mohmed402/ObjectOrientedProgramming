package com.example.surgery;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookingsTableView extends Application {
    private Stage bookingListStageHolder;
    private Stage menuStageHolder;

    @Override
    public void start(Stage primaryStage) {
        showDatabaseTableView(primaryStage);
    }

    public void showDatabaseTableView(Stage primaryStage) {
        menuStageHolder = primaryStage;
        Stage bookingListStage = new Stage();
        bookingListStageHolder = bookingListStage;
        BookingSection bookingSection = new BookingSection();

        Button bookButton = new Button("Make Booking");
        bookButton.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        bookButton.setMinWidth(180);
        bookButton.setOnAction(e ->{
            bookingListStageHolder.close();
            bookingSection.start(menuStageHolder);
        });

        Button checkBookingsButton = new Button("Check bookings");
        checkBookingsButton.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        checkBookingsButton.setMinWidth(180);
        checkBookingsButton.setOnAction(e -> seeBookings());

        Button backButton = new Button("Back");
        backButton.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        backButton.setMinWidth(180);
        backButton.setOnAction(e -> returnPage());

        // Add buttons to a vertical layout
        VBox section1 = new VBox(15, bookButton, checkBookingsButton, backButton);
        section1.setStyle("-fx-background-color: #fc5102; -fx-padding: 30px; -fx-alignment: center;");

        // Create a TableView to display the data
        TableView<String[]> tableView = new TableView<>();

        // Create columns for the TableView
        TableColumn<String[], String> petNameColumn = new TableColumn<>("Pet Name");
        petNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[0]));

        TableColumn<String[], String> breedColumn = new TableColumn<>("Breed");
        breedColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[1]));

        TableColumn<String[], String> ageColumn = new TableColumn<>("Age");
        ageColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[2]));

        TableColumn<String[], String> genderColumn = new TableColumn<>("Gender");
        genderColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[3]));

        TableColumn<String[], String> reasonColumn = new TableColumn<>("Reason");
        reasonColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[4]));

        TableColumn<String[], String> urgencyColumn = new TableColumn<>("Urgency");
        urgencyColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[5]));

        TableColumn<String[], String> customerNameColumn = new TableColumn<>("Customer Name");
        customerNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[7]));

        TableColumn<String[], String> veterinaryColumn = new TableColumn<>("Veterinary");
        veterinaryColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[8]));

        TableColumn<String[], String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[9]));

        TableColumn<String[], String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[10]));

        TableColumn<String[], String> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[6]));

        // Add columns to the TableView
        tableView.getColumns().addAll(petNameColumn, breedColumn, ageColumn, genderColumn, reasonColumn, urgencyColumn, customerNameColumn, veterinaryColumn, dateColumn, timeColumn, locationColumn);

        // Set up database connection and retrieve data
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/surgery", "mohammed", "1429015")) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT b.pet_name, b.breed, b.age, b.gender, b.reason, b.urgency, b.location, u.firstName, u.lastName, v.first_name, v.last_name, c.date, c.time " +
                            "FROM bookings b " +
                            "JOIN calendar c ON b.calender_id = c.calender_id " +
                            "JOIN veterinary v ON b.veterinary_id = v.veterinary_id " +
                            "JOIN users u ON b.user_id = u.id");
            ResultSet resultSet = statement.executeQuery();

            // Populate TableView with data from the database
            ObservableList<String[]> data = FXCollections.observableArrayList();
            while (resultSet.next()) {
                String[] rowData = new String[11]; // Assuming eleven columns in the table
                rowData[0] = resultSet.getString("pet_name");
                rowData[1] = resultSet.getString("breed");
                rowData[2] = resultSet.getString("age");
                rowData[3] = resultSet.getString("gender");
                rowData[4] = resultSet.getString("reason");
                rowData[5] = resultSet.getString("urgency");
                rowData[6] = resultSet.getString("location");
                rowData[7] = resultSet.getString("firstName") + " " + resultSet.getString("lastName"); // customer name
                rowData[8] = resultSet.getString("first_name") + " " + resultSet.getString("last_name"); // veterinary
                rowData[9] = resultSet.getString("date");
                rowData[10] = resultSet.getString("time");
                data.add(rowData);
            }
            tableView.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create a StackPane for the table and the buttons
        StackPane root = new StackPane(new VBox(section1, tableView));
        Scene bookingListScene = new Scene(root, 1050, 800);

        bookingListStage.setTitle("Database Table View");
        bookingListStage.setScene(bookingListScene);
        bookingListStage.show();
        primaryStage.close();
    }


    private void seeBookings() {
        System.out.println("Check bookings button clicked.");
    }

    private void returnPage() {
        bookingListStageHolder.close();
        menuStageHolder.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
