package domain;

import java.util.HashMap;
import javafx.scene.text.Font;

/**
 * A cache that holds a font for each font size.
 *
 * @author Brent Couck
 */
public class FontCache {

    private static final HashMap<Double, Font> cache = new HashMap<>();

    /**
     * Get the font for a specific font size.
     *
     * @param size the font size
     * @return the font having the specified font size
     */
    public static Font getIconFont(double size) {
	if (!cache.containsKey(size)) {
	    cache.put(size, Font.loadFont(FontCache.class.getResourceAsStream("/resources/fonts/FontAwesome.otf"), size));
	}

	return cache.get(size);
    }
}
