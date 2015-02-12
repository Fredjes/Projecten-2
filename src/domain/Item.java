package domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

@Entity
@Access(AccessType.PROPERTY)
@NamedQueries({
    @NamedQuery(name = "Item.findAll", query = "SELECT i FROM Item i")
})
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.FIELD)
    private int id;

    private StringProperty name = new SimpleStringProperty();
    private StringProperty description = new SimpleStringProperty();
    private StringProperty theme = new SimpleStringProperty();
    private StringProperty ageCategory = new SimpleStringProperty();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    @Access(AccessType.FIELD)
    private List<ItemCopy> itemCopies;

    private Image image;

    public Item() {
    }

    public Item(String name, String description, String theme, String ageCategory) {
	setTheme(theme);
	setAgeCategory(ageCategory);
	setName(name);
	setDescription(description);
    }

    public StringProperty themeProperty() {
	return theme;
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

    @Transient
    public int getId() {
	return id;
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
}
