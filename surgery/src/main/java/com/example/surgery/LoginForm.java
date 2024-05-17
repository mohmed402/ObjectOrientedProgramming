package com.example.surgery;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class LoginForm extends Application {

    private TextField usernameField;
    private PasswordField passwordField;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login Form");

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        Label usernameLabel = new Label("Username:");
        GridPane.setConstraints(usernameLabel, 0, 0);
        usernameField = new TextField();
        GridPane.setConstraints(usernameField, 1, 0);

        Label passwordLabel = new Label("Password:");
        GridPane.setConstraints(passwordLabel, 0, 1);
        passwordField = new PasswordField();
        GridPane.setConstraints(passwordField, 1, 1);

        Button submitButton = new Button("Submit");
        GridPane.setConstraints(submitButton, 1, 2);
        submitButton.setPrefWidth(160);
        submitButton.setStyle("-fx-background-color: #FC5102; -fx-text-fill: white;");
        submitButton.setOnAction(e -> handleSubmit(primaryStage));

        gridPane.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, submitButton);

        Scene scene = new Scene(gridPane, 800, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleSubmit(Stage primaryStage) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Authenticate the user
        if (AuthenticationService.authenticate(username, password)) {
            System.out.println("Login successful!");
            // Open another page upon successful login
            openMenuPage(primaryStage);
        } else {
            usernameField.setStyle("-fx-border-color: #ff2222");
            passwordField.setStyle("-fx-border-color: #ff2222");
            System.out.println("Login failed. Incorrect username or password.");
            // You can add code here to display an error message to the user
        }
    }

    private void openMenuPage(Stage primaryStage) {
        // Create a new stage for the Menu page
        Stage menuStage = new Stage();
        BookingSection bookingSection = new BookingSection();
        CustomersSection customersSection = new CustomersSection(menuStage);
        menuStage.setTitle("Menu Page");

        GridPane menuPane = new GridPane();
        menuPane.setAlignment(Pos.CENTER);
        menuPane.setPadding(new Insets(10));
        menuPane.setVgap(10);
        menuPane.setHgap(10);
        menuPane.setStyle("-fx-background-color:#FC5102;");
        Label menuLabel = new Label("Welcome, " + usernameField.getText() + "!");
        menuLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");

        Button customerSectionButton = new Button("Customers");
        GridPane.setConstraints(customerSectionButton, 0, 1);
        customerSectionButton.setPrefWidth(160);
        customerSectionButton.setPrefHeight(60);
        customerSectionButton.setStyle("-fx-background-color: #ffffff; -fx-text-fill: black;");
        customerSectionButton.setOnAction(e -> {customersSection.show(menuStage);});

        Button bookingSectionButton = new Button("Bookings");
        GridPane.setConstraints(bookingSectionButton, 0, 2);
        bookingSectionButton.setPrefWidth(160);
        bookingSectionButton.setPrefHeight(60);
        bookingSectionButton.setStyle("-fx-background-color: #ffffff; -fx-text-fill: black;");
        bookingSectionButton.setOnAction(e -> bookingSection.start(menuStage));

        menuPane.getChildren().addAll(menuLabel, customerSectionButton, bookingSectionButton);
        Scene menuScene = new Scene(menuPane, 800, 800);

        menuStage.setScene(menuScene);
        menuStage.show();
        primaryStage.close();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
