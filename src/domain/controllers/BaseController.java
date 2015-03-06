package domain.controllers;

import javafx.scene.layout.GridPane;

/**
 *
 * @author Frederik
 */
public abstract class BaseController {

    /**
     * Log in using the email of the user and a non-encrypted password.
     *
     * @param email the email of the user
     * @param password the password of the user
     * @return true if the email and password are correct
     */
    public boolean login(String email, String password) {
	throw new UnsupportedOperationException("UserRepository required");
    }

    /**
     * Retrieves the name of the currently logged in user by delegating to the
     * UserRepository.
     *
     * @return the name of the currently logged in user
     */
    public String getNameAuthenticatedUser() {
	throw new UnsupportedOperationException("UserRepository required");
    }

    /**
     * Removes the navigation nodes for which a higher security clearance is
     * required.
     */
    public void removeSecuredNavigationNodes(GridPane navigationGrid) {
	throw new UnsupportedOperationException("UserRepository required");
    }
    
    public abstract void updateToAuthenticatedUser();
}
