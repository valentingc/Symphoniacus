package at.fhv.teamb.symphoniacus.presentation;

import at.fhv.teamb.symphoniacus.presentation.internal.Parentable;
import com.jfoenix.controls.JFXSpinner;
import java.io.IOException;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.StatusBar;

/**
 * Little helper to assist us in various UI tasks.
 * Mainly needed to switch visibiltiy in {@link DutyScheduleController}.
 *
 * @author Valentin Goronjic
 */
public class MasterController {

    private static final Logger LOG = LogManager.getLogger(MasterController.class);
    private static MasterController INSTANCE;
    private static final JFXSpinner SPINNER = new JFXSpinner();
    private static final Label statusTextField = new Label();
    private final Map<String, Initializable> map = new HashedMap<>();

    private MasterController() {
    }

    /**
     * Returns the instance of MasterController (Singleton).
     *
     * @return The one and only wonderful instanceof {@link MasterController}
     * @deprecated in favor of {@link Parentable} interface methods
     */
    @Deprecated(forRemoval = false, since = "01.05.2020")
    public static MasterController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MasterController();
        }

        return INSTANCE;
    }

    /**
     * Switches the current scene to the given FXML.
     * @param fxmlPath The path to the FXML file (ex. "/view/login.fxml")
     * @param bundle Current Resource Bundle
     * @param node Node that is required to aquire the current scene
     * @param <T> Type of target Calendar, ex. MainController
     * @return Controller instance of the loaded FXML
     * @throws IOException If unable to load FXML file or instantiation of Controller failed
     */
    public static <T> T switchSceneTo(String fxmlPath, ResourceBundle bundle, Node node)
        throws IOException {
        Scene oldScene = node.getScene();
        Stage stage = (Stage) oldScene.getWindow();

        FXMLLoader loader = new FXMLLoader(MasterController.class.getResource(fxmlPath), bundle);
        Parent parent = loader.load();
        T controller = loader.getController();
        Scene newScene = new Scene(parent, oldScene.getWidth(), oldScene.getHeight());
        newScene.getStylesheets().add("css/styles.css");
        stage.setScene(newScene);

        return controller;
    }

    /**
     * Adds and shows a Loading Spinner in the given AnchorPane.
     *
     * @param pane The AnchorPane which will have the Spinner added at the bottom-center
     */
    public static void enableSpinner(AnchorPane pane) {
        LOG.debug("Enabling Spinner");
        SPINNER.setPrefSize(50, 50);

        if (!pane.getChildren().contains(SPINNER)) {
            pane.getChildren().add(SPINNER);
        }

        AnchorPane.setBottomAnchor(SPINNER, 50d);
        AnchorPane.setLeftAnchor(SPINNER, (pane.getWidth() - SPINNER.getPrefWidth()) / 2);
    }

    /**
     * Removes an active Loading Spinner in the given AnchorPane.
     *
     * @param pane The AnchorPane which will have the Spinner removed again
     */
    public static void disableSpinner(AnchorPane pane) {
        LOG.debug("Disabling Spinner");
        if (pane.getChildren().contains(SPINNER)) {
            pane.getChildren().remove(SPINNER);
        }
    }

    /**
     * Gets an {@link Initializable}.
     *
     * @param key The key to identify the Initializable
     * @deprecated in favor of {@link Parentable} interface methods
     */
    @Deprecated(forRemoval = false, since = "01.05.2020")
    public Initializable get(Object key) {
        return map.get(key);
    }

    /**
     * Stores a Initializable with a given String as key.
     *
     * @param key   The key to store the Initializable
     * @param value The value to store
     * @deprecated in favor of {@link Parentable} interface methods
     */
    @Deprecated(forRemoval = false, since = "01.05.2020")
    public Initializable put(String key, Initializable value) {
        return map.put(key, value);
    }

    /**
     * Sets the StatusBar. Is called by {@link TabPaneController} once.
     *
     * @param statusBar The StatusBar which is defined in {@link TabPaneController} FXML
     */
    public void setStatusBar(StatusBar statusBar) {
        this.statusTextField.textProperty().addListener(
            it -> {
                LOG.debug("set to {}", this.statusTextField.getText());
                statusBar.setText(this.statusTextField.getText());
            }
        );
        this.statusTextField.setText("Loaded");
    }

    public static void showStatusBarLoading() {
        statusTextField.setText("Loading");
    }

    public static void showStatusBarLoaded() {
        statusTextField.setText("Loaded");
    }
}
