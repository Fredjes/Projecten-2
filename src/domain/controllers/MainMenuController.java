package domain.controllers;

import gui.MainMenu;
import persistence.UserRepository;

/**
 *
 * @author Frederik
 */
public class MainMenuController extends BaseController<MainMenu> {

    @Override
    public void updateToAuthenticatedUser(MainMenu mainMenu) {
	super.removeSecuredNavigationNodes(mainMenu.getNavigationGrid());
	boolean loggedOut = UserRepository.getInstance().getAuthenticatedUser() == null;
	mainMenu.getAuthenicatedUserLabel().setText(loggedOut ? "" : "Welkom " + UserRepository.getInstance().getAuthenticatedUser().getName());
	mainMenu.getLoginButton().setText(loggedOut ? "Aanmelden" : "Afmelden");
    }
    
}
