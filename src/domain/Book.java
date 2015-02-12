package domain;

public class Book extends Item {

    private String title;
    private String author;
    private String publisher;

    public Book() {
        super();
        this.title = "fredje in wonderland";
        this.author = "brentje codiction";
        this.publisher = "pj";
    }

    public Book(String theme, String ageCategory, String location,
            Damage damage, String title, String author, String publisher) {

        super(theme, ageCategory, location, damage);
        this.title = title;
        this.author = author;
        this.publisher = publisher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
