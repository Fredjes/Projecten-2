package gui;

import java.util.HashMap;
import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author Frederik
 */
public class IconConfig {

    public static final String ITEM_ICON = "\uf02a";
    public static final String LOGOUT_ICON = "\uf08b";

    private static final HashMap<String, Icon> iconMapping = new HashMap() {
	{
	    put("icon-voorwerpen-beheren", new Icon("\uf1b3", IconType.TEXT, IconPosition.OVERWRITE));
	    put("icon-uitleningen-beheren", new Icon("\uf02c", IconType.TEXT, IconPosition.OVERWRITE));
	    put("icon-gebruikers-beheren", new Icon("\uf0c0", IconType.TEXT, IconPosition.OVERWRITE));
	    put("icon-excel-importeren", new Icon("\uf0ce", IconType.TEXT, IconPosition.PREPEND));
	    put("icon-te-laat", new Icon("\uf071", IconType.TEXT, IconPosition.OVERWRITE));
	}
    };

    private static HashMap<Integer, Font> fontCache = new HashMap();

    public enum IconType {

	GRAPHIC, TEXT
    }

    public enum IconPosition {

	PREPEND, APPEND, OVERWRITE
    }

    public static void identify(Node node, String cssClass) {
	if (node instanceof Text) {
	    Text target = (Text) node;
	    Font iconFont = FontCache.getIconFont(target.getFont().getSize());
	    target.setFont(iconFont);

	    if (iconMapping.containsKey(cssClass)) {
		Icon icon = iconMapping.get(cssClass);
		setIcon(target, icon);
	    }
	} else if (node instanceof Labeled) {
	    Labeled target = (Labeled) node;
	    Font iconFont = FontCache.getIconFont(target.getFont().getSize());
	    target.setFont(iconFont);

	    if (iconMapping.containsKey(cssClass)) {
		Icon icon = iconMapping.get(cssClass);
		setIcon(target, icon);
	    }
	}

    }

    private static void setIcon(Text text, Icon icon) {
	switch (icon.getType()) {
	    case GRAPHIC:
	    case TEXT:
		switch (icon.getPosition()) {
		    case APPEND:
			text.setText(text.getText() + icon.getIcon());
			break;
		    case OVERWRITE:
			text.setText(icon.getIcon());
			break;
		    case PREPEND:
			text.setText(icon.getIcon() + text.getText());
			break;
		}
		break;
	}
    }

    private static void setIcon(Labeled text, Icon icon) {
	switch (icon.getType()) {
	    case GRAPHIC:
		Text t = new Text(icon.getIcon());
		t.setFont(FontCache.getIconFont(12));
		text.setGraphic(t);
		break;
	    case TEXT:
		switch (icon.getPosition()) {
		    case APPEND:
			text.setText(text.getText() + icon.getIcon());
			break;
		    case OVERWRITE:
			text.setText(icon.getIcon());
			break;
		    case PREPEND:
			text.setText(icon.getIcon() + text.getText());
			break;
		}
		break;
	}
    }
}
