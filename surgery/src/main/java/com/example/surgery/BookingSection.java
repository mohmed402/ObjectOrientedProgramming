package com.example.surgery;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class BookingSection extends Application {
    private Stage menuStageHolder;
    private Stage bookingStageHolder;
    @Override
    public void start(Stage primaryStage) {
        // Create a VBox as the root node
        Stage bookingStage = new Stage();
        VBox root = new VBox();
        Animals animals = new Animals();
        DatabaseTableView bookingList = new DatabaseTableView();

        bookingStageHolder = bookingStage;
        menuStageHolder = primaryStage;

        Button bookButton = new Button("Make Booking");
        bookButton.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        bookButton.setMinWidth(180);
        bookButton.setOnAction(e -> makeBooking());

        Button checkBookingsButton = new Button("Check bookings");
        checkBookingsButton.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        checkBookingsButton.setMinWidth(180);
        checkBookingsButton.setOnAction(e -> bookingList.start(menuStageHolder));

        Button backButton = new Button("Back");
        backButton.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        backButton.setMinWidth(180);
        backButton.setOnAction(e -> returnPage());


        // Add buttons to a vertical layout
        VBox section1 = new VBox(15, bookButton, checkBookingsButton, backButton);
        section1.setStyle("-fx-background-color: #fc5102; -fx-padding: 30px; -fx-alignment: center;");

        // Create the second section (an empty table in this case)
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setStyle("-fx-alignment: center;");
        grid.setPadding(new Insets(20));


        Label nameLabel = new Label("Customer Name:");
        ComboBox<String> customerComboBox = new ComboBox<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/surgery", "mohammed", "1429015");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM users")) {

            // Process the data and populate the ComboBox

            while (resultSet.next()) {
                String userId = resultSet.getString("id");
                String fName = resultSet.getString("firstName");
                String lName = resultSet.getString("lastName");
                String fullName = userId + " " + fName + " " + lName;

                customerComboBox.getItems().add(fullName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        customerComboBox.setMinWidth(220);
        grid.add(nameLabel, 0, 0);
        grid.add(customerComboBox, 1, 0);


        Label petNameLabel = new Label("Animal Name:");
        TextField petNameField = new TextField();
        grid.add(petNameLabel, 0, 1);
        grid.add(petNameField, 1, 1);

        Label ageLabel = new Label("Age:");
        TextField ageField = new TextField();
        grid.add(ageLabel, 0, 2);
        grid.add(ageField, 1, 2);

        Label reasonLabel = new Label("Reason:");
        TextField reasonField = new TextField();
        grid.add(reasonLabel, 0, 3);
        grid.add(reasonField, 1, 3);

        Label genderLabel = new Label("Gender:");
        RadioButton maleRadioButton = new RadioButton("Male");
        RadioButton femaleRadioButton = new RadioButton("Female");
        radioCreater(grid, genderLabel, maleRadioButton, femaleRadioButton, 4);

        Label animalLabel = new Label("Animal:");
        ComboBox<String> animalComboBox = new ComboBox<>();
        for (String[] animal : animals.animals) {
            animalComboBox.getItems().add(animal[0]);
        }
        animalComboBox.setMinWidth(220);
        animalComboBox.setOnAction(e ->{

        });
        grid.add(animalLabel, 0, 5);
        grid.add(animalComboBox, 1, 5);

        Label breedLabel = new Label("Breed:");
        ComboBox<String> breedComboBox = new ComboBox<>();
        breedComboBox.getItems().addAll(animals.breeds);
        breedComboBox.setMinWidth(220);
        grid.add(breedLabel, 0, 6);
        grid.add(breedComboBox, 1, 6);

        Label urgencyLabel = new Label("Urgency:");
        RadioButton emergencyRadioButton = new RadioButton("Emergency");
        RadioButton checkUpRadioButton = new RadioButton("Check-Up");
        radioCreater(grid, urgencyLabel, emergencyRadioButton, checkUpRadioButton, 7);

//work

        DatePicker datePicker = new DatePicker();
        Label dateLabel = new Label("Date:");
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                DayOfWeek day = date.getDayOfWeek();
                if (day == DayOfWeek.FRIDAY || day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc8a4;"); // Change the color of disabled dates (optional)
                }
            }
        });
        datePicker.setMinWidth(220);
        datePicker.setPromptText("Select a date");
        grid.add(dateLabel, 0, 8);
        grid.add(datePicker, 1, 8);


        Label timeLabel = new Label("Time:");
        ComboBox<String> timeComboBox = new ComboBox<>();
        timeComboBox.getItems().addAll(Time.timing);
        timeComboBox.setMinWidth(220);

        datePicker.setOnAction(e -> {
            String dateToCheck = String.valueOf(datePicker.getValue());
            String[] availableTime = Time.checkTime(dateToCheck);
            if (availableTime != null && availableTime.length > 0) {
                timeComboBox.getItems().clear();
                timeComboBox.getItems().addAll(availableTime);
            }else {
                timeComboBox.getItems().clear();
                timeComboBox.getItems().add("No bookings available");
                timeComboBox.getSelectionModel().clearSelection();
            }
        });

        grid.add(timeLabel, 0, 9);
        grid.add(timeComboBox, 1, 9);


        Label locationLabel = new Label("Location:");
        Label locationText = new Label("");
        locationText.setMinWidth(220);
        grid.add(locationLabel, 0, 10);
        grid.add(locationText, 1, 10);


        Label vetLabel = new Label("Veterinarian:");
        ComboBox<String> vetComboBox = new ComboBox<>();
        vetComboBox.setMinWidth(220);

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/surgery", "mohammed", "1429015");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM veterinary")) {

            // Process the data and populate the ComboBox
            while (resultSet.next()) {
                String veterinaryId = resultSet.getString("veterinary_id");
                String fName =  resultSet.getString("first_name");
                String lName =  resultSet.getString("last_name");
                vetComboBox.getItems().add(veterinaryId + " " + fName + " " + lName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        grid.add(vetLabel, 0, 11);
        grid.add(vetComboBox, 1, 11);

        animalComboBox.setOnAction(event -> {
            String selectedAnimal = animalComboBox.getValue();
            String location = animals.getLocation(selectedAnimal);
            locationText.setText(location);
            String type = animals.getType(selectedAnimal);
            vetComboBox.getItems().clear();
            vetComboBox.getItems().addAll(Veterinarian.getVets(type));

            System.out.println(type);
        });

        // Add a submit button
        Button submitButton = new Button("Submit");
        submitButton.setMinWidth(220);
        submitButton.setStyle("-fx-background-color: #fc5102; -fx-text-fill: white");
        submitButton.setOnAction(e ->{
                    saveBooking(String.valueOf(datePicker.getValue()),  animalComboBox.getValue(), petNameField.getText(), breedComboBox.getValue(), ageField.getText(), String.valueOf(maleRadioButton.isSelected()), customerComboBox.getValue(), reasonField.getText(), String.valueOf(emergencyRadioButton.isSelected()), String.valueOf(timeComboBox.getValue()), vetComboBox.getValue(),locationText.getText());
                    clearInputs(datePicker, animalComboBox, petNameField, breedComboBox, ageField, customerComboBox, reasonField, timeComboBox, vetComboBox);
                });



        grid.add(submitButton, 1, 12);
        StackPane section2 = new StackPane(grid);
        section2.setStyle("-fx-background-color: #f0f0f0; -fx-width: 80%; -fx-padding: 20px;");

        // Add sections to the root VBox
        root.getChildren().addAll(section1, section2);

        // Create the scene
        Scene bookingScene = new Scene(root, 800, 800);

        // Set the scene and show the stage
        bookingStage.setScene(bookingScene);
        bookingStage.setTitle("booking Page");
        bookingStage.show();
        primaryStage.close();


    }

    private void radioCreater(GridPane grid, Label genderLabel, RadioButton emergencyRadioButton, RadioButton checkUpRadioButton, int index) {
        ToggleGroup urgencyGroup = new ToggleGroup();
        emergencyRadioButton.setToggleGroup(urgencyGroup);
        checkUpRadioButton.setToggleGroup(urgencyGroup);
        HBox urgencyBox = new HBox(10, emergencyRadioButton, checkUpRadioButton);
        grid.add(genderLabel, 0, index);
        grid.add(urgencyBox, 1, index);
    }

    private void returnPage() {
        bookingStageHolder.close();
        menuStageHolder.show();
    }

    // Event handler for Make Booking button
    private void makeBooking() {
        System.out.println("Make Booking button clicked.");
    }



    public static void saveBooking(String datePicker, String animalComboBox, String petNameField, String breedComboBox, String ageField, String maleRadioButton, String customerName , String reasonField,
                                   String emergencyRadioButton, String timeComboBox, String vetName, String locationText) {


         int dateId = Time.insertDate(datePicker, timeComboBox);
        String genderRadioButton = maleRadioButton.equals("true") ? "Male" : "Female";
        String urgencyRadioButton = emergencyRadioButton.equals("true") ? "Emergency" : "Check-Up";
//        int customerSelectedIndex = Integer.parseInt(customerComboBox);
        int userId = Integer.parseInt(customerName.split(" ")[0]);
        int vetId = Integer.parseInt(vetName.split(" ")[0]);
        System.out.println(dateId);

        String sql = "INSERT INTO bookings (calender_id, pet, pet_name, breed, age, gender, user_id, reason, urgency, veterinary_id, location) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/surgery", "mohammed", "1429015");
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, dateId);
            statement.setString(2, animalComboBox);
            statement.setString(3, petNameField);
            statement.setString(4, breedComboBox);
            statement.setInt(5, Integer.parseInt(ageField));
            statement.setString(6, genderRadioButton);
            statement.setInt(7, userId);
            statement.setString(8, reasonField);
            statement.setString(9, urgencyRadioButton);
            statement.setInt(10, vetId);
            statement.setString(11, locationText);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Booking inserted successfully!");
                showSuccessMessage("Booking successfully inserted!");
                BookingSection bookingSection= new BookingSection();


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void showSuccessMessage(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void clearInputs(DatePicker datePicker, ComboBox<String> animalComboBox, TextField petNameField, ComboBox<String> breedComboBox, TextField ageField, ComboBox<String> customerComboBox, TextField reasonField, ComboBox<String> timeComboBox, ComboBox<String> vetComboBox) {
        datePicker.setValue(null);
        animalComboBox.getSelectionModel().clearSelection();
        petNameField.clear();
        breedComboBox.getSelectionModel().clearSelection();
        ageField.clear();
        customerComboBox.getSelectionModel().clearSelection();
        reasonField.clear();
        timeComboBox.getSelectionModel().clearSelection();
        vetComboBox.getSelectionModel().clearSelection();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
