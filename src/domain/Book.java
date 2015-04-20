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
 * Item-subclass representing a book.
 *
 * @author Frederik De Smedt
 */
@Entity
@Access(AccessType.PROPERTY)
public class Book extends Item implements Serializable {

    private StringProperty author = new SimpleStringProperty();
    private StringProperty publisher = new SimpleStringProperty();

    public Book() {
	super();
    }

    public Book(List<String> theme, String ageCategory, String title, String description, String author, String publisher) {
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

    @Override
    public String toString() {
	return getName() + (getAuthor().isEmpty() ? "" : " (" + getAuthor() + ")");
    }

    @Override
    public boolean test(String query) {
	for (String t : query.split("\\s+")) {
	    boolean temp = SearchPredicate.containsIgnoreCase(getAuthor(), t)
		    || SearchPredicate.containsIgnoreCase(getPublisher(), t);

	    if (temp == false) {
		if (!super.test(t)) {
		    return false;
		}
	    }
	}

	return true;
    }

    @Override
    public Map<String, BiConsumer<String, Book>> createHeaderList() {
	Map<String, BiConsumer<String, Book>> temp = super.createHeaderList();

	temp.put("Auteur", new BiConsumer<String, Book>() {

	    public void accept(String d, Book b) {
		b.setAuthor(d);
	    }
	});

	temp.put("Uitgeverij", new BiConsumer<String, Book>() {

	    public void accept(String d, Book b) {
		b.setPublisher(d);
	    }
	});

	return temp;
    }

    @Override
    public Map<String, Predicate<String>> createHeaderAssignmentList() {
	Map<String, Predicate<String>> map = super.createHeaderAssignmentList();
	
	map.put("Auteur", new Predicate<String>() {

	    @Override
	    public boolean test(String t) {
		return SearchPredicate.containsAnyIgnoreCase(t, "auteur", "schrijver", "maker");
	    }
	});
	
	final Predicate<String> original = map.get("Titel");
	map.put("Titel", new Predicate<String>() {

	    @Override
	    public boolean test(String t) {
		return original.test(t) || SearchPredicate.containsIgnoreCase(t, "boek");
	    }
	});
	
	map.put("Uitgeverij", new Predicate<String>() {

	    @Override
	    public boolean test(String t) {
		return SearchPredicate.containsAnyIgnoreCase(t, "uitgeverij", "bedrijf", "firma");
	    }
	});
	
	return map;
    }

}
