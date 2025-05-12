package cqu.wis.view;

import cqu.wis.data.WhiskeyData;
import cqu.wis.roles.WhiskeyDataManager;
import cqu.wis.roles.ValidationResponse;
import cqu.wis.roles.WhiskeyDataValidator;
import cqu.wis.roles.WhiskeyDataValidator.*;
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
    
    private WhiskeyData wd;
    private WhiskeyDataManager wdm;
    private WhiskeyDataValidator wdv;
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize button states
        setNavigationButtonsDisabled(true);
    }
    
    public void inject(WhiskeyData wd, WhiskeyDataManager wdm, WhiskeyDataValidator wdv) {
        this.wd = wd;
        this.wdm = wdm;
        this.wdv = wdv;
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
        ValidationResponse validation = wdv.checkRegion(r);
        
        if (!validation.result()) {
           messageText.setText(validation.message());
           return;
        }
        
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
        RangeValidationResponse validation = wdv.checkAgeRange(r1, r2);
        
        if (!validation.result()) {
            messageText.setText(validation.message());
            return;
        }
        
        try {
            Range range = validation.r();
            int count = wdm.findMaltsInAgeRange(range.lower(), range.upper());
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
