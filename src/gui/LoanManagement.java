package gui;

import domain.Book;
import domain.Cache;
import domain.Cd;
import domain.Dvd;
import domain.FontCache;
import domain.Game;
import domain.Loan;
import domain.LocaleConfig;
import domain.SearchPredicate;
import domain.Setting.SettingType;
import domain.StoryBag;
import domain.controllers.LoanManagementListItemController;
import gui.dialogs.PopupUtil;
import java.util.Calendar;
import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import persistence.LoanRepository;
import persistence.SettingsManager;

/**
 * A GUI used to manage all {@link Loan}s.
 *
 * Uses {@link LoanRepository} to retrieve Loans.
 *
 * @author Frederik De Smedt
 */
public class LoanManagement extends BorderPane {

    @FXML
    private ListView<Loan> loanList;

    @FXML
    private Button returnButton, extendButton;

    @FXML
    private TextField searchBar;

    @FXML
    private StackPane contentStackPane;

    private boolean displayAll = false;

    private LoanManagementListItemController controller;

    private FilteredList<Loan> filteredList;
    private SearchPredicate predicate;

    public LoanManagement(LoanManagementListItemController controller) {
	FXUtil.loadFXML(this, "loan_management");
	this.controller = controller;
	predicate = new SearchPredicate(Loan.class, "");
	predicate.searchQueryProperty().bind(searchBar.textProperty());
	filteredList = new FilteredList<>(LoanRepository.getInstance().getLoans().sorted((l1, l2) -> l1.getDate().compareTo(l2.getDate())));
	loanList.setCellFactory(LoanManagementListItemCell.forListView());
	onSearchQuery();
	loanList.setItems(filteredList);

	returnButton.graphicProperty().addListener((obs, ov, nv) -> {
	    if (nv != null) {
		((Text) nv).setFont(FontCache.getIconFont(16));
	    }
	});

	extendButton.graphicProperty().addListener((obs, ov, nv) -> {
	    if (nv != null) {
		((Text) nv).setFont(FontCache.getIconFont(16));
	    }
	});

	// Show loading indicator
	ProgressIndicator loadingIndicator = new ProgressIndicator(-1);
	loadingIndicator.setMaxWidth(40);
	StackPane.setAlignment(loadingIndicator, Pos.CENTER);
	contentStackPane.getChildren().add(loadingIndicator);
	loadingIndicator.visibleProperty().bind(LoanRepository.getInstance().isLoaded().not());
	// End code loading indicator
    }

    @FXML
    public void onSearchQuery() {
	if (displayAll) {
	    filteredList.setPredicate(predicate::test);
	} else {
	    filteredList.setPredicate(p -> predicate.test(p) && !p.getReturned());
	}
    }

    @FXML
    public void onDisplayAllLoans() {
	displayAll = !displayAll;
	onSearchQuery();
    }

    @FXML
    public void onReturnLoan() {
	if (!loanList.getSelectionModel().isEmpty()) {
	    Loan selectedLoan = loanList.getSelectionModel().getSelectedItem();
	    if (!selectedLoan.getReturned()) {
		PopupUtil.showDamageQuestionPopOver(selectedLoan, returnButton, () -> Platform.runLater(() -> {
		    PopupUtil.showNotification("Uitlening teruggebracht", "De uitlening van " + selectedLoan.getUser().getName() + " is terug gebracht.");
		    onSearchQuery();
		}));
	    }
	}
    }

    @FXML
    public void onRenewLoanPeriod() {
	if (!loanList.getSelectionModel().isEmpty()) {
	    Loan selectedLoan = loanList.getSelectionModel().getSelectedItem();
	    SettingsManager sm = SettingsManager.INSTANCE;

	    if (selectedLoan.getItemCopy().getItem() instanceof Book && selectedLoan.getAmountOfExtensions() >= sm.getSettingValue(SettingType.EXTENSION_COUNT_BOOK)) {
		Platform.runLater(() -> PopupUtil.showNotification("Verlening", "Een boek kan maar " + sm.getSettingValue(SettingType.EXTENSION_COUNT_BOOK) + " keer worden verlengd.", PopupUtil.Notification.WARNING));
	    } else if ((selectedLoan.getItemCopy().getItem() instanceof Cd || selectedLoan.getItemCopy().getItem() instanceof Dvd) && selectedLoan.getAmountOfExtensions() >= sm.getSettingValue(SettingType.EXTENSION_COUNT_CD_DVD)) {
		Platform.runLater(() -> PopupUtil.showNotification("Verlening", "Een cd of dvd kan maar " + sm.getSettingValue(SettingType.EXTENSION_COUNT_CD_DVD) + " keer worden verlengd.", PopupUtil.Notification.WARNING));
	    } else if (selectedLoan.getItemCopy().getItem() instanceof Game && selectedLoan.getAmountOfExtensions() >= sm.getSettingValue(SettingType.EXTENSION_COUNT_GAME)) {
		Platform.runLater(() -> PopupUtil.showNotification("Verlening", "Een spelletje kan maar " + sm.getSettingValue(SettingType.EXTENSION_COUNT_GAME) + " keer worden verlengd.", PopupUtil.Notification.WARNING));
	    } else if (selectedLoan.getItemCopy().getItem() instanceof StoryBag && selectedLoan.getAmountOfExtensions() >= sm.getSettingValue(SettingType.EXTENSION_COUNT_STORYBAG)) {
		Platform.runLater(() -> PopupUtil.showNotification("Verlening", "Een verteltas kan maar " + sm.getSettingValue(SettingType.EXTENSION_COUNT_STORYBAG) + " keer worden verlengd.", PopupUtil.Notification.WARNING));
	    } else {
		Calendar newDate = Calendar.getInstance();
		newDate.setTime(selectedLoan.getDate().getTime());
		newDate.add(Calendar.DAY_OF_YEAR, sm.getSettingValue(SettingType.DAY_COUNT_LOAN_EXTENSION));
		selectedLoan.setDate(newDate);
		selectedLoan.setAmountOfExtensions(selectedLoan.getAmountOfExtensions() + 1);

		Cache.getLoanCache().get(selectedLoan).updateAll();
		LoanRepository.getInstance().addSyncListener(() -> {
		    Platform.runLater(() -> PopupUtil.showNotification("Verlengd", "De uitlening is verlengd tot " + LocaleConfig.DATE_FORMAT.format(newDate.getTime())));
		});
		
		LoanRepository.getInstance().saveLoan(selectedLoan);
	    }
	}
    }
}
