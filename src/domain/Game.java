package domain;

public class Game extends Item {

    private String name;
    private String brand;

    public Game() {
        super();
        this.name = "game";
        this.brand = "brand";
    }

    public Game(String theme, String ageCategory, String location, Damage damage, String name, String brand) {
        super(theme, ageCategory, location, damage);
        this.name = name;
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
