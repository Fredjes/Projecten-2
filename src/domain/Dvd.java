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

    @Override
    public void bind(Item item) {
	super.bind(item);
	if (item instanceof Dvd) {
	    Dvd dvd = (Dvd) item;
	    director.set(dvd.getDirector());
	}
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
    public Importer createImporter() {
	return new DvdImporter();
    }

    private class DvdImporter extends ItemImporter<Dvd> {

	private final Set<String> fieldSet = super.getFields();

	public DvdImporter() {
	    super(Dvd::new);
	    fieldSet.add("Regisseur");
	}

	@Override
	public Set<String> getFields() {
	    return Collections.unmodifiableSet(fieldSet);
	}

	@Override
	public void setField(String field, String value) {
	    if (field.equals("Regisseur")) {
		getCurrentEntity().setDirector(value);
	    } else {
		super.setField(field, value);
	    }
	}

	@Override
	public String predictField(String columnName) {
	    if (SearchPredicate.containsAnyIgnoreCase(columnName, "regisseur", "producer", "ontwikkelaar")) {
		return "Regisseur";
	    } else {
		return super.predictField(columnName);
	    }
	}

    }

    @Override
    public int getVersionID() {
	return ChangeConfig.DVD_VERSION_ID;
    }
}
