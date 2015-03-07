package domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Transient;

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
}
