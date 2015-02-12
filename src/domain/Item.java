package domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public abstract class Item {

    @Id
    private int id;
    private String theme;
    private String ageCategory;
    private String location;
    private Damage damage;

    public Item() {
        this.theme = "kindjes";
        this.ageCategory = "5+";
        this.location = "rek";
        this.damage = Damage.NO_DAMAGE;
    }

    public Item(String theme, String ageCategory, String location, Damage damage) {
        this.theme = theme;
        this.ageCategory = ageCategory;
        this.location = location;
        this.damage = damage;
    }

    public int getId() {
        return id;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getAgeCategory() {
        return ageCategory;
    }

    public void setAgeCategory(String ageCategory) {
        this.ageCategory = ageCategory;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Damage getDamage() {
        return damage;
    }

    public void setDamage(Damage damage) {
        this.damage = damage;
    }

}
