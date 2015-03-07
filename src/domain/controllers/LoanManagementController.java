package domain.controllers;

import gui.LoanManagement;
import persistence.UserRepository;

/**
 *
 * @author Frederik
 */
public class LoanManagementController extends BaseController<LoanManagement> {

    @Override
    public void updateToAuthenticatedUser(LoanManagement root) {
	super.removeSecuredNavigationNodes(root.getNavigationGrid());
	boolean loggedOut = UserRepository.getInstance().getAuthenticatedUser() == null;
	root.getAuthenicatedUserLabel().setText(loggedOut ? "" : "Welkom " + UserRepository.getInstance().getAuthenticatedUser().getName());
	root.getLoginButton().setText(loggedOut ? "Aanmelden" : "Afmelden");
    }

}
