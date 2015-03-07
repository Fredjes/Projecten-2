package domain.controllers;

import gui.ItemManagement;
import persistence.UserRepository;

/**
 *
 * @author Frederik
 */
public class ItemManagementController extends BaseController<ItemManagement> {

    @Override
    public void updateToAuthenticatedUser(ItemManagement root) {
	super.removeSecuredNavigationNodes(root.getNavigationGrid());
	boolean loggedOut = UserRepository.getInstance().getAuthenticatedUser() == null;
	root.getAuthenicatedUserLabel().setText(loggedOut ? "" : "Welkom " + UserRepository.getInstance().getAuthenticatedUser().getName());
	root.getLoginButton().setText(loggedOut ? "Aanmelden" : "Afmelden");
    }

}
