package gui;

import domain.FontCache;
import domain.Loan;
import domain.SearchPredicate;
import domain.User;
import domain.User.UserType;
import gui.dialogs.PopupUtil;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import persistence.UserRepository;

/**
 * The GUI to manage every {@link User}.
 *
 * Uses the {@link UserRepository} to retrieve its users.
 *
 * @author Frederik De Smedt
 */
public class UserManagement extends BorderPane {

    @FXML
    private ListView<User> userList;

    @FXML
    private TextField searchBar;

    @FXML
    private StackPane contentStackPane;

    @FXML
    private Label iconSave;

    @FXML
    private Button addButton, removeButton, saveButton;

    private DetailViewUser detailViewUser;

    private SearchPredicate predicate;
    private FilteredList<User> filteredList;
    private boolean saved = true;

    @FXML
    public void onSearchQuery() {
	filteredList.setPredicate(predicate::test);
    }

    @FXML
    public void onAdd() {
	User user = new User(User.UserType.STUDENT);
	user.setVisible(true);
	UserRepository.getInstance().add(user);
	searchBar.setText("");
	user.setName(" ");
	onSearchQuery();
	user.setName("");
	userList.getSelectionModel().select(user);
    }

    @FXML
    public void onRemove() {
	if (!userList.getSelectionModel().isEmpty()) {
	    int index = userList.getSelectionModel().getSelectedIndex();
	    User user = userList.getSelectionModel().getSelectedItem();

	    List<Loan> loans = user.getLoans();
	    if (user.getUserType() == UserType.TEACHER && UserRepository.getInstance().getUsers().stream().filter(u -> u.getVisible() && u.getUserType() == UserType.TEACHER).count() <= 1) {
		PopupUtil.showNotification("Laatste leerkracht", "Kan de laatste leerkracht niet verwijderen.", PopupUtil.Notification.WARNING);
		return;
	    }
	    if (user.getLoans().stream().anyMatch(l -> !l.getReturned())) {
		PopupUtil.showNotification("Nog uitleningen", user.getName() + " heeft nog uitleningen open staan.", PopupUtil.Notification.WARNING);
		return;
	    }

	    if (PopupUtil.confirm("Gebruiker verwijderen", String.format("Bent u zeker dat u %s wilt verwijderen?", user.getName()))) {
		UserRepository.getInstance().remove(user);
		filteredList.setPredicate(predicate::test);
		if (index > 0) {
		    userList.getSelectionModel().select(index - 1);
		}
	    }
	}
    }

    @FXML
    public void onSave() {
	final Runnable r = () -> Platform.runLater(() -> PopupUtil.showNotification("Opgeslaan", "De wijzigingen zijn succesvol opgeslaan."));
	PopupUtil.showNotification("Opslaan", "De wijzigingen worden opgeslaan.");
	if (!saved) {
	    saved = true;
	    UserRepository.getInstance().addSyncListener(r);
	    UserRepository.getInstance().saveChanges();
	} else {
	    r.run();
	}
    }

    public UserManagement() {
	FXUtil.loadFXML(this, "user_management");
	predicate = new SearchPredicate(User.class, "");
	predicate.searchQueryProperty().bind(searchBar.textProperty());
	filteredList = new FilteredList<>(UserRepository.getInstance().getUsers());
	userList.setCellFactory(UserManagementListItemCell.forListView());
	detailViewUser = new DetailViewUser();
	onSearchQuery();
	userList.setItems(filteredList);
	userList.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
	    saved = false;
	    if (nv == null) {
		Platform.runLater(() -> super.setBottom(null));
	    } else {
		detailViewUser.bind(nv);
		Platform.runLater(() -> super.setBottom(detailViewUser));
	    }
	});

	FXUtil.loadFXML(this, "user_management");
	predicate = new SearchPredicate(User.class, "");
	predicate.searchQueryProperty().bind(searchBar.textProperty());
	filteredList = new FilteredList<>(UserRepository.getInstance().getUsers());
	userList.setCellFactory(UserManagementListItemCell.forListView());
	detailViewUser = new DetailViewUser();
	onSearchQuery();
	userList.setItems(filteredList);
	saveButton.graphicProperty().addListener((obs, ov, nv) -> {
	    if (nv != null) {
		((Text) nv).setFont(FontCache.getIconFont(16));
	    }
	});
	userList.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
	    saved = false;
	    if (nv == null) {
		Platform.runLater(() -> super.setBottom(null));
	    } else {
		detailViewUser.bind(nv);
		Platform.runLater(() -> super.setBottom(detailViewUser));
	    }
	});
    }
}
