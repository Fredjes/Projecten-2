package domain;

import static domain.DragUtil.ITEM;
import static domain.DragUtil.ITEM_REMOVE;
import java.io.Serializable;
import javafx.scene.input.DataFormat;

/**
 *
 * @author Frederik
 */
public class DragCommand implements Serializable {
    
    public static final DataFormat DRAG_COMMAND_DATA_FORMAT = new DataFormat("domain.DragCommand");

    private String command;

    public DragCommand(Item item) {
	this(item, DragUtil.ITEM);
    }

    public DragCommand(Item item, String flag) {
	this(DragUtil.createItemString(item, flag));
    }

    public DragCommand(String command) {
	this.command = command;
    }

    public String getCommand() {
	return command;
    }

    public void setCommand(String command) {
	this.command = command;
    }
    
    public static boolean isItemDrag(DragCommand drag) {
	return drag.command != null && drag.command.startsWith(ITEM);
    }

    public static boolean isRemoveItemDrag(DragCommand drag) {
	return drag.command != null && drag.command.startsWith(ITEM_REMOVE);
    }
}
