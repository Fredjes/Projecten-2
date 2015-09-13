package persistence;

import domain.Loan;
import domain.PdfExporter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.EntityManager;

/**
 * Repository for {@link Loan}s.
 *
 * @author Frederik De Smedt
 */
public class LoanRepository extends Repository<Loan> {

    private final ObservableList<Loan> loans = FXCollections.observableArrayList();
    private List<Loan> removedLoans = new ArrayList<>();

    private static LoanRepository repositoryInstance;

    private LoanRepository() {
	super();
    }

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
	    synchronized (this) {
		loaded.set(false);
		loans.setAll(JPAUtil.getInstance().getEntityManager().createNamedQuery("Loan.findAll", Loan.class).getResultList());
		Logger.getLogger("Notification").log(Level.INFO, "Synchronized loan repository with database");
		super.triggerListeners();
		PdfExporter.saveLoans();
		PdfExporter.saveLoanHistory();
		loaded.set(true);
	    }

	    getLoans().stream().map(Loan::itemCopyProperty).map(ObjectProperty::get).forEach(ItemRepository.getInstance()::manage);
	    updateManagedLoans();
	});

	t.setName("Loan repository sync thread");
	t.start();
    }

    public void remove(Loan l) {
	loans.remove(l);
	removedLoans.add(l);
    }

    public void add(Loan l) {
	loans.add(l);
    }

    public void saveChanges() {
	Thread t = new Thread(() -> {
	    synchronized (this) {
		loaded.set(false);
		List<Loan> loanCopy = loans;
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

		PdfExporter.saveLoans();
		PdfExporter.saveLoanHistory();
		loaded.set(true);
	    }
	});

	t.setName("Save loans thread");
	t.start();
    }

    public void saveLoan(Loan loan) {
	Thread t = new Thread(() -> {
	    synchronized (this) {
		EntityManager manager = JPAUtil.getInstance().getEntityManager();
		manager.getTransaction().begin();

		if (loan.getId() != 0) {
		    manager.merge(loan);
		} else {
		    manager.persist(loan);
		}

		manager.getTransaction().commit();
		super.triggerListeners();
		PdfExporter.saveLoans();
		PdfExporter.saveLoanHistory();
		LoanRepository.getInstance().sync();
	    }
	});

	t.setName("Loan save thread: " + loan.getId());
	t.start();
    }

    private List<WeakReference<Loan>> foreignLoans = new ArrayList<>();

    public void manage(Loan loan) {
	if (!loans.contains(loan)) {
	    loans.add(loan);
	}

	foreignLoans.removeIf(r -> r.get() == null);
	if (foreignLoans.stream().map(WeakReference::get).noneMatch(loan::equals)) {
	    foreignLoans.add(new WeakReference<>(loan));
	}
	
	loans.stream().filter(loan::equals).filter(l -> loan != l).forEach(l -> {
	    loan.amountOfExtensions().bindBidirectional(l.amountOfExtensions());
	    loan.dateProperty().bindBidirectional(l.dateProperty());
	    loan.itemCopyProperty().bindBidirectional(l.itemCopyProperty());
	    loan.returnedProperty().bindBidirectional(l.returnedProperty());
	    loan.startDateProperty().bindBidirectional(l.startDateProperty());
	    loan.userProperty().bindBidirectional(l.userProperty());
	});
    }
    
    private void updateManagedLoans() {
	foreignLoans.removeIf(r -> r.get() == null);
	foreignLoans.stream().map(WeakReference::get).filter(Objects::nonNull).forEach(this::manage);
    }

    public static void main(String[] args) {
	ItemRepository.getInstance().sync();
	UserRepository.getInstance().sync();

	try {
	    Thread.sleep(5000); // Waiting for async synchronization
	} catch (InterruptedException ex) {
	}

//	Loan loan = new Loan(ItemRepository.getInstance().getItemCopies().get(0), UserRepository.getInstance().getUsers().get(0));
//	Loan loan2 = new Loan(ItemRepository.getInstance().getItemCopies().get(1), UserRepository.getInstance().getUsers().get(1));
//	Loan loan3 = new Loan(ItemRepository.getInstance().getItemCopies().get(2), UserRepository.getInstance().getUsers().get(2));
//	Loan loan4 = new Loan(ItemRepository.getInstance().getItemCopies().get(3), UserRepository.getInstance().getUsers().get(2));
//	LoanRepository.getInstance().add(loan);
//	LoanRepository.getInstance().add(loan2);
//	LoanRepository.getInstance().add(loan3);
//	LoanRepository.getInstance().add(loan4);
	LoanRepository.getInstance().saveChanges();
    }

    private BooleanProperty loaded = new SimpleBooleanProperty(false);

    @Override
    public BooleanProperty isLoaded() {
	return loaded;
    }
}
