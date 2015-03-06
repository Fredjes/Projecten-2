package gui;

import domain.Cd;
import java.io.IOException;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class DetailViewCd extends TabPane implements Binding<Cd> {

    @FXML
    private TextField title;
    @FXML
    private TextArea description;
    @FXML
    private TextField themes;
    @FXML
    private TextField ageCategory;
    @FXML
    private TextField artist;
    @FXML
    private ListView lstSongs;

    public DetailViewCd() {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/gui/detailview_cd.fxml"));

        try {
            loader.setRoot(this);
            loader.setController(this);
            loader.load();

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void bind(Cd t) {
        Bindings.bindBidirectional(title.textProperty(), t.nameProperty());
        Bindings.bindBidirectional(description.textProperty(), t.descriptionProperty());
        //Bindings.bindBidirectional(themes.textProperty(), t.getThemeFX(), StringConverter);
        Bindings.bindBidirectional(ageCategory.textProperty(), t.ageCategoryProperty());
        Bindings.bindBidirectional(artist.textProperty(), t.artistProperty());
        lstSongs.setItems(t.getObservableSongList());
    }

}
