package domain.controllers;

import gui.ScreenSwitcher;
import java.util.HashMap;
import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import persistence.UserRepository;

/**
 * An abstract Controller (the MVC kind), that gives a default implementation
 * for hiding and showing nodes based on the currently logged in user.
 *
 * @author Frederik De Smedt
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
		FadeTransition transition = new FadeTransition(Duration.seconds(0.5), node);
		transition.setToValue(0);
		transition.play();
		transition.setOnFinished(e -> pane.getChildren().remove(node));
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
		node.setOpacity(0);
		FadeTransition transition = new FadeTransition(Duration.seconds(0.5), node);
		transition.toValueProperty().set(1);
		Pane pane = (Pane) parentNode;
		pane.getChildren().add(node);
		transition.play();
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
