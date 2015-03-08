package gui.dialogs;

import javafx.scene.Node;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.PopOver;

/**
 *
 * @author Brent
 */
public class PopupManager {

    /**
     * Shows the popOver with content & parent of your choice
     *
     * @param parent Node to show popOver at.
     * @param content Content to place in the popOver
     */
    public static PopOver showPopOver(Node parent, Node content) {
	return showPopOver(parent, content, PopOver.ArrowLocation.TOP_RIGHT);
    }

    public static PopOver showPopOver(Node parent, Node content, PopOver.ArrowLocation location) {
	PopOver logPop = new PopOver(content);
	logPop.setArrowLocation(location);
	logPop.setAutoHide(true);
	logPop.show(parent);
	logPop.setAutoFix(true);
	return logPop;
    }

    public static void showNotification(String title, String message) {
	showNotification(title, message, Notification.INFORMATION);
    }

    public static void showNotification(String title, String message, Notification notification) {
	if (notification == Notification.CONFIRM) {
	    showNotification(title, message, notification, Duration.INDEFINITE);
	} else {
	    showNotification(title, message, notification, Duration.seconds(10));
	}
    }

    public static void showNotification(String title, String message, Notification notification, Duration duration) {
	Notifications defaultTemplate = Notifications.create().title(title).text(message).hideAfter(duration);
	if (notification == Notification.CONFIRM) {
	    defaultTemplate.showConfirm();
	} else if (notification == Notification.WARNING) {
	    defaultTemplate.showWarning();
	} else if (notification == Notification.ERROR) {
	    defaultTemplate.showError();
	} else {
	    defaultTemplate.showInformation();
	}
    }

    public enum Notification {

	ERROR, INFORMATION, WARNING, CONFIRM
    }
}
