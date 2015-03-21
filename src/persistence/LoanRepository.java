package persistence;

import domain.Loan;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.EntityManager;

/**
 *
 * @author Frederik
 */
public class LoanRepository {

    private final ObservableList<Loan> loans = FXCollections.observableArrayList();
    private List<Loan> removedLoans = new ArrayList<>();

    /**
     * Will return all the loans that are in the repository, note that
     * {@link LoanRepository#sync()} should be called first. This isn't checked
     * in the method itself as it would cause the JavaFX UI Thread to block
     * while syncing in case the list is empty.
     *
     * @return
     */
    public ObservableList<Loan> getLoans() {
	return loans;
    }

    public void sync() {
	loans.setAll(JPAUtil.getInstance().getEntityManager().createNamedQuery("Loan.findAll", Loan.class).getResultList());
    }

    public void removeLoan(Loan l) {
	loans.remove(l);
	removedLoans.add(l);
    }

    public void addLoan(Loan l) {
	loans.add(l);
    }

    public void saveChanges() {
	EntityManager manager = JPAUtil.getInstance().getEntityManager();
	// Make sure no deleted loans will be persisted
	loans.removeAll(removedLoans);
	try {
	    manager.getTransaction().begin();

	    removedLoans.forEach(manager::remove);
	    loans.forEach(l -> {
		if (l.getId() == 0) {
		    manager.persist(l);
		} else {
		    manager.merge(l);
		}
	    });

	    manager.getTransaction().commit();
	} catch (Exception ex) {
	    ex.printStackTrace();
	    manager.getTransaction().rollback();
	}
    }
}
