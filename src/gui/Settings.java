package gui;

import domain.Loan;
import domain.Setting.SettingType;
import domain.User;
import gui.dialogs.PopupUtil;
import java.io.File;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import persistence.ItemRepository;
import persistence.SettingsManager;
import persistence.UserRepository;

/**
 * The advanced settings window only displayed for administrators.
 *
 * @author Brent C.
 */
public class Settings extends VBox {

    public Settings() {
	FXUtil.loadFXML(this, "settings");
	updateGui();
    }

    @FXML
    private TextField dayCountLoan;

    @FXML
    private TextField txtHost;

    @FXML
    private TextField loanExtensionCountStoryBag;

    @FXML
    private TextField loanExtensionCountCdDvd;

    @FXML
    private PasswordField txtWachtwoord;

    @FXML
    private TextField loanExtensionCountGame;

    @FXML
    private TextField dayCountLoanExtension;

    @FXML
    private TextField txtPort;

    @FXML
    private TextField loanCountGame;

    @FXML
    private TextField loanExtensionCountBook;

    @FXML
    private TextField loanCountCdDvd;

    @FXML
    private TextField loanCountBook;

    @FXML
    private TextField txtSchema;

    @FXML
    private TextField loanCountStoryBag;

    @FXML
    private Button changeAdminPasswordButton;

    @FXML
    private TextField txtPdfPath;

    @FXML
    private TextField txtGebruikersnaam;

    @FXML
    private void onDeleteItems() {
	ItemRepository.getInstance().getItemsByPredicate(i -> i.getItemCopies().stream().flatMap(ic -> ic.getLoans().stream()).allMatch(Loan::getReturned)).forEach(ItemRepository.getInstance()::remove);
	PopupUtil.showNotification("Opslaan", "De wijzigingen worden opgeslaan.");
	ItemRepository.getInstance().saveChanges();
    }

    @FXML
    private void onDeleteUsers() {
	UserRepository.getInstance().getUsersByPredicate(u -> u.getUserType() == User.UserType.STUDENT && u.getLoans().stream().allMatch(Loan::getReturned)).forEach(UserRepository.getInstance()::remove);
	PopupUtil.showNotification("Opslaan", "De wijzigingen worden opgeslaan.");
	UserRepository.getInstance().saveChanges();
    }

    @FXML
    private void onPdfPathSearch() {
	File pdfPath = new File(SettingsManager.INSTANCE.getString("pdfPath"));

	if (!pdfPath.exists()) {
	    pdfPath = new File(System.getProperty("user.home"));
	}

	File f = PopupUtil.showDirectoryChooser(null, "Kies een folder om de PDFs in op te slaan", pdfPath);
	txtPdfPath.setText(f.getAbsolutePath());
	updateValues();
    }

    @FXML
    private void onSave() {
	updateValues();
	if (SettingsManager.INSTANCE.save()) {
	    PopupUtil.showNotification("Configuratie", "Configuratie opgeslagen. Gelieve te herstarten!", PopupUtil.Notification.INFORMATION, Duration.seconds(15));
	} else {
	    PopupUtil.showNotification("Configuratie", "Configuratie niet opgeslagen!", PopupUtil.Notification.ERROR, Duration.seconds(15));
	}
    }

    @FXML
    private void onChangeAdminPassword() {
	PasswordChanger changer = new PasswordChanger();
	Future<String> passwordFuture = changer.show(changeAdminPasswordButton);
	Thread t = new Thread(() -> {
	    try {
		String password = passwordFuture.get();
		if (password != null && !password.isEmpty()) {
		    SettingsManager.INSTANCE.setSettingString(SettingType.ADMIN_PASSWORD, new String(MessageDigest.getInstance("SHA-512").digest(password.getBytes(Charset.forName("UTF-8"))), Charset.forName("UTF-8")));
		}
	    } catch (InterruptedException | ExecutionException | NoSuchAlgorithmException ex) {
	    }
	});
	
	t.start();
    }

