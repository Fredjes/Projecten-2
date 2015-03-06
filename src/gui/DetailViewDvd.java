package gui;

import domain.Dvd;
import java.io.IOException;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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

    public DetailViewDvd() {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/gui/detailview_dvd.fxml"));

        try {
            loader.setRoot(this);
            loader.setController(this);
            loader.load();

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void bind(Dvd t) {
        Bindings.bindBidirectional(name.textProperty(), t.nameProperty());
        Bindings.bindBidirectional(description.textProperty(), t.descriptionProperty());
        //Bindings.bindBidirectional(themes.textProperty(), t.getThemeFX(), StringConverter);
        Bindings.bindBidirectional(ageCategory.textProperty(), t.ageCategoryProperty());
        Bindings.bindBidirectional(director.textProperty(), t.directorProperty());
    }

}
