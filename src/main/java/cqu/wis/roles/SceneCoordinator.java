package cqu.wis.roles;

import java.util.HashMap;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Ayush Bhandari S12157470
 */
public class SceneCoordinator {

    public enum SceneKey {LOGIN, PASSWORD, QUERY};
    
    private final Stage stage;
    private final HashMap<SceneKey, Scene> scenes;
    
    public SceneCoordinator(Stage s) {
        this.scenes = new HashMap<>();
        this.stage = s;
    }
    
    public void addScene(SceneKey key, Scene value) {
        scenes.put(key, value);
    }
    
    public void setScene(SceneKey key) {
        Scene s = scenes.get(key);
        stage.setScene(s);
        stage.setTitle("Whiskey Information System");
        stage.show();
    }
    
    public void start(SceneKey key) {
        setScene(key);
    }
}
