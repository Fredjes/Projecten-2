package domain.controllers;

import domain.User;
import gui.LoanManagementListItem;
import gui.ScreenSwitcher;
import persistence.UserRepository;

/**
 *
 * @author Frederik De Smedt
 */
public class LoanManagementListItemController extends BaseController<LoanManagementListItem> {

    public LoanManagementListItemController(LoanManagementListItem view, ScreenSwitcher sw) {
	super(view, sw);
    }

    @Override
    public void updateToAuthenticatedUser() {
	User authenticatedUser = UserRepository.getInstance().getAuthenticatedUser();
	if (authenticatedUser != null && authenticatedUser.getUserType() == User.UserType.TEACHER) {
	    showNode(getView().getEditLoanButton());
	} else {
	    hideNode(getView().getEditLoanButton());
	}
    }

}
