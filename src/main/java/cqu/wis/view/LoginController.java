package cqu.wis.view;

import cqu.wis.roles.SceneCoordinator;
import cqu.wis.roles.UserDataManager;
import cqu.wis.roles.UserDataValidator;
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
        
    }

    @FXML
    private void handleChangePassword() {
        handleClear();
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