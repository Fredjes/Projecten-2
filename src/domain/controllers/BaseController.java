package domain.controllers;

import gui.ScreenSwitcher;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 *
 * @author Frederik
 * @param <E>
 */
public abstract class BaseController<E extends Node> {

    private final E view;
    private ScreenSwitcher switcher;

    public BaseController(E view, ScreenSwitcher sw) {
	this.view = view;
	this.switcher = sw;
    }

    public E getView() {
	return view;
    }

    public ScreenSwitcher getSwitcher() {
	return switcher;
    }

    public abstract void updateToAuthenticatedUser();

    public void removeNode(Pane p, Node node) {

	if (!p.getChildren().isEmpty()) {
	    for (Node pane : p.getChildren()) {
		if (pane instanceof Pane) {
		    removeNode((Pane) pane, node);
		}
	    }
	}

	p.getChildren().remove(node);
    }

    public void hideNode(Node node) {
	node.setVisible(false);
    }

    public void showNode(Node node) {
	node.setVisible(true);
    }
}
