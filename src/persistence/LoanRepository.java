package persistence;

import domain.Loan;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private static LoanRepository repositoryInstance;

    public static LoanRepository getInstance() {
	if (repositoryInstance == null) {
	    repositoryInstance = new LoanRepository();
	}

	return repositoryInstance;
    }

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
	Thread t = new Thread(() -> {
	    loans.setAll(JPAUtil.getInstance().getEntityManager().createNamedQuery("Loan.findAll", Loan.class).getResultList());
	    Logger.getLogger("Notification").log(Level.INFO, "Synchronized loan repository with database");
	});
	
	t.setName("Loan repository sync thread");
	t.start();
    }

    public void removeLoan(Loan l) {
	loans.remove(l);
	removedLoans.add(l);
    }

    public void addLoan(Loan l) {
	loans.add(l);
    }

    public void saveChanges() {
	Thread t = new Thread(() -> {
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
	});
	
	t.setName("Save loans thread");
	t.start();
    }

    public static void main(String[] args) {
	ItemRepository.getInstance().sync();
	UserRepository.getInstance().sync();

	try {
	    Thread.sleep(5000); // Waiting for async synchronization
	} catch (InterruptedException ex) {
	}

	Loan loan = new Loan(ItemRepository.getInstance().getItemCopies().get(0), UserRepository.getInstance().getUsers().get(0));
	Loan loan2 = new Loan(ItemRepository.getInstance().getItemCopies().get(1), UserRepository.getInstance().getUsers().get(1));
	Loan loan3 = new Loan(ItemRepository.getInstance().getItemCopies().get(2), UserRepository.getInstance().getUsers().get(2));
	Loan loan4 = new Loan(ItemRepository.getInstance().getItemCopies().get(3), UserRepository.getInstance().getUsers().get(2));

	LoanRepository.getInstance().addLoan(loan);
	LoanRepository.getInstance().addLoan(loan2);
	LoanRepository.getInstance().addLoan(loan3);
	LoanRepository.getInstance().addLoan(loan4);

	LoanRepository.getInstance().saveChanges();
    }
}
