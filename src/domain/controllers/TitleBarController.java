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
	    hideNode(getView().getExcelImporteren());
	    hideNode(getView().getGebruikersBeheren());
	    hideNode(getView().getVoorwerpenBeheren());
	    hideNode(getView().getUitleningenBeheren());
	    return;
	}

	switch (u.getUserType()) {
	    case VOLUNTEER:
		hideNode(getView().getExcelImporteren());
		hideNode(getView().getGebruikersBeheren());
		hideNode(getView().getVoorwerpenBeheren());
		showNode(getView().getUitleningenBeheren());
		break;

	    case TEACHER:
		showNode(getView().getExcelImporteren());
		showNode(getView().getGebruikersBeheren());
		showNode(getView().getVoorwerpenBeheren());
		showNode(getView().getUitleningenBeheren());
		break;
	}
    }

}
