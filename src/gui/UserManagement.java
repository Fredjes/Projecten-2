package gui;

import domain.SearchPredicate;
import domain.User;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import persistence.UserRepository;

/**
 *
 * @author Frederik De Smedt
 */
public class UserManagement extends BorderPane {
    
    @FXML
    private ListView<User> userList;

    @FXML
    private TextField searchBar;

    @FXML
    private HBox detailView;

    @FXML
    public void onSearchQuery() {
	filteredList.setPredicate(predicate::test);
    }
    
    private SearchPredicate predicate;
    private FilteredList<User> filteredList;

    public UserManagement() {
	FXUtil.loadFXML(this, "user_management");
	predicate = new SearchPredicate(User.class, "");
	predicate.searchQueryProperty().bind(searchBar.textProperty());
	filteredList = new FilteredList<>(UserRepository.getInstance().getUsers());
	userList.setCellFactory(UserManagementListItemCell.forListView());
	onSearchQuery();
	userList.setItems(filteredList);
    }
}
