package gui;

import com.sun.javafx.scene.control.skin.ListViewSkin;
import javafx.scene.control.ListView;

/**
 *
 * @author Frederik
 */
public class PerformantListViewSkin<E> extends ListViewSkin<E> {

    public PerformantListViewSkin(ListView<E> listView) {
	super(listView);
    }

    public void refresh() {
	super.flow.recreateCells();
    }
}
