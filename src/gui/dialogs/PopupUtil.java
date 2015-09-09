package gui.dialogs;

import domain.Damage;
import domain.Item;
import domain.ItemCopy;
import domain.Loan;
import domain.Searchable;
import gui.ScreenSwitcher;
import gui.controls.SearchTextField;
import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.PopOver;
import persistence.ItemRepository;
import persistence.LoanRepository;

/**
 * A Util-class used for showing all kinds of popovers.
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

    public static ScreenSwitcher switcher;

    public static PopOver showPopOver(Node parent, Node content, PopOver.ArrowLocation location) {
	PopOver logPop = new PopOver(content);
	logPop.setDetachable(false);
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
	ok.setAlignment(Pos.BOTTOM_CENTER);
	ok.getStyleClass().addAll("btn", "btn-lime");
	ok.setOnAction(e -> {
	    LoanRepository.getInstance().addSyncListener(completionCallback);
	    loan.setReturned(true);
	    LoanRepository.getInstance().saveLoan(loan);
	    ItemRepository.getInstance().saveItemCopy(loan.getItemCopy());
	    Stream.concat(ItemRepository.getInstance().getItems().stream().map(Item::getItemCopies).flatMap(List::stream),
		    ItemRepository.getInstance().getItemCopies().stream()).filter(loan.getItemCopy()::equals).forEach(ic -> {
			ic.setDamage(loan.getItemCopy().getDamage());
			ic.getLoans().stream().forEach(l -> l.setReturned(true));
		    });
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

    public static <E extends Searchable> E showSelectionQuestion(ObservableList<E> list, String title, String text) {
	return showSelectionQuestion(list, title, text, null);
    }

    public static <E extends Searchable> E showSelectionQuestion(ObservableList<E> list, String title, String text, Callback<ListView<E>, ListCell<E>> cellFactory, double width) {
	FilteredList<? extends Searchable> filteredList = new FilteredList<>(list);
	Dialog<E> dialog = new Dialog<>();
	ButtonType selectButton = new ButtonType("Selecteren", ButtonBar.ButtonData.OK_DONE);
	dialog.getDialogPane().getButtonTypes().addAll(selectButton, ButtonType.CANCEL);

	VBox box = new VBox();
	SearchTextField searchBar = new SearchTextField();
	searchBar.textProperty().addListener((obs, ov, nv) -> {
	    if (nv != null) {
		filteredList.setPredicate(i -> i.test(nv));
	    } else {
		filteredList.setPredicate(i -> true);
	    }
	});
	box.setFillWidth(true);
	searchBar.setIcon("icon-search");
	searchBar.setMinWidth(Region.USE_PREF_SIZE);
	searchBar.setMaxWidth(Integer.MAX_VALUE);
	ListView<E> listView = new ListView(filteredList);
	listView.setCellFactory(cellFactory);
	listView.setFocusTraversable(false);
	listView.setMinWidth(Region.USE_PREF_SIZE);
	listView.setPrefWidth(width);
	Label lblTitle = new Label(text);
	lblTitle.setTextAlignment(TextAlignment.CENTER);

	box.getChildren().addAll(lblTitle, searchBar, listView);
	box.setSpacing(10);

	dialog.setTitle(title);
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

	((Labeled) dialog.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuleren");
	dialog.getDialogPane().lookupButton(ButtonType.CANCEL).getStyleClass().addAll("btn", "btn-red");
	dialog.getDialogPane().getStylesheets().add(PopupUtil.class.getResource("/resources/css/global.css").toExternalForm());
	dialog.getDialogPane().setContent(box);

	Optional<E> selected = dialog.showAndWait();
	if (selected.isPresent()) {
	    return selected.get();
	}

	return null;
    }

    public static <E extends Searchable> E showSelectionQuestion(ObservableList<E> list, String title, String text, Callback<ListView<E>, ListCell<E>> cellFactory) {
	return showSelectionQuestion(list, title, text, cellFactory, 350);
    }

    public static boolean confirm(String title, String message) {
	Dialog<Boolean> dialog = new Dialog<>();
	dialog.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
	dialog.setTitle(title);
	dialog.setContentText(message);
	dialog.setResultConverter(bt -> bt.equals(ButtonType.YES));

	((Labeled) dialog.getDialogPane().lookupButton(ButtonType.NO)).setText("Nee");
	dialog.getDialogPane().lookupButton(ButtonType.NO).getStyleClass().addAll("btn", "btn-red");

	((Labeled) dialog.getDialogPane().lookupButton(ButtonType.YES)).setText("Ja");
	dialog.getDialogPane().lookupButton(ButtonType.YES).getStyleClass().addAll("btn", "btn-lime");
	dialog.getDialogPane().setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
	dialog.getDialogPane().getStylesheets().add(PopupUtil.class.getResource("/resources/css/global.css").toExternalForm());

	Optional<Boolean> answer = dialog.showAndWait();
	if (answer.isPresent()) {
	    return answer.get();
	} else {
	    return false;
	}
    }

    public static File showDirectoryChooser(javafx.stage.Window owner, String title, File initialPath) {
	DirectoryChooser dc = new DirectoryChooser();
	dc.setInitialDirectory(initialPath);
	dc.setTitle(title);

	return dc.showDialog(owner);
    }

    public static String input(String title, String question) {
	Dialog<String> dialog = new Dialog<>();
	dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
	dialog.setTitle(title);

	PasswordField field = new PasswordField();
	VBox box = new VBox(new Label(question), field);
	box.setPadding(new Insets(10));
	box.setSpacing(5);
	dialog.getDialogPane().setContent(box);
	dialog.setResultConverter((r) -> {
	    return field.getText();
	});
	((Labeled) dialog.getDialogPane().lookupButton(ButtonType.OK)).setText("Ok");
	dialog.getDialogPane().lookupButton(ButtonType.OK).getStyleClass().addAll("btn", "btn-lime");
	((Labeled) dialog.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Annuleren");
	dialog.getDialogPane().lookupButton(ButtonType.CANCEL).getStyleClass().addAll("btn", "btn-red");
	dialog.getDialogPane().setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
	dialog.getDialogPane().getStylesheets().add(PopupUtil.class.getResource("/resources/css/global.css").toExternalForm());

	Optional<String> answer = dialog.showAndWait();
	if (answer.isPresent()) {
	    return answer.get();
	} else {
	    return "";
	}
    }

    public enum Notification {

	ERROR, INFORMATION, WARNING, CONFIRM
    }
}
