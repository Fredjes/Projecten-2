package gui;

import domain.FilterOption;
import domain.User;
import java.io.IOException;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class DetailViewUser extends TabPane implements Binding<User> {

    @FXML
    private TextField name;
    @FXML
    private TextArea classRoom;
    @FXML
    private TextField email;
    @FXML
    private ChoiceBox<FilterOption> userType;
    @FXML
    private ImageView image;

    private User boundedUser;

    public DetailViewUser() {

	FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/gui/detailview_user.fxml"));

	try {
	    loader.setRoot(this);
	    loader.setController(this);
	    loader.load();

	} catch (IOException ex) {
	    throw new RuntimeException(ex);
	}
    }

    @Override
    public void bind(User t) {
	if (boundedUser != null) {
	    Bindings.unbindBidirectional(name.textProperty(), boundedUser.nameProperty());
	    Bindings.unbindBidirectional(classRoom.textProperty(), boundedUser.classRoomProperty());
	    Bindings.unbindBidirectional(email.textProperty(), boundedUser.emailProperty());
//	    Bindings.unbindBidirectional(image.imageProperty(), boundedUser.imageProperty());
	}

	Bindings.bindBidirectional(name.textProperty(), t.nameProperty());
	Bindings.bindBidirectional(classRoom.textProperty(), t.classRoomProperty());
	Bindings.bindBidirectional(email.textProperty(), t.emailProperty());
	userType.setItems(FXCollections.observableArrayList(FilterOption.values()));
	// TODO: Add image-property for user
//	Bindings.bindBidirectional(image.imageProperty(), t.imageProperty());
	this.boundedUser = t;
    }

}
