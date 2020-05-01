package at.fhv.teamb.symphoniacus.presentation;

import at.fhv.teamb.symphoniacus.application.SeriesOfPerformancesManager;
import at.fhv.teamb.symphoniacus.persistence.model.InstrumentationEntity;
import at.fhv.teamb.symphoniacus.persistence.model.MusicalPieceEntity;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextFlow;
import javafx.util.StringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.JsonUtils;
import org.checkerframework.checker.nullness.Opt;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.controlsfx.validation.decoration.StyleClassValidationDecoration;
import org.controlsfx.validation.decoration.ValidationDecoration;

/**
 * GUI Controller responsible for creating a new Series of Performances.
 *
 * @author Danijel Antonijevic
 * @author Nino Heinzle
 */
public class SeriesOfPerformancesController implements Initializable {
    private static final Logger LOG = LogManager.getLogger(SeriesOfPerformancesController.class);
    private boolean isValid = false;
    private ValidationSupport validationSupport = new ValidationSupport();
    private SeriesOfPerformancesManager seriesManager;

    @FXML
    private AnchorPane pane;

    @FXML
    private TextField nameOfSeries;

    @FXML
    private CheckComboBox<MusicalPieceEntity> musicalPieceCheckComboBox;

    @FXML
    private CheckComboBox<InstrumentationEntity> instrumentationCheckComboBox;

    /*
    This one is and will not be implemented.
     */
    @FXML
    private Button addModifyButton;

    @FXML
    private TextFlow instrumentationPositionsFlow;

    @FXML
    private DatePicker startingDate;

    @FXML
    private DatePicker endingDate;

    @FXML
    private CheckBox isTour;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private ResourceBundle resources;

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        this.seriesManager = new SeriesOfPerformancesManager();

        ValidationDecoration cssDecorator = new StyleClassValidationDecoration(
            "error",
            "warning"
        );
        this.validationSupport.setValidationDecorator(cssDecorator);

        this.validationSupport.registerValidator(this.nameOfSeries,
            Validator.createEmptyValidator("A name is required."));

        this.validationSupport.registerValidator(musicalPieceCheckComboBox,
            Validator.createEmptyValidator("You must pick a musical piece."));

        this.validationSupport.registerValidator(this.instrumentationCheckComboBox,
            Validator.createEmptyValidator("You must pick an instrumentation."));

        this.validationSupport.registerValidator(this.startingDate,
            Validator.createEmptyValidator("A starting date is required."));

        this.validationSupport.registerValidator(this.endingDate,
            Validator.createEmptyValidator("An ending date is required."));


        //Sets Save button disabled if form is not valid
        this.validationSupport.validationResultProperty()
            .addListener((observable, oldValue, newValue) -> {
                this.isValid = newValue.getErrors().isEmpty();
                this.saveButton.setDisable(!isValid);
            });

        initMusicialPiecesCheckListView();

    }

    public void initMusicialPiecesCheckListView() {
        final ObservableList<MusicalPieceEntity> musicalPieces =
            FXCollections.observableArrayList();
        Set<MusicalPieceEntity> mp = seriesManager.getAllMusicalPieces();
        musicalPieces.addAll(mp);

        StringConverter<MusicalPieceEntity> musicalConverter =
            new StringConverter<MusicalPieceEntity>() {
                @Override
                public String toString(MusicalPieceEntity piece) {
                    return piece.getName();
                }

                @Override
                public MusicalPieceEntity fromString(String nameOfPiece) {
                    Optional<MusicalPieceEntity> piece =
                        SeriesOfPerformancesManager.getMusicalPieceFromName(nameOfPiece);
                    if (piece.isPresent()) {
                        return piece.get();
                    } else {
                        LOG.error(
                            "Somehow the musicial piece couldn't get found by its name in the SeriesOfPerformancesController");
                        //Should never be able to get here
                        return null;
                    }
                }
            };
        musicalPieceCheckComboBox.setConverter(musicalConverter);
        /*
        Der schlimmste Fehler meines Lebens!:
        https://stackoverflow.com/questions/30643979/checkcombobox-choices-are-empty
         */
        musicalPieceCheckComboBox.getItems().addAll(musicalPieces);
        System.out.println("TESTESTETSETSETESTET");

        System.out.println();

        // Call init Instrumentations, when Musical Pieces have been chosen
        musicalPieceCheckComboBox.getCheckModel().getCheckedItems().addListener(
            new ListChangeListener<MusicalPieceEntity>() {
                @Override
                public void onChanged(Change<? extends MusicalPieceEntity> c) {
                    loadInstrumentationsFromChosenMusicalPieces(musicalPieceCheckComboBox.getCheckModel().getCheckedItems());

                }
            });
    }

    public void loadInstrumentationsFromChosenMusicalPieces(
        ObservableList<MusicalPieceEntity> musicalPieces) {
        System.out.println("aufruf!!!");
        Set<MusicalPieceEntity> mp = new LinkedHashSet<>();
        mp.addAll(musicalPieces);

        Set<InstrumentationEntity> inst = this.seriesManager.getInstrumentationsToMusicalPieces(mp);
        for (InstrumentationEntity i : inst) {
            System.out.println(i.getName());
        }

        ObservableSet<InstrumentationEntity> test = FXCollections.observableSet();
        test.addAll(inst);


        StringConverter<InstrumentationEntity> instrumentationConverter =
            new StringConverter<InstrumentationEntity>() {
                @Override
                public String toString(InstrumentationEntity instrumentation) {
                    return instrumentation.getName();
                }

                @Override
                public InstrumentationEntity fromString(String nameOfPiece) {
                /*
                Optional<MusicalPieceEntity> piece = SeriesOfPerformancesManager.getMusicalPieceFromName(nameOfPiece);
                if (piece.isPresent()) {
                    return piece.get();
                } else {
                    LOG.error(
                        "Somehow the musicial piece couldn't get found by its name in the SeriesOfPerformancesController");
                    //Should never be able to get here
                    return null;
                }
            }

                 */
                    return null;
                }
            };

        instrumentationCheckComboBox.setConverter(instrumentationConverter);
        instrumentationCheckComboBox.getItems().clear();
        instrumentationCheckComboBox.getItems().addAll(test);
    }

    public void save() {
        System.out.println("test");
    }

    public void cancel() {

    }
}
