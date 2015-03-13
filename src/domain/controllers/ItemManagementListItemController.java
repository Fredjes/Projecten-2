package domain.controllers;

import domain.User;
import static domain.User.UserType.TEACHER;
import static domain.User.UserType.VOLUNTEER;
import gui.ItemManagementListItem;
import gui.ScreenSwitcher;
import persistence.UserRepository;

/**
 *
 * @author Frederik
 */
public class ItemManagementListItemController extends BaseController<ItemManagementListItem> {

    public ItemManagementListItemController(ItemManagementListItem view, ScreenSwitcher sw) {
	super(view, sw);
    }

    @Override
    public void updateToAuthenticatedUser() {
	User u = UserRepository.getInstance().getAuthenticatedUser();
	if (u == null || u.getUserType() == null || u.getUserType() == User.UserType.STUDENT) {
	    hideNode(getView().getManagementTab());
	    return;
	}

	switch (u.getUserType()) {
	    case VOLUNTEER:
		hideNode(getView().getAddCopyButton());
		break;

	    case TEACHER:
		showNode(getView().getAddCopyButton());
		break;
	}
    }
}
