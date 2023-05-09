import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class MatchSearchController {

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private ListView<String> resultsList;

    @FXML
    private Label priceValueLabel;

    @FXML
    private Button addButton;

    @FXML
    private ListView<String> selectedList;
    
    @FXML
    private Label totalValueLabel3;
    
    @FXML
    private Label totalValueLabel2;
    
    @FXML
    private Label totalValueLabel1;
    
    @FXML
    private Button removeButton;

    @FXML
    private Button emailMe;

    @FXML
    private Label inflationRateLabel;

    @FXML
    private Text inflationRateText;

    public TextField getSearchField() {
        return searchField;
    }

    public Button getSearchButton() {
        return searchButton;
    }

    public ListView<String> getResultsList() {
        return resultsList;
    }

    public Label getPriceValueLabel() {
        return priceValueLabel;
    }

    public Button getAddButton() {
        return addButton;
    }

    public ListView<String> getSelectedList() {
        return selectedList;
    }
    public Label getTotalValueLabel3( ) {
    	return totalValueLabel3;
    }

    public Label getTotalValueLabel2() {
        return totalValueLabel2;
    }
    public Label getTotalValueLabel1() {
    	return totalValueLabel1;
    }

    public Button getRemoveButton() {
        return removeButton;
    }
    public Button getEmailMe() {
        return emailMe;
    }

    public Label getInflationValueLabel() {
        return inflationRateLabel;
    }
    public Text getInflationRateText() {
        return inflationRateText;
    }

}
