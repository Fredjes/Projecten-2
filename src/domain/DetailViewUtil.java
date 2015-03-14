package domain;

import domain.controllers.StoryBagController;
import gui.Binding;
import gui.DetailViewBook;
import gui.DetailViewCd;
import gui.DetailViewDvd;
import gui.DetailViewGame;
import gui.DetailViewStoryBag;
import gui.DetailViewUser;
import gui.dialogs.PopupUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;
import persistence.UserRepository;

public class DetailViewUtil {

    public static Object getDetailPane(FilterOption o) {
	if (o == FilterOption.BOOK) {
	    return new DetailViewBook();
	} else if (o == FilterOption.CD) {
	    return new DetailViewCd();
	} else if (o == FilterOption.DVD) {
	    return new DetailViewDvd();
	} else if (o == FilterOption.GAME) {
	    return new DetailViewGame();
	} else if (o == FilterOption.STORYBAG) {
	    return new DetailViewStoryBag(new StoryBagController());
	} else if (o == FilterOption.USER) {
	    return new DetailViewUser();
	}
	return null;
    }

    public static void initImageDragAndDrop(ImageView image) {

	User u = UserRepository.getInstance().getAuthenticatedUser();
	if (u == null || u.getUserType() == null || u.getUserType() == User.UserType.STUDENT || u.getUserType() == User.UserType.VOLUNTEER) {
	    return;
	}

	final ObjectProperty<PopOver> shownPopup = new SimpleObjectProperty<>(new PopOver());
	image.setOnDragEntered(evt -> {
	    if (evt.getDragboard().hasFiles()) {
		Label shownLabel = new Label();
		shownLabel.setTextFill(Color.BLACK);

		if (evt.getDragboard().getFiles().size() == 1 && isImageDirectory(evt.getDragboard().getFiles().get(0).getAbsolutePath())) {
		    shownLabel.setText("Afbeelding toevoegen aan voorwerp.");
		} else {
		    shownLabel.setText("Gelieve een afbeelding toe te voegen.");
		}

		shownLabel.setPadding(new Insets(-5, 5, -5, 5));
		shownPopup.set(PopupUtil.showPopOver(image, shownLabel, PopOver.ArrowLocation.RIGHT_BOTTOM));
	    }
	});

	image.setOnDragDropped(evt -> {
	    if (evt.getDragboard().hasFiles() && evt.getDragboard().getFiles().size() == 1 && evt.getDragboard().getFiles().get(0).getAbsolutePath().toLowerCase().matches(".*(png|gif|jpg|jps|mpo)$")) {
		try {
		    image.imageProperty().set(new Image(new FileInputStream(evt.getDragboard().getFiles().get(0))));
		} catch (FileNotFoundException ex) {
		    PopupUtil.showNotification("Mislukt", "De afbeelding kon niet worden toegevoegd.", PopupUtil.Notification.ERROR);
		}
	    }
	});

	image.setOnDragExited(evt -> {
	    shownPopup.get().hide(Duration.seconds(1));
	});

	image.setOnDragOver(evt -> {
	    if (evt.getDragboard().hasFiles() && evt.getDragboard().getFiles().size() == 1 && evt.getDragboard().getFiles().get(0).getAbsolutePath().toLowerCase().matches(".*(png|gif|jpg|jps|mpo)$")) {
		evt.acceptTransferModes(TransferMode.LINK);
	    }
	});
    }

    public static void selectImage(ImageView image) {
	FileChooser chooser = new FileChooser();
	chooser.setTitle("Kies een afbeelding");
	chooser.getExtensionFilters().addAll(
		new FileChooser.ExtensionFilter("Alle afbeeldingen", "*.*"),
		new FileChooser.ExtensionFilter("PNG-afbeeldingen", "*.png"),
		new FileChooser.ExtensionFilter("GIF-afbeeldingen", "*.gif"),
		new FileChooser.ExtensionFilter("JPEG-afbeeldingen", "*.jpg"),
		new FileChooser.ExtensionFilter("JPS-afbeeldingen", "*.jps"),
		new FileChooser.ExtensionFilter("MPO-afbeeldingen", "*.mpo"));
	File selectedFile = chooser.showOpenDialog(null);

	if (selectedFile == null) {
	    return;
	}

	if (isImageDirectory(selectedFile.getAbsolutePath())) {
	    try {
		image.imageProperty().set(new Image(new FileInputStream(selectedFile)));
	    } catch (FileNotFoundException ex) {
		PopupUtil.showNotification("Mislukt", "De afbeelding kon niet worden toegevoegd.", PopupUtil.Notification.ERROR);
	    }
	} else {
	    PopupUtil.showNotification("Niet ondersteund", "Het bestand is geen geldige foto.", PopupUtil.Notification.WARNING);
	}
    }

    private static boolean isImageDirectory(String directory) {
	return directory.matches(".*(png|gif|jpg|jps|mpo)$");
    }

    public static void setBounds(Binding binding) {
	try {
	    TabPane pane = ((TabPane) binding);
	    pane.setPrefHeight(300);
	    pane.setMinHeight(Region.USE_PREF_SIZE);
	    pane.setMaxHeight(Region.USE_PREF_SIZE);
	} catch (Exception ex) {
	}
    }
}
