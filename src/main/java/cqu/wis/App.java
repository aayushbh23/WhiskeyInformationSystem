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
 * This JavaFX application initializes the UI and injects the required
 * data model and validation components. It connects to the whiskey and
 * user databases, configures scene transitions and manages scene lifecycle
 * via the {@link SceneCoordinator}.
 * 
 * The application supports the following scenes:
 * Login scene (for user authentication)
 * Password scene (for password changes)
 * Query scene (for browsing and filtering whiskey records)
 * 
 * Expected FXML location: {@code /cqu/wis/view/{sceneKey}.fxml}
 * Users can query whiskeys by distillery, region, age, and price criteria.
 *
 * @author Ayush Bhandari S12157470
 */
public class App extends Application {

    /**
     * Initializes the JavaFX application.
     * 
     * This method:
     * Creates and connects the data models: {@link WhiskeyData} and {@link UserData}
     * Initializes managers and validators for both whiskey and user data
     * Loads the FXML layouts for each scene
     * Injects dependencies into each scene’s controller
     * Registers scenes in the {@link SceneCoordinator} and starts the application with the login scene
     *
     * @param stage the primary stage for this application
     * @throws IOException if an error occurs during loading of the FXML layout
     */
    @Override
    public void start(Stage stage) throws IOException {
        SceneCoordinator sc = new SceneCoordinator(stage);

        try {
            // Initialize data models, managers and validators
            WhiskeyData wd = new WhiskeyData();
            WhiskeyDataManager wdm = new WhiskeyDataManager(wd);
            WhiskeyDataValidator wdv = new WhiskeyDataValidator();

            UserData ud = new UserData();
            UserDataManager udm = new UserDataManager(ud);
            UserDataValidator udv = new UserDataValidator();

            // Connect to databases
            wd.connect();
            ud.connect();

            // Setup login scene
            Scene ls = makeScene(SceneKey.LOGIN);
            LoginController lc = (LoginController) ls.getUserData();
            lc.inject(sc, udm, udv);
            sc.addScene(SceneKey.LOGIN, ls);

            // Setup password scene
            Scene ps = makeScene(SceneKey.PASSWORD);
            PasswordController pc = (PasswordController) ps.getUserData();
            pc.inject(sc, udm, udv);
            sc.addScene(SceneKey.PASSWORD, ps);

            // Setup query scene
            Scene qs = makeScene(SceneKey.QUERY);
            QueryController qc = (QueryController) qs.getUserData();
            qc.inject(sc, wdm, wdv);
            sc.addScene(SceneKey.QUERY, qs);

            // Start application with login scene
            sc.start(SceneKey.LOGIN);

        } catch (Exception e) {
            System.err.println("Failed to start application: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Helper method to create and load a {@link Scene} for the given {@link SceneKey}.
     * 
     * The FXML file path is constructed as {@code /cqu/wis/view/{sceneKey}.fxml},
     * and the scene’s controller is set as user data for later injection.
     *
     * @param key the scene key indicating which scene to load
     * @return the loaded {@link Scene} object
     * @throws Exception if an error occurs during FXML loading
     */
    private static Scene makeScene(SceneKey key) throws Exception {
        String fxml = "/cqu/wis/view/" + key.name().toLowerCase() + ".fxml";
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml));
        Scene scene = new Scene(loader.load());
        scene.setUserData(loader.getController());
        return scene;
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        launch();
    }
}
