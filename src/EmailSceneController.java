import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;
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

    public Stage getStage() {
        return stage;
    }

    public void setMatchSearchController(MatchSearchController controller) {
        this.controller = controller;
    }

    @FXML
    private void sendEmail() {
        setStage(stage);
        String recipient = emailField.getText();
        String subject = "Your Shopping List";
        String body = generateEmailBody();

        try {
            HtmlEmail email = new HtmlEmail();
            email.setHostName("smtp.gmail.com");
            email.setSmtpPort(465);
            email.setAuthenticator(new DefaultAuthenticator("inflationcalc244@gmail.com", "zfpoyvjnteodzfel"));
            email.setSSLOnConnect(true);
            email.addTo(recipient);
            email.setFrom("inflationcalc244@gmail.com", "Inflation Calculator");
            email.setSubject(subject);

            email.setHtmlMsg(body);
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
        sb.append("<html><head><title>Grocery List</title><style>" +
                "body {font-family: Helvetica, sans-serif; background-color: #f2f2f2;}" +
                ".container {max-width: 600px; margin: auto; padding: 20px; background-color: #fff; border-radius: 5px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);}" +
                "h1 {font-size: 24px; margin-top: 0; color: #333;}" +
                "table {border-collapse: collapse; width: 100%;}" +
                "td, th {border: 1px solid #ddd; text-align: left; padding: 8px;}" +
                "th {background-color: #f2f2f2;}" +
                ".total {font-size: 14px; font-weight: bold;}" +
                ".before {color: #555;}" +
                ".after {color: #555;}" +
                ".diff {color: #e01b1b;}" +
                "</style></head><body><div class=\"container\"><h1>Grocery List</h1><table><tr><th>Item</th></tr>");
        for (String item : items) {
            sb.append("<tr><td>").append(item).append("</td></tr>");
        }
        sb.append("</table>");
        sb.append("<p class=\"total before\">Total cost today: $").append(df.format(total1)).append("</p>");
        sb.append("<p class=\"total after\">Total cost after a year of inflation at current rate: $").append(df.format(total2)).append("</p>");
        sb.append("<p class=\"total diff\">Difference: $").append(df.format(total3)).append("</p>");
        sb.append("</div></body></html>");
        return sb.toString();
    }
}
