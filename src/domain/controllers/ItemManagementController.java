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
	    getSwitcher().openMainMenu();
	    return;
	}

	switch (u.getUserType()) {
	    case VOLUNTEER:
		hideNode(getView().getListCommands(), "listTools");
		//hideNode(getView().getDetailView());
		hideNode(getView().getSaveButton(), "save");
		getView().getFilteredList().forEach((item) -> hideNode(item.getAddCopyButton()));
		break;

	    case TEACHER:
		showNode("listTools");
		showNode("save");
		getView().getFilteredList().forEach((item) -> showNode(item.getAddCopyButton()));
		break;
	}
    }

}
