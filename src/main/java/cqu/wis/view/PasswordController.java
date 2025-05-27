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
 * 
 *
 * @author Ayush Bhandari S12157470
 */
public class PasswordController implements Initializable {

    @FXML 
    private TextField usernameField;
    
    @FXML 
    private PasswordField oldPasswordField;
    
    @FXML 
    private PasswordField newPasswordField;
    
    @FXML 
    private Button submitButton;
    
    @FXML 
    private Button exitButton;
    
    @FXML 
    private Button clearButton;
    
    @FXML 
    private TextArea messageText;
    
    private SceneCoordinator sc;
    private UserDataManager udm;
    private UserDataValidator udv;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    public void inject(SceneCoordinator sc, UserDataManager udm, UserDataValidator udv) {
        this.sc = sc;
        this.udm = udm;
        this.udv = udv;
    }

    @FXML
    private void handleSubmit() {
        String username = usernameField.getText().trim();
        String oldPassword = oldPasswordField.getText().trim();
        String newPassword = newPasswordField.getText().trim();
        
        try {
            UserData.UserDetails userDetails = udm.findUser(username);
            
            // Check if new password is valid
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
            
            // Check new password meets requirement.
            ValidationResponse newPasswordCredentialCheck = udv.checkNewDetails(userDetails, username, oldPassword, newPassword);
            if (!newPasswordCredentialCheck.result()) {
                messageText.setText(newPasswordCredentialCheck.message());
                return;
            }
            
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

    @FXML
    private void handleExit() {
        try {
            udm.disconnect();
        } catch (Exception e) {
            System.err.println("Error during shutdown: " + e.getMessage());
        }
        System.exit(0);
    }
    
    @FXML
    private void handleClear() {
        usernameField.clear();
        oldPasswordField.clear();
        newPasswordField.clear();
        messageText.clear();
    }    
    
}
