package domain;

import java.util.HashMap;
import javafx.scene.text.Font;

/**
 *
 * @author Brent
 */
public class FontCache {

    private static final HashMap<Double, Font> cache = new HashMap<>();

    public static Font getIconFont(double size) {
	if (!cache.containsKey(size)) {
	    cache.put(size, Font.loadFont(FontCache.class.getResourceAsStream("/resources/fonts/FontAwesome.otf"), size));
	}

	return cache.get(size);
    }
}
