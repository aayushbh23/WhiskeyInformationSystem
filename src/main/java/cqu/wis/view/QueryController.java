package cqu.wis.view;

import cqu.wis.data.WhiskeyData;
import cqu.wis.roles.WhiskeyDataManager;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 *
 * @author Ayush Bhandari S12157470
 */

public class QueryController implements Initializable{

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
    
    WhiskeyData wd = new WhiskeyData();
    WhiskeyDataManager wdm = new WhiskeyDataManager(wd);
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize button states
        setNavigationButtonsDisabled(true);
        
        try {
            wd.connect();
        } catch (SQLException e) {
            System.err.println("Failed to connect to whiskey database: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleAllMalts() {
        try {
            int count = wdm.findAllMalts();
            updateDisplayAfterQuery(count);
        } catch (Exception e) {
            messageText.setText("Error retrieving all malts: " + e.getMessage());
        }
    }

    @FXML
    private void handleRegionMalts() {
        String r = regionQueryField.getText().trim();
        try {
            int count = wdm.findMaltsFromRegion(r);
            updateDisplayAfterQuery(count);
        } catch (Exception e) {
            messageText.setText("Error retrieving region malts: " + e.getMessage());
        }
    }

    @FXML
    private void handleAgeRangeMalts() {
        String r1 = minAgeField.getText().trim();
        String r2 = maxAgeField.getText().trim();
        try {
            int min = r1.isEmpty() ? 0 : Integer.parseInt(r1);
            int max = r2.isEmpty() ? 100 : Integer.parseInt(r2);
            int count = wdm.findMaltsInAgeRange(min, max);
            updateDisplayAfterQuery(count);
        } catch (Exception e) {
            messageText.setText("Error retrieving age range malts: " + e.getMessage());
        }
    }

    private void updateDisplayAfterQuery(int count) {
        if (count == 0) {
            messageText.setText("No records found");
            clearWhiskeyDetails();
            setNavigationButtonsDisabled(true);
        } else {
            messageText.setText(String.format("Found %d records", count));
            displayCurrentRecord();
            setNavigationButtonsDisabled(false);
        }
    }

    @FXML
    private void handlePrevious() {
        displayRecord(wdm.previous());
    }

    @FXML
    private void handleNext() {
        displayRecord(wdm.next());
    }

    private void displayCurrentRecord() {
        displayRecord(wdm.first());
    }

    private void displayRecord(WhiskeyData.WhiskeyDetails details) {
        if (details == null) {
            clearWhiskeyDetails();
            return;
        }
        
        distilleryField.setText(details.distillery());
        ageField.setText(String.valueOf(details.age()));
        regionField.setText(details.region());
        priceField.setText(String.valueOf(details.price()));
    }

    @FXML
    private void handleClear() {
        clearWhiskeyDetails();
        minAgeField.clear();
        maxAgeField.clear();
        regionQueryField.clear();
        messageText.clear();
        setNavigationButtonsDisabled(true);
    }

    @FXML
    private void handleExit() {
        try {
            wdm.disconnect();
        } catch (Exception e) {
            messageText.setText("Error during shutdown: " + e.getMessage());
        }
        System.exit(0);
    }

    private void clearWhiskeyDetails() {
        distilleryField.clear();
        ageField.clear();
        regionField.clear();
        priceField.clear();
    }

    private void setNavigationButtonsDisabled(boolean disabled) {
        previousButton.setDisable(disabled);
        nextButton.setDisable(disabled);
    }
}
