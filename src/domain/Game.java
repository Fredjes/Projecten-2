package domain;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Game extends Item {

    private StringProperty name = new SimpleStringProperty();
    private StringProperty brand = new SimpleStringProperty();

    public Game() {
        super();
    }

    public Game(String theme, String ageCategory, String location, Damage damage, String name, String brand) {
        super(theme, ageCategory, location, damage);
        setName(name);
        setBrand(brand);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty brandProperty() {
        return brand;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getBrand() {
        return brand.get();
    }

    public void setBrand(String brand) {
        this.brand.set(brand);
    }
}
