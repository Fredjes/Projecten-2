package domain.controllers;

import javafx.scene.Node;

/**
 *
 * @author Frederik
 * @param <E>
 */
public interface BaseController<E extends Node> {

    public abstract void updateToAuthenticatedUser(E root);
}
