package domain.controllers;

import domain.User;
import gui.MainMenu;
import gui.ScreenSwitcher;
import persistence.UserRepository;

/**
 * Controller for MainMenu.
 *
 * @author Frederik De Smedt
 */
public class MainMenuController extends BaseController<MainMenu> {

    public MainMenuController(MainMenu view, ScreenSwitcher sw) {
	super(view, sw);
    }

    @Override
    public void updateToAuthenticatedUser() {
	User u = UserRepository.getInstance().getAuthenticatedUser();

	if (u == null || u.getUserType() == null || u.getUserType() == User.UserType.STUDENT) {
	    hideNode(getView().getExcelImporteren(), "excel");
	    hideNode(getView().getGebruikersBeheren(), "gebruikersBeheren");
	    //hideNode(getView().getVoorwerpenBeheren(), "voorwerpenBeheren");
	    hideNode(getView().getUitleningenBeheren(), "uitleningenBeheren");
	    getView().getLblVoorwerpen().setText("Voorwerpen opzoeken");
	    return;
	}

	switch (u.getUserType()) {
	    case VOLUNTEER:
		hideNode(getView().getExcelImporteren(), "excel");
		hideNode(getView().getGebruikersBeheren(), "gebruikersBeheren");
		showNode("voorwerpenBeheren");
		showNode("uitleningenBeheren");
		getView().getLblVoorwerpen().setText("Voorwerpen");
		getView().getVoorwerpenBeherenIcon().getStyleClass().remove("icon-voorwerpen-beheren");
		getView().getVoorwerpenBeherenIcon().getStyleClass().add("icon-search");
		break;

	    case TEACHER:
		showNode("excel");
		showNode("gebruikersBeheren");
		showNode("voorwerpenBeheren");
		showNode("uitleningenBeheren");
		getView().getLblVoorwerpen().setText("Voorwerpen");
		break;
	}
    }

}
