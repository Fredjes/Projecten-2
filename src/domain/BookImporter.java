package domain;

/**
 *
 * @author Frederik
 */
public class BookImporter extends ItemImporter<Book> {

    public BookImporter() {
	super(Book::new);
    }

    @Override
    public String predictField(String columnName) {
	if (SearchPredicate.containsAnyIgnoreCase(columnName, "auteur", "schrijver", "maker")) {
	    return "Auteur";
	} else if (SearchPredicate.containsIgnoreCase(columnName, "boek")){
	    return "Titel";
	} else if (SearchPredicate.containsAnyIgnoreCase(columnName, "uitgeverij", "bedrijf", "firma")){
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