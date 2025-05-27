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

public class LoginController implements Initializable {
    @FXML 
    private TextField usernameField;
    
    @FXML 
    private PasswordField passwordField;
    
    @FXML 
    private Button loginButton;
    
    @FXML 
    private Button changePasswordButton;
    
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

    @FXML
    private void handleChangePassword() {
        handleClear();
        sc.setScene(SceneCoordinator.SceneKey.PASSWORD);
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
        passwordField.clear();
        messageText.clear();
    }
}