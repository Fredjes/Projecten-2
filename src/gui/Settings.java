package gui;

import domain.Loan;
import domain.User;
import gui.dialogs.PopupUtil;
import java.io.File;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import persistence.ItemRepository;
import persistence.SettingsManager;
import persistence.UserRepository;

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
    void onDeleteItems() {
	ItemRepository.getInstance().getItemsByPredicate(i -> i.getItemCopies().stream().flatMap(ic -> ic.getLoans().stream()).allMatch(Loan::getReturned)).forEach(ItemRepository.getInstance()::remove);
	final Runnable r = () -> Platform.runLater(() -> PopupUtil.showNotification("Opgeslaan", "De wijzigingen zijn succesvol opgeslaan."));
	PopupUtil.showNotification("Opslaan", "De wijzigingen worden opgeslaan.");
	ItemRepository.getInstance().addSyncListener(r);
	ItemRepository.getInstance().saveChanges();
    }

    @FXML
    void onDeleteUsers() {
	UserRepository.getInstance().getUsersByPredicate(u -> u.getUserType() == User.UserType.STUDENT && u.getLoans().stream().allMatch(Loan::getReturned)).forEach(UserRepository.getInstance()::remove);
	final Runnable r = () -> Platform.runLater(() -> PopupUtil.showNotification("Opgeslaan", "De wijzigingen zijn succesvol opgeslaan."));
	PopupUtil.showNotification("Opslaan", "De wijzigingen worden opgeslaan.");
	UserRepository.getInstance().addSyncListener(r);
	UserRepository.getInstance().saveChanges();
    }

    @FXML
    void onPdfPathSearch() {
	File pdfPath = new File(SettingsManager.instance.getString("pdfPath"));
	
	if(!pdfPath.exists()){
	    pdfPath = new File(System.getProperty("user.home"));
	}
	
	File f = PopupUtil.showDirectoryChooser(null, "Kies een folder om de PDFs in op te slaan", pdfPath);
	txtPdfPath.setText(f.getAbsolutePath());
	updateValues();
    }

    @FXML
    void onSave() {
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
