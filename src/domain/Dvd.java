package domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

/**
 * An entity-class representing a dvd.
 *
 * @author Frederik
 */
@Entity
@Access(AccessType.PROPERTY)
public class Dvd extends Item implements Serializable {

    private StringProperty director = new SimpleStringProperty();

    public Dvd() {
	super();
    }

    public Dvd(List<String> theme, String ageCategory, String title, String description, String director) {
	super(title, description, theme, ageCategory);
	setDirector(director);
    }

    public StringProperty directorProperty() {
	return director;
    }

    public String getDirector() {
	return director.get();
    }

    public void setDirector(String director) {
	this.director.set(director);
    }

    @Override
    public String toString() {
	return getName() + " (" + getDirector() + ")";
    }

    @Override
    public boolean test(String query) {
	for (String t : query.split("\\s+")) {
	    boolean temp = SearchPredicate.containsIgnoreCase(getDirector(), t);

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
    public Map<String, BiConsumer<String, Dvd>> createHeaderList() {
	Map<String, BiConsumer<String, Dvd>> map = super.createHeaderList();
	map.put("Regisseur", new BiConsumer<String, Dvd>() {

	    public void accept(String da, Dvd dv) {
		dv.setDirector(da);
	    }
	});
	return map;
    }

    @Override
    public Map<String, Predicate<String>> createHeaderAssignmentList() {
	Map<String, Predicate<String>> map = super.createHeaderAssignmentList();
	map.put("Regisseur", new Predicate<String>() {

	    @Override
	    public boolean test(String t) {
		return SearchPredicate.containsAnyIgnoreCase(t, "regisseur", "producer", "ontwikkelaar");
	    }
	});
	final Predicate<String> original = map.get("Titel");
	map.put("Titel", new Predicate<String>() {

	    @Override
	    public boolean test(String t) {
		return original.test(t) || SearchPredicate.containsIgnoreCase(t, "dvd");
	    }
	});
	return map;
    }
    
    @Override
    public int getVersionID() {
	return ChangeConfig.DVD_VERSION_ID;
    }
}
