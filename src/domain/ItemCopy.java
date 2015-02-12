package domain;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Frederik
 */
@Entity
@Access(AccessType.PROPERTY)
public class ItemCopy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.FIELD)
    private int id;

    private final StringProperty location = new SimpleStringProperty();
    private final ObjectProperty<Item> item = new SimpleObjectProperty<>();
    private final ObjectProperty<Damage> damage = new SimpleObjectProperty<>();
    private int copyNumber;

    public ItemCopy() {
    }

    public ItemCopy(int copyNumber, String location, Item i, Damage d) {
	setCopyNumber(copyNumber);
	setLocation(location);
	setItem(i);
	setDamage(d);
    }

    public void setCopyNumber(int copyNumber) {
	this.copyNumber = copyNumber;
    }

    public int getCopyNumber() {
	return this.copyNumber;
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

    @ManyToOne(fetch = FetchType.EAGER)
    public Item getItem() {
	return this.item.get();
    }
}
