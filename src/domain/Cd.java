package domain;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * Entity representing a cd.
 *
 * @author Frederik De Smedt
 */
@Entity
@Access(AccessType.PROPERTY)
public class Cd extends Item implements Serializable {

    private StringProperty artist = new SimpleStringProperty();
    private ObservableList<String> songs = FXCollections.observableArrayList();

    public Cd() {
	super();
    }

    public Cd(List<String> theme, String ageCategory, String title, String description, String artist) {
	super(title, description, theme, ageCategory);
	setArtist(artist);
    }

    public StringProperty artistProperty() {
	return artist;
    }

    public String getArtist() {
	return artist.get();
    }

    public void setArtist(String artist) {
	this.artist.set(artist);
    }

    @Transient
    public ObservableList<String> getObservableSongList() {
	return songs;
    }

    @ElementCollection
    public List<String> getSongList() {
	return songs;
    }

    public void setSongList(List<String> songList) {
	this.songs = FXCollections.observableList(songList);
    }

    @Override
    public String toString() {
	return getName() + " (" + getArtist() + ")";
    }

    @Override
    public boolean test(String query) {
	if (!super.getVisible()) {
	    return false;
	}

	outer:
	for (String t : query.split("\\s+")) {
	    boolean temp = SearchPredicate.containsIgnoreCase(getArtist(), t);

	    if (temp == false) {
		if (super.test(t)) {
		    continue;
		}

		for (String s : getSongList()) {
		    if (SearchPredicate.containsIgnoreCase(s, t)) {
			continue outer;
		    }
		}

		return false;
	    }
	}

	return true;
    }

    @Override
    public Importer createImporter() {
	return new CdImporter();
    }

    private class CdImporter extends ItemImporter<Cd> {

	private final Set<String> fieldSet = super.getFields();

	public CdImporter() {
	    super(Cd::new);
	    fieldSet.add("Artiest");
	}

	@Override
	public Set<String> getFields() {
	    return Collections.unmodifiableSet(fieldSet);
	}

	@Override
	public String predictField(String columnName) {
	    if (SearchPredicate.containsAnyIgnoreCase(columnName, "artiest", "zanger", "zangeres", "persoon")) {
		return "Artiest";
	    } else if (SearchPredicate.containsIgnoreCase(columnName, "cd")) {
		return "Titel";
	    } else {
		return super.predictField(columnName);
	    }
	}

	@Override
	public void setField(String field, String value) {
	    if (field.equals("Artiest")) {
		super.getCurrentEntity().setArtist(value);
	    } else {
		super.setField(field, value);
	    }
	}

    }
}
