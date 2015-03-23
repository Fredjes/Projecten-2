package domain;

import java.io.Serializable;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

/**
 * Representation of a real-life object containing the location, number for
 * real-life identification, damage and definition of an item.
 *
 * @author Frederik
 */
@Entity
@Access(AccessType.PROPERTY)
@NamedQueries({
    @NamedQuery(name = "ItemCopy.findAll", query = "SELECT ic FROM ItemCopy ic")
})
public class ItemCopy implements Serializable, Searchable {

    private int id;
    private final StringProperty location = new SimpleStringProperty();
    private final ObjectProperty<Item> item = new SimpleObjectProperty<>();
    private final ObjectProperty<Damage> damage = new SimpleObjectProperty<>();
    private final StringProperty copyNumber = new SimpleStringProperty();
    private final ObservableList<Loan> loans = FXCollections.observableArrayList();

    public ItemCopy() {
    }

    public ItemCopy(String copyNumber, String location, Item i, Damage d) {
	setCopyNumber(copyNumber);
	setLocation(location);
	setItem(i);
	setDamage(d);
    }

    @OneToMany(mappedBy = "itemCopy")
    public List<Loan> getLoans() {
	return this.loans;
    }

    @Transient
    public ObservableList<Loan> getObservableLoans() {
	return this.loans;
    }

    public void setLoans(List<Loan> loans) {
	this.loans.setAll(loans);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
	return this.id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public void setCopyNumber(String copyNumber) {
	this.copyNumber.set(copyNumber);
    }

    public String getCopyNumber() {
	return this.copyNumber.get();
    }

    public StringProperty copyNumberProperty() {
	return copyNumber;
    }

    public StringProperty locationProperty() {
	return location;
    }

    public String getLocation() {
	return location.get();
    }

    public void setLocation(String location) {
	this.location.set(location);
    }

    @Enumerated(EnumType.STRING)
    public Damage getDamage() {
	return damage.get();
    }

    public void setDamage(Damage damage) {
	this.damage.set(damage);
    }

    public ObjectProperty<Damage> damageProperty() {
	return damage;
    }

    public ObjectProperty<Item> itemProperty() {
	return item;
    }

    public void setItem(Item i) {
	this.item.set(i);
    }

    @ManyToOne
    public Item getItem() {
	return this.item.get();
    }

    @Override
    public int hashCode() {
	int hash = 7;
	hash = 97 * hash + this.id;
	return hash;
    }

    @Override
    public String toString() {
	try {
	    return this.getItem().getName() + " (Exemplaar #" + getCopyNumber() + ")";
	} catch (NullPointerException npex) {
	    return "Exemplaar #" + getCopyNumber();
	}
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final ItemCopy other = (ItemCopy) obj;
	if (this.id != other.id) {
	    return false;
	}
	return true;
    }

    @Override
    public boolean test(String query) {
	for (String t : query.split("\\s+")) {
	    boolean temp = SearchPredicate.containsIgnoreCase(getLocation(), t)
		    || SearchPredicate.containsIgnoreCase(getCopyNumber(), t)
		    || SearchPredicate.containsIgnoreCase(getDamage().toString(), t);

	    if (temp == false) {
		if (getItem() != null && !getItem().test(t)) {
		    return false;
		}
	    }
	}

	return true;
    }
}
