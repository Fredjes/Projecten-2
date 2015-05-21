package gui;

import domain.Item;
import domain.SearchPredicate;
import gui.controls.ThemeItem;
import gui.dialogs.PopupUtil;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import org.controlsfx.control.PopOver;
import persistence.ItemRepository;

/**
 * Class that handles the theme system, which uses separate components for each
 * theme, automatically looks up existing themes, etc.
 *
 * @author Frederik
 */
public class ThemeManager {

    private ObservableList<String> themes;
    private HBox box;
    private TextField textField = new TextField();
    private final EventHandler<ActionEvent> onRemoveHandler = e -> {
	box.getChildren().remove(e.getTarget());
    };

    private FilteredList<String> filteredList = new FilteredList<>(allThemesList);
    private PopOver popOver;
    private static ListView<String> filteredThemeList;
    private static final ObservableList<String> allThemesList = FXCollections.observableArrayList();
    private static final ListChangeListener<String> themeChangeListener = (ListChangeListener.Change<? extends String> c) -> {
	while (c.next()) {
	    if (c.wasAdded()) {
		addThemes(c.getAddedSubList());
	    }
	}
    };

    static {
	ItemRepository.getInstance().getItems().stream().forEach(i -> {
	    addThemes(i.getThemes());
	    i.getThemeFX().addListener(themeChangeListener);
	});

	ItemRepository.getInstance().getItems().addListener((ListChangeListener.Change<? extends Item> c) -> {
	    while (c.next()) {
		if (c.wasAdded()) {
		    c.getAddedSubList().forEach(i -> {
			addThemes(i.getThemes());
			i.getThemeFX().addListener(themeChangeListener);
		    });
		}

		if (c.wasRemoved()) {
		    c.getRemoved().forEach(i -> {
			i.getThemeFX().removeListener(themeChangeListener);
		    });
		}
	    }
	});
    }

    public ThemeManager(HBox box) {
	filteredThemeList = new ListView<>(filteredList);

	final Runnable themeSelectionHandler = () -> {
	    if (!filteredThemeList.getSelectionModel().isEmpty()) {
		add(filteredThemeList.getSelectionModel().getSelectedItem());
	    }
	};

	filteredThemeList.setOnKeyReleased(e -> {
	    if (e.getCode() == KeyCode.ENTER) {
		themeSelectionHandler.run();
	    }
	});

	filteredThemeList.setOnMouseClicked(e -> themeSelectionHandler.run());

	this.box = box;
	box.setAlignment(Pos.CENTER_LEFT);
	box.setSpacing(3);
	box.setMaxHeight(22);
	textField.setBorder(Border.EMPTY);
	box.getChildren().add(textField);
    }

    private void add(String theme) {
	if (theme == null) {
	    return;
	}

	textField.setText("");
	themes.add(theme);
	ThemeItem themeItem = new ThemeItem(themes, theme);
	themeItem.setOnRemove(onRemoveHandler);
	box.getChildren().add(box.getChildren().size() - 1, themeItem);
	refreshPopOver(true);
    }

    private void refreshPopOver(boolean hide) {
	if (popOver == null) {
	    final int maxHeight = 100;
	    filteredThemeList.setMaxHeight(maxHeight);
	    popOver = PopupUtil.showPopOver(textField, filteredThemeList, PopOver.ArrowLocation.BOTTOM_CENTER);
	    popOver.setMaxHeight(maxHeight);

	} else {
	    if (popOver.isShowing() && hide) {
		popOver.hide();
	    } else if (!popOver.isShowing() && !hide) {
		popOver.show(textField);
	    }
	}
    }

    public void bind(ObservableList<String> themes) {
	this.themes = themes;
	box.getChildren().clear();

	themes.stream().map(t -> new ThemeItem(themes, t)).forEach(ti -> {
	    box.getChildren().add(ti);
	    ti.setOnRemove(onRemoveHandler);
	});

	box.getChildren().add(textField);
	textField.setOnKeyReleased(e -> {
	    String theme = textField.getText().trim();
	    if (e.getCode() == KeyCode.ENTER) {
		add(theme);
		filteredList.setPredicate(t -> SearchPredicate.containsIgnoreCase(t, ""));
	    } else {
		filteredList.setPredicate(t -> SearchPredicate.containsIgnoreCase(t, theme));
		refreshPopOver(false);
	    }
	});
    }

    /**
     * Convenience method to allow the list of themes to only contain unique
     * elements, the List expects String or elements extending String for full
     * generic support.
     *
     * @param themes
     */
    private static void addThemes(List<? extends String> themes) {
	themes.forEach(t -> {
	    if (!allThemesList.contains(t)) {
		allThemesList.add(t);
	    }
	});
    }
}
