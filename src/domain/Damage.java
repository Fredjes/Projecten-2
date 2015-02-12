package domain;

public enum Damage {

    NO_DAMAGE("Geen schade"),
    MODERATE_DAMAGE("Gemiddelde schade"),
    HIGH_DAMAGE("Hoge schade");

    private final String translation;

    Damage(String translation) {
        this.translation = translation;
    }

    public String getTranslation() {
        return translation;
    }

}
