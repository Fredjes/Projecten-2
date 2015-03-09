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
	Titlebar titlebar = getView();
	User u = UserRepository.getInstance().getAuthenticatedUser();

	if (u == null || u.getUserType() == null || u.getUserType() == User.UserType.STUDENT) {
	    hideNode(titlebar.getExcelImporteren());
	    hideNode(titlebar.getGebruikersBeheren());
	    hideNode(titlebar.getVoorwerpenBeheren());
	    hideNode(titlebar.getUitleningenBeheren());
	    return;
	}

	switch (u.getUserType()) {
	    case VOLUNTEER:
		hideNode(titlebar.getExcelImporteren());
		hideNode(titlebar.getGebruikersBeheren());
		hideNode(titlebar.getVoorwerpenBeheren());
		showNode(titlebar.getUitleningenBeheren());
		break;

	    case TEACHER:
		showNode(titlebar.getExcelImporteren());
		showNode(titlebar.getGebruikersBeheren());
		showNode(titlebar.getVoorwerpenBeheren());
		showNode(titlebar.getUitleningenBeheren());
		break;
	}
    }

}
