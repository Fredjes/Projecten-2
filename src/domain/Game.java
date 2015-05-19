package domain;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Set;
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

    @Override
    public void bind(Item item) {
	super.bind(item);
	if (item instanceof Item) {
	    brand.set(((Game) item).getBrand());
	}
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
    public Importer createImporter() {
	return new GameImporter();
    }

    private class GameImporter extends ItemImporter<Game> {

	private Set<String> fieldSet = super.getFields();

	public GameImporter() {
	    super(Game::new);
	    fieldSet.add("Uitgeverij");
	}

	@Override
	public Set<String> getFields() {
	    return Collections.unmodifiableSet(fieldSet);
	}

	@Override
	public String predictField(String columnName) {
	    if (SearchPredicate.containsAnyIgnoreCase(columnName, "merk", "bedrijf", "producer", "ontwikkelaar", "uitgever")) {
		return "Uitgeverij";
	    } else if (SearchPredicate.containsIgnoreCase(columnName, "spel")) {
		return "Titel";
	    } else {
		return super.predictField(columnName);
	    }
	}

	@Override
	public void setField(String field, String value) {
	    if (field.equals("Uitgeverij")) {
		getCurrentEntity().setBrand(value);
	    } else {
		super.setField(field, value);
	    }
	}
    }

    @Override
    public int getVersionID() {
	return ChangeConfig.GAME_VERSION_ID;
    }
}
