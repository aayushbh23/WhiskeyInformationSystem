package cqu.wis.view;

import cqu.wis.data.WhiskeyData;
import cqu.wis.roles.SceneCoordinator;
import cqu.wis.roles.WhiskeyDataManager;
import cqu.wis.roles.ValidationResponse;
import cqu.wis.roles.WhiskeyDataValidator;
import cqu.wis.roles.WhiskeyDataValidator.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Controller class for handling user interaction with the Whiskey Data application view.
 * This class handles the logic behind user actions in the UI, such as querying whiskey data based on different filters (region, age range), 
 * navigating through whiskey records, and displaying information to the user. It works with the {@code WhiskeyDataManager} to query the data 
 * and with {@code WhiskeyDataValidator} to validate user inputs.
 * 
 * The {@code QueryController} is responsible for initializing the user interface components, handling actions on buttons like 
 * searching for malts, clearing input fields, navigating through records and displaying results or error messages.
 * 
 * @author Ayush Bhandari S12157470
 */
public class QueryController implements Initializable {

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

    private SceneCoordinator sc;
    private WhiskeyDataManager wdm;
    private WhiskeyDataValidator wdv;
    
    /**
     * Initializes the controller by setting the initial state of the buttons.
     * The navigation buttons are initially disabled, awaiting user input.
     * 
     * @param url The location used to resolve relative paths for the root object or null if the location is not known.
     * @param rb The resources used to localize the root object or null if the resources are not available.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize button states
        setNavigationButtonsDisabled(true);
    }

    /**
     * Injects the required dependencies into the controller.This method is typically called by the JavaFX framework 
 to set up the controller with the necessary data and services.
     * 
     * @param sc The {@code SceneCoordinator} object containing scene coordinator data.
     * @param wdm The {@code WhiskeyDataManager} object used for managing whiskey data.
     * @param wdv The {@code WhiskeyDataValidator} object used for validating input.
     */
    public void inject(SceneCoordinator sc, WhiskeyDataManager wdm, WhiskeyDataValidator wdv) {
        this.sc = sc;
        this.wdm = wdm;
        this.wdv = wdv;
    }

    /**
     * Handles the action when the "All Malts" button is clicked. This method retrieves all whiskey malts from the database 
     * and displays the results.
     */
    @FXML
    private void handleAllMalts() {
        try {
            int count = wdm.findAllMalts();
            updateDisplayAfterQuery(count);
        } catch (Exception e) {
            messageText.setText("Error retrieving all malts: " + e.getMessage());
        }
    }

    /**
     * Handles the action when the "Region Malts" button is clicked. This method validates the region input and retrieves 
     * whiskey malts based on the region provided by the user.
     */
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

    /**
     * Handles the action when the "Age Range Malts" button is clicked. This method validates the input age range and 
     * retrieves whiskey malts within the specified age range.
     */
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

    /**
     * Updates the display with the results of a query. If no records are found, an appropriate message is shown.
     * 
     * @param count The number of records found by the query.
     */
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

    /**
     * Handles the action when the "Previous" button is clicked. It displays the previous whiskey record.
     */
    @FXML
    private void handlePrevious() {
        displayRecord(wdm.previous());
    }

    /**
     * Handles the action when the "Next" button is clicked. It displays the next whiskey record.
     */
    @FXML
    private void handleNext() {
        displayRecord(wdm.next());
    }

    /**
     * Displays the first whiskey record in the list.
     */
    private void displayCurrentRecord() {
        displayRecord(wdm.first());
    }

    /**
     * Displays the details of the given whiskey record in the UI.
     * 
     * @param details The whiskey details to display.
     */
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

    /**
     * Handles the action when the "Clear" button is clicked. This method clears all the input fields and resets the UI.
     */
    @FXML
    private void handleClear() {
        clearWhiskeyDetails();
        minAgeField.clear();
        maxAgeField.clear();
        regionQueryField.clear();
        messageText.clear();
        setNavigationButtonsDisabled(true);
    }

    /**
     * Handles the action when the "Exit" button is clicked. This method disconnects from the database and exits the application.
     */
    @FXML
    private void handleExit() {
        try {
            wdm.disconnect();
        } catch (Exception e) {
            messageText.setText("Error during shutdown: " + e.getMessage());
        }
        System.exit(0);
    }

    /**
     * Clears all whiskey details from the UI fields.
     */
    private void clearWhiskeyDetails() {
        distilleryField.clear();
        ageField.clear();
        regionField.clear();
        priceField.clear();
    }

    /**
     * Enables or disables the navigation buttons based on the provided state.
     * 
     * @param disabled {@code true} to disable the buttons, {@code false} to enable them.
     */
    private void setNavigationButtonsDisabled(boolean disabled) {
        previousButton.setDisable(disabled);
        nextButton.setDisable(disabled);
    }
}
