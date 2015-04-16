package gui;

import domain.User;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 *
 * @author Frederik
 */
public class UserManagementListItem extends HBox {

    @FXML
    private Text name;

    @FXML
    private Text classroom;

    @FXML
    private Text email;
    
    public UserManagementListItem(User user) {
	FXUtil.loadFXML(this, "listview_user");
	name.textProperty().bindBidirectional(user.nameProperty());
	classroom.textProperty().bind(user.classRoomProperty());
	email.textProperty().bind(user.emailProperty());
    }
    
}
