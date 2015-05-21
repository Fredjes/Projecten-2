package gui;

import domain.User;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;
import persistence.UserRepository;

/**
 * A separate GUI component that can be used to change a password and checks for
 * all specified constraints.
 *
 * @author Frederik
 */
public class PasswordChanger extends VBox {

    @FXML
    private PasswordField passwordConfirmation;

    @FXML
    private PasswordField password;

    @FXML
    private Label exceptionLabel;

    private PopOver popOver = new PopOver(this);
    private User user;

    public PasswordChanger() {
	FXUtil.loadFXML(this, "password_change");
	popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_CENTER);
	popOver.setAutoHide(true);
	popOver.setDetachable(false);
	popOver.setOnHiding(e -> {
	    this.user = null;
	    exceptionLabel.setText("");
	    password.setText("");
	    passwordConfirmation.setText("");
	});

	password.setOnAction(e -> passwordConfirmation.requestFocus());
	passwordConfirmation.setOnAction(e -> {
	    onSetPassword();
	    password.requestFocus();
	});
    }

    @FXML
    public void onSetPassword() {
	if (!password.getText().equals(passwordConfirmation.getText())) {
	    exceptionLabel.setText("Wachtwoorden komen niet overeen.");
	    return;
	}

	final String password = this.password.getText();

	try {
	    if (UserRepository.getInstance().validatePassword(password)) {
		if (user != null) {
		    user.setPasswordHash(UserRepository.getInstance().generatePasswordHash(password));
		    UserRepository.getInstance().saveUser(user);
		}

		popOver.hide();
	    }
	} catch (IllegalArgumentException ex) {
	    exceptionLabel.setText(ex.getMessage());
	}
    }

    public void show(Node parent, User user) {
	popOver.show(parent);
	this.user = user;
    }
}
