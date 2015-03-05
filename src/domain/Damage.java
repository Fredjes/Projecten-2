package domain;

/**
 * Used to determine how much damage is inflicted on an {@link ItemCopy} and
 * holds a textual representation of how the damage will be shown in the GUI.
 *
 * @author Frederik
 */
public enum Damage {

    NO_DAMAGE("Geen schade"),
    MODERATE_DAMAGE("Gemiddelde schade"),
    HIGH_DAMAGE("Niet beschikbaar");

    private final String translation;

    Damage(String translation) {
        this.translation = translation;
    }

    @Override
    public String toString() {
        return translation;
    }
}
