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

/**
 * A Util-class for DetailView management.
 *
 * @author Pieter-Jan Geeroms
 */
public class DetailViewUtil {

    /**
     * Get a detailpane based on the filter options.
     *
     * @param o The filter option
     * @return The detailpane based on the filter option.
     */
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

    /**
     * Will initialize drag and drop support for images. It will allow the image
     * in the imageview to update by a drag and drop of an image in a certain
     * directory.
     *
     * @param image The {@link ImageView} that requires drag and drop support.
     */
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

    /**
     * Open an image selection wizard (based on the operating system), when an
     * image is selected, the ImageView will automatically update to the newly
     * selected image.
     *
     * @param image The ImageView in which the selected image should be shown
     */
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

    /**
     * Convenience method that checks whether a certain directory referres to a
     * supported image format.
     *
     * @param directory The directory to a file
     * @return true if the directory is an image directory, false otherwise
     */
    private static boolean isImageDirectory(String directory) {
	return directory.matches(".*(png|gif|jpg|jps|mpo)$");
    }

    /**
     * Convenience method to allow a uniform detailview height.
     *
     * @param binding The detailview that should adapt to the new height
     */
    public static void setBounds(Binding binding) {
	try {
	    TabPane pane = ((TabPane) binding);
	    pane.setPrefHeight(250);
	    pane.setMinHeight(Region.USE_PREF_SIZE);
	    pane.setMaxHeight(Region.USE_PREF_SIZE);
	} catch (Exception ex) {
	}
    }
}
