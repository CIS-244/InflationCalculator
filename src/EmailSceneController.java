import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class EmailSceneController {
    @FXML
    private TextField emailField;

    private Stage stage;
    private MatchSearchController controller;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage(){
        return stage;
    }
    public void setMatchSearchController(MatchSearchController controller) {
        this.controller = controller;
    }

    @FXML
    private void sendEmail() {
        setStage(stage);
        String recipient = emailField.getText();
        String subject = "Your shopping list";
        String body = generateEmailBody();

        Email email = new SimpleEmail();
        email.setHostName("smtp.gmail.com");
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator("inflationcalc244@gmail.com", "zfpoyvjnteodzfel"));
        email.setSSLOnConnect(true);

        try {
            email.setFrom("inflationcalc244@gmail.com");
            email.setSubject(subject);
            email.setMsg(body);
            email.addTo(recipient);
            email.send();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Email sent");
            alert.setHeaderText(null);
            alert.setContentText("Your shopping list has been sent to " + recipient + ".");
            alert.showAndWait();
            closeDialog();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error sending email");
            alert.setHeaderText(null);
            alert.setContentText("There was an error sending your email: " + ex.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void closeDialog() {
        stage.close();
    }

    private String generateEmailBody() {
        ArrayList<String> items = new ArrayList<>((controller.getSelectedList().getItems()));
        double total1 = Double.parseDouble(controller.getTotalValueLabel1().getText());
        double total2 = Double.parseDouble(controller.getTotalValueLabel2().getText());
        double total3 = Double.parseDouble(controller.getTotalValueLabel3().getText());
        DecimalFormat df = new DecimalFormat("#.00");

        StringBuilder sb = new StringBuilder();
        sb.append("Here is your shopping list:\n\n");
        for (String item : items) {
            sb.append("- ").append(item).append("\n");
        }
        sb.append("\n");
        sb.append("Total cost before inflation: $").append(df.format(total1)).append("\n");
        sb.append("Total cost after inflation: $").append(df.format(total2)).append("\n");
        sb.append("Inflation-adjusted cost: $").append(df.format(total3)).append("\n");

        return sb.toString();
    }

}
