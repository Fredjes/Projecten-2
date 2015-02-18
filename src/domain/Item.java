package domain;

import domain.annotations.Display;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
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
 * Represents the definition of every item stored, containing all shared properties of every item.
 *
 * @author Frederik
 */
@Entity
@Access(AccessType.PROPERTY)
@NamedQueries({
    @NamedQuery(name = "Item.findAll", query = "SELECT i FROM Item i")
})
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Item implements Serializable {

    private StringProperty name = new SimpleStringProperty();
    private StringProperty description = new SimpleStringProperty();
    private StringProperty theme = new SimpleStringProperty();
    private StringProperty ageCategory = new SimpleStringProperty();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    @Access(AccessType.FIELD)
    private List<ItemCopy> itemCopies;

    private Image image;

    private long id;

    public Item() {
    }

    public Item(String name, String description, String theme, String ageCategory) {
	setTheme(theme);
	setAgeCategory(ageCategory);
	setName(name);
	setDescription(description);
    }

    @Display("Thema")
    public StringProperty themeProperty() {
	return theme;
    }

    @Display("Naam")
    public StringProperty nameProperty() {
	return name;
    }

    @Display("Omschrijving")
    public StringProperty descriptionProperty() {
	return description;
    }

    @Display("Leeftijd")
    public StringProperty ageCategoryProperty() {
	return ageCategory;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public String getTheme() {
	return theme.get();
    }

    public void setTheme(String theme) {
	this.theme.set(theme);
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

    public String getDescription() {
	return description.get();
    }

    public void setDescription(String description) {
	this.description.set(description);
    }

    @Lob
    public byte[] getImage() {
	if (image == null) {
	    return new byte[0];
	}

	try {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", out);
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
	    image = SwingFXUtils.toFXImage(ImageIO.read(new ByteArrayInputStream(bytes)), null);
	} catch (IOException ex) {
	    System.err.println("Could not load image-data: " + ex.getMessage());
	}
    }

    @Transient
    public Image getFXImage() {
	return image;
    }

    public void setFXImage(Image i) {
	this.image = i;
    }

    @Override
    public int hashCode() {
	int hash = 5;
	hash = 67 * hash + (int) (this.id ^ (this.id >>> 32));
	return hash;
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final Item other = (Item) obj;
	if (this.id != other.id) {
	    return false;
	}
	return true;
    }
}
