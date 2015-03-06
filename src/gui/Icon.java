package gui;

import gui.IconConfig.IconPosition;
import gui.IconConfig.IconType;

/**
 *
 * @author Brent
 */
public class Icon {

    private String icon;

    private IconPosition position;

    private IconType type;

    public Icon(String icon, IconType type, IconPosition position) {
	this.icon = icon;
	this.position = position;
	this.type = type;
    }

    public String getIcon() {
	return icon;
    }

    public void setIcon(String icon) {
	this.icon = icon;
    }

    public IconPosition getPosition() {
	return position;
    }

    public void setPosition(IconPosition position) {
	this.position = position;
    }

    public IconType getType() {
	return type;
    }

    public void setType(IconType type) {
	this.type = type;
    }

}
