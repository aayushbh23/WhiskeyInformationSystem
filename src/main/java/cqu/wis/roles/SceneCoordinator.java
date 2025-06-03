package cqu.wis.roles;

import java.util.HashMap;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Manages scene transitions within the Whiskey Information System JavaFX application.
 * 
 * This class acts as a central coordinator for switching between different UI scenes,
 * such as login, password change and query views. It maintains a mapping between
 * {@link SceneKey} identifiers and {@link Scene} objects and it can set the active
 * scene on the application stage as needed.
 * 
 * @author Ayush Bhandari S12157470
 */
public class SceneCoordinator {

    /**
     * Keys identifying the different scenes in the application.
     */
    public enum SceneKey { LOGIN, PASSWORD, QUERY }

    private final Stage stage;
    private final HashMap<SceneKey, Scene> scenes;

    /**
     * Constructs a new {@code SceneCoordinator} with the given primary stage.
     *
     * @param s the primary {@link Stage} of the application
     */
    public SceneCoordinator(Stage s) {
        this.scenes = new HashMap<>();
        this.stage = s;
    }

    /**
     * Adds a new scene to the coordinator's registry.
     *
     * @param key the unique {@link SceneKey} identifier for the scene
     * @param value the {@link Scene} object to associate with the key
     */
    public void addScene(SceneKey key, Scene value) {
        scenes.put(key, value);
    }

    /**
     * Sets the active scene based on the given key.
     *
     * This method updates the application's main stage to display the new scene
     * and sets a standard window title.
     *
     * @param key the {@link SceneKey} of the scene to display
     */
    public void setScene(SceneKey key) {
        Scene s = scenes.get(key);
        stage.setScene(s);
        stage.setTitle("Whiskey Information System");
        stage.show();
    }

    /**
     * Starts the application with the specified initial scene.
     * This is a convenience method that delegates to {@link #setScene(SceneKey)}.
     *
     * @param key the {@link SceneKey} of the scene to start with
     */
    public void start(SceneKey key) {
        setScene(key);
    }
}
