package domain;

import java.util.HashMap;
import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Configurations to manage icons.
 *
 * @author Frederik De Smedt
 */
public class IconConfig {

    private static final HashMap<String, Icon> iconMapping = new HashMap() {
        {
            put("icon-voorwerpen-beheren", new Icon("\uf1b3", IconType.TEXT, IconPosition.OVERWRITE));
            put("icon-uitleningen-beheren", new Icon("\uf02c", IconType.TEXT, IconPosition.OVERWRITE));
            put("icon-gebruikers-beheren", new Icon("\uf0c0", IconType.TEXT, IconPosition.OVERWRITE));
            put("icon-excel-importeren", new Icon("\uf0ce", IconType.TEXT, IconPosition.OVERWRITE));
            put("icon-te-laat", new Icon("\uf071", IconType.TEXT, IconPosition.OVERWRITE));
            put("icon-search", new Icon("\uf002", IconType.TEXT, IconPosition.OVERWRITE));
            put("icon-home", new Icon("\uf015", IconType.TEXT, IconPosition.OVERWRITE));
            put("icon-add", new Icon("\uf0fe", IconType.TEXT, IconPosition.OVERWRITE));
            put("icon-delete", new Icon("\uf014", IconType.TEXT, IconPosition.OVERWRITE));
            put("icon-save", new Icon("\uf0c7", IconType.TEXT, IconPosition.OVERWRITE));
            put("icon-return", new Icon("\uf064", IconType.TEXT, IconPosition.OVERWRITE));
            put("icon-extend", new Icon("\uf017", IconType.TEXT, IconPosition.OVERWRITE));

            put("icon-book", new Icon("\uf02d", IconType.TEXT, IconPosition.OVERWRITE));
            put("icon-game", new Icon("\uf091", IconType.TEXT, IconPosition.OVERWRITE));
            put("icon-cd", new Icon("\uf025", IconType.TEXT, IconPosition.OVERWRITE));
            put("icon-dvd", new Icon("\uf008", IconType.TEXT, IconPosition.OVERWRITE));
            put("icon-story", new Icon("\uf0b1", IconType.TEXT, IconPosition.OVERWRITE));
            put("icon-all", new Icon("\uf1ce", IconType.TEXT, IconPosition.OVERWRITE));

        }
    };

    /**
     * IconType enumeration to specify how the icon should be placed, as part of
     * a {@link Labeled} or as a separate graphic.
     */
    public enum IconType {

        GRAPHIC, TEXT
    }

    /**
     * IconPosition enumeration that secifies the position of the icon in the
     * node: before, after, or as a replacement of the node content.
     */
    public enum IconPosition {

        PREPEND, APPEND, OVERWRITE
    }

    /**
     * Returns an icon for the CSS class flag.
     *
     * @param cssClass The CSS flag
     * @return The icon
     */
    public static Icon getIconFor(String cssClass) {
        return iconMapping.get(cssClass);
    }

    /**
     * Checks whether the node - class combination is a legal combination,
     * meaning whether an icon placement is supported.
     *
     * @param node
     * @param cssClass
     */
    public static void identify(Node node, String cssClass) {
        if (!iconMapping.containsKey(cssClass)) {
            return;
        }

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

    /**
     * Put the icon in the text, based on the IconType and IconPosition.
     *
     * @param text The {@link Text} in which the icon should be placed
     * @param icon The {@link Icon} that should be used
     */
    public static void setIcon(Text text, Icon icon) {
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

    /**
     * Put the icon in the text, based on the IconType and IconPosition.
     *
     * @param text The {@link Labeled} in which the icon should be placed
     * @param icon The {@link Icon} that should be used
     */
    public static void setIcon(Labeled text, Icon icon) {
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
