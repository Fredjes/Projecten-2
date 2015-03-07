package domain.controllers;

import javafx.scene.layout.GridPane;
import persistence.UserRepository;

/**
 *
 * @author Frederik
 */
public abstract class BaseController {

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

    /**
     * Retrieves the name of the currently logged in user by delegating to the
     * UserRepository.
     *
     * @return the name of the currently logged in user
     */
    public String getNameAuthenticatedUser() {
        return INSTANCE.getAuthenticatedUser().getName();
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
