package gui.dialogs;

import domain.Damage;
import domain.Loan;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.PopOver;
import persistence.ItemRepository;
import persistence.LoanRepository;

/**
 *
 * @author Brent
 */
public class PopupUtil {

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

    public static void showDamageQuestionPopOver(Loan loan, Node parent, Runnable completionCallback) {
	ChoiceBox<Damage> box = new ChoiceBox<>(FXCollections.observableArrayList(Damage.values()));
	box.getSelectionModel().select(loan.getItemCopy().getDamage());
	box.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
	    if (ov != nv) {
		loan.getItemCopy().damageProperty().set(nv);
	    }
	});

	PopOver popOver = new PopOver();
	Button ok = new Button("OK");
	ok.setAlignment(Pos.CENTER_RIGHT);
	ok.getStyleClass().addAll("btn", "btn-lime");
	ok.setOnAction(e -> {
	    LoanRepository.getInstance().addSyncListener(completionCallback);
	    loan.setReturned(true);
	    LoanRepository.getInstance().saveLoan(loan);
	    ItemRepository.getInstance().saveItemCopy(loan.getItemCopy());
	    ItemRepository.getInstance().sync();
	    popOver.hide();
	});

	popOver.setAutoHide(true);
	popOver.setAutoFix(true);
	VBox content = new VBox(new Label("Hoeveel schade is er?"), box, ok);
	content.setSpacing(10);
	content.setPadding(new Insets(10));
	popOver.setContentNode(content);
	popOver.show(parent);
    }

    public static void showNotification(String title, String message) {
	showNotification(title, message, Notification.INFORMATION);
    }

    public static void showNotification(String title, String message, Notification notification) {
	if (notification == Notification.CONFIRM) {
	    showNotification(title, message, notification, Duration.INDEFINITE);
	} else {
	    showNotification(title, message, notification, Duration.seconds(5));
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

    public static <E> E showSelectionQuestion(ObservableList<E> list, String text) {
	Dialog<E> dialog = new Dialog<>();
	ButtonType selectButton = new ButtonType("Selecteren", ButtonBar.ButtonData.OK_DONE);
	dialog.getDialogPane().getButtonTypes().addAll(selectButton, ButtonType.CANCEL);
	
	
	VBox box = new VBox();
	ListView<E> listView = new ListView(list);
	Label title = new Label(text);
	title.setTextAlignment(TextAlignment.CENTER);

	box.getChildren().addAll(title, listView);
	
	dialog.setTitle("Gebruiker selecteren");
	dialog.initStyle(StageStyle.UTILITY);
	dialog.setResultConverter(dialogButton -> {
	    if (dialogButton == selectButton) {
		return listView.getSelectionModel().getSelectedItem();
	    }

	    return null;
	});

	Node selectButtonBtn = dialog.getDialogPane().lookupButton(selectButton);
	selectButtonBtn.setDisable(true);
	selectButtonBtn.getStyleClass().addAll("btn", "btn-lime");
	listView.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
	    if (nv != null) {
		selectButtonBtn.setDisable(false);
	    } else {
		selectButtonBtn.setDisable(true);
	    }
	});
	
	dialog.getDialogPane().lookupButton(ButtonType.CANCEL).getStyleClass().addAll("btn", "btn-red");

	dialog.getDialogPane().setContent(box);
	
	Optional<E> selected = dialog.showAndWait();
	if (selected.isPresent()) {
	    return selected.get();
	}

	return null;
    }

    public enum Notification {

	ERROR, INFORMATION, WARNING, CONFIRM
    }
}
