package domain;

import domain.annotations.Display;
import java.io.Serializable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

/**
 * Item-subclass representing a book.
 *
 * @author Frederik
 */
@Entity
@Access(AccessType.PROPERTY)
public class Book extends Item implements Serializable {

    private StringProperty author = new SimpleStringProperty();
    private StringProperty publisher = new SimpleStringProperty();

    public Book() {
	super();
    }

    public Book(String theme, String ageCategory, String title, String description, String author, String publisher) {
	super(title, description, theme, ageCategory);
	setAuthor(author);
	setPublisher(publisher);
    }

    @Display(name = "Autheur")
    public StringProperty authorProperty() {
	return author;
    }

    @Display(name = "Uitgeverij")
    public StringProperty publisherProperty() {
	return publisher;
    }

    public String getAuthor() {
	return author.get();
    }

    public void setAuthor(String author) {
	this.author.set(author);
    }

    public String getPublisher() {
	return publisher.get();
    }

    public void setPublisher(String publisher) {
	this.publisher.set(publisher);
    }
}