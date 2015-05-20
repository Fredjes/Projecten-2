package gui;

import domain.Item;
import domain.User;
import gui.dialogs.PopupUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
	List<Item> items = new ArrayList();
	for (Item item : ItemRepository.getInstance().getItems()) {
	    if (!item.getItemCopies().stream().flatMap(ic -> ic.getLoans().stream()).anyMatch(l -> !l.getReturned())) {
		items.add(item);
	    }
	}

	items.forEach(System.out::println);
    }

    @FXML
    void onDeleteUsers() {
	for (User u : UserRepository.getInstance().getUsersByPredicate(uu -> uu.getUserType() == User.UserType.STUDENT)) {
	    UserRepository.getInstance().remove(u);
	}
	final Runnable r = () -> Platform.runLater(() -> PopupUtil.showNotification("Opgeslaan", "De wijzigingen zijn succesvol opgeslaan."));
	PopupUtil.showNotification("Opslaan", "De wijzigingen worden opgeslaan.");
	UserRepository.getInstance().addSyncListener(r);
	UserRepository.getInstance().saveChanges();
    }

    @FXML
    void onPdfPathSearch() {
	File pdfPath = new File(SettingsManager.instance.getString("pdfPath"));
	if (!pdfPath.exists()) {
	    pdfPath.mkdirs();
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
