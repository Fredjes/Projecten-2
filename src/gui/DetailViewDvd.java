package gui;

import domain.Dvd;
import domain.PropertyListBinding;
import domain.ThemeConverter;
import java.io.IOException;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class DetailViewDvd extends TabPane implements Binding<Dvd> {

    @FXML
    private TextField name;
    @FXML
    private TextArea description;
    @FXML
    private TextField themes;
    @FXML
    private TextField ageCategory;
    @FXML
    private TextField director;
    @FXML
    private ImageView image;

    private PropertyListBinding themesBinding;

    private Dvd boundedDvd;

    public DetailViewDvd() {
	FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/gui/detailview_dvd.fxml"));
	themesBinding = new PropertyListBinding();

	try {
	    loader.setRoot(this);
	    loader.setController(this);
	    loader.load();
	    description.setWrapText(true);
	} catch (IOException ex) {
	    throw new RuntimeException(ex);
	}
    }

    @Override
    public void bind(Dvd t) {
	if (boundedDvd != null) {
	    Bindings.unbindBidirectional(name.textProperty(), boundedDvd.nameProperty());
	    Bindings.unbindBidirectional(description.textProperty(), boundedDvd.descriptionProperty());
	    themesBinding.unbind();
	    Bindings.unbindBidirectional(ageCategory.textProperty(), boundedDvd.ageCategoryProperty());
	    Bindings.unbindBidirectional(director.textProperty(), boundedDvd.directorProperty());
	    Bindings.unbindBidirectional(image.imageProperty(), boundedDvd.imageProperty());
	}

	Bindings.bindBidirectional(name.textProperty(), t.nameProperty());
	Bindings.bindBidirectional(description.textProperty(), t.descriptionProperty());
	themesBinding.bind(themes.textProperty(), t.getThemeFX(), new ThemeConverter());
	Bindings.bindBidirectional(ageCategory.textProperty(), t.ageCategoryProperty());
	Bindings.bindBidirectional(director.textProperty(), t.directorProperty());
	Bindings.bindBidirectional(image.imageProperty(), t.imageProperty());
	boundedDvd = t;
    }

}
