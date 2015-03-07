package domain.controllers;

import domain.User.UserType;
import gui.dialogs.LoginPanel;
import gui.dialogs.PopupManager;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import javafx.beans.property.BooleanProperty;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.PopOver;
import persistence.UserRepository;

/**
 *
 * @author Frederik
 */
public abstract class BaseController<E extends Node> {

    private final UserRepository INSTANCE = UserRepository.getInstance();

    /**
     * Log in using the email of the user and a non-encrypted password.
     *
     * @param email the email of the user
     * @param password the password of the user
     * @return true if the email and password are correct
     */
    public boolean login(String email, String password) {
	return INSTANCE.authenticate(email, password);
    }

    public void processLoginRequest(E root, Button loginButton, Label authenticatedUserLabel, BooleanProperty loggedIn) {
	if (loggedIn.get()) {
	    INSTANCE.logout();
	    loggedIn.set(false);
	    loginButton.setText("Aanmelden");
	    authenticatedUserLabel.setText("");
	} else {
	    LoginPanel loginPanel = new LoginPanel();
	    PopOver pop = PopupManager.showPopOver(loginButton, loginPanel);
	    loginPanel.setOnLogin(e -> {
		if (login(loginPanel.getUsername(), loginPanel.getPassword())) {
		    authenticatedUserLabel.setText("Welkom " + INSTANCE.getAuthenticatedUser().getName());
		    pop.hide();
		    loginButton.setText("Afmelden");
		    loggedIn.set(true);
		} else {
		    loginPanel.resetPanel(true);
		    authenticatedUserLabel.setText("");
		    loggedIn.set(false);
		}
	    });
	}
    }

    /**
     * Retrieves the name of the currently logged in user by delegating to the
     * UserRepository.
     *
     * @return the name of the currently logged in user
     */
    public String getNameAuthenticatedUser() {
	return INSTANCE.getAuthenticatedUser().getName();
    }

    private GridPane originalGrid;

    /**
     * Removes the navigation nodes for which a higher security clearance is
     * required.
     */
    public void removeSecuredNavigationNodes(GridPane navigationGrid) {
	
    }

    public abstract void updateToAuthenticatedUser(E root);
}
