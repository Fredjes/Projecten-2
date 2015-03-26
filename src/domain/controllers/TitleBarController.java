package domain.controllers;

import domain.User;
import gui.ScreenSwitcher;
import gui.Titlebar;
import persistence.UserRepository;

/**
 *
 * @author Frederik De Smedt
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
	    //hideNode(getView().getVoorwerpenBeheren(), "voorwerpenBeheren");
	    hideNode(getView().getUitleningenBeheren(), "uitleningenBeheren");
	    getView().getVoorwerpenBeheren().getStyleClass().remove("icon-voorwerpen-beheren");
	    getView().getVoorwerpenBeheren().getStyleClass().add("icon-search");
	    return;
	}

	switch (u.getUserType()) {
	    case VOLUNTEER:
		hideNode(getView().getExcelImporteren(), "excel");
		hideNode(getView().getGebruikersBeheren(), "gebruikersBeheren");
		showNode("voorwerpenBeheren");
		showNode("uitleningenBeheren");
		getView().getVoorwerpenBeheren().getStyleClass().remove("icon-voorwerpen-beheren");
		getView().getVoorwerpenBeheren().getStyleClass().add("icon-search");
		break;

	    case TEACHER:
		showNode("voorwerpenBeheren");
		showNode("uitleningenBeheren");
		showNode("gebruikersBeheren");
		showNode("excel");
		getView().getVoorwerpenBeheren().getStyleClass().add("icon-voorwerpen-beheren");
		getView().getVoorwerpenBeheren().getStyleClass().remove("icon-search");
		break;
	}
    }

}
