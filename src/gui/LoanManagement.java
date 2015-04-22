package gui;

import domain.Icon;
import domain.IconConfig;
import domain.Loan;
import domain.LocaleConfig;
import domain.SearchPredicate;
import domain.controllers.LoanManagementListItemController;
import gui.dialogs.PopupUtil;
import java.util.Calendar;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import persistence.ItemRepository;
import persistence.LoanRepository;

/**
 *
 * @author Frederik De Smedt
 */
public class LoanManagement extends BorderPane {

    @FXML
    private ListView<Loan> loanList;

    @FXML
    private Label iconReturn, iconExtend;

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
        filteredList = new FilteredList<>(LoanRepository.getInstance().getLoans());
        loanList.setCellFactory(LoanManagementListItemCell.forListView());
        onSearchQuery();
        loanList.setItems(filteredList);
        setIcons();
    }

    private void setIcons() {
        // Mouse events for triggering button hover when hovering over the icon
        iconReturn.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent event) -> {
            returnButton.fireEvent(event);
        });
        iconReturn.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent event) -> {
            returnButton.fireEvent(event);
        });

        iconExtend.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent event) -> {
            extendButton.fireEvent(event);
        });
        iconExtend.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent event) -> {
            extendButton.fireEvent(event);
        });

        // Setting icons for the buttons
        Icon i = IconConfig.getIconFor("icon-extend");
        iconExtend.setText(i.getIcon());

        i = IconConfig.getIconFor("icon-return");
        iconReturn.setText(i.getIcon());
	FXUtil.loadFXML(this, "loan_management");
	this.controller = controller;
	predicate = new SearchPredicate(Loan.class, "");
	predicate.searchQueryProperty().bind(searchBar.textProperty());
	filteredList = new FilteredList<>(LoanRepository.getInstance().getLoans());
	loanList.setCellFactory(LoanManagementListItemCell.forListView());
	onSearchQuery();
	loanList.setItems(filteredList);

	// Show temporary loading indicator
	ProgressIndicator loadingIndicator = new ProgressIndicator(-1);
	loadingIndicator.setMaxWidth(50);
	StackPane.setAlignment(loadingIndicator, Pos.CENTER);
	contentStackPane.getChildren().add(loadingIndicator);
	if (filteredList.size() == 0) {
	    Runnable removeIndicator = () -> Platform.runLater(() -> contentStackPane.getChildren().remove(loadingIndicator));
	    final BooleanProperty prop = new SimpleBooleanProperty(false);
	    filteredList.addListener((Observable obs) -> {
		if (!prop.get()) {
		    prop.set(true);
		} else {
		    removeIndicator.run();
		}
	    });
	    LoanRepository.getInstance().addSyncListener(removeIndicator);
	}
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
            Calendar newDate = Calendar.getInstance();
            newDate.setTime(selectedLoan.getDate().getTime());
            newDate.add(Calendar.WEEK_OF_MONTH, 1);
            selectedLoan.setDate(newDate);

            LoanRepository.getInstance().addSyncListener(() -> {
                Platform.runLater(() -> PopupUtil.showNotification("Verlengd", "De uitlening is verlengd tot " + LocaleConfig.DATE_FORMAT.format(newDate.getTime())));
            });

            LoanRepository.getInstance().saveLoan(selectedLoan);
        }
    }
}
