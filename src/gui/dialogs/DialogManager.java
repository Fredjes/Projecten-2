package gui.dialogs;

import domain.Item;
import domain.ItemCopy;
import domain.StoryBag;
import java.util.List;
import java.util.Optional;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import persistence.ItemRepository;

/**
 *
 * @author Brent
 */
public class DialogManager {

    public static List<ItemCopy> showStorybagItemSelectionDialog(StoryBag sb) {
	Dialog<List<ItemCopy>> dialog = new Dialog();
	dialog.setTitle("Inhoud verteltas");
	dialog.setHeaderText("Selecteer de exemplaren die in de verteltas zitten");

	ButtonType loginButtonType = new ButtonType("Ok", ButtonData.OK_DONE);
	ButtonType cancelButtonType = new ButtonType("Annuleren", ButtonData.CANCEL_CLOSE);

	StoryBagDialogVBox box = new StoryBagDialogVBox();

	box.setItems(ItemRepository.getInstance().getItemCopies());
	box.setSelectedItems(sb.getItems());

	dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, cancelButtonType);
	dialog.getDialogPane().setContent(box);

	dialog.setResultConverter(btn -> {
	    if (btn == loginButtonType) {
		return box.getSelectedItems();
	    }
	    return null;
	});

	Optional<List<ItemCopy>> result = dialog.showAndWait();
	return result.isPresent() ? result.get() : null;
    }

    public static Item showItemCopyItemSelectionDialog(ItemCopy copy) {
	Dialog<Item> dialog = new Dialog();
	dialog.setTitle("Exemplaar koppelen");
	dialog.setHeaderText("Selecteer het voorwerp waarvan dit exemplaar is");

	ButtonType loginButtonType = new ButtonType("Ok", ButtonData.OK_DONE);
	ButtonType cancelButtonType = new ButtonType("Annuleren", ButtonData.CANCEL_CLOSE);

	ItemCopyDialogVBox box = new ItemCopyDialogVBox();

	box.setItems(ItemRepository.getInstance().getItems());
	box.setSelectedItem(copy.getItem());

	dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, cancelButtonType);
	dialog.getDialogPane().setContent(box);

	dialog.setResultConverter(btn -> {
	    if (btn == loginButtonType) {
		return box.getSelectedItem();
	    }
	    return null;
	});

	Optional<Item> result = dialog.showAndWait();
	return result.isPresent() ? result.get() : null;
    }
}
