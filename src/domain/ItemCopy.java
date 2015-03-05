package domain;

import java.io.Serializable;
import java.util.Arrays;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * Representation of a real-life object containing the location, number for real-life identification, damage and definition of an item.
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

    public ItemCopy() {
    }

    public ItemCopy(String copyNumber, String location, Item i, Damage d) {
	setCopyNumber(copyNumber);
	setLocation(location);
	setItem(i);
	setDamage(d);
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

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
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
	boolean currentResult = Arrays.stream(query.split("\\s")).anyMatch(t -> SearchPredicate.containsIgnoreCase(getLocation(), t)
		|| SearchPredicate.containsIgnoreCase(getCopyNumber(), t)
		|| SearchPredicate.containsIgnoreCase(getDamage().toString(), t));
	
	return currentResult || (getItem() != null && getItem().test(query));
    }
}
