package domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

/**
 * Represents the definition of every item stored, containing all shared
 * properties of every item.
 *
 * @author Frederik De Smedt
 */
@Entity
@Access(AccessType.PROPERTY)
@NamedQueries({
    @NamedQuery(name = "Item.findAll", query = "SELECT i FROM Item i")
})
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Item implements Serializable, Searchable, Importable {

    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final ObservableList<String> theme = FXCollections.observableArrayList();
    private final StringProperty ageCategory = new SimpleStringProperty();

    private final ObservableList<ItemCopy> itemCopies = FXCollections.<ItemCopy>observableArrayList();

    private final ObjectProperty<Image> image = new SimpleObjectProperty<>();

    private int id;

    public Item() {
    }

    public Item(String name, String description, List<String> theme, String ageCategory) {
	setThemes(theme);
	setAgeCategory(ageCategory);
	setName(name);
	setDescription(description);
    }

    @Transient
    public ObservableList<String> getThemeFX() {
	return theme;
    }

    @Transient
    public ObservableList<ItemCopy> getObservableItemCopies() {
	return itemCopies;
    }

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    public List<ItemCopy> getItemCopies() {
	return itemCopies;
    }

    public void setItemCopies(List<ItemCopy> itemCopies) {
	this.itemCopies.setAll(itemCopies);
    }

    public StringProperty nameProperty() {
	return name;
    }

    public StringProperty descriptionProperty() {
	return description;
    }

    public StringProperty ageCategoryProperty() {
	return ageCategory;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    @ElementCollection
    public List<String> getThemes() {
	return theme;
    }

    public void setThemes(List<String> theme) {
	this.theme.setAll(theme);
    }

    public String getAgeCategory() {
	return ageCategory.get();
    }

    public void setAgeCategory(String ageCategory) {
	this.ageCategory.set(ageCategory);
    }

    public String getName() {
	return name.get();
    }

    public void setName(String name) {
	this.name.set(name);
    }

    @Column(length = 15000)
    public String getDescription() {
	return description.get();
    }

    public void setDescription(String description) {
	this.description.set(description);
    }

    @Lob
    public byte[] getImage() {
	if (image.get() == null) {
	    return new byte[0];
	}

	try {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ImageIO.write(SwingFXUtils.fromFXImage(image.get(), null), "png", out);
	    return out.toByteArray();
	} catch (IOException ex) {
	    System.err.println("Could not save image-data: " + ex.getMessage());
	    return null;
	}
    }

    public void setImage(byte[] bytes) {
	if (bytes.length == 0) {
	    return;
	}

	try {
	    image.set(SwingFXUtils.toFXImage(ImageIO.read(new ByteArrayInputStream(bytes)), null));
	} catch (IOException ex) {
	    System.err.println("Could not load image-data: " + ex.getMessage());
	}
    }

    @Transient
    public Image getFXImage() {
	return image.get();
    }

    public void setFXImage(Image i) {
	this.image.set(i);
    }

    public ObjectProperty<Image> imageProperty() {
	return image;
    }

    @Override
    public int hashCode() {
	int hash = 5;
	hash = 67 * hash + (int) (this.id ^ (this.id >>> 32));
	return hash;
    }

    @Override
    public String toString() {
	return getName();
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	if (this != obj) {
	    return false;
	}
	final Item other = (Item) obj;
	if (this.id != other.id) {
	    return false;
	}
	return true;
    }

    @Override
    public boolean test(String query) {
	outer:
	for (String t : query.split("\\s+")) {
	    boolean temp = SearchPredicate.containsIgnoreCase(getAgeCategory(), t)
		    || SearchPredicate.containsIgnoreCase(getDescription(), t)
		    || SearchPredicate.containsIgnoreCase(getName(), t);

	    if (!temp) {
		boolean found = false;

		for (String theme : getThemes()) {
		    if (SearchPredicate.containsIgnoreCase(theme, t)) {
			continue outer;
		    }
		}

		for (ItemCopy copy : getItemCopies()) {
		    if (SearchPredicate.containsIgnoreCase(copy.getCopyNumber(), t)) {
			found = true;
			break;
		    }
		}

		if (!found) {
		    return false;
		}
	    }
	}

	return true;
    }

    public boolean testNoCopies(String query) {
	for (String t : query.split("\\s+")) {
	    boolean temp = SearchPredicate.containsIgnoreCase(getAgeCategory(), t)
		    || SearchPredicate.containsIgnoreCase(getDescription(), t)
		    || SearchPredicate.containsIgnoreCase(getName(), t);

	    if (!temp) {
		return false;
	    }
	}

	return true;
    }

    public static String getCodePrefixFor(Item item) {
	if (item instanceof Book) {
	    return "B";
	} else if (item instanceof Dvd) {
	    return "D";
	} else if (item instanceof Cd) {
	    return "C";
	} else if (item instanceof Game) {
	    return "S";
	} else if (item instanceof StoryBag) {
	    return "V";
	}

	throw new IllegalStateException("unknown item-type");
    }
}