package cqu.wis;

import cqu.wis.data.WhiskeyData;
import cqu.wis.roles.WhiskeyDataManager;
import cqu.wis.roles.WhiskeyDataValidator;
import cqu.wis.view.QueryController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
     * The main scene used by the application.
     */
    private static Scene scene;

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
        try {
            // Initialize data layer and services
            WhiskeyData wd = new WhiskeyData();
            wd.connect();
            WhiskeyDataManager wdm = new WhiskeyDataManager(wd);
            WhiskeyDataValidator wdv = new WhiskeyDataValidator();

            // Load FXML and controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cqu/wis/view/query.fxml"));
            Parent root = loader.load();

            // Inject dependencies into controller
            QueryController queryController = loader.getController();
            queryController.inject(wd, wdm, wdv);

            // Setup and show scene
            stage.setScene(new Scene(root));
            stage.setTitle("Whiskey Information System");
            stage.show();

        } catch (Exception e) {
            System.err.println("Failed to start application: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
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
