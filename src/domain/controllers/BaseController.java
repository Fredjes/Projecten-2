package domain.controllers;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 *
 * @author Frederik
 * @param <E>
 */
public abstract class BaseController<E extends Node> {

    private final E view;

    public BaseController(E view) {
	this.view = view;
    }

    public E getView() {
	return view;
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
