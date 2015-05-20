package gui;

import gui.dialogs.PopupUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.util.Duration;
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
	updateValues();
	if (SettingsManager.instance.save()) {
	    PopupUtil.showNotification("Configuratie", "Configuratie opgeslagen. Gelieve te herstarten!", PopupUtil.Notification.INFORMATION, Duration.seconds(15));
	} else {
	    PopupUtil.showNotification("Configuratie", "Configuratie niet opgeslagen!", PopupUtil.Notification.ERROR, Duration.seconds(15));
	}
    }

    public void updateValues() {
	SettingsManager.instance.setString("host", txtHost.getText());
	SettingsManager.instance.setString("port", txtPort.getText());
	SettingsManager.instance.setString("schema", txtSchema.getText());
	SettingsManager.instance.setString("username", txtGebruikersnaam.getText());
	SettingsManager.instance.setString("password", txtWachtwoord.getText());
	SettingsManager.instance.setString("pdfPath", txtPdfPath.getText());
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
