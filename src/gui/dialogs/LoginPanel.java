package gui.dialogs;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import persistence.UserRepository;

/**
 * The component that is used for logging in. Will check whether the user is
 * succesfully logged in and acts accordingly if not.
 *
 * @author Frederik
 */
public class LoginPanel extends GridPane {

    private UserRepository INSTANCE = UserRepository.getInstance();
    private EventHandler<ActionEvent> action;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button btnLogin;

    public LoginPanel() {
	FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/gui/login_panel.fxml"));
	try {
	    loader.setRoot(this);
	    loader.setController(this);
	    loader.load();

	    initialize();
	} catch (IOException ex) {
	    throw new RuntimeException(ex);
	}
    }

    /**
     * Settings for the loginPanel
     */
    public void initialize() {
	Platform.runLater(() -> {
	    username.requestFocus();
	});
	btnLogin.focusTraversableProperty().set(false);
    }

    /**
     * Get the username from the TextField
     *
     * @return username
     */
    public String getUsername() {
	return username.textProperty().get().trim();
    }

    /**
     * Get the password from the TextField
     *
     * @return password
     */
    public String getPassword() {
	return password.textProperty().get().trim();
    }

    /**
     * Set a custom action event when clicking the button
     *
     * @param e the custom action event to execute when the button is clicked
     */
    public void setOnLogin(EventHandler<ActionEvent> e) {
	this.action = e;
    }

    /**
     * Event that is called when the button is clicked
     *
     * @param evt Event to execute
     */
    @FXML
    public void onLogin(ActionEvent evt) {
	action.handle(evt);
    }

    @FXML
    public void onLoginEnter(KeyEvent evt) {
	if (evt.getCode() == KeyCode.ENTER) {
	    onLogin(null);
	}
    }

    public void resetPanel(boolean showNotification) {
	password.setText("");
	if (showNotification) {
	    PopupUtil.showNotification("Kon niet aanmelden", "Het wachtwoord of de gebruikersnaam is verkeerd.", PopupUtil.Notification.WARNING);
	}
    }
}
