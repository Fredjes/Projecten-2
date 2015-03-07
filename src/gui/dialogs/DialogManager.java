package gui.dialogs;

import javafx.scene.Node;
import org.controlsfx.control.PopOver;

/**
 *
 * @author Brent
 */
public class DialogManager {

    /**
     * Shows the popOver with content & parent of your choice
     *
     * @param parent Node to show popOver at.
     * @param content Content to place in the popOver
     */
    public static void showPopOver(Node parent, Node content) {
        PopOver logPop = new PopOver(content);
        logPop.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
	logPop.setAutoHide(true);
        logPop.show(parent);
    }
}
