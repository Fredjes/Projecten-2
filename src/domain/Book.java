package domain;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Book extends Item {

    private StringProperty title = new SimpleStringProperty();
    private StringProperty author = new SimpleStringProperty();
    private StringProperty publisher = new SimpleStringProperty();

    public Book() {
        super();
    }

    public Book(String theme, String ageCategory, String location,
            Damage damage, String title, String author, String publisher) {

        super(theme, ageCategory, location, damage);
        setTitle(title);
        setAuthor(author);
        setPublisher(publisher);
    }

    public StringProperty titleProperty() {
        return title;
    }

    public StringProperty authorProperty() {
        return author;
    }

    public StringProperty publisherProperty() {
        return publisher;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
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
