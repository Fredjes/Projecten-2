package gui.dialogs;

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
}
