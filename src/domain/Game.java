package domain;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
@Access(AccessType.FIELD)
public class Game extends Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Transient
    private StringProperty brand = new SimpleStringProperty();

    public Game() {
	super();
    }

    public Game(String theme, String ageCategory, String name, String description, String brand) {
	super(name, description, theme, ageCategory);
	setBrand(brand);
    }

    public StringProperty brandProperty() {
	return brand;
    }

    @Access(AccessType.PROPERTY)
    public String getBrand() {
	return brand.get();
    }

    public void setBrand(String brand) {
	this.brand.set(brand);
    }
}
