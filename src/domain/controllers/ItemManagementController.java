package domain.controllers;

import domain.User;
import gui.ItemManagement;
import gui.ScreenSwitcher;
import persistence.UserRepository;

/**
 *
 * @author Frederik
 */
public class ItemManagementController extends BaseController<ItemManagement> {

    public ItemManagementController(ItemManagement view, ScreenSwitcher sw) {
	super(view, sw);
    }

    @Override
    public void updateToAuthenticatedUser() {
	User u = UserRepository.getInstance().getAuthenticatedUser();

	if (u == null || u.getUserType() == null || u.getUserType() == User.UserType.STUDENT) {

	    return;
	}

	switch (u.getUserType()) {
	    case VOLUNTEER:

		break;

	    case TEACHER:

		break;
	}
    }

}
