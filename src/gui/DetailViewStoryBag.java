package gui;

import domain.PropertyListBinding;
import domain.StoryBag;
import domain.ThemeConverter;
import java.io.IOException;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class DetailViewStoryBag extends TabPane implements Binding<StoryBag> {

    @FXML
    private TextField name;
    @FXML
    private TextArea description;
    @FXML
    private TextField themes;
    @FXML
    private TextField ageCategory;
    @FXML
    private ListView lstItems;
    
    private PropertyListBinding themesBinding;

    public DetailViewStoryBag() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/gui/detailview_storybag.fxml"));
	themesBinding = new PropertyListBinding();

        try {
            loader.setRoot(this);
            loader.setController(this);
            loader.load();

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void bind(StoryBag t) {
        Bindings.bindBidirectional(name.textProperty(), t.nameProperty());
        Bindings.bindBidirectional(description.textProperty(), t.descriptionProperty());
        themesBinding.bind(themes.textProperty(), t.getThemeFX(), new ThemeConverter());
        Bindings.bindBidirectional(ageCategory.textProperty(), t.ageCategoryProperty());
        lstItems.setItems(t.getObservableItems());
    }

}
