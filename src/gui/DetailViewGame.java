package gui;

import domain.DetailViewUtil;
import domain.Game;
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

public class DetailViewGame extends TabPane implements Binding<Game> {

    @FXML
    private TextField name;
    @FXML
    private TextArea description;
    @FXML
    private TextField themes;
    @FXML
    private TextField ageCategory;
    @FXML
    private TextField brand;
    @FXML
    private ImageView image;

    private Game boundedGame;

    private PropertyListBinding themesBinding;

    public DetailViewGame() {
	FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/gui/detailview_game.fxml"));
	themesBinding = new PropertyListBinding();

	try {
	    loader.setRoot(this);
	    loader.setController(this);
	    loader.load();
	    description.setWrapText(true);
	    DetailViewUtil.initImageDragAndDrop(image);
	} catch (IOException ex) {
	    throw new RuntimeException(ex);
	}
    }

    @FXML
    public void onSaveImage() {
	DetailViewUtil.selectImage(image);
    }

    @Override
    public void bind(Game t) {
	if (boundedGame != null) {
	    Bindings.unbindBidirectional(name.textProperty(), boundedGame.nameProperty());
	    Bindings.unbindBidirectional(description.textProperty(), boundedGame.descriptionProperty());
	    themesBinding.unbind();
	    Bindings.unbindBidirectional(ageCategory.textProperty(), boundedGame.ageCategoryProperty());
	    Bindings.unbindBidirectional(brand.textProperty(), boundedGame.brandProperty());
	    Bindings.unbindBidirectional(image.imageProperty(), boundedGame.imageProperty());
	}

	Bindings.bindBidirectional(name.textProperty(), t.nameProperty());
	Bindings.bindBidirectional(description.textProperty(), t.descriptionProperty());
	themesBinding.bind(themes.textProperty(), t.getThemeFX(), new ThemeConverter());
	Bindings.bindBidirectional(ageCategory.textProperty(), t.ageCategoryProperty());
	Bindings.bindBidirectional(brand.textProperty(), t.brandProperty());
	Bindings.bindBidirectional(image.imageProperty(), t.imageProperty());
	this.boundedGame = t;
    }

}
