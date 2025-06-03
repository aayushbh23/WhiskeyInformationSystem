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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Controller class for managing the password change functionality in the Whiskey Information System.
 * 
 * This class handles user interactions within the password change view. It manages:
 * Collecting user input for username, old password and new password
 * Validating the entered details
 * Updating the password in the database if validation passes
 * Providing feedback to the user through messages
 *
 * The {@code PasswordController} also manages navigation back to the login scene
 * and ensures that the database connection is properly closed on exit.
 * 
 * @author Ayush Bhandari S12157470
 */
public class PasswordController implements Initializable {

    @FXML private TextField usernameField;
    @FXML private PasswordField oldPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private Button submitButton;
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
     * This method is typically called by the JavaFX framework to set up the controller with necessary data.
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
     * Handles the action when the "Submit" button is clicked.
     * 
     * This method:
     * Collects input fields: username, old password and new password
     * Performs validation on required fields and password patterns
     * Verifies the current password
     * Encrypts the new password and updates it in the database if valid
     * Provides feedback to the user via the {@code messageText} area
     *
     */
    @FXML
    private void handleSubmit() {
        String username = usernameField.getText().trim();
        String oldPassword = oldPasswordField.getText().trim();
        String newPassword = newPasswordField.getText().trim();

        try {
            UserData.UserDetails userDetails = udm.findUser(username);

            // Check if all required fields are filled
            ValidationResponse newPasswordCheck = udv.checkForFieldsPresent(username, oldPassword, newPassword);
            if (!newPasswordCheck.result()) {
                messageText.setText(newPasswordCheck.message());
                return;
            }

            // Verify current credentials
            ValidationResponse credentialCheck = udv.checkCurrentDetails(userDetails, username, oldPassword);
            if (!credentialCheck.result()) {
                messageText.setText(credentialCheck.message());
                return;
            }

            // Check new password requirements
            ValidationResponse newPasswordCredentialCheck = udv.checkNewDetails(userDetails, username, oldPassword, newPassword);
            if (!newPasswordCredentialCheck.result()) {
                messageText.setText(newPasswordCredentialCheck.message());
                return;
            }

            // Update password
            String encryptedPassword = udv.generateSHA1(newPassword);
            int success = udm.updatePassword(username, encryptedPassword);

            if (success == 1) {
                messageText.setText("Password changed successfully");
                sc.setScene(SceneCoordinator.SceneKey.LOGIN);
            } else {
                messageText.setText("Failed to update password");
            }
        } catch (Exception e) {
            messageText.setText("Error: " + e.getMessage());
        }
    }

    /**
     * Handles the action when the "Exit" button is clicked.
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
     * Clears all input fields and the message area in the UI.
     */
    @FXML
    private void handleClear() {
        usernameField.clear();
        oldPasswordField.clear();
        newPasswordField.clear();
        messageText.clear();
    }
}
