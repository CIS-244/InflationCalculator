import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class createAccountController extends AnchorPane {

        @FXML
        private TextField usernameField;

        @FXML
        private PasswordField passwordField;

        @FXML
        private PasswordField confirmPasswordField;

        @FXML
        private Button createButton;
        @FXML
        private Button cancelButton;


        public TextField getUsernameField() {
                return usernameField;
        }
        public PasswordField getPasswordField() {
                return passwordField;
        }
        public PasswordField getConfirmPasswordField() {
                return confirmPasswordField;
        }
        public Button getCreateButton() {
                return createButton;
        }
        public Button getCancelButton() {return cancelButton;}
}