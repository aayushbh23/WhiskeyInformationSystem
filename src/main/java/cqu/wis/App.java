package cqu.wis;

import cqu.wis.data.UserData;
import cqu.wis.data.WhiskeyData;
import cqu.wis.roles.SceneCoordinator;
import cqu.wis.roles.SceneCoordinator.SceneKey;
import cqu.wis.roles.UserDataManager;
import cqu.wis.roles.UserDataValidator;
import cqu.wis.roles.WhiskeyDataManager;
import cqu.wis.roles.WhiskeyDataValidator;
import cqu.wis.view.LoginController;
import cqu.wis.view.PasswordController;
import cqu.wis.view.QueryController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main application entry point for the Whiskey Information System.
 * This class initializes the JavaFX application, sets up the user interface using FXML,
 * connects to the whiskey database, and injects the necessary model and controller components
 * for application functionality.
 *
 * Expected FXML location: {@code /cqu/wis/view/query.fxml}
 * 
 * This application allows users to browse and query whiskey data based on
 * distillery, region, age, and price criteria.
 *
 * @author Ayush Bhandari S12157470
 */
public class App extends Application {
    /**
     * Called when the JavaFX application is launched.
     * 
     * Initializes the model classes ({@link WhiskeyData}, {@link WhiskeyDataManager}, {@link WhiskeyDataValidator}),
     * loads the FXML user interface, injects dependencies into the controller, and displays the main window.
     *
     * @param stage the primary stage for this application
     * @throws IOException if an error occurs during loading of the FXML layout
     */
    @Override
    public void start(Stage stage) throws IOException {
        SceneCoordinator sc = new SceneCoordinator(stage);
        // Configure each scene and add it to the coordinator.
        // Transitions between scenes are achieved by the controller for a 
        // scene requesting the coordinator to make a particular scene the 
        // root node of the scene graph.

        try {
            // Create data objects
            WhiskeyData wd = new WhiskeyData();
            WhiskeyDataManager wdm = new WhiskeyDataManager(wd);
            WhiskeyDataValidator wdv = new WhiskeyDataValidator();

            UserData ud = new UserData();
            UserDataManager udm = new UserDataManager(ud);
            UserDataValidator udv = new UserDataValidator();

            // Connect to databases
            wd.connect();
            ud.connect();
            
            // create the login scene
            Scene ls = makeScene(SceneKey.LOGIN);
            // inject required objects into the login controller
            LoginController lc = (LoginController) ls.getUserData();
            lc.inject(sc, udm, udv);
            sc.addScene(SceneKey.LOGIN, ls);
            
            // create the password scene
            Scene ps = makeScene(SceneKey.PASSWORD);
            // inject required objects into the password controller
            PasswordController pc = (PasswordController) ps.getUserData();
            pc.inject(sc, udm, udv);
            sc.addScene(SceneKey.PASSWORD, ps);
            
            // create the query scene 
            Scene qs = makeScene(SceneKey.QUERY);
            // inject required objects into the query controller
            QueryController qc = (QueryController) qs.getUserData();
            qc.inject(sc, wdm, wdv);
            sc.addScene(SceneKey.QUERY, qs);
            
            sc.start(SceneKey.LOGIN);

        } catch (Exception e) {
            System.err.println("Failed to start application: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private static Scene makeScene(SceneKey key) throws Exception  {
        // construct path name for fxml file
        String fxml = "/cqu/wis/view/"+key.name().toLowerCase()+".fxml";
        // create scene object and add a reference to its controller object
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml));
        Scene scene = new Scene(loader.load());
        scene.setUserData(loader.getController());
        return scene;
    }

    /**
     * Launches the application.
     *
     * @param args the command line arguments (not used)
     */
    public static void main(String[] args) {
        launch();
    }
}
