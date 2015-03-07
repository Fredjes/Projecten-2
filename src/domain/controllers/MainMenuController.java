package domain.controllers;

import gui.MainMenu;

/**
 *
 * @author Frederik
 */
public class MainMenuController extends BaseController<MainMenu> {

    @Override
    public void updateToAuthenticatedUser(MainMenu mainMenu) {
	super.removeSecuredNavigationNodes(mainMenu.getNavigationGrid());
    }
    
}
