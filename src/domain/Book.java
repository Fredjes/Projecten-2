package domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    public Importer createImporter() {
	return new BookImporter();
    }

    private class BookImporter extends ItemImporter<Book> {

	private final Set<String> fieldSet = super.getFields();

	public BookImporter() {
	    super(Book::new);
	    fieldSet.addAll(Arrays.asList(new String[]{"Auteur", "Titel", "Uitgeverij"}));
	}

	@Override
	public Set<String> getFields() {
	    return Collections.unmodifiableSet(fieldSet);
	}

	@Override
	public String predictField(String columnName) {
	    if (SearchPredicate.containsAnyIgnoreCase(columnName, "auteur", "schrijver", "maker")) {
		return "Auteur";
	    } else if (SearchPredicate.containsIgnoreCase(columnName, "boek")) {
		return "Titel";
	    } else if (SearchPredicate.containsAnyIgnoreCase(columnName, "uitgeverij", "bedrijf", "firma")) {
		return "Uitgeverij";
	    } else {
		return super.predictField(columnName);
	    }
	}

	@Override
	public void setField(String field, String value) {
	    switch (field) {
		case "Auteur":
		    super.getCurrentEntity().setAuthor(value);
		    return;
		case "Uitgeverij":
		    super.getCurrentEntity().setPublisher(value);
		    return;
		default:
		    super.setField(field, value);
	    }
	}
    }

    @Override
    public int getVersionID() {
	return ChangeConfig.BOOK_VERSION_ID;
    }

}
