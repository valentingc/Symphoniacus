package at.fhv.teamb.symphoniacus.presentation;

import at.fhv.teamb.symphoniacus.application.AdministrativeAssistantManager;
import at.fhv.teamb.symphoniacus.application.MusicianManager;
import at.fhv.teamb.symphoniacus.application.dto.LoginUserDto;
import at.fhv.teamb.symphoniacus.application.type.DomainUserType;
import at.fhv.teamb.symphoniacus.application.type.MusicianRoleType;
import at.fhv.teamb.symphoniacus.domain.AdministrativeAssistant;
import at.fhv.teamb.symphoniacus.domain.Musician;
import at.fhv.teamb.symphoniacus.persistence.model.interfaces.IMusicianRoleEntity;
import at.fhv.teamb.symphoniacus.presentation.internal.TabPaneEntry;
import java.net.URL;
import java.util.Comparator;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainController implements Initializable {

    private static final Logger LOG = LogManager.getLogger(MainController.class);

    @FXML
    public AnchorPane userHeaderMenu;

    @FXML
    public AnchorPane tabPane;

    @FXML
    private TabPaneController tabPaneController;

    @FXML
    private UserController userHeaderMenuController;

    private LoginUserDto currentUser;
    private Musician currentMusician;
    private AdministrativeAssistant currentAssistant;
    private MusicianManager musicianManager;
    private AdministrativeAssistantManager administrativeManager;
    private ResourceBundle resources;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.userHeaderMenuController.setParentController(this);
        this.tabPaneController.setParentController(this);
        this.resources = resources;
        LOG.debug("Initialized MainController");
    }

    /**
     * Finds out if the currently logged-in user is a {@link Musician} or an
     * {@link AdministrativeAssistant} and sets his Domainobject as attribute in this class.
     *
     * @param user Current login user
     */
    @SuppressWarnings("checkstyle:FallThrough")
    public synchronized void setLoginUser(LoginUserDto user) {
        this.currentUser = user;

        if (this.currentUser.getType().equals(DomainUserType.DOMAIN_MUSICIAN)) {
            if (musicianManager == null) {
                musicianManager = new MusicianManager();
            }
            Optional<Musician> musician = musicianManager
                .loadMusician(this.currentUser.getUserId());

            if (musician.isPresent()) {
                LOG.debug("Musician successfully loaded");
                this.currentMusician = musician.get();
            } else {
                LOG.error("Cannot load Login Musician");
            }
        }
        if (this.currentUser.getType()
            .equals(DomainUserType.DOMAIN_ADMINISTRATIVE_ASSISTANT)) {
            if (administrativeManager == null) {
                administrativeManager = new AdministrativeAssistantManager();
            }
            Optional<AdministrativeAssistant> administrativeAssistant =
                administrativeManager.loadAdministrativeAssistant(
                    this.currentUser.getUserId()
                );
            if (administrativeAssistant.isPresent()) {
                LOG.debug("AdministrativeAssistant successfully loaded");
                this.currentAssistant = administrativeAssistant.get();
            } else {
                LOG.debug("Cannot load Login AdministrativeAssistant");
            }
        }

        this.tabPaneController.initializeTabMenu();
        this.userHeaderMenuController.setUserShortcut(this.currentUser.getFullName());
        if (this.currentMusician != null) {

            StringBuilder sb = new StringBuilder();
            sb.append("Musician");
            for (IMusicianRoleEntity role : currentMusician.getEntity().getMusicianRoles()) {
                switch (role.getDescription()) {
                    case TUTTI:
                        sb.append(" | ");
                        sb.append("Tutti");
                        break;
                    case DUTY_SCHEDULER:
                        sb.append(" | ");
                        sb.append("Duty Scheduler");
                        break;
                    case SECTION_PRINCIPAL:
                        sb.append(" | ");
                        sb.append("Section Principal");
                        break;
                    default:
                        sb.append("");
                }
                this.userHeaderMenuController.setUserTxtRole(sb.toString());
            }
            this.userHeaderMenuController.setUserTxtSection(
                this.getCurrentMusician().getSection().getEntity().getDescription().toString());
        } else if (this.currentAssistant != null) {
            String role = "";
            switch (this.currentAssistant.getAdministrativeAssistantEntity().getDescription()
                .toString()) {
                case "ORGANIZATIONAL_OFFICER":
                    role =
                        this.resources.getString("global.label.user.role.organizational.officer");
                    break;
                case "MUSIC_LIBRARIAN":
                    role = this.resources.getString("global.label.user.role.music.librarian");
                    break;
                case "ORCHESTRA_LIBRARIAN":
                    role = this.resources.getString("global.label.user.role.orchestra.librarian");
                    break;
                default:
                    role = "";
            }
            this.userHeaderMenuController.setUserTxtSection(role);

            this.userHeaderMenuController.setUserTxtRole(
                this.resources.getString("global.label.user.type.administrative.assistant"));
        }

    }

    public DomainUserType getLoginUserType() {
        return currentUser.getType();
    }

    public Musician getCurrentMusician() {
        return currentMusician;
    }

    public AdministrativeAssistant getCurrentAssistant() {
        return currentAssistant;
    }

    // intentionally set package modifier
    Queue<TabPaneEntry> getPermittedTabs(
        Musician m,
        AdministrativeAssistant assistant
    ) {
        Queue<TabPaneEntry> result = new PriorityQueue<>(
            Comparator.comparingInt(TabPaneEntry::getOrder)
        );
        if (m == null && assistant == null) {
            LOG.error("Cannot getPermittedTabs for null users");
            return result;
        }

        // Musician
        if (m != null && assistant == null) {
            LOG.debug("Getting permittedTabs for Musician");
            LOG.debug("No default view for Musician atm");

            for (IMusicianRoleEntity role : m.getEntity().getMusicianRoles()) {
                // Duty Scheduler only
                if (role.getDescription().equals(MusicianRoleType.DUTY_SCHEDULER)) {
                    result.add(TabPaneEntry.DUTY_SCHEDULER_CALENDAR_VIEW);
                }

                // Duty Scheduler, Section Principal & Tutti
                if (role.getDescription().equals(MusicianRoleType.SECTION_PRINCIPAL)
                    || role.getDescription().equals(MusicianRoleType.TUTTI)
                    || role.getDescription().equals(MusicianRoleType.DUTY_SCHEDULER)
                ) {
                    result.add(TabPaneEntry.MUSICIAN_CALENDAR_VIEW);
                }
            }
        } else if (m == null) { // Organizational Officer
            LOG.debug("Getting permittedTabs for Administrative Assistant");
            result.add(TabPaneEntry.ORG_OFFICER_CALENDAR_VIEW);
            result.add(TabPaneEntry.USER_MANAGEMENT);
        }

        if (result.isEmpty()) {
            LOG.debug("No Tabs allowed. Adding unsupported tab as backup");
            result.add(
                TabPaneEntry.UNSUPPORTED
            );
        }

        return result;
    }

    /**
     * Shows an error alert with a custom title and error message.
     *
     * @param alertTitle The alert title to use
     * @param errorText  The error text to use
     */
    protected static void showErrorAlert(String alertTitle, String errorText, String buttonTitle) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(alertTitle);
        alert.setContentText(errorText);

        ButtonType okButton = new ButtonType(buttonTitle, ButtonBar.ButtonData.YES);
        alert.getButtonTypes().setAll(okButton);
        alert.showAndWait().ifPresent(type -> {
            if (type.equals(okButton)) {
                alert.close();
            }
        });
    }
}
