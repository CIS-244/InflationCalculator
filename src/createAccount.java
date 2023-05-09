import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;

public class createAccount {

    // Constants for connecting to the database and hashing passwords
    private static final String DB_URL = "jdbc:mysql://cis244-prod.c28qsj4v6lea.us-east-2.rds.amazonaws.com:3306/Login";
    private static final String DB_USER = "loginagent";
    private static final String DB_PASSWORD = "cis244";

    // Method to add a new user to the database
    private static boolean addUser(String username, String password, String confirmPassword) {
        // Check if the two password fields match
        if (!password.equals(confirmPassword)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Account Creation Failed");
            alert.setHeaderText(null);
            alert.setContentText("Your passwords do not match. Make sure your passwords match and try again.");
            alert.showAndWait();
            return false;
        }
        // Check if the username or password fields are blank
        if (username.isBlank() || password.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Account Creation Failed");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a username and password.");
            alert.showAndWait();
            return false;
        }
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Check if the username already exists in the database
            PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) AS count FROM users WHERE LOWER(username) = ?");
            checkStmt.setString(1, username.toLowerCase());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt("count") > 0) {
                // The username already exists in the database, return false
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Account Creation Failed");
                alert.setHeaderText(null);
                alert.setContentText("That username is taken. Try a different username.");
                alert.showAndWait();
                return false;
            }
            // Insert the new user into the database
            String hashedPassword = passwordUtils.hashPassword(password,username.toLowerCase());
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)");
            insertStmt.setString(1, username.toLowerCase());
            insertStmt.setString(2, hashedPassword);
            int rowsInserted = insertStmt.executeUpdate();
            // If a row was successfully inserted into the database, return true
            return rowsInserted == 1;
        } catch (SQLException ex) {
            // Print an error message if there was an error adding the user to the database
            System.out.println("Error adding user to database: " + ex.getMessage());
            return false;
        }
    }


    // Method to display the create account scene
    public static void displayCreateAccountScene() {
        try {
            FXMLLoader loader = new FXMLLoader(createAccount.class.getResource("createAccount.fxml"));
            loader.setController(new createAccountController()); // instantiate the controller class
            Parent root = loader.load();
            Scene scene = new Scene(root, 400, 300);
            Stage stage = new Stage();
            stage.setTitle("Account Creation");
            stage.setScene(scene);
            stage.show();

            // Get the create button, error label, username field, password field, and confirm password field from the controller object
            createAccountController controller = loader.getController();
            TextField usernameField = controller.getUsernameField();
            PasswordField passwordField = controller.getPasswordField();
            PasswordField confirmPasswordField = controller.getConfirmPasswordField();
            Button createButton = controller.getCreateButton();
            Button cancelButton = controller.getCancelButton();

            // Add an event handler to the "Create" button that calls the addUser method
            createButton.setOnAction(event -> {
                        String username = usernameField.getText();
                        String password = passwordField.getText();
                        String confirmPassword = confirmPasswordField.getText();
                        if (addUser(username, password, confirmPassword)) {
                            // If the user was successfully added, close the create account window
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Account Creation Successful!");
                            alert.setHeaderText(null);
                            alert.setContentText("Please login with your new account credentials.");
                            alert.showAndWait();
                            stage.close();
                            Login login = new Login();
                            try {
                                login.start(new Stage());
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                cancelButton.setOnAction(event -> {
                        stage.close();
                        Login login = new Login();
                        try {
                            login.start(new Stage());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
            });

        } catch (Exception ex) {
            System.out.println("Error loading create account scene: " + ex.getMessage());
        }
    }
}
