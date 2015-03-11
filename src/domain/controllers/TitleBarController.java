package domain.controllers;

import domain.User;
import gui.ScreenSwitcher;
import gui.Titlebar;
import persistence.UserRepository;

/**
 *
 * @author Frederik
 */
public class TitleBarController extends BaseController<Titlebar> {

    public TitleBarController(Titlebar view, ScreenSwitcher sw) {
	super(view, sw);
    }

    @Override
    public void updateToAuthenticatedUser() {
	User u = UserRepository.getInstance().getAuthenticatedUser();

	if (u == null || u.getUserType() == null || u.getUserType() == User.UserType.STUDENT) {
	    hideNode(getView().getExcelImporteren(), "excel");
	    hideNode(getView().getGebruikersBeheren(), "gebruikersBeheren");
	    hideNode(getView().getVoorwerpenBeheren(), "voorwerpenBeheren");
	    hideNode(getView().getUitleningenBeheren(), "uitleningenBeheren");
	    return;
	}

	switch (u.getUserType()) {
	    case VOLUNTEER:
		hideNode(getView().getExcelImporteren(), "excel");
		hideNode(getView().getGebruikersBeheren(), "gebruikersBeheren");
		showNode("voorwerpenBeheren");
		showNode("uitleningenBeheren");
		break;

	    case TEACHER:
		showNode("excel");
		showNode("gebruikersBeheren");
		showNode("voorwerpenBeheren");
		showNode("uitleningenBeheren");
		break;
	}
    }

}
