package domain;

import domain.annotations.Display;
import java.io.Serializable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

@Entity
@Access(AccessType.PROPERTY)
public class Dvd extends Item implements Serializable {

    /*
     * http://stackoverflow.com/questions/1966503/does-imdb-provide-an-api
     * http://www.imdb.com/interfaces
     * IMDB api
     */
    private StringProperty director = new SimpleStringProperty();

    public Dvd() {
        super();
    }

    public Dvd(String theme, String ageCategory, String title, String description, String director) {
        super(title, description, theme, ageCategory);
        setDirector(director);
    }

    @Display(name = "Director")
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
}
