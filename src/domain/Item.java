package domain;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public abstract class Item {

    @Id
    private int id;
    private StringProperty theme = new SimpleStringProperty();
    private StringProperty ageCategory = new SimpleStringProperty();
    private StringProperty location = new SimpleStringProperty();
    private ObjectProperty<Damage> damage = new SimpleObjectProperty<>();

    public Item() {
    }

    public Item(String theme, String ageCategory, String location, Damage damage) {
	setTheme(theme);
	setAgeCategory(ageCategory);
	setLocation(location);
	setDamage(damage);
    }

    public StringProperty themeProperty() {
	return theme;
    }

    public StringProperty ageCategoryProperty() {
	return ageCategory;
    }

    public StringProperty locationProperty() {
	return location;
    }

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

    public String getLocation() {
	return location.get();
    }

    public void setLocation(String location) {
	this.location.set(location);
    }

    public Damage getDamage() {
	return damage.get();
    }

    public void setDamage(Damage damage) {
	this.damage.set(damage);
    }

}
