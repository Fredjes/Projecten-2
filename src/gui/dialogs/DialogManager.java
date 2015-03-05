package gui.dialogs;

import javafx.scene.Node;
import org.controlsfx.control.PopOver;

/**
 *
 * @author Brent
 */
public class DialogManager {

    public static void showLogin(Node e) {
        PopOver logPop = new PopOver(new LoginPanel());
        logPop.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
        logPop.show(e);
    }
}