    public void updateValues() {
	SettingsManager.INSTANCE.setString("host", txtHost.getText());
	SettingsManager.INSTANCE.setString("port", txtPort.getText());
	SettingsManager.INSTANCE.setString("schema", txtSchema.getText());
	SettingsManager.INSTANCE.setString("username", txtGebruikersnaam.getText());
	SettingsManager.INSTANCE.setString("password", txtWachtwoord.getText());
	SettingsManager.INSTANCE.setString("pdfPath", txtPdfPath.getText());
	SettingsManager.INSTANCE.setSettingInt(SettingType.DAY_COUNT_LOAN, dayCountLoan.getText());
	SettingsManager.INSTANCE.setSettingInt(SettingType.DAY_COUNT_LOAN_EXTENSION, dayCountLoanExtension.getText());
	SettingsManager.INSTANCE.setSettingInt(SettingType.EXTENSION_COUNT_BOOK, loanExtensionCountBook.getText());
	SettingsManager.INSTANCE.setSettingInt(SettingType.EXTENSION_COUNT_CD_DVD, loanExtensionCountCdDvd.getText());
	SettingsManager.INSTANCE.setSettingInt(SettingType.EXTENSION_COUNT_GAME, loanExtensionCountGame.getText());
	SettingsManager.INSTANCE.setSettingInt(SettingType.EXTENSION_COUNT_STORYBAG, loanExtensionCountStoryBag.getText());
	SettingsManager.INSTANCE.setSettingInt(SettingType.LOAN_COUNT_BOOK, loanCountBook.getText());
	SettingsManager.INSTANCE.setSettingInt(SettingType.LOAN_COUNT_CD_DVD, loanCountCdDvd.getText());
	SettingsManager.INSTANCE.setSettingInt(SettingType.LOAN_COUNT_GAME, loanCountGame.getText());
	SettingsManager.INSTANCE.setSettingInt(SettingType.LOAN_COUNT_STORYBAG, loanCountStoryBag.getText());
    }

    public void updateGui() {
	txtHost.setText(SettingsManager.INSTANCE.getString("host"));
	txtPort.setText(SettingsManager.INSTANCE.getString("port"));
	txtSchema.setText(SettingsManager.INSTANCE.getString("schema"));
	txtGebruikersnaam.setText(SettingsManager.INSTANCE.getString("username"));
	txtWachtwoord.setText(SettingsManager.INSTANCE.getString("password"));
	txtPdfPath.setText(SettingsManager.INSTANCE.getString("pdfPath"));
	dayCountLoan.setText(String.valueOf(SettingsManager.INSTANCE.getSettingValue(SettingType.DAY_COUNT_LOAN)));
	dayCountLoanExtension.setText(String.valueOf(SettingsManager.INSTANCE.getSettingValue(SettingType.DAY_COUNT_LOAN_EXTENSION)));
	loanExtensionCountBook.setText(String.valueOf(SettingsManager.INSTANCE.getSettingValue(SettingType.EXTENSION_COUNT_BOOK)));
	loanExtensionCountCdDvd.setText(String.valueOf(SettingsManager.INSTANCE.getSettingValue(SettingType.EXTENSION_COUNT_CD_DVD)));
	loanExtensionCountGame.setText(String.valueOf(SettingsManager.INSTANCE.getSettingValue(SettingType.EXTENSION_COUNT_GAME)));
	loanExtensionCountStoryBag.setText(String.valueOf(SettingsManager.INSTANCE.getSettingValue(SettingType.EXTENSION_COUNT_STORYBAG)));
	loanCountBook.setText(String.valueOf(SettingsManager.INSTANCE.getSettingValue(SettingType.LOAN_COUNT_BOOK)));
	loanCountCdDvd.setText(String.valueOf(SettingsManager.INSTANCE.getSettingValue(SettingType.LOAN_COUNT_CD_DVD)));
	loanCountGame.setText(String.valueOf(SettingsManager.INSTANCE.getSettingValue(SettingType.LOAN_COUNT_GAME)));
	loanCountStoryBag.setText(String.valueOf(SettingsManager.INSTANCE.getSettingValue(SettingType.LOAN_COUNT_STORYBAG)));
    }
}
