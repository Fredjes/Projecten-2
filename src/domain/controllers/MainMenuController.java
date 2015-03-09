package domain.controllers;

import domain.User;
import gui.MainMenu;
import persistence.UserRepository;

/**
 *
 * @author Frederik
 */
public class MainMenuController extends BaseController<MainMenu> {

    public MainMenuController(MainMenu view) {
	super(view);
    }

    @Override
    public void updateToAuthenticatedUser() {
	MainMenu mainMenu = getView();
	User u = UserRepository.getInstance().getAuthenticatedUser();

	if (u == null || u.getUserType() == null || u.getUserType() == User.UserType.STUDENT) {
	    hideNode(mainMenu.getExcelImporteren());
	    hideNode(mainMenu.getGebruikersBeheren());
	    hideNode(mainMenu.getVoorwerpenBeheren());
	    hideNode(mainMenu.getUitleningenBeheren());
	    return;
	}

	switch (u.getUserType()) {
	    case VOLUNTEER:
		hideNode(mainMenu.getExcelImporteren());
		hideNode(mainMenu.getGebruikersBeheren());
		hideNode(mainMenu.getVoorwerpenBeheren());
		showNode(mainMenu.getUitleningenBeheren());
		break;

	    case TEACHER:
		showNode(mainMenu.getExcelImporteren());
		showNode(mainMenu.getGebruikersBeheren());
		showNode(mainMenu.getVoorwerpenBeheren());
		showNode(mainMenu.getUitleningenBeheren());
		break;
	}

    }

}
