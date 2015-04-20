package domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

/**
 * Item-subclass representing a game.
 *
 * @author Frederik De Smedt
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
	outer:
	for (String t : query.split("\\s+")) {
	    boolean temp = SearchPredicate.containsIgnoreCase(getBrand(), t);

	    if (temp == false) {
		if (super.test(t)) {
		    continue;
		}

		return false;
	    }
	}

	return true;
    }

    @Override
    public Map<String, BiConsumer<String, Game>> createHeaderList() {
	Map<String, BiConsumer<String, Game>> map = super.createHeaderList();
	map.put("Merk", new BiConsumer<String, Game>() {

	    public void accept(String d, Game g) {
		g.setBrand(d);
	    }
	});
	return map;
    }
    
}
