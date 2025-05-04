package cqu.wis.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 *
 * @author Ayush Bhandari S12157470
 */

public class QueryController{

    @FXML 
    private TextField distilleryField;
    
    @FXML 
    private TextField ageField;
    
    @FXML 
    private TextField regionField;
    
    @FXML 
    private TextField priceField;
    
    @FXML 
    private TextField minAgeField;
    
    @FXML 
    private TextField maxAgeField;
    
    @FXML 
    private TextField regionQueryField;
    
    @FXML 
    private TextArea messageText;
    
    @FXML 
    private Button previousButton;
    
    @FXML 
    private Button nextButton;
    
    @FXML 
    private Button allMaltsButton;
    
    @FXML 
    private Button regionMaltsButton;
    
    @FXML 
    private Button ageRangeMaltsButton;
    
    @FXML 
    private Button clearButton;
    
    @FXML 
    private Button exitButton;
    
    @FXML
    private void handleAllMalts() {
        messageText.setText("All Malts Button: under development");
    }

    @FXML
    private void handleRegionMalts() {
        messageText.setText("Malts from Region Button: under development");
    }

    @FXML
    private void handleAgeRangeMalts() {
        messageText.setText("Malts in Age Range Button: under development");
    }

    @FXML
    private void handlePrevious() {
        messageText.setText("Previous Button: under development");
    }

    @FXML
    private void handleNext() {
        messageText.setText("Next Button: under development");
    }

    @FXML
    private void handleClear() {
        minAgeField.clear();
        maxAgeField.clear();
        regionQueryField.clear();
        messageText.clear();
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }
}
