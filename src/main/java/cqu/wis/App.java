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
 *
 * @author Ayush Bhandari S12157470
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        try {
            WhiskeyData wd = new WhiskeyData();
            wd.connect();
            WhiskeyDataManager wdm = new WhiskeyDataManager(wd);
            WhiskeyDataValidator wdv = new WhiskeyDataValidator();
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cqu/wis/view/query.fxml"));
            Parent root = loader.load();
            
            QueryController queryController = loader.getController();
            queryController.inject(wd, wdm, wdv);

            stage.setScene(new Scene(root));
            stage.setTitle("Whiskey Information System");
            stage.show();

        } catch (Exception e) {
            System.err.println("Failed to start application: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        
    }

    public static void main(String[] args) {
        launch();
    }

}