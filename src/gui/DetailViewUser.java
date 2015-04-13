package gui;

import domain.User;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

public class DetailViewUser extends TabPane {

    @FXML
    private TextField name;

    @FXML
    private TextField classRoom;

    @FXML
    private ChoiceBox<User.UserType> userType;

    @FXML
    private TextField registerNumber;

    @FXML
    private TextField email;

    private User boundUser;

    public DetailViewUser() {
	this(null);
    }

    public DetailViewUser(User user) {
	FXUtil.loadFXML(this, "detailview_user");
	userType.setItems(FXCollections.observableArrayList(User.UserType.values()));
	bind(user);
    }

    public void bind(User user) {
	InvalidationListener userTypeChangeListener = o -> userType.getSelectionModel().select(boundUser.getUserType());

	if (boundUser != null) {
	    Bindings.unbindBidirectional(name.textProperty(), boundUser.nameProperty());
	    Bindings.unbindBidirectional(email.textProperty(), boundUser.emailProperty());
	    Bindings.unbindBidirectional(classRoom.textProperty(), boundUser.classRoomProperty());
	    Bindings.unbindBidirectional(registerNumber.textProperty(), boundUser.registerNumberProperty());
	    boundUser.userTypeProperty().removeListener(userTypeChangeListener);;
	    boundUser.userTypeProperty().unbind();
	}

	boundUser = user;

	if (user != null) {
	    Bindings.bindBidirectional(name.textProperty(), user.nameProperty());
	    Bindings.bindBidirectional(email.textProperty(), user.emailProperty());
	    Bindings.bindBidirectional(classRoom.textProperty(), user.classRoomProperty());
	    Bindings.bindBidirectional(registerNumber.textProperty(), user.registerNumberProperty());
	    userType.getSelectionModel().select(user.getUserType());
	    user.userTypeProperty().addListener(userTypeChangeListener);
	    user.userTypeProperty().bind(userType.getSelectionModel().selectedItemProperty());
	}
    }
}
