package gui;

import domain.Cd;
import domain.DetailViewUtil;
import domain.PropertyListBinding;
import domain.ThemeConverter;
import java.io.IOException;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.ImageView;

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
    private ListView<String> lstSongs;
    @FXML
    private ImageView image;

    private PropertyListBinding themesBinding;

    private Cd boundedCd;

    public DetailViewCd() {
	FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/gui/detailview_cd.fxml"));
	themesBinding = new PropertyListBinding();

	try {
	    loader.setRoot(this);
	    loader.setController(this);
	    loader.load();
	    lstSongs.setCellFactory(TextFieldListCell.forListView());
	    description.setWrapText(true);
	    lstSongs.setEditable(true);
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
    public void bind(Cd t) {
	if (boundedCd != null) {
	    Bindings.unbindBidirectional(title.textProperty(), boundedCd.nameProperty());
	    Bindings.unbindBidirectional(description.textProperty(), boundedCd.descriptionProperty());
	    Bindings.unbindBidirectional(ageCategory.textProperty(), boundedCd.ageCategoryProperty());
	    themesBinding.unbind();
	    Bindings.unbindBidirectional(artist.textProperty(), boundedCd.artistProperty());
	    lstSongs.setItems(boundedCd.getObservableSongList());
	    Bindings.unbindBidirectional(image.imageProperty(), boundedCd.imageProperty());
	}

	Bindings.bindBidirectional(title.textProperty(), t.nameProperty());
	Bindings.bindBidirectional(description.textProperty(), t.descriptionProperty());
	Bindings.bindBidirectional(ageCategory.textProperty(), t.ageCategoryProperty());
	themesBinding.bind(themes.textProperty(), t.getThemeFX(), new ThemeConverter());
	Bindings.bindBidirectional(artist.textProperty(), t.artistProperty());
	lstSongs.setItems(t.getObservableSongList());
	Bindings.bindBidirectional(image.imageProperty(), t.imageProperty());
	this.boundedCd = t;
    }

    @FXML
    public void onAddSong() {
	boundedCd.getSongList().add("Nieuw liedje");
    }

    @FXML
    public void onRemoveSong() {
	if (!lstSongs.getSelectionModel().isEmpty()) {
	    boundedCd.getSongList().remove(lstSongs.getSelectionModel().getSelectedItem());
	}
    }
}
