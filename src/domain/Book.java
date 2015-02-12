package domain;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
@Access(AccessType.FIELD)
public class Book extends Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Transient
    private StringProperty author = new SimpleStringProperty();

    @Transient
    private StringProperty publisher = new SimpleStringProperty();

    public Book() {
	super();
    }

    public Book(String theme, String ageCategory, String title, String description, String author, String publisher) {
	super(title, description, theme, ageCategory);
	setAuthor(author);
	setPublisher(publisher);
    }

    public StringProperty authorProperty() {
	return author;
    }

    public StringProperty publisherProperty() {
	return publisher;
    }

    @Access(AccessType.PROPERTY)
    public String getAuthor() {
	return author.get();
    }

    public void setAuthor(String author) {
	this.author.set(author);
    }

    @Access(AccessType.PROPERTY)
    public String getPublisher() {
	return publisher.get();
    }

    public void setPublisher(String publisher) {
	this.publisher.set(publisher);
    }
}
