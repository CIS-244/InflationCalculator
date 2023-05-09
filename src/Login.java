import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.InputStream;
import java.sql.*;

public class Login extends Application {

    // Constants for connecting to the database and hashing passwords
    private static final String DB_URL = "jdbc:mysql://cis244-prod.c28qsj4v6lea.us-east-2.rds.amazonaws.com:3306/Login";
    private static final String DB_USER = "loginagent";
    private static final String DB_PASSWORD = "cis244";

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file for the login screen and create a new instance of the LoginController
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        LoginController controller = new LoginController();

        // Set the LoginController as the controller for the FXML file and load the root node
        loader.setController(controller);
        Parent root = loader.load();
        
        // Add the stylesheet to the list of stylesheets for the entire application
        String stylesheet = getClass().getResource("stylesheet.css").toExternalForm();
        Application.setUserAgentStylesheet(stylesheet);
        // Set the title and size of the login screen and show it
        primaryStage.setTitle("Inflation Calculator");
        primaryStage.setScene(new Scene(root, 450, 450));
        // Set the application icon
        try (InputStream iconStream = getClass().getResourceAsStream("/Logo_CIS244.png")) {
            if (iconStream != null) {
                primaryStage.getIcons().add(new Image(iconStream));
            }
        }
        primaryStage.show();

        // Handle the login button click event
        controller.getLoginButton().setOnAction(event -> {
            String username = controller.getUsernameField().getText();
            String password = controller.getPasswordField().getText();
            if (isValidUser(username, password)) {
                // Display an information dialog indicating a successful login
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Login Successful");
                alert.setHeaderText(null);
                alert.setContentText("Welcome, " + username.toUpperCase() + "!");
                alert.showAndWait();

                // Launch the MatchSearch application
                MatchSearch matchSearch = new MatchSearch();
                try {
                    matchSearch.start(new Stage());
                    primaryStage.hide();
                } catch (Exception e) {
                    System.out.println("Error launching Inflation Calculator" + e.getMessage());
                }
            } else {
                // Display an error dialog indicating an invalid login
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText(null);
                alert.setContentText("Invalid username or password. Please try again.");
                alert.showAndWait();
            }
        });

        // Handle the create account button click event
        controller.getCreateAccountButton().setOnAction(event -> {

            // Launch the create account scene
            createAccount createAccount = new createAccount();
            try {
                createAccount.displayCreateAccountScene();
            } catch (Exception e) {
                System.out.println("Cannot launch account creation" + e.getMessage());
            }
             primaryStage.close();
            });

        }


    // Method to validate a user's credentials
    private boolean isValidUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            PreparedStatement stmt = conn.prepareStatement("SELECT password FROM users WHERE username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                String inputHashedPassword = passwordUtils.hashPassword(password,username.toLowerCase());
                // Compare the hashed password from the database with the hashed input password
                return hashedPassword.equals(inputHashedPassword);
            } else {
                // If the username does not exist in the database, return false
                return false;
            }
        } catch (SQLException ex) {
            // Print an error message if there was an error validating the user's credentials
            System.out.println("Error validating user credentials: " + ex.getMessage());
            return false;
        }
    }
}
