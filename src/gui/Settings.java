package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import persistence.SettingsManager;

/**
 *
 * @author Brent C.
 */
public class Settings extends ScrollPane {

    public Settings() {
	FXUtil.loadFXML(this, "settings");

	updateGui();
    }

    @FXML
    private TextField txtHost;

    @FXML
    private TextField txtPort;

    @FXML
    private TextField txtSchema;

    @FXML
    private TextField txtGebruikersnaam;

    @FXML
    private PasswordField txtWachtwoord;

    @FXML
    private TextField txtPdfPath;

    @FXML
    void onDeleteItems(ActionEvent event) {

    }

    @FXML
    void onDeleteUsers(ActionEvent event) {

    }

    @FXML
    void onPdfPathSearch(ActionEvent event) {

    }

    @FXML
    void onSave(ActionEvent event) {

    }

    public void updateGui() {
	txtHost.setText(SettingsManager.instance.getString("host"));
	txtPort.setText(SettingsManager.instance.getString("port"));
	txtSchema.setText(SettingsManager.instance.getString("schema"));
	txtGebruikersnaam.setText(SettingsManager.instance.getString("username"));
	txtWachtwoord.setText(SettingsManager.instance.getString("password"));
	txtPdfPath.setText(SettingsManager.instance.getString("pdfPath"));
    }

}
