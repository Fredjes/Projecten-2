package domain.controllers;

import domain.User;
import gui.ItemManagement;
import gui.ScreenSwitcher;
import persistence.UserRepository;

/**
 *
 * @author Frederik De Smedt
 */
public class ItemManagementController extends BaseController<ItemManagement> {

    public ItemManagementController(ItemManagement view, ScreenSwitcher sw) {
	super(view, sw);
    }

    @Override
    public void updateToAuthenticatedUser() {
	if (getView() == null) {
	    return;
	}
	
	User u = UserRepository.getInstance().getAuthenticatedUser();

	if (u == null || u.getUserType() == null || u.getUserType() == User.UserType.STUDENT) {
	    hideNode(getView().getListCommands(), "listTools");
	    hideNode(getView().getSaveButton(), "save");
	    return;
	}

	switch (u.getUserType()) {
	    case VOLUNTEER:
		hideNode(getView().getListCommands(), "listTools");
		hideNode(getView().getSaveButton(), "save");
		getView().getChildren().remove(getView().getDetailView());
		break;

	    case TEACHER:
		showNode("listTools");
		showNode("save");
		break;
	}

	getView().updateList();
    }

}
