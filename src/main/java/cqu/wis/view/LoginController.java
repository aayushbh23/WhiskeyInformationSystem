package cqu.wis.view;

import cqu.wis.data.UserData;
import cqu.wis.roles.SceneCoordinator;
import cqu.wis.roles.UserDataManager;
import cqu.wis.roles.UserDataValidator;
import cqu.wis.roles.ValidationResponse;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;

/**
 * Controller class for handling user login interactions within the Whiskey Information System.
 * 
 * This controller manages the UI for the login scene, handling user actions including:
 * Logging in with a username and password
 * Clearing fields and messages
 * Exiting the application
 * Navigating to the password change scene if required
 * 
 * The controller interacts with:
 * {@link UserDataManager} for retrieving user information
 * {@link UserDataValidator} for validating user input
 * {@link SceneCoordinator} for scene transitions
 * 
 * It also displays relevant feedback messages to the user.
 * 
 * @author Ayush Bhandari S12157470
 */
public class LoginController implements Initializable {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button changePasswordButton;
    @FXML private Button exitButton;
    @FXML private Button clearButton;
    @FXML private TextArea messageText;

    private SceneCoordinator sc;
    private UserDataManager udm;
    private UserDataValidator udv;

    /**
     * Initializes the controller when the FXML view is loaded.
     * 
     * @param url the location used to resolve relative paths for the root object or {@code null} if unknown
     * @param rb the resources used to localize the root object or {@code null} if not available
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // No initialization logic needed at this time
    }

    /**
     * Injects the required dependencies into the controller.
     * 
     * @param sc the {@link SceneCoordinator} for managing scene transitions
     * @param udm the {@link UserDataManager} for managing user data
     * @param udv the {@link UserDataValidator} for validating user input
     */
    public void inject(SceneCoordinator sc, UserDataManager udm, UserDataValidator udv) {
        this.sc = sc;
        this.udm = udm;
        this.udv = udv;
    }

    /**
     * Handles the action when the "Login" button is clicked.
     * 
     * This method:
     * Validates the entered username and password fields
     * Checks the user credentials against the stored data
     * If the user is using the default password, redirects to the password change scene
     * If credentials are valid and not default, transitions to the query scene
     * Displays relevant error or success messages
     * 
     */
    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        ValidationResponse fieldsCheck = udv.checkForFieldsPresent(username, password);
        if (!fieldsCheck.result()) {
            messageText.setText(fieldsCheck.message());
            return;
        }

        try {
            UserData.UserDetails userDetails = udm.findUser(username);
            ValidationResponse credentialCheck = udv.checkCurrentDetails(userDetails, username, password);

            if (!credentialCheck.result()) {
                messageText.setText(credentialCheck.message());
                return;
            }

            if (userDetails.hasDefaultPassword()) {
                // Only show this message if they used the actual default password
                if (password.equals("password")) {
                    messageText.setText("Please change your default password");
                    sc.setScene(SceneCoordinator.SceneKey.PASSWORD);
                } else {
                    messageText.setText("Invalid password");
                }
            } else {
                sc.setScene(SceneCoordinator.SceneKey.QUERY);
            }
        } catch (Exception e) {
            messageText.setText("Login error: " + e.getMessage());
        }
    }

    /**
     * Handles the action when the "Change Password" button is clicked.
     * 
     * Clears the current input fields and navigates to the password change scene.
     */
    @FXML
    private void handleChangePassword() {
        handleClear();
        sc.setScene(SceneCoordinator.SceneKey.PASSWORD);
    }

    /**
     * Handles the action when the "Exit" button is clicked.
     * 
     * Closes the database connection and exits the application.
     */
    @FXML
    private void handleExit() {
        try {
            udm.disconnect();
        } catch (Exception e) {
            System.err.println("Error during shutdown: " + e.getMessage());
        }
        System.exit(0);
    }

    /**
     * Handles the action when the "Clear" button is clicked.
     * 
     * Clears the username and password fields and the message area.
     */
    @FXML
    private void handleClear() {
        usernameField.clear();
        passwordField.clear();
        messageText.clear();
    }
}
