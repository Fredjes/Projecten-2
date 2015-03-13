package domain.controllers;

import gui.ScreenSwitcher;
import java.util.HashMap;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import persistence.UserRepository;

/**
 *
 * @author Frederik
 * @param <E>
 */
public abstract class BaseController<E extends Node> {

    private final E view;
    private ScreenSwitcher switcher;

    private HashMap<String, Node> nodeCache = new HashMap<>();
    private HashMap<Node, Parent> nodeMapping = new HashMap<>();

    public BaseController(E view, ScreenSwitcher sw) {
	this.view = view;
	this.switcher = sw;
	UserRepository.getInstance().authenticatedUserProperty().addListener(o -> {
	    updateToAuthenticatedUser();
	    if (sw != null) {
		sw.loadIcons(view);
	    }
	});
	updateToAuthenticatedUser();
    }

    public E getView() {
	return view;
    }

    public ScreenSwitcher getSwitcher() {
	return switcher;
    }

    public abstract void updateToAuthenticatedUser();

    private void removeNode(Node node, String identifier) {
	Parent parentNode = node.getParent();

	if (parentNode == null) {
	    return;
	}

	if (parentNode instanceof Pane) {
	    Pane pane = (Pane) parentNode;
	    if (!pane.getChildren().isEmpty()) {
		nodeCache.put(identifier, node);
		nodeMapping.put(node, parentNode);
		pane.getChildren().remove(node);
	    }
	} else {

	    System.err.println("Parent is not a pane --> " + parentNode.getClass().getSimpleName());
	}

    }

    private void restoreNode(String identifier) {
	if (nodeCache.containsKey(identifier)) {
	    Node node = nodeCache.get(identifier);
	    Parent parentNode = nodeMapping.get(node);
	    if (parentNode == null) {
		System.err.println("Could not restore node " + node + " --> " + identifier);
	    }
	    if (parentNode instanceof Pane) {
		Pane pane = (Pane) parentNode;
		pane.getChildren().add(node);
		nodeCache.remove(identifier);
		nodeMapping.remove(node);
	    }
	}
    }

    public void hideNode(Node node) {
	node.setVisible(false);
    }

    public void hideNode(Node node, String identifier) {
	removeNode(node, identifier);
    }

    public void showNode(Node node) {
	node.setVisible(true);
    }

    public void showNode(String identifier) {
	restoreNode(identifier);
    }
}
