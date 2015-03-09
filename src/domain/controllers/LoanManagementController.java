package domain.controllers;

import gui.LoanManagement;

/**
 *
 * @author Frederik
 */
public class LoanManagementController extends BaseController<LoanManagement> {

    public LoanManagementController(LoanManagement view) {
	super(view);
    }

    @Override
    public void updateToAuthenticatedUser() {

    }

}
