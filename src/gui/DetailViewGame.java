package gui;

import domain.Game;
import java.io.IOException;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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

    public DetailViewGame() {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/gui/detailview_game.fxml"));

        try {
            loader.setRoot(this);
            loader.setController(this);
            loader.load();

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void bind(Game t) {
        Bindings.bindBidirectional(name.textProperty(), t.nameProperty());
        Bindings.bindBidirectional(description.textProperty(), t.descriptionProperty());
        //Bindings.bindBidirectional(themes.textProperty(), t.getThemeFX(), StringConverter);
        Bindings.bindBidirectional(ageCategory.textProperty(), t.ageCategoryProperty());
        Bindings.bindBidirectional(brand.textProperty(), t.brandProperty());
    }

}
