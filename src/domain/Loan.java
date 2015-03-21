package domain;

import java.io.Serializable;
import java.util.Calendar;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Frederik
 */
@Entity
@Access(AccessType.PROPERTY)
@NamedQueries({
    @NamedQuery(name = "Loan.findAll", query = "SELECT l FROM Loan l")
})
public class Loan implements Serializable, Searchable {

    private int id;

    private final ObjectProperty<ItemCopy> itemCopy = new SimpleObjectProperty<>();
    private final ObjectProperty<User> user = new SimpleObjectProperty<>();
    private final ObjectProperty<Calendar> date = new SimpleObjectProperty<>();

    public Loan() {
    }

    public Loan(ItemCopy copy, User user) {
	this.itemCopy.set(copy);
	this.user.set(user);
	date.set(Calendar.getInstance());
    }

    @ManyToOne
    public ItemCopy getItemCopy() {
	return itemCopy.get();
    }

    public void setItemCopy(ItemCopy copy) {
	this.itemCopy.set(copy);
    }

    public ObjectProperty<ItemCopy> itemCopyProperty() {
	return itemCopy;
    }

    @ManyToOne
    public User getUser() {
	return user.get();
    }

    public void setUser(User user) {
	this.user.set(user);
    }

    public ObjectProperty<User> userProperty() {
	return this.user;
    }

    @Temporal(TemporalType.DATE)
    public Calendar getDate() {
	return this.date.get();
    }

    public void setDate(Calendar date) {
	this.date.set(date);
    }

    public ObjectProperty<Calendar> dateProperty() {
	return this.date;
    }

    @Override
    public boolean test(String t) {
	for (String token : t.split("\\s+")) {
	    boolean inDate = SearchPredicate.containsIgnoreCase(String.valueOf(date.get().get(Calendar.YEAR)), token)
		    || SearchPredicate.containsIgnoreCase(String.valueOf(date.get().get(Calendar.MONTH)), token)
		    || SearchPredicate.containsIgnoreCase(String.valueOf(date.get().get(Calendar.DAY_OF_MONTH)), token);

	    if (inDate || user.get().test(token) || itemCopy.get().test(token)) {
		continue;
	    } else {
		return false;
	    }
	}

	return true;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }
}
