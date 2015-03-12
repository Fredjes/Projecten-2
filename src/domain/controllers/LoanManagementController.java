package domain.controllers;

import gui.LoanManagement;
import gui.ScreenSwitcher;

/**
 *
 * @author Frederik
 */
public class LoanManagementController extends BaseController<LoanManagement> {

    public LoanManagementController(LoanManagement view, ScreenSwitcher sw) {
	super(view, sw);
    }

    @Override
    public void updateToAuthenticatedUser() {
    }

}
