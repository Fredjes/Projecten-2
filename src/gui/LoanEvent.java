package gui;

import domain.Loan;
import javafx.event.Event;
import javafx.event.EventType;

/**
 *
 * @author Frederik
 */
public class LoanEvent extends Event {

    private Loan loan;
    
    public LoanEvent(Loan l) {
	this(null, l);
    }

    public LoanEvent(EventType<? extends Event> eventType, Loan l) {
	super(eventType);
	this.loan = l;
    }

    public Loan getLoan() {
	return loan;
    }
}
