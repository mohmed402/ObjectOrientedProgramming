package com.example.surgery;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

//import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomersSection extends Application {
    private TableView<Customer> tableView;
    private TextField firstNameField;
    private TextField lastNameField;
    private TextField emailField;
    private TextField phoneField;
    private Stage menuStageHolder;
    private Stage customerStageHolder;


    public CustomersSection(Stage menuStageHolder) {
        this.menuStageHolder = menuStageHolder;
    }

    @Override
    public void start(Stage customerStage) {
        customerStage.setTitle("Customer Section");
        customerStageHolder = customerStage;

        // TableView setup
        tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Customer, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Customer, String> firstNameColumn = new TableColumn<>("First Name");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Customer, String> lastNameColumn = new TableColumn<>("Last Name");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Customer, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Customer, String> phoneColumn = new TableColumn<>("Phone");
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        tableView.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn, emailColumn, phoneColumn);

        // Input fields setup
        firstNameField = new TextField();
        firstNameField.setPromptText("First Name");

        lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");

        emailField = new TextField();
        emailField.setPromptText("Email");

        phoneField = new TextField();
        phoneField.setPromptText("Phone");

        Button addButton = new Button("Add");
        addButton.setStyle("-fx-background-color: #FC5102; -fx-text-fill: white;");
        addButton.setOnAction(e -> addData());

        // Add input fields and button to an HBox
        HBox inputBox = new HBox(10, firstNameField, lastNameField, emailField, phoneField, addButton);
        inputBox.setAlignment(Pos.CENTER);

        // Load Data button
        Button loadDataButton = new Button("Load Data");
        loadDataButton.setStyle("-fx-background-color: #FC5102; -fx-text-fill: white;");
        loadDataButton.setOnAction(e -> loadData());

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: black; -fx-text-fill: white;");
        backButton.setPrefWidth(72);
        backButton.setOnAction(e -> returnPage());

        // Layout setup
        VBox vbox = new VBox(tableView, inputBox, loadDataButton, backButton);
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 800, 800);
        customerStage.setScene(scene);
        customerStage.show();
    }

    private void loadData() {
        ObservableList<Customer> data = FXCollections.observableArrayList();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/surgery", "mohammed", "1429015");
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");

                data.add(new Customer(id, firstName, lastName, email, phone));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error occurred while loading data from the database.");
        }

        tableView.setItems(data);
    }

    private void addData() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/surgery", "mohammed", "1429015");
             PreparedStatement statement = connection.prepareStatement("INSERT INTO users (firstName, lastName, email, phone) VALUES (?, ?, ?, ?)")) {

            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, email);
            statement.setString(4, phone);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 1) {
                // Data inserted successfully
                showAlert(Alert.AlertType.INFORMATION, "Success", "Data added successfully.");
                // Clear input fields after adding data
                firstNameField.clear();
                lastNameField.clear();
                emailField.clear();
                phoneField.clear();
                // Reload data to update TableView
                loadData();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add data.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error occurred while adding data to the database.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static class Customer {
        private final SimpleIntegerProperty id;
        private final SimpleStringProperty firstName;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty email;
        private final SimpleStringProperty phone;

        public Customer(int id, String firstName, String lastName, String email, String phone) {
            this.id = new SimpleIntegerProperty(id);
            this.firstName = new SimpleStringProperty(firstName);
            this.lastName = new SimpleStringProperty(lastName);
            this.email = new SimpleStringProperty(email);
            this.phone = new SimpleStringProperty(phone);
        }

        public int getId() {
            return id.get();
        }

        public String getFirstName() {
            return firstName.get();
        }

        public String getLastName() {
            return lastName.get();
        }

        public String getEmail() {
            return email.get();
        }

        public String getPhone() {
            return phone.get();
        }
    }

    public void show(Stage menuStage) {
        menuStageHolder = menuStage;
        System.out.println("Opening customer section...");
        new CustomersSection(menuStageHolder).start(new Stage());
        menuStage.close();
    }

    private void returnPage(){
        customerStageHolder.close();
        menuStageHolder.show();
    }
}
