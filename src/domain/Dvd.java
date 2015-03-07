package domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

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
}
