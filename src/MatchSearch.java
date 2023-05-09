import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

//import InflationAPI.ArrayList;



public class MatchSearch extends Application {

    // Database connection information
    private static final String DB_URL = "jdbc:mysql://cis244-prod.c28qsj4v6lea.us-east-2.rds.amazonaws.com:3306/Pricing Data";
    private static final String DB_USER = "Calculator";
    private static final String DB_PASSWORD = "cis244";

    // Instance variables for the inflation rate and database connection the original data
    private Connection conn;
    private final  ArrayList<InflationAPI.DataEntry> data;
    {
        // Initialize data entry arraylist using InflationAPI's API call
        try {
        	 data = InflationAPI.APIcall();
        	
        	       } catch (IOException e) {
            // If there is an error, throw a runtime exception
            throw new RuntimeException(e);
        }
    }
    private final double inflationRate;
    {
        inflationRate = 1 + InflationAPI.InflationCalc(data);
    }
    

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MatchSearch.fxml"));
        MatchSearchController controller = new MatchSearchController();
        loader.setController(controller);
        Parent root = loader.load();

        //Add css stylesheet
        root.getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Inflation Calculator");
        stage.show();

        // Open the database connection
        conn = DriverManager.getConnection(DB_URL,
                DB_USER,
                DB_PASSWORD);

        // Set up event listeners for the GUI buttons
        controller.getSearchButton().setOnAction(event -> {
            String searchTerm = controller.getSearchField().getText();
            ArrayList<String> results = searchDatabase(searchTerm);
            controller.getResultsList().getItems().clear();
            controller.getResultsList().getItems().addAll(results);
            controller.getPriceValueLabel().setText("");
            controller.getAddButton().setDisable(true);
        });

        controller.getResultsList().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                controller.getPriceValueLabel().setText(getPrice(newValue));
                controller.getAddButton().setDisable(false);
            }
        });

        controller.getAddButton().setOnAction(event -> {
            String selectedValue = controller.getResultsList().getSelectionModel().getSelectedItem();
            double selectedPrice = Double.parseDouble(getPrice(selectedValue));
            double total1 = Double.parseDouble(controller.getTotalValueLabel1().getText());
            double total2 = Double.parseDouble(controller.getTotalValueLabel2().getText());
            double total3 = Double.parseDouble(controller.getTotalValueLabel3().getText());
            total1 += selectedPrice;
            total2 += selectedPrice * inflationRate;
            total3 = total2 - total1;
            controller.getTotalValueLabel1().setText(String.format("%.2f", total1));
            controller.getTotalValueLabel2().setText(String.format("%.2f", total2));
            controller.getTotalValueLabel3().setText(String.format("%.2f", total3));
            controller.getSelectedList().getItems().add(selectedValue);
        });

        controller.getRemoveButton().setOnMouseClicked(event -> {
            String selectedValue = controller.getSelectedList().getSelectionModel().getSelectedItem();
            if (selectedValue != null) {
                double selectedPrice = Double.parseDouble(getPrice(selectedValue));
                double total1 = Double.parseDouble(controller.getTotalValueLabel1().getText());
                double total2 = Double.parseDouble(controller.getTotalValueLabel2().getText());
                double total3 = Double.parseDouble(controller.getTotalValueLabel3().getText());
                total1 -= selectedPrice;
                total2 -= selectedPrice * inflationRate;
                total3 = total2 - total1;
                controller.getTotalValueLabel3().setText(String.format("%.2f", total3));
                controller.getTotalValueLabel2().setText(String.format("%.2f", total2));
                controller.getTotalValueLabel1().setText(String.format("%.2f", total1));
                
                controller.getSelectedList().getItems().remove(selectedValue);
            }
        });
    }

    private ArrayList<String> searchDatabase(String searchTerm) {
        ArrayList<String> results = new ArrayList<>();
        try {
            // Query the database for products matching the search term
            String query = "SELECT * FROM WMT_Grocery_202209 WHERE PRODUCT_NAME LIKE ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                // Add each matching product to the results list
                String name = rs.getString("PRODUCT_NAME");
                String price = rs.getString("PRICE_RETAIL");
                String product = name + " ($" + price + ")";
                if (!results.contains(product)) {
                    results.add(product);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error searching database: " + ex.getMessage());
        }
        return results;
    }


    private String getPrice(String value) {
        String[] parts = value.split("\\(");
        String name = parts[0].trim();
        String query = "SELECT PRICE_RETAIL FROM WMT_Grocery_202209 WHERE PRODUCT_NAME = ?";
        try {
            // Query the database for the price of a specific product
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("PRICE_RETAIL");
            }
        } catch (SQLException ex) {
            System.out.println("Error getting price from database: " + ex.getMessage());
        }
        return "";
    }
}



