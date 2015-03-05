package domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

/**
 * Item-subclass representing a game.
 *
 * @author Frederik
 */
@Entity
@Access(AccessType.PROPERTY)
public class Game extends Item implements Serializable {

    private StringProperty brand = new SimpleStringProperty();

    public Game() {
	super();
    }

    public Game(List<String> theme, String ageCategory, String name, String description, String brand) {
	super(name, description, theme, ageCategory);
	setBrand(brand);
    }

    public StringProperty brandProperty() {
	return brand;
    }

    public String getBrand() {
	return brand.get();
    }

    public void setBrand(String brand) {
	this.brand.set(brand);
    }

    @Override
    public String toString() {
	return getName() + " (" + getBrand() + ")";
    }

    @Override
    public boolean test(String query) {
	boolean currentMatch = Arrays.stream(query.split("\\s*")).anyMatch(t -> SearchPredicate.containsIgnoreCase(getBrand(), t));
	
	return currentMatch || super.test(query);
    }
    
    
}
